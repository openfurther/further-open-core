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
package edu.utah.further.core.util.schema;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.springframework.util.xml.DomUtils.getChildElementValueByTagName;
import static org.springframework.util.xml.DomUtils.getChildElementsByTagName;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.w3c.dom.Element;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;

/**
 * Reusable Spring XML schema utilities.
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
 * @version May 13, 2010
 */
@Utility
public final class SchemaUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Reusable, for location element parsing.
	 */
	private static final StringBeanDefinitionParser stringBeanDefinitionParser = new StringBeanDefinitionParser();

	// ========================= NESTED TYPES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private SchemaUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Parse an XML element into a string. The element must contain a standard
	 * {@link CoreSchemaConstants#ATTRIBUTE_VALUE} attribute.
	 *
	 * @param element
	 *            XML element representing a string
	 * @return a bean definition builder of a string-type bean
	 */
	public static BeanDefinitionBuilder parseStringElement(final Element element)
	{
		final BeanDefinitionBuilder bean = BeanDefinitionBuilder
				.rootBeanDefinition(String.class);
		stringBeanDefinitionParser.doParse(element, bean);
		return bean;
	}

	/**
	 * Retrieve all child elements of the given DOM element that match any of the given
	 * element names. Only look at the direct child level of the given element; do not go
	 * into further depth (in contrast to the DOM API's <code>getElementsByTagName</code>
	 * method).
	 *
	 * @param element
	 *            the DOM element to analyze
	 * @param childEleNames
	 *            the child element names to look for
	 * @return a List of child <code>org.w3c.dom.Element</code> instances. Never returns
	 *         <code>null</code> - only empty or non-empty lists
	 * @see org.w3c.dom.Element
	 * @see org.w3c.dom.Element#getElementsByTagName
	 */
	public static List<Element> getChildElementsByTagNameNullSafe(final Element element,
			final String... childEleNames)
	{
		final List<Element> childElements = getChildElementsByTagName(element,
				childEleNames);
		return (childElements == null) ? CollectionUtil.<Element> newList()
				: childElements;
	}

	/**
	 * Create a managed bean definition list.
	 *
	 * @param size
	 *            list size
	 * @return bean definition element list
	 */
	public static List<BeanDefinition> newManagedList(final int size)
	{
		return new ManagedList<>(size);
	}

	/**
	 * Parse a values from an XML element list (each containing a value attribute).
	 *
	 * @param elements
	 *            XML elements
	 * @return list of element value attribute values
	 */
	public static List<String> parseValueElements(final List<Element> elements)
	{
		final List<String> list = CollectionUtil.newList();
		for (final Element element : elements)
		{
			list.add(element.getAttribute(CoreSchemaConstants.ATTRIBUTE_VALUE));
		}
		return list;
	}

	/**
	 * @param element
	 * @param factory
	 * @param elementName
	 * @param propertyName
	 */
	public static void addOptionalPropertyValue(final Element element,
			final BeanDefinitionBuilder factory, final String elementName,
			final String propertyName)
	{
		final String alwaysFailValue = getChildElementValueByTagName(element, elementName);
		if (alwaysFailValue != null)
		{
			factory.addPropertyValue(propertyName, alwaysFailValue);
		}
	}

	// ========================= PRIVATE METHODS ===========================

}
