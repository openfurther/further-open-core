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
package edu.utah.further.core.util.observer;

import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.collections.CollectionUtil.SetType.CONCURRENT_HASH_SET;
import static edu.utah.further.core.api.text.StringUtil.pluralForm;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;
import edu.utah.further.core.api.observer.Subject;

/**
 * A standard implementation of the {@link Subject} using an {@link ArrayList} of
 * observers. The {@link #notifyObservers(Message)} method is a template method that
 * relies on the abstract {@link #newMessage()} to send messages to observers. This object
 * can be composed with the &quot;real&quot; subject. Hence we call it a
 * <code>Transmitter</code>, whose only function is to communicate a change in state of
 * the subject.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 8, 2008
 */
@Implementation
public class SimpleSubject implements Subject
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(SimpleSubject.class);

	// ========================= FIELDS ====================================

	/**
	 * List of observers.
	 */
	private final Collection<Observer> observers = newSet(CONCURRENT_HASH_SET);

	/**
	 * Instead of implementing Subject, Subject can be a field in a higher level object,
	 * state, whose state is monitored by observers.
	 */
	private final Subject subject;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a subject.
	 * 
	 * @param subject
	 */
	public SimpleSubject(final Subject subject)
	{
		this.subject = subject;
	}

	// ========================= ABSTRACT METHODS ==========================

	// ========================= IMPLEMENTATION: Subject ===================

	/**
	 * @return the subject
	 */
	public Subject getSubject()
	{
		return subject;
	}

	/**
	 * @see edu.utah.further.core.api.observer.Subject#addObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void addObserver(final Observer o)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Adding observer " + o);
		}
		observers.add(o);
	}

	/**
	 * Unsubscribe an observer with this subject.
	 * 
	 * @param o
	 *            observer to remove
	 * @see edu.utah.further.core.api.observer.Subject#removeObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void removeObserver(final Observer o)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Removing observer " + o);
		}
		observers.remove(o);
	}

	/**
	 * Notify all observers of a change in the subject's internal state. The
	 * <code>Observer.update()</code> method will be called in all observers from this
	 * method, with an optional message.
	 * 
	 * @param message
	 * @see edu.utah.further.core.api.observer.Subject#notifyObservers(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	synchronized public void notifyObservers(final Message message)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Sending message to " + observers.size() + " "
					+ pluralForm("observer", observers.size()) + " " + message);
		}
		for (final Observer observer : observers)
		{
			// Can only notify live observer references. If an observer forgot to remove
			// itself and was then garbage-collected, die
			if (observer == null)
			{
				throw new IllegalStateException(
						"Null observer. It is likely that an observer forgot to remove itself and "
								+ "was then garbage-collected. Make sure to call "
								+ "subject.removeObserver(this) in all observers.");
			}
			if (log.isDebugEnabled())
			{
				log.debug("Sending message to observer " + observer);
			}
			observer.update(message);
		}
	}

	// ========================= GETTERS & SETTERS ==========================

}
