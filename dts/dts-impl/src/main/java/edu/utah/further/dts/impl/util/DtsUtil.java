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
package edu.utah.further.dts.impl.util;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSet.DEFAULT_PROPERTIES;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.association.Synonym;
import com.apelon.dts.client.attribute.DTSProperty;
import com.apelon.dts.client.concept.ConceptAttributeSetDescriptor;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.concept.DTSSearchOptions;
import com.apelon.dts.client.concept.OntylogConcept;
import com.apelon.dts.client.namespace.NamespaceType;
import com.apelon.dts.tqlutil.TQL.StatementType;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.DtsProperty;
import edu.utah.further.dts.api.domain.concept.DtsPropertyImpl;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsNamespaceType;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.impl.domain.DtsConceptImpl;
import edu.utah.further.dts.impl.domain.RawConceptImpl;
import edu.utah.further.dts.impl.domain.association.DtsAssociationImpl;
import edu.utah.further.dts.impl.domain.association.DtsSynonymImpl;

/**
 * Terminology service utilities.
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
 * @version Mar 23, 2009
 */
@Utility
public final class DtsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DtsUtil.class);

	/**
	 * Search limit when a unique result is expected.
	 */
	public static final int SEARCH_LIMIT_UNIQUE_RESULT = 1;

	/**
	 * Cached instance used by {@link toConceptAttributeSetDescriptorr(DtsAttributeSet)}.
	 */
	private static final ConceptAttributeSetDescriptor DAS_PROPERTIES;

	/**
	 * Caches the Apelon-to-our namespace type mapping.
	 */
	private static final Map<NamespaceType, DtsNamespaceType> toOurNamespaceType = CollectionUtil
			.newMap();

	/**
	 * Used to statically obtain and cache the list of non-static {@link StatementType}
	 * non-read-only types in TQL 3.0.0 pre-release.
	 */
	private static Set<StatementType> NON_READ_ONLY_STATEMENT_TYPES = CollectionUtil
			.newSet();

	static
	{
		// Initialize the Apelon-to-ours DTS Namespace type mapping
		synchronized (DtsUtil.class)
		{
			DAS_PROPERTIES = new ConceptAttributeSetDescriptor("All properties");
			DAS_PROPERTIES.setAllPropertyTypes(true);

			for (final DtsNamespaceType ours : DtsNamespaceType.values())
			{
				toOurNamespaceType.put(toApelonNamespaceType(ours), ours);
			}
		}

		// Initialize TQL non-read-only types
		for (final StatementType type : StatementType.values())
		{
			if (!type.isReadonly())
			{
				NON_READ_ONLY_STATEMENT_TYPES.add(type);
			}
		}
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private DtsUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Convert an attribute set descriptor from our API to Apelon API.
	 * 
	 * @param das
	 *            our API's attribute set descriptor
	 * @return corresponding Apelon APIOS attribute set descriptor
	 */
	public static NamespaceType toApelonNamespaceType(final DtsNamespaceType namespaceType)
	{
		switch (namespaceType)
		{
			case CONNECTION:
			{
				return NamespaceType.CONNECTION;
			}

			case ONTYLOG:
			{
				return NamespaceType.ONTYLOG;
			}

			case ONTYLOG_EXTENSION:
			{
				return NamespaceType.ONTYLOG_EXTENSION;
			}

			case THESAURUS:
			{
				return NamespaceType.THESAURUS;
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized namespace type: "
						+ namespaceType);
			}
		}
	}

	/**
	 * Convert an attribute set descriptor from Apelon API to our API.
	 * 
	 * @param das
	 *            our API's attribute set descriptor
	 * @return corresponding Apelon APIOS attribute set descriptor
	 */
	public static DtsNamespaceType toOurNamespaceType(
			final NamespaceType apelonNamespaceType)
	{
		return toOurNamespaceType.get(apelonNamespaceType);
	}

	/**
	 * Convert an attribute set descriptor from our API to Apelon API.
	 * 
	 * @param das
	 *            our API's attribute set descriptor
	 * @return corresponding Apelon APIOS attribute set descriptor
	 */
	public static ConceptAttributeSetDescriptor toConceptAttributeSetDescriptor(
			final DtsAttributeSet das)
	{
		switch (das.getType())
		{
			case ALL_ATTRIBUTES:
			{
				return ConceptAttributeSetDescriptor.ALL_ATTRIBUTES;
			}

			case NO_ATTRIBUTES:
			{
				return ConceptAttributeSetDescriptor.NO_ATTRIBUTES;
			}

			case PROPERTIES:
			{
				return DAS_PROPERTIES;
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized DtsAttributeSet: "
						+ das);
			}
		}
	}

	/**
	 * Return a search options object for returning a concept's properties only.
	 * 
	 * @return a search options object for returning a concept's properties only
	 */
	public static DTSSearchOptions getSearchOptionsProperties()
	{
		final DTSSearchOptions searchOptions = new DTSSearchOptions();
		searchOptions
				.setAttributeSetDescriptor(toConceptAttributeSetDescriptor(DEFAULT_PROPERTIES));
		return searchOptions;
	}

	/**
	 * Return a search options object for returning a concept's properties only, searching
	 * in a single namespace only.
	 * 
	 * @param namespace
	 *            namespace to search in
	 * 
	 * @return a search options object for returning a concept's properties only
	 */
	public static DTSSearchOptions getSearchOptionsProperties(final DtsNamespace namespace)
	{
		return getSearchOptionsProperties(namespace.getId());
	}

	/**
	 * Return a search options object for returning a concept's properties only, searching
	 * in a single namespace only.
	 * 
	 * @param namespaceId
	 *            ID of namespace to search in
	 * 
	 * @return a search options object for returning a concept's properties only
	 */
	public static DTSSearchOptions getSearchOptionsProperties(final int namespaceId)
	{
		final DTSSearchOptions searchOptions = getSearchOptionsProperties();
		searchOptions.setNamespaceId(namespaceId);
		return searchOptions;
	}

	/**
	 * Return a search options object for returning a concept with the given attribute set
	 * type, searching a single namespace only.
	 * 
	 * @param namespaceId
	 *            ID of the namespace to search in
	 * @param attrSetType
	 *            the set of options to return
	 * @return a search options object for returning a concept with the given attribute
	 *         set type.
	 */
	public static DTSSearchOptions getSearchOptionsProperties(final int namespaceId,
			final DtsAttributeSetType attrSetType)
	{
		final DTSSearchOptions searchOptions = getSearchOptionsProperties(namespaceId);
		searchOptions
				.setAttributeSetDescriptor(toConceptAttributeSetDescriptor(new DtsAttributeSet(
						attrSetType)));
		return searchOptions;
	}

	/**
	 * Return a search options object for returning a concept's properties only, assuming
	 * that the result set of a query is unique.
	 * 
	 * @return a search options object for returning a concept's properties only, assuming
	 *         that the result set of a query is unique
	 */
	public static DTSSearchOptions getSearchOptionsUniqueResultProperties()
	{
		final DTSSearchOptions searchOptions = getSearchOptionsProperties();
		searchOptions.setLimit(SEARCH_LIMIT_UNIQUE_RESULT);

		// Leave searchOptions.namespaceId unset; we should search in all FURTHeR
		// standard namespaces, e.g. SNOWMD, LOINC, etc., not in a particular
		// namespace.

		return searchOptions;
	}

	/**
	 * @param namespaceName
	 * @param conceptName
	 * @return
	 */
	public static String getTranslateChildrenTqlQuery(final String namespaceName,
			final String conceptName)
	{
		return PreparedTqlQuery.TRANSLATE_CHILDREN.evaluate(namespaceName, conceptName);
	}

	/**
	 * Convert an Apelon concept array to our A * cept list.
	 * 
	 * @param concepts
	 *            an Apelon concept array
	 * @return corresponding list our API concept
	 */
	public static List<DtsConcept> getOntylogConceptArrayAsToList(
			final DTSConcept[] concepts)
	{
		final List<DtsConcept> children = newList();
		for (final DTSConcept concept : concepts)
		{
			final DtsConcept adapter = newDtsConcept(concept);
			children.add(adapter);
		}
		return children;
	}

	/**
	 * A factory method for {@link DtsConcept}s.
	 * 
	 * @param concept
	 *            Apelon concept or ontylog concept
	 * @return the appropriate FURTHeR DTS API adapter
	 */
	public static DtsConcept newDtsConcept(final DTSConcept concept)
	{
		return ReflectionUtil.instanceOf(concept, OntylogConcept.class) ? new DtsConceptImpl(
				(OntylogConcept) concept) : new RawConceptImpl(concept);
	}

	/**
	 * A factory method for {@link DtsConcept}s
	 * 
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 */
	public static DtsConceptId newDtsConceptId(final String namespace,
			final String propertyName, final String propertyValue)
	{
		return new DtsConceptId(((namespace == null) ? " " : namespace), propertyName,
				propertyValue);
	}

	/**
	 * Convert an Apelon ontology concept array to our * ncept list.
	 * 
	 * @param concepts
	 *            an Apelon ontology concept array
	 * @return corresponding list our API concept
	 */
	public static List<DtsConcept> getOntylogConceptArrayAsToList(
			final OntylogConcept[] concepts)
	{
		final List<DtsConcept> children = newList();
		for (final OntylogConcept concept : concepts)
		{
			children.add(new DtsConceptImpl(concept));
		}
		return children;
	}

	/**
	 * Convert an Apelon ontology concept list to our * oncept list.
	 * 
	 * @param concepts
	 *            an Apelon ontology concept list
	 * @return corresponding list our API concept
	 */
	public static List<DtsConcept> getOntylogConceptListAsToList(
			final List<? extends OntylogConcept> concepts)
	{
		final List<DtsConcept> children = newList();
		for (final OntylogConcept concept : concepts)
		{
			children.add(new DtsConceptImpl(concept));
		}
		return children;
	}

	/**
	 * Convert an array of Apelon properties into a list of pro * s in our API.
	 * 
	 * @param propertyArray
	 *            an array of Apelon properties
	 * @return a map of properties in our API, keyed and sorted by property name
	 */
	public static Map<String, List<DtsProperty>> toDtsPropertyMap(
			final DTSProperty[] propertyArray)
	{
		final Map<String, List<DtsProperty>> propertySet = newMap();
		for (final DTSProperty property : propertyArray)
		{
			final String propertyName = property.getName();
			if (propertySet.get(propertyName) == null) {
				propertySet.put(propertyName, CollectionUtil.<DtsProperty>newList());
			}
			propertySet.get(propertyName).add(
					new DtsPropertyImpl(propertyName, property.getValue()));
		}
		return propertySet;
	}

	/**
	 * Convert an Apelon synonym array to our API synonym list.
	 * 
	 * @param synonyms
	 *            Apelon synonym array
	 * @return corresponding list in our API
	 */
	public static List<DtsSynonym> getAsDtsSynonymList(final Synonym[] synonyms)
	{
		final List<DtsSynonym> children = newList();
		for (final Synonym synonum : synonyms)
		{
			children.add(new DtsSynonymImpl(synonum));
		}
		return children;
	}

	/**
	 * Convert an Apelon concept association array to our API synonym list.
	 * 
	 * @param associations
	 *            Apelon concept association
	 * @return corresponding list in our API
	 */
	public static List<DtsAssociation> getAsDtsAssociationList(
			final ConceptAssociation[] associations)
	{
		final List<DtsAssociation> children = newList();
		for (final ConceptAssociation association : associations)
		{
			children.add(new DtsAssociationImpl(association));
		}
		return children;
	}

	/**
	 * Is a TQL query a read-only query or not. Uses simply regular expressions to match
	 * {@link TqlType}s in the query string.
	 * 
	 * @param tql
	 *            query string
	 * @return is query a read-only query
	 */
	public static boolean isReadOnly(final String tql)
	{
		// Simple case-insensitive match of non-read-only TQL keywords
		final String lcTql = tql.toLowerCase();
		for (final StatementType nonReadOnlyType : NON_READ_ONLY_STATEMENT_TYPES)
		{
			if (lcTql.contains(nonReadOnlyType.name().toLowerCase()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Wrap Apelon API exceptions with our own, if needed.
	 * 
	 * @param throwExceptionOnFailure
	 *            if <code>false</code>, throw no exception
	 * @param e
	 *            original exception; thrown as an {@link ApplicationException} if it is a
	 *            {@link DTSException} or as is, if it is an {@link ApplicationException}.
	 *            Otherwise this method has no effect
	 */
	public static void wrapAndThrowApelonException(final boolean throwExceptionOnFailure,
			final Throwable e)
	{
		if (throwExceptionOnFailure)
		{
			if (instanceOf(e, ApplicationException.class))
			{
				throw (ApplicationException) e;
			}
			else if (instanceOf(e, DTSException.class))
			{
				throw new ApplicationException(ErrorCode.DTS_ERROR,
						"Apelon DTS failure: " + e.getClass() + ": " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Return a temporary TQL output file handle.
	 * 
	 * @return file handle
	 * @throws IOException
	 *             if file creation failed
	 */
	public static File getTqlTempOutputFile() throws IOException
	{
		// Create a temp file for TQL output
		final File temp = File.createTempFile("tql"
				+ UUID.randomUUID().toString().substring(0, 8), ".out");
		// Delete temp file when program exits.
		temp.deleteOnExit();
		return temp;
	}

	// ========================= PRIVATE METHODS ===================================
}
