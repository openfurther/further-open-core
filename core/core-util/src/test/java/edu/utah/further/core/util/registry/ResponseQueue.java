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
package edu.utah.further.core.util.registry;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.util.registry.SimpleDataMessageImpl.newResponseMessage;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;

/**
 * A registry of sources.
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
class ResponseQueue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ResponseQueue.class);

	// ========================= FIELDS ====================================

	/**
	 *Original request message.
	 */
	private final SimpleDataMessage requestMessage;

	/**
	 * Expected # of responded sources.
	 */
	private final int expectedNumSources;

	/**
	 * Keeps a queue of source responses messages. Once all sources respond, this object
	 * is flagged as full.
	 */
	private final Queue<SimpleDataMessage> queue = new PriorityQueue<>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param requestMessage
	 * @param expectedNumSources
	 */
	public ResponseQueue(final SimpleDataMessage requestMessage, final int expectedNumSources)
	{
		super();
		this.requestMessage = requestMessage;
		this.expectedNumSources = expectedNumSources;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= METHODS ===================================

	/**
	 * @param e
	 * @return
	 * @see java.util.Queue#add(java.lang.Object)
	 */
	public void add(final SimpleDataMessage e)
	{
		queue.add(e);
	}

	/**
	 * @return
	 * @see java.util.Queue#element()
	 */
	public SimpleDataMessage element()
	{
		return queue.element();
	}

	/**
	 * @return
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<SimpleDataMessage> iterator()
	{
		return queue.iterator();
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	public boolean offer(final SimpleDataMessage e)
	{
		return queue.offer(e);
	}

	/**
	 * @return
	 * @see java.util.Queue#peek()
	 */
	public SimpleDataMessage peek()
	{
		return queue.peek();
	}

	/**
	 * @return
	 * @see java.util.Queue#poll()
	 */
	public SimpleDataMessage poll()
	{
		return queue.poll();
	}

	/**
	 * @return
	 * @see java.util.Queue#remove()
	 */
	public SimpleDataMessage remove()
	{
		return queue.remove();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(final Object obj)
	{
		return queue.remove(obj);
	}

	/**
	 * @return
	 * @see java.util.Collection#size()
	 */
	public int size()
	{
		return queue.size();
	}

	/**
	 * Is the queue full.
	 *
	 * @return
	 */
	public boolean isFull()
	{
		return queue.size() == expectedNumSources;
	}

	/**
	 * Return a message that contains data from all sources in the queue.
	 *
	 * @return a joined message from all sources
	 */
	public SimpleDataMessage getJoinedMessage()
	{
		final List<String> allData = newList();
		while (!queue.isEmpty())
		{
			final SimpleDataMessage m = queue.poll();
			allData.addAll(m.getData());
		}
		return newResponseMessage(null, null, allData);
	}

	/**
	 * Return the requestMessage property.
	 *
	 * @return the requestMessage
	 */
	public SimpleDataMessage getRequestMessage()
	{
		return requestMessage;
	}

	// ========================= PRIVATE METHODS ===========================

}
