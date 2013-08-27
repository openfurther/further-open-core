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
package edu.utah.further.core.xml.jaxb;

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.constant.Strings.PROPERTY_SCOPE_CHAR;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.collections.ToParameterMapBuilder;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Serializes an object to a map of property name-value pairs. Uses reflection to scan for
 * JAXB annotations on the object's fields. Each annotated field is recursively scanned
 * and serialized into the map, so this is a "deep-serialization" of the JAXB-annotated
 * part of the <i>entire</i> object's graph.
 * <p>
 * This class uses reflection to determine the fields to append. Because these fields are
 * usually private, the class uses
 * {@link java.lang.reflect.AccessibleObject#setAccessible(java.lang.reflect.AccessibleObject[], boolean)}
 * to change the visibility of the fields. This will fail under a security manager, unless
 * the appropriate permissions are set up correctly.
 * <p>
 * TODO 1: add JAXB XML namespace ("ns") values into account in parameter names.
 * <p>
 * TODO 2: add a registry of objects to avoid circular object reference problems. Low
 * priority.
 * <p>
 * TODO 3: have an option to retrieve field values from getters instead from the reflected
 * fields.
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
public final class ToParameterMapJaxbBuilder implements Builder<Map<String, String>>
{
	// ========================= CONSTANTS =================================

	/**
	 * Default string to print in case of null values.
	 */
	public static final String DEFAULT_NULL_VALUE = "null";

	// ========================= FIELDS ====================================

	/**
	 * A parameter map builder delegate.
	 */
	private final ToParameterMapBuilder builder = new ToParameterMapBuilder();

	/**
	 * JavaBean Object to serialize to a parameter map.
	 */
	private final Object object;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a reflection-based parameter map builder.
	 * 
	 * @param object
	 *            object to serialize
	 */
	public ToParameterMapJaxbBuilder(final Object object)
	{
		super();
		this.object = object;
		if (object != null)
		{
			appendAllFieldsUsingReflection(EMPTY_STRING, object);
		}
	}

	// ========================= STATIC METHODS ============================

	/**
	 * A static factory method. Serializes an object into a parameter map. Forwards to a
	 * {@link ToParameterMapJaxbBuilder} instance. Uses the default
	 * {@link ToParameterMapBuilder} settings.
	 * 
	 * @param object
	 *            object to serialize
	 * @see ReflectionToStringBuilder#toString(Object)
	 */
	public static Map<String, String> getAsParameterMap(final Object object)
	{
		return new ToParameterMapJaxbBuilder(object).build();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the String representation that this ToParameterMapBuilder built.
	 */
	@Override
	public String toString()
	{
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: Builder ===================

	/**
	 * A factory method that builds a parameter map.
	 * 
	 * @return UML element instance
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public Map<String, String> build()
	{
		return builder.build();
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.ToParameterMapBuilder#getNullValue()
	 */
	public String getNullValue()
	{
		return builder.getNullValue();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.ToParameterMapBuilder#isIncludeNullValues()
	 */
	public boolean isIncludeNullValues()
	{
		return builder.isIncludeNullValues();
	}

	/**
	 * @param includeNullValues
	 * @see edu.utah.further.core.api.collections.ToParameterMapBuilder#setIncludeNullValues(boolean)
	 */
	public void setIncludeNullValues(final boolean includeNullValues)
	{
		builder.setIncludeNullValues(includeNullValues);
	}

	/**
	 * @param nullValue
	 * @see edu.utah.further.core.api.collections.ToParameterMapBuilder#setNullValue(java.lang.String)
	 */
	public void setNullValue(final String nullValue)
	{
		builder.setNullValue(nullValue);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the object to serialize.
	 * 
	 * @return the object being serialized by this builder
	 */
	public Object getObject()
	{
		return object;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Append all properties of the object to the parameter map using reflection.
	 * 
	 * @param namePrefix
	 *            string to prefix object parameter name with
	 * @param obj
	 *            object to serialize
	 */
	private void appendAllFieldsUsingReflection(final String namePrefix, final Object obj)
	{
		final Class<?> clazz = obj.getClass();
		final Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		final String parentPrefix = isBlank(namePrefix) ? EMPTY_STRING
				: (namePrefix + PROPERTY_SCOPE_CHAR);
		try
		{
			for (final Field field : fields)
			{
				final Object fieldValue = field.get(obj);
				final XmlElement xmlElement = field.getAnnotation(XmlElement.class);
				if (xmlElement != null)
				{
					// Add field to map with a proper parameter name
					// final String xmlName = xmlElement.name();
					// final String propertyName = (xmlName == null) ? field.getName()
					// : xmlName;

					// Important: for now, we ignore JAXB names and use the field names,
					// for consistency with Spring's data binder. In the future, if we
					// find an efficient way to alias property names (recursively, for the
					// whole object graph) in Spring, we can use the code commented above
					// so that the XML binding is taken from the JAXB annotation's "name"
					// attribute.
					final String propertyName = field.getName();
					appendFieldUsingReflection(parentPrefix + propertyName, fieldValue);
				}
			}
		}
		catch (final Throwable e)
		{
			throw new IllegalStateException(
					"Could not serialize object into a parameter map", e);
		}
	}

	/**
	 * Append an object's value to the parameter map. Clone an object.
	 * 
	 * @param fieldName
	 *            fully qualified name of field within the object graph
	 * @param fieldValue
	 *            object to serialize
	 */
	private void appendFieldUsingReflection(final String fieldName,
			final Object fieldValue)
	{
		if (fieldValue == null)
		{
			// The clone of null is null
			return;
		}
		// look if obj is a primitive type
		else if (ReflectionUtil.instanceOfOneOf(fieldValue, String.class, Integer.class,
				Double.class, Boolean.class, Long.class, Byte.class, Character.class,
				Short.class, Float.class, Enum.class))
		{
			builder.append(fieldName, fieldValue);
		}
		else
		{
			appendAllFieldsUsingReflection(fieldName, fieldValue);
		}
	}
}
