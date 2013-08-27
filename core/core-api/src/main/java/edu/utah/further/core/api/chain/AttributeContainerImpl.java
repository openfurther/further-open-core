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
package edu.utah.further.core.api.chain;

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.constant.Strings.TAB_CHAR;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A default implementation of an attribute container based on a simple attribute hash
 * map.
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
 * @version Sep 28, 2009
 */
public final class AttributeContainerImpl implements AttributeContainer
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Request attributes placed/removed by handlers.
	 */
	private final Map<String, Object> attributes = CollectionUtil.newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default Constructor, initializes an empty container.
	 */
	public AttributeContainerImpl()
	{
		// Nothing to do
	}

	/**
	 * @param attributes
	 */
	public AttributeContainerImpl(final Map<String, ?> attributes)
	{
		setAttributes(attributes);
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
		final AttributeContainerImpl that = (AttributeContainerImpl) obj;
		return new EqualsBuilder().append(this.attributes, that.attributes).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(attributes).toHashCode();
	}

	/**
	 * Print the attributes in this container as a list of name-value pairs.
	 *
	 * @return textual representation of the container as a list of name-value pairs
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		final StringBuilder s = StringUtil
				.newStringBuilder()
				.append("--- Attributes ---")
				.append(NEW_LINE_STRING);
		for (final String attributeName : getAttributeNames())
		{
			s.append(attributeName);
			s.append(TAB_CHAR);
			s.append(getAttribute(attributeName));
			s.append(NEW_LINE_STRING);
		}
		return s.toString();
	}

	// ========================= IMPLEMENTATION: ChainRequest ==============

	/**
	 * Return the attributes property.
	 *
	 * @return the attributes
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributes()
	 */
	@Override
	public Map<String, Object> getAttributes()
	{
		return CollectionUtil.newMap(attributes);
	}

	/**
	 * @param attributes
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttributes(java.util.Map)
	 */
	@Override
	public void setAttributes(final Map<String, ?> attributes)
	{
		CollectionUtil.setMapElements(this.attributes, attributes);
	}

	/**
	 * @param map
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttributes(java.util.Map)
	 */
	@Override
	public void addAttributes(final Map<String, ?> map)
	{
		this.attributes.putAll(map);
	}

	/**
	 * Note: Swallows unchecked warnings.
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(java.lang.String)
	 */
	@Override
	public <T> T getAttribute(final String name)
	{
		return (T) attributes.get(name);
	}

	/**
	 * @param <T>
	 * @param label
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(edu.utah.further.core.api.context.Labeled)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(final Labeled label)
	{
		return (label == null) ? null : (T) getAttribute(label.getLabel());
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributeNames()
	 */
	@Override
	public Set<String> getAttributeNames()
	{
		return attributes.keySet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAllAttributes()
	 */
	@Override
	public void removeAllAttributes()
	{
		attributes.clear();
	}

	/**
	 * @param key
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(final String key)
	{
		attributes.remove(key);
	}

	/**
	 * @param key
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final String key, final Object value)
	{
		attributes.put(key, value);
	}

	/**
	 * @param label
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(edu.utah.further.core.api.context.Labeled,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final Labeled label, final Object value)
	{
		if (label != null)
		{
			setAttribute(label.getLabel(), value);
		}
	}
}
