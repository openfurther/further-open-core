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
package edu.utah.further.ds.api.request;

import static edu.utah.further.ds.api.util.AttributeName.META_DATA;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static edu.utah.further.ds.api.util.AttributeName.SEARCH_QUERY;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessage;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.ds.api.util.StatusMetaDataUtil;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * A query execution request is a type which holds inputs and outputs of a query
 * execution. A {@link QueryExecutionRequest} uses the Decorator and Delegation patterns
 * to enhance a {@link ChainRequest} while adding new standard methods for retrieving
 * typed attribute values, such as getResult().
 *
 * The results of all {@link RequestProcessor} are stored and retrieved using
 * {@link QueryExecutionRequest#setResult(Object)} and
 * {@link QueryExecutionRequest#getResult()}
 *
 * Additionally, this class is <i>designed</i> to be subclassed with the Decorator and
 * Delegation patterns to provide typed retrieval and setting of inputs and outputs
 * instead of dealing directly with associated attribute map provided by
 * {@link ChainRequest}
 *
 * Typically request inputs and outputs will be grouped into one subclass of
 * {@link QueryExecutionRequest} and several {@link RequestProcessor}'s will operate with
 * this request subclass (see <code>HibernateExecReq</code> or
 * <code>WebserviceExecReq</code>).
 *
 * All subclasses must implement the constructor
 * {@link QueryExecutionRequest#QueryExecutionRequest(ChainRequest)}
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
 * @version Sep 28, 2009
 */
public class QueryExecutionRequest implements ChainRequest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(QueryExecutionRequest.class);

	// ========================= FIELDS ==================================

	/**
	 * The wrapped chain request
	 */
	private final ChainRequest chainRequest;

	// ========================= CONSTRUCTORS ============================

	/**
	 * Constructor
	 *
	 * @param chainRequest
	 *            the chain request of this execution
	 */
	public QueryExecutionRequest(final ChainRequest chainRequest)
	{
		this.chainRequest = chainRequest;
	}

	// ============== IMPLEMENTATION: QueryExecutionRequest ===============

	/**
	 * Gets the chain request associated with this execution
	 *
	 * @return the chainRequest
	 */
	public final ChainRequest getChainRequest()
	{
		return chainRequest;
	}

	/**
	 * Gets the result of an execution
	 *
	 * @param <T>
	 *            the type of the result
	 * @return the result of type T
	 */
	public final <T> T getResult()
	{
		// chainRequest.<T> required otherwise Sun javac complains, even though Eclipse
		// does not.
		// @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
		// @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=98379
		return chainRequest.<T> getAttribute(QUERY_RESULT);
	}

	/**
	 * Sets the result of an execution
	 *
	 * @param obj
	 *            the result
	 */
	public final void setResult(final Object obj)
	{
		chainRequest.setAttribute(QUERY_RESULT, obj);
	}

	/**
	 * @return the searchQuery
	 */
	public final SearchQuery getSearchQuery()
	{
		return chainRequest.getAttribute(SEARCH_QUERY);
	}

	/**
	 * @param searchQuery
	 *            the searchQuery to set
	 */
	public final void setSearchQuery(final SearchQuery searchQuery)
	{
		chainRequest.setAttribute(SEARCH_QUERY, searchQuery);
	}

	/**
	 * Return the queryContext property.
	 *
	 * @return the queryContext
	 */
	public final QueryContext getQueryContext()
	{
		return chainRequest.getAttribute(AttributeName.QUERY_CONTEXT);
	}

	/**
	 * Sets the status of the execution request.
	 *
	 * @param status
	 *            the status to set
	 */
	public final void setStatus(final String status)
	{
		final QueryContext queryContext = getQueryContext();
		if (queryContext == null)
		{
			// This should only happen in isolated unit tests of executors where the full
			// query context is not available
			log.warn("QueryContext was not found in request, cannot set status. "
					+ "If this isn't an isolated test, this is a bug");
			return;
		}

		// Prepare new query context
		final QueryContext newQueryContext = QueryContextToImpl.newCopy(queryContext);
		final DsMetaData metaData = chainRequest.getAttribute(META_DATA);
		// Do not time execution processor here for now
		StatusMetaDataUtil.setCurrentStatus(newQueryContext, metaData.getName(), status,
				0l);
		setQueryContext(queryContext);
	}

	// ============== IMPLEMENTATION: SeverityMessageContainer ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.message.SeverityMessageContainer#addMessage(edu.utah.
	 * further.core.api.message.Severity, java.lang.String)
	 */
	@Override
	public final void addMessage(final Severity severity, final String text)
	{
		chainRequest.addMessage(severity, text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.message.SeverityMessageContainer#addMessages(edu.utah
	 * .further.core.api.message.SeverityMessageContainer)
	 */
	@Override
	public final void addMessages(final SeverityMessageContainer other)
	{
		chainRequest.addMessages(other);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.message.SeverityMessageContainer#getMessages(edu.utah
	 * .further.core.api.message.Severity)
	 */
	@Override
	public final Set<SeverityMessage> getMessages(final Severity severity)
	{
		return chainRequest.getMessages(severity);
	}

	// ============== IMPLEMENTATION: MessageContainer===============

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.message.MessageContainer#addMessage(java.lang.Object)
	 */
	@Override
	public final void addMessage(final SeverityMessage message)
	{
		chainRequest.addMessage(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#clearMessages()
	 */
	@Override
	public final void clearMessages()
	{
		chainRequest.clearMessages();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#getAsList()
	 */
	@Override
	public final List<SeverityMessage> getAsList()
	{
		return chainRequest.getAsList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#getSize()
	 */
	@Override
	public final int getSize()
	{
		return chainRequest.getSize();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#isEmpty()
	 */
	@Override
	public final boolean isEmpty()
	{
		return chainRequest.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.message.MessageContainer#removeMessage(java.lang.Object)
	 */
	@Override
	public final void removeMessage(final SeverityMessage uuid)
	{
		chainRequest.removeMessage(uuid);
	}

	// ============== IMPLEMENTATION: Iterable ========================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public final Iterator<SeverityMessage> iterator()
	{
		return chainRequest.iterator();
	}

	// ============== IMPLEMENTATION: AttributeContainer ===============

	/*
	 * chainRequest.<T> required otherwise Sun javac complains, however, Eclipse does not.
	 *
	 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
	 *
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=98379
	 *
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.AttributeContainer#getAttribute(java.lang.String)
	 */
	@Override
	public final <T> T getAttribute(final String name)
	{
		return chainRequest.<T> getAttribute(name);
	}

	/**
	 * @param <T>
	 * @param label
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(edu.utah.further.core.api.context.Labeled)
	 */
	@Override
	public final <T> T getAttribute(final Labeled label)
	{
		return chainRequest.<T> getAttribute(label);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributeNames()
	 */
	@Override
	public final Set<String> getAttributeNames()
	{
		return chainRequest.getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributes()
	 */
	@Override
	public final Map<String, Object> getAttributes()
	{
		return chainRequest.getAttributes();
	}

	/**
	 * @param attributes
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttributes(java.util.Map)
	 */
	@Override
	public final void setAttributes(final Map<String, ?> attributes)
	{
		chainRequest.setAttributes(attributes);
	}

	/**
	 * @param map
	 * @see edu.utah.further.core.api.chain.AttributeContainer#addAttributes(java.util.Map)
	 */
	@Override
	public void addAttributes(final Map<String, ?> map)
	{
		chainRequest.addAttributes(map);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAllAttributes()
	 */
	@Override
	public void removeAllAttributes()
	{
		chainRequest.removeAllAttributes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.AttributeContainer#removeAttribute(java.lang.String
	 * )
	 */
	@Override
	public final void removeAttribute(final String key)
	{
		chainRequest.removeAttribute(key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.AttributeContainer#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public final void setAttribute(final String key, final Object value)
	{
		chainRequest.setAttribute(key, value);
	}

	/**
	 * @param label
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(edu.utah.further.core.api.context.Labeled,
	 *      java.lang.Object)
	 */
	@Override
	public final void setAttribute(final Labeled label, final Object value)
	{
		chainRequest.setAttribute(label, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.ChainRequest#setException(java.lang.Throwable)
	 */
	@Override
	public final void setException(final Throwable throwable)
	{
		// Exception handling not yet supported in QueryExecutionRequest
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.ChainRequest#getException()
	 */
	@Override
	public Throwable getException()
	{
		// Exception handling not yet supported in QueryExecutionRequest
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.ChainRequest#hasException()
	 */
	@Override
	public boolean hasException()
	{
		return getException() != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.ChainRequest#cancel()
	 */
	@Override
	public void cancel()
	{
		throw new UnsupportedOperationException(
				"Canceling a QueryExecutionRequest is not supported");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.ChainRequest#isCanceled()
	 */
	@Override
	public boolean isCanceled()
	{
		log.warn("Canceling a QueryExecutionRequest is not supported");
		return false;
	}

	// ============== PRIVATE METHODS =================================

	/**
	 * @param queryContext
	 */
	private void setQueryContext(final QueryContext queryContext)
	{
		chainRequest.setAttribute(QUERY_CONTEXT, queryContext);
	}
}
