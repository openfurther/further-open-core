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

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.math.schedule.JobEvent;
import edu.utah.further.core.math.schedule.JobRunnerNotifier;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.impl.service.route.CommandRunners;

/**
 * Manages federated query execution plans.
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
@Service("queryPlanService")
public class QueryPlanServiceImpl implements QueryPlanService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryPlanServiceImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * A deocrator of this object that can be observed by query plans.
	 */
	private final JobRunnerNotifier<QueryJob> notifier = new JobRunnerNotifier<>(
			this);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles {@link QueryContext} DAO operations and searches.
	 */
	@Autowired
	private QueryContextService queryContextService;
	
	/**
	 * The producer template for sending messages
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	// ========================= IMPLEMENTATION: QueryPlanService ==========

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.impl.service.plan.QueryPlanService#createPlan(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void createPlan(final QueryContext federatedQueryContext)
	{
		final Long queryId = federatedQueryContext.getId();
		if (log.isDebugEnabled())
		{
			log.debug("Creating plan of query ID " + queryId);
		}
		final QueryPlan plan = newPlan(federatedQueryContext);
		// Trigger plan execution
		plan.start();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.impl.service.plan.QueryPlanService#updatePlan(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void updatePlan(final QueryContext child)
	{
		// Update plan if necessary
		final Long queryId = getParentQueryId(child);
		if (queryId == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("This QC does not have a child, ignoring plan update " + child);
			}
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Updating plan based on child status message " + child);
			}
			if (child.isFailed())
			{
				stopPlan(queryId);
			}
			else if (child.isInFinalState())
			{
				notifier.completeJob(new QueryJob(child));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.impl.service.plan.QueryPlanService#stopPlan(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void stopPlan(final Long federatedQueryId)
	{
		// Send a notification to the query plan corresponding FQC ID so
		// that it can stop itself
		final QueryContext dummyQc = QueryContextToImpl.newInstance(federatedQueryId
				.longValue());
		notifier.notifyObservers(new QueryJob(dummyQc), JobEvent.Type.SCHEDULER_COMPLETED);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.math.schedule.JobRunner#startJob(java.lang.Object)
	 */
	@Override
	public void startJob(final QueryJob job)
	{
		if (log.isDebugEnabled())
		{
			log.debug(StringUtil.centerWithSpacePadSize("Starting job " + job, 20, "#"));
		}

		// Feed job to camel
		CommandRunners.dataQuery(job.getDelegate(), producerTemplate).run();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.math.schedule.JobRunner#completeJob(java.lang.Object)
	 */
	@Override
	public void completeJob(final QueryJob job)
	{
		if (log.isDebugEnabled())
		{
			log.debug(StringUtil.centerWithSpacePadSize("Completed job " + job, 20, "#"));
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param queryId
	 */
	private Long getParentQueryId(final QueryContext child)
	{
		final QueryContext parent = queryContextService.findParent(child, false);
		return (parent == null) ? null : parent.getId();
	}

	/**
	 * A factory method that instantiates the plan type appropriate for a query.
	 *
	 * @param queryContext
	 *            federated query context
	 * @return query plan, in initial state (before start)
	 */
	private QueryPlan newPlan(final QueryContext queryContext)
	{
		// The plan instrumentation piece of a QC; not to be confused with the query's
		// execution plan (QueryPlan). The latter is constructed from the former.
		final Plan planStructure = queryContext.getPlan();
		final Plan.Type planType = (planStructure == null) ? Plan.Type.BROADCAST
				: planStructure.getType();
		switch (planType)
		{
			case BROADCAST:
			{
				return new QueryPlanBroadcastImpl(notifier, queryContext);
			}

			case PHASED:
			{
				return new QueryPlanPhasedImpl(notifier, queryContext);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"No plan is supported for this query " + queryContext);
			}
		}
	}
}
