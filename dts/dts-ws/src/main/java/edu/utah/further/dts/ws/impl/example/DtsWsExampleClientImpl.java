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
package edu.utah.further.dts.ws.impl.example;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;

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
public class DtsWsExampleClientImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(DtsWsExampleClientImpl.class);

	// ========================= DEPENEDENCIES ============================

	/**
	 * DTS operation service. AOPed.
	 */
	private DtsOperationService dtsOperationService;

	// ========================= CONSTRUCTORS =============================

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing DTS connection");
		}
		// ----------------------------------
		// Add testing code here
		// ----------------------------------
		testNamespaceCountUsingDos();
	}

	/**
	 * Shut down.
	 */
	@PreDestroy
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
	public void testNamespaceCountUsingDos()
	{
		final List<DtsNamespace> namespaces = dtsOperationService.getNamespaceList();
		Validate.notNull(namespaces);
		Validate.isTrue(namespaces.size() > 0, "Expected greater than 0 namespaces");
	}

	// ========================= GETTERS & SETTERS =========================

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
}
