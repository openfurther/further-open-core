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
package edu.utah.further.mdr.impl.service.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.dts.api.service.DtsOperationService;

/**
 * A convenient resource-locating class to inject services into non-Spring-managed classes
 * in this package. An alternative to using Spring's <code>Configurable</code> annotation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 17, 2008
 */
@Component(DtsResourceLocator.BEAN_NAME)
@Scope(Constants.Scope.SINGLETON)
@ResourceLocator
public class DtsResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * Spring bean name.
	 */
	public static final String BEAN_NAME = "dtsResourceLocator";

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static DtsResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Spring application context.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Executes DTS operations.
	 */
	@Autowired
	private DtsOperationService dtsOperationService;

	// ========================= METHODS ===================================

	/**
	 * Initialize the static instance of this class that is visible to classes in this
	 * package.
	 */
	@PostConstruct
	protected void initializeInstance()
	{
		DtsResourceLocator.instance = (DtsResourceLocator) applicationContext
				.getBean(DtsResourceLocator.BEAN_NAME);
	}

	/**
	 * Return the instance property.
	 *
	 * @return the instance
	 */
	public static DtsResourceLocator getInstance()
	{
		return instance;
	}

	/**
	 * Return the dtsOperationService property.
	 *
	 * @return the dtsOperationService
	 */
	public DtsOperationService getDtsOperationService()
	{
		return dtsOperationService;
	}
}
