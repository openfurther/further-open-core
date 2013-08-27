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
package edu.utah.further.core.api.lang;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A persistent entity for a time interval. Unclear at this point whether this can be
 * reused by different JAXB entities with field name overrides.
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
 * @resource Mar 19, 2009
 */
public class SimpleCloneable implements PubliclyCloneable<SimpleCloneable>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Starting point of this time interval.
	 */
	private int value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public SimpleCloneable()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
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
		final SimpleCloneable that = (SimpleCloneable) obj;
		return new EqualsBuilder().append(this.value, that.value).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(value).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("value", value)
				.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object.
	 *
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public SimpleCloneable copy()
	{
		return CoreUtil.clone(this);
	}

	// ========================= IMPLEMENTATION: TimeInterval ==============

	/**
	 * Return the value property.
	 *
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 *
	 * @param value
	 *            the value to set
	 */
	public void setValue(final int value)
	{
		this.value = value;
	}
}
