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
package edu.utah.further.core.qunit.schema;

import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ATTRIBUTE_NAME;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ATTRIBUTE_VALUE;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_EXPECTED_CLASS_PATH;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_IGNORED_ELEMENT;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_IN;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_INPUT_CLASS_PATH;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_OUT;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_PARAM;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_SERVICE_USER_ID;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_SPECIAL_ACTION;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_TARGET_NAMESPACE_ID;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_TEST;
import static edu.utah.further.core.qunit.schema.QunitSchemaConstants.ELEMENT_TRANSFORMER;
import static edu.utah.further.core.util.schema.SchemaUtil.addOptionalPropertyValue;
import static edu.utah.further.core.util.schema.SchemaUtil.getChildElementsByTagNameNullSafe;
import static edu.utah.further.core.util.schema.SchemaUtil.newManagedList;
import static org.springframework.util.xml.DomUtils.getChildElementValueByTagName;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestSuite;
import edu.utah.further.core.util.context.PFixtureManager;

/**
 * Parses a portable fixture manager XML element into a {@link PFixtureManager} bean.
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
 * @version May 4, 2010
 */
final class QTestSuiteBeanDefinitionParser extends AbstractBeanDefinitionParser
{
	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: AbstractBeanDefinitionPa. =

	/**
	 * @param element
	 * @param parserContext
	 * @return
	 * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element,
	 *      org.springframework.beans.factory.xml.ParserContext)
	 */
	@Override
	protected AbstractBeanDefinition parseInternal(final Element element,
			final ParserContext parserContext)
	{
		final BeanDefinitionBuilder factory = parseXmlTestSuite(element);
		parseXqueryTesterElements(
				getChildElementsByTagNameNullSafe(element, ELEMENT_TEST), factory,
				parserContext);
		return factory.getBeanDefinition();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parseXmlTestSuite(final Element element)
	{
		final BeanDefinitionBuilder factory = BeanDefinitionBuilder
				.rootBeanDefinition(QTestSuite.class);

		// Add test suite attributes here in the future

		// Optional arguments
		addOptionalPropertyValue(element, factory, ELEMENT_TARGET_NAMESPACE_ID,
				"context.targetNamespaceId");
		addOptionalPropertyValue(element, factory, ELEMENT_SERVICE_USER_ID,
				"context.serviceUserId");
		addOptionalPropertyValue(element, factory, ELEMENT_INPUT_CLASS_PATH,
				"context.inputClassPath");
		addOptionalPropertyValue(element, factory, ELEMENT_EXPECTED_CLASS_PATH,
				"context.expectedClassPath");

		return factory;
	}

	/**
	 * @param childElements
	 * @param factory
	 */
	private static void parseXqueryTesterElements(final List<Element> childElements,
			final BeanDefinitionBuilder factory, final ParserContext parserContext)
	{
		final List<BeanDefinition> children = newManagedList(childElements.size());
		for (final Element childElement : childElements)
		{
			children.add(parseXmlTestData(childElement, parserContext)
					.getBeanDefinition());
		}
		factory.addPropertyValue("testers", children);
	}

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parseXmlTestData(final Element element,
			final ParserContext parserContext)
	{
		final BeanDefinitionBuilder factory = BeanDefinitionBuilder
				.rootBeanDefinition(QTestData.class);

		// Mandatory arguments
		factory.addPropertyValue("input",
				getChildElementValueByTagName(element, ELEMENT_IN));
		factory.addPropertyValue("expected",
				getChildElementValueByTagName(element, ELEMENT_OUT));

		// Optional arguments
		addOptionalPropertyValue(element, factory, ELEMENT_TRANSFORMER, "transformer");
		addOptionalPropertyValue(element, factory, ELEMENT_SPECIAL_ACTION, "specialAction");

		parseParamElements(getChildElementsByTagNameNullSafe(element, ELEMENT_PARAM),
				factory, parserContext);

		parseIgnoredElements(
				getChildElementsByTagNameNullSafe(element, ELEMENT_IGNORED_ELEMENT),
				factory, parserContext);

		return factory;
	}

	/**
	 * @param childElements
	 * @param factory
	 */
	private static void parseParamElements(final List<Element> childElements,
			final BeanDefinitionBuilder factory, final ParserContext parserContext)
	{
		final Map<String, String> parameters = CollectionUtil.newMap();
		for (final Element childElement : childElements)
		{
			final String name = childElement.getAttribute(ATTRIBUTE_NAME);
			final String value = childElement.getAttribute(ATTRIBUTE_VALUE);
			if (org.apache.commons.lang.StringUtils.isBlank(name))
			{
				throw new BeanDefinitionParsingException(new Problem(
						"Parameter must have a name", new Location(parserContext
								.getReaderContext()
								.getResource())));
			}
			parameters.put(name, value);
		}
		factory.addPropertyValue("parameters", parameters);
	}

	/**
	 * @param childElements
	 * @param factory
	 */
	private static void parseIgnoredElements(final List<Element> childElements,
			final BeanDefinitionBuilder factory, final ParserContext parserContext)
	{
		final Set<String> ignoredElements = CollectionUtil.newSet();
		for (final Element childElement : childElements)
		{
			final String ignoredElement = childElement.getTextContent();
			if (org.apache.commons.lang.StringUtils.isBlank(ignoredElement))
			{
				throw new BeanDefinitionParsingException(new Problem(
						"Ignored element must not be a blank string", new Location(
								parserContext.getReaderContext().getResource())));
			}
			ignoredElements.add(ignoredElement);
		}
		factory.addPropertyValue("ignoredElements", ignoredElements);
	}
}
