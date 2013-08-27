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

import java.util.List;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import edu.utah.further.core.api.constant.MavenPhase;
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
final class PFixtureManagerBeanDefinitionParser extends AbstractBeanDefinitionParser
{
	// ========================= FIELDS ==================================-=

	/**
	 * Reusable, for location element parsing.
	 */
	private static final StringBeanDefinitionParser stringBeanDefinitionParser = new StringBeanDefinitionParser();

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
		final BeanDefinitionBuilder factory = parsePFixture(element);

		final List<Element> childElements = DomUtils.getChildElementsByTagName(element,
				CoreSchemaConstants.ELEMENT_LOCATION);
		// Must have at least one element
		if (childElements == null || childElements.isEmpty())
		{
			throw new BeanDefinitionParsingException(
					new Problem(
							"Portable test fixture manager must be wired with at least one context location",
							new Location(parserContext.getReaderContext().getResource())));
		}
		parseChildLocations(childElements, factory);

		return factory.getBeanDefinition();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parsePFixture(final Element element)
	{
		final BeanDefinitionBuilder factory = BeanDefinitionBuilder
				.rootBeanDefinition(PFixtureManager.class);

		// Set maven phase only if specified; otherwise, use the default value declared
		// within the PFixtureManager class
		final MavenPhase mavenPhase = MavenPhase.getValue(element
				.getAttribute("mavenPhase"));
		if (mavenPhase != null)
		{
			factory.addPropertyValue("mavenPhase", mavenPhase);
		}
		return factory;
	}

	/**
	 * @param childElements
	 * @param factory
	 */
	private static void parseChildLocations(final List<Element> childElements,
			final BeanDefinitionBuilder factory)
	{
		final ManagedList<AbstractBeanDefinition> children = new ManagedList<>(
				childElements.size());
		for (final Element childElement : childElements)
		{
			final BeanDefinitionBuilder child = parseLocation(childElement);
			children.add(child.getBeanDefinition());
		}
		factory.addConstructorArgValue(children);
	}

	/**
	 * @param element
	 * @param bean
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element,
	 *      org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	private static BeanDefinitionBuilder parseLocation(final Element element)
	{
		final BeanDefinitionBuilder bean = BeanDefinitionBuilder
				.rootBeanDefinition(String.class);
		stringBeanDefinitionParser.doParse(element, bean);
		return bean;
	}
}