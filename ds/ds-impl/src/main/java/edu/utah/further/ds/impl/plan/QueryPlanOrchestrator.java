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
package edu.utah.further.ds.impl.plan;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * A query plan orchestrator is dispatcher of {@link RequestProcessor}'s defined by a
 * {@link RequestHandler}. Each implementation is responsible for defining how a query
 * plan is dispatched (linearly, branched, etc) and what should be returned from execution
 * (typically the result).
 * 
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
 * @version Sep 29, 2009
 */
public interface QueryPlanOrchestrator
{
	// ========================= METHODS ============================

	/**
	 * Executes a query plan as specified by implementation.
	 * 
	 * @param <T>
	 *            the type that will be returned
	 * @param plan
	 *            the query plan to execute
	 * @param executionRequest
	 *            the request object used for inputs and outputs
	 * @return an object of type T
	 */
	<T> T execute(RequestHandler plan, ChainRequest executionRequest);
}
