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
package edu.utah.further.ds.impl.executor.db.hibernate.criteria;

import static org.slf4j.LoggerFactory.getLogger;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.adapter.ScrollMode;
import edu.utah.further.ds.api.annotation.HibernateExecutor;
import edu.utah.further.ds.api.request.QueryExecutionRequest;

/**
 * Retrieves a {@link GenericCriteria} from the result of a previous
 * {@link RequestProcessor} , executes Hibernate's {@link GenericCriteria#list()} on that
 * {@link GenericCriteria} and places the results back in the execution chain.
 * 
 * This executor is not a final point of execution and can be used between other
 * {@link RequestProcessor}'s.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 1, 2009
 */
@Service("hibernateCriteriaScrollableResultsExecutor")
public class HibernateCriteriaScrollableResultsExecutor extends
		AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(HibernateCriteriaScrollableResultsExecutor.class);

	// ========================= IMPLEMENTATION: RequestProcessor ============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final QueryExecutionRequest executionRequest = new QueryExecutionRequest(request);
		final GenericCriteria criteria = executionRequest.getResult();
		final ScrollableResults results = getResultListFromHibernate(criteria);
		executionRequest.setResult(results);
		executionRequest.setStatus("Executed query @ " + TimeService.getDate());
		// Hibernate's ScrollableResults interface has no hasNext() or peek(), so go one
		// row forward (if exists) and then back all the way so that we don't miss the
		// first row in subsequent processors
		final boolean hasNext = results.next();
		if (hasNext)
		{
			results.previous();
		}
		return hasNext;
	}

	// ========================= PRIVATE METHODS =============================

	// TODO: replace by a template method wrapping a function pointer that executes a
	// Hibernate query.
	// This version should return a <T> to cover both List, other return. values
	//
	// Better yet: use AOP to wrap function pointers with an advice, and ruQn retval =
	// proceed() twice.
	/**
	 * Execute Hibernate criteria query and return a scrollable result set.
	 * 
	 * @param criteria
	 *            Hibernate criteria
	 * @return scrollable result set
	 * @see https://jira.chpc.utah.edu/browse/FUR-1274
	 */
	@HibernateExecutor
	private ScrollableResults getResultListFromHibernate(final GenericCriteria criteria)
	{
		ScrollableResults results = null;
		// Need to use INSENSITIVE scroll-mode per FUR-1300 and
		// http://www.coderanch.com/t/301684/JDBC/java/Unsupported-syntax-refreshRow
		final ScrollMode scrollMode = ScrollMode.SCROLL_INSENSITIVE;
		try
		{
			results = criteria.setCacheMode(CacheMode.IGNORE).scroll(scrollMode);
		}
		catch (final HibernateException e)
		{
			log.error("An exception occurred while scrolling results", e);
		}

		// Normally we won't know up-front the size of a streaming result set. But for
		// easier debugging of the subsequent paging sub-chain, print out the size of the
		// list because we know it already in this case.
		if (log.isDebugEnabled())
		{
			log.debug("Result set = " + StringUtil.getNullSafeToString(results));
		}
		return results;
	}
}
