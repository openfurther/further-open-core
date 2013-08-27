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
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Part of the request-reply headers-based solution. 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Feb 25, 2010
 */
public final class ReplyPostProcessor implements Processor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(ReplyPostProcessor.class);

	@Autowired
	private ProducerTemplate producerTemplate;

	// ========================= IMPLEMENTATION: Processor =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception
	{
		log.debug("Post processing");
		log.debug(exchange.getProperties().toString());

		final Destination replyDestination = exchange.getProperty(
				"org.apache.camel.jms.replyDestination", Destination.class);

		// Could probably put this in a bean instead of an actual processor, camel can
		// call void someMethod(Exchange exchange)...

		// we pretend to send it to some non existing dummy queue
		producerTemplate.send("activemq:queue:dummy", new Processor()
		{
			@Override
			public void process(Exchange innerExchange) throws Exception
			{
				// and here we override the destination with the ReplyTo destination
				// object so the message is sent to there instead of dummy
				innerExchange.setIn(exchange.getIn());
				innerExchange.getIn().setHeader(JmsConstants.JMS_DESTINATION,
						replyDestination);
			}
		});

	}
}
