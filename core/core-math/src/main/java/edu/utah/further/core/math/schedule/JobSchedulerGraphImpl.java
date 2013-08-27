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
package edu.utah.further.core.math.schedule;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Schedules jobs associated with graph vertices in topological sort order, i.e. if x -> y
 * is an edge, then job x must be completed before job y.
 * <p>
 * This class implements the observer pattern: it observes {@link JobRunner} and decides
 * what jobs to start next based on job completion events.
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
 * @version Dec 15, 2010
 */
public final class JobSchedulerGraphImpl<V> extends AbstractJobScheduler<V>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(JobSchedulerGraphImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Graph in question.
	 */
	private final DirectedGraph<V, DefaultEdge> graph;

	/**
	 * Keeps track of the currently running jobs and jobs that remain to be run.
	 */
	private final Set<V> remainingJobs = CollectionUtil.newSet();

	/**
	 * Keeps track of all completed jobs to date.
	 */
	private final Set<V> completedJobs = CollectionUtil.newSet();

	// ========================= FIELDS ====================================

	/**
	 * Keeps track of how many dependencies each vertex has that didn't yet complete.
	 */
	private final Map<V, Integer> remainingDependencies = CollectionUtil
			.newConcurrentMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a job scheduler.
	 * 
	 * @param graph
	 *            job dependency graph to schedule
	 * @param runner
	 *            the runner we observe and schedule tasks in
	 */
	public JobSchedulerGraphImpl(final JobRunnerNotifier<V> notifier,
			final DirectedGraph<V, DefaultEdge> graph)
	{
		super(notifier);
		this.graph = graph;

		// Initialize internal data structures
		this.remainingJobs.addAll(graph.vertexSet());
		initializeRemainingDependencies();
	}

	// ========================= IMPL: Observer ============================

	// ========================= PRIVATE METHODS ============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.schedule.AbstractJobScheduler#onStart()
	 */
	@Override
	public void onSchedulerStart()
	{
		final Collection<V> dependents = graph.vertexSet();
		if (dependents != null)
		{
			boolean atLeastOneJobStarted = false;
			// Encountered a relevant event
			for (final V dependent : dependents)
			{
				// There must be at least one job which can be started when the schedule
				// starts.
				if (NumberUtils.INTEGER_ZERO.equals(remainingDependencies.get(dependent)))
				{
					startJob(dependent);
					atLeastOneJobStarted = true;
				}
			}

			// Ensure that a job is started or nothing will happen
			if (!atLeastOneJobStarted)
			{
				throw new IllegalStateException(
						"Job scheduling graph is unresolvable, there are no jobs which can start. "
								+ "All jobs have dependencies which must be started first. Expected at least one job with no dependencies that could start");
			}

		}
		else
		{
			throw new IllegalStateException(
					"Expected job scheduler graph with dependencies but graph has no dependencies (vertices)");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.math.schedule.AbstractJobScheduler#onJobCompleted(java.lang
	 * .Object)
	 */
	@Override
	public void onJobAfterCompleted(final V job)
	{
		// Update tracking lists
		completedJobs.add(job);

		// Remove dependency of job on its dependents and start those that have no
		// remaining dependencies
		final Collection<V> dependents = getDependents(job);
		if (dependents != null)
		{
			// Encountered a relevant event
			for (final V dependent : dependents)
			{
				if (job != null)
				{
					removeDependency(dependent);
				}
				if (NumberUtils.INTEGER_ZERO.equals(remainingDependencies.get(dependent)))
				{
					startJob(dependent);
				}
			}
		}
	}

	/**
	 * @param dependent
	 */
	private void startJob(final V v)
	{
		checkIfDependenciesAreSatisfied(v);

		// Automatically completes when there are no more jobs to run
		remainingJobs.remove(v);
		if (remainingJobs.isEmpty())
		{
			stop();
		}

		notifier.startJob(v);
	}

	/**
	 * Get dependencies relevant to a job runner event. runner start-up event, the
	 * "dependents" are defined as all vertices.
	 * 
	 * @param job
	 *            completed job's vertex, if applicable
	 * @return dependent collection
	 */
	private Collection<V> getDependents(final V job)
	{
		if (graph.containsVertex(job))
		{
			return Graphs.successorListOf(graph, job);
		}
		return Collections.EMPTY_SET;
	}

	/**
	 * Calculate the initial number of remaining dependencies of each job.
	 */
	private void initializeRemainingDependencies()
	{
		for (final V v : graph.vertexSet())
		{
			final int numDependencies = graph.incomingEdgesOf(v).size();
			remainingDependencies.put(v, new Integer(numDependencies));
		}
	}

	/**
	 * @param dependent
	 */
	private void removeDependency(final V dependent)
	{
		remainingDependencies.put(dependent,
				new Integer(remainingDependencies.get(dependent).intValue() - 1));
	}

	/**
	 * Check that all job dependencies are fulfilled.
	 * 
	 * @param v
	 */
	private void checkIfDependenciesAreSatisfied(final V v)
	{
		final Set<V> dependencies = CollectionUtil.newSet(Graphs.predecessorListOf(graph,
				v));
		if (!completedJobs.containsAll(dependencies))
		{
			final Set<V> unfulfilledDependencies = CollectionUtil.newSet(dependencies);
			dependencies.removeAll(completedJobs);
			throw new IllegalStateException("Cannot start job " + v
					+ " due to unfulfilled dependencies " + unfulfilledDependencies
					+ ". Completed jobs " + completedJobs + " dependencies "
					+ dependencies);
		}
	}
}
