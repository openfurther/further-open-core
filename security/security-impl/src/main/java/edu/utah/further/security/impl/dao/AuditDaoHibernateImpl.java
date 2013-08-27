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
package edu.utah.further.security.impl.dao;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.security.api.dao.AuditDao;
import edu.utah.further.security.api.domain.AuditableEvent;

/**
 * Hibernate implementation of audit DAO services.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}
 * @version May 4, 2011
 */

@Implementation
@Repository("auditDao")
public class AuditDaoHibernateImpl implements AuditDao
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AuditDaoHibernateImpl.class);
	
	// ========================= DEPENDENCIES =================================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;
	
	
	// ========================= IMPLEMENTATION: AuditDAOHibernateImpl ===================

	/**
	 * Log (persist, etc) an {@link AuditableEvent}
	 * 
	 * @param event
	 *            the event to log
	 */
	@Override
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void logEvent(final AuditableEvent event)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Logging query " + event);
		}
		dao.save(event);
	}

}
