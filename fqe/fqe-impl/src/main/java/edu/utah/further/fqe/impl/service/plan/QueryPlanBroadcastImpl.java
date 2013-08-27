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

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.math.schedule.JobRunnerNotifier;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * A plan of a broadcast query (release 0.7-M2): the same query is sent in parallel to all
 * active data sources.
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
final class QueryPlanBroadcastImpl extends AbstractQueryPlan
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryPlanBroadcastImpl.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a plan.
	 * 
	 * @param notifier
	 *            runs our query jobs; we get notifications on them
	 * @param queryContext
	 *            federated query contexts whose plan this is
	 */
	public QueryPlanBroadcastImpl(final JobRunnerNotifier<QueryJob> notifier,
			final QueryContext queryContext)
	{
		super(notifier, queryContext);
		if (log.isDebugEnabled())
		{
			log.debug("Creating broadcast plan for " + queryContext);
		}
	}

	// ========================= IMPLEMENTATION: AbstractJobScheduler ======

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onStart()
	 */
	@Override
	public void onSchedulerStart()
	{
		// Trigger the federated query flow that broadcasts the same query to all
		// listening DS's (no dataSourceId is set ==> everyone gets the QC)
		final QueryContextTo qcToSend = QueryContextToImpl.newCopy(queryContext);
		notifier.startJob(new QueryJob(qcToSend));

		// This plan is now done!
		stop();
	}

	// ========================= PRIVATE METHODS ===========================
}
