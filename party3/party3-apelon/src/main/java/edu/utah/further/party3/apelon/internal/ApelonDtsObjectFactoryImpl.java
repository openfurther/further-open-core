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
package edu.utah.further.party3.apelon.internal;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.tqlutil.TQL;

/**
 * A factory that instantiates Apelon objects.
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
 * @version May 29, 2009
 */
public final class ApelonDtsObjectFactoryImpl
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Manually-wired DTS connection factory. We refrain from using Spring testing for the
	 * time being, for speedy tests.
	 */
	private ApelonConnectionFactory connectionFactory;

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Create a new namespace query object.
	 *
	 * @return a new namespace query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createNamespaceQuery()
	 */
	public NamespaceQuery createNamespaceQuery()
	{
		return NamespaceQuery.createInstance(getCurrentConnection());
	}

	/**
	 * Create a new search query object.
	 *
	 * @return a new search query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createSearchQuery()
	 */
	public SearchQuery createSearchQuery()
	{
		return SearchQuery.createInstance(getCurrentConnection());
	}

	/**
	 * Create a new navigation query object.
	 *
	 * @return a new navigation query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createNavQuery()
	 */
	public NavQuery createNavQuery()
	{
		return NavQuery.createInstance(getCurrentConnection());
	}

	/**
	 * Create a new association query object.
	 *
	 * @return a new association query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createAssociationQuery()
	 */
	public AssociationQuery createAssociationQuery()
	{
		return AssociationQuery.createInstance(getCurrentConnection());
	}

	/**
	 * Create a new TQL query object.
	 *
	 * @return a new TQL query instance
	 * @see edu.utah.further.dts.impl.service.DtsObjectFactory#createTqlQuery()
	 */
	public TQL createTqlQuery()
	{
		return new TQL(getCurrentConnection());
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the connectionFactory property.
	 *
	 * @return the connectionFactory
	 */
	public ApelonConnectionFactory getConnectionFactory()
	{
		return connectionFactory;
	}

	/**
	 * Set a new value for the connectionFactory property.
	 *
	 * @param connectionFactory
	 *            the connectionFactory to set
	 */
	public void setConnectionFactory(final ApelonConnectionFactory connectionFactory)
	{
		this.connectionFactory = connectionFactory;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.party3.apelon.internal.internal.ApelonConnectionFactory.api.service.ConnectionFactory#getCurrentConnection()
	 */
	private ServerConnection getCurrentConnection()
	{
		return connectionFactory.getCurrentConnection();
	}
}
