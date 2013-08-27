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
package edu.utah.further.core.data.hibernate.query;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteriaFactory;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryAlias;
import edu.utah.further.core.query.domain.SortCriterion;
import edu.utah.further.core.query.domain.SortType;

/**
 * Converts a composite {@link SearchQuery} object to a Hibernate {@link GenericCriteria}
 * object. Note that this class is not thread-safe and must be synchronized externally.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 28, 2009
 */
@Implementation
public final class QueryBuilderHibernateImpl implements Builder<GenericCriteria>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(QueryBuilderHibernateImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * A destination object of the principal searchable entity. Filled with
	 * {@link Criterion}s upon returning from this factory.
	 */
	private GenericCriteria destination;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Session factory, required for retrieving a Hibernate session to which the hibernate
	 * criteria are bound.
	 */
	private final SessionFactory sessionFactory;

	/**
	 * Whether or not this criteria should return distinct results, done programmatically.
	 */
	private boolean isDistinct = false;

	/**
	 * What type of criteria, either main or sub
	 */
	private final CriteriaType criteriaType;

	/**
	 * The {@link SearchQuery} from which to build the {@link GenericCriteria} from.
	 */
	private SearchQuery searchQuery;

	/**
	 * The package where the domainClass lives.
	 */
	private final String domainClassPackage;

	/**
	 * Very useful object to get information about the root entity.
	 */
	private ClassMetadata classMetadata;

	/**
	 * Return a Hibernate criteria instance.
	 * 
	 * @param criteriaType
	 *            criteria type (main criteria/detached criteria=sub-criteria)
	 * @param domainClass
	 *            root entity type
	 * @param sessionFactory
	 *            session factory to bind to
	 */
	public static QueryBuilderHibernateImpl newInstance(final CriteriaType criteriaType,
			final String domainClassPackage, final SessionFactory sessionFactory)
	{
		return new QueryBuilderHibernateImpl(criteriaType, domainClassPackage,
				sessionFactory);
	}

	/**
	 * Convert a search query to Hibernate criteria.
	 * 
	 * @param criteriaType
	 *            criteria type (main criteria/detached criteria=sub-criteria)
	 * @param domainClass
	 *            root entity type
	 * @param sessionFactory
	 *            session factory to bind to
	 * @param searchQuery
	 *            search query to convert
	 */
	public static GenericCriteria convert(final CriteriaType criteriaType,
			final String domainClassPackage, final SessionFactory sessionFactory,
			final SearchQuery searchQuery)
	{
		if (searchQuery.getRootObjectName() == null)
		{
			throw new ApplicationException(
					"SearchQuery requires a root object in the package "
							+ domainClassPackage + " but root object name was null");
		}

		return newInstance(criteriaType, domainClassPackage, sessionFactory).setQuery(
				searchQuery).build();
	}

	/**
	 * Construct a Hibernate criteria builder.
	 * 
	 * @param criteriaType
	 *            criteria type (main criteria/detached criteria=sub-criteria)
	 * @param domainClass
	 *            root entity type
	 * @param sessionFactory
	 *            session factory to bind to
	 */
	private QueryBuilderHibernateImpl(final CriteriaType criteriaType,
			final String domainClassPackage, final SessionFactory sessionFactory)
	{
		super();
		this.criteriaType = criteriaType;
		this.sessionFactory = sessionFactory;
		this.domainClassPackage = domainClassPackage;
	}

	// ========================= IMPLEMENTATION: Builder<Criteria> =========

	/**
	 * Convert abstract the search criteria to a Hibernate Criterion.
	 * 
	 * @return root entity Hibernate Criterion destination object
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public GenericCriteria build()
	{
		Validate.notNull(searchQuery, "Search query must exist");
		Validate.notNull(searchQuery.getRootCriterion(),
				"Search query root criterion must exist");
		Validate.notNull(sessionFactory, "Session Factory");

		final Class<? extends PersistentEntity<?>> domainClass = searchQuery
				.getRootObject(domainClassPackage);

		destination = createCriteria(domainClass);

		// Set aliases
		addAliases(searchQuery, destination);

		classMetadata = sessionFactory.getClassMetadata(domainClass);

		// Add in the criterion objects
		destination.add(convertTree(searchQuery.getRootCriterion()));

		// Provide an opportunity to attach projections inside the entire criterion tree.
		// Note: COUNT-type criteria currently add their Projections as part
		// of convertTree(), so ProjectionFactoryHibernateImpl does not currently add any
		// projections.
		destination.setProjection(new ProjectionFactoryHibernateImpl(searchQuery
				.getRootCriterion(), classMetadata).build());

		// Set sorting options
		addSortOrder();

		// Set result set limits
		setLimits();

		// Set result set view
		if (isDistinct)
		{
			addDistinct();
		}

		return destination;
	}

	// ========================= METHODS ===================================

	/**
	 * Add abstract search criteria to the Hibernate destination criteria.
	 * 
	 * @param aSearchQuery
	 *            input set of search criteria
	 * @return {@code this}, for method chaining
	 */
	public QueryBuilderHibernateImpl setQuery(final SearchQuery aSearchQuery)
	{
		this.searchQuery = aSearchQuery;
		return this;
	}

	/**
	 * 
	 * Sets whether the Hibernate {@link GenericCriteria} should return distinct results
	 * or not. This distinct is done programmatically versus database distinct. The
	 * distinct is based on the root entity.
	 * 
	 * @param distinct
	 *            A flag indicating whether or not return distinct results
	 *            programmatically
	 * @return {@code this}, for method chaining
	 */
	public QueryBuilderHibernateImpl distinct(final boolean distinct)
	{
		this.isDistinct = distinct;
		return this;
	}

	/**
	 * Return the sessionFactory property. For testing only.
	 * 
	 * @return the sessionFactory
	 */
	SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Apply alias definitions of a search criterion to the corresponding Hibernate
	 * criteria.
	 * 
	 * @param searchQuery
	 * @param destination
	 */
	static void addAliases(final SearchQuery searchQuery,
			final GenericCriteria destination)
	{
		for (final SearchQueryAlias alias : searchQuery.getAliases())
		{
			// Note the argument order reversing between the search framework
			// and Hibernate but not between our search framework and Hibernate adapters
			destination.addAlias(alias.getKey(), alias.getValue()).setFetchMode(
					alias.getValue(), FetchMode.JOIN);
		}
	}

	/**
	 * Convert a search criterion tree into a Hibernate search criterion tree (like an
	 * adapter, but simply generates a Hibernate criterion instance).
	 */
	private Criterion convertTree(final SearchCriterion searchCriterion)
	{
		// Convert sub-criteria first
		final List<Criterion> convertedCriteria = newList();
		for (final SearchCriterion childCriterion : searchCriterion.getCriteria())
		{
			convertedCriteria.add(convertTree(childCriterion));
		}

		final List<DetachedCriteria> convertedQueries = newList();
		for (final SearchQuery childQuery : searchCriterion.getQueries())
		{
			final GenericCriteria convertedQuery = QueryBuilderHibernateImpl.convert(
					CriteriaType.SUB_CRITERIA, domainClassPackage, sessionFactory,
					childQuery);
			// Copy all root query aliases to sub-queries? Right now letting them have
			// their own
			// addAliases(searchQuery, convertedQuery);
			convertedQueries.add(convertedQuery.getHibernateDetachedCriteria());
		}

		// Now convert the parent
		return CriterionBuilderHibernateImpl.convert(searchCriterion, convertedCriteria,
				convertedQueries, classMetadata);
	}

	/**
	 * Apply sort orders of the search criteria to the Hibernate criteria.
	 */
	private void addSortOrder()
	{
		for (final SortCriterion criterion : searchQuery.getSortCriteria())
		{
			final SortType sortType = criterion.getSortType();
			switch (sortType)
			{
				case ASCENDING:
				{
					destination.addOrder(asc(criterion.getPropertyName()));
					break;
				}

				case DESCENDING:
				{
					destination.addOrder(desc(criterion.getPropertyName()));
					break;
				}

				default:
				{
					throw new BusinessRuleException(unsupportedMessage(sortType));
				}
			}
		}
	}

	/**
	 * Adds a {@link ResultTransformer} to the {@link #destination} that makes it
	 * distinct.
	 */
	private void addDistinct()
	{
		destination.setResultTransformer(DISTINCT_ROOT_ENTITY);
	}

	/**
	 * Set result set limits on {@link #destination}.
	 */
	private void setLimits()
	{
		if (searchQuery.getFirstResult() != null)
		{
			destination.setFirstResult(searchQuery.getFirstResult().intValue());
		}
		if (searchQuery.getMaxResults() != null)
		{
			destination.setMaxResults(searchQuery.getMaxResults().intValue());
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Create Hibernate search criteria for a root class <T> root entity type
	 * 
	 * @param criteriaType
	 *            criteria type (main criteria/detached criteria=sub-criteria)
	 * @param clazz
	 *            root entity type
	 * @return empty hibernate search criteria for root entity
	 */
	private GenericCriteria createCriteria(
			final Class<? extends PersistentEntity<?>> clazz)
	{
		return GenericCriteriaFactory.criteria(criteriaType, clazz,
				sessionFactory.getCurrentSession());
	}
}
