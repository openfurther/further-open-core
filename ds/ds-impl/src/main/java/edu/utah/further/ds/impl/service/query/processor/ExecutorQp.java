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
package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static edu.utah.further.fqe.ds.api.to.QueryContextToImpl.newCopy;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.Executor;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Query execution processor.
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
 * @version Apr 21, 2010
 */
public class ExecutorQp extends AbstractDelegatingUtilityProcessor<Executor>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ExecutorQp.class);

	// ========================= Impl: RequestProcessor ====================
	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext preQueryContext = request.getAttribute(QUERY_CONTEXT);
		// FUR-1313: clear the query's maxResults setting; we may have more physical
		// entities than distinct logical entities, so set maxResults instead on the
		// paging loop controller below
		final Integer maxResults = preQueryContext.getQuery().getMaxResults();
		preQueryContext.getQuery().setMaxResults(null);
		
		final Map<String, Object> attributes = request.getAttributes();
		
		// Execute query
		final Object result = getDelegate().execute(preQueryContext,
				attributes);
		
		Validate.notNull(result);
		
		// Get the latest QC after execution, set the result in the QueryContext
		
		request.setAttributes(attributes);
		
		final QueryContext postQueryContext = request.getAttribute(QUERY_CONTEXT);
		final ResultContext resultContext = postQueryContext.getResultContext();
		// Reset number of records; will be set by post-execution processors
		postQueryContext.setNumRecords(0);

		resultContext.setResult(result);

		// Save results in the chain request
		final QueryContextToImpl queryContext = newCopy(postQueryContext);
		request.setAttribute(QUERY_CONTEXT, queryContext);
		final Object qcResult = queryContext.getResultContext().getResult();
		request.setAttribute(QUERY_RESULT, qcResult);

		// Create a paging loop controller and set its maxResults fields using the user's
		// specification in the QC
		final PagingLoopController controller = new PagingLoopController();
		if (maxResults != null)
		{
			if (log.isTraceEnabled())
			{
				log.trace("Setting paging loop controller max results to " + maxResults);
			}
			controller.setMaxResults(maxResults.intValue());
		}
		request.setAttribute(AttributeName.PAGING_LOOP_CONTROLLER, controller);

		// Return failure code obtained from the delegate
		return postQueryContext.isFailed();
	}

	// ========================= PRIVATE METHODS ===========================
}
