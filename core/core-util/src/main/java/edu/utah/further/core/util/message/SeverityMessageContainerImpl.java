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
package edu.utah.further.core.util.message;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;
import static java.util.Collections.unmodifiableList;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessage;
import edu.utah.further.core.api.message.SeverityMessageContainer;

/**
 * A list-based implementation of a container of {@link SeverityMessage}s.
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
 * @version Dec 31, 2008
 */
public class SeverityMessageContainerImpl implements SeverityMessageContainer
{
	// ========================= CONSTANTS =================================

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Message unique identifier.
	 */
	private final SortedSet<SeverityMessage> messages = newSortedSet();

	// ========================= IMPLEMENTATION: SeverityMessageContainer ==

	/**
	 * @param severity
	 * @param text
	 * @see edu.utah.further.core.api.message.SeverityMessageContainer#addMessage(edu.utah.further.core.api.message.Severity,
	 *      java.lang.String)
	 */
	@Override
	public void addMessage(final Severity severity, final String text)
	{
		addMessage(new SeverityMessage(severity, text));
	}

	/**
	 * @param message
	 * @see edu.utah.further.core.api.message.MessageContainer#addMessage(java.lang.Object)
	 */
	@Override
	public void addMessage(final SeverityMessage message)
	{
		messages.add(message);
	}

	/**
	 * Add all messages in another container to this container.
	 *
	 * @param other
	 *            another message container
	 * @see edu.utah.further.core.api.message.SeverityMessageContainer#addMessages(edu.utah.further.core.api.message.SeverityMessageContainer)
	 */
	@Override
	public void addMessages(final SeverityMessageContainer other)
	{
		for (final SeverityMessage message : other)
		{
			addMessage(message);
		}
	}

	/**
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#clearMessages()
	 */
	@Override
	public void clearMessages()
	{
		messages.clear();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.message.MessageContainer#getSize()
	 */
	@Override
	public int getSize()
	{
		return messages.size();
	}

	/**
	 * Returns true iff the list of messages is empty.
	 *
	 * @return <code>true</code> iff the list of messages is empty
	 * @see edu.utah.further.core.api.message.MessageContainer#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return messages.isEmpty();
	}

	/**
	 * @param uuid
	 * @see edu.utah.further.core.api.message.MessageContainer#removeMessage(java.lang.Object)
	 */
	@Override
	public void removeMessage(final SeverityMessage message)
	{
		messages.remove(message);
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<SeverityMessage> iterator()
	{
		return messages.iterator();
	}

	/**
	 * @return
	 */
	@Override
	public List<SeverityMessage> getAsList()
	{
		return unmodifiableList(newList(messages));
	}

	/**
	 * Return all messages of a certain severity level.
	 *
	 * @param message
	 *            severity severity level
	 * @return list of messages at that level
	 */
	@Override
	public Set<SeverityMessage> getMessages(final Severity severity)
	{
		final Set<SeverityMessage> selected = CollectionUtil.newSet();
		for (final SeverityMessage message : messages)
		{
			if (message.getSeverity() == severity)
			{
				selected.add(message);
			}
		}
		return selected;
	}
}
