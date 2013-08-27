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

import static edu.utah.further.core.api.constant.ErrorCode.RESOURCE_NOT_FOUND;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.ALL_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.NO_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.PROPERTIES;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.isConceptAssociatedTo;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.toDtsConceptList;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.validateAndReturnMatches;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.validateAndReturnUniqueMatch;
import static edu.utah.further.dts.impl.service.DtsOperationUtil.validateBusinessEntity;
import static edu.utah.further.dts.impl.util.DtsUtil.SEARCH_LIMIT_UNIQUE_RESULT;
import static edu.utah.further.dts.impl.util.DtsUtil.getSearchOptionsProperties;
import static edu.utah.further.dts.impl.util.DtsUtil.toConceptAttributeSetDescriptor;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.attribute.DTSPropertyType;
import com.apelon.dts.client.attribute.DTSQualifier;
import com.apelon.dts.client.attribute.QualifierType;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.concept.DTSSearchOptions;
import com.apelon.dts.client.concept.OntylogConcept;
import com.apelon.dts.client.concept.SearchQuery;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.math.misc.Pair;
import edu.utah.further.core.util.cache.CachingService;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.TranslationResult;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.impl.domain.DtsConceptImpl;
import edu.utah.further.dts.impl.domain.association.DtsQualifierImpl;

/**
 * A factory of {@link ConceptFinderService}. Re-factored out of
 * {@link DtsOperationServiceImpl} to make it a little simpler.
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
@Utility
@Service("conceptFinderService")
public class ConceptFinderService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ConceptFinderService.class);

	// ========================= FIELDS ====================================

	/**
	 * Double-locking-initialized-singleton instances of {@link ConceptFinder}s supported
	 * by this class.
	 */
	private volatile Map<ConceptFinderType, ConceptFinder> conceptFinderMap;

	/**
	 * Our cache region name - property types.
	 */
	private static final String CACHE_REGION_PROPERTY_TYPE = "dtsPropertyType";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	@Autowired
	private DtsOperationService dos;

	/**
	 * Executes basic DTS operations.
	 */
	@Autowired
	private DtsObjectFactory dtsObjectFactory;

	/**
	 * Caches objects.
	 */
	@Autowired
	private CachingService cacheService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Register the default paging providers here.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		// Initialize cache regions
		cacheService.addRegion(CACHE_REGION_PROPERTY_TYPE);
	}

	// ========================= METHODS ===================================

	/**
	 * Return a concept finder.
	 * 
	 * @param type
	 *            concept finder type
	 * @return corresponding concept finder instance, wired to the proper
	 *         {@link DtsOperationServiceImpl} instance auto-wired to this object.
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public ConceptFinder get(final ConceptFinderType type)
	{
		return getConceptFinderMap().get(type);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the type-to-finder map. Once initialized, caches the map so that no more
	 * finder objects are unnecessarily created upon further demand.
	 * 
	 * @return the type-to-finder map
	 */
	private Map<ConceptFinderType, ConceptFinder> getConceptFinderMap()
	{
		// using the double-checked locking with volatile
		// @see http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
		if (conceptFinderMap == null)
		{
			synchronized (this)
			{
				if (conceptFinderMap == null)
				{
					setupConceptFinderMap();
				}
			}
		}
		return conceptFinderMap;
	}

	/**
	 * Set up the concept finder map. Adds all finders as its entries. Then
	 * {@link #getConceptFinderMap()} caches the map so that no more finder objects are
	 * unnecessarily created upon further demand.
	 */
	private void setupConceptFinderMap()
	{
		conceptFinderMap = CollectionUtil.newMap();
		conceptFinderMap.put(ConceptFinderType.PROPERTY, CONCEPT_FINDER_BY_PROPERTY());
		conceptFinderMap.put(ConceptFinderType.TRANSLATIONS,
				CONCEPT_FINDER_TRANSLATIONS());
		conceptFinderMap.put(ConceptFinderType.UNIQUE_ID, CONCEPT_FINDER_BY_UNIQUE_ID());
		conceptFinderMap.put(ConceptFinderType.MULTI_BY_PROPERTY,
				CONCEPT_FINDER_MULTI_BY_PROPERTY());
	}

	// ========================= CONCEPT FINDERS ===========================

	/**
	 * Finds a concept by namespace ID and concept ID.
	 */
	private ConceptFinder CONCEPT_FINDER_BY_UNIQUE_ID()
	{
		return new AbstractConceptFinder(dos, dtsObjectFactory)
		{
			/**
			 * Find a concept by namespace ID and concept ID.
			 * 
			 * @param attributeSet
			 *            attribute set to retrieve wit the concept
			 * @param namespaceId
			 *            ID of namespace to search in
			 * @param args
			 *            criteria to select concept for translation.
			 *            <ul>
			 *            <li><i>args[0] = conceptId</i>: concept ID to search for in the
			 *            namespace.</li>
			 *            </ul>
			 * @return uniquely matching concept
			 * @see edu.utah.further.dts.impl.service.ConceptFinder#findConcept(edu.utah.further.dts.api.domain.namespace.DtsAttributeSet,
			 *      int, java.lang.Object[])
			 */
			@Override
			public TranslationResult findConcepts(final DtsOptions options,
					final int namespaceId, final Object... args)
			{
				final int conceptId = ((Integer) args[0]).intValue();
				final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
				DTSConcept apelonConcept = null;
				try
				{
					apelonConcept = searchQuery.findConceptById(conceptId, namespaceId,
							toConceptAttributeSetDescriptor(options.getAttributeSet()));
				}
				catch (final DTSException e)
				{
					if (options.isThrowExceptionOnFailure())
					{
						throw new ApplicationException(RESOURCE_NOT_FOUND,
								"Expected a unique concept but did not find any concepts");
					}
				}
				if (options.isThrowExceptionOnFailure() && apelonConcept == null)
				{
					throw new ApplicationException(RESOURCE_NOT_FOUND,
							"Expected a unique concept but did not find any concepts");
				}
				return new TranslationResult(toDtsConceptList(new DTSConcept[]
				{ apelonConcept }));
			}
		};
	}

	/**
	 * Finds a concept by namespace, property name and value. Useful for many
	 * {@link DtsOperationService} finders and translators.
	 */
	private ConceptFinder CONCEPT_FINDER_BY_PROPERTY()
	{
		return new AbstractConceptFinder(dos, dtsObjectFactory)
		{
			/**
			 * Find a concept by namespace, property name and value.
			 * 
			 * @param attributeSet
			 *            attribute set to retrieve with the concept
			 * @param namespaceId
			 *            ID of namespace to search in
			 * @param args
			 *            criteria to select concept for translation.
			 *            <ul>
			 *            <li><i>args[0] = propertyName</i>: name of property to search by
			 *            in the namespace.</li>
			 *            <li><i>args[1] = propertyValue</i>: value of property to search
			 *            by in the namespace.</li>
			 *            </ul>
			 * @return uniquely matching concept
			 * @throws ApplicationException
			 *             if failed to execute Apelon API search
			 * @see edu.utah.further.dts.impl.service.ConceptFinder#findConcept(edu.utah.further.dts.api.domain.namespace.DtsAttributeSet,
			 *      int, java.lang.Object[])
			 */
			@Override
			public TranslationResult findConcepts(final DtsOptions options,
					final int namespaceId, final Object... args)
			{
				final String propertyName = (String) args[0];
				final String propertyValue = (String) args[1];
				final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
				final DTSPropertyType prefNameType = findPropertyType(namespaceId,
						propertyName, searchQuery);
				final DTSSearchOptions searchOptions = new DTSSearchOptions(
						SEARCH_LIMIT_UNIQUE_RESULT, namespaceId,
						toConceptAttributeSetDescriptor(options.getAttributeSet()));
				DTSConcept[] matches = null;
				try
				{
					matches = searchQuery.findConceptsWithPropertyMatching(prefNameType,
							propertyValue, searchOptions);
				}
				catch (final DTSException e)
				{
					throw new ApplicationException(
							"Unable to find concepts with matching property", e);
				}
				return new TranslationResult(validateAndReturnUniqueMatch(
						toDtsConceptList(matches), options));
			}
		};
	}

	/**
	 * Finds a concept by namespace, property name and value. Useful for many
	 * {@link DtsOperationService} finders and translators.
	 */
	private ConceptFinder CONCEPT_FINDER_MULTI_BY_PROPERTY()
	{
		return new AbstractConceptFinder(dos, dtsObjectFactory)
		{
			/**
			 * Find a concept by namespace, property name and value.
			 * 
			 * @param attributeSet
			 *            attribute set to retrieve with the concept
			 * @param namespaceId
			 *            ID of namespace to search in
			 * @param args
			 *            criteria to select concept for translation.
			 *            <ul>
			 *            <li><i>args[0] = propertyName</i>: name of property to search by
			 *            in the namespace.</li>
			 *            <li><i>args[1] = propertyValue</i>: value of property to search
			 *            by in the namespace.</li>
			 *            </ul>
			 * @return uniquely matching concept
			 * @throws ApplicationException
			 *             if failed to execute Apelon API search
			 * @see edu.utah.further.dts.impl.service.ConceptFinder#findConcept(edu.utah.further.dts.api.domain.namespace.DtsAttributeSet,
			 *      int, java.lang.Object[])
			 */
			@Override
			public TranslationResult findConcepts(final DtsOptions options,
					final int namespaceId, final Object... args)
			{
				final String propertyName = (String) args[0];
				final String propertyValue = (String) args[1];
				final SearchQuery searchQuery = dtsObjectFactory.createSearchQuery();
				final DTSPropertyType prefNameType = findPropertyType(namespaceId,
						propertyName, searchQuery);
				final DTSSearchOptions searchOptions = new DTSSearchOptions(
						DTSSearchOptions.DEFAULT_LIMIT, namespaceId,
						toConceptAttributeSetDescriptor(options.getAttributeSet()));
				DTSConcept[] matches = null;
				try
				{
					matches = searchQuery.findConceptsWithPropertyMatching(prefNameType,
							propertyValue, searchOptions);
				}
				catch (final DTSException e)
				{
					throw new ApplicationException(
							"Unable to find concepts with matching property", e);
				}
				return new TranslationResult(validateAndReturnMatches(
						toDtsConceptList(matches), options));
			}
		};
	}

	/**
	 * A finder that is reused by all concept translation methods.
	 */
	private ConceptFinder CONCEPT_FINDER_TRANSLATIONS()
	{
		return new AbstractConceptFinder(dos, dtsObjectFactory)
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * edu.utah.further.dts.impl.service.AbstractConceptFinder#findConcepts(edu
			 * .utah.further.dts.api.service.DtsOptions, int, java.lang.Object[])
			 */
			@Override
			public TranslationResult findConcepts(final DtsOptions options,
					final int namespaceId, final Object... args)
			{
				final DtsConceptId sourceConceptId = (DtsConceptId) args[0];
				final boolean standardSourceNamespace = ((Boolean) args[1])
						.booleanValue();
				final DTSSearchOptions searchOptions = (DTSSearchOptions) args[2];
				if (log.isDebugEnabled())
				{
					log.debug("Translating concept from " + sourceConceptId + " "
							+ (standardSourceNamespace ? "from" : "to")
							+ " standard FURTHeR namespace ID "
							+ searchOptions.getNamespaceId());
				}
				// Retrieve source concept including its associations
				final DtsNamespace sourceNs = dos.findNamespaceByName(sourceConceptId
						.getNamespace());

				// do we have a qualifier?
				// the qualifier is assumed to be a property name-value pair.
				if (sourceConceptId.getQualifierName() != null)
				{
					return findQualifiedConcept(sourceNs, sourceConceptId, options,
							(Boolean) args[1]);
				}
				final DtsConcept sourceConcept = findConceptWithParent(sourceNs,
						sourceConceptId, options
								.copy()
								.setAttributeSetType(NO_ATTRIBUTES));
				if (log.isDebugEnabled())
				{
					log.debug("Source concept: " + sourceConcept);
				}

				final AssociationType associationType;
				// Retrieve the association type using the name specified in the
				// source DtsConceptId; if that doesn't exist, we'll default to
				// ExactMatch association type;owned by the FURTHeR namespace
				if (sourceConceptId.getAssociationType() != null)
				{
					associationType = getAssociationType(sourceConceptId
							.getAssociationType());
				}
				else
				{
					associationType = getExactMatchAssociationType();
				}

				// Try to retrieve association to target namespace first
				OntylogConcept[] matches = findAssociatedConcepts(
						standardSourceNamespace, searchOptions, sourceConcept,
						associationType);
				final Integer extensionId = dos.getNamespaceBaseToExtension().get(
						new Integer(searchOptions.getNamespaceId()));

				// If target namespace has an extension, look for an
				// association to its extension namespace
				if ((extensionId != null))
				{
					/*
					 * FUR-1703 - we need to get ALL_ATTRIBUTES in order for filtering to
					 * be successful.
					 */
					matches = concat(
							matches,
							findAssociatedConcepts(
									standardSourceNamespace,
									getSearchOptionsProperties(extensionId.intValue(),
											ALL_ATTRIBUTES), sourceConcept,
									associationType));
				}

				// FUR-1537
				// FUR-1703 - removed extensionId, we need to filter everything.
				final List<OntylogConcept> filteredMatches = filterMatches(sourceConcept,
						matches, !standardSourceNamespace);

				final List<DtsConcept> targetConcepts = toDtsConceptList(filteredMatches);
				if (log.isDebugEnabled())
				{
					final int size = targetConcepts.size();
					log.debug("Found " + size + " target "
							+ StringUtil.pluralForm("concept", size) + ":");
					for (final DtsConcept targetConcept : targetConcepts)
					{
						log.debug("   " + targetConcept);
					}
				}
				return new TranslationResult(targetConcepts, ((Boolean) args[1]),
						new Boolean(sourceConcept != null));
			}

			private TranslationResult findQualifiedConcept(final DtsNamespace sourceNs,
					final DtsConceptId sourceConceptId, final DtsOptions options,
					final Boolean standardSourceNamespace)
			{
				final List<DtsConcept> sourceConcepts = findConceptsWithSuperConcept(
						sourceNs, sourceConceptId,
						options.copy().setAttributeSetType(ALL_ATTRIBUTES));

				TranslationResult translationResult = TranslationResult.NULL_RESULT;
				if (sourceConcepts != null && sourceConcepts.size() > 0)
				{
					final QualifierType qualifierType = getQualifierTypeByName(sourceConceptId
							.getQualifierName());
					final DTSQualifier qualifier = new DTSQualifier(qualifierType,
							sourceConceptId.getQualifierValue());
					final List<DtsConcept> targetConcepts = CollectionUtil.newList();
					boolean sourceConceptFound = false;
					for (final DtsConcept sourceConcept : sourceConcepts)
					{
						for (final DtsAssociation association : sourceConcept
								.getFetchedConceptAssociations())
						{
							if (association.containsQualifier(new DtsQualifierImpl(
									qualifier)))
							{
								sourceConceptFound = true;
								// "refresh" the target obtained from association; we only
								// need properties
								targetConcepts.add(dos.findConceptByUniqueId(
										new DtsConceptUniqueId(association
												.getToItem()
												.getNamespaceId(), association
												.getToItem()
												.getId()), options
												.copy()
												.setAttributeSetType(PROPERTIES)));
							}
						}
					}
					translationResult = new TranslationResult(targetConcepts,
							standardSourceNamespace, Boolean.valueOf(sourceConceptFound));
				}
				return translationResult;
			}

			private DtsConcept findConceptWithParent(final DtsNamespace sourceNs,
					final DtsConceptId dtsConceptId, final DtsOptions options)
			{
				if (dtsConceptId.getSuperConceptName() == null)
				{
					return dos.findConceptByProperty(sourceNs,
							dtsConceptId.getPropertyName(),
							dtsConceptId.getPropertyValue(), options);
				}

				final List<DtsConcept> concepts = findConceptsWithSuperConcept(sourceNs,
						dtsConceptId, options);
				DtsConcept narrowedConcept = null;
				if (concepts != null && concepts.size() > 0)
				{
					narrowedConcept = concepts.get(0);
				}
				return narrowedConcept;
			}

			private List<DtsConcept> findConceptsWithSuperConcept(
					final DtsNamespace sourceNs, final DtsConceptId dtsConceptId,
					final DtsOptions options)
			{
				List<DtsConcept> narrowedConcepts;
				final List<DtsConcept> concepts = dos.findConceptsByProperty(sourceNs,
						dtsConceptId.getPropertyName(), dtsConceptId.getPropertyValue(),
						options);
				if (concepts != null && dtsConceptId.getSuperConceptName() != null)
				{
					narrowedConcepts = CollectionUtil.newList();
					for (final DtsConcept concept : concepts)
					{
						if (hasSuperConcept(concept, dtsConceptId))
						{
							narrowedConcepts.add(concept);
						}
					}
				}
				else
				{
					narrowedConcepts = concepts;
				}
				return narrowedConcepts;
			}

			private boolean hasSuperConcept(final DtsConcept concept,
					final DtsConceptId conceptId)
			{
				boolean hasParentConcept = false;
				try
				{
					hasParentConcept = dos.hasSuperConcept(concept,
							conceptId.getSuperConceptName(),
							conceptId.getSuperConceptAssociationTypeName());
				}
				catch (final NullPointerException npe)
				{
					log.error("Received NPE when searching for parent concept.");
				}
				return hasParentConcept;
			}

			/**
			 * Filter target concepts to those that are associated to the source concept
			 * and not to another concept with the same name in a different source
			 * namespace (FUR-1537).
			 * <p>
			 * 
			 * @param sourceConcept
			 * @param extensionId
			 * @param matches
			 * @param inverse
			 * @return
			 */
			private List<OntylogConcept> filterMatches(final DtsConcept sourceConcept,
					final OntylogConcept[] matches, final boolean inverse)
			{
				if ((sourceConcept == null))
				{
					return CollectionUtil.newList(matches);
				}
				validateBusinessEntity(sourceConcept);
				final OntylogConcept apelonSourceConcept = ((DtsConceptImpl) sourceConcept)
						.getAsOntylogyConcept();

				final List<OntylogConcept> filteredMatches = CollectionUtil.newList();
				for (final OntylogConcept targetConcept : matches)
				{
					if (isConceptAssociatedTo(targetConcept, apelonSourceConcept, inverse))
					{
						filteredMatches.add(targetConcept);
					}
				}
				return filteredMatches;
			}
		};
	}

	/**
	 * Concatenates two array of concepts
	 * 
	 * @param concepts1
	 *            first array of {@link OntylogConcept}s
	 * @param concepts2
	 *            second array of {@link OntylogConcept}s
	 * @return a concatenated array of concepts
	 */
	private OntylogConcept[] concat(final OntylogConcept[] concepts1,
			final OntylogConcept[] concepts2)
	{
		final OntylogConcept[] concepts3 = new OntylogConcept[concepts1.length
				+ concepts2.length];
		System.arraycopy(concepts1, 0, concepts3, 0, concepts1.length);
		System.arraycopy(concepts2, 0, concepts3, concepts1.length, concepts2.length);

		return concepts3;
	}

	/**
	 * Return a property type within a namespace.
	 * 
	 * @param namespaceId
	 * @param propertyName
	 * @param searchQuery
	 * @return
	 */
	private DTSPropertyType findPropertyType(final int namespaceId,
			final String propertyName, final SearchQuery searchQuery)
	{
		// TODO: use AOP to do the common if-else caching check here and in DOSImpl
		final Pair<Integer, String> key = new Pair<>(new Integer(
				namespaceId), propertyName);
		final DtsPropertyType cached = cacheService.getObject(CACHE_REGION_PROPERTY_TYPE,
				key);
		if (cached != null)
		{
			return cached.value;
		}
		return extracted(namespaceId, propertyName, searchQuery, key);
	}

	private DTSPropertyType extracted(final int namespaceId, final String propertyName,
			final SearchQuery searchQuery, final Pair<Integer, String> key)
	{
		DTSPropertyType loaded;
		try
		{
			loaded = searchQuery.findPropertyTypeByName(propertyName, namespaceId);
		}
		catch (final DTSException e)
		{
			throw new ApplicationException("Unable to find property type by name", e);
		}
		cacheService.saveObject(CACHE_REGION_PROPERTY_TYPE, key, new DtsPropertyType(
				loaded));
		return loaded;
	}

}
