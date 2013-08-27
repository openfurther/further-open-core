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
package edu.utah.further.fqe.ds.api.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.fqe.ds.api.factory.DefaultStaleDateTimeFactory;
import edu.utah.further.fqe.ds.api.factory.StaleDateTimeFactory;

/**
 * A convenient resource-locating class to inject services into non-Spring-managed
 * classes. This implementation does not depend on LTW support/Configurable annotation.
 * Obviously, only domain classes created AFTER this service is initialized can be DI-ed.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Mar 25, 2010
 */
@Service("fqeDsApiResourceLocator")
@ResourceLocator
public final class FqeDsApiResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static FqeDsApiResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * The default stale time factory
	 */
	@Autowired
	private StaleDateTimeFactory staleDateTime;

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
		synchronized (FqeDsApiResourceLocator.class)
		{
			instance = this;
		}
	}

	/**
	 * Return the instance property.
	 * 
	 * @return the instance
	 */
	public static FqeDsApiResourceLocator getInstance()
	{
		return instance;
	}

	/**
	 * Return the stale date & time factory
	 * 
	 * @return
	 */
	public StaleDateTimeFactory getStaleDateTimeFactory()
	{
		if (staleDateTime == null)
		{
			// Default instance with 1 HOUR
			staleDateTime = new DefaultStaleDateTimeFactory();
		}

		return staleDateTime;
	}

}
