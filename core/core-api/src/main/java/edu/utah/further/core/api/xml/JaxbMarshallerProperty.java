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
package edu.utah.further.core.api.xml;

import static edu.utah.further.core.api.message.Messages.notNullMessage;

import java.util.Map;

import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Mirrors {@link Marshaller} properties with type-safe enumerated constants.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 9, 2010
 */
public enum JaxbMarshallerProperty implements Named
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * The name of the property used to specify the output encoding in the marshalled XML
	 * data.
	 */
	ENCODING(Marshaller.JAXB_ENCODING),

	/**
	 * The name of the property used to specify whether or not the marshalled XML data is
	 * formatted with linefeeds and indentation.
	 */
	FORMATTED_OUTPUT(Marshaller.JAXB_FORMATTED_OUTPUT),

	/**
	 * The name of the property used to specify the xsi:schemaLocation attribute value to
	 * place in the marshalled XML output.
	 */
	SCHEMA_LOCATION(Marshaller.JAXB_SCHEMA_LOCATION),

	/**
	 * The name of the property used to specify the xsi:noNamespaceSchemaLocation
	 * attribute value to place in the marshalled XML output.
	 */
	NO_NAMESPACE_SCHEMA_LOCATION(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION),

	/**
	 * The name of the property used to specify whether or not the marshaller will
	 * generate document level events (ie calling startDocument or endDocument).
	 */
	FRAGMENT(Marshaller.JAXB_FRAGMENT),

	/**
	 * The name of the property used to specify a custom namespace-to-prefix mapper.
	 */
	NAMESPACE_PREFIX_MAPPER("com.sun.xml.bind.namespacePrefixMapper");

	// ========================= CONSTANTS ====================================

	/**
	 * Caches the {@link Marshaller}-value-to-our-enum-type mapping.
	 */
	private static final Map<String, JaxbMarshallerProperty> instances = CollectionUtil
			.newMap();

	static
	{
		for (final JaxbMarshallerProperty instance : JaxbMarshallerProperty.values())
		{
			instances.put(instance.getName(), instance);
		}
	}

	// ========================= FIELDS =======================================

	/**
	 * Property standard name in {@link Marshaller}.
	 */
	private final String name;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Construct an XML element type.
	 *
	 * @param name
	 *            header name
	 */
	private JaxbMarshallerProperty(final String name)
	{
		this.name = name;
	}

	// ========================= IMPL: Named ==================================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= METHODS ======================================

	/**
	 * A factory method.
	 *
	 * @param marshallerConstant
	 *            {@link Marshaller} constant value
	 * @return corresponding enum constant
	 */
	public static JaxbMarshallerProperty valueOfMarshallerConstant(
			final String marshallerConstant)
	{
		return instances.get(marshallerConstant);
	}

	/**
	 * Set all marshaller properties found in a JAXB configuration. Swallows
	 * {@link PropertyException}s because we are type-safe here.
	 *
	 * @param marshaller
	 *            JAXB marshaller
	 * @param properties
	 *            JAXB configuration object
	 */
	public static void setProperties(final Marshaller marshaller,
			final Map<JaxbMarshallerProperty, Object> properties)
	{
		for (final Map.Entry<JaxbMarshallerProperty, Object> entry : properties
				.entrySet())
		{
			setProperty(marshaller, entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Set a marshaller property. Swallows {@link PropertyException}s because we are
	 * type-safe here.
	 *
	 * @param marshaller
	 *            JAXB marshaller
	 * @param property
	 *            property name
	 * @param value
	 *            property value
	 */
	public static void setProperty(final Marshaller marshaller,
			final JaxbMarshallerProperty property, final Object value)
	{
		if (property == null)
		{
			throw new IllegalArgumentException(notNullMessage("property"));
		}
		try
		{
			marshaller.setProperty(property.getName(), value);
		}
		catch (final PropertyException e)
		{
			throw new ApplicationException("Failed to set marshaller property", e);
		}
	}
}
