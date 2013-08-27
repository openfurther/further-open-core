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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Aggregate all exchanges into a single combined Exchange holding all the aggregated
 * exchanges in a {@link java.util.List} as a exchange property with the key
 * {@link org.apache.camel.Exchange#GROUPED_EXCHANGE}. Based on Camel's Revision: 791088
 * default aggregation strategy.
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
 * @version Feb 26, 2010
 */
public final class FederatedAggregationStrategy implements AggregationStrategy
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(FederatedAggregationStrategy.class);

	/**
	 * The body assigned to the aggregated exchange.
	 */
	public static final String FEDERATED_BODY = "Federated";

	// ========================= IMPL: AggregationStrategy =================

	/**
	 * @param oldExchange
	 * @param newExchange
	 * @return
	 * @see org.apache.camel.processor.aggregate.AggregationStrategy#aggregate(org.apache.camel.Exchange,
	 *      org.apache.camel.Exchange)
	 */
	@Override
	public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange)
	{
		List<Exchange> list;
		Exchange answer = oldExchange;

		if (oldExchange == null)
		{
			answer = new DefaultExchange(newExchange);
			list = CollectionUtil.newList();
			answer.setProperty(Exchange.GROUPED_EXCHANGE, list);
		}
		else
		{
			list = oldExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
		}

		list.add(newExchange);
		CamelUtil.copyHeaders(newExchange, answer);
		answer.getOut().setBody(FEDERATED_BODY, String.class);
		return answer;
	}
}
