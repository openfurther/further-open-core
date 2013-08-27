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
package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.constant.Constants.Scope.PROTOTYPE;
import static edu.utah.further.ds.api.util.AttributeName.DS_TYPE;
import static edu.utah.further.ds.api.util.AttributeName.SEARCH_QUERY;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.ds.api.executor.QueryExecution;
import edu.utah.further.ds.api.service.query.logic.Executor;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.ds.api.util.DatasourceType;
import edu.utah.further.ds.impl.executor.QueryExecutionFactory;
import edu.utah.further.ds.impl.plan.QueryPlanOrchestrator;
import edu.utah.further.ds.impl.plan.SimpleQueryPlanOrchestrator;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Executes a query using QUEST (Query Execution Strategy). This execution strategy is a
 * reusable component which can execute varying types of data sources. It excepts that the
 * {@link DatasourceType} be set during query initialization.
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
 * @version Apr 23, 2010
 */
@Service("questExecution")
@Scope(PROTOTYPE)
public class ExecutorQuestImpl implements Executor
{
	// ========================= CONSTANTS ======================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ExecutorQuestImpl.class);

	// ========================= DEPENDENCIES ===================================

	/**
	 * Determines our type of execution based on the data source type.
	 */
	@Autowired
	private QueryExecutionFactory executionFactory;

	// ========================= IMPL: Executor =================================

	/**
	 * @param <T>
	 * @param queryContext
	 * @param attributes
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.Executor#execute(edu.utah.further.fqe.ds.api.domain.QueryContext,
	 *      java.util.Map)
	 */
	@Override
	public <T> T execute(final QueryContext queryContext,
			final Map<String, Object> attributes)
	{
		// Utilize the execution factory to retrieve our execution type
		final DatasourceType dsType = (DatasourceType) attributes.get(DS_TYPE.getLabel());
		Validate.notNull(dsType,
				"A data source type is required for query execution, ensure one is set under attribute "
						+ DS_TYPE.getLabel());

		// All query executions will need the search query, inject it by default
		attributes.put(SEARCH_QUERY.getLabel(), queryContext.getQuery());

		// Get the execution type
		final QueryExecution execution = executionFactory.getInstance(dsType);

		// Might need a factory for the type of QueryPlanOrchestrator, using simple for
		// now
		final QueryPlanOrchestrator orchestrator = new SimpleQueryPlanOrchestrator();
		final ChainRequest request = new ChainRequestImpl(new AttributeContainerImpl(
				attributes));
		final T result = orchestrator.<T> execute(execution.getQueryPlan(attributes),
				request);
		
		// Clean up temporary attribute used to store result so their is no dependency on
		// QueryContext
		request.setAttribute(AttributeName.QUERY_RESULT, null);
		
		attributes.clear();
		attributes.putAll(request.getAttributes());
		
		// Get the plan and the request from the execution type and execute it.
		return result;
	}
}
