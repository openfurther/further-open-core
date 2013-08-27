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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.math.schedule.AbstractJobScheduler;
import edu.utah.further.core.math.schedule.JobEvent;
import edu.utah.further.core.math.schedule.JobRunnerNotifier;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * A base class of query plans.
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
abstract class AbstractQueryPlan extends AbstractJobScheduler<QueryJob> implements
		QueryPlan
{
	// ========================= FIELDS ====================================

	/**
	 * The federated query contexts whose plan this is.
	 */
	protected final QueryContext queryContext;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a plan.
	 *
	 * @param notifier
	 *            query job runner
	 * @param queryContext
	 *            federated query contexts whose plan this is
	 */
	public AbstractQueryPlan(final JobRunnerNotifier<QueryJob> notifier,
			final QueryContext queryContext)
	{
		super(notifier);
		this.queryContext = queryContext;
	}

	// ========================= IMPL: Object ==============================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append(
				"queryContextId", getQueryContextId()).toString();
	}

	// ========================= IMPL: JobScheduler ========================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.math.schedule.AbstractJobScheduler#onSchedulerCompleted(edu
	 * .utah.further.core.math.schedule.JobEvent)
	 */
	@Override
	public final void onSchedulerCompleted(final JobEvent<QueryJob> jobEvent)
	{
		if (getQueryContextId().equals(jobEvent.getEntity().getId()))
		{
			// Message was directed to us, stop this plan
			stop();
		}
	}

	// ========================= METHODS ===================================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.impl.service.plan.QueryPlan#getQueryContextId()
	 */
	@Override
	public Long getQueryContextId()
	{
		return queryContext.getId();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param executionRule
	 * @return
	 */
	protected static final QueryJob buildQueryJob(final QueryContext queryContext,
			final ExecutionRule executionRule)
	{
		final QueryContextToImpl executionQc = QueryContextToImpl.newCopy(queryContext);
		executionQc.setExecutionId(executionRule.getId());
		executionQc
				.setQuery(queryContext.getQueryByQid(executionRule.getSearchQueryId()));
		executionQc.setDataSourceId(executionRule.getDataSourceId());
		return new QueryJob(executionQc);
	}

	/**
	 * @param executionRule
	 */
	protected static void validateExecutionRule(final ExecutionRule executionRule)
	{
		ValidationUtil.validateNotNull("search query ID of execution rule "
				+ executionRule, executionRule.getSearchQueryId());
		ValidationUtil.validateNotNull("Execution ID of execution rule " + executionRule,
				executionRule.getId());
	}
}
