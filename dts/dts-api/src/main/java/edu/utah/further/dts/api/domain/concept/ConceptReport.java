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
package edu.utah.further.dts.api.domain.concept;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.PubliclyCloneable;
import edu.utah.further.core.api.message.Severity;

/**
 * A convenient JavaBean that holds concept information. Used by content validation
 * services.
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
 * @version Sep 3, 2010
 */
@Api
public final class ConceptReport implements PubliclyCloneable<ConceptReport>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Code to look concept by (usually a property value) within a namespace. Uniquely
	 * identifies the concept therein.
	 */
	private final String code;

	/**
	 * Result of content validation related to <code>code</code>.
	 */
	private Severity severity;

	/**
	 * Number of occurrences of <code>code</code> in the current context.
	 */
	private int numOccurrences = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param code
	 */
	public ConceptReport(final String code)
	{
		super();
		this.code = code;
	}

	/**
	 * @param code
	 * @param severity
	 * @param numOccurrences
	 */
	public ConceptReport(final String code, final Severity severity,
			final int numOccurrences)
	{
		super();
		this.code = code;
		this.severity = severity;
		this.numOccurrences = numOccurrences;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("code", code)
				.append("severity", severity)
				// .append("numOccurrences", numOccurrences)
				.toString();
	}

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see edu.utah.further.audit.api.domain.AbstractMessage#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(code)
				.append(severity)
				// .append(numOccurrences)
				.toHashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (getClass() != o.getClass())
			return false;

		final ConceptReport that = (ConceptReport) o;
		return new EqualsBuilder()
				.append(code, that.code)
				.append(severity, that.severity)
				// .append(numOccurrences, that.numOccurrences)
				.isEquals();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object.
	 *
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ConceptReport copy()
	{
		try
		{
			// Could alternatively call CoreUtil.clone(), but reflection is slower
			// than direct invocation
			return (ConceptReport) super.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			throw new InternalError("We are cloneable so we shouldn't be here!");
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the code property.
	 *
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

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
	 * Set a new value for the severity property.
	 *
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(final Severity severity)
	{
		this.severity = severity;
	}

	/**
	 * Return the numOccurrences property.
	 *
	 * @return the numOccurrences
	 */
	public int getNumOccurrences()
	{
		return numOccurrences;
	}

	/**
	 * Set a new value for the numOccurrences property.
	 *
	 * @param numOccurrences
	 *            the numOccurrences to set
	 */
	public void setNumOccurrences(final int numOccurrences)
	{
		this.numOccurrences = numOccurrences;
	}

	// ========================= METHODS ===================================
}
