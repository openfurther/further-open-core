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
package edu.utah.further.ds.impl.executor.db.hibernate;

import static edu.utah.further.ds.api.util.AttributeName.SEARCH_QUERY;
import static edu.utah.further.ds.api.util.AttributeName.SEARCH_QUERY_PKG;
import static edu.utah.further.ds.api.util.AttributeName.SEARCH_QUERY_ROOT;
import static edu.utah.further.ds.api.util.AttributeName.SESSION_FACTORY;
import static org.slf4j.LoggerFactory.getLogger;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.api.request.QueryExecutionRequest;

/**
 * A Hibernate Query Execution Request which wraps a {@link ChainRequest} and defines key
 * constants for setting and retrieving objects within a request that are related to
 * Hibernate.
 * <p>
 * This pattern allows for typed retrieval of objects.
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
 * @version Oct 1, 2009
 */
public class HibernateExecReq extends QueryExecutionRequest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(HibernateExecReq.class);

	// ========================= CONSTRUCTORS =======================

	/**
	 * @param chainRequest
	 *            the chainRequest to set
	 */
	public HibernateExecReq(final ChainRequest chainRequest)
	{
		super(chainRequest);
	}

	// ========================= GETTERS & SETTERS ==================

	/**
	 * @return the rootEntity
	 */
	public Class<? extends PersistentEntity<?>> getRootEntity()
	{
		// Check if it's already set
		Class<? extends PersistentEntity<?>> clazz = getChainRequest().getAttribute(
				SEARCH_QUERY_ROOT);

		if (clazz == null)
		{
			// Load & cache the class
			final String pkg = getChainRequest().getAttribute(SEARCH_QUERY_PKG);
			final SearchQuery query = getChainRequest().getAttribute(SEARCH_QUERY);

			clazz = query.getRootObject(pkg);

			setRootEntity(clazz);
		}

		return clazz;

	}

	/**
	 * @param clazz
	 *            the rootEntity to set
	 */
	public void setRootEntity(final Class<? extends PersistentEntity<?>> clazz)
	{
		getChainRequest().setAttribute(SEARCH_QUERY_ROOT, clazz);
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory()
	{
		return getChainRequest().getAttribute(SESSION_FACTORY);
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		getChainRequest().setAttribute(SESSION_FACTORY, sessionFactory);
	}
}
