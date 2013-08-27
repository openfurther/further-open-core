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
package edu.utah.further.core.api.collections;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Map;

import edu.utah.further.core.api.lang.Builder;

/**
 * Serializes an object to a map of property name-value pairs. Analogous to commons-lang
 * <code>ToStringBuilder</code>.
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
 * @version Jan 16, 2009
 */
public final class ToParameterMapBuilder implements Builder<Map<String, String>>
{
	// ========================= CONSTANTS =================================

	/**
	 * Default string to print in case of null values.
	 */
	public static final String DEFAULT_NULL_VALUE = "null";

	// ========================= FIELDS ====================================

	/**
	 * The output parameter map.
	 */
	private final Map<String, String> parameterMap = newMap();

	/**
	 * A flag to ignore or include null parameter values in the output map.
	 */
	private boolean includeNullValues = false;

	/**
	 * String to print in case of null values.
	 */
	private String nullValue = DEFAULT_NULL_VALUE;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the String representation that this ToParameterMapBuilder built.
	 */
	@Override
	public String toString()
	{
		return parameterMap.toString();
	}

	// ========================= IMPLEMENTATION: Builder ===================

	/**
	 * A factory method that builds a a parameter map.
	 *
	 * @return UML element instance
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public Map<String, String> build()
	{
		return unmodifiableMap(parameterMap);
	}

	// ========================= METHODS ===================================

	/**
	 * Append a byte field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final byte value)
	{
		return append(fieldName, new Byte(value));
	}

	/**
	 * Append a short field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final short value)
	{
		return append(fieldName, new Short(value));
	}

	/**
	 * Append a integer field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final int value)
	{
		return append(fieldName, new Integer(value));
	}

	/**
	 * Append a float field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final float value)
	{
		return append(fieldName, new Float(value));
	}

	/**
	 * Append a double field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final double value)
	{
		return append(fieldName, new Double(value));
	}

	/**
	 * Append a long field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final long value)
	{
		return append(fieldName, new Long(value));
	}

	/**
	 * Append a boolean field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final boolean value)
	{
		return append(fieldName, value ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Append a parameter map (no prefix is added to parameter names).
	 *
	 * @param parameters
	 *            parameter map to add
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder appendParameters(final Map<String, String> parameters)
	{
		return appendParameters(null, parameters);
	}

	/**
	 * Append a parameter map.
	 *
	 * @param namePrefix
	 *            string to prefix parameter names in the map before adding them into this
	 *            object's map
	 * @param parameters
	 *            parameter map to add
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder appendParameters(final String namePrefix,
			final Map<String, String> parameters)
	{
		for (final Map.Entry<String, String> entry : parameters.entrySet())
		{
			final String parameterName = addPrefix(namePrefix, entry.getKey());
			append(parameterName, entry.getValue());
		}
		return this;
	}

	/**
	 * Append a field value.
	 *
	 * @param fieldName
	 *            the name of the field, usually the member variable name
	 * @param value
	 *            the field value
	 * @return this, to support call-chaining
	 */
	public ToParameterMapBuilder append(final String fieldName, final Object value)
	{
		if ((value != null) || includeNullValues)
		{
			if (value != null)
			{
				parameterMap.put(fieldName, value.toString());
			}
			else if (includeNullValues)
			{
				parameterMap.put(fieldName, nullValue);
			}
		}
		return this;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the includeNullValues property. This is a flag to ignore or include null
	 * parameter values in the output map. The flag's default value is <code>false</code>.
	 *
	 * @return the includeNullValues
	 */
	public boolean isIncludeNullValues()
	{
		return includeNullValues;
	}

	/**
	 * Set a new value for the includeNullValues property.
	 *
	 * @param includeNullValues
	 *            the includeNullValues to set
	 */
	public void setIncludeNullValues(boolean includeNullValues)
	{
		this.includeNullValues = includeNullValues;
	}

	/**
	 * Return the nullValue property = the string to print in case of null values. By
	 * default, this string is {@link #DEFAULT_NULL_VALUE}.
	 *
	 * @return the nullValue
	 */
	public String getNullValue()
	{
		return nullValue;
	}

	/**
	 * Set a new value for the nullValue property.
	 *
	 * @param nullValue
	 *            the nullValue to set
	 */
	public void setNullValue(String nullValue)
	{
		this.nullValue = nullValue;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param prefix
	 * @param fieldName
	 * @return
	 */
	private static String addPrefix(final String prefix, final String fieldName)
	{
		return isBlank(prefix) ? fieldName : (prefix + fieldName);
	}
}
