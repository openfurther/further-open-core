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
package edu.utah.further.fqe.impl.service.query;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.query.domain.Relation.EQ;
import static edu.utah.further.core.query.domain.Relation.GT;
import static edu.utah.further.core.query.domain.Relation.LT;
import static edu.utah.further.core.query.domain.Relation.NE;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchEngine;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.impl.data.QueryContextDao;
import edu.utah.further.fqe.impl.domain.FqeDomainFactory;
import edu.utah.further.fqe.impl.domain.StatusMetaDataEntity;

/**
 * Hibernate implementation of the query context CRUD service.
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
 * @version Jan 28, 2009
 */
@Service("queryContextService")
@Transactional
public class QueryContextServiceImpl implements QueryContextService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextServiceImpl.class);

	/**
	 * Main entity's class.
	 */
	private static final Class<QueryContext> DOMAIN_CLASS = QueryContext.class;

	/**
	 * Main entity's class.
	 */
	private static final Class<StatusMetaDataEntity> STATUS_DOMAIN_CLASS = StatusMetaDataEntity.class;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates persistent entities. Its implementation is provided by the ds data
	 * implementation module.
	 */
	@Autowired
	private FqeDomainFactory fqeDomainFactory;

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	@Autowired
	@Qualifier("queryContextDao")
	private QueryContextDao queryContextDao;

	// ========================= IMPLEMENTATION: QueryContextService =======

	/**
	 * Persist a query context object to the database. This is useful for transient
	 * entities with embedded IDs that can be non-<code>null</code>.
	 * 
	 * @param queryContext
	 *            a transient entity or a transfer object
	 * @return persisted entity; if an entity was passed in, it is returned; if a transfer
	 *         object was passed in, a new entity is created, persisted and returned
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#queue(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public QueryContext queue(final QueryContext queryContext)
	{
		final QueryContext entity = copyIntoEntity(queryContext);
		entity.queue();
		if (entity.getState() != QueryState.QUEUED)
		{
			throw new ApplicationException("Expected query to be queued but instead was "
					+ entity.getState() + ". Aborting saving query in malformed state.");
		}
		dao.save(entity);
		if (log.isDebugEnabled())
		{
			log.debug("Queued query " + entity);
		}
		return entity;
	}

	/**
	 * Performs a persistent (if this is a transient object) or merge (if this is a
	 * persistent object) operation.
	 * 
	 * @param queryContext
	 *            a detached query context instance with state to be persisted/updated in
	 *            the database, or a transfer object
	 * @return persisted entity; if an entity was passed in, it is returned; if a transfer
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#update(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public QueryContext update(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Updating query " + queryContext);
		}
		// final QueryContext entity = copyIntoEntity(queryContext);

		// If this is a new, transient query, save it. Otherwise, update it with the
		// queryContext TO values
		QueryContext entity = dao.findByUniqueProperty(QueryContext.class, "executionId",
				queryContext.getExecutionId());
		if (log.isTraceEnabled())
		{
			log.trace("Query loaded from database: " + entity);
		}
		if (entity == null)
		{
			entity = saveNewQueryContext(queryContext);
		}
		else
		{
			if (isStaleQuery(queryContext, entity))
			{
				queryContext.setStale();
			}
			updateExistingQuery(entity, queryContext);
		}

		// // Update parent state since we are not cascading updates in the QC entity
		// final QueryContext parent = entity.getParent();
		// if ((parent != null) && (parent.getId() != null))
		// {
		// aggregationService.updateParent(entity);
		// }

		return entity;
	}

	/**
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#stop(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void stop(final QueryContext queryContext)
	{
		final QueryContext entity = findById(queryContext.getId());
		// Stop all running children
		final List<QueryContext> children = findChildren(entity);
		for (final QueryContext child : children)
		{
			if (child.isStarted())
			{
				child.stop();
			}
			dao.update(child);
		}

		// Stop this query
		entity.stop();
		dao.update(entity);
	}

	/**
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#delete(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void delete(final QueryContext queryContext)
	{
		final QueryContext entity = findById(queryContext.getId());
		final List<QueryContext> children = findChildren(entity);
		for (final QueryContext child : children)
		{
			dao.delete(child);
		}
		dao.delete(entity);
	}

	/**
	 * Delete all instances of the queryContext entity from database.
	 * 
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#deleteAll()
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void deleteAll()
	{
		// Stop all running federated queries
		final List<QueryContext> federatedQueryContexts = findAll();
		for (final QueryContext queryContext : federatedQueryContexts)
		{
			if ((queryContext.getParent() == null) && queryContext.isStarted())
			{
				stop(queryContext);
			}
		}

		// Delete all queries.
		dao.deleteAll(DOMAIN_CLASS);
	}

	/**
	 * Delete all queryContexts submitted by a specific user from database.
	 */

	@Override
	public void deleteByUser(final String userId)
	{
		final List<QueryContext> queryContextsByUser = findAllByUser(userId);
		// Stop all running queries
		for (final QueryContext queryContext : queryContextsByUser)
		{
			if ((queryContext.getParent() == null))
			{
				// This will also stop children queries
				if (queryContext.isStarted())
				{
					stop(queryContext);
				}
				final List<QueryContext> children = findChildren(queryContext);
				for (final QueryContext child : children)
				{
					dao.delete(child);
				}
				dao.delete(queryContext);
			}
		}

		// MySQLIntegrityConstraintViolationException because of the OnDelete=restrict on
		// the foreign key constraint of "parent"
		// queryContextDao.deleteByUser(userId);

	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAll()
	 */
	@Override
	public List<QueryContext> findAll()
	{
		return CollectionUtil.<QueryContext> newList(dao.findAll(DOMAIN_CLASS));
	}

	/**
	 * Find all queries from a user
	 */
	@Override
	public List<QueryContext> findAllByUser(final String userId)
	{
		return CollectionUtil.<QueryContext> newList(dao.findByProperty(DOMAIN_CLASS,
				"userId", userId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findStaleQueries(java
	 * .util.Date)
	 */
	@Override
	public List<QueryContext> findStaleQueries(final Date now)
	{
		final SearchCriterion criterion = createStaleQueriesSearchCriterion(now);
		return CollectionUtil.<QueryContext> newList(((SearchEngine) dao).search(
				DOMAIN_CLASS,
				SearchCriteria.query(criterion, DOMAIN_CLASS.getSimpleName())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findExecutingNotStaleQueries
	 * (java.util.Date)
	 */
	@Override
	public List<QueryContext> findExecutingNotStaleQueries(final Date now)
	{
		final SearchCriterion criterion = createExecutingNotStaleQueriesSearchCriterion(now);
		return CollectionUtil.<QueryContext> newList(((SearchEngine) dao).search(
				DOMAIN_CLASS,
				SearchCriteria.query(criterion, DOMAIN_CLASS.getSimpleName())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findChildren(edu.utah
	 * .further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public List<QueryContext> findChildren(final QueryContext parent)
	{
		return CollectionUtil.<QueryContext> newList(dao.findByProperty(DOMAIN_CLASS,
				"parent", parent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findChildren(edu.utah
	 * .further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public List<QueryContext> findCompletedChildren(final QueryContext parent)
	{
		return findChildrenByState(parent, QueryState.COMPLETED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findFailedChildren(edu
	 * .utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public List<QueryContext> findFailedChildren(final QueryContext parent)
	{
		return findChildrenByState(parent, QueryState.FAILED);
	}

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findById(java.lang.Long)
	 */
	@Override
	public QueryContext findById(final Long id)
	{
		return dao.findById(DOMAIN_CLASS, id);
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
		return queryContextDao.findQueryContextWithOriginId(originId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findChildrenQueryIdsByParent
	 * (edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public List<String> findChildrenQueryIdsByParent(final QueryContext parent)
	{
		final List<QueryContext> children = findCompletedChildren(parent);
		final List<String> queryIds = newList();
		for (final QueryContext queryContext : children)
		{
			queryIds.add(queryContext.getExecutionId());
		}
		return queryIds;
	}

	/**
	 * Load a DQC's parent QC.
	 * 
	 * @param child
	 *            child query context
	 * @param forceReload
	 *            if true, forces a reload of the parent entity from the database
	 * @return parent entity, freshly loaded from the database
	 */
	@Override
	public QueryContext findParent(final QueryContext child, final boolean forceReload)
	{
		final QueryContext parent = child.getParent();
		if (forceReload || (parent == null))
		{
			final QueryContext reloadedChild = findById(child.getId());
			return reloadedChild.getParent();
		}
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatus()
	 */
	@Override
	public List<StatusMetaData> findAllStatuses()
	{
		return CollectionUtil.<StatusMetaData> newList(dao.findAll(STATUS_DOMAIN_CLASS));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatus(long)
	 */
	@Override
	public List<StatusMetaData> findAllStatuses(final Long id)
	{
		final QueryContext queryContext = findById(id);
		return (queryContext == null) ? CollectionUtil.<StatusMetaData> newList()
				: queryContext.getStatuses();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findStatusById(java.
	 * lang.Long)
	 */
	@Override
	public StatusMetaData findCurrentStatusById(final Long id)
	{
		/* This should work since we are in within a session */
		return findById(id).getCurrentStatus();
	}

	/**
	 * @param date
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatusesLaterThan(java.util.Date)
	 */
	@Override
	public List<StatusMetaData> findAllStatusesLaterThan(final Date date)
	{
		return CollectionUtil.<StatusMetaData> newList(dao.findByPropertyGt(
				STATUS_DOMAIN_CLASS, StatusMetaData.PROPERTY_STATUS_DATE, date));
	}

	// ========================= GET/SET METHODS ===========================

	/**
	 * Set a new value for the dao property.
	 * 
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param queryContext
	 * @return
	 */
	private QueryContext copyIntoEntity(final QueryContext queryContext)
	{
		return queryContext.isPersistent() ? queryContext : fqeDomainFactory
				.newQueryContextEntity(queryContext);
	}

	/**
	 * @param queryContext
	 * @return
	 */
	private QueryContext saveNewQueryContext(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("New query, saving");
		}
		final QueryContext entity = copyIntoEntity(queryContext);
		if (!entity.isStarted() && !entity.isFailed())
		{
			entity.queue();
			if (entity.getState() != QueryState.QUEUED)
			{
				throw new ApplicationException(
						"Expected query to be queued but instead was "
								+ entity.getState()
								+ ". Aborting saving query in malformed state.");
			}
		}
		dao.save(entity);
		if (log.isDebugEnabled())
		{
			log.debug("Saved query " + entity);
		}
		return entity;
	}

	/**
	 * @param currentContext
	 * @param newContext
	 */
	private void updateExistingQuery(final QueryContext currentContext,
			final QueryContext newContext)
	{
		// Use the loaded newContext entity and not a fresh one (by calling
		// copyIntoEntity()) because it might already be associated with the persistent
		// session
		currentContext.copyFrom(newContext);
		if (log.isTraceEnabled())
		{
			log.trace("Existing query, updating " + currentContext + " with "
					+ newContext);
		}
		// Parent state is updated as well because we cascade updates in the QC entity
		dao.update(currentContext);
	}

	/**
	 * Decide whether a prospective query to be updated into the database is stale by
	 * examining its state, i.e. there exists a more recent version of this query in the
	 * database.
	 * 
	 * @param queryContext
	 * @param entity
	 * @return
	 */
	private boolean isStaleQuery(final QueryContext queryContext,
			final QueryContext entity)
	{
		// If query is stopped, reject any state changes
		return entity.isStopped() && (queryContext.getState() != entity.getState());
	}

	/**
	 * Private helper method to find children by a given state.
	 * 
	 * @param parent
	 * @param state
	 * @return
	 */
	private List<QueryContext> findChildrenByState(final QueryContext parent,
			final QueryState state)
	{
		final Map<String, Object> propertyCriteria = CollectionUtil.newMap();
		propertyCriteria.put("parent", parent);
		propertyCriteria.put("state", state);
		return CollectionUtil.<QueryContext> newList(dao.findByProperties(DOMAIN_CLASS,
				propertyCriteria));
	}

	/**
	 * Create DAO search criterion for finding stale queries.
	 * 
	 * @param now
	 *            current date to compare query stale dates with
	 * @return stale query search criterion
	 */
	private static SearchCriterion createStaleQueriesSearchCriterion(final Date now)
	{
		final SearchCriterion andcriterion = SearchCriteria
				.junction(SearchType.CONJUNCTION);
		andcriterion.addCriterion(simpleExpression(LT, "staleDateTime", now));
		andcriterion.addCriterion(simpleExpression(EQ, "isStale", Boolean.FALSE));
		final SearchCriterion orcriterion = SearchCriteria
				.junction(SearchType.CONJUNCTION);
		orcriterion.addCriterion(simpleExpression(NE, "state", QueryState.COMPLETED));
		orcriterion.addCriterion(simpleExpression(NE, "state", QueryState.STOPPED));
		andcriterion.addCriterion(orcriterion);
		return andcriterion;
	}

	/**
	 * Create DAO search criterion for finding queries which are EXECUTING but not stale
	 * 
	 * @param now
	 *            current date to compare query stale dates with
	 * @return stale query search criterion
	 */
	private static SearchCriterion createExecutingNotStaleQueriesSearchCriterion(
			final Date now)
	{
		final SearchCriterion criterion = SearchCriteria.junction(SearchType.CONJUNCTION);
		criterion.addCriterion(simpleExpression(GT, "staleDateTime", now));
		criterion.addCriterion(simpleExpression(EQ, "isStale", Boolean.FALSE));
		criterion.addCriterion(simpleExpression(EQ, "state", QueryState.EXECUTING));
		return criterion;
	}

}
