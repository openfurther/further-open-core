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

import java.util.Set;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A simple job runner emulation. Starts and completes jobs associated with graph
 * vertices, and notifies observers (schedulers) of job completion.
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
 * @version Dec 16, 2010
 */
final class SimpleJobRunner<V> implements JobRunner<V>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SimpleJobRunner.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Keeps track of the currently running jobs. Package-private for access by other test
	 * classes in this package.
	 */
	final Set<V> runningJobs = CollectionUtil.newSet();

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.schedule.JobRunner#startJob(java.lang.Object)
	 */
	@Override
	public void startJob(final V v)
	{
		runningJobs.add(v);
		if (log.isDebugEnabled())
		{
			log.debug("Job " + v + " started");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.graph.JobRunner#completeJob(V)
	 */
	@Override
	public void completeJob(final V v)
	{
		runningJobs.remove(v);
		if (log.isDebugEnabled())
		{
			log.debug("Job " + v + " completed");
		}
	}
}
