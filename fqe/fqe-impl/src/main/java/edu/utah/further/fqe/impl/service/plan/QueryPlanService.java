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
package edu.utah.further.fqe.impl.service.plan;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.math.schedule.JobRunner;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Manages federated query execution plans. Keeps track of all active plans: executes them
 * by stages, updates them based on data source status notifications, and disposes of them
 * when they are done.
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
 * @version Dec 14, 2010
 */
@Api
public interface QueryPlanService extends JobRunner<QueryJob>
{
	// ========================= CRUD METHODS ==============================

	/**
	 * Create an plan for an FQC and start executing it.
	 *
	 * @param federatedQueryContext
	 *            federated query context
	 */
	void createPlan(QueryContext federatedQueryContext);

	/**
	 * Updates an FQC plan based on a child query's status notification. Called by Camel.
	 *
	 * @param childQueryContext
	 *            a child query context of the FQC in question containing its latest
	 *            status. The FQC plan is updated according to this child's execution ID
	 *            and state
	 */
	void updatePlan(QueryContext childQueryContext);

	/**
	 * Manually stop a running FQC plan.
	 *
	 * @param queryId
	 *            federated query contexts ID
	 */
	void stopPlan(Long queryId);
}
