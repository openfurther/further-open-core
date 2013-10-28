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
package edu.utah.further.ds.impl.executor.db.hibernate.criteria;

import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * An executor which takes a List of results from a Hibernate execution and re-reads the
 * state of the object. This processor is used when EAGER fetch strategies may not be
 * possible but an object needs to be fully hyrdated before it can be properly used.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 28, 2013
 */
@Service("hibernateResultListRefresher")
public class HibernateResultListRefresher extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MethodHandles.lookup().lookupClass());

	// ========================= IMPL =================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Re-reading entity state by calling refresh");
		}

		final List<?> results = request.getAttribute(QUERY_RESULT);
		Validate.notNull(results, "Expected query results");
		final HibernateExecReq executionReq = new HibernateExecReq(request);
		final SessionFactory sessionFactory = executionReq.getSessionFactory();
		Validate.notNull(sessionFactory,
				"SessionFactory required to re-read entity state");

		final Session session = sessionFactory.getCurrentSession();
		for (final Object result : results)
		{
			if (log.isTraceEnabled())
			{
				log.trace("Before refreshing " + result);
			}

			session.refresh(result);

			if (log.isTraceEnabled())
			{
				log.trace("After refreshing " + result);
			}
		}

		if (log.isDebugEnabled())
		{
			log.debug("All entities refreshed");
		}

		return false;
	}

}
