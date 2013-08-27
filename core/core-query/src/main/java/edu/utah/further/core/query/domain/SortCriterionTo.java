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

import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A criterion that represents a result set ordering by a single field's value. Immutable.
 * This implementation is also a transfer object.
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
@Api
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "propertyName", "sortType" })
@XmlRootElement(namespace = XmlNamespace.CORE_QUERY, name = SortCriterionTo.ENTITY_NAME)
final class SortCriterionTo implements SortCriterion,
		CopyableFrom<SortCriterion, SortCriterionTo>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SortCriterionTo.class);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "sortCriterion";

	// ========================= FIELDS ====================================

	/**
	 * Property name to sort by.
	 */
	@XmlElement(name = "propertyName", required = true)
	private String propertyName;

	/**
	 * Sort type (e.g. ascending descending).
	 */
	@XmlElement(name = "sortType", required = true)
	private SortType sortType;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	private SortCriterionTo()
	{
		super();
	}

	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	public static SortCriterionTo newInstance()
	{
		return new SortCriterionTo();
	}

	/**
	 * Initialize a sort criterion from fields.
	 *
	 * @param propertyName
	 *            property name to sort by
	 * @param sortType
	 *            sort type (e.g. ascending descending)
	 */
	public SortCriterionTo(final String propertyName, final SortType sortType)
	{
		super();
		this.propertyName = propertyName;
		this.sortType = sortType;
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public static SortCriterionTo newCopy(final SortCriterion other)
	{
		return new SortCriterionTo().copyFrom(other);
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
	public SortCriterionTo copyFrom(final SortCriterion other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy fields
		this.setPropertyName(other.getPropertyName());
		this.setSortType(other.getSortType());

		return this;
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SortCriterionTo.SortCriterionToImpl#getPropertyName()
	 */
	public String getFieldName()
	{
		return propertyName;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SortCriterionTo.SortCriterionToImpl#getSortType()
	 */
	@Override
	public SortType getSortType()
	{
		return sortType;
	}

	/**
	 * Return the propertyName property.
	 *
	 * @return the propertyName
	 */
	@Override
	public String getPropertyName()
	{
		return propertyName;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param propertyName
	 * @see edu.utah.further.core.query.domain.SortCriterionTo#setPropertyName(java.lang.String)
	 */
	private void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	/**
	 * @param sortType
	 * @see edu.utah.further.core.query.domain.SortCriterionTo#setSortType(edu.utah.further.core.query.domain.SortType)
	 */
	private void setSortType(final SortType sortType)
	{
		this.sortType = sortType;
	}

}
