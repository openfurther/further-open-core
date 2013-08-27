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
package edu.utah.further.core.data.hibernate.hql;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Implements the builder pattern to construct an HQL query from snippets that represent
 * the main query and selection criteria.
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
 * @version May 29, 2009
 */
public final class HqlQueryBuilderImpl implements HqlQueryBuilder
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(HqlQueryBuilderImpl.class);

	/**
	 * Add this string before the first criterion only.
	 */
	private static final String CRITERION_PREFIX = " where ";

	/**
	 * Add this string before every criterion except the first one.
	 */
	private static final String CRITERION_DELIMITER = " and ";

	// ========================= FIELDS ====================================

	/**
	 * Keeps track of progress while building the HQL query string.
	 */
	private StringBuilder hqlBuilder = StringUtil.newStringBuilder();

	/**
	 * Keeps track of parametric substitution entities in the HQL query.
	 */
	private final Map<String, PersistentEntity<?>> entitiesToSet = newMap();

	/**
	 * A guard that keeps track of whether HQL query was already appended.
	 */
	private boolean appendedQuery = false;

	/**
	 * Keeps track of criteria state: whether this is the first criterion or not.
	 */
	private boolean firstCriterion = true;

	// ========================= METHODS ===================================

	/**
	 * Append the main query.
	 *
	 * @param query
	 *            HQL query string
	 * @return this object, for method call chaining
	 * @see edu.utah.further.core.data.hibernate.hql.HqlQueryBuilder#appendQuery(java.lang.String)
	 */
	@Override
	public HqlQueryBuilder appendQuery(final String query)
	{
		// Update flags
		if (appendedQuery)
		{
			throw new ApplicationException("Cannot append more than one query");
		}
		appendedQuery = true;

		// Prepend the builder with the query string
		hqlBuilder = new StringBuilder(query).append(hqlBuilder);
		return this;
	}

	/**
	 * Append a <code>WHERE</code>-clause criterion.
	 *
	 * @param criterion
	 *            <code>WHERE</code>-clause criterion
	 * @return this object, for method call chaining
	 * @see edu.utah.further.core.data.hibernate.hql.HqlQueryBuilder#appendCriterion(java.lang.String)
	 */
	@Override
	public HqlQueryBuilder appendCriterion(final String criterion)
	{
		// Append criterion snippet
		hqlBuilder
				.append(firstCriterion ? CRITERION_PREFIX : CRITERION_DELIMITER)
				.append("(")
				.append(criterion)
				.append(")");
		// Update flags
		if (firstCriterion)
		{
			firstCriterion = false;
		}

		return this;
	}

	/**
	 * Add a parametric entity entry.
	 *
	 * @param name
	 *            entry name in the query
	 * @param object
	 *            persistent entity
	 * @return this object, for method call chaining
	 */
	@Override
	public HqlQueryBuilder setEntity(final String name, final PersistentEntity<?> object)
	{
		entitiesToSet.put(name, object);
		return this;
	}

	/**
	 * Produce the HQL query.
	 *
	 * @param session
	 *            Hibernate session
	 * @return HQL query
	 * @see edu.utah.further.core.data.hibernate.hql.HqlQueryBuilder#toHqlQuery(org.hibernate.Session)
	 */
	@Override
	public Query toHqlQuery(final Session session)
	{
		final Query query = session.createQuery(hqlBuilder.toString());
		for (final Map.Entry<String, PersistentEntity<?>> entry : entitiesToSet
				.entrySet())
		{
			query.setEntity(entry.getKey(), entry.getValue());
		}
		return query;
	}

	// ========================= PRIVATE METHODS ===========================

}
