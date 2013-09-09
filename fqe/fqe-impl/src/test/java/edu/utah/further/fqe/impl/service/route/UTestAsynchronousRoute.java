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
package edu.utah.further.fqe.impl.service.route;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.query.domain.Relation.EQ;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchType.CONJUNCTION;
import static edu.utah.further.fqe.ds.api.domain.QueryState.COMPLETED;
import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertQueryContextStateEquals;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.lang.math.NumberUtils;
import org.easymock.EasyMock;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.plan.DependencyRuleToImpl;
import edu.utah.further.fqe.ds.api.to.plan.ExecutionRuleToImpl;
import edu.utah.further.fqe.ds.api.to.plan.PlanToImpl;
import edu.utah.further.fqe.impl.fixture.FqeImplRouteFixture;

/**
 * Unit test for testing the asynchronous status/result notification route.
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
 * @version Feb 26, 2010
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public final class UTestAsynchronousRoute extends FqeImplRouteFixture
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The topic to send status/results too.
	 */
	@EndpointInject(uri = "statusResult")
	private MockEndpoint statusResult;

	/**
	 * Federated query service in order to trigger a query
	 */
	@Autowired
	private FqeService fqeService;

	/**
	 * Context service in order to retrieve and assert insertions.
	 */
	@Autowired
	private QueryContextService queryContextService;

	/**
	 * Apache Camel Context
	 */
	@Autowired
	private CamelContext camelContext;

	/**
	 * Mock number of records to report for independent queries.
	 */
	@Resource(name = "mockNumResultsIndependentQuery")
	private Long numResultsIndependentQuery;

	/**
	 * Desired output number results from a mock data source for dependent queries.
	 */
	@Resource(name = "mockNumResultsDependentQuery")
	private Long numResultsDependentQuery;

	/**
	 * Service for retrieving count results for result views.
	 */
	@Autowired
	private ResultService resultService;

	// ========================= SETUP METHODS =============================

	@SuppressWarnings("boxing")
	@Before
	public void setup()
	{
		// mock setup
		expect(
				resultService.join(EasyMock.<List<String>> anyObject(),
						anyObject(ResultType.class), anyInt()))
				.andStubReturn(new Long(1));
		replay(resultService);
	}

	/**
	 * Shut everything down. Otherwise some camel exceptions are thrown.
	 * 
	 * @throws Exception
	 */
	@After
	public void after() throws Exception
	{
		try
		{
			camelContext.stop();
		}
		catch (final Exception e)
		{
			// Ignore
		}

		queryContextService.deleteAll();
	}

	// ========================= METHODS ===================================

	/**
	 * Test a broadcast plan with a single data source.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@DirtiesContext
	public void broadcastQueryOneDs() throws InterruptedException
	{
		testBroadcastQuery(1, Relation.GE);
	}

	/**
	 * Test a broadcast plan with a single data source.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@DirtiesContext
	public void broadcastQueryTwoDs() throws InterruptedException
	{
		testBroadcastQuery(2, Relation.EQ);
	}

	/**
	 * Test a branched plan with two data sources.
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	@Test
	@DirtiesContext
	public void branchedQueryMultipleDs() throws InterruptedException
	{
		final QueryContext parent = newBranchedQueryContext(numResultsIndependentQuery,
				numResultsIndependentQuery);
		testPlan(parent, 2, Relation.EQ, 2 * numResultsIndependentQuery);
	}

	/**
	 * Test a phased plan with two data sources.
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	@Test
	@DirtiesContext
	@Ignore("SearchQueryDeEvaluator has been disabled until fully implemented")
	public void phasedQueryMultipleDs() throws InterruptedException
	{
		final QueryContext parent = newPhasedQueryContext(numResultsIndependentQuery);
		testPlan(parent, 2, Relation.EQ, numResultsIndependentQuery
				+ numResultsDependentQuery);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private QueryContext newBroadcastQueryContext()
	{
		final QueryContext parent = QueryContextToImpl.newInstanceWithExecutionId();
		parent.addQuery(newSearchQuery(1l, numResultsIndependentQuery));
		return parent;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private QueryContext newBranchedQueryContext(final long numRecords1,
			final long numRecords2)
	{
		final QueryContext parent = newBroadcastQueryContext();

		// Queries
		final Long qid1 = 1l;
		parent.addQuery(newSearchQuery(qid1, numRecords1));
		final Long qid2 = 2l;
		parent.addQuery(newSearchQuery(qid2, numRecords2));

		// Plan
		final Plan plan = new PlanToImpl();
		plan.addExecutionRule(new ExecutionRuleToImpl(UUID.randomUUID().toString(), qid1,
				"DS3"));
		plan.addExecutionRule(new ExecutionRuleToImpl(UUID.randomUUID().toString(), qid2,
				"DS4"));
		parent.setPlan(plan);
		return parent;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private QueryContext newPhasedQueryContext(final long numRecords1)
	{
		final QueryContext parent = newBroadcastQueryContext();

		// Queries
		final long qid1 = 1l;
		parent.addQuery(newSearchQuery(qid1, numRecords1));
		final long qid2 = 2l;
		parent.addQuery(newDependentSearchQuery(qid1, qid2));

		// Plan - execution rules
		final Plan plan = new PlanToImpl();
		plan.addExecutionRule(new ExecutionRuleToImpl(UUID.randomUUID().toString(), qid1,
				"DS3"));
		plan.addExecutionRule(new ExecutionRuleToImpl(UUID.randomUUID().toString(), qid2,
				"DS4"));

		// Plan - dependency rules
		final String execId1 = plan.getExecutionRules().get(0).getId();
		final String execId2 = plan.getExecutionRules().get(1).getId();
		// Note: violates PLK -- relies on the fact that getPlan() does not return a
		// defensive copy.
		plan.addDependencyRule(new DependencyRuleToImpl(execId1, execId2));

		parent.setPlan(plan);
		return parent;
	}

	/**
	 * A dummy search query for plan tests.
	 * 
	 * @param qid
	 *            search query identifier within the QC + plan
	 * @return search query instance
	 */
	private static SearchQuery newSearchQuery(final Long qid, final long desiredNumRecords)
	{
		final SearchCriterion searchCriterion = SearchCriteria.junction(CONJUNCTION);
		searchCriterion.addCriterion(simpleExpression(EQ, "numRecords", new Long(
				desiredNumRecords)));
		final SearchQuery searchQuery = SearchCriteria.query(searchCriterion, "Person");
		searchQuery.setId(qid);
		return searchQuery;
	}

	/**
	 * A dummy search query that depends on another search query.
	 * 
	 * @param dependencyQid
	 *            the QID we depend on
	 * @param outcomeQid
	 *            our QID
	 * @return search query instance
	 */
	private static SearchQuery newDependentSearchQuery(final long dependencyQid,
			final long outcomeQid)
	{
		final SearchCriterion searchCriterion = SearchCriteria.junction(CONJUNCTION);
		// Set desired # records to 0 on purpose; if the dependent query is not
		// correctly parsed by SearchQueryDeEvaluator, this will cause a wrong
		// expected # results in the test. If the parser works correctly, it will replace
		// the value of the second criterion by the desired # records, which will then
		// be output by DataSourceMock.
		searchCriterion.addCriterion(simpleExpression(EQ, "numRecords",
				NumberUtils.LONG_ZERO));
		searchCriterion.addCriterion(simpleExpression(EQ, "id", "QUERY[" + dependencyQid
				+ "].id"));
		final SearchQuery searchQuery = SearchCriteria.query(searchCriterion, "Person");
		searchQuery.setId(new Long(outcomeQid));
		return searchQuery;
	}

	/**
	 * @param numDataSources
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	private void testBroadcastQuery(final int numDataSources, final Relation relation)
			throws InterruptedException
	{
		testPlan(newBroadcastQueryContext(), numDataSources, relation, numDataSources
				* numResultsIndependentQuery);
	}

	/**
	 * Test a query plan use case. We count how many data sources respond and assert that
	 * their responses are correct.
	 * 
	 * @param fqc
	 *            federated query context
	 * @param numDataSources
	 *            minimum number of data sources required to respond
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	private void testPlan(final QueryContext fqc, final int numDataSources,
			final Relation relation, final long expectedNumResults)
			throws InterruptedException
	{
		// Has no effect
		fqc.setMinRespondingDataSources(numDataSources);
		// Simulate query sealer by forcing the query to complete after so many DSs
		// respond
		fqc.setMaxRespondingDataSources(numDataSources);

		// Start the asynchronous route
		final QueryContext parent = fqeService.triggerQuery(fqc);

		// Add delay so parent is persisted before we find it
		Thread.sleep(1000L);

		// Expect one message per DS at the statusResult topic
		statusResult.expectedMinimumMessageCount(numDataSources);

		// Reload parent QC from database
		final QueryContext reloadedParent = queryContextService.findById(parent.getId());
		assertQueryContextStateEquals(reloadedParent, COMPLETED);
		// Must have results from at least MaxRespondingDataSources DS's
		assertThat(reloadedParent.getNumRecords(),
				toMatcher(relation, expectedNumResults));

		// Find the child by the parent and ensure status was saved by the route
		final List<QueryContext> children = queryContextService.findChildren(parent);
		assertNotNull(children);
		// Must have results from at least MaxRespondingDataSources DS's. Note that
		// some children may be added after the parent is completed, so not all of them
		// may be used to computing the parent's # records
		assertThat(children.size(), greaterThanOrEqualTo(numDataSources));

		for (final QueryContext child : children)
		{
			assertQueryContextStateEquals(child, COMPLETED);
			// assertThat(child.getNumRecords(), is(numResultsIndependentQuery));
		}
		statusResult.assertIsSatisfied();
	}

	/**
	 * Convert a relation operation to a Hamcrest matcher.
	 * 
	 * @param relation
	 *            relation operation
	 * @param argument
	 *            matcher argument
	 * @return corresponding Hamcrest matcher
	 */
	private static <T extends Comparable<T>> Matcher<T> toMatcher(
			final Relation relation, final T argument)
	{
		switch (relation)
		{
			case EQ:
			{
				return is(argument);
			}

			case NE:
			{
				return not(argument);
			}

			case LE:
			{
				return lessThanOrEqualTo(argument);
			}

			case LT:
			{
				return lessThan(argument);
			}

			case GE:
			{
				return greaterThanOrEqualTo(argument);
			}

			case GT:
			{
				return greaterThan(argument);
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relation));
			}
		}
	}
}
