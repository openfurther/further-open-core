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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.text.StringUtil.repeat;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSet.DEFAULT_ALL_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSet.DEFAULT_NO_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSet.DEFAULT_PROPERTIES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.NO_ATTRIBUTES;
import static edu.utah.further.dts.api.service.DtsOperationService.PropertyName.CODE_IN_SOURCE;
import static edu.utah.further.dts.api.service.DtsOperationService.PropertyName.LOCAL_CODE;
import static edu.utah.further.dts.impl.service.ConceptFinderType.MULTI_BY_PROPERTY;
import static edu.utah.further.dts.impl.service.ConceptFinderType.PROPERTY;
import static edu.utah.further.dts.impl.service.ConceptFinderType.TRANSLATIONS;
import static edu.utah.further.dts.impl.service.ConceptFinderType.UNIQUE_ID;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.parseTqlResultLine;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.toDtsConceptList;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.validateBusinessEntity;
import static edu.utah.further.dts.impl.util.DtsUtil.getOntylogConceptArrayAsToList;
import static edu.utah.further.dts.impl.util.DtsUtil.getSearchOptionsProperties;
import static edu.utah.further.dts.impl.util.DtsUtil.getSearchOptionsUniqueResultProperties;
import static edu.utah.further.dts.impl.util.DtsUtil.toConceptAttributeSetDescriptor;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.concept.ConceptChild;
import com.apelon.dts.client.concept.ConceptParent;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.concept.DTSSearchOptions;
import com.apelon.dts.client.concept.NavChildContext;
import com.apelon.dts.client.concept.NavParentContext;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.concept.OntylogConcept;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.tqlutil.TQL;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.exception.ApplicationError;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.cache.CachingService;
import edu.utah.further.dts.api.annotation.DtsTransactional;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.TranslationResult;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsNamespaceAdapter;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.util.SearchBy;
import edu.utah.further.dts.impl.domain.DtsConceptImpl;
import edu.utah.further.dts.impl.domain.DtsNamespaceImpl;
import edu.utah.further.dts.impl.util.DtsUtil;

/**
 * The default implementation of the DTS data service.
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
@Service("dtsOperationService")
@DtsTransactional
public class DtsOperationServiceImpl implements DtsOperationService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DtsOperationServiceImpl.class);

	/**
	 * Our cache region name - namespaces.
	 */
	private static final String CACHE_REGION_NAMESPACE = "dtsNamespace";

	// ========================= FIELDS ====================================

	/**
	 * A dual map from a base namespace ID to an extension namespace ID, if the extension
	 * exists for the base.
	 */
	private volatile Map<Integer, Integer> namespaceBaseToExtension;
	// private volatile Map<Integer, Integer> namespaceExtensionToBase;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes basic DTS operations.
	 */
	@Autowired
	private DtsObjectFactory dtsObjectFactory;

	/**
	 * Concept finder factory.
	 */
	@Autowired
	private ConceptFinderService cfs;

	/**
	 * Caches objects.
	 */
	@Autowired
	private CachingService cacheService;

	/**
	 * Maximum number of concepts to retrieve for a match search.
	 */
	private int maxResultSize = 100;

	/**
	 * Maximum number of traversals when searching for concept parents.
	 */
	private int maxTraversals = 15;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Register the default paging providers here.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		// Initialize cache regions
		cacheService.addRegion(CACHE_REGION_NAMESPACE);
	}

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
		final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
		Namespace[] namespaceArray = null;
		try
		{
			namespaceArray = nameQuery.getNamespaces();
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to get namespaces", e);
		}
		final List<DtsNamespace> namespaceList = newList();
		for (final Namespace ns : namespaceArray)
		{
			namespaceList.add(new DtsNamespaceImpl(ns));
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
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findNamespaceByName(java.lang.String)
	 */
	@Override
	public DtsNamespace findNamespaceById(final int id)
	{
		final Integer key = new Integer(id);
		final DtsNamespace cached = cacheService.getObject(CACHE_REGION_NAMESPACE, key);
//		TODO: Utah DS on Dev CSV Export sometimes breaks, apparently because of cache logic below
//		      comment out until fix can be found
//		if (cached != null)
//		{
//			return cached;
//		}
		final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
		Namespace ns;
		try
		{
			ns = nameQuery.findNamespaceById(id);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Namespace with ID " + id + " not found ");
		}
		final DtsNamespaceImpl namespace = new DtsNamespaceImpl(ns);
		cacheService.saveObject(CACHE_REGION_NAMESPACE, key, namespace);
		return namespace;
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
		final DtsNamespace namespace = findNamespaceById(id);
		if (namespace == null)
		{
			throw new ApplicationException("Invalid namespace " + id);
		}
		return namespace.getName();
	}

	/**
	 * Return a namespace by name.
	 * 
	 * @param name
	 *            namespace name
	 * @return the namespace
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findNamespaceByName(java.lang.String)
	 */
	@Override
	public DtsNamespace findNamespaceByName(final String name)
	{
		final DtsNamespace cached = cacheService.getObject(CACHE_REGION_NAMESPACE, name);
		if (cached != null)
		{
			return cached;
		}
		final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
		Namespace ns;
		try
		{
			ns = nameQuery.findNamespaceByName(name);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Namespace " + quote(name) + " not found ", e);
		}
		if (ns == null)
		{
			throw new ApplicationException("Namespace " + quote(name) + " not found ");
		}
		final DtsNamespaceImpl namespace = new DtsNamespaceImpl(ns);
		cacheService.saveObject(CACHE_REGION_NAMESPACE, name, namespace);
		return namespace;
	}

	/**
	 * Return the base-to-extension namespace ID map.
	 * 
	 * @return the base-to-extension namespace ID map
	 */
	@Override
	public Map<Integer, Integer> getNamespaceBaseToExtension()
	{
		// using the double-checked locking with volatile
		// @see http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
		if (namespaceBaseToExtension == null)
		{
			synchronized (this)
			{
				if (namespaceBaseToExtension == null)
				{
					setupNamespaceBaseToExtension();
				}
			}
		}
		return namespaceBaseToExtension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.dts.api.service.DtsOperationService#printNamespaceInfo()
	 */
	@Override
	public String printNamespaceInfo()
	{
		final StringBuilder sb = new StringBuilder(String.format("%-55s | %-20s | %-5s",
				"Namespace name", "Type", "ID"))
				.append(NEW_LINE_STRING)
				.append(String.format("%-56s+%-22s+%-6s", repeat("-", 56),
						repeat("-", 22), repeat("-", 6)))
				.append(NEW_LINE_STRING);
		for (final DtsNamespace ns : getNamespaceList())
		{
			sb.append(
					String.format("%-55s | %-20s | %-5d", ns.getName(),
							ns.getNamespaceType(), new Integer(ns.getId()))).append(
					NEW_LINE_STRING);
		}
		return sb.toString();
	}

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
	@Override
	public DtsConcept findConceptByName(final int namespaceId, final String conceptName)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Finding concept by name " + quote(conceptName)
					+ " in namespace ID " + namespaceId);
		}
		final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
		OntylogConcept concept;
		try
		{
			concept = (OntylogConcept) searchQuery.findConceptByName(conceptName,
					namespaceId, toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES));
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find concept with name "
					+ quote(conceptName), e);
		}
		return new DtsConceptImpl(concept);
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
		return findConceptByName(namespace.getId(), conceptName);
	}

	/**
	 * Find <i>multiple</i> concepts in a namespace whose name contains a specified
	 * string. Limited to a 100 results.
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByName(int,
	 *      java.lang.String)
	 */
	@Override
	public List<DtsConcept> findConceptsByName(final int namespaceId,
			final String conceptName)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Finding concept by name " + quote(conceptName)
					+ " in namespace ID " + namespaceId + " search limit "
					+ maxResultSize);
		}
		final DTSSearchOptions searchOptions = new DTSSearchOptions(maxResultSize,
				namespaceId, toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES));
		final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
		OntylogConcept[] matches;
		try
		{
			matches = searchQuery.findConceptsWithNameMatching(conceptName + "*",
					searchOptions);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find concepts with name matching "
					+ quote(conceptName), e);
		}
		return toDtsConceptList(matches);
	}

	/**
	 * Find <i>multiple</i> concepts in a namespace whose name contains a specified string
	 * and whether to retrieve all synonyms; retrieves all properties. Limited to a 100
	 * results.
	 * 
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name
	 * @param searchBy
	 *            search by type
	 * @param synonyms
	 *            synonyms
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByName(int,
	 *      java.lang.String)
	 */
	@Override
	public List<DtsConcept> findConceptsByName(final int namespaceId,
			final String conceptName, final SearchBy searchBy, final boolean synonyms)
	{
		SearchBy sbType;
		if (searchBy == null)
		{
			sbType = SearchBy.STARTS_WITH;
		}
		else
		{
			sbType = searchBy;
		}
		if (log.isDebugEnabled())
		{
			log.debug("Finding concept by name " + quote(conceptName)
					+ " in namespace ID " + namespaceId + " search limit "
					+ maxResultSize + " synonyms " + synonyms);
		}
		final DTSSearchOptions searchOptions = new DTSSearchOptions(maxResultSize,
				namespaceId, toConceptAttributeSetDescriptor(DEFAULT_PROPERTIES));
		final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
		OntylogConcept[] matches;
		try
		{
			matches = searchQuery.findConceptsWithNameMatching(sbType.pad(conceptName),
					searchOptions, synonyms);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find concept named "
					+ quote(conceptName), e);
		}
		return toDtsConceptList(matches);
	}

	/**
	 * Return a unique concept by namespace ID, property name and property value.
	 * 
	 * @param namespaceId
	 *            namespace ID
	 * @param propertyValue
	 *            property value
	 * @param propertyValue
	 *            property value
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @return the unique matching concept
	 * @throws ApplicationException
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByProperty(int,
	 *      java.lang.String, java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public DtsConcept findConceptByProperty(final int namespaceId,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		final List<DtsConcept> results = findConceptsByCriteria(cfs.get(PROPERTY),
				options, namespaceId, propertyName, propertyValue).getConcepts();
		return results != null ? results.get(0) : null;
	}

	/**
	 * Return a unique concept by namespace, property name and property value.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyValue
	 *            property value
	 * @param propertyValue
	 *            property value
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByProperty(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public DtsConcept findConceptByProperty(final DtsNamespace namespace,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return findConceptByProperty(namespace.getId(), propertyName, propertyValue,
				options);
	}

	/**
	 * Return a list of concepts by namespace ID, property name and property value.
	 * 
	 * @param namespaceId
	 *            namespace ID
	 * @param propertyValue
	 *            property value
	 * @param propertyValue
	 *            property value
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @return the unique matching concept
	 * @throws ApplicationException
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByProperty(int,
	 *      java.lang.String, java.lang.String,
	 *      edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public List<DtsConcept> findConceptsByProperty(final int namespaceId,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return findConceptsByCriteria(cfs.get(MULTI_BY_PROPERTY), options, namespaceId,
				propertyName, propertyValue).getConcepts();
	}

	/**
	 * Return a list of concepts by namespace, property name and property value.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyValue
	 *            property value
	 * @param propertyValue
	 *            property value
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @return the unique matching concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptsByProperty(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<DtsConcept> findConceptsByProperty(final DtsNamespace namespace,
			final String propertyName, final String propertyValue,
			final DtsOptions options)
	{
		return findConceptsByProperty(namespace.getId(), propertyName, propertyValue,
				options);
	}

	/**
	 * Return a unique concept by concept ID.
	 * 
	 * @param conceptId
	 *            ID that includes namespace, property name, property value
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptById(edu.utah.further.dts.api.to.DtsConceptId)
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
		final List<DtsConcept> results = findConceptsByCriteria(cfs.get(UNIQUE_ID),
				options, uniqueId.getNamespaceId(), new Integer(uniqueId.getId()))
				.getConcepts();
		return results != null ? results.get(0) : null;
	}

	/**
	 * Return a unique concept by namespace and "Code in Source" property.
	 * 
	 * @param namespace
	 *            namespace
	 * @param propertyValue
	 *            concept's code in source
	 * @return the unique matching concept
	 * @param attributeSet
	 *            attribute set to retrieve wit the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs, no concept or more than one concept is found
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findConceptByCodeInSource(edu.utah.further.dts.api.domain.namespace.DtsNamespace,
	 *      java.lang.String)
	 */
	@Override
	public DtsConcept findConceptByCodeInSource(final DtsNamespace namespace,
			final String propertyValue, final DtsOptions options)
	{
		return findConceptByProperty(namespace, CODE_IN_SOURCE, propertyValue, options);
	}

	/**
	 * Return a unique concept by namespace and "Code in Source" property. No attributes
	 * are retrieved with the concept.
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
	@Override
	public DtsConcept findConceptByCodeInSource(final DtsNamespace namespace,
			final String propertyValue)
	{
		return findConceptByCodeInSource(namespace, propertyValue,
				DtsOptions.DEFAULT_NO_ATTRIBUTES);
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
		return findConceptByProperty(namespace, LOCAL_CODE, localCode, options);
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
		return findConceptByLocalCode(namespace, localCode,
				DtsOptions.DEFAULT_NO_ATTRIBUTES);
	}

	/**
	 * Return the list of children of a DTS object (namespace or concept). A facade that
	 * retrieves the children of any type of DTS object. If <code>parent</code> is a
	 * namespace, it retrieves the list of root children. Otherwise, it retrieves the
	 * concept children of <code>parent</code>.
	 * 
	 * @param parent
	 *            parent DTS object
	 * @return list of children of <code>concept</code>
	 * @throws ApplicationException
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getChildren(edu.utah.further.dts.api.domain.namespace.DtsData)
	 */
	@Override
	public List<DtsConcept> getChildren(final DtsData parent)
	{
		switch (parent.getType())
		{
			case NAMESPACE:
			{
				return getConceptChildrenOfNamespace(parent);
			}

			case CONCEPT:
			case CONCEPT_CHILD:
			{
				final DtsConcept parentConcept = (DtsConcept) parent;
				final DtsNamespace ns = findNamespaceById(parentConcept.getNamespaceId());
				switch (ns.getNamespaceType())
				{
					case ONTYLOG:
					case ONTYLOG_EXTENSION:
					{
						return getChildrenByChildContext(parentConcept);
					}

					default:
					{
						return getChildrenByAssociation(ns, parentConcept);
					}
				}
			}

			default:
			{
				throw new UnsupportedOperationException("Unsupported parent type "
						+ parent.getType());
			}
		}
	}

	/**
	 * Does this concept have children or not.
	 * 
	 * @param parent
	 *            parent DTS object
	 * @return <code>true</code> if and only if the concept has children
	 * @throws ApplicationException
	 * @see edu.utah.further.dts.api.service.DtsOperationService#isHasChildren(edu.utah.further.dts.api.domain.namespace.DtsData)
	 */
	@Override
	public boolean isHasChildren(final DtsData parent)
	{
		switch (parent.getType())
		{
			case CONCEPT_CHILD:
			{
				final DtsConcept parentConcept = (DtsConcept) parent;
				final DtsNamespace ns = findNamespaceById(parentConcept.getNamespaceId());
				switch (ns.getNamespaceType())
				{
					case ONTYLOG:
					case ONTYLOG_EXTENSION:
					{
						return parentConcept.getHasChildren();
					}

					default:
					{
						return !getChildrenByAssociation(ns, parentConcept).isEmpty();
					}
				}
			}

			default:
			{
				return !getChildren(parent).isEmpty();
			}
		}
	}

	/**
	 * Return the list of children of a concept.
	 * 
	 * @param vd
	 *            CA DSR Value Domain descriptor they are not find in the children context
	 * @return list of concept children
	 * @throws ApplicationException
	 */
	@Override
	public List<DtsConcept> getChildren(final DtsConceptId vd)
	{
		return getChildren(findConceptByProperty(findNamespaceByName(vd.getNamespace()),
				vd.getPropertyName(), vd.getPropertyValue(),
				DtsOptions.DEFAULT_NO_ATTRIBUTES));
	}

	// /**
	// * @param concept
	// * @param associationType
	// * @param inverse
	// * @return
	// * @see
	// edu.utah.further.dts.api.service.DtsOperationService#getAssociatedConcepts(edu.utah.further.dts.api.domain.concept.DtsConcept,
	// * com.apelon.dts.client.association.AssociationType, boolean)
	// */
	// public List<DtsConcept> getAssociatedConcepts(final DtsConcept concept,
	// final AssociationType associationType, final boolean inverse)
	// {
	// if (log.isDebugEnabled())
	// {
	// log.debug("Getting associated concepts of " + concept
	// + " by association type " + associationType);
	// }
	// final List<DtsConcept> associatedConceptList = newList();
	// // TODO: remove this workaround cast and internal usage of an Apelon object.
	// // Will throw an exception if concept is a TO rather than a DTS business object!
	// validateBusinessEntity(concept);
	// final OntylogConcept apelonConcept = ((DtsConceptImpl) concept)
	// .getAsOntylogyConcept();
	// final ConceptAssociation[] associationArray = inverse ? apelonConcept
	// .getFetchedInverseConceptAssociations() : apelonConcept
	// .getFetchedConceptAssociations();
	// for (final ConceptAssociation association : associationArray)
	// {
	// if (associationType.getName().equals(
	// association.getAssociationType().getName()))
	// {
	// associatedConceptList.add(loadAssociatedConcept(association, inverse));
	// }
	// }
	// return associatedConceptList;
	// }

	/**
	 * Translate a concept in a source namespace to a <strong>directly or
	 * inversely</strong> associated concept in a target namespace.
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
	 * @return a bean holding the target concept and direction; the target concept
	 *         property is <code>null</code> if not found using both directions (and then
	 *         the direction property value is meaningless)
	 * @throws ApplicationException
	 *             if source concept is not found
	 */
	@Override
	public TranslationResult translateConcept(final DtsConceptId sourceConceptId,
			final DtsOptions options)
	{
		return new TranslateConceptBuilder(sourceConceptId).setOptions(options).build();
	}

	/**
	 * Translate the children of a concept to their matches in a different namespace.
	 * <p>
	 * <b><i>Warning: not supported yet.</i></b>
	 * 
	 * @param conceptId
	 *            parent concept in the source namespace
	 * @param targetNs
	 *            "local namespace" - the target namespace we translate
	 *            <code>parent</code>'s children to
	 * @return a map of localNs-concept-to-prent-child-concept. Includes all concepts in
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
		// TODO: implement this method based on Susan's DTS requirements when it is needed
		return newMap();
	}

	/**
	 * Return the preferred name synonym of a concept.
	 * 
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @param throw an exception upon failure to find a concept if <code>true</code>,
	 *        otherwise gracefully return a <code>null</code>
	 * @return concept's preferred name synonym
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getConceptPreferredName(edu.utah.further.dts.api.to.DtsConceptId,
	 *      boolean)
	 */
	@Override
	public String getConceptPreferredName(final DtsConceptId conceptId,
			final DtsOptions options)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting preferred name of concept " + conceptId);
		}
		// Retrieve source concept including its associations
		final DtsNamespace sourceNs = findNamespaceByName(conceptId.getNamespace());
		final DtsConcept concept = findConceptByProperty(sourceNs,
				conceptId.getPropertyName(), conceptId.getPropertyValue(),
				DtsOptions.DEFAULT_ALL_ATTRIBUTES);

		validateConceptNotNull(concept, options);
		if (concept == null)
		{
			return null;
		}

		// TODO: remove this workaround cast and internal usage of an Apelon object.
		// Will throw an exception if concept is a TO rather than a DTS business
		// object!
		validateBusinessEntity(concept);
		final OntylogConcept apelonConcept = ((DtsConceptImpl) concept)
				.getAsOntylogyConcept();
		final String preferredName = apelonConcept
				.getFetchedPreferredTerm()
				.getTerm()
				.getName();
		validateConceptPreferredName(options, preferredName);
		return preferredName;
	}

	/**
	 * Execute a TQL query. Only read-only queries are allowed.
	 * 
	 * @param tqlQuery
	 *            TQL query string
	 * @param options
	 *            defines the attributes to be returned on the concepts
	 * @return a TQL result set = list of DTS concepts, with the attributes specified by
	 *         <code>das</code>
	 */
	@Override
	public List<DtsConcept> runTql(final String tqlQuery, final DtsOptions options)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Running TQL without export " + quote(tqlQuery));
		}
		if (!DtsUtil.isReadOnly(tqlQuery))
		{
			throw new ApplicationException(ErrorCode.UNSUPPORTED_OPERATION, "TQL Query "
					+ StringUtil.quote(tqlQuery) + " is not read-only");
		}
		// Prepare query
		final TQL tql = dtsObjectFactory.createTqlQuery();
		tql.setQuerystring(tqlQuery);
		tql.setExportfile(null);
		tql.setExportwriter(null);

		// Run and convert results to our API
		tql.run();
		try
		{
			return getOntylogConceptArrayAsToList(tql
					.getCollection(toConceptAttributeSetDescriptor(options
							.getAttributeSet())));
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to run tql query " + quote(tqlQuery),
					e);
		}
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
		if (log.isDebugEnabled())
		{
			log.debug("Running TQL with export " + quote(tqlQuery));
		}
		File temp;
		try
		{
			temp = DtsUtil.getTqlTempOutputFile();
		}
		catch (final IOException e)
		{
			// Error opening/closing file, etc.
			throw new ApplicationException(ErrorCode.IO_ERROR, "Invalid TQL query: "
					+ tqlQuery, e);
		}
		try (final PrintWriter writer = new PrintWriter(temp);)
		{
			final TQL tql = dtsObjectFactory.createTqlQuery();
			tql.setQuerystring(tqlQuery);
			tql.setExportfile(null);
			tql.setExportwriter(writer);
			tql.run();
			writer.close();

			final List<List<String>> result = readTqlOutputFile(temp);
			return result;
		}
		catch (final IOException e)
		{
			// Error opening/closing file, etc.
			throw new ApplicationException(ErrorCode.IO_ERROR, "Invalid TQL query: "
					+ tqlQuery, e);
		}
	}

	/**
	 * This implementation searches the hierarchy of the first parent of the given concept
	 * for a concept with the given superConceptName
	 * 
	 * @param concept
	 * @param superConceptName
	 * @return true if the given concept has a superConcept with superConceptName
	 * @see DtsOperationService#hasSuperConcept(DtsConcept, String, String)
	 */
	@Override
	public boolean hasSuperConcept(final DtsConcept concept,
			final String superConceptName, final String associationTypeName)
	{
		boolean hasParentWithName = false;
		final NavQuery navQuery = dtsObjectFactory.createNavQuery();
		try
		{
			if (ReflectionUtil.instanceOf(concept, DtsNamespaceAdapter.class))
			{
				return false;
			}
			validateBusinessEntity(concept);
			DTSConcept apelonConcept = ((DtsConceptImpl) concept).getAsDTSConcept();

			final AssociationType associationType = dtsObjectFactory
					.createAssociationQuery()
					.findAssociationTypeByName(associationTypeName,
							concept.getNamespaceId());
			NavParentContext navParentContext = navQuery.getNavParentContext(
					apelonConcept,
					toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES),
					associationType);
			int traversals = 1;
			do
			{
				final ConceptParent[] parents = navParentContext.getParents();
				if (parents.length > 0)
				{
					apelonConcept = parents[0];
					if (apelonConcept.getName().equals(superConceptName))
					{
						hasParentWithName = true;
					}
					else
					{
						navParentContext = navQuery.getNavParentContext(apelonConcept,
								toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES),
								associationType);
						traversals++;
					}
				}
			}
			while (!hasParentWithName && traversals <= maxTraversals
					&& navParentContext.getParents().length != 0);
		}
		catch (final DTSException de)
		{
			log.error("Unable to get parents: " + de.getMessage());
		}
		return hasParentWithName;

	}

	/**
	 * Returns a list of the first parents found for a given concept. Does not support
	 * concepts with multiple parents. Does not include the concept Namespace.
	 * 
	 * This implementation checks and casts to {@link DtsConceptImpl} to retrieve the
	 * underlying Apelon DTS Concept, you have been warned.
	 * 
	 * @param concept
	 *            the concept to get parents for
	 * @return A list of parents of the concept
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getFirstParents(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public List<DtsConcept> getFirstParents(final DtsConcept concept)
	{
		return getFirstParents(concept, false);
	}

	/**
	 * Returns a list of the first parents found for a given concept. Does not support
	 * concepts with multiple parents.
	 * 
	 * This implementation checks and casts to {@link DtsConceptImpl} to retrieve the
	 * underlying Apelon DTS Concept with the expception of a {@link DtsNamespaceAdapter}
	 * which is used to represent the namespace as a DtsConcept, this always returns an
	 * empty list. You have been warned.
	 * 
	 * @param concept
	 *            the concept to get parents for
	 * @param includeNamespace
	 *            whether or not to include the namespace as a parent in the list
	 * @return a list of parents of the concept
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getFirstParents(edu.utah.further.dts.api.domain.concept.DtsConcept,
	 *      boolean)
	 */
	@Override
	public List<DtsConcept> getFirstParents(final DtsConcept concept,
			final boolean includeNamespace)
	{
		final List<DtsConcept> superConcepts = newList();
		final NavQuery navQuery = dtsObjectFactory.createNavQuery();
		try
		{
			// Always return an empty list for a NamespaceAdapter, this is needed for tree
			// walking
			if (ReflectionUtil.instanceOf(concept, DtsNamespaceAdapter.class))
			{
				return superConcepts;
			}
			// TODO: remove this workaround cast and internal usage of an Apelon object.
			// Will throw an exception if concept is a TO rather than a DTS business
			// object!
			validateBusinessEntity(concept);
			OntylogConcept apelonConcept = ((DtsConceptImpl) concept)
					.getAsOntylogyConcept();

			// Retrieve an initial NavParentContext which is a structure that holds the
			// parents
			NavParentContext navParentContext = navQuery.getNavParentContext(
					apelonConcept,
					toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES));
			do
			{
				final ConceptParent[] conceptParents = navParentContext.getParents();
				if (conceptParents.length <= 0)
				{
					return superConcepts;
				}
				// Retrieve the first parent and assign it as the current concept
				apelonConcept = conceptParents[0];
				// Add the parent to the list
				superConcepts.add(new DtsConceptImpl(apelonConcept));
				// Retrieve a new NavParentContext to find the next parent
				navParentContext = navQuery.getNavParentContext(apelonConcept,
						toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES));
			}
			// iterate until their are no more parents
			while (navParentContext.getParents().length != 0);
		}
		catch (final DTSException de)
		{
			// if it failed half way through, return an empty list
			log.error("Unable to get first parents: " + de.getMessage());
			return newList();
		}
		if (!includeNamespace)
		{
			// Remove the last element which is assumed to be the namespace based on the
			// fact that we are walking backwards up the tree
			// List is zero indexed, therefore, last element is N - 1
			superConcepts.remove(superConcepts.size() - 1);
		}
		return superConcepts;
	}

	// ========================= GET/SET METHODS ===========================

	/**
	 * Set a new value for the dtsObjectFactory property.
	 * 
	 * @param dtsObjectFactory
	 *            the dtsObjectFactory to set
	 */
	public void setDtsObjectFactory(final DtsObjectFactory dtsObjectFactory)
	{
		this.dtsObjectFactory = dtsObjectFactory;
	}

	/**
	 * Return the maxResultSize property.
	 * 
	 * @return the maxResultSize
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getMaxResultSize()
	 */
	@Override
	public int getMaxResultSize()
	{
		return maxResultSize;
	}

	/**
	 * Set a new value for the maxResultSize property.
	 * 
	 * @param maxResultSize
	 *            the maxResultSize to set
	 */
	@Override
	public void setMaxResultSize(final int maxResultSize)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Setting search limit to " + maxResultSize);
		}
		this.maxResultSize = maxResultSize;
	}

	/**
	 * Return the maxTraversals property.
	 */
	@Override
	public int getMaxTraversals()
	{
		return maxTraversals;
	}

	/**
	 * Set a new value for the maxTraversals property.
	 * 
	 * @param maxTraversals
	 *            the maxTraversals to set
	 */
	@Override
	public void setMaxTraversals(final int maxTraversals)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Setting maximum parent traversal limit to " + maxTraversals);
		}
		this.maxTraversals = maxTraversals;
	}

	/**
	 * Set a new value for the cfs property.
	 * 
	 * @param cfs
	 *            the cfs to set
	 */
	public void setCfs(final ConceptFinderService cfs)
	{
		this.cfs = cfs;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the dtsObjectFactory property.
	 * 
	 * @return the dtsObjectFactory
	 * @see edu.utah.further.dts.api.service.DtsOperationService#getDtsObjectFactory()
	 */
	DtsObjectFactory getDtsObjectFactory()
	{
		return dtsObjectFactory;
	}

	// /**
	// * @param association
	// * @param inverse
	// * @return
	// */
	// private DtsConcept loadAssociatedConcept(final ConceptAssociation association,
	// final boolean inverse)
	// {
	// if (log.isDebugEnabled())
	// {
	// log.debug("Loading " + (inverse ? "inversely " : "") + "associated concept: "
	// + association);
	// }
	// final DTSConcept associatedConcept = inverse ? association.getFromConcept()
	// : association.getToConcept();
	// final DtsConcept fullToConcept = findConceptByName(
	// associatedConcept.getNamespaceId(), associatedConcept.getName());
	// return fullToConcept;
	// }

	/**
	 * @param parent
	 * @return list of children of the parent concept
	 */
	private List<DtsConcept> getChildrenByChildContext(final DtsData parent)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting children of " + parent);
		}
		final DtsConcept concept = (DtsConcept) parent;

		// Retrieve children via the child context mechanism
		final NavQuery navQuery = dtsObjectFactory.createNavQuery();

		// TODO: remove this workaround cast and internal usage of an Apelon object.
		// Will throw an exception if concept is a TO rather than a DTS business object!
		final OntylogConcept apelonConcept = ((DtsConceptImpl) concept)
				.getAsOntylogyConcept();

		// If parent is in a base namespace that has an extension, retrieve its children
		// from both the base and extension namespace using the extra argument to
		// getNavChildConext().
		// @see
		// http://apelon-dts.sourceforge.net/javadoc/com/apelon/dts/client/doc-files/tutorial.html#Chapter_5_The_SearchQuery_Class
		final Integer extensionId = getNamespaceBaseToExtension().get(
				new Integer(concept.getNamespaceId()));
		NavChildContext childCtx = null;
		if (extensionId != null)
		{
			final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
			Namespace extensionNs = null;
			try
			{
				extensionNs = nameQuery.findNamespaceById(extensionId.intValue());
			}
			catch (final DTSException e)
			{
				throw new ApplicationException(
						"Unable to find extension namespace with id"
								+ quote(String.valueOf(extensionId)), e);
			}
			try
			{
				childCtx = navQuery.getNavChildContext(apelonConcept,
						toConceptAttributeSetDescriptor(DEFAULT_NO_ATTRIBUTES),
						extensionNs);
			}
			catch (final DTSException e)
			{
				throw new ApplicationException(
						"Unable to retrieve child context for querying", e);
			}
		}
		else
		{
			try
			{
				childCtx = navQuery.getNavChildContext(apelonConcept,
						toConceptAttributeSetDescriptor(DEFAULT_NO_ATTRIBUTES));
			}
			catch (final DTSException e)
			{
				throw new ApplicationException(
						"Unable to retrieve child context for querying", e);
			}
		}
		final ConceptChild[] childrenArray = childCtx.getChildren();
		return getOntylogConceptArrayAsToList(childrenArray);
	}

	/**
	 * Retrieve children via Parent-Of associations. Applies to thesaurus namespace
	 * concept tree navigation.
	 * 
	 * @param concept
	 * @return
	 */
	private List<DtsConcept> getChildrenByAssociation(final DtsNamespace namespace,
			final DtsConcept concept)
	{
		final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
		final AssociationType associationType = getParentOfAssociation(namespace);
		OntylogConcept[] matches;
		try
		{
			matches = searchQuery.findConceptsWithInverseConceptAssociationMatching(
					associationType, concept.getName(), getSearchOptionsProperties());
		}
		catch (final DTSException e)
		{
			throw new ApplicationException(
					"Unable to find concepts with inverse assosciation", e);
		}
		return getOntylogConceptArrayAsToList(matches);
	}

	/**
	 * @param parent
	 * @return
	 */
	private List<DtsConcept> getConceptChildrenOfNamespace(final DtsData parent)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting children of " + parent);
		}
		final DtsNamespace namespace = (DtsNamespace) parent;
		final NavQuery navQuery = dtsObjectFactory.createNavQuery();
		ConceptChild[] conceptChildren;
		try
		{
			conceptChildren = navQuery.getConceptChildRoots(
					DtsUtil.toConceptAttributeSetDescriptor(DEFAULT_NO_ATTRIBUTES),
					namespace.getId());
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to get children roots", e);
		}
		return getOntylogConceptArrayAsToList(conceptChildren);
	}

	/**
	 * Return a parent-of association object.
	 * 
	 * @param namespace
	 *            association's owning namespace
	 * @return a parent-of association object
	 */
	private AssociationType getParentOfAssociation(final DtsNamespace namespace)
	{
		AssociationType associationType;
		try
		{
			associationType = dtsObjectFactory
					.createAssociationQuery()
					.findAssociationTypeByName("Parent Of", namespace.getId());
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find assosciation type by name", e);
		}
		validateNotNull("association type", associationType);
		return associationType;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private List<List<String>> readTqlOutputFile(final File file) throws IOException
	{
		final List<List<String>> result = newList();
		try (final Scanner scanner = new Scanner(file))
		{
			scanner.useDelimiter(Strings.NEW_LINE_STRING);
			while (scanner.hasNext())
			{
				result.add(parseTqlResultLine(scanner.next()));
			}
			return result;
		}
	}

	/**
	 * Set up a dual map from a base namespace ID to an extension namespace ID, if the
	 * extension exists for the base.
	 */
	private void setupNamespaceBaseToExtension()
	{
		namespaceBaseToExtension = CollectionUtil.newMap();
		for (final DtsNamespace namespace : getNamespaceList())
		{
			// Here: namespace is extension and linked namespace is the base
			final int linkedNamespaceId = namespace.getLinkedNamespaceId();
			if (linkedNamespaceId > 0)
			{
				namespaceBaseToExtension.put(new Integer(linkedNamespaceId), new Integer(
						namespace.getId()));
			}
		}
	}

	/**
	 * A generic finder that returns a unique concept by some search arguments. Searches
	 * in both a base and extension namespaces.
	 * 
	 * @param finder
	 *            contains search criteria
	 * @param attributeSet
	 *            attribute set to retrieve with the concept
	 * @param args
	 *            other search criteria arguments specific to this finder, not including
	 *            the namespace criterion
	 * @return the matching concept(s)
	 */
	private TranslationResult findConceptsByCriteria(final ConceptFinder finder,
			final DtsOptions options, final int namespaceId, final Object... args)
	{
		// Search in base namespace first
		final TranslationResult result = findConceptsAndCatchExceptions(finder, options,
				namespaceId, args);
		final List<DtsConcept> baseConcepts = result.getConcepts();
		if (baseConcepts != null && !baseConcepts.isEmpty())
		{
			return result;
		}

		// If not found and the namespace has an extension, search in the extension
		final Integer extensionId = getNamespaceBaseToExtension().get(
				new Integer(namespaceId));
		if (extensionId != null)
		{
			return findConceptsAndCatchExceptions(finder, options,
					extensionId.intValue(), args);
		}

		// Not found, throw exception or fall back with null
		validateConceptNotNull(null, options);
		return TranslationResult.NULL_RESULT;
	}

	/**
	 * Finds concepts using a concept finder function pointer. Propagates DTS exceptions.
	 * 
	 * @param finder
	 *            concept finder
	 * @param attributeSet
	 *            specify {@link DtsAttributeSet#isThrowExceptionOnFailure()} to throw an
	 *            exception here upon failure, otherwise returns <code>null</code> and
	 *            does not throw an exception
	 * @param namespaceId
	 *            ID of concept's namespace
	 * @param args
	 *            search criteria parameter values
	 * @return concept, if found, or <code>null</code> if not found or if an Apelon DTS
	 *         failure occurs, and if {@link DtsAttributeSet#isThrowExceptionOnFailure()}
	 *         is <code>false</code>
	 * @throws ApplicationException
	 *             If concept not found or an Apelon DTS failure occurs, and
	 *             {@link DtsAttributeSet#isThrowExceptionOnFailure()} is
	 *             <code>true</code>
	 */
	private TranslationResult findConceptsAndCatchExceptions(final ConceptFinder finder,
			final DtsOptions options, final int namespaceId, final Object... args)
	{
		try
		{
			return finder.findConcepts(options, namespaceId, args);
		}
		catch (final Throwable e)
		{
			DtsUtil.wrapAndThrowApelonException(options.isThrowExceptionOnFailure(), e);
			return TranslationResult.NULL_RESULT;
		}
	}

	/**
	 * -----------------------------------------------------------------------------------<br>
	 * A convenient builder of concept translation options.
	 * -----------------------------------------------------------------------------------<br>
	 */
	private final class TranslateConceptBuilder implements Builder<TranslationResult>
	{
		// Required fields
		private final DtsConceptId sourceConceptId;
		private DtsOptions options = new DtsOptions();

		// Internal useful fields
		private DTSSearchOptions dtsSearchOptions;
		private Boolean[] associationDirections;

		/**
		 * @param sourceConceptId
		 */
		public TranslateConceptBuilder(final DtsConceptId sourceConceptId)
		{
			super();
			this.sourceConceptId = sourceConceptId;

			// Set default options: assume a unique target namespace and search in all
			// association directions
			setTargetNamespace(null);
			setStandardSourceNamespace(null);
		}

		/**
		 * Set a new value for the options property.
		 * 
		 * @param options
		 *            the options to set
		 */
		public TranslateConceptBuilder setOptions(final DtsOptions options)
		{
			this.options = options;
			setTargetNamespace(options.getTargetNamespace());
			setStandardSourceNamespace(options.getStandardSourceNamespace());
			return this;
		}

		/**
		 * Set a new value for the targetNamespace property.
		 * 
		 * @param targetNamespace
		 *            the targetNamespace to set
		 */
		private TranslateConceptBuilder setTargetNamespace(final String targetNamespace)
		{
			this.dtsSearchOptions = (targetNamespace == null) ? getSearchOptionsUniqueResultProperties()
					: getSearchOptionsProperties(findNamespaceByName(targetNamespace));
			this.dtsSearchOptions
					.setAttributeSetDescriptor(toConceptAttributeSetDescriptor(DEFAULT_ALL_ATTRIBUTES));
			return this;
		}

		/**
		 * Set a new value for the standardSourceNamespace property.
		 * 
		 * @param standardSourceNamespace
		 *            the standardSourceNamespace to set
		 */
		private TranslateConceptBuilder setStandardSourceNamespace(
				final Boolean standardSourceNamespace)
		{
			this.associationDirections = (standardSourceNamespace == null) ? new Boolean[]
			{ TRUE, FALSE }
					: new Boolean[]
					{ standardSourceNamespace };
			return this;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public TranslationResult build()
		{
			// Try all possible directions and return the first non-null target
			// encountered
			Boolean foundSourceConcept = null;
			for (final Boolean standardSourceNamespace : associationDirections)
			{
				final TranslationResult result = findConceptsByCriteria(
						cfs.get(TRANSLATIONS),
						options.copy()
								.setThrowExceptionOnFailure(false)
								.setAttributeSetType(NO_ATTRIBUTES),
						INVALID_VALUE_INTEGER, sourceConceptId, standardSourceNamespace,
						dtsSearchOptions);
				foundSourceConcept = result.isFoundSourceConcept();
				final List<DtsConcept> targetConcepts = result.getConcepts();
				if (targetConcepts != null && targetConcepts.size() != 0)
				{
					return result;
				}
			}

			// Didn't find any match using all possible directions
			if (options.isThrowExceptionOnFailure())
			{
				throw new ApplicationException(new ApplicationError(
						ErrorCode.RESOURCE_NOT_FOUND,
						"Could not find concept via all possible association directions"));
			}
			return new TranslationResult(CollectionUtil.<DtsConcept> newList(), null,
					foundSourceConcept);
		}
	}

	/**
	 * @param concept
	 * @param options
	 */
	private static void validateConceptNotNull(final DtsConcept concept,
			final DtsOptions options)
	{
		if (options.isThrowExceptionOnFailure() && (concept == null))
		{
			throw new ApplicationException(new ApplicationError(
					ErrorCode.RESOURCE_NOT_FOUND, "Could not find concept by criteria"));
		}
	}

	/**
	 * @param options
	 * @param preferredName
	 * @return
	 */
	private static void validateConceptPreferredName(final DtsOptions options,
			final String preferredName)
	{
		if (options.isThrowExceptionOnFailure() && (preferredName == null))
		{
			throw new ApplicationException(ErrorCode.PROPERTY_NOT_FOUND,
					"Concept preferred name was not found");
		}
	}
}
