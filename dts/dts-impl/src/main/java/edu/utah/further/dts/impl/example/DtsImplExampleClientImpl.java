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
package edu.utah.further.dts.impl.example;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.namespace.NamespaceQuery;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.impl.service.DtsObjectFactory;
import edu.utah.further.dts.impl.util.DtsImplResourceLocator;

/**
 * A client code that calls the Apelon DTS API. Think about that as the ESB test suite
 * that invokes the code we would like to test upon starting this bundle.
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
 * @version Aug 4, 2009
 */
public class DtsImplExampleClientImpl implements InitializingBean, DisposableBean
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(DtsImplExampleClientImpl.class);

	// ========================= DEPENEDENCIES ============================

	/**
	 * DTS resource locator. Not really needed, but forces this dependency to be loaded
	 * before this tester class is, an issue that has been causing some problems before.
	 */
	@SuppressWarnings("unused")
	private DtsImplResourceLocator dtsImplResourceLocator;

	/**
	 * DTS object factory.
	 */
	private DtsObjectFactory dtsObjectFactory;

	/**
	 * DTS Connection factory.
	 */
	private ConnectionFactory connectionFactory;

	/**
	 * DTS operation service. AOPed.
	 */
	private DtsOperationService dtsOperationService;

	// ========================= IMPLEMENTATION: InitializingBean =========

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing DTS connection");
		}

		// ----------------------------------
		// Add testing code here
		// ----------------------------------
		connectionFactory.startSession();
		testNamespaceCountUsingLowLevelApi();
		connectionFactory.closeSession();

		testNamespaceCountUsingDos();
	}

	// ========================= IMPLEMENTATION: DisposableBean ===========

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Shutting down");
		}
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Identify namespace types.
	 */
	public void testNamespaceCountUsingLowLevelApi()
	{
		final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
		Namespace[] namespaces = null;
		try
		{
			namespaces = nameQuery.getNamespaces();
		}
		catch (final DTSException e)
		{
			throw new ApplicationException(
					"Unable to call getNamespaces using low level api", e);
		}
		Validate.notNull(namespaces);
		Validate.isTrue(namespaces.length > 0, "Expected greater than 0 namespaces using low-level api");
	}

	/**
	 * Identify namespace types.
	 */
	public void testNamespaceCountUsingDos()
	{
		final List<DtsNamespace> namespaces = dtsOperationService.getNamespaceList();
		Validate.isTrue(namespaces.size() > 0, "Expected greater than 0 namespaces using DOS");
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the dtsObjectFactory property.
	 * 
	 * @param dtsObjectFactory
	 *            the dtsObjectFactory to set
	 */
	public void setDtsObjectFactory(final DtsObjectFactory dtsObjectFactory)
	{
		this.dtsObjectFactory = dtsObjectFactory;
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

	/**
	 * Set a new value for the dos property.
	 * 
	 * @param dtsOperationService
	 *            the dos to set
	 */
	public void setDtsOperationService(final DtsOperationService dtsOperationService)
	{
		this.dtsOperationService = dtsOperationService;
	}

	/**
	 * Set a new value for the dtsImplResourceLocator property.
	 * 
	 * @param dtsImplResourceLocator
	 *            the dtsImplResourceLocator to set
	 */
	public void setDtsImplResourceLocator(
			final DtsImplResourceLocator dtsImplResourceLocator)
	{
		this.dtsImplResourceLocator = dtsImplResourceLocator;
	}
}
