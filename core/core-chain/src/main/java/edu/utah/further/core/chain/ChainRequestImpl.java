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
package edu.utah.further.core.chain;

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessage;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.message.SeverityMessageContainerImpl;

/**
 * A default and base implementation of a chain request. Delegates functionality to a
 * message container and an attribute container.
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
 * @version Sep 28, 2009
 */
public class ChainRequestImpl implements ChainRequest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ChainRequestImpl.class);

	/**
	 * Canceled attribute. This attribute is not public so it is not mistakenly
	 * overwritten.
	 */
	private static final String CANCELED = "request.canceled";

	// ========================= FIELDS ====================================

	/**
	 * Request attribute container.
	 */
	private AttributeContainer attributeContainer = new AttributeContainerImpl();

	/**
	 * Message container.
	 */
	private final SeverityMessageContainer severityMessageContainer = new SeverityMessageContainerImpl();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default Constructor
	 */
	public ChainRequestImpl()
	{
	}

	/**
	 * @param attributeContainer
	 */
	public ChainRequestImpl(final AttributeContainer attributeContainer)
	{
		super();
		this.attributeContainer = attributeContainer;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Equality is based on the request attributes only, not messages.
	 * SeverityMessageContainer
	 *
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ChainRequestImpl that = (ChainRequestImpl) obj;
		return new EqualsBuilder().append(this.attributeContainer,
				that.attributeContainer).isEquals();
	}

	/**
	 * Hash code is based on the request attributes only, not messages.
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(attributeContainer).toHashCode();
	}

	/**
	 * Print the attributes and messages in this request.
	 *
	 * @return textual representation of this request object
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return StringUtil
				.newStringBuilder()
				.append("===== Request =====")
				.append(NEW_LINE_STRING)
				.append(attributeContainer)
				.append(NEW_LINE_STRING)
				.append(severityMessageContainer)
				.toString();
	}

	// ========================= IMPLEMENTATION: ChainRequest ==============

	/**
	 * @param severity
	 * @param text
	 * @see edu.utah.further.core.api.message.SeverityMessageContainer#addMessage(edu.utah.further.core.api.message.Severity,
	 *      java.lang.String)
	 */
	@Override
	public void addMessage(final Severity severity, final String text)
	{
		severityMessageContainer.addMessage(severity, text);
	}

	/**
	 * @param other
	 * @see edu.utah.further.core.api.message.SeverityMessageContainer#addMessages(edu.utah.further.core.api.message.SeverityMessageContainer)
	 */
	@Override
	public void addMessages(final SeverityMessageContainer other)
	{
		severityMessageContainer.addMessages(other);
	}

	/**
	 * @param severity
	 * @return
	 * @see edu.utah.further.core.api.message.SeverityMessageContainer#getMessages(edu.utah.further.core.api.message.Severity)
	 */
	@Override
	public Set<SeverityMessage> getMessages(final Severity severity)
	{
		return severityMessageContainer.getMessages(severity);
	}

	/**
	 * @param message
	 * @see edu.utah.further.core.api.message.MessageContainer#addMessage(java.lang.Object)
	 */
	@Override
	public void addMessage(final SeverityMessage message)
	{
		severityMessageContainer.addMessage(message);
	}

	/**
	 *
	 * @see edu.utah.further.core.api.message.MessageContainer#clearMessages()
	 */
	@Override
	public void clearMessages()
	{
		severityMessageContainer.clearMessages();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.message.MessageContainer#getAsList()
	 */
	@Override
	public List<SeverityMessage> getAsList()
	{
		return severityMessageContainer.getAsList();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.message.MessageContainer#getSize()
	 */
	@Override
	public int getSize()
	{
		return severityMessageContainer.getSize();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.message.MessageContainer#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return severityMessageContainer.isEmpty();
	}

	/**
	 * @param uuid
	 * @see edu.utah.further.core.api.message.MessageContainer#removeMessage(java.lang.Object)
	 */
	@Override
	public void removeMessage(final SeverityMessage uuid)
	{
		severityMessageContainer.removeMessage(uuid);
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<SeverityMessage> iterator()
	{
		return severityMessageContainer.iterator();
	}

	/**
	 * @param <T>
	 * @param name
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(java.lang.String)
	 */
	@Override
	public <T> T getAttribute(final String name)
	{
		return attributeContainer.<T> getAttribute(name);
	}

	/**
	 * @param <T>
	 * @param label
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(edu.utah.further.core.api.context.Labeled)
	 */
	@Override
	public <T> T getAttribute(final Labeled label)
	{
		return attributeContainer.<T> getAttribute(label);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributeNames()
	 */
	@Override
	public Set<String> getAttributeNames()
	{
		return attributeContainer.getAttributeNames();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributes()
	 */
	@Override
	public Map<String, Object> getAttributes()
	{
		return attributeContainer.getAttributes();
	}

	/**
	 * @param attributes
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttributes(java.util.Map)
	 */
	@Override
	public void setAttributes(final Map<String, ?> attributes)
	{
		attributeContainer.setAttributes(attributes);
	}

	/**
	 * @param map
	 * @see edu.utah.further.core.api.chain.AttributeContainer#addAttributes(java.util.Map)
	 */
	@Override
	public void addAttributes(final Map<String, ?> map)
	{
		attributeContainer.addAttributes(map);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAllAttributes()
	 */
	@Override
	public void removeAllAttributes()
	{
		attributeContainer.removeAllAttributes();
	}

	/**
	 * @param key
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(final String key)
	{
		attributeContainer.removeAttribute(key);
	}

	/**
	 * @param key
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final String key, final Object value)
	{
		attributeContainer.setAttribute(key, value);
	}

	/**
	 * @param label
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(edu.utah.further.core.api.context.Labeled,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final Labeled label, final Object value)
	{
		attributeContainer.setAttribute(label, value);
	}

	/**
	 * Return the latest exception associated with this request.
	 *
	 * @return the latest exception instance
	 * @see edu.utah.further.core.api.chain.ChainRequest#getException()
	 */
	@Override
	public Throwable getException()
	{
		return this.<Throwable> getAttribute(EXCEPTION);
	}

	/**
	 * Set the latest exception associated with this request.
	 *
	 * @param throwable
	 *            exception class
	 * @see edu.utah.further.core.api.chain.ChainRequest#setException(java.lang.Throwable)
	 */
	@Override
	public void setException(final Throwable throwable)
	{
		this.setAttribute(EXCEPTION, throwable);
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
	public synchronized void cancel()
	{
		this.setAttribute(CANCELED, Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.ChainRequest#isCanceled()
	 */
	@Override
	public synchronized boolean isCanceled()
	{
		return (this.<Boolean> getAttribute(CANCELED) != null && this
				.<Boolean> getAttribute(CANCELED)
				.equals(Boolean.TRUE));
	}

}
