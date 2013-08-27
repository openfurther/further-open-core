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

import static edu.utah.further.core.camel.CamelUtil.setHeader;
import static edu.utah.further.fqe.ds.api.util.MessageHeader.COMMAND_ID;

import java.util.Map;
import java.util.UUID;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.discrete.HasQueryIdentifier;

/**
 * Creates and maintain Camel exchange headers and body to be used by a single query's
 * flow.
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
 * @version Feb 22, 2010
 */
final class QueryContextMock implements HasQueryIdentifier<String>
{
	// ========================= FIELDS ====================================

	/**
	 * Message headers.
	 */
	private final Map<String, Object> headers;

	/**
	 * Message body.
	 */
	private final String body;

	/**
	 * Used for data source response federation (aggregation).
	 */
	private final String queryId;

	/**
	 * The special message header value identifying the sender of this message (=its
	 * context: federated/data source/etc.).
	 */
	private final MessageContext messageContext;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Generates a unique query ID.
	 * 
	 * @param messageContext
	 * @param body
	 */
	public QueryContextMock(final MessageContext messageContext, final String body)
	{
		this(messageContext, body, UUID.randomUUID().toString());
	}

	/**
	 * @param messageContext
	 * @param body
	 * @param queryId
	 */
	public QueryContextMock(final MessageContext messageContext, final String body,
			final String queryId)
	{
		super();

		this.messageContext = messageContext;
		this.body = body;
		this.queryId = queryId;

		headers = CollectionUtil.newMap();
		setHeader(headers, MessageContext.Constants.HEADER_NAME, messageContext
				.toString());
		setHeader(headers, COMMAND_ID, queryId);
	}
	
	// ========================= GETTERS & SETTERS =========================

	/**
	 * @param queryId
	 * @see edu.utah.further.core.api.discrete.HasQueryIdentifier#setQueryId(java.lang.Comparable)
	 */
	@Override
	public void setQueryId(final String queryId)
	{
		throw new UnsupportedOperationException("Query ID is final and cannot be set");
	}

	/**
	 * Return the headers property.
	 * 
	 * @return the headers
	 */
	public Map<String, Object> getHeaders()
	{
		return headers;
	}

	/**
	 * Return the body property.
	 * 
	 * @return the body
	 */
	public String getBody()
	{
		return body;
	}

	/**
	 * Return the queryId property.
	 * 
	 * @return the queryId
	 */
	@Override
	public String getQueryId()
	{
		return queryId;
	}

	/**
	 * Return the messageContext property.
	 * 
	 * @return the messageContext
	 */
	public MessageContext getMessageContext()
	{
		return messageContext;
	}

}
