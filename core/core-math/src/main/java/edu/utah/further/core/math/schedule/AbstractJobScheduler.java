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

import org.slf4j.Logger;

import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.observer.Message;

/**
 * An base class of job schedulers.
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
 * @version Dec 20, 2010
 */
public abstract class AbstractJobScheduler<V> implements JobScheduler<V>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractJobScheduler.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * The runner we observe and schedule tasks in.
	 */
	public final JobRunnerNotifier<V> notifier;

	// ========================= FIELDS ====================================

	/**
	 * Is this plan completed yet.
	 */
	private boolean completed = false;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a job scheduler.
	 *
	 * @param notifier
	 *            the runner we observe and schedule tasks in
	 */
	public AbstractJobScheduler(final JobRunnerNotifier<V> notifier)
	{
		super();
		this.notifier = notifier;
	}

	// ========================= IMPL: JobScheduler ========================

	/**
	 * @see edu.utah.further.core.api.state.SimpleSwitch#start()
	 */
	@Override
	public final void start()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Starting " + this);
		}
		if (completed)
		{
			throw new IllegalStateException(
					"Cannot restart a scheduler after it is stopped");
		}
		notifier.addObserver(this);
		onSchedulerStart();
	}

	/**
	 * @see edu.utah.further.core.math.schedule.JobScheduler#stop()
	 */
	@Override
	public final void stop()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Stopping " + this);
		}
		completed = true;
		notifier.removeObserver(this);
	}

	/**
	 * @param message
	 * @see edu.utah.further.core.math.schedule.JobScheduler#update(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	public final void update(final Message message)
	{
		if (ReflectionUtil.instanceOf(message, JobEvent.class)
				&& notifier.equals(message.getSubject()))
		{
			@SuppressWarnings("unchecked")
			final JobEvent<V> jobEvent = (JobEvent<V>) message;

			final V job = jobEvent.getEntity();
			switch (jobEvent.getType())
			{
				case BEFORE_STARTED:
				{
					onBeforeJobStarted(job);
					break;
				}

				case AFTER_STARTED:
				{
					onAfterJobStarted(job);
					break;
				}

				case BEFORE_COMPLETED:
				{
					onBeforeJobCompleted(job);
					break;
				}

				case AFTER_COMPLETED:
				{
					onJobAfterCompleted(job);
					break;
				}

				case SCHEDULER_COMPLETED:
				{
					onSchedulerCompleted(jobEvent);
					break;
				}

				default:
				{
					// Irrelevant event
					break;
				}
			}
		}
	}

	/**
	 * Return the completed property.
	 *
	 * @return is this plan completed
	 * @see edu.utah.further.fqe.impl.service.route.QueryPlan#isCompleted()
	 */
	@Override
	public final boolean isCompleted()
	{
		return completed;
	}

	/**
	 * A hook for sub-classes: runs upon starting the scheduler.
	 */
	public abstract void onSchedulerStart();

	/**
	 * A hook for sub-classes: process a job before-start event.
	 *
	 * @param job
	 *            job in question
	 */
	public void onBeforeJobStarted(final V job)
	{
		// A hook
	}

	/**
	 * A hook for sub-classes: process a job after-start event.
	 *
	 * @param job
	 *            job in question
	 */
	public void onAfterJobStarted(final V job)
	{
	}

	/**
	 * A hook for sub-classes: process a job before-completion event.
	 *
	 * @param job
	 *            job in question
	 */
	public void onBeforeJobCompleted(final V job)
	{
		// A hook
	}

	/**
	 * A hook for sub-classes: process a job after-completion event.
	 *
	 * @param job
	 *            job in question
	 */
	public void onJobAfterCompleted(final V job)
	{
		// A hook
	}

	/**
	 * A hook for sub-classes: process a scheduler completion event.
	 *
	 * @param jobEvent
	 *            the received job event message
	 */
	public void onSchedulerCompleted(final JobEvent<V> jobEvent)
	{
		// A hook
	}

	// ========================= PRIVATE METHODS ===========================

}