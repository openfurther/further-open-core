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

import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;
import edu.utah.further.core.api.observer.Subject;
import edu.utah.further.core.math.schedule.JobEvent.Type;
import edu.utah.further.core.util.observer.DataTransmitter;

/**
 * A decorator of a {@link JobRunner} that adds an Observer Pattern functionality --
 * notifies observers of job start and completion events.
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
public final class JobRunnerNotifier<V> implements JobRunner<V>, Subject
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The native runner of jobs that this decorator invokes.
	 */
	private final JobRunner<V> runner;

	// ========================= FIELDS ====================================

	/**
	 * Adds observer pattern (subject) functionality to this object.
	 */
	private final DataTransmitter<?> dataTransmitter = new DataTransmitter<>(
			this);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Decorate a runner.
	 * 
	 * @param runner
	 *            native job runner
	 */
	public JobRunnerNotifier(final JobRunner<V> runner)
	{
		super();
		this.runner = runner;
	}

	// ========================= METHODS ===================================

	/**
	 * Notify observers of event type <code>type</code> for job <code>v</code>.
	 * 
	 * @param v
	 *            job identifier
	 * @param type
	 *            job event type
	 */
	public void notifyObservers(final V v, final Type type)
	{
		notifyObservers(JobEvent.<V> newEvent(this, type, v));
	}

	// ========================= IMPL: JobRunner ===========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.schedule.JobRunner#startJob(java.lang.Object)
	 */
	@Override
	public void startJob(final V v)
	{
		notifyObservers(v, JobEvent.Type.BEFORE_STARTED);
		runner.startJob(v);
		notifyObservers(v, JobEvent.Type.AFTER_STARTED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.math.graph.JobRunner#completeJob(V)
	 */
	@Override
	public void completeJob(final V v)
	{
		notifyObservers(v, JobEvent.Type.BEFORE_COMPLETED);
		runner.completeJob(v);
		notifyObservers(v, JobEvent.Type.AFTER_COMPLETED);
	}

	// ========================= IMPL: Subject =============================

	/**
	 * @param o
	 * @see edu.utah.further.core.util.observer.SimpleSubject#addObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void addObserver(final Observer o)
	{
		dataTransmitter.addObserver(o);
	}

	/**
	 * @param o
	 * @see edu.utah.further.core.util.observer.SimpleSubject#removeObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void removeObserver(final Observer o)
	{
		dataTransmitter.removeObserver(o);
	}

	/**
	 * @param message
	 * @see edu.utah.further.core.util.observer.SimpleSubject#notifyObservers(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	public void notifyObservers(final Message message)
	{
		dataTransmitter.notifyObservers(message);
	}

	// ========================= PRIVATE METHODS ============================
}
