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

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.constant.Strings;

/**
 * A Camel processor that sets an aggregated message body to some function of the
 * individual message bodies. By default, concatenates all bodies, but can be composed
 * with any other {@link BodyAggregator} strategy.
 * <p>
 * TODO: this is a very rudimentary and potentially slow way of aggregation, consider XML
 * techniques if this becomes a problem.
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
 * @version Jul 3, 2009
 */
public final class BodyAggregationProcessor implements Processor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(BodyAggregationProcessor.class);

	// ========================= FIELDS ====================================

	/**
	 * Body aggregation strategy.
	 */
	private BodyAggregator bodyAggregator = new BodyAggregatorDelimitedConcatenation(
			Strings.EMPTY_STRING);

	// ========================= IMPLEMENTATION: Processor =================

	/**
	 * @param exchange
	 * @throws Exception
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception
	{
		// Aggregate bodies using bodyAggregator
		if (log.isDebugEnabled())
		{
			log.debug("Aggregating bodies");
		}
		final List<Exchange> exchanges = CamelUtil.getGroupedExchangeList(exchange);
		final List<String> exchangeBodies = CamelUtil.getExchangeInBodies(exchanges);
		final String aggregatedBody = bodyAggregator.aggregate(exchangeBodies);
		if (log.isDebugEnabled())
		{
			log.debug("Aggregated body = " + aggregatedBody);
		}

		// Update and return the exchange
		CamelUtil.copyHeaders(exchange, exchange);
		exchange.getOut().setBody(aggregatedBody, String.class);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the bodyAggregator property.
	 * 
	 * @param bodyAggregator
	 *            the bodyAggregator to set
	 */
	public void setBodyAggregator(final BodyAggregator bodyAggregator)
	{
		this.bodyAggregator = bodyAggregator;
	}

	// ========================= PRIVATE METHODS ===========================
}