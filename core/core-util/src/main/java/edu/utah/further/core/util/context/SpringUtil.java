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
package edu.utah.further.core.util.context;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import edu.utah.further.core.api.context.Utility;


/**
 * Spring context utilities.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Utility
public final class SpringUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private SpringUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param <T>
	 * @param beanName
	 * @param beanType
	 * @return
	 */
	public static <T> T getBean(final ApplicationContext applicationContext,
			final String beanName, final Class<T> beanType)
	{
		return beanType.cast(applicationContext.getBean(beanName));
	}

	/**
	 * @param <T>
	 * @param beanType
	 * @return
	 */
	public static <T> T getFirstBeanOfType(final ApplicationContext applicationContext,
			final Class<T> beanType)
	{
		final Map<String, T> beansOfType = applicationContext.getBeansOfType(beanType);
		return beansOfType.isEmpty() ? null : beanType.cast(beansOfType
				.entrySet()
				.iterator()
				.next()
				.getValue());
	}

	// ========================= PRIVATE METHODS ===========================

}
