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
package edu.utah.further.party3.apelon.internal.test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.client.namespace.NamespaceType;

import edu.utah.further.party3.apelon.internal.ApelonDtsObjectFactoryImpl;
import edu.utah.further.party3.apelon.internal.ApelonHardcodedNamespaceForTests;

/**
 * A client code that calls the Apelon DTS API without many dependencies on other FURTHeR
 * modules. Think about that as the ESB test suite that invokes the code we would like to
 * test upon starting this bundle.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 4, 2009
 */
public final class ApelonTester implements InitializingBean, DisposableBean
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(ApelonTester.class);

	// ========================= DEPENEDENCIES ============================

	/**
	 * Manually-wired DTS object factory.
	 */
	private ApelonDtsObjectFactoryImpl dtsObjectFactory;

	// ========================= CONSTRUCTORS =============================

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	@PostConstruct
	public void afterPropertiesSet() throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing DTS connection");
		}

		// ----------------------------------
		// Add testing code here
		// ----------------------------------
		dtsObjectFactory.getConnectionFactory().startSession();
		testNamespaceTypes();
		dtsObjectFactory.getConnectionFactory().closeSession();
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
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
	public void testNamespaceTypes()
	{
		final Namespace ns = findNamespaceByName(ApelonHardcodedNamespaceForTests.SNOMED
				.getName());
		if (log.isDebugEnabled())
		{
			log.debug("Found namespace: " + ns);
		}
		if ((ns == null) || (NamespaceType.ONTYLOG != ns.getNamespaceType()))
		{
			throw new BeanInitializationException("Wrong namespace type. Expected: "
					+ NamespaceType.ONTYLOG + " found " + getNamespaceTypeNullSafe(ns));
		}

		final Namespace ns2 = findNamespaceByName(ApelonHardcodedNamespaceForTests.CERNER
				.getName());
		if ((ns2 == null) || (NamespaceType.THESAURUS != ns2.getNamespaceType()))
		{
			throw new BeanInitializationException("Wrong namespace type. Expected: "
					+ NamespaceType.THESAURUS + " found " + getNamespaceTypeNullSafe(ns2));
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the dtsObjectFactory property.
	 * 
	 * @param dtsObjectFactory
	 *            the dtsObjectFactory to set
	 */
	public void setDtsObjectFactory(final ApelonDtsObjectFactoryImpl dtsObjectFactory)
	{
		this.dtsObjectFactory = dtsObjectFactory;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return a namespace by name.
	 * 
	 * @param name
	 *            namespace name
	 * @return the namespace
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @see edu.utah.further.dts.api.service.DtsOperationService#findNamespaceByName(java.lang.String)
	 */
	private Namespace findNamespaceByName(final String name)
	{
		final NamespaceQuery nameQuery = dtsObjectFactory.createNamespaceQuery();
		Namespace ns = null;
		try
		{
			ns = nameQuery.findNamespaceByName(name);
		}
		catch (final DTSException e)
		{
			throw new IllegalStateException("Namespace exception", e);
		}

		if (ns == null)
		{
			throw new IllegalStateException("Namespace '" + name + "' not found ");
		}
		return ns;
	}

	/**
	 * @param ns
	 * @return
	 */
	private NamespaceType getNamespaceTypeNullSafe(final Namespace ns)
	{
		return ((ns == null) ? null : ns.getNamespaceType());
	}
}
