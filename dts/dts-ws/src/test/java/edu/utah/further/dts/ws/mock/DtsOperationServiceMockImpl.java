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
package edu.utah.further.dts.ws.mock;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.CONCEPT;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.NAMESPACE;
import static edu.utah.further.dts.api.util.DtsNames.NONE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.apelon.dts.client.association.AssociationType;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.util.math.random.RandomWordGenerationService;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.DtsTypeFactory;
import edu.utah.further.dts.api.domain.concept.TranslationResult;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.util.SearchBy;

/**
 * A mock implementation of the DTS data service. Useful for unit testing, or when DTS
 * connection is slow or unavailable.
 * <p>
 * Right now this implementation returns randomly generated concept objects. In this
 * future, this implementation may use a hard-coded tree of concepts. Useful for unit
 * testing, or when DTS connection is slow or unavailable.
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
@Mock
public final class DtsOperationServiceMockImpl implements DtsOperationService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DtsOperationServiceMockImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Number of namespaces in a namespace list, children in a children list.
	 */
	private int collectionSize = 5;

	/**
	 * Mock concept name length.
	 */
	private int wordLength = 10;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates DTS data types.
	 */
	@Autowired
	@Qualifier("dtsTypeFactoryMockImpl")
	private DtsTypeFactory dtsTypeFactory;

	/**
	 * Generates random words.
	 */
	@Autowired
	private RandomWordGenerationService wordGenerationService;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: DtsOperationService =======

	/**
	 * Return the list of namespaces. If no namespaces are found, returns an empty list.
	 * 
	 * @return the list of namespaces
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getNamespaceList()
	 */
	@Override
	public List<DtsNamespace> getNamespaceList()
	{
		final List<DtsNamespace> namespaceList = newList();
		for (int i = 0; i < collectionSize; i++)
		{
			namespaceList.add(newNamespace());
		}
		return namespaceList;
	}

	/**
	 * Return a namespace by id.
	 * 
	 * @param id
	 *            namespace ID
	 * @return the namespace
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findNamespaceById(int)
	 */
	@Override
	public DtsNamespace findNamespaceById(final int id)
	{
		return newNamespace(wordGenerationService.generate(wordLength));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.dts.api.service.DtsOperationService#findNamespaceNameById(int)
	 */
	@Override
	public String findNamespaceNameById(final int id)
	{
		return findNamespaceById(id).getName();
	}

	/**
	 * Return a namespace by name.
	 * 
	 * @return the namespaces
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findNamespaceByName(java.lang.String)
	 */
	@Override
	public DtsNamespace findNamespaceByName(final String namespace)
	{
		return newNamespace(namespace);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getNamespaceBaseToExtension()
	 */
	@Override
	public Map<Integer, Integer> getNamespaceBaseToExtension()
	{
		return newMap();
	}

	/**
	 * @param namespaceId
	 * @param conceptName
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByName(int,
	 *      java.lang.String)
	 */
	@Override
	public DtsConcept findConceptByName(final int namespaceId, final String conceptName)
	{
		return newConcept(conceptName);
	}

	/**
	 * Return a concept by namespace and name.
	 * 
	 * @param namespace
	 *            namespace
	 * @param conceptName
	 *            concept name
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByName(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String)
	 */
	@Override
	public DtsConcept findConceptByName(final DtsNamespace namespace,
			final String conceptName)
	{
		return newConcept(conceptName);
	}

	/**
	 * @param namespaceId
	 * @param conceptName
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByName(int,
	 *      java.lang.String)
	 */
	@Override
	public List<DtsConcept> findConceptsByName(final int namespaceId,
			final String conceptName)
	{
		return CollectionUtil.<DtsConcept> newList();
	}

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
	@Override
	public List<DtsConcept> findConceptsByName(final int namespaceId,
			final String conceptName, final SearchBy searchBy, final boolean synonyms)
	{
		return CollectionUtil.<DtsConcept> newList();
	}

	/**
	 * @param namespaceId
	 * @param propertyName
	 * @param propertyValue
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByProperty(int,
	 *      java.lang.String, java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptByProperty(final int namespaceId,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return newConcept();
	}

	/**
	 * Return a unique concept by namespace, property name and property value.
	 * 
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByProperty(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String, java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptByProperty(final DtsNamespace namespace,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return newConcept();
	}

	/**
	 * @param namespaceId
	 * @param propertyName
	 * @param propertyValue
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByProperty(int,
	 *      java.lang.String, java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public List<DtsConcept> findConceptsByProperty(final int namespaceId,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return CollectionUtil.<DtsConcept> newList();
	}

	/**
	 * Return a list of concepts by namespace, property name and property value.
	 * 
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByPropertyWithQualifier(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String, java.lang.String, String, String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public List<DtsConcept> findConceptsByProperty(final DtsNamespace namespace,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return CollectionUtil.<DtsConcept> newList();
	}

	/**
	 * @param conceptId
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptById(edu.utah.further.dts.api.to.DtsConceptId,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptById(final DtsConceptId conceptId,
			final DtsOptions options)
	{
		final DtsNamespace ns = findNamespaceByName(conceptId.getNamespace());
		return findConceptByProperty(ns, conceptId.getPropertyName(),
				conceptId.getPropertyValue(), options);
	}

	/**
	 * @param uniqueId
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByUniqueId(edu.utah.further.dts.api.to.DtsConceptUniqueId,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptByUniqueId(final DtsConceptUniqueId uniqueId,
			final DtsOptions options)
	{
		return newConcept();
	}

	/**
	 * Return a unique concept by namespace and "Code in Source" property.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyValue
	 *            concept's code in source
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByCodeInSource(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String)
	 */
	// TODO: Reusing existing connection opened in findConceptByProperty() instead of
	// opening it twice, by using CGLIB Spring proxies
	@Override
	public DtsConcept findConceptByCodeInSource(final DtsNamespace namespace,
			final String propertyValue)
	{
		return propertyValue.equals(NONE) ? null : newConcept();
	}

	/**
	 * @param namespace
	 * @param propertyValue
	 * @param attributeSet
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByCodeInSource(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String, edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptByCodeInSource(final DtsNamespace namespace,
			final String propertyValue, final DtsOptions options)
	{
		return propertyValue.equals(NONE) ? null : newConcept();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.dts.api.service.DtsOperationService#findConceptByLocalCode(edu
	 * .utah.further.dts.api.domain.namespace.DtsNamespace, java.lang.String,
	 * edu.utah.further.dts.api.service.DtsOptions)
	 */
	@Override
	public DtsConcept findConceptByLocalCode(final DtsNamespace namespace,
			final String localCode, final DtsOptions options)
	{
		return localCode.equals(NONE) ? null : newConcept();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.dts.api.service.DtsOperationService#findConceptByLocalCode(edu
	 * .utah.further.dts.api.domain.namespace.DtsNamespace, java.lang.String)
	 */
	@Override
	public DtsConcept findConceptByLocalCode(final DtsNamespace namespace,
			final String localCode)
	{
		return localCode.equals(NONE) ? null : newConcept();
	}

	/**
	 * @param parent
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#isHasChildren(edu.utah.further.dts.api.domain.namespace.DtsData)
	 */
	@Override
	public boolean isHasChildren(final DtsData parent)
	{
		return !getChildren(new DtsConceptId()).isEmpty();
	}

	/**
	 * @param conceptId
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getChildren(edu.utah.further.dts.api.to.DtsConceptId)
	 */
	@Override
	public List<DtsConcept> getChildren(final DtsConceptId conceptId)
	{
		return getChildren(newConcept());
	}

	/**
	 * @param parent
	 * @param lookInAssociations
	 * @return
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getChildren(edu.utah.further.dts.api.domain.namespace.DtsData)
	 */
	@Override
	public List<DtsConcept> getChildren(final DtsData parent)
	{
		final List<DtsConcept> childList = newList();
		for (int i = 0; i < collectionSize; i++)
		{
			childList.add(newConcept());
		}
		return childList;
	}

	/**
	 * @param concept
	 * @param associationType
	 * @param inverse
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getAssociatedConcepts(edu.utah.further.dts.api.domain.concept.DtsConcept,
	 *      com.apelon.dts.client.association.AssociationType, boolean)
	 */
	public List<DtsConcept> getAssociatedConcepts(final DtsConcept concept,
			final AssociationType associationType, final boolean inverse)
	{
		final List<DtsConcept> conceptList = newList();
		for (int i = 0; i < collectionSize; i++)
		{
			conceptList.add(newConcept());
		}
		return conceptList;
	}

	/**
	 * @param sourceConceptId
	 * @param options
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#translateConcept(edu.utah.further.dts.api.to.DtsConceptId,
	 *      edu.utah.further.dts.api.service.DtsOptions)
	 */
	@Override
	public TranslationResult translateConcept(final DtsConceptId sourceConceptId,
			final DtsOptions options)
	{
		final List<DtsConcept> targetConcepts = CollectionUtil.newList();
		targetConcepts.add(newConcept(wordGenerationService.generate(wordLength)));
		return new TranslationResult(targetConcepts, Boolean.FALSE, Boolean.TRUE);
	}

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
	 * @see edu.utah.further.dts.api.service.DtsOperationService#translateChildren(edu.utah.further.dts.api.to.DtsConceptId,
	 *      java.lang.String)
	 */
	@Override
	public Map<DtsConcept, DtsConcept> translateChildren(final DtsConceptId conceptId,
			final String targetNs)
	{
		return newMap();
	}

	/**
	 * @param conceptId
	 * @param throwExceptionOnFailure
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getConceptPreferredName(edu.utah.further.dts.api.to.DtsConceptId,
	 *      boolean)
	 */
	@Override
	public String getConceptPreferredName(final DtsConceptId conceptId,
			final DtsOptions options)
	{
		return null;
	}

	/**
	 * @param tqlQuery
	 * @param das
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#runTql(java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public List<DtsConcept> runTql(final String tqlQuery, final DtsOptions options)
	{
		return newList();
	}

	/**
	 * Execute a TQL query with a custom export format.
	 * 
	 * @param tqlQuery
	 *            TQL query string
	 * @param das
	 *            defines the attributes to be returned on the concepts
	 * @return TQL result set, delimited from the output TQL file. Each element represents
	 *         the list of items in one row in the file
	 * @see edu.utah.further.dts.api.service.DtsOperationService#runTqlExport(java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public List<List<String>> runTqlExport(final String tqlQuery, final DtsOptions options)
	{
		return newList();
	}

	/**
	 * @param concept
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getFirstParents(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public List<DtsConcept> getFirstParents(final DtsConcept concept)
	{
		return getFirstParents(concept, false);
	}

	/**
	 * @param concept
	 * @param includeNamespace
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getFirstParents(edu.utah.further.dts.api.domain.concept.DtsConcept,
	 *      boolean)
	 */
	@Override
	public List<DtsConcept> getFirstParents(final DtsConcept concept,
			final boolean includeNamespace)
	{
		return newList();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the collectionSize property.
	 * 
	 * @return the collectionSize
	 */
	public int getCollectionSize()
	{
		return collectionSize;
	}

	/**
	 * Set a new value for the collectionSize property.
	 * 
	 * @param collectionSize
	 *            the collectionSize to set
	 */
	public void setCollectionSize(final int collectionSize)
	{
		this.collectionSize = collectionSize;
	}

	/**
	 * Return the wordLength property.
	 * 
	 * @return the wordLength
	 */
	public int getWordLength()
	{
		return wordLength;
	}

	/**
	 * Set a new value for the wordLength property.
	 * 
	 * @param wordLength
	 *            the wordLength to set
	 */
	public void setWordLength(final int wordLength)
	{
		this.wordLength = wordLength;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private DtsNamespace newNamespace()
	{
		final String name = wordGenerationService.generate(wordLength);
		return newNamespace(name);
	}

	/**
	 * @return
	 */
	private DtsNamespace newNamespace(final String name)
	{
		return (DtsNamespace) dtsTypeFactory.newInstance(NAMESPACE, name);
	}

	/**
	 * @return
	 */
	private DtsConcept newConcept()
	{
		final String name = wordGenerationService.generate(wordLength);
		return newConcept(name);
	}

	/**
	 * @return
	 */
	private DtsConcept newConcept(final String name)
	{
		return (DtsConcept) dtsTypeFactory.newInstance(CONCEPT, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.dts.api.service.DtsOperationService#printNamespaceInfo()
	 */
	@Override
	public String printNamespaceInfo()
	{
		return null;
	}

	/**
	 * @param maxResultSize
	 * @see edu.utah.further.dts.api.service.DtsOperationService#setMaxResultSize(int)
	 */
	@Override
	public void setMaxResultSize(final int maxResultSize)
	{
		// This setting has no effect in this mock implementation
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getMaxResultSize()
	 */
	@Override
	public int getMaxResultSize()
	{
		return 0;
	}

	@Override
	public boolean hasSuperConcept(final DtsConcept thisConcept,
			final String superConceptName, final String associationTypeName)
	{
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getMaxTraversals()
	 */
	@Override
	public int getMaxTraversals()
	{
		return 0;
	}

	/**
	 * @param maxTraversals
	 * @see edu.utah.further.dts.api.service.DtsOperationService#setMaxTraversals(int)
	 */
	@Override
	public void setMaxTraversals(final int maxTraversals)
	{
		// htis setting has no effect in this mock implementation
	}
}
