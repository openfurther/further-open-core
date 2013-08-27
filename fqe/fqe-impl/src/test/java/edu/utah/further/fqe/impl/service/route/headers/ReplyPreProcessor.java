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
package edu.utah.further.fqe.impl.service.route.headers;

import javax.jms.Destination;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes aggregated replies to FQE requests.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Feb 24, 2010
 */
public final class ReplyPreProcessor implements Processor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(ReplyPreProcessor.class);

	// ========================= IMPLEMENTATION: Processor =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception
	{
		log.debug("Reply pre-processing");

		// Extract and save the exchange.getMessageId and then set it on the exchange in
		// ReplyPostProcessor

		final Destination destination = exchange.getIn().getHeader("JMSReplyTo",
				Destination.class);
		log.debug("Destination : " + destination.toString());

		// I don't think this property has significance, it's just a key to ref the
		// destination and camel properties seem to hold throughout the route. Note, the
		// property must match for the aggregator.
		exchange.setProperty("org.apache.camel.jms.replyDestination", destination);
	}
}