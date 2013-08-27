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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;

/**
 * ...
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
 * @version Nov 9, 2008
 */
public class RegistryClient implements Observer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RegistryClient.class);

	// ========================= FIELDS ====================================

	private final Registry registry;

	private SimpleDataMessage joinedData;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param registry
	 */
	public RegistryClient(final Registry registry)
	{
		super();
		this.registry = registry;
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
		if (log.isDebugEnabled())
		{
			log.debug("Received response: " + response);
		}
		joinedData = response;
	}

	// ========================= METHODS ===================================

	/**
	 * @param timeout
	 */
	public void sendDataRequest(final long timeout)
	{
		joinedData = null;
		UUID requestId = registry.sendDataRequest(this);
		sleep(timeout);

		if (joinedData != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Received an automatic response from the registry");
			}
		}
		else
		{
			registry.sendDataNotification(requestId, true);
			sleep(timeout);
			if (joinedData != null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Received a manual response from the registry");
				}
			}
			else
			{
				throw new ApplicationException("Registry failed to respond within "
						+ timeout + " ms");
			}
		}
	}

	private void sleep(final long timeout)
	{
		try
		{
			Thread.sleep(timeout);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Return the data property.
	 * 
	 * @return the data
	 */
	public SimpleDataMessage getJoinedData()
	{
		return joinedData;
	}

}
