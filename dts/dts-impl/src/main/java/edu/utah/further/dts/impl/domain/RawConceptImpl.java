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
package edu.utah.further.dts.impl.domain;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.CONCEPT;
import static edu.utah.further.dts.impl.util.DtsUtil.getAsDtsAssociationList;
import static edu.utah.further.dts.impl.util.DtsUtil.getAsDtsSynonymList;
import static edu.utah.further.dts.impl.util.DtsUtil.toDtsPropertyMap;

import java.util.List;
import java.util.Map;

import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.association.Synonym;
import com.apelon.dts.client.attribute.DTSProperty;
import com.apelon.dts.client.attribute.PropertiedObject;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.match.MatchItemType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.DtsProperty;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;
import edu.utah.further.dts.api.to.DtsConceptTo;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.impl.domain.association.DtsSynonymImpl;

/**
 * A DTS concept implementation that also serves as a transfer object. Wraps an Apelon DTS
 * concept object. Cannot be marshalled and marshalled into XML using JAXB; use the
 * separate transfer object {@link DtsConceptTo}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
public class RawConceptImpl extends AbstractDtsData implements DtsConcept
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The Apelon DTS concept to be wrapped.
	 */
	private final DTSConcept concept;

	/**
	 * Concept's property map. Keyed and sorted by property name. Copied from
	 * {@link #concept} and cached.
	 */
	private Map<String, ? extends DtsProperty> properties = newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A default c-tor. Required by JAXB.
	 */
	public RawConceptImpl()
	{
		super();
		this.concept = null;
	}

	/**
	 * Wrap an Apelon concept with our API.
	 *
	 * @param concept
	 *            Apelon concept
	 */
	public RawConceptImpl(final DTSConcept concept)
	{
		this(CONCEPT, concept);
	}

	/**
	 * Wrap an Apelon concept with our API.
	 *
	 * @param type
	 *            DTS object type of the Apelon concept
	 * @param concept
	 *            Apelon concept
	 */
	protected RawConceptImpl(final DtsDataType type, final DTSConcept concept)
	{
		super(type, concept);
		this.concept = concept;

		// Copy concept fields. This way we can use them in JAXB, and also cache them for
		// faster getter access.
		this.properties = toDtsPropertyMap(concept.getProperties());
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return (concept == null) ? null : concept.toString();
	}

	// ========================= IMPLEMENTATION: DtsConcept ================

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#addConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean addConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.addConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#addInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean addInverseConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.addInverseConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#addProperty(com.apelon.dts.client.attribute.DTSProperty)
	 */
	public boolean addProperty(final DTSProperty arg0)
	{
		return concept.addProperty(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#addSynonym(com.apelon.dts.client.association.Synonym)
	 */
	public boolean addSynonym(final Synonym arg0)
	{
		return concept.addSynonym(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#containsConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean containsConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.containsConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#containsInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean containsInverseConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.containsInverseConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#containsProperty(com.apelon.dts.client.attribute.DTSProperty)
	 */
	public boolean containsProperty(final DTSProperty arg0)
	{
		return concept.containsProperty(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#containsSynonym(com.apelon.dts.client.association.Synonym)
	 */
	public boolean containsSynonym(final Synonym arg0)
	{
		return concept.containsSynonym(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#deleteConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean deleteConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.deleteConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#deleteInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	 */
	public boolean deleteInverseConceptAssociation(final ConceptAssociation arg0)
	{
		return concept.deleteInverseConceptAssociation(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#deleteProperty(com.apelon.dts.client.attribute.DTSProperty)
	 */
	public boolean deleteProperty(final DTSProperty arg0)
	{
		return concept.deleteProperty(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#deleteSynonym(com.apelon.dts.client.association.Synonym)
	 */
	public boolean deleteSynonym(final Synonym arg0)
	{
		return concept.deleteSynonym(arg0);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedConceptAssociations()
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedConceptAssociations()
	{
		return getAsDtsAssociationList(concept.getFetchedConceptAssociations());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedInverseConceptAssociations()
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedInverseConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedInverseConceptAssociations()
	{
		return getAsDtsAssociationList(concept.getFetchedInverseConceptAssociations());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedPreferredTerm()
	 */
	@Override
	public DtsSynonym getFetchedPreferredTerm()
	{
		return new DtsSynonymImpl(concept.getFetchedPreferredTerm());
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSynonyms()
	 */
	@Override
	public List<DtsSynonym> getFetchedSynonyms()
	{
		return getAsDtsSynonymList(concept.getFetchedSynonyms());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getMatchItemType()
	 */
	public MatchItemType getMatchItemType()
	{
		return concept.getMatchItemType();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getMatchString()
	 */
	@Override
	public String getMatchString()
	{
		return concept.getMatchString();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getNamespaceId()
	 */
	@Override
	public int getNamespaceId()
	{
		return concept.getNamespaceId();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumberOfSpecifiedConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedConceptAssociations()
	{
		return concept.getNumberOfSpecifiedConceptAssociations();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumberOfSpecifiedInverseConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedInverseConceptAssociations()
	{
		return concept.getNumberOfSpecifiedInverseConceptAssociations();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#getNumberOfSpecifiedProperties()
	 */
	@Override
	public int getNumSpecifiedProperties()
	{
		return concept.getNumberOfSpecifiedProperties();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumberOfSpecifiedSynonyms()
	 */
	@Override
	public int getNumSpecifiedSynonyms()
	{
		return concept.getNumberOfSpecifiedSynonyms();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getProperties()
	 */
	@Override
	public Map<String, DtsProperty> getProperties()
	{
		return newMap(properties);
	}

	/**
	 * Return a property by name.
	 *
	 * @param propertyName
	 *            property name
	 * @return corresponding property of this object; if not found, returns
	 *         <code>null</code>
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public DtsProperty getProperty(final String propertyName)
	{
		return properties.get(propertyName);
	}

	/**
	 * Return a property value by name.
	 *
	 * @param propertyName
	 *            property name
	 * @return corresponding property value; if the property not found, returns
	 *         <code>null</code>
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getPropertyValue(java.lang.String)
	 */
	@Override
	public String getPropertyValue(final String propertyName)
	{
		final DtsProperty property = (propertyName == null) ? null : properties
				.get(propertyName);
		return (property == null) ? null : property.getValue();
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setCode(java.lang.String)
	 */
	@Override
	public void setCode(final String arg0)
	{
		concept.setCode(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setId(int)
	 */
	@Override
	public void setId(final int arg0)
	{
		concept.setId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setName(java.lang.String)
	 */
	@Override
	public void setName(final String arg0)
	{
		concept.setName(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setNamespaceId(int)
	 */
	@Override
	public void setNamespaceId(final int arg0)
	{
		concept.setNamespaceId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumberOfSpecifiedConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedConceptAssociations(final int arg0)
	{
		concept.setNumberOfSpecifiedConceptAssociations(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumberOfSpecifiedInverseConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedInverseConceptAssociations(final int arg0)
	{
		concept.setNumberOfSpecifiedInverseConceptAssociations(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumberOfSpecifiedSynonyms(int)
	 */
	@Override
	public void setNumSpecifiedSynonyms(final int arg0)
	{
		concept.setNumberOfSpecifiedSynonyms(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.attribute.PropertiedObject#setProperties(com.apelon.dts.client.attribute.DTSProperty[])
	 */
	public void setProperties(final DTSProperty[] arg0)
	{
		concept.setProperties(arg0);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getAsDTSPropertiedObject()
	 */
	public PropertiedObject getAsDTSPropertiedObject()
	{
		return concept;
	}

	/**
	 * @param arg0
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#addDefiningConcept(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public boolean addDefiningConcept(final DtsConcept arg0)
	{
		// Method stub
		return false;
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean containsDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage("containsDefiningConcept()"));
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean deleteDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage("deleteDefiningConcept()"));
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedDefiningConcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedDefiningConcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedHasSubs()
	 */
	@Override
	public boolean getFetchedHasSubs()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedHasSups()
	 */
	@Override
	public boolean getFetchedHasSups()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedPrimitive()
	 */
	@Override
	public boolean getFetchedPrimitive()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSubconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSubconcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSuperconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSuperconcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedInverseRoles()
	 */
	@Override
	public int getNumSpecifiedInverseRoles()
	{
		// Method stub
		return 0;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedRoles()
	 */
	@Override
	public int getNumSpecifiedRoles()
	{
		// Method stub
		return 0;
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedHasSubs(boolean)
	 */
	@Override
	public void setFetchedHasSubs(final boolean arg0)
	{
		// Method stub
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedHasSups(boolean)
	 */
	@Override
	public void setFetchedHasSups(final boolean arg0)
	{
		// Method stub
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedPrimitive(boolean)
	 */
	@Override
	public void setFetchedPrimitive(final boolean arg0)
	{
		// Method stub
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSubconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSubconcepts(final List<? extends DtsConcept> arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage("setFetchedSubconcepts()"));
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSuperconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSuperconcepts(final List<? extends DtsConcept> arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage("setFetchedSuperconcepts()"));
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedInverseRoles(int)
	 */
	@Override
	public void setNumSpecifiedInverseRoles(final int arg0)
	{
		// Method stub
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedRoles(int)
	 */
	@Override
	public void setNumSpecifiedRoles(final int arg0)
	{
		// Method stub
	}

	/**
	 * Return a unique identifier view of the concept.
	 *
	 * @return a unique identifier object corresponding to this object
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsUniqueId()
	 */
	@Override
	public DtsConceptUniqueId getAsUniqueId()
	{
		return new DtsConceptUniqueId(getNamespaceId(), getId());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsDTSConcept()
	 */
	public DTSConcept getAsDTSConcept()
	{
		return concept;
	}
}
