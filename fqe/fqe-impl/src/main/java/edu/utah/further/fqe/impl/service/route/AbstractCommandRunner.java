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
package edu.utah.further.fqe.impl.service.route;

import static edu.utah.further.fqe.ds.api.util.MessageHeader.COMMAND_ID;
import static edu.utah.further.fqe.ds.api.util.MessageHeader.COMMAND_TYPE;

import java.util.Map;
import java.util.UUID;

import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.camel.CamelUtil;
import edu.utah.further.fqe.ds.api.util.CommandType;

/**
 * A base class for FQE asynchronous command runners (builders).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 26, 2010
 */
abstract class AbstractCommandRunner<T> implements CommandRunner<T>
{
	// ========================= COSNTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(AbstractCommandRunner.class);

	// ========================= FIELDS ====================================

	/**
	 * Convenient for printouts. Set as the message header vaule.
	 */
	private final CommandType commandType;

	/**
	 * Internally-generated command message's ID.
	 */
	private final String queryId = UUID.randomUUID().toString();

	/**
	 * Input message headers.
	 */
	private final Map<String, Object> headers = CollectionUtil.newMap();

	/**
	 * Endpoint to send input message to.
	 */
	private final Endpoint requestEndpoint;

	/**
	 * ProducerTemplate for sending messages
	 */
	private final ProducerTemplate producerTemplate;

	/**
	 * Optional input message body. TODO: consider making this mandatory and validating
	 * that it is non-<code>null</code> builder.
	 */
	private Object body;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param commandType
	 * @param requestEndpoint
	 */
	protected AbstractCommandRunner(final CommandType commandType,
			final Endpoint requestEndpoint, final ProducerTemplate producerTemplate)
	{
		super();
		this.commandType = commandType;
		this.requestEndpoint = requestEndpoint;
		this.producerTemplate = producerTemplate;
		setInitialHeaders(commandType);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the body property.
	 * 
	 * @return the body
	 */
	protected final Object getBody()
	{
		return body;
	}

	/**
	 * Set a new value for the body property.
	 * 
	 * @param body
	 *            the body to set
	 */
	protected final void setBody(final Object body)
	{
		this.body = body;
	}

	/**
	 * Return the commandType property.
	 * 
	 * @return the commandType
	 */
	protected final CommandType getCommandType()
	{
		return commandType;
	}

	/**
	 * Return the queryId property.
	 * 
	 * @return the queryId
	 */
	protected final String getQueryId()
	{
		return queryId;
	}

	/**
	 * Return the headers property.
	 * 
	 * @return the headers
	 */
	protected final Map<String, Object> getHeaders()
	{
		return headers;
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	protected final void addHeaders(final Map<? extends String, ? extends Object> m)
	{
		headers.putAll(m);
	}

	/**
	 * Return the requestEndpoint property.
	 * 
	 * @return the requestEndpoint
	 */
	protected final Endpoint getRequestEndpoint()
	{
		return requestEndpoint;
	}

	/**
	 * Return the producerTemplate property
	 * 
	 * @return
	 */
	protected final ProducerTemplate getProducerTemplate()
	{
		return producerTemplate;
	}

	/**
	 * @param commandType
	 */
	private final void setInitialHeaders(final CommandType commandType)
	{
		CamelUtil.setHeader(headers, COMMAND_TYPE, commandType.getName());
		CamelUtil.setHeader(headers, COMMAND_ID, queryId);
	}
}
