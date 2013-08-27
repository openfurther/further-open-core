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

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * A template parser that generates a bean definition with a prescribed ID. If the bean is
 * encountered twice in the application context, it is only processed the first time and
 * ignored in all subsequent occurrences.
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
 * @version Jun 2, 2010
 */
public abstract class AbstractUniqueBeanDefinitionParser implements BeanDefinitionParser
{
	// ========================= IMPL: BeanDefinitionParser ================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element
	 * , org.springframework.beans.factory.xml.ParserContext)
	 */
	@Override
	public final BeanDefinition parse(final Element element,
			final ParserContext parserContext)
	{
		final BeanDefinition definition = parseInternal(element, parserContext);
		if (!parserContext.isNested())
		{
			try
			{
				if (!parserContext.getRegistry().containsBeanDefinition(getGeneratedId()))
				{
					final String id = getGeneratedId();
					final BeanDefinitionHolder holder = new BeanDefinitionHolder(
							definition, id);
					BeanDefinitionReaderUtils.registerBeanDefinition(holder,
							parserContext.getRegistry());
					final BeanComponentDefinition componentDefinition = new BeanComponentDefinition(
							holder);
					// postProcessComponentDefinition(componentDefinition);
					parserContext.registerComponent(componentDefinition);
				}
			}
			catch (final BeanDefinitionStoreException ex)
			{
				parserContext.getReaderContext().error(ex.getMessage(), element);
				return null;
			}

		}
		return definition;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parse the spring XML element into a bean definition. Similar to
	 * {@link AbstractBeanDefinitionParser}'s corresponding method.
	 * 
	 * @param element
	 *            XML element to be parsed
	 * @param parserContext
	 *            Spring context's parser registry
	 * @return bean definition
	 */
	protected abstract BeanDefinition parseInternal(final Element element,
			final ParserContext parserContext);

	/**
	 * Return the generated ID of the bean to be created.
	 * 
	 * @return the bean ID
	 */
	protected abstract String getGeneratedId();
}