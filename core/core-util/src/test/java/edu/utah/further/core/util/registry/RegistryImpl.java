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

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.collections.CollectionUtil.MapType.CONCURRENT_HASH_MAP;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.util.registry.SimpleDataMessage.Type.DATA_REQUEST;
import static edu.utah.further.core.util.registry.SimpleDataMessageImpl.newRequestMessage;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;

import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;

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
public class RegistryImpl extends NodeImpl implements Registry
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RegistryImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * List of sources registered with this registry.
	 */
	private final Collection<Source> sourceCollection = newSet();

	/**
	 * List of clients that the registry reports to.
	 */
	private final Collection<Observer> clientCollection = newSet();

	/**
	 * For each request message, this object keeps a queue of source responses messages.
	 * Once all sources respond, a response is sent from this registry to its client, and
	 * the request queue is removed from the queue map.
	 */
	private final Map<UUID, ResponseQueue> requestQueue = newMap(CONCURRENT_HASH_MAP);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 * @param description
	 * @param url
	 * @param admin
	 */
	public RegistryImpl(final String name, final String description, final String url,
			final InternetAddress admin)
	{
		super(name, description, url, admin);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String string = "Registry[";
		final Iterator<Source> iterator = sourceCollection.iterator();
		while (iterator.hasNext())
		{
			final Source source = iterator.next();
			string = string + source.getName();
			if (iterator.hasNext())
				string = string + ", ";
		}
		string = string + "; queue = " + requestQueue;
		string = string + "]";
		return string;
		// return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("name",
		// getName())
		// .append("description", getDescription()).append("url", getUrl()).append(
		// "admin", getMainContact()).append("sourceCollection",
		// sourceCollection).toString();
	}

	// ========================= IMPLEMENTATION: Subject ===================

	/**
	 * Register a source with this registry.
	 *
	 * @see edu.utah.further.core.api.observer.Subject#addObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void addObserver(final Observer o)
	{
		// verifyType(o, Source.class);
		if (instanceOf(o, Source.class))
		{
			// Add a source
			final Source source = (Source) o;
			sourceCollection.add(source);
			// Establish the reverse connection: register the registry in the source
			source.addObserver(this);
		}
		else
		{
			// Add a client
			clientCollection.add(o);
		}
	}

	/**
	 * Unregister a source from this registry.
	 *
	 * @param o
	 *            observer to remove
	 * @see edu.utah.further.core.api.observer.Subject#removeObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void removeObserver(final Observer o)
	{
		// verifyType(o, Source.class);
		if (instanceOf(o, Source.class))
		{
			// Remove a source
			final Source source = (Source) o;
			sourceCollection.remove(source);
			// Establish the reverse connection: unregister the registry from the source
			source.removeObserver(this);
		}
		else
		{
			// Remove a client
			clientCollection.remove(o);
		}
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
	public void notifyObservers(final Message message)
	{
		// If this is a data request, notify subjects. If this is a response, notify
		// clients.
		final SimpleDataMessage m = (SimpleDataMessage) message;
		final Collection<? extends Observer> observers = (m.getType() == DATA_REQUEST) ? sourceCollection
				: clientCollection;
		for (final Observer observer : observers)
		{
			observer.update(message);
		}
	}

	// ========================= IMPLEMENTATION: Observer ==================

	/**
	 * @param message
	 * @see edu.utah.further.core.api.observer.Observer#update(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	public void update(final Message message)
	{
		final SimpleDataMessage response = (SimpleDataMessage) message;
		final SimpleDataMessage request = response.getOriginalMessage();
		final UUID id = request.getId();
		final ResponseQueue responseQueue = requestQueue.get(id);
		if (log.isDebugEnabled())
		{
			log.debug("Received response: " + response);
		}
		responseQueue.offer(response);

		// If all sources responded, pro-actively notify client and
		// delete the response queue
		if (responseQueue.isFull())
		{
			// Notify only the client that sent this message.
			final Observer observer = request.getObserver();
			observer.update(responseQueue.getJoinedMessage());
			requestQueue.remove(id);
		}
	}

	// ========================= IMPLEMENTATION: Registry ==================

	/**
	 * Send out a data request to all sources.
	 *
	 * @param observer
	 *            client requesting the data
	 * @return identifier of request message
	 * @see edu.utah.further.core.util.registry.Registry#sendDataRequest(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public UUID sendDataRequest(final Observer observer)
	{
		final SimpleDataMessage request = newRequestMessage(this, "data request",
				observer);
		final UUID id = request.getId();

		// Create a response queue for the request. To make sure that the number
		// of sources does not change while creating the queue, this code is synchronized.
		synchronized (sourceCollection)
		{
			requestQueue.put(id, new ResponseQueue(request, size()));
		}

		if (log.isDebugEnabled())
		{
			log.debug("Sending request: " + request);
		}
		notifyObservers(request);
		return id;
	}

	/**
	 * @param id
	 * @param deleteData
	 * @see edu.utah.further.core.util.registry.Registry#sendDataNotification(java.util.UUID,
	 *      boolean)
	 */
	@Override
	public void sendDataNotification(final UUID id, final boolean deleteData)
	{
		final ResponseQueue responseQueue = requestQueue.get(id);
		if (responseQueue != null)
		{
			// Notify only the client that sent this message.
			final Observer observer = responseQueue.getRequestMessage().getObserver();
			observer.update(responseQueue.getJoinedMessage());
			if (deleteData)
			{
				requestQueue.remove(id);
			}
		}
	}

	/**
	 * @return
	 * @see java.util.Collection#size()
	 * @see edu.utah.further.core.util.registry.Registry#size()
	 */
	@Override
	public int size()
	{
		return sourceCollection.size();
	}

	// ========================= PRIVATE METHODS ===========================

}
