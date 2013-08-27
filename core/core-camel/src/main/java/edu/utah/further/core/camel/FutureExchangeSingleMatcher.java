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

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.observer.DataMessage;
import edu.utah.further.core.api.observer.Message;

/**
 * Monitors {@link EndpointConsumer} {@link Exchange}s and saves the first one that
 * matches search criteria, and then stops listening to exchanges.
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
public final class FutureExchangeSingleMatcher extends AbstractFutureExchange
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(FutureExchangeSingleMatcher.class);

	// ========================= FIELDS ====================================

	/**
	 * Defines search criteria for the exchange to find.
	 */
	private final ExchangeMatcher exchangeMatcher;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Start observing incoming messages from a consumer.
	 * 
	 * @param endpointConsumer
	 *            Camel endpoint consumer
	 * @param exchangeMatcher
	 *            defines search criteria for the exchange to find
	 */
	public FutureExchangeSingleMatcher(final EndpointConsumer endpointConsumer,
			final ExchangeMatcher exchangeMatcher)
	{
		super(endpointConsumer);
		this.exchangeMatcher = exchangeMatcher;
	}

	// ========================= IMPL: Observer ============================

	/**
	 * @param message
	 * @see edu.utah.further.core.api.observer.Observer#update(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final void update(final Message message)
	{
		if (log.isDebugEnabled())
		{
			// Received an exchange, but not hours
			log.debug("update() " + message);
		}
		if (getEndpointConsumer().equals(message.getSubject()))
		{
			final DataMessage<EndpointConsumer, Exchange> exchangeMessage = (DataMessage<EndpointConsumer, Exchange>) message;
			final Exchange receivedExchange = exchangeMessage.getEntity();
			if (log.isDebugEnabled())
			{
				// Received an exchange, but not hours
				log.debug("Received exchange: " + receivedExchange);
			}
			if (exchangeMatcher.match(receivedExchange))
			{
				// Found our exchange!
				log.debug("Saving matching exchange: " + receivedExchange);
				setExchange(receivedExchange);

				// Stop observing exchanges and endpoint consumer
				cancel(true);
			}
		}
	}
}
