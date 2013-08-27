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

import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.lang.ReflectionUtil.verifyType;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.core.util.registry.SimpleDataMessageImpl.newResponseMessage;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;

/**
 * A simulated data source.
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
public class SourceImpl extends NodeImpl implements Source
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SourceImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * The list of registries that this source registers itself in.
	 */
	private final Collection<Registry> registryCollection = newSet();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 * @param description
	 * @param url
	 * @param admin
	 */
	public SourceImpl(final String name, final String description, final String url,
			final InternetAddress admin)
	{
		super(name, description, url, admin);
	}

	// ========================= IMPLEMENTATION: Object ====================

	// TODO: implement Named, equals() and hashCode by name or ID.

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("name",
				getName()).append("description", getDescription())
				.append("url", getUrl()).append("admin", getMainContact())
				// .append("registryCollection", registryCollection)
				.toString();
	}

	// ========================= IMPLEMENTATION: Subject ===================

	/**
	 * @param o
	 * @see edu.utah.further.core.api.observer.Subject#addObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void addObserver(final Observer o)
	{
		verifyType(o, Registry.class);
		registryCollection.add((Registry) o);
	}

	/**
	 * Unsubscribe an observer of this subject.
	 *
	 * @param o
	 *            observer to remove
	 * @see edu.utah.further.core.api.observer.Subject#removeObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void removeObserver(final Observer o)
	{
		verifyType(o, Registry.class);
		registryCollection.remove(o);
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
		for (final Registry observer : registryCollection)
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
		verifyType(message, SimpleDataMessage.class);
		final SimpleDataMessage request = (SimpleDataMessage) message;

		// Write a response message containing this source's name
		if (request.getType() == SimpleDataMessage.Type.DATA_REQUEST)
		{
			final SimpleDataMessage response = newResponseMessage(request, this,
					getName());
			if (log.isDebugEnabled())
			{
				log.debug("Sending response: " + response);
			}
			notifyObservers(response);
		}
	}

	// ========================= IMPLEMENTATION: Source ====================

	/**
	 * Register this source with a registry.
	 *
	 * @param registry
	 *            a registry
	 * @see edu.utah.further.core.util.registry.Source#registerWith(edu.utah.further.core.util.registry.Registry)
	 */
	@Override
	public void registerWith(final Registry registry)
	{
		registry.addObserver(this);
	}

	/**
	 * Unregister this source from a registry.
	 *
	 * @param registry
	 *            a registry
	 * @see edu.utah.further.core.util.registry.Source#unregisterFrom(edu.utah.further.core.util.registry.Registry)
	 */
	@Override
	public void unregisterFrom(final Registry registry)
	{
		registry.removeObserver(this);
	}

	// ========================= PRIVATE METHODS ===========================

}
