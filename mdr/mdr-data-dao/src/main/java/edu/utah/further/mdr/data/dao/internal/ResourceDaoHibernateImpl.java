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
package edu.utah.further.mdr.data.dao.internal;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.data.util.HibernateUtil;
import edu.utah.further.mdr.api.dao.ResourceDao;
import edu.utah.further.mdr.api.domain.asset.Resource;

/**
 * A custom Hibernate DAO implementation for {@link Resource} CRUD operations.
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
 * @version Apr 26, 2010
 */
@Implementation
@Repository("resourceDao")
public class ResourceDaoHibernateImpl extends HibernateDaoSupport implements ResourceDao
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResourceDaoHibernateImpl.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for a Spring DAO bean.
	 *
	 * @param sessionFactory
	 *            Hibernate session factory
	 */
	@Autowired
	public ResourceDaoHibernateImpl(final SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	// ========================= IMPLEMENTATION: ResourceDao ===============

	/**
	 * @param path
	 * @return
	 * @see edu.utah.further.mdr.api.dao.ResourceDao#getActiveResourceByPath(java.lang.String)
	 */
	@Override
	public Resource getActiveResourceByPath(final String path)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting active resource by path " + StringUtil.quote(path));
		}
		final List<Resource> resources = HibernateUtil
				.findByCriteriaUsingHql(getHibernateTemplate(), dao
						.getEntityClass(Resource.class), "resource", true,
						"resource.path = ? and resource.version.status.label = ?", path,
						"Active");
		ValidationUtil.validateIsTrue(resources.size() == 1,
				"A unique active resource was not found for path "
						+ StringUtil.quote(path) + ". Found " + resources);
		return resources.get(0);
	}

	// ========================= PRIVATE METHODS ===========================
}
