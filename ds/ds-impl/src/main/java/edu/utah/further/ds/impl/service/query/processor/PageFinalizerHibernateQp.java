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
package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static edu.utah.further.ds.api.util.AttributeName.SESSION_FACTORY;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.PageFinalizer;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Result set page finalization processor suited for Hibernate-based data sources. Clears
 * the Hibernate session.
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
 * @version Jul 29, 2010
 */
public class PageFinalizerHibernateQp extends
		AbstractDelegatingUtilityProcessor<PageFinalizer>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(PageFinalizerHibernateQp.class);

	// ========================= Impl: RequestHandler ======================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
		final List<?> resultPage = request.getAttribute(QUERY_RESULT);
		final PagingLoopController controller = request
				.getAttribute(AttributeName.PAGING_LOOP_CONTROLLER);

		// Perform finalization logic
		// In the future pass only selected attributes to the delegate. For simplicity,
		// pass all of them for now.
		final QueryContext newQueryContext = getDelegate().finalizePage(queryContext,
				resultPage, controller);

		// Clear persistent session
		final SessionFactory sessionFactory = request.getAttribute(SESSION_FACTORY);
		final Session session = sessionFactory.getCurrentSession();
		// Flush the current page's batch of reads, inserts and updates and release memory
		session.flush();
		session.clear();

		// Save results in the chain request
		request.setAttribute(QUERY_CONTEXT, newQueryContext);

		return false;
	}
}
