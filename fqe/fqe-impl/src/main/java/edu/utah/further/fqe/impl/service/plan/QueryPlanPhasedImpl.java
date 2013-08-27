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

import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.math.schedule.AbstractJobScheduler;
import edu.utah.further.core.math.schedule.JobRunnerNotifier;
import edu.utah.further.core.math.schedule.JobSchedulerGraphImpl;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.DependencyRule;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;

/**
 * A plan of a branched query (release 0.7-M4): a general dependency graph of queries is
 * sent to data sources according to the {@link QueryContext}'s plan section.
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
final class QueryPlanPhasedImpl extends AbstractQueryPlan
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryPlanPhasedImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * A delegate that schedules the query plan graph.
	 */
	private final AbstractJobScheduler<QueryJob> graphScheduler;

	/**
	 * Maps graph vertices to {@link QueryContext} fed to Camel during plan run.
	 */
	private final Map<String, QueryJob> executionIdToQc = CollectionUtil.newMap();

	/**
	 * Holds on to the dependency graph data structure. Useful for debugging.
	 */
	private final DirectedGraph<QueryJob, DefaultEdge> graph;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a plan.
	 * 
	 * @param notifier
	 *            runs our query jobs; we get notifications on them
	 * @param queryContext
	 *            federated query contexts whose plan this is
	 */
	public QueryPlanPhasedImpl(final JobRunnerNotifier<QueryJob> notifier,
			final QueryContext queryContext)
	{
		super(notifier, queryContext);
		this.graph = buildGraph();
		this.graphScheduler = new JobSchedulerGraphImpl<>(notifier, this.graph);
		if (log.isDebugEnabled())
		{
			log.debug("Creating phased plan for " + queryContext);
		}
	}

	// ========================= IMPLEMENTATION: AbstractJobScheduler ======

	/**
	 * 
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onSchedulerStart()
	 */
	@Override
	public void onSchedulerStart()
	{
		graphScheduler.onSchedulerStart();
	}

	/**
	 * @param job
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onJobStarted(java.lang.Object)
	 */
	@Override
	public void onBeforeJobStarted(final QueryJob job)
	{
		// TODO: Enable evaluating dependency expressions when true phased queries can be
		// implemented - otherwise this is just a waste of processing

		// job.evaluateDependencyExpressions();
		graphScheduler.onBeforeJobStarted(job);
	}

	/**
	 * @param job
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onJobStarted(java.lang.Object)
	 */
	@Override
	public void onAfterJobStarted(final QueryJob job)
	{
		graphScheduler.onAfterJobStarted(job);

		// After each job starts, check to see if the graph scheduler is done and stop
		// this scheduler as well, otherwise this job scheduler will not be removed from
		// the list of observers
		if (graphScheduler.isCompleted())
		{
			stop();
		}
	}

	/**
	 * @param job
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onJobAfterCompleted(java.lang.Object)
	 */
	@Override
	public void onBeforeJobCompleted(final QueryJob job)
	{
		graphScheduler.onBeforeJobCompleted(job);
	}

	/**
	 * @param job
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onJobAfterCompleted(java.lang.Object)
	 */
	@Override
	public void onJobAfterCompleted(final QueryJob job)
	{
		try
		{
			graphScheduler.onJobAfterCompleted(job);
		}
		catch (final IllegalArgumentException e)
		{
			final List<String> vertices = CollectionUtil.newList();
			for (final QueryJob vertex : graph.vertexSet())
			{
				vertices.add(vertex.toString());
			}
			throw new ApplicationException(
					"Plan internal data structure is corrupted. "
							+ "Got a job completion event for job ID "
							+ job
							+ " but it is not a vertex of the dependency graph. Valid vertex job IDs are "
							+ vertices, e);
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Build a graph that represents the {@link QueryContext} plan. Graph vertices are
	 * execution rules and edges are dependency rules.
	 * <p>
	 * We also store a map from execution ID to the dedicated {@link QueryContext} that is
	 * actually fed to Camel.
	 * 
	 * @return plan graph corresponding to queryContext.getPlan
	 */
	private DirectedGraph<QueryJob, DefaultEdge> buildGraph()
	{
		final DirectedGraph<QueryJob, DefaultEdge> g = new SimpleDirectedGraph<>(
				DefaultEdge.class);
		final Plan plan = queryContext.getPlan();

		// Add vertices = execution rules. Index them by their execution ID so that we can
		// easily define edges in the next loop
		executionIdToQc.clear();
		for (final ExecutionRule executionRule : plan.getExecutionRules())
		{
			validateExecutionRule(executionRule);
			final QueryJob job = buildQueryJob(queryContext, executionRule);
			g.addVertex(job);
			executionIdToQc.put(executionRule.getId(), job);
		}

		// Add edges = dependency rules
		for (final DependencyRule dependencyRule : plan.getDependencyRules())
		{
			final QueryJob dependency = executionIdToQc.get(dependencyRule
					.getDependencyExecutionId());
			final QueryJob outcome = executionIdToQc.get(dependencyRule
					.getOutcomeExecutionId());
			g.addEdge(dependency, outcome);
		}

		// Trigger one query per execution rule. Each query is directed to a single DS.
		if (log.isDebugEnabled())
		{
			log.debug("Dependency graph: " + g);
		}
		return g;
	}
}
