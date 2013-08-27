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

/**
 * Monitors {@link EndpointConsumer} {@link Exchange}s and saves the first one that
 * matches a specified exchange ID.
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
public final class ExchangeMatcherId implements ExchangeMatcher
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(ExchangeMatcherId.class);

	// ========================= FIELDS ====================================

	/**
	 * Exchange ID to look for in consumer notifications.
	 */
	private final String exchangeId;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Start observing incoming messages from a consumer.
	 * 
	 * @param exchangeId
	 *            Exchange ID to look for in consumer notifications
	 */
	public ExchangeMatcherId(final String exchangeId)
	{
		super();
		this.exchangeId = exchangeId;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Match an exchange based on ID.
	 * 
	 * @param receivedExchange
	 *            exchange to match
	 * @return <code>true</code> if and only if the exchange's ID matches
	 *         {@link #exchangeId}
	 * @see edu.utah.further.core.camel.ExchangeMatcher#match(org.apache.camel.Exchange)
	 */
	@Override
	public boolean match(final Exchange receivedExchange)
	{
		return exchangeId.equals(receivedExchange.getExchangeId());
	}
}
