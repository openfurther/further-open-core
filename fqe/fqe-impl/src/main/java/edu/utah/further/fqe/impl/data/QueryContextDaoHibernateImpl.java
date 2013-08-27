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
package edu.utah.further.fqe.impl.data;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.data.util.HibernateUtil;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A custom Hibernate DAO implementation for {@link QueryContext} CRUD operations.
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
 * @version Apr 26, 2010
 */
@Implementation
@Repository("queryContextDao")
public class QueryContextDaoHibernateImpl extends HibernateDaoSupport implements
		QueryContextDao
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextDaoHibernateImpl.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for a Spring DAO bean.
	 *
	 * @param sessionFactory
	 *            Hibernate session factory
	 */
	@Autowired
	public QueryContextDaoHibernateImpl(@Qualifier("sessionFactory")final SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	// ========================= IMPLEMENTATION: QueryContextDao ===========

	/**
	 * Return all federated query contexts (roots of query context trees).
	 *
	 * @return list of query contexts that don't have parents
	 * @see edu.utah.further.fqe.impl.data.QueryContextDao#findAllFederatedQueryContexts()
	 */
	@Override
	public List<QueryContext> findAllFederatedQueryContexts()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Finding all federated queries");
		}
		final List<QueryContext> queryContexts = HibernateUtil.findByCriteriaUsingHql(
				getHibernateTemplate(), dao.getEntityClass(QueryContext.class), "qc",
				true, "qc.parent is null");
		return queryContexts;
	}

	/**
	 * Delete all query contexts from a specific user.
	 *
	 * @param userId
	 * @see edu.utah.further.fqe.impl.data.QueryContextDao#deleteByUser(java.lang.String)
	 */
	@Override
	public void deleteByUser(final String userId)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Deleting all queries for user " + userId);
		}

		final int count = HibernateUtil.deleteByCriteriaUsingHql(getHibernateTemplate(),
				dao.getEntityClass(QueryContext.class), "qc", "qc.userId = ?", userId);

		if (log.isDebugEnabled())
		{
			log.debug(count + " queries from user " + userId + " deleted");
		}
	}

	/**
	 * Find a query by origin ID.
	 *
	 * @param originId
	 *            query origin ID (e.g. i2b2 ID)
	 * @return FQE query, or <code>null</code>, if not found
	 */
	@Override
	public QueryContext findQueryContextWithOriginId(final Long originId)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Finding query with origin ID " + originId);
		}
		final List<QueryContext> queryContexts = HibernateUtil.findByCriteriaUsingHql(
				getHibernateTemplate(), dao.getEntityClass(QueryContext.class), "qc",
				true, "qc.originId = ?", originId);
		// We should obtain a unique result
		return queryContexts.isEmpty() ? null : queryContexts.get(0);
	}

	// ========================= PRIVATE METHODS ===========================
}
