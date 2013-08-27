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
package edu.utah.further.core.query.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A transfer object implementation of a JavaBean containing extra options passed to a
 * {@link SearchCriterion} beyond its basic search type.
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
 * @version Oct 10, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "matchType", "escapeChar", "ignoreCase" })
@XmlRootElement(namespace = XmlNamespace.CORE_QUERY, name = SearchCriterionTo.ENTITY_NAME)
final class SearchOptionsTo implements SearchOptions
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "options";

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * String expressions: match type (starts with/exact/...).
	 */
	@XmlElement(name = "matchType", required = false)
	private MatchType matchType;

	/**
	 * String expressions: escape character for value.
	 */
	@XmlElement(name = "escapeChar", required = false)
	private Character escapeChar;

	/**
	 * String expressions: is the search case insensitive.
	 */
	@XmlElement(name = "ignoreCase", required = false)
	private Boolean ignoreCase;

	// ========================= CONSTRUCTORS ==============================


	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	private SearchOptionsTo()
	{
		super();
	}

	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	public static SearchOptionsTo newInstance()
	{
		return new SearchOptionsTo();
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public static SearchOptionsTo newCopy(final SearchOptions other)
	{
		return new SearchOptionsTo().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
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
		final SearchOptionsTo that = (SearchOptionsTo) obj;
		return new EqualsBuilder()
				.append(this.matchType, that.matchType)
				.append(this.escapeChar, that.escapeChar)
				.append(this.ignoreCase, that.ignoreCase)
				.isEquals();
	}

	/**
	 * Two {@link SearchOptionsTo} objects must be equal <i>if and only if</i> their
	 * hash codes are equal. This is important because a dependent object (e.g. a query
	 * context) may decide whether to deep-copy its search query field based on hash code
	 * equality.
	 *
	 * @return hash code
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(this.matchType)
				.append(this.escapeChar)
				.append(this.ignoreCase)
				.toHashCode();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Returns a deep-copy of the argument into this object. This object is usually
	 * constructed with a no-argument constructor first, and then this method is called to
	 * copy fields into it.
	 *
	 * @param other
	 *            object to copy
	 * @return this object, for chaining
	 * @see edu.utah.further.core.misc.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public SearchOptionsTo copyFrom(final SearchOptions other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy fields
		this.setEscapeChar(other.getEscapeChar());
		this.setIgnoreCase(other.isIgnoreCase());
		this.setMatchType(other.getMatchType());

		return this;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getMatchType()
	 */
	@Override
	public MatchType getMatchType()
	{
		return matchType;
	}

	/**
	 * @param matchType
	 * @see edu.utah.further.core.query.domain.SearchOptions#setMatchType(edu.utah.further.core.query.domain.MatchType)
	 */
	@Override
	public SearchOptionsTo setMatchType(final MatchType matchType)
	{
		this.matchType = matchType;
		return this;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getEscapeChar()
	 */
	@Override
	public Character getEscapeChar()
	{
		return escapeChar;
	}

	/**
	 * @param escapeChar
	 * @see edu.utah.further.core.query.domain.SearchOptions#setEscapeChar(java.lang.Character)
	 */
	@Override
	public SearchOptionsTo setEscapeChar(final Character escapeChar)
	{
		this.escapeChar = escapeChar;
		return this;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#isIgnoreCase()
	 */
	@Override
	public Boolean isIgnoreCase()
	{
		return ignoreCase;
	}

	/**
	 * @param ignoreCase
	 * @see edu.utah.further.core.query.domain.SearchOptions#setIgnoreCase(boolean)
	 */
	@Override
	public SearchOptionsTo setIgnoreCase(final Boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
		return this;
	}

	// ========================= PRIVATE METHODS ===========================

}
