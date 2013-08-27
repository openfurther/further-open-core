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

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.observer.DataMessage;
import edu.utah.further.core.api.observer.Subject;

/**
 * A data transmitter that prepares concrete messages for a subject.
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
public final class DataTransmitter<T extends Subject> extends SimpleSubject
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a transmitter for a subject.
	 * 
	 * @param subject
	 *            this object is usually composed with a transmitter
	 */
	public DataTransmitter(final T subject)
	{
		super(subject);
	}

	// ========================= IMPLEMENTATION: Subject ===================

	/**
	 * @return the subject
	 */
	@Override
	public T getSubject()
	{
		return (T) super.getSubject();
	}

	// ========================= METHODS ===================================

	/**
	 * A factory method. Returns a new blank message to observers. This should use the
	 * subject's getters methods.
	 * 
	 * @param entityClass
	 *            type of message data entity
	 * @return the message
	 */
	public <E> DataMessage<T, E> newMessage(final Class<E> entityClass)
	{
		return new DataMessage<>(getSubject());
	}
}
