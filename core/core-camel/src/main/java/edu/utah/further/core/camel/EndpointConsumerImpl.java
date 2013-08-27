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
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.observer.DataMessage;
import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;
import edu.utah.further.core.util.observer.DataTransmitter;

/**
 * A simple bridge between a Camel endpoint and observers of this class.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Feb 25, 2010
 */
@Implementation
@Service("endpointConsumer")
public final class EndpointConsumerImpl implements EndpointConsumer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(EndpointConsumerImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * A delegate that adds Observer Pattern functionality to this object.
	 */
	private final DataTransmitter<? extends EndpointConsumer> dataTransmitter = new DataTransmitter<>(
			this);

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * @param exchange
	 * @see edu.utah.further.core.camel.EndpointConsumer#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange)
	{
		if (log.isDebugEnabled())
		{
			log.debug("process() " + exchange);
		}
		final String key = exchange.getExchangeId();
		if (key == null)
		{
			log.debug("Ignoring exchange " + exchange + " without ID");
		}
		else
		{

			// Notify observers of the new exchange
			final DataMessage<? extends EndpointConsumer, Exchange> message = dataTransmitter
					.newMessage(Exchange.class);
			message.setEntity(exchange);
			notifyObservers(message);
		}

		// Pass exchange on intact
		CamelUtil.copyBodyAndHeaders(exchange);

		// return exchange;
	}

	// ========================= IMPL: Subject =============================

	/**
	 * @param o
	 * @see edu.utah.further.core.util.observer.SimpleSubject#addObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void addObserver(final Observer o)
	{
		dataTransmitter.addObserver(o);
	}

	/**
	 * @param message
	 * @see edu.utah.further.core.util.observer.SimpleSubject#notifyObservers(edu.utah.further.core.api.observer.Message)
	 */
	@Override
	public void notifyObservers(final Message message)
	{
		dataTransmitter.notifyObservers(message);
	}

	/**
	 * @param o
	 * @see edu.utah.further.core.util.observer.SimpleSubject#removeObserver(edu.utah.further.core.api.observer.Observer)
	 */
	@Override
	public void removeObserver(final Observer o)
	{
		dataTransmitter.removeObserver(o);
	}

	// ========================= METHODS ===================================

	// ========================= PRIVATE METHODS ===========================
}
