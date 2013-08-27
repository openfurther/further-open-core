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
package edu.utah.further.dts.impl.service;

import static edu.utah.further.core.api.constant.ErrorCode.SERVER_ERROR;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.beans.dts.plugin.DTSAppManager;
import com.apelon.dts.client.DTSQuery;
import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.tqlutil.TQL;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.impl.util.DtsUtil;

/**
 * A mother object for queries and other useful Apelon DTS objects.
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
 * @version Nov 6, 2008
 */
@Service("dtsObjectFactory")
public class DtsObjectFactoryImpl implements DtsObjectFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DtsObjectFactoryImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Useful internal reference into {@link DTSAppManager}.
	 */
	private DTSQuery dtsQuery;

	// ========================= DEPENDENCIES ==============================

	/**
	 * produces DTS connections.
	 */
	@Autowired
	private ConnectionFactory connectionFactory;

	// ========================= CONSTRUCTORS ==============================

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPLEMENTATION: DtsObjectFactory ==========

	/**
	 * Create a new namespace query object.
	 *
	 * @return a new namespace query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createNamespaceQuery()
	 */
	@Override
	public NamespaceQuery createNamespaceQuery()
	{
		try
		{
			return NamespaceQuery.createInstance(getCurrentConnection());
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(SERVER_ERROR, "Apelon DTS failure: "
					+ e.getClass() + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Create a new search query object.
	 *
	 * @return a new search query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createSearchQuery()
	 */
	@Override
	public SearchQuery createSearchQuery()
	{
		try
		{
			return SearchQuery.createInstance(getCurrentConnection());
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(SERVER_ERROR, "Apelon DTS failure: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Create a new navigation query object.
	 *
	 * @return a new navigation query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createNavQuery()
	 */
	@Override
	public NavQuery createNavQuery()
	{
		try
		{
			return NavQuery.createInstance(getCurrentConnection());
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(SERVER_ERROR, "Apelon DTS failure: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Create a new association query object.
	 *
	 * @return a new association query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createAssociationQuery()
	 */
	@Override
	public AssociationQuery createAssociationQuery()
	{
		try
		{
			return AssociationQuery.createInstance(getCurrentConnection());
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(SERVER_ERROR, "Apelon DTS failure: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Create a new TQL query object.
	 *
	 * @return a new TQL query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createTqlQuery()
	 */
	@Override
	public TQL createTqlQuery()
	{
		try
		{
			final File temp = DtsUtil.getTqlTempOutputFile();
			return new TQL(getCurrentConnection(), temp.getAbsolutePath());
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(SERVER_ERROR, "Apelon DTS failure: "
					+ e.getMessage(), e);
		}
	}

	// ========================= GET/SET METHODS ===========================

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

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#getCurrentConnection()
	 */
	private ServerConnection getCurrentConnection()
	{
		// Set Xerces to be the SAX parser driver. Per Jack Bowie's email, August 24, 2009
		System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");

		// Temporary workaround; TODO: in the future, wrap class TQL etc. with our APIs
		// so that we don't need to use the apelon connection explicitly in this class
		final ServerConnection apelonConnection = ((DtsServerConnectionImpl) connectionFactory
				.getCurrentConnection()).apelonConnection;
		if (apelonConnection == null)
		{
			throw new ApplicationException(SERVER_ERROR,
					"Could not connect to DTS. Connection configuration is "
							+ connectionFactory.getConfig());
		}

		// Initialize the DTS connection and point DTSAppManager to it
		// Note: not necessary if this is a plug-in code
		dtsQuery = DTSAppManager.getQuery();
		dtsQuery.setConnection(apelonConnection);

		return apelonConnection;
	}

}
