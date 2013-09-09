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
package edu.utah.further.fqe.impl.test;

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static edu.utah.further.fqe.ds.api.service.results.ResultType.SUM;
import static edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil.addResultViewTo;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;

/**
 * A unit test class of the Hibernate configuration and services that runs upon bundle's
 * start-up in an OSGi container.
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
 * @version Jan 28, 2009
 */
public class QueryContextServiceTester
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextServiceTester.class);

	// ========================= FIELDS ====================================

	/**
	 * Handles {@link QueryContext} DAO operations and searches.
	 */
	@Autowired
	private QueryContextService queryContextService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A unit test of this service that runs when the bundle is started.
	 */
	@PostConstruct
	public void createQueryContextFromTo()
	{
		debugPrintAndCenter(log, "createQueryContextFromTo() begin");
		clearQcDatabase();

		// Create a transient context
		final QueryContext queryContext = newQueryContextTo();

		// Persist context to database
		final QueryContext entity = queryContextService.queue(queryContext);
		assertTransientContext(queryContext);
		assertSingleEntityInDatabase(entity);

		debugPrintAndCenter(log, "createQueryContextFromTo() end");
		clearQcDatabase();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Delete all current query contexts from the database.
	 */
	private void clearQcDatabase()
	{
		if (log.isInfoEnabled())
		{
			log.info("Clearing query context database");
		}
		queryContextService.deleteAll();
	}

	/**
	 * @param queryContext
	 * @param expected
	 */
	protected static final void assertQueryContextState(final QueryContext queryContext,
			final QueryState expected)
	{
		assertEquals("Wrong query state", expected, queryContext.getState());
	}

	/**
	 * Print a query context to the console.
	 *
	 * @param queryContext
	 *            object to print
	 */
	protected static final void print(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("QC: " + queryContext);
		}
	}

	/**
	 * @param expected
	 */
	protected final void assertNumberQueryContextsInDatabase(final int expected)
	{
		final List<QueryContext> list = queryContextService.findAll();
		if (log.isDebugEnabled())
		{
			log.debug("Entities in database: " + list);
		}
		assertEquals("Wrong number of query contexts in the database", expected,
				list.size());
	}

	/**
	 * @return
	 */
	private QueryContextTo newQueryContextTo()
	{
		final QueryContextTo entity = QueryContextToImpl.newInstanceWithExecutionId();
		entity.setNumRecords(50L);
		addResultViewTo(entity, SUM, null, FqeDsQueryContextUtil.NUM_RESULTS_IN_VIEW);
		assertTransientContext(entity);
		return entity;
	}

	/**
	 * @param queryContext
	 */
	private void assertTransientContext(final QueryContext queryContext)
	{
		print(queryContext);
		assertQueryContextState(queryContext, QueryState.SUBMITTED);
	}

	/**
	 * @param entity
	 */
	private void assertSingleEntityInDatabase(final QueryContext entity)
	{
		print(entity);
		assertNotNull(entity.getId());
		assertQueryContextState(entity, QueryState.QUEUED);
		assertNumberQueryContextsInDatabase(1);
	}
}
