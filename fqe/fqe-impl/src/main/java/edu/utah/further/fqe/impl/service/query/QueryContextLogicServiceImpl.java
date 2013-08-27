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

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.fqe.api.service.query.QueryContextLogicService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Default implementation of the query logic operation service.
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
 * @version Oct 26, 2010
 */
@Service("queryContextLogicService")
@Transactional
public class QueryContextLogicServiceImpl implements QueryContextLogicService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextLogicServiceImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * {@link QueryContext} CRUD service.
	 */
	@Autowired
	private QueryContextService qcService;

	// ========================= IMPLEMENTATION: QueryContextLogicService ==

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextService#parentExists(QueryContext)
	 */
	@Override
	public boolean parentExists(final QueryContext queryContext)
	{
		final QueryContextTo queryContextTo = QueryContextToImpl.newCopy(queryContext);

		// Parent
		if (queryContextTo.getParentId() == null)
		{
			return false;
		}

		// We are a child
		final Long parentId = queryContextTo.getParentId();
		final QueryContext parent = qcService.findById(parentId);

		final boolean parentExists = (parent != null) ? true : false;

		if (log.isTraceEnabled())
		{
			log.trace("parentExists()=" + parentExists + " " + queryContextTo);
		}

		return parentExists;
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextLogicService#getParent(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext getParent(final QueryContext queryContext)
	{
		return queryContext.getParent();
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextLogicService#isCompleted(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public boolean isCompleted(final QueryContext queryContext)
	{
		final QueryContext reloaded = qcService.findById(queryContext.getId());
		final boolean isCompleted = (reloaded.getState() == QueryState.COMPLETED);
		if (log.isTraceEnabled())
		{
			log.trace("isCompleted()=" + isCompleted + " " + reloaded);
		}
		return isCompleted;
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextLogicService#isCompleted(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public boolean isParentNotCompleted(final QueryContext queryContext)
	{
		return !isCompleted(getParent(queryContext));
	}

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.QueryContextLogicService#isFinalState(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public boolean isFinalState(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Check if the query is in final state");
		}

		return queryContext.isInFinalState();
	}

	// ========================= PRIVATE METHODS ===========================
}
