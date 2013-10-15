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
package edu.utah.further.fqe.impl.service.query;

import static edu.utah.further.core.test.util.AssertUtil.assertSizeEquals;
import static edu.utah.further.core.util.io.LoggingUtil.debugPrintBigTitle;
import static edu.utah.further.fqe.ds.api.service.results.ResultType.SUM;
import static edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil.NUM_RESULTS_IN_VIEW;
import static edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil.addResultViewTo;
import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertTransientContext;
import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.newQueryContextTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.impl.domain.StatusMetaDataEntity;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;

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
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestQueryContextService extends FqeImplUtestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestQueryContextService.class);

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 * Clean up database.
	 */
	@Before
	public void setup()
	{
		// Delete all contexts in the database
		queryContextService.deleteAll();
		assertNumberQueryContextsInDatabase(0);
	}

	/**
	 * Clean up database.
	 */
	@After
	public void after()
	{
		// Delete all contexts in the database
		queryContextService.deleteAll();
		assertNumberQueryContextsInDatabase(0);
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * A unit test of persisting a context to the database.
	 */
	@Test
	public void createQueryContextFromEntity()
	{
		// Create a transient context
		final QueryContext queryContext = newQueryContextEntity();

		// Persist context to database
		// No need to retrieve create()'s return value, because we passed in an entity
		queryContextService.queue(queryContext);
		assertSingleEntityInDatabase(queryContext);
	}

	/**
	 * A unit test of persisting a context to the database from a {@link QueryContextTo}.
	 */
	@Test
	public void createQueryContextFromTo()
	{
		// Create a transient context
		final QueryContext queryContext = newQueryContextTo();

		// Persist context to database
		final QueryContext entity = queryContextService.queue(queryContext);
		assertTransientContext(queryContext);
		assertSingleEntityInDatabase(entity);
	}

	/**
	 * A unit test of updating a context to the database.
	 */
	@Test
	public void updateTransientQueryContextFromEntity()
	{
		// Create a transient context
		final QueryContext queryContext = newQueryContextEntity();

		// Persist context to database
		// No need to retrieve create()'s return value, because we passed in an entity
		queryContextService.update(queryContext);
		assertSingleEntityInDatabase(queryContext);
	}

	/**
	 * A unit test of updating a context to the database from a {@link QueryContextTo}.
	 */
	@Test
	public void updateTransientQueryContextFromTo()
	{
		// Create a transient context
		final QueryContext queryContext = newQueryContextTo();

		// Persist context to database
		final QueryContext entity = queryContextService.update(queryContext);
		assertTransientContext(queryContext);
		assertSingleEntityInDatabase(entity);
	}

	/**
	 * A unit test of adding a result view and saving it to the database.
	 *
	 * @see https://jira.chpc.utah.edu/browse/FUR-1361
	 */
	@SuppressWarnings("boxing")
	@Test
	public void resultViewAssociationIsPersisted()
	{
		// Create a transient context
		final QueryContext queryContext = newQueryContextEntity();
		addResultViewTo(queryContext, SUM, NUM_RESULTS_IN_VIEW);

		// Persist context to database
		// No need to retrieve create()'s return value, because we passed in an entity
		queryContextService.update(queryContext);
		assertSingleEntityInDatabase(queryContext);

		// See if we saved the result view along with the QC
		final QueryContext qc = queryContextService.findAll().get(0);
		final ResultContext rc = qc.getResultView(SUM);
		assertNotNull("Result view was not saved", rc);
		assertNotNull("Retrieved result view is not a persistent entity", rc.getId());
		assertThat(rc.getNumRecords(), is(NUM_RESULTS_IN_VIEW));
	}

	/**
	 * Unit test for finding all stale queries
	 */
	@Test
	public void findStaleQueries()
	{
		// Create a transient context
		final QueryContext newQueryContext = newQueryContextEntity();

		// Persist context to database
		queryContextService.queue(newQueryContext);

		final List<QueryContext> staleQueries = queryContextService
				.findStaleQueries(TimeService.getDate());
		for (final QueryContext queryContext : staleQueries)
		{
			assertThat(queryContext.getStaleDateTime(), lessThan(TimeService.getDate()));
		}
	}

	/**
	 * Finds all statuses
	 */
	@Test
	public void findAllStatus()
	{
		// Create 2 transient contexts
		QueryContext queryContext1 = newQueryContextEntity();
		QueryContext queryContext2 = newQueryContextEntity();

		// Persist
		queryContext1 = queryContextService.queue(queryContext1);
		queryContext2 = queryContextService.queue(queryContext2);

		// Update status, persist
		final StatusMetaData statusMetaData1 = StatusMetaDataEntity.newInstance();
		statusMetaData1.setStatus("Status 1");
		queryContext1.setCurrentStatus(statusMetaData1);
		queryContextService.update(queryContext1);

		// Update status, persist
		final StatusMetaData statusMetaData2 = StatusMetaDataEntity.newInstance();
		statusMetaData2.setStatus("Status 2");
		queryContext2.setCurrentStatus(statusMetaData2);
		queryContextService.update(queryContext2);

		// Ensure updates
		final List<StatusMetaData> statuses = queryContextService.findAllStatuses();
		assertSizeEquals(statuses, 2);
	}

	/**
	 * Finds a given status metadata by query context id.
	 */
	@Test
	public void findStatusById()
	{
		// Create transient contexts
		QueryContext queryContext = newQueryContextEntity();

		// Persist QC
		queryContext = queryContextService.queue(queryContext);

		// Update status, update QC
		final StatusMetaData statusMetaData1 = StatusMetaDataEntity.newInstance();
		statusMetaData1.setStatus("Status 1");
		queryContext.setCurrentStatus(statusMetaData1);
		queryContext = queryContextService.update(queryContext);

		final StatusMetaData statusMetaData = queryContextService
				.findCurrentStatusById(queryContext.getId());

		assertThat(statusMetaData.getId(), is(queryContext.getCurrentStatus().getId()));
		assertThat(statusMetaData.getStatus(), is(queryContext
				.getCurrentStatus()
				.getStatus()));

	}

	@Test
	public void findStatusesById()
	{
		debugPrintBigTitle(log, "findStatusesById()");

		// Create transient contexts
		QueryContext queryContext = newQueryContextEntity();

		queryContext = queryContextService.queue(queryContext);

		// Update status, persist
		final StatusMetaData statusMetaData1 = StatusMetaDataEntity.newInstance();
		statusMetaData1.setStatus("Status 1");
		queryContext.setCurrentStatus(statusMetaData1);
		queryContextService.update(queryContext);

		// Update status, persist
		final StatusMetaData statusMetaData2 = StatusMetaDataEntity.newInstance();
		statusMetaData2.setStatus("Status 2");
		queryContext.setCurrentStatus(statusMetaData2);
		queryContextService.update(queryContext);

		final List<StatusMetaData> statuses = queryContextService
				.findAllStatuses(queryContext.getId());

		assertThat(statuses, notNullValue());
		if (log.isDebugEnabled())
		{
			log.debug("statuses = " + statuses);
		}
		assertSizeEquals(statuses, 2);
		assertThatStatusesQueryContextEquals(statuses, queryContext);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param statuses
	 * @param expectedQueryContext
	 */
	private static void assertThatStatusesQueryContextEquals(
			final List<StatusMetaData> statuses, final QueryContext expectedQueryContext)
	{
		for (final StatusMetaData status : statuses)
		{
			assertThat(((StatusMetaDataEntity) status).getQueryContext(),
					is(expectedQueryContext));
		}
	}
}
