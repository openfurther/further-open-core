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

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * A parser of the Spring schema "further"'s <code>dateformat</code> element.
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
final class SimpleDateFormatBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser
{
	// ========================= CONSTANTS =================================

	/**
	 * Element attribute names.
	 */
	private static final String PATTERN = "pattern";
	private static final String ATTRIBUTE_LENIENT = "lenient";

	// ========================= IMPLEMENTATION: AbstractSingleBeanDefini. =

	/**
	 * @param element
	 * @return
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(final Element element)
	{
		return SimpleDateFormat.class;
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
		// this will never be null since the schema explicitly requires that a value be
		// supplied
		final String pattern = element.getAttribute(PATTERN);
		bean.addConstructorArgValue(pattern);

		// this however is an optional property
		final String lenient = element.getAttribute(ATTRIBUTE_LENIENT);
		if (StringUtils.hasText(lenient))
		{
			bean.addPropertyValue(ATTRIBUTE_LENIENT, Boolean.valueOf(lenient));
		}
	}
}