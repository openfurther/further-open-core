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

import static edu.utah.further.core.util.io.ClasspathUtil.isAnnotatedWith;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.util.io.ClasspathUtil;

/**
 * A parser of the Spring schema "further"'s <code>aspect</code> element. Creates an
 * aspect singleton instance that is annotated with {@link Aspect} and has a default
 * constructor.
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
 * @version Oct 12, 2009
 */
final class AspectBeanDefinitionParser extends AbstractSingleBeanDefinitionParser
{
	// ========================= CONSTANTS =================================

	/**
	 * Attribute names used for this tag. 
	 */
	private static final String ATTRIBUTE_CLASS = "class";

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
	 * @return
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(final Element element)
	{
		// We don't know the aspect's type beyond the assumptions that it is annotated
		// with @Aspect and has a default constructor
		return Object.class;
	}

	/**
	 * @param element
	 * @param bean
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element,
	 *      org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doParse(final Element element, final BeanDefinitionBuilder bean)
	{
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		final Class<?> aspectClass = getAspectClass(className);
		bean.getRawBeanDefinition().setBeanClass(aspectClass);

		// We may add customization attributes here in the future. For now, we use
		// the default constructor of the aspect.
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param className
	 * @return
	 */
	private static Class<?> getAspectClass(final String className)
	{
		final Class<?> clazz = ClasspathUtil.loadClass(className);
		ValidationUtil.validateNotNull("Aspect class", clazz);
		if (!isAnnotatedWith(clazz, Aspect.class))
		{
			throw new BeanInitializationException("Aspect class " + className
					+ " is not annotated with @Aspect!");
		}
		return clazz;
	}
}