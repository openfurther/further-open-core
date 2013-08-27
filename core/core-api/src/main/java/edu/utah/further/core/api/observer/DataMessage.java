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
package edu.utah.further.core.api.observer;

import edu.utah.further.core.api.context.Implementation;

/**
 * A simple empty message that holds its sender only.
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
public class DataMessage<T extends Subject, E> implements Message
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The sender of this message.
	 */
	private final T subject;

	/**
	 * Entity containing the data of this message.
	 */
	protected E entity;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a message from a sender.
	 * 
	 * @param subject
	 *            The sender of this message
	 */
	public DataMessage(final T subject)
	{
		this.subject = subject;
	}

	// ========================= IMPLEMENTATION: Message ===================

	/**
	 * @return
	 * @see edu.utah.further.core.api.observer.Message#getSubject()
	 */
	@Override
	public T getSubject()
	{
		return subject;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the entity property.
	 * 
	 * @return the entity
	 */
	public E getEntity()
	{
		return entity;
	}

	/**
	 * Set a new value for the entity property.
	 * 
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final E entity)
	{
		this.entity = entity;
	}
}
