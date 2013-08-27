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
package edu.utah.further.core.api.message;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.FastDateFormat;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.api.lang.PubliclyCloneable;
import edu.utah.further.core.api.time.TimeService;

/**
 * An observer pattern message that signals a change in a task's state.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 23, 2009
 */
@Implementation
public final class SeverityMessage implements HasIdentifier<UUID>,
		Comparable<SeverityMessage>, PubliclyCloneable<SeverityMessage>
{
	// ========================= CONSTANTS =================================

	/**
	 * These fields handle date format in message printouts.
	 */
	public static final String PRETTY_DATE_FORMAT = "MMM dd, yyyy hh:mm:ss";

	/**
	 * A thread-safe version of SimpleDateFormat
	 */
	public static final FastDateFormat PRETTY_DATE_FORMATTER = FastDateFormat
			.getInstance(PRETTY_DATE_FORMAT);

	// ========================= NESTED TYPES ==============================

	/**
	 * Message unique identifier.
	 */
	private final UUID id;

	/**
	 * The type of this message
	 */
	private final Severity severity;

	/**
	 * Message text.
	 */
	private final String text;

	/**
	 * Time of message generation.
	 */
	private final Date date = TimeService.getDate();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a message.
	 * 
	 * @param severity
	 *            message severity level
	 * @param text
	 *            message text
	 */
	public SeverityMessage(final Severity severity, final String text)
	{
		this.id = UUID.randomUUID();
		this.severity = severity;
		this.text = text;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the hash code of this object. includes all fields.
	 * 
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
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

		final SeverityMessage that = (SeverityMessage) obj;
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
		return PRETTY_DATE_FORMATTER.format(date)
				+ String.format(" %-7s  %s", severity, text);
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two messages. Messages are ordered by decreasing severity
	 * order, then by increasing text order.
	 * 
	 * @param other
	 *            the other {@link SeverityMessage} object
	 * @return the result of comparison
	 */
	@Override
	public int compareTo(final SeverityMessage other)
	{
		return new CompareToBuilder()
				.append(other.severity, this.severity)
				.append(this.text, other.text)
				.toComparison();
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

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object.
	 * 
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public SeverityMessage copy()
	{
		return new SeverityMessage(getSeverity(), getText());
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the severity property.
	 * 
	 * @return the severity
	 */
	public Severity getSeverity()
	{
		return severity;
	}

	/**
	 * Return the text property.
	 * 
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Return the date property.
	 * 
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}
}
