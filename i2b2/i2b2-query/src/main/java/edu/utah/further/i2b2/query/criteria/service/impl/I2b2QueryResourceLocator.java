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
package edu.utah.further.i2b2.query.criteria.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;

/**
 * A convenient resource-locating class to inject services into non-Spring-managed
 * classes. This implementation does not depend on LTW support/Configurable annotation.
 * Obviously, only domain classes created AFTER this service is initialized can be DI-ed.
 * <p>
 * Located in this package to avoid exposing it outside this bundle; in theory, even
 * outside this package.
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
@Service("i2b2QueryResourceLocator")
@ResourceLocator
public class I2b2QueryResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static I2b2QueryResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Required for example to translate i2b2 terms into logical concepts.
	 */
	@Autowired
	private I2b2QueryService i2b2QueryService;

	/**
	 * For resolving namespace names and identifiers
	 */
	@Autowired
	private NamespaceService namespaceService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Set a static reference to the Spring-managed instance so that we can refer to it in
	 * domain classes without LTW/Configurable annotation.
	 * </p>
	 */
	@PostConstruct
	protected void initializeStaticAccess()
	{
		synchronized (I2b2QueryResourceLocator.class)
		{
			instance = this;
		}
	}

	/**
	 * Return the instance property.
	 * 
	 * @return the instance
	 */
	public static I2b2QueryResourceLocator getInstance()
	{
		return instance;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the i2b2QueryService property.
	 * 
	 * @return the i2b2QueryService
	 */
	public I2b2QueryService getI2b2QueryService()
	{
		return i2b2QueryService;
	}

	/**
	 * Return the namespaceService property.
	 * 
	 * @return the namespaceService
	 */
	public NamespaceService getNamespaceService()
	{
		return namespaceService;
	}

}
