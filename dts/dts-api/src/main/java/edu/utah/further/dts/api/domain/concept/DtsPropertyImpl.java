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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A DTS concept property, which is a mutable key-value pair. Marshalled to/from XML using
 * JAXB.
 * <p>
 * Note: in principle, the business object should be immutable and we should create a
 * separate TO. Not a big deal. This object also serves as the TO for now.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsPropertyImpl.ENTITY_NAME, propOrder =
{ "name", "value" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsPropertyImpl.ENTITY_NAME)
public class DtsPropertyImpl implements DtsProperty
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "property";

	// ========================= FIELDS ====================================

	/**
	 * Property name.
	 */
	@XmlElement(name = "name", required = false)
	private String name;

	/**
	 * Property value.
	 */
	@XmlElement(name = "value", required = false)
	private String value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for JAXB.
	 */
	public DtsPropertyImpl()
	{
		this(null, null);
	}

	/**
	 * Create a property.
	 *
	 * @param name
	 *            property name
	 * @param value
	 *            property value
	 */
	public DtsPropertyImpl(final String name, final String value)
	{
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other
	 *            property to copy fields from
	 */
	public DtsPropertyImpl(final DtsProperty other)
	{
		super();
		this.name = other.getName();
		this.value = other.getValue();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Properties are equal if and only if their names are equal.
	 *
	 * @param o
	 *            another object
	 * @return result of equality by name
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}

		if (!ReflectionUtil.instanceOf(o, DtsPropertyImpl.class))
		{
			return false;
		}

		final DtsPropertyImpl that = (DtsPropertyImpl) o;
		return new EqualsBuilder().append(getName(), that.getName()).isEquals();
	}

	/**
	 * Property hash code is computed from the name field only.
	 *
	 * @return hash code based on the name field
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("name", getName())
				.append("value", getValue())
				.toString();
	}

	// ========================= IMPLEMENTATION: Comparable<DtsProperty> ===

	/**
	 * Compare two pairs by lexicographic name order.
	 *
	 * @param other
	 *            the other {@link DtsProperty} to be compared with this one.
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final DtsProperty other)
	{
		return new CompareToBuilder()
				.append(this.getName(), other.getName())
				.toComparison();
	}

	// ========================= IMPLEMENTATION: DtsProperty ===============

	/**
	 * Return the name property.
	 *
	 * @return the name
	 * @see edu.utah.further.core.api.collections.Parameter#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Return the value property.
	 *
	 * @return the value
	 */
	@Override
	public String getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the name property.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Set a new value for the value property.
	 *
	 * @param value
	 *            the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

}
