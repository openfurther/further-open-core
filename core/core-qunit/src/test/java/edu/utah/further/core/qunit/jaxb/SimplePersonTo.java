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
package edu.utah.further.core.qunit.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.xml.jaxb.adapter.IntegerAttributeAdapter;
import edu.utah.further.core.xml.jaxb.adapter.RequiredIntegerAttributeAdapter;
import edu.utah.further.core.xml.jaxb.adapter.RequiredStringAttributeAdapter;
import edu.utah.further.core.xml.jaxb.adapter.StringAttributeAdapter;

/**
 * A simple person TO to illustrate attribute adapters.
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
 * @version Aug 18, 2010
 */
@XmlRootElement(name = SimplePersonTo.SIMPLE_PERSON)
@XmlAccessorType(XmlAccessType.FIELD)
// Note: JAXB does not guarantee any attribute ordering
public final class SimplePersonTo
{
	// ========================= CONSTANTS =================================

	/**
	 * The root element of this transfer object
	 */
	static final String SIMPLE_PERSON = "SimplePerson";

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SimplePersonTo.class);

	// ========================= FIELDS ====================================

	/**
	 * A required integer attribute example.
	 */
	@XmlAttribute(name = "requiredInt")
	@XmlJavaTypeAdapter(RequiredIntegerAttributeAdapter.class)
	private Integer requiredInt = null;

	/**
	 * An optional integer attribute example.
	 */
	@XmlAttribute(name = "optionalInt")
	@XmlJavaTypeAdapter(IntegerAttributeAdapter.class)
	private Integer optionalInt = null;

	/**
	 * A required string attribute example.
	 */
	@XmlAttribute(name = "requiredString")
	@XmlJavaTypeAdapter(RequiredStringAttributeAdapter.class)
	private String requiredString = null;

	/**
	 * An optional string attribute example.
	 */
	@XmlAttribute(name = "optionalString")
	@XmlJavaTypeAdapter(StringAttributeAdapter.class)
	private String optionalString = null;

	// ========================= IMPL: Object ==============================

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

		final SimplePersonTo that = (SimplePersonTo) obj;
		return new EqualsBuilder()
				.append(requiredInt, that.getRequiredInt())
				.append(optionalInt, that.getOptionalInt())
				.append(requiredString, that.getRequiredString())
				.append(optionalString, that.getOptionalString())
				.isEquals();
	}

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getRequiredInt())
				.append(getOptionalInt())
				.append(getRequiredString())
				.append(getOptionalString())
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= GET/SET ====================================

	/**
	 * Return the requiredInt property.
	 *
	 * @return the requiredInt
	 */
	public Integer getRequiredInt()
	{
		return requiredInt;
	}

	/**
	 * Set a new value for the requiredInt property.
	 *
	 * @param requiredInt
	 *            the requiredInt to set
	 */
	public void setRequiredInt(final Integer requiredInt)
	{
		this.requiredInt = requiredInt;
	}

	/**
	 * Return the optionalInt property.
	 *
	 * @return the optionalInt
	 */
	public Integer getOptionalInt()
	{
		return optionalInt;
	}

	/**
	 * Set a new value for the optionalInt property.
	 *
	 * @param optionalInt
	 *            the optionalInt to set
	 */
	public void setOptionalInt(final Integer optionalInt)
	{
		this.optionalInt = optionalInt;
	}

	/**
	 * Return the requiredString property.
	 *
	 * @return the requiredString
	 */
	public String getRequiredString()
	{
		return requiredString;
	}

	/**
	 * Set a new value for the requiredString property.
	 *
	 * @param requiredString
	 *            the requiredString to set
	 */
	public void setRequiredString(final String requiredString)
	{
		this.requiredString = requiredString;
	}

	/**
	 * Return the optionalString property.
	 *
	 * @return the optionalString
	 */
	public String getOptionalString()
	{
		return optionalString;
	}

	/**
	 * Set a new value for the optionalString property.
	 *
	 * @param optionalString
	 *            the optionalString to set
	 */
	public void setOptionalString(final String optionalString)
	{
		this.optionalString = optionalString;
	}
}
