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

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * An example of a parser of a composite class for a spring custom schema tag nesting
 * test.
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
 * @version Oct 20, 2009
 */
final class ComponentBeanDefinitionParser extends AbstractBeanDefinitionParser
{
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
		final BeanDefinitionBuilder factory = BeanDefinitionBuilder
				.rootBeanDefinition(ComponentFactoryBean.class);
		final BeanDefinitionBuilder parent = parseComponent(element);
		factory.addPropertyValue("parent", parent.getBeanDefinition());

		final List<Element> childElements = DomUtils.getChildElementsByTagName(element,
				CoreSchemaConstants.ELEMENT_COMPONENT);
		if (childElements != null && childElements.size() > 0)
		{
			parseChildComponents(childElements, factory);
		}
		return factory.getBeanDefinition();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parseComponent(final Element element)
	{
		final BeanDefinitionBuilder component = BeanDefinitionBuilder
				.rootBeanDefinition(Component.class);
		component.addPropertyValue("name", element.getAttribute("name"));
		return component;
	}

	/**
	 * @param childElements
	 * @param factory
	 */
	private static void parseChildComponents(final List<Element> childElements,
			final BeanDefinitionBuilder factory)
	{
		final ManagedList<AbstractBeanDefinition> children = new ManagedList<>(
				childElements.size());
		for (final Element childElement : childElements)
		{
			final BeanDefinitionBuilder child = parseComponent(childElement);
			children.add(child.getBeanDefinition());
		}
		factory.addPropertyValue("children", children);
	}
}