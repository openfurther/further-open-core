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
package edu.utah.further.dts.api.service;

import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.ALL_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.NO_ATTRIBUTES;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType.PROPERTIES;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.lang.PubliclyCloneable;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType;

/**
 * A JavaBean that holds options for a variety of DTS services.
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
 * @version Oct 19, 2009
 */
public class DtsOptions implements PubliclyCloneable<DtsOptions>
{
	// ========================= CONSTANTS =================================

	/**
	 * Convenient <strong>immutable</strong> shortcuts that can also be reused rather than
	 * creating a lot of identical objects. These should be used except in aspects or
	 * special cases where the other flags of this object need to be changed.
	 */
	public static ImmutableDtsOptions DEFAULT = new ImmutableDtsOptions();

	public static ImmutableDtsOptions DEFAULT_ALL_ATTRIBUTES = new ImmutableDtsOptions(
			ALL_ATTRIBUTES);

	public static ImmutableDtsOptions DEFAULT_NO_ATTRIBUTES = new ImmutableDtsOptions(
			NO_ATTRIBUTES);

	public static ImmutableDtsOptions DEFAULT_PROPERTIES = new ImmutableDtsOptions(
			PROPERTIES);

	// ========================= FIELDS ====================================

	/**
	 * Signals the DTS service methods to throw an exception upon failure to find a
	 * concept, or gracefully return a <code>null</code> or a fallback value.
	 */
	private boolean throwExceptionOnFailure = false;

	/**
	 * Concept attribute set to retrieve.
	 */
	private DtsAttributeSet attributeSet = DtsAttributeSet.DEFAULT_NO_ATTRIBUTES;

	// Default option values
	private String targetNamespace = null;
	private Boolean standardSourceNamespace = null;

	/**
	 * The number of attributes to retrieve when querying DTS. This value is the same as
	 * the default value in ConceptAttributeSetDescriptor.
	 */
	private int attributeSetLimit = 100;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets no fields.
	 */
	public DtsOptions()
	{
		super();
	}

	/**
	 * @param attributeSet
	 */
	public DtsOptions(final DtsAttributeSetType type)
	{
		this();
		// Do not call setter because it might be overidden by sub-classes
		this.attributeSet = new DtsAttributeSet(type);
	}

	/**
	 * Sets all fields.
	 * 
	 * @param targetNamespace
	 * @param standardSourceNamespace
	 */
	public DtsOptions(final String targetNamespace, final Boolean standardSourceNamespace)
	{
		super();
		this.targetNamespace = targetNamespace;
		this.standardSourceNamespace = standardSourceNamespace;
	}

	/**
	 * A factory method from an exception flag value.
	 * 
	 * @param throwExceptionOnFailure
	 *            throws an exception in DTS services if and only if this flag is
	 *            <code>true</code>
	 * @return options instance corresponding to the flag. Rest of the parameters use
	 *         default values
	 */
	public static DtsOptions newInstanceFromThrowExceptionOnFailure(
			final boolean throwExceptionOnFailure)
	{
		return new DtsOptions().setThrowExceptionOnFailure(throwExceptionOnFailure);
	}

	/**
	 * A factory method from an exception flag value.
	 * 
	 * @param throwExceptionOnFailure
	 *            throws an exception in DTS services if and only if this flag is
	 *            <code>true</code>
	 * @return options instance corresponding to the flag. Rest of the parameters use
	 *         default values
	 */
	public static ImmutableDtsOptions newImmutableInstanceFromType(
			final DtsAttributeSetType type)
	{
		switch (type)
		{
			case ALL_ATTRIBUTES:
			{
				return DEFAULT_ALL_ATTRIBUTES;
			}

			case NO_ATTRIBUTES:
			{
				return DEFAULT_NO_ATTRIBUTES;
			}

			case PROPERTIES:
			{
				return DEFAULT_PROPERTIES;
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized type " + type);
			}
		}
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

		final DtsOptions that = (DtsOptions) o;
		return new EqualsBuilder()
				.append(this.throwExceptionOnFailure, that.throwExceptionOnFailure)
				.append(this.standardSourceNamespace, that.standardSourceNamespace)
				.append(this.targetNamespace, that.targetNamespace)
				.append(this.attributeSet, that.attributeSet)
				.isEquals();
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
		return new HashCodeBuilder(17, 37)
				.append(throwExceptionOnFailure)
				.append(standardSourceNamespace)
				.append(targetNamespace)
				.append(attributeSet)
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

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object.
	 * 
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public DtsOptions copy()
	{
		final DtsOptions copy = new DtsOptions();

		// Deep-copy primitive fields
		copy.throwExceptionOnFailure = this.throwExceptionOnFailure;
		copy.targetNamespace = this.targetNamespace;
		copy.standardSourceNamespace = this.standardSourceNamespace;
		copy.attributeSetLimit = this.attributeSetLimit;

		// Deep-copy composite fields
		copy.attributeSet = this.attributeSet.copy();
		return copy;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the targetNamespace property.
	 * 
	 * @return the targetNamespace
	 */
	public String getTargetNamespace()
	{
		return targetNamespace;
	}

	/**
	 * Set a new value for the targetNamespace property.
	 * 
	 * @param targetNamespace
	 *            the targetNamespace to set
	 */
	public DtsOptions setTargetNamespace(final String targetNamespace)
	{
		this.targetNamespace = targetNamespace;
		return this;
	}

	/**
	 * Return the standardSourceNamespace property.
	 * 
	 * @return the standardSourceNamespace
	 */
	public Boolean getStandardSourceNamespace()
	{
		return standardSourceNamespace;
	}

	/**
	 * Set a new value for the standardSourceNamespace property.
	 * 
	 * @param standardSourceNamespace
	 *            the standardSourceNamespace to set
	 */
	public DtsOptions setStandardSourceNamespace(final Boolean standardSourceNamespace)
	{
		this.standardSourceNamespace = standardSourceNamespace;
		return this;
	}

	public int getAttributeSetLimit()
	{
		return attributeSetLimit;
	}

	public DtsOptions setAttributeSetLimit(final int attributeSetLimit)
	{
		this.attributeSetLimit = attributeSetLimit;
		return this;
	}

	/**
	 * Return the throwExceptionOnFailure property.
	 * 
	 * @return the throwExceptionOnFailure
	 */
	public boolean isThrowExceptionOnFailure()
	{
		return throwExceptionOnFailure;
	}

	/**
	 * Set a new value for the throwExceptionOnFailure property.
	 * 
	 * @param throwExceptionOnFailure
	 *            the throwExceptionOnFailure to set
	 */
	public DtsOptions setThrowExceptionOnFailure(final boolean throwExceptionOnFailure)
	{
		this.throwExceptionOnFailure = throwExceptionOnFailure;
		return this;
	}

	/**
	 * Return the attributeSet property.
	 * 
	 * @return the attributeSet
	 */
	public DtsAttributeSet getAttributeSet()
	{
		return attributeSet;
	}

	/**
	 * Set a new value for the attributeSet property.
	 * 
	 * @param attributeSet
	 *            the attributeSet to set
	 */
	public DtsOptions setAttributeSet(final DtsAttributeSet attributeSet)
	{
		ValidationUtil.validateNotNull("attributeSet", attributeSet);
		this.attributeSet = attributeSet;
		return this;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsAttributeSet#getType()
	 */
	public DtsAttributeSetType getType()
	{
		return attributeSet.getType();
	}

	/**
	 * Set a new value for the attributeSet property using an attribute set type.
	 * 
	 * @param type
	 *            the attribute set type to set
	 */
	public DtsOptions setAttributeSetType(final DtsAttributeSetType type)
	{
		this.attributeSet = new DtsAttributeSet(type);
		return this;
	}
}
