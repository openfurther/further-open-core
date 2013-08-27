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
package edu.utah.further.core.util.registry;

import static edu.utah.further.core.api.message.ValidationUtil.validateEqualClass;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.core.util.registry.SimpleDataMessage.Type.DATA_REQUEST;
import static edu.utah.further.core.util.registry.SimpleDataMessage.Type.RESPONSE;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.observer.DataMessage;
import edu.utah.further.core.api.observer.Observer;

/**
 * An observer pattern message that signals a change in a task's state.
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
 * @version May 29, 2009
 */
final class SimpleDataMessageImpl extends DataMessage<Node, Object> implements SimpleDataMessage
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Message unique identifier.
	 */
	private final UUID id;

	/**
	 * The type of this message
	 */
	private final Type type;

	/**
	 * The original request message, if this message is sent in reply to that original
	 * message. Otherwise, returns <code>null</code>.
	 */
	private final SimpleDataMessage originalMessage;

	/**
	 * Type of state for which we report a change in this message.
	 */
	private final List<String> data;

	/**
	 * Observer to notify of this message's status.
	 */
	private final Observer observer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a data message in reply to another message.
	 *
	 * @param type
	 * @param originalMessage
	 * @param node
	 * @param data
	 * @param observer
	 */
	private SimpleDataMessageImpl(final Type type,
			final SimpleDataMessage originalMessage, final Node node,
			final List<String> data, final Observer observer)
	{
		super(node);
		this.id = UUID.randomUUID();
		this.type = type;
		this.originalMessage = originalMessage;
		this.data = data;
		this.observer = observer;
	}

	/**
	 * A factory method: create a data request message.
	 *
	 * @param node
	 * @param data
	 */
	public static SimpleDataMessage newRequestMessage(final Node node,
			final List<String> data, final Observer observer)
	{
		return new SimpleDataMessageImpl(DATA_REQUEST, null, node, data, observer);
	}

	/**
	 * A factory method: create a data request message.
	 *
	 * @param node
	 * @param data
	 */
	public static SimpleDataMessage newRequestMessage(final Node node, final String data,
			final Observer observer)
	{
		return newRequestMessage(node, Arrays.asList(data), observer);
	}

	/**
	 * A factory method: create a data response message.
	 *
	 * @param node
	 * @param data
	 */
	public static SimpleDataMessage newResponseMessage(
			final SimpleDataMessage originalMessage, final Node node,
			final List<String> data)
	{
		return new SimpleDataMessageImpl(RESPONSE, originalMessage, node, data, null);
	}

	/**
	 * A factory method: create a data response message.
	 *
	 * @param node
	 * @param data
	 */
	public static SimpleDataMessage newResponseMessage(
			final SimpleDataMessage originalMessage, final Node node, final String data)
	{
		return newResponseMessage(originalMessage, node, Arrays.asList(data));
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see edu.utah.further.audit.api.domain.AbstractMessage#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(id).toHashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final SimpleDataMessageImpl that = (SimpleDataMessageImpl) obj;
		return new EqualsBuilder().append(id, that.id).isEquals();
	}

	/**
	 * Print debugging information on the message.
	 *
	 * @return debugging information on the message
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id).append("type", type).append("data", data).append(
						"reply-to", originalMessage);
		final Node subject = getSubject();
		if (subject != null)
		{
			builder.append("node", subject.getName());
		}
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Compare messages by IDs.
	 *
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final SimpleDataMessage o)
	{
		// Cast to friendlier version
		validateEqualClass(this, o);
		final SimpleDataMessageImpl other = (SimpleDataMessageImpl) o;
		return new CompareToBuilder().append(this.id, other.id).toComparison();
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @return
	 * @see edu.utah.further.core.util.misc.Identifiable#getId()
	 */
	@Override
	public UUID getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: TaskStateChangeMessage ====

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.SimpleDataMessage#getData()
	 */
	@Override
	public List<String> getData()
	{
		return data;
	}

	/**
	 * Returns the original message, if this message is sent in reply to that original
	 * message. Otherwise, returns <code>null</code>.
	 *
	 * @return theoriginal message
	 * @see edu.utah.further.core.util.registry.SimpleDataMessage#getOriginalMessageId()
	 */
	@Override
	public SimpleDataMessage getOriginalMessage()
	{
		return originalMessage;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.SimpleDataMessage#getType()
	 */
	@Override
	public Type getType()
	{
		return type;
	}

	/**
	 * Return the observer property.
	 *
	 * @return the observer
	 */
	@Override
	public Observer getObserver()
	{
		return observer;
	}

	// ========================= METHODS ===================================
}
