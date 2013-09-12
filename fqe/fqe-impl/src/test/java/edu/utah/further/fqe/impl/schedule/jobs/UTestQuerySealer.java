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
package edu.utah.further.fqe.impl.schedule.jobs;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.factory.DefaultStaleDateTimeFactory;
import edu.utah.further.fqe.ds.api.factory.StaleDateTimeFactory;
import edu.utah.further.fqe.ds.api.service.results.ResultSummaryService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;
import edu.utah.further.fqe.impl.scheduler.jobs.QuerySealer;

/**
 * Tests sealing stale queries in the various {@link QueryState}'s.
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
 * @version Mar 29, 2010
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public final class UTestQuerySealer extends FqeImplUtestFixture
{
	// ========================= FIELDS =============================

	/**
	 * Stale date & time for the test QC.
	 */
	private Date staleDateTime;

	// ========================= DEPENDENCIES =======================

	/**
	 * A sealer service - no need to use Spring in this case to instantiate it.
	 */
	@Autowired
	private QuerySealer querySealer;

	/**
	 * This test uses the DAO directory instead of {@link QueryContextService} to insert
	 * QC's into the database in a very specific state without worrying about the
	 * intricacies of the inner workings of the various methods which persist QC's to a
	 * database.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	/**
	 * Service for retrieving count results for result views.
	 */
	@Autowired
	private ResultSummaryService resultService;

	/**
	 * A factory for producing stale date times.
	 */
	private final StaleDateTimeFactory staleDateTimeFactory = new DefaultStaleDateTimeFactory();

	// ========================= SETUP METHODS ======================

	/**
	 * Clean up database.
	 */
	@Before
	public void setup()
	{
		querySealer.setQcService(queryContextService);
		// Delete all contexts in the database
		queryContextService.deleteAll();
		assertNumberQueryContextsInDatabase(0);
		staleDateTime = FqeDsQueryContextUtil.getStaleDateTimeInPast();
		expect(
				resultService.join(EasyMock.<List<String>> anyObject(),
						anyObject(ResultType.class)))
				.andStubReturn(new Long(1));
		replay(resultService);
	}

	/**
	 * Reset environment. Clean up database.
	 */
	@Before
	public void tearDown()
	{
		// Delete all contexts in the database
		queryContextService.deleteAll();
		assertNumberQueryContextsInDatabase(0);
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Tests sealing a query in the SUBMITTED state.
	 */
	@Test
	public void submittedQuery()
	{

		// Create a transient context
		final QueryContext newQueryContext = newTweakedEntity();
		dao.save(newQueryContext);

		querySealer.run();

		assertFailedStaleState(newQueryContext);
	}

	/**
	 * Tests sealing a query in the QUEUED state.
	 */
	@Test
	public void queuedQuery()
	{

		// Create a transient context
		final QueryContext newQueryContext = newTweakedEntity();
		newQueryContext.queue();
		dao.save(newQueryContext);

		querySealer.run();

		assertFailedStaleState(newQueryContext);
	}

	/**
	 * Tests sealing a query in the EXECUTING state that is stale and the minimum number
	 * of data sources have not responded.
	 */
	@Test
	public void executingMinNotReturnedQuery()
	{
		// Create a transient context
		final QueryContext newQueryContext = newTweakedEntity();
		newQueryContext.setMinRespondingDataSources(10);
		newQueryContext.queue();
		newQueryContext.start();
		dao.save(newQueryContext);

		querySealer.run();

		assertFailedStaleState(newQueryContext);
	}

	/**
	 * Tests sealing a query in the EXECUTING state that is stale and the minimum number
	 * of data sources have responded.
	 */
	@Test
	public void executingMinReturnedQuery()
	{
		// Create a transient context
		final QueryContext parentQueryContext = newTweakedEntity();
		parentQueryContext.setMinRespondingDataSources(1);
		parentQueryContext.queue();
		parentQueryContext.start();
		dao.save(parentQueryContext);

		// Child has COMPLETED
		dao.save(completedChildQc(parentQueryContext));

		querySealer.run();

		assertCompletedState(parentQueryContext);
	}

	/**
	 * Tests sealing a query in the EXECUTING state.
	 */
	@Test
	public void executingNotStaleCompletedQuery()
	{
		// Create a transient context
		final QueryContext parentQueryContext = newTweakedEntity();
		// Default is +1 HOUR from now
		parentQueryContext.setStaleDateTime(staleDateTimeFactory.getStaleDateTime());
		parentQueryContext.queue();
		parentQueryContext.start();
		dao.save(parentQueryContext);

		// Child is completed
		dao.save(completedChildQc(parentQueryContext));

		querySealer.run();

		assertCompletedState(parentQueryContext);
	}

	/**
	 * Tests sealing a query in the EXECUTING state.
	 */
	@Test
	public void executingNotStaleNotCompletedQuery()
	{
		// Create a transient context
		final QueryContext parentQueryContext = newTweakedEntity();
		// Default is +1 HOUR from now
		parentQueryContext.setStaleDateTime(staleDateTimeFactory.getStaleDateTime());
		parentQueryContext.queue();
		parentQueryContext.start();
		dao.save(parentQueryContext);

		// Child is executing
		final QueryContext childQueryContext = newTweakedEntity();
		childQueryContext.setStaleDateTime(staleDateTimeFactory.getStaleDateTime());
		childQueryContext.setParent(parentQueryContext);
		childQueryContext.queue();
		childQueryContext.start();
		dao.save(childQueryContext);

		querySealer.run();

		assertExecutingState(parentQueryContext);
	}

	/**
	 * Tests sealing a query in the FAILED state.
	 */
	@Test
	public void failedQuery()
	{
		assertNumberQueryContextsInDatabase(0);

		// Create a transient context
		final QueryContext newQueryContext = newTweakedEntity();
		newQueryContext.queue();
		newQueryContext.start();
		newQueryContext.fail();
		dao.save(newQueryContext);

		querySealer.run();

		assertFailedStaleState(newQueryContext);
	}

	/**
	 * Tests sealing a query in the COMPLETED state.
	 */
	@Test
	public void completedQuery()
	{
		// Create a transient context
		final QueryContext newQueryContext = newTweakedEntity();
		newQueryContext.queue();
		newQueryContext.start();
		newQueryContext.finish();
		dao.save(newQueryContext);

		querySealer.run();

		final QueryContext staleQc = queryContextService.findAll().get(0);
		assertThat(staleQc.getState(), is(QueryState.COMPLETED));
		assertFalse(staleQc.isStale());
	}

	/**
	 * Returns a child query context in completed state.
	 * 
	 * @param parentQueryContext
	 */
	private QueryContext completedChildQc(final QueryContext parentQueryContext)
	{
		final QueryContext childQueryContext = newTweakedEntity();
		childQueryContext.setStaleDateTime(staleDateTimeFactory.getStaleDateTime());
		childQueryContext.setParent(parentQueryContext);
		childQueryContext.queue();
		childQueryContext.start();
		childQueryContext.finish();
		return childQueryContext;
	}

	/**
	 * Private helper method to assert that all queries move to a failed state.
	 * 
	 * @param queryContext
	 */
	private void assertFailedStaleState(final QueryContext queryContext)
	{
		final QueryContext staleQc = queryContextService.findAll().get(0);
		assertThat(staleQc.getState(), is(QueryState.FAILED));
		assertTrue(staleQc.isStale());
	}

	/**
	 * Private helper method to assert a completed state.
	 * 
	 * @param queryContext
	 */
	private void assertCompletedState(final QueryContext queryContext)
	{
		final QueryContext completedQc = queryContextService.findAll().get(0);
		assertThat(completedQc.getState(), is(QueryState.COMPLETED));
	}

	/**
	 * Private helper method to assert an executing state.
	 * 
	 * @param queryContext
	 */
	private void assertExecutingState(final QueryContext queryContext)
	{
		final QueryContext executingQc = queryContextService.findAll().get(0);
		assertThat(executingQc.getState(), is(QueryState.EXECUTING));
	}

	/**
	 * @return
	 */
	private QueryContext newTweakedEntity()
	{
		final QueryContext entity = newQueryContextEntity();
		entity.setStaleDateTime(staleDateTime);
		return entity;
	}
}
