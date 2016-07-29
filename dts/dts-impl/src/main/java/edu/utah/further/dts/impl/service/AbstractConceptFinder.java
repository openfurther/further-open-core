/**
 * Copyright (C) [2013] [The FURTHeR Project]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.utah.further.dts.impl.service;

import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;
import static edu.utah.further.dts.api.service.DtsOperationService.FURTHER_NAMESPACE_NAME;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.attribute.QualifierType;
import com.apelon.dts.client.concept.ConceptAttributeSetDescriptor;
import com.apelon.dts.client.concept.DTSSearchOptions;
import com.apelon.dts.client.concept.OntylogConcept;
import com.apelon.dts.client.concept.SearchQuery;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;

/**
 * A base class for all {@link ConceptFinder}s that has useful dependencies injected into
 * it.
 * <p>
 * TODO: Use non-public factory methods instead of c-tor telescoping?
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 17, 2008
 */
@Implementation
abstract class AbstractConceptFinder implements ConceptFinder
{
	// ========================= CONSTANTS =================================
	private static final Logger log = getLogger(AbstractConceptFinder.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	protected final DtsOperationService dos;

	/**
	 * Executes basic DTS operations.
	 */
	protected final DtsObjectFactory dtsObjectFactory;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param dos
	 * @param dtsObjectFactory
	 */
	public AbstractConceptFinder(final DtsOperationService dos,
			final DtsObjectFactory dtsObjectFactory)
	{
		super();
		this.dos = dos;
		this.dtsObjectFactory = dtsObjectFactory;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the Apelon object of the FURTHeR exact match association type.
	 * <p>
	 * TODO: cache this association as it is frequently used.
	 * 
	 * @return FURTHeR exact match association type
	 * @throws ApplicationException
	 *             if a DTS I/O problem occurs
	 */
	protected final AssociationType getExactMatchAssociationType()
	{
		return getAssociationType("ExactMatch");
	}

	/**
	 * Return the Apelon object of the FURTHeR association type with the given
	 * associationType name, as provided by the method user.
	 */
	protected final AssociationType getAssociationType(final String associationTypeName)
	{
		final DtsNamespace targetNs = dos.findNamespaceByName(FURTHER_NAMESPACE_NAME);
		AssociationType associationType = null;
		try
		{
			associationType = dtsObjectFactory
					.createAssociationQuery()
					.findAssociationTypeByName(associationTypeName, targetNs.getId());
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to create assosciation query", e);
		}
		validateNotNull("association type", associationType);
		return associationType;
	}

	/**
	 * @param standardSourceNamespace
	 * @param options
	 * @param sourceConcept
	 * @param associationType
	 * @return
	 */
	protected final OntylogConcept[] findAssociatedConcepts(
			final boolean standardSourceNamespace, final DTSSearchOptions options,
			final DtsConcept sourceConcept, final AssociationType associationType,
			final int attributeSetLimit)
	{
		// Find the targetConcept associated with concept via associationType.
		// That means that targetConcept has an inverse association of type
		// associationType to concept
		ConceptAttributeSetDescriptor asd = options.getAttributeSetDescriptor();
		try
		{
			ConceptAttributeSetDescriptor casd = (ConceptAttributeSetDescriptor) asd
					.clone();
			casd.setConceptAssociationTypes(new AssociationType[]
			{ associationType });
			casd.setInverseConceptAssociationTypes(new AssociationType[]
			{ associationType });
			casd.setAllConceptAssociationTypes(false);
			casd.setAllInverseConceptAssociationTypes(false);
			log.debug("SETTING Attributes Limit to '" + attributeSetLimit + "'");
			casd.setAttributesLimit(attributeSetLimit);
			options.setAttributeSetDescriptor(casd);
		}
		catch (CloneNotSupportedException e)
		{
			log.error("Unable to update DTSSearchOptions; using original options.");
		}

		final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
		try
		{
			if (standardSourceNamespace)
			{

				return searchQuery.findConceptsWithConceptAssociationMatching(
						associationType, sourceConcept.getName(), options);
			}
			return searchQuery.findConceptsWithInverseConceptAssociationMatching(
					associationType, sourceConcept.getName(), options);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException(
					"Unable to find concept with assosciation or inverse association", e);
		}
		finally
		{
			options.setAttributeSetDescriptor(asd);
		}
	}

	/**
	 * Returns the qualifier type with the given qualifierTypeName, within the FURTHeR
	 * namespace.
	 * 
	 * @param qualifierTypeName
	 * @return
	 */
	protected final QualifierType getQualifierTypeByName(final String qualifierTypeName)
	{
		final DtsNamespace targetNs = dos.findNamespaceByName(FURTHER_NAMESPACE_NAME);
		QualifierType qualifierType = null;
		try
		{
			qualifierType = dtsObjectFactory.createSearchQuery().findQualifierTypeByName(
					qualifierTypeName, targetNs.getId());
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find qualifier type", e);
		}
		validateNotNull("qualifier type", qualifierType);
		return qualifierType;
	}

}
