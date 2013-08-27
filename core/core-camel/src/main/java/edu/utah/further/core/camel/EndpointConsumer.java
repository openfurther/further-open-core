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
import org.apache.camel.Processor;

import edu.utah.further.core.api.observer.Subject;

/**
 * A bridge between a Camel endpoint and a cache. Useful to simulate a Camel request-reply
 * cycle that cannot be achieved through Camel alone. Typically, an
 * {@link EndpointConsumer} would be linked to the output endpoint of a flow and save the
 * outgoing results; then the results are correlated with the original request message
 * using some unique identifier embedded in the {@link Exchange}.
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
public interface EndpointConsumer extends Subject, Processor
{
	// ========================= METHODS ===================================

	/**
	 * Save an exchange and pass it intact back to Camel.
	 * 
	 * @param exchange
	 *            exchange to process and save in the cache
	 */
	@Override
	void process(Exchange exchange);
}