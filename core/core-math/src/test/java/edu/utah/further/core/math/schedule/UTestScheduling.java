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

import static edu.utah.further.core.math.util.GraphTestUtil.directedGraph;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.math.util.GraphTestUtil;

/**
 * Test job schedulding algorithms.
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
public final class UTestScheduling
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestScheduling.class);

	// ========================= TESTING METHODS ===========================

	/**
	 * Simulate running jobs associated with graph vertices in topological order, i.e. if
	 * x -> y is an edge, then job x must be completed before job y.
	 */
	@Test
	public void simulateDependencyRun()
	{
		// Generate a small test graph
		final DirectedGraph<Integer, DefaultEdge> g = directedGraph();
		final Iterator<Set<Integer>> simulation = new DependencyRunSimulation(g);

		while (simulation.hasNext())
		{
			final Set<Integer> runningJobs = simulation.next();
			if (log.isDebugEnabled())
			{
				log.debug("Jobs started: " + runningJobs);
			}
		}
	}

	/**
	 * Simulate running jobs associated with graph vertices in topological order, i.e. if
	 * x -> y is an edge, then job x must be completed before job y.
	 */
	@Test
	public void simulateDependencyRunObserverPatternStyle()
	{
		// Generate a small test graph
		final DirectedGraph<Integer, DefaultEdge> g = directedGraph();

		// Runs jobs
		final SimpleJobRunner<Integer> runner = new SimpleJobRunner<>();
		final JobRunnerNotifier<Integer> notifier = new JobRunnerNotifier<>(runner);

		// Schedules new jobs
		final JobScheduler<Integer> scheduler = new JobSchedulerGraphImpl<>(
				notifier, g);
		// Monitors job starts and completions
		final JobMonitor<Integer> monitor = new JobMonitorImpl<>(notifier);

		// Run job running simulation. In a real scenario there is no need to call
		// nextEvent(); runner triggers job completion events on its own. Note that we run
		// the simulation until the scheduler is done scheduling, but then continue until
		// there are no more running jobs.
		scheduler.start();
		while (!scheduler.isCompleted() || hasNextEvent(runner))
		{
			nextEvent(notifier, runner);
		}

		// If we are here, everything is fine because runner didn't crash trying to run a
		// job with unfulfilled dependencies
		if (log.isDebugEnabled())
		{
			log.debug("Runner log:" + Strings.NEW_LINE_STRING + Strings.NEW_LINE_STRING
					+ monitor.toString());
		}
	}

	// ========================= PRIVATE METHODS ============================

	// ========================= METHODS ====================================

	/**
	 * Trigger the completion of a random job.
	 */
	public <V> void nextEvent(final JobRunnerNotifier<V> notifier,
			final SimpleJobRunner<V> simpleRunner)
	{
		if (hasNextEvent(simpleRunner))
		{
			notifier.completeJob(GraphTestUtil.randomElement(simpleRunner.runningJobs));
		}
	}

	/**
	 * @param <V>
	 * @param simpleRunner
	 * @return
	 */
	private <V> boolean hasNextEvent(final SimpleJobRunner<V> simpleRunner)
	{
		return !simpleRunner.runningJobs.isEmpty();
	}
}
