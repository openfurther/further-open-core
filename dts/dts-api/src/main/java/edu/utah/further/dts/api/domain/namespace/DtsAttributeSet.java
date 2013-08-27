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
package edu.utah.further.dts.api.domain.namespace;

import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.ALL_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.NO_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.PROPERTIES;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * A wrapper around Apelon's <code>DtsAttributeSet</code>.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 22, 2008
 */
@Api
public final class DtsAttributeSet implements PubliclyCloneable<DtsAttributeSet>
{
	// ========================= CONSTANTS =================================

	/**
	 * Convenient shortcuts that can also be reused rather than creating a lot of
	 * identical objects. These should be used except in aspects or special cases where
	 * the other flags of this object need to be changed.
	 */
	public static DtsAttributeSet DEFAULT_ALL_ATTRIBUTES = new DtsAttributeSet(
			ALL_ATTRIBUTES);
	public static DtsAttributeSet DEFAULT_NO_ATTRIBUTES = new DtsAttributeSet(
			NO_ATTRIBUTES);
	public static DtsAttributeSet DEFAULT_PROPERTIES = new DtsAttributeSet(PROPERTIES);

	// ========================= FIELDS ====================================

	/**
	 * Concept attribute set type to retrieve.
	 */
	private final DtsAttributeSetType type;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param type
	 */
	public DtsAttributeSet(final DtsAttributeSetType type)
	{
		this.type = type;
	}

	// ========================= IMPLEMENTATION: Object ====================

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

		final DtsAttributeSet that = (DtsAttributeSet) o;
		return new EqualsBuilder().append(this.type, that.type).isEquals();
	}

	/**
	 * Return the hash code of this object. Includes the name field only.
	 *
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(type).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object.
	 *
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public DtsAttributeSet copy()
	{
		try
		{
			// Could alternatively call CoreUtil.clone(), but reflection is slower
			// than direct invocation
			return (DtsAttributeSet) super.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			throw new InternalError("We are cloneable so we shouldn't be here!");
		}
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the type property.
	 *
	 * @return the type
	 */
	public DtsAttributeSetType getType()
	{
		return type;
	}
}
