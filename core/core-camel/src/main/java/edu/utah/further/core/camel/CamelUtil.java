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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Apache Camel EIP utilities.
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
 * @version Feb 25, 2010
 */
@Utility
public final class CamelUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CamelUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private CamelUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param endpoint
	 * @return
	 */
	public static Exchange getLatestResult(final MockEndpoint endpoint)
	{
		return endpoint.getExchanges().get(endpoint.getReceivedCounter() - 1);
	}

	/**
	 * @param endpoint
	 * @param index
	 * @return
	 */
	public static List<Exchange> getGroupedExchangeList(final MockEndpoint endpoint,
			final int index)
	{
		return getGroupedExchangeList(endpoint.getExchanges().get(index));
	}

	/**
	 * @param entity
	 * @return
	 */
	public static List<Exchange> getGroupedExchangeList(final Exchange entity)
	{
		return entity.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
	}

	/**
	 * @param endpoint
	 * @return
	 */
	public static String getGroupedExchangeBodiesAsString(final MockEndpoint endpoint)
	{
		final StringBuilder builder = StringUtil.newStringBuilder();
		for (int index = 0; index < endpoint.getExchanges().size(); index++)
		{
			final List<Exchange> group = getGroupedExchangeList(endpoint, index);
			builder.append("Group " + index + " " + getExchangeInBodies(group)).append(
					Strings.NEW_LINE_STRING);
		}
		return builder.toString();
	}

	/**
	 * Return the list of bodies of the IN parts of a list of exchanges.
	 * 
	 * @param exchanges
	 *            input message
	 * @return the list [exchanges[0].getIn()'s body,...,exchanges[end].getIn()'s body]
	 */
	public static List<String> getExchangeInBodies(
			final List<? extends Exchange> exchanges)
	{
		final List<String> bodies = CollectionUtil.newList();
		for (final Exchange exchange : exchanges)
		{
			bodies.add(exchange.getIn().getBody(String.class));
		}
		return bodies;
	}

	/**
	 * Copy an in-out exchange's IN body and headers into its OUT.
	 * 
	 * @param exchange
	 *            an in-out exchange
	 */
	public static void copyBodyAndHeaders(final Exchange exchange)
	{
		copyBodyAndHeaders(exchange, exchange);
	}

	/**
	 * Copy a source exchange's IN body and headers into a target exchange's OUT.
	 * 
	 * @param sourceExchange
	 *            source exchange to copy from
	 * @param targetExchange
	 *            target exchange to copy into
	 */
	public static void copyBodyAndHeaders(final Exchange sourceExchange,
			final Exchange targetExchange)
	{
		targetExchange.getOut().setBody(sourceExchange.getIn().getBody());
		copyHeaders(sourceExchange, targetExchange);
	}

	/**
	 * Copy a source exchange's IN headers into a target exchange's OUT.
	 * 
	 * @param sourceExchange
	 *            source exchange to copy from
	 * @param targetExchange
	 *            target exchange to copy into
	 */
	public static void copyHeaders(final Exchange sourceExchange,
			final Exchange targetExchange)
	{
		targetExchange.getOut().setHeaders(sourceExchange.getIn().getHeaders());
	}

	/**
	 * Return a header using a {@link Named} object to determine the header's name.
	 * 
	 * @param headers
	 *            headers map to update
	 * @param headerName
	 *            header name is this object's <code>getName()</code> method's return
	 *            value
	 * @return corresponding header value
	 * @see https://jira.chpc.utah.edu/browse/FUR-507
	 */
	public static Object getHeader(final Map<String, Object> headers,
			final Named headerName)
	{
		return headers.get(headerName.getName());
	}

	/**
	 * Return an message header using a {@link Named} object to determine the header's
	 * name.
	 * 
	 * @param message
	 *            camel exchange message
	 * @param headerName
	 *            header name is this object's <code>getName()</code> method's return
	 *            value
	 * @return corresponding header value
	 * @see https://jira.chpc.utah.edu/browse/FUR-507
	 */
	public static Object getHeader(final Message message, final Named headerName)
	{
		return message.getHeader(headerName.getName());
	}

	/**
	 * Set a header using a {@link Named} object to determine the header's name.
	 * 
	 * @param headers
	 *            headers map to update
	 * @param headerName
	 *            header name is this object's <code>getName()</code> method's return
	 *            value
	 * @param headerValue
	 *            method value
	 * @see https://jira.chpc.utah.edu/browse/FUR-507
	 */
	public static void setHeader(final Map<String, Object> headers,
			final String headerName, final Object headerValue)
	{
		headers.put(headerName, headerValue);
	}

	/**
	 * Set a header using a {@link Named} object to determine the header's name.
	 * 
	 * @param headers
	 *            headers map to update
	 * @param headerName
	 *            header name is this object's <code>getName()</code> method's return
	 *            value
	 * @param headerValue
	 *            method value
	 * @see https://jira.chpc.utah.edu/browse/FUR-507
	 */
	public static void setHeader(final Map<String, Object> headers,
			final Named headerName, final Object headerValue)
	{
		setHeader(headers, headerName.getName(), headerValue);
	}

	/**
	 * Set a message header using a {@link Named} object to determine the header's name.
	 * 
	 * @param message
	 *            message to update
	 * @param headerName
	 *            header name is this object's <code>getName()</code> method's return
	 *            value
	 * @param headerValue
	 *            method value
	 * @see https://jira.chpc.utah.edu/browse/FUR-507
	 */
	public static void setHeader(final Message message, final Named headerName,
			final Object headerValue)
	{
		message.setHeader(headerName.getName(), headerValue);
	}

	/**
	 * Return a Camel IN message's body as a typed entity. Assumes that the IN body is an
	 * instance of type <code>T</code>.
	 * 
	 * @param <T>
	 *            returned entity type parameter
	 * @param exchange
	 *            camel exchange
	 * @param returnType
	 *            returned entity class
	 * @param fallBackValue
	 *            fall back value to return if exchange's IN body is <code>null</code> or
	 *            cannot be converted to a <code>T</code>
	 * @return exchange's IN body as type <code>T</code>, or <code>fallBackValue</code> if
	 *         the latter is <code>null</code> or cannot be converted to a <code>T</code>
	 */
	public static <T> T getMessageBody(final Exchange exchange,
			final Class<T> returnType, final T fallBackValue)
	{
		if (exchange == null)
		{
			return fallBackValue;
		}
		final Message in = exchange.getIn();
		if (in == null)
		{
			return fallBackValue;
		}
		final T convertedBody = in.getBody(returnType);
		return (convertedBody == null) ? fallBackValue : convertedBody;
	}
}
