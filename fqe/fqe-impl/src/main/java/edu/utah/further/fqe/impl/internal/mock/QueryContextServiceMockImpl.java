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
package edu.utah.further.fqe.impl.internal.mock;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.StatusMetaDataToImpl;

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
@Implementation
@Mock
@Component("queryContextService")
public class QueryContextServiceMockImpl implements QueryContextService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(QueryContextServiceMockImpl.class);

	/**
	 * Constant current status for testing.
	 */
	public static final String CURR_STATUS = "Current Status";

	// ========================= IMPLEMENTATION: QueryContextService =======

	/**
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#stop(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void stop(final QueryContext queryContext)
	{
		// Method stub
	}

	/**
	 * /**
	 *
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#delete(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void delete(final QueryContext queryContext)
	{
		// Method stub
	}

	/**
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#deleteAll()
	 */
	@Override
	public void deleteAll()
	{
		// Method stub
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAll()
	 */
	@Override
	public List<QueryContext> findAll()
	{
		return CollectionUtil.newList();
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
		final QueryContext qc = QueryContextToImpl.newInstance(1);
		TimeService.fixSystemTime(10000);
		qc.setStaleDateTime(TimeService.getDate());
		final List<QueryContext> staleQueries = newList();
		staleQueries.add(qc);
		return staleQueries;
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param queryId
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findById(java.lang.Long)
	 */
	@Override
	public QueryContext findById(final Long queryId)
	{
		final QueryContextTo context = QueryContextToImpl
				.newInstance(queryId.longValue());
		context.setState(QueryState.SUBMITTED);
		return context;
	}

	/**
	 * @param originId
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findQueryContextWithOriginId(java.lang.String)
	 */
	@Override
	public QueryContext findQueryContextWithOriginId(final Long originId)
	{
		// FQE QID = 0, origin ID = originId
		final QueryContextTo context = QueryContextToImpl.newInstance(0l);
		context.setOriginId(originId);
		context.setState(QueryState.SUBMITTED);
		return context;
	}

	/**
	 * @param childQueryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findParent(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext findParent(final QueryContext childQueryContext,
			final boolean forceReload)
	{
		return (childQueryContext == null) ? null : childQueryContext.getParent();
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
		// Method stub
		return null;
	}

	/**
	 * @param parent
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findCompletedChildren(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public List<QueryContext> findCompletedChildren(final QueryContext parent)
	{
		// Method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#queue(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext queue(final QueryContext queryContext)
	{
		queryContext.queue();
		return queryContext;
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#update(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext update(final QueryContext queryContext)
	{
		return queryContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatus()
	 */
	@Override
	public List<StatusMetaData> findAllStatuses()
	{
		final Long specialId = new Long(314159L);
		return findAllStatuses(specialId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatus(java.lang
	 * .Long)
	 */
	@Override
	public List<StatusMetaData> findAllStatuses(final Long queryContextId)
	{
		final List<StatusMetaData> statuses = newList();
		statuses.add(newStatusMetaData(queryContextId, 100L, "Status 1"));
		statuses.add(newStatusMetaData(queryContextId, 200L, "Status 2"));
		return statuses;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.api.service.query.QueryContextService#findStatusById(java.
	 * lang.Long)
	 */
	@Override
	public StatusMetaData findCurrentStatusById(final Long queryContextId)
	{
		return newStatusMetaData(queryContextId, 100L, CURR_STATUS);
	}

	/**
	 * @param date
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findAllStatusesLaterThan(java.util.Date)
	 */
	@Override
	public List<StatusMetaData> findAllStatusesLaterThan(final Date date)
	{
		// These will always have dates > than what will be tested
		return findAllStatuses();
	}
	
	@Override
	public void deleteByUser(final String userId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<QueryContext> findAllByUser(final String userId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= PRIVATE METHODS ===========================

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#findChildrenQueryIdsByParent(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */

	/**
	 * @param id
	 * @param statusMetaData
	 */
	private void setId(final StatusMetaData statusMetaData, final Long id)
	{
		ReflectionUtil.setField(statusMetaData, "id", id);
	}

	/**
	 * @param id
	 * @param statusId
	 * @param status
	 * @return
	 */
	private StatusMetaData newStatusMetaData(final Long id, final long statusId,
			final String status)
	{
		final StatusMetaData statusMetaData1 = new StatusMetaDataToImpl();
		statusMetaData1.setStatus(status);
		final QueryContext qc = QueryContextToImpl.newInstance(id.longValue());
		statusMetaData1.setQueryContext(qc);
		setId(statusMetaData1, new Long(statusId));
		return statusMetaData1;
	}
}