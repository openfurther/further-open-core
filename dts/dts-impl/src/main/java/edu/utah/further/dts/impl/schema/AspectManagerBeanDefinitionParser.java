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
package edu.utah.further.dts.impl.schema;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.utah.further.dts.impl.aspect.AspectManager;
import edu.utah.further.dts.impl.aspect.AspectManagerFactoryBean;

/**
 * A parser of the Spring schema "dts"'s <code>aspects</code> element.
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
 * @version Oct 18, 2009
 */
@Deprecated
public class AspectManagerBeanDefinitionParser extends AbstractBeanDefinitionParser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(AspectManagerBeanDefinitionParser.class);

	// ========================= IMPLEMENTATION: AbstractSingleBeanDefini. =

	/**
	 * @return
	 * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#shouldGenerateId()
	 */
	@Override
	protected boolean shouldGenerateId()
	{
		return true;
	}

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
				.rootBeanDefinition(AspectManagerFactoryBean.class);
		final BeanDefinitionBuilder parent = parseAspectManager(element);
		factory.addPropertyValue("parent", parent.getBeanDefinition());

		final List<?> list = parserContext.getDelegate().parseListElement(element,
				factory.getBeanDefinition());
		factory.addPropertyValue("children", list);

		return factory.getBeanDefinition();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 * @return
	 */
	private static BeanDefinitionBuilder parseAspectManager(final Element element)
	{
		final BeanDefinitionBuilder aspectManager = BeanDefinitionBuilder
				.rootBeanDefinition(AspectManager.class);
		// Example -- a manager's attribute could be:
		// aspectManager.addPropertyValue("name", element.getAttribute("name"));
		return aspectManager;
	}
}