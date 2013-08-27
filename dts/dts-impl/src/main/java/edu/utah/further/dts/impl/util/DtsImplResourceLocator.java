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
package edu.utah.further.dts.impl.util;

import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.api.service.DtsOperationService;

/**
 * A convenient resource-locating class to inject services into non-Spring-managed
 * classes. This implementation does not depend on LTW support/Configurable annotation.
 * Obviously, only domain classes created AFTER this service is initialized can be DI-ed.
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
@Service("dtsImplResourceLocator")
@ResourceLocator
public class DtsImplResourceLocator implements InitializingBean
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DtsImplResourceLocator.class);

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static DtsImplResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	@Autowired
	private DtsOperationService dtsOperationService;

	/**
	 * produces DTS connections.
	 */
	@Autowired
	private ConnectionFactory connectionFactory;

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
		synchronized (DtsImplResourceLocator.class)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Initializing instance");
			}
			instance = this;
		}
	}

	/**
	 * Initialize dependencies.
	 */
	@Override
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Validating dependencies");
		}
		ValidationUtil.validateNotNull("dtsOperationService", dtsOperationService);
		ValidationUtil.validateNotNull("connectionFactory", connectionFactory);
	}

	/**
	 * Return the instance property.
	 * 
	 * @return the instance
	 */
	public static DtsImplResourceLocator getInstance()
	{
		// if ((instance == null) && log.isWarnEnabled())
		// {
		// log
		// .warn("Attempting to access dtsOperationService before it was initialized!");
		// }
		return instance;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the dtsOperationService property.
	 * 
	 * @return the dtsOperationService
	 */
	public DtsOperationService getDtsOperationService()
	{
		return dtsOperationService;
	}

	/**
	 * Set a new value for the dtsOperationService property.
	 * 
	 * @param dtsOperationService
	 *            the dtsOperationService to set
	 */
	public void setDtsOperationService(final DtsOperationService dtsOperationService)
	{
		this.dtsOperationService = dtsOperationService;
	}

	/**
	 * Return the connectionFactory property.
	 * 
	 * @return the connectionFactory
	 */
	public ConnectionFactory getConnectionFactory()
	{
		return connectionFactory;
	}

	/**
	 * Set a new value for the connectionFactory property.
	 * 
	 * @param connectionFactory
	 *            the connectionFactory to set
	 */
	public void setConnectionFactory(final ConnectionFactory connectionFactory)
	{
		this.connectionFactory = connectionFactory;
	}
}
