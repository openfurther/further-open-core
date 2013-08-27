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
package edu.utah.further.core.camel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A convenient base class for {@link EndpointConsumer} {@link Exchange} monitors.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Feb 26, 2010
 */
abstract class AbstractFutureExchange implements FutureExchange
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(AbstractFutureExchange.class);

	// ========================= FIELDS ====================================

	/**
	 * Endpoint consumer to observe.
	 */
	private final EndpointConsumer endpointConsumer;

	/**
	 * The first exchange that matches the ID.
	 */
	private Exchange exchange;

	/**
	 * Keeps track of whether this object's observation has been cancelled.
	 */
	private boolean cancelled = false;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Start observing incoming messages from a consumer.
	 * 
	 * @param endpointConsumer
	 *            Camel endpoint consumer
	 */
	public AbstractFutureExchange(final EndpointConsumer endpointConsumer)
	{
		super();
		this.endpointConsumer = endpointConsumer;

		// Start observing incoming messages
		endpointConsumer.addObserver(this);
	}

	// ========================= IMPL: PollingFuture =======================

	/**
	 * @param period
	 * @param timeout
	 * @param unit
	 * @return
	 * @see edu.utah.further.core.api.concurrent.PollingFuture#poll(long, long,
	 *      java.util.concurrent.TimeUnit)
	 */
	@Override
	public final Exchange poll(final long period, final long timeout, final TimeUnit unit)
	{
		for (int pollingTime = 0; pollingTime < timeout; pollingTime += period)
		{
			try
			{
				final Exchange result = get(period, unit);
				if (result != null)
				{
					return result;
				}
			}
			catch (final InterruptedException e)
			{
				return null;
			}
			catch (final ExecutionException e)
			{
				return null;
			}
			catch (final TimeoutException e)
			{
				// Ignore local timeout of this polling period, keep going
			}
		}
		return null;
	}

	// ========================= IMPL: Future<Exchange> ====================

	/**
	 * Stop observing incoming messages.
	 * 
	 * @param mayInterruptIfRunning
	 * @return <code>true</code>
	 * @see java.util.concurrent.Future#cancel(boolean)
	 */
	@Override
	public final boolean cancel(final boolean mayInterruptIfRunning)
	{
		// Stop observing incoming messages
		endpointConsumer.removeObserver(this);
		cancelled = true;
		return true;
	}

	/**
	 * Return the matching exchange.
	 * 
	 * @return matching exchange, or <code>null</code> if it wasn't found yet.
	 * @throws InterruptedException
	 *             not thrown
	 * @throws ExecutionException
	 * @see java.util.concurrent.Future#get()
	 */
	@Override
	public final Exchange get() throws InterruptedException, ExecutionException
	{
		return exchange;
	}

	/**
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public final Exchange get(final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException
	{
		Thread.sleep(unit.toSeconds(timeout));
		return get();
	}

	/**
	 * @return
	 * @see java.util.concurrent.Future#isCancelled()
	 */
	@Override
	public final boolean isCancelled()
	{
		return cancelled;
	}

	/**
	 * @return
	 * @see java.util.concurrent.Future#isDone()
	 */
	@Override
	public final boolean isDone()
	{
		return (exchange != null);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set a new value for the exchange property.
	 * 
	 * @param exchange
	 *            the exchange to set
	 */
	protected final void setExchange(final Exchange exchange)
	{
		this.exchange = exchange;
	}

	/**
	 * Return the endpointConsumer property.
	 * 
	 * @return the endpointConsumer
	 */
	protected final EndpointConsumer getEndpointConsumer()
	{
		return endpointConsumer;
	}
}
