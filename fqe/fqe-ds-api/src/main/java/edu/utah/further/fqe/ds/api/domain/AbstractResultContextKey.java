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
package edu.utah.further.fqe.ds.api.domain;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * A convenient JavaBean that holds the key fields of a result context, for map indexing.
 * This can be thought of as a view of a {@link ResultContext} object.
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
 * @version Oct 19, 2010
 */
@Api
@XmlTransient
public abstract class AbstractResultContextKey implements ResultContextKey
{
	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.appendSuper(super.toString())
				.append("type", getType())
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
				.append(getType())
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
		// ------------------------------------------------
		// Works only because this method is final!!!
		// ------------------------------------------------
		if (!ReflectionUtil.instanceOf(o, AbstractResultContextKey.class))
			return false;

		final ResultContextKey that = (ResultContextKey) o;
		return new EqualsBuilder()
				.append(this.getType(), that.getType())
				.isEquals();
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two keys. keys are ordered by type, then by intersection index.
	 *
	 * @param that
	 *            the other {@link ResultContextKey} object
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final ResultContextKey that)
	{
		return new CompareToBuilder()
				.append(this.getType(), that.getType())
				.toComparison();
	}

	// ========================= GETTERS & SETTERS =========================

	// ========================= PRIVATE METHODS ===========================
}
