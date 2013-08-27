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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType;

/**
 * A JavaBean that holds options for a variety of DTS services. Has getters only.
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
final class ImmutableDtsOptions extends DtsOptions
{
	// ========================= CONSTANTS =================================

	/**
	 * For common exception throwing.
	 */
	private static final String IMMUTABLITY_ERROR_MESSAGE = "Can't change an immutable options object";

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param attributeSet
	 */
	ImmutableDtsOptions()
	{
		super();
	}

	/**
	 * @param attributeSet
	 */
	ImmutableDtsOptions(final DtsAttributeSetType type)
	{
		super(type);
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

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the targetNamespace property.
	 * 
	 * @param targetNamespace
	 *            the targetNamespace to set
	 */
	@Override
	public ImmutableDtsOptions setTargetNamespace(final String targetNamespace)
	{
		throw new UnsupportedOperationException(IMMUTABLITY_ERROR_MESSAGE);
	}

	/**
	 * Set a new value for the standardSourceNamespace property.
	 * 
	 * @param standardSourceNamespace
	 *            the standardSourceNamespace to set
	 */
	@Override
	public ImmutableDtsOptions setStandardSourceNamespace(
			final Boolean standardSourceNamespace)
	{
		throw new UnsupportedOperationException(IMMUTABLITY_ERROR_MESSAGE);
	}

	/**
	 * Set a new value for the throwExceptionOnFailure property.
	 * 
	 * @param throwExceptionOnFailure
	 *            the throwExceptionOnFailure to set
	 */
	@Override
	public ImmutableDtsOptions setThrowExceptionOnFailure(
			final boolean throwExceptionOnFailure)
	{
		throw new UnsupportedOperationException(IMMUTABLITY_ERROR_MESSAGE);
	}

	/**
	 * Set a new value for the attributeSet property.
	 * 
	 * @param attributeSet
	 *            the attributeSet to set
	 */
	@Override
	public ImmutableDtsOptions setAttributeSet(final DtsAttributeSet attributeSet)
	{
		throw new UnsupportedOperationException(IMMUTABLITY_ERROR_MESSAGE);
	}

	/**
	 * Set a new value for the attributeSet property using an attribute set type.
	 * 
	 * @param type
	 *            the attribute set type to set
	 */
	@Override
	public ImmutableDtsOptions setAttributeSetType(final DtsAttributeSetType type)
	{
		throw new UnsupportedOperationException(IMMUTABLITY_ERROR_MESSAGE);
	}
}
