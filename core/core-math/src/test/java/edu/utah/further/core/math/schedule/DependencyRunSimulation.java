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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.UnmodifiableIterator;
import edu.utah.further.core.math.util.GraphTestUtil;

/**
 * Simulate running jobs associated with graph vertices in topological order, i.e. if x ->
 * y is an edge, then job x must be completed before job y.
 * <p>
 * This class implements {@link Iterator}, so the simulation is run by iterating. At every
 * step of the simulation, {@link Iterator#next()} returns the set of jobs started at that
 * step. If all these sets are merged into a large list (keeping the sets' ordering;
 * element ordering within a set does not matter), the list is a topological sort of the
 * graph's vertices.
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
final class DependencyRunSimulation extends UnmodifiableIterator<Set<Integer>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DependencyRunSimulation.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Graph in question.
	 */
	private final DirectedGraph<Integer, DefaultEdge> graph;

	// ========================= FIELDS ====================================

	/**
	 * Keep track of the currently running jobs and jobs that remain to be run.
	 */
	private final Set<Integer> runningJobs = CollectionUtil.newSet();
	private final Set<Integer> remainingJobs = CollectionUtil.newSet();

	/**
	 * Keeps track of how many dependencies each vertex has that didn't yet complete.
	 */
	private final Map<Integer, Integer> remainingDependencies = CollectionUtil
			.newConcurrentMap();

	/**
	 * Are we at the initial state of the simulation loop or not.
	 */
	private boolean initialState;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param graph
	 */
	public DependencyRunSimulation(final DirectedGraph<Integer, DefaultEdge> graph)
	{
		super();
		this.graph = graph;
		initialize();
	}

	// ========================= IMPL: Iterator ============================

	/**
	 * Simulate running jobs associated with graph vertices in topological order, i.e. if
	 * x -> y is an edge, then job x must be completed before job y.
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Set<Integer> next()
	{
		if (initialState)
		{
			initialState = false;
			return initialRunsEvent();
		}
		// Simulate completing and starting jobs given the current state
		return jobCompletionEvent(GraphTestUtil.randomElement(runningJobs));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return !remainingJobs.isEmpty();
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * Populate internal data structures with initial values.
	 */
	private void initialize()
	{
		initialState = true;
		runningJobs.clear();
		remainingJobs.clear();
		remainingDependencies.clear();
		for (final Integer v : graph.vertexSet())
		{
			final int numDependencies = graph.incomingEdgesOf(v).size();
			remainingDependencies.put(v, new Integer(numDependencies));
			remainingJobs.add(v);
		}
	}

	/**
	 * @param runningJobs
	 * @param v
	 */
	private void startJob(final Integer v)
	{
		runningJobs.add(v);
		remainingJobs.remove(v);
		if (log.isDebugEnabled())
		{
			log.debug("Job " + v + " started");
		}
	}

	/**
	 * @param runningJobs
	 * @param v
	 */
	private void completeJob(final Integer v)
	{
		runningJobs.remove(v);
		if (log.isDebugEnabled())
		{
			log.debug("Job " + v + " completed");
		}
	}

	/**
	 * @param dependent
	 */
	private void removeDependency(final Integer dependent)
	{
		remainingDependencies.put(dependent,
				new Integer(remainingDependencies.get(dependent).intValue() - 1));
	}

	/**
	 * @return
	 */
	private Set<Integer> initialRunsEvent()
	{
		// Initialization: run all jobs that already don't have any dependencies
		final Set<Integer> started = CollectionUtil.newSet();
		for (final Integer v : graph.vertexSet())
		{
			if (remainingDependencies.get(v).intValue() == 0)
			{
				startJob(v);
				started.add(v);
			}
		}
		return started;
	}

	@SuppressWarnings("boxing")
	private Set<Integer> jobCompletionEvent(final Integer completed)
	{
		final Set<Integer> started = CollectionUtil.newSet();
		completeJob(completed);

		// Update dependencies and trigger those that can now be started
		for (final DefaultEdge edge : graph.outgoingEdgesOf(completed))
		{
			final Integer dependent = graph.getEdgeTarget(edge);
			removeDependency(dependent);
			if (remainingDependencies.get(dependent) == 0)
			{
				startJob(dependent);
				started.add(dependent);
			}
		}
		return started;
	}
}
