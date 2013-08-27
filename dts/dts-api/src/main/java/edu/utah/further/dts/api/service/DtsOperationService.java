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
package edu.utah.further.dts.api.service;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.TranslationResult;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.util.SearchBy;

/**
 * Executes DTS business operations. All methods are allowed to throw a
 * {@link ApplicationException} upon failure to execute any DTS-related operation, with an
 * appropriate {@link ErrorCode}.
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
 * @version Nov 6, 2008
 */
@Api
public interface DtsOperationService
{
	// ========================= CONSTANTS =================================

	/**
	 * Names of useful properties we search for.
	 */
	public abstract class PropertyName
	{
		/**
		 * Name of code-in-source property.
		 */
		public static final String CODE_IN_SOURCE = "Code in Source";

		/**
		 * Name of local code property.
		 */
		public static final String LOCAL_CODE = "Local Code";
	}

	/**
	 * FURTHeR namespace name.
	 */
	String FURTHER_NAMESPACE_NAME = "Further";

	// ========================= METHODS: SEARCH ===========================

	/**
	 * Return the list of namespaces. If no namespaces are found, returns an empty list.
	 * 
	 * @return the list of namespaces
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	List<DtsNamespace> getNamespaceList();

	/**
	 * Return a namespace by id.
	 * 
	 * @param id
	 *            namespace ID
	 * @return the namespace
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	DtsNamespace findNamespaceById(int id);
	
	/**
	 * Return a namespace name by id.
	 * 
	 * @param id
	 *            namespace ID
	 * @return the namespace name
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	String findNamespaceNameById(int id);

	/**
	 * Return a namespace by name.
	 * 
	 * @param name
	 *            namespace name
	 * @return the namespace
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	DtsNamespace findNamespaceByName(String namespace);

	/**
	 * Return the base-to-extension namespace ID map.
	 * 
	 * @return the base-to-extension namespace ID map
	 */
	Map<Integer, Integer> getNamespaceBaseToExtension();

	/**
	 * Return a concept by namespace ID and name.
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	DtsConcept findConceptByName(int namespaceId, String conceptName);

	/**
	 * Return a concept by namespace ID and name.
	 * 
	 * @param namespace
	 *            namespace
	 * @param conceptName
	 *            concept name
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	DtsConcept findConceptByName(DtsNamespace namespaceId, String conceptName);

	/**
	 * Return a list of concepts in a namespace that start with a certain string.
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name prefix
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	List<DtsConcept> findConceptsByName(int namespaceId, String conceptName);

	/**
	 * Return a list of concepts in a namespace that start with a certain string, and
	 * whether to retrieve all synonyms
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name prefix
	 * @param searchBy
	 *            search by type
	 * @param synonyms
	 *            synonyms
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 */
	List<DtsConcept> findConceptsByName(int namespaceId, String conceptName,
			SearchBy searchBy, boolean synonyms);

	/**
	 * Return a unique concept by namespace ID, property name and property value.
	 * 
	 * @param namespaceId
	 *            namespace ID
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @param options
	 *            search options
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByProperty(int namespaceId, String propertyName,
			String propertyValue, DtsOptions options);

	/**
	 * Return a unique concept by namespace, property name and property value.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @param options
	 *            search options
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByProperty(DtsNamespace namespace, String propertyName,
			String propertyValue, DtsOptions options);

	/**
	 * Return a list of concepts by namespace ID, property name and property value.
	 * 
	 * @param namespaceId
	 *            namespace ID
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @param options
	 *            search options
	 * @return the matching concepts
	 * @throws ApplicationException
	 *             if a DTS error occurs or no concept is found.
	 */
	List<DtsConcept> findConceptsByProperty(int namespaceId, String propertyName,
			String propertyValue, DtsOptions options);

	/**
	 * Return a list of concepts by namespace ID, property name and property value.
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @param options
	 *            search options
	 * @return the matching concepts
	 * @throws ApplicationException
	 *             if a DTS error occurs or no concept is found.
	 */
	List<DtsConcept> findConceptsByProperty(DtsNamespace namespace, String propertyName,
			String propertyValue, DtsOptions options);

	/**
	 * Return a unique concept by concept ID.
	 * 
	 * @param conceptId
	 *            ID that includes namespace, property name, property value
	 * @param options
	 *            search options
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptById(DtsConceptId conceptId, DtsOptions options);

	/**
	 * Return a unique concept by unique identifier.
	 * 
	 * @param uniqueId
	 *            ID that includes namespace ID and concept ID
	 * @param options
	 *            search options
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByUniqueId(DtsConceptUniqueId uniqueId, DtsOptions options);

	/**
	 * Return a unique concept by namespace and "Code in Source" property.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyValue
	 *            concept's code in source
	 * @return the unique matching concept
	 * @param options
	 *            search options
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByCodeInSource(DtsNamespace namespace, String propertyValue,
			DtsOptions options);

	/**
	 * Return a concepts by namespace and "Code in Source" property. No attributes are
	 * retrieved with the concept.
	 * 
	 * @param namespace
	 *            namespace
	 * @param conceptCodeInSource
	 *            concept's code in source
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByCodeInSource(DtsNamespace namespace,
			String conceptCodeInSource);

	/**
	 * Return a concepts by namespace and "Local Code" property.
	 * 
	 * @param namespace
	 *            namespace
	 * @param localCode
	 *            concept's local code
	 * @param options
	 *            search options
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByLocalCode(DtsNamespace namespace, String localCode,
			DtsOptions options);

	/**
	 * Return a concepts by namespace and "Local Code" property. No attributes are
	 * retrieved with the concept.
	 * 
	 * @param namespace
	 *            namespace
	 * @param conceptCodeInSource
	 *            concept's local code
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 */
	DtsConcept findConceptByLocalCode(DtsNamespace namespace, String localCode);

	// /**
	// * Return a concept's associated concepts or inverse-associated concepts).
	// *
	// * @param concept
	// * a fully loaded concept
	// * @param associationType
	// * association criteria to match
	// * @param inverse
	// * if <code>true</code>, returns the inverse associations, otherwise
	// * returns the associations
	// * @return list of associated concepts of <code>concept</code>
	// */
	// List<DtsConcept> getAssociatedConcepts(DtsConcept concept,
	// AssociationType associationType, boolean inverse);

	// ========================= METHODS: NAVIGATE =========================

	/**
	 * Return the list of children of a DTS object (namespace or concept). A facade that
	 * retrieves the children of any type of DTS object. If <code>parent</code> is a
	 * namespace, it retrieves the list of root children. Otherwise, it retrieves the
	 * concept children of <code>parent</code>.
	 * <p>
	 * If this is a concept in a base namespace that has an extension, children from both
	 * the base and the extension will be returned.
	 * 
	 * @param parent
	 *            parent DTS object
	 * @return list of children of <code>concept</code>
	 * @throws ApplicationException
	 */
	List<DtsConcept> getChildren(DtsData parent);

	/**
	 * Does this concept have children or not.
	 * 
	 * @param parent
	 *            parent DTS object
	 * @return <code>true</code> if and only if the concept has children
	 * @throws ApplicationException
	 */
	boolean isHasChildren(DtsData parent);

	/**
	 * Return the list of children of a concept.
	 * 
	 * @param boolean lookInAssociations if true, looks for children in Parent-Of
	 *        associations of this parent if they are not find in the children context
	 * @return list of concept children
	 * @throws ApplicationException
	 */
	List<DtsConcept> getChildren(DtsConceptId conceptId);

	// ========================= METHODS: TRANSLATE ========================

	/**
	 * Translate a concept in a source namespace to one or more <strong>directly or
	 * inversely</strong> associated concepts in a target namespace.
	 * <p>
	 * If <code>options.targetNamespace</code> is <code>null</code>, assumes that there is
	 * only such target namespace and finds it. Otherwise, attempts to translate to the
	 * specified <code>options.targetNamespace</code>.
	 * <p>
	 * If <code>options.standardSourceNamespace</code> is <code>true</code>,
	 * <code>conceptId</code>'s namespace is assumed to be in a FURTHeR namespace, and we
	 * look for translations in a non-FURTHeR target namespace; if it is
	 * <code>false</code>,<code>conceptId</code>'s namespace is assumed to be a
	 * non-FURTHeR namespace, and we look for translations in target namespaces that are
	 * FURTHeR standard. If it is <code>null</code>, tries both <code>true</code> and
	 * <code>false</code> and returns the target concept if it is found using one of the
	 * these vaules.
	 * 
	 * @param sourceConceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @param options
	 *            concept translation options bean
	 * @return a bean holding the target concepts and direction; the target concepts
	 *         property is <code>null</code> if not found using both directions and then
	 *         the direction property value is meaningless
	 * @throws ApplicationException
	 *             if source concept is not found
	 */
	TranslationResult translateConcept(DtsConceptId sourceConceptId, DtsOptions options);

	/**
	 * Translate the children of a concept to their matches in a different namespace.
	 * 
	 * @param conceptId
	 *            parent concept in the source namespace
	 * @param targetNs
	 *            "local namespace" - the target namespace we translate
	 *            <code>parent</code>'s children to
	 * @return a map of localNs-concept-to-parent-child-concept. Includes all concepts in
	 *         localNs that have a "HasMatch" association to a child concept of
	 *         <code>parent</code> all "Has
	 * @throws ApplicationException
	 *             upon DTS call failure
	 */
	Map<DtsConcept, DtsConcept> translateChildren(DtsConceptId conceptId, String targetNs);

	/**
	 * Return the preferred name synonym of a concept.
	 * 
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @param options
	 *            throw an exception upon failure to find a concept if
	 *            <code>options.throwExceptionOnFailure</code> is <code>true</code>,
	 *            otherwise gracefully return a <code>null</code>
	 * @return concept's preferred name synonym
	 */
	String getConceptPreferredName(DtsConceptId conceptId, DtsOptions options);

	// ========================= METHODS: UTILITY ==========================

	/**
	 * Execute a TQL query. Only read-only queries are allowed.
	 * 
	 * @param tqlQuery
	 *            TQL query string
	 * @param options
	 *            options containing the set the attributes to be returned on the concepts
	 * @return a TQL result set = list of DTS concepts, with the attributes specified by
	 *         <code>das</code>
	 */
	List<DtsConcept> runTql(String tqlQuery, DtsOptions options);

	/**
	 * Execute a TQL query with a custom export format.
	 * 
	 * @param tqlQuery
	 *            TQL query string
	 * @param options
	 *            options containing the set the attributes to be returned on the concepts
	 * @return TQL result set, delimited from the output TQL file. Each element represents
	 *         the list of items in one row in the file
	 */
	List<List<String>> runTqlExport(String tqlQuery, DtsOptions options);

	/**
	 * Returns a list of the first parents found for a given concept. Does not support
	 * concepts with multiple parents. Does not include the concept Namespace.
	 * 
	 * This fetches {@link DtsAttributeSet.ALL_ATTRIBUTES} of a concept.
	 * 
	 * @param concept
	 *            The concept for which to get the parents
	 * @return a List of parents starting with the direct parent and ending with the
	 *         namespace
	 */
	List<DtsConcept> getFirstParents(DtsConcept concept);

	/**
	 * Returns a list of the first parents found for a given concept. Does not support
	 * concepts with multiple parents.
	 * 
	 * This fetches {@link DtsAttributeSet.ALL_ATTRIBUTES} of a concept
	 * 
	 * @param concept
	 *            The concept for which to get the parents
	 * @param includeNamespace
	 *            Whether to include the concept namespace as a parent
	 * @return a List of parents starting with the direct parent and ending with the
	 *         namespace
	 */
	List<DtsConcept> getFirstParents(DtsConcept concept, boolean includeNamespace);

	/**
	 * Print out DTS namespace information in a table.
	 * 
	 * @return a string with DTS namespace information
	 */
	String printNamespaceInfo();

	/**
	 * Return the maxResultSize property (maximum number of concepts to retrieve in search
	 * queries)
	 * 
	 * @return the maxResultSize property (maximum number of concepts to retrieve in
	 *         search queries)
	 */
	int getMaxResultSize();

	/**
	 * Set a new value for the maxResultSize property (maximum number of concepts to
	 * retrieve in search queries).
	 * 
	 * @param maxResultSize
	 *            the maxResultSize to set
	 */
	void setMaxResultSize(int maxResultSize);

	/**
	 * Return the maxTraversals property (maximum number of parents or children to search
	 * for parent or child concepts.
	 * 
	 * @return maxTraversals property (maximum number of parents or children to search for
	 *         parent or child concepts.
	 */
	int getMaxTraversals();

	/**
	 * Set a new value for the maxTraversals property (maximum number of parents or
	 * children to search for parent or child concepts.
	 * 
	 * @param maxTraversals
	 *            the maxTraversals to set
	 */
	void setMaxTraversals(int maxTraversals);

	/**
	 * Returns whether a concept has a parent with the given name.
	 * 
	 * @param concept
	 * @param superConceptName
	 * @param associationTypeName
	 *            TODO
	 * @return true if the given concept has a parent with the given name.
	 */
	boolean hasSuperConcept(DtsConcept concept, String superConceptName,
			String associationTypeName);

}
