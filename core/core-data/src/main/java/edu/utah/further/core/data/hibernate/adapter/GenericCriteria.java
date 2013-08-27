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
package edu.utah.further.core.data.hibernate.adapter;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

/**
 * A common-denominator interface of Hibernate <code>Criteria</code> and
 * <code>DetachedCriteria</code>. Also uses some enumerated types instead of Hibernate
 * static constants, which is a deprecated Java design pattern.
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
 * @version Jul 21, 2010
 */
public interface GenericCriteria
{
	// ========================= METHODS - Criteria API ====================

	/**
	 * Get the alias of the entity encapsulated by this criteria instance.
	 *
	 * @return The alias for the encapsulated entity.
	 */
	String getAlias();

	/**
	 * Used to specify that the query results will be a projection (scalar in nature).
	 * Implicitly specifies the {@link #PROJECTION} result transformer.
	 * <p/>
	 * The individual components contained within the given {@link Projection projection}
	 * determines the overall "shape" of the query result.
	 *
	 * @param projection
	 *            The projection representing the overall "shape" of the query results.
	 * @return this (for method chaining)
	 */
	GenericCriteria setProjection(Projection projection);

	/**
	 * Add a {@link Criterion restriction} to constrain the results to be retrieved.
	 *
	 * @param criterion
	 *            The {@link Criterion criterion} object representing the restriction to
	 *            be applied.
	 * @return this (for method chaining)
	 */
	GenericCriteria add(Criterion criterion);

	/**
	 * Add an {@link Order ordering} to the result set.
	 *
	 * @param order
	 *            The {@link Order order} object representing an ordering to be applied to
	 *            the results.
	 * @return this (for method chaining)
	 */
	GenericCriteria addOrder(Order order);

	/**
	 * Specify an association fetching strategy for an association or a collection of
	 * values.
	 *
	 * @param associationPath
	 *            a dot seperated property path
	 * @param mode
	 *            The fetch mode for the referenced association
	 * @return this (for method chaining)
	 */
	GenericCriteria setFetchMode(String associationPath, FetchMode mode);

	/**
	 * Set the lock mode of the current entity
	 *
	 * @param lockMode
	 *            The lock mode to be applied
	 * @return this (for method chaining)
	 */
	GenericCriteria setLockMode(LockMode lockMode);

	/**
	 * Set the lock mode of the aliased entity
	 *
	 * @param alias
	 *            The previously assigned alias representing the entity to which the given
	 *            lock mode should apply.
	 * @param lockMode
	 *            The lock mode to be applied
	 * @return this (for method chaining)
	 */
	GenericCriteria setLockMode(String alias, LockMode lockMode);

	/**
	 * NOTE THAT THE ARGUMENTS ARE REVERSED WITH RESPECT TO THE HIBERNATE API!!!
	 * <p>
	 * Join an association, assigning an alias to the joined association.
	 * <p/>
	 * Functionally equivalent to {@link #createAlias(String, String, int)} using
	 * {@link #INNER_JOIN} for the joinType.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @param alias
	 *            The alias to assign to the joined association (for later reference).
	 * @return this (for method chaining)
	 */
	GenericCriteria addAlias(String alias, String associationPath);

	/**
	 * NOTE THAT THE FIRST TWO ARGUMENTS ARE REVERSED WITH RESPECT TO THE HIBERNATE API!!!
	 * <p>
	 * Join an association using the specified join-type, assigning an alias to the joined
	 * association.
	 * <p/>
	 * The joinType is expected to be one of {@link #INNER_JOIN} (the default),
	 * {@link #FULL_JOIN}, or {@link #LEFT_JOIN}.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @param alias
	 *            The alias to assign to the joined association (for later reference).
	 * @param joinType
	 *            The type of join to use.
	 * @return this (for method chaining)
	 */
	GenericCriteria addAlias(String alias, String associationPath, int joinType);

	/**
	 * Create a new <tt>Criteria</tt>, "rooted" at the associated entity.
	 * <p/>
	 * Functionally equivalent to {@link #createCriteria(String, int)} using
	 * {@link #INNER_JOIN} for the joinType.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @return the created "sub criteria"
	 */
	GenericCriteria createCriteria(String associationPath);

	/**
	 * Create a new <tt>Criteria</tt>, "rooted" at the associated entity, using the
	 * specified join type.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @param joinType
	 *            The type of join to use.
	 * @return the created "sub criteria"
	 */
	GenericCriteria createCriteria(String associationPath, int joinType);

	/**
	 * Create a new <tt>Criteria</tt>, "rooted" at the associated entity, assigning the
	 * given alias.
	 * <p/>
	 * Functionally equivalent to {@link #createCriteria(String, String, int)} using
	 * {@link #INNER_JOIN} for the joinType.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @param alias
	 *            The alias to assign to the joined association (for later reference).
	 * @return the created "sub criteria"
	 */
	GenericCriteria createCriteria(String associationPath, String alias);

	/**
	 * Create a new <tt>Criteria</tt>, "rooted" at the associated entity, assigning the
	 * given alias and using the specified join type.
	 *
	 * @param associationPath
	 *            A dot-seperated property path
	 * @param alias
	 *            The alias to assign to the joined association (for later reference).
	 * @param joinType
	 *            The type of join to use.
	 * @return the created "sub criteria"
	 */
	GenericCriteria createCriteria(String associationPath, String alias, int joinType);

	/**
	 * Set a strategy for handling the query results. This determines the "shape" of the
	 * query result.
	 *
	 * @param resultTransformer
	 *            The transformer to apply
	 * @return this (for method chaining)
	 *
	 * @see #ROOT_ENTITY
	 * @see #DISTINCT_ROOT_ENTITY
	 * @see #ALIAS_TO_ENTITY_MAP
	 * @see #PROJECTION
	 */
	GenericCriteria setResultTransformer(ResultTransformer resultTransformer);

	/**
	 * Set a limit upon the number of objects to be retrieved.
	 *
	 * @param maxResults
	 *            the maximum number of results
	 * @return this (for method chaining)
	 */
	GenericCriteria setMaxResults(int maxResults);

	/**
	 * Set the first result to be retrieved.
	 *
	 * @param firstResult
	 *            the first result to retrieve, numbered from <tt>0</tt>
	 * @return this (for method chaining)
	 */
	GenericCriteria setFirstResult(int firstResult);

	/**
	 * Set a fetch size for the underlying JDBC query.
	 *
	 * @param fetchSize
	 *            the fetch size
	 * @return this (for method chaining)
	 *
	 * @see java.sql.Statement#setFetchSize
	 */
	GenericCriteria setFetchSize(int fetchSize);

	/**
	 * Set a timeout for the underlying JDBC query.
	 *
	 * @param timeout
	 *            The timeout value to apply.
	 * @return this (for method chaining)
	 *
	 * @see java.sql.Statement#setQueryTimeout
	 */
	GenericCriteria setTimeout(int timeout);

	/**
	 * Enable caching of this query result, provided query caching is enabled for the
	 * underlying session factory.
	 *
	 * @param cacheable
	 *            Should the result be considered cacheable; default is to not cache
	 *            (false).
	 * @return this (for method chaining)
	 */
	GenericCriteria setCacheable(boolean cacheable);

	/**
	 * Set the name of the cache region to use for query result caching.
	 *
	 * @param cacheRegion
	 *            the name of a query cache region, or <tt>null</tt> for the default query
	 *            cache
	 * @return this (for method chaining)
	 *
	 * @see #setCacheable
	 */
	GenericCriteria setCacheRegion(String cacheRegion);

	/**
	 * Add a comment to the generated SQL.
	 *
	 * @param comment
	 *            a human-readable string
	 * @return this (for method chaining)
	 */
	GenericCriteria setComment(String comment);

	/**
	 * Override the flush mode for this particular query.
	 *
	 * @param flushMode
	 *            The flush mode to use.
	 * @return this (for method chaining)
	 */
	GenericCriteria setFlushMode(FlushMode flushMode);

	/**
	 * Override the cache mode for this particular query.
	 *
	 * @param cacheMode
	 *            The cache mode to use.
	 * @return this (for method chaining)
	 */
	GenericCriteria setCacheMode(CacheMode cacheMode);

	/**
	 * Get the results.
	 *
	 * @return The list of matched query results.
	 */
	<T> List<T> list();

	/**
	 * Get the results as an instance of {@link ScrollableResults}
	 *
	 * @return The {@link ScrollableResults} representing the matched query results.
	 */
	ScrollableResults scroll();

	/**
	 * Get the results as an instance of {@link ScrollableResults} based on the given
	 * scroll mode.
	 *
	 * @param scrollMode
	 *            Indicates the type of underlying database cursor to request.
	 * @return The {@link ScrollableResults} representing the matched query results.
	 */
	ScrollableResults scroll(ScrollMode scrollMode);

	/**
	 * Convenience method to return a single instance that matches the query, or null if
	 * the query returns no results.
	 *
	 * @return the single result or <tt>null</tt> @ if there is more than one matching
	 *         result
	 */
	<T> T uniqueResult();

	// ========================= METHODS ===================================

	/**
	 * Return the criteria type.
	 *
	 * @return the criteria type
	 */
	CriteriaType getType();

	/**
	 * Return the underlying Hibernate Criteria object, if this object's type is
	 * {@value CriteriaType#CRITERIA}. Otherwise, returns <code>null</code>.
	 *
	 * @return underlying Hibernate Criteria object
	 */
	Criteria getHibernateCriteria();

	/**
	 * Return the underlying Hibernate detached Criteria object, if this object's type is
	 * {@value CriteriaType#SUB_CRITERIA}. Otherwise, returns <code>null</code>.
	 *
	 * @return underlying Hibernate detached Criteria object
	 */
	DetachedCriteria getHibernateDetachedCriteria();
}
