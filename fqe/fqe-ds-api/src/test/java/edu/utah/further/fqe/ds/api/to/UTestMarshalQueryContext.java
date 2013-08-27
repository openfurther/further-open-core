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
package edu.utah.further.fqe.ds.api.to;

import static edu.utah.further.core.query.domain.MatchType.CONTAINS;
import static edu.utah.further.core.query.domain.Relation.EQ;
import static edu.utah.further.core.query.domain.Relation.GT;
import static edu.utah.further.core.query.domain.Relation.LT;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.queryBuilder;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.stringExpression;
import static edu.utah.further.core.query.domain.SearchType.CONJUNCTION;
import static edu.utah.further.core.query.domain.SearchType.DISJUNCTION;
import static edu.utah.further.core.query.domain.SearchType.LIKE;
import static edu.utah.further.fqe.ds.api.results.ResultType.INTERSECTION;
import static edu.utah.further.fqe.ds.api.results.ResultType.SUM;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.xml.bind.api.JAXBRIContext;

import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.core.query.domain.SortType;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.JaxbConfigurationFactoryBean;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.domain.plan.DependencyRule;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.fixture.FqeDsApiFixture;
import edu.utah.further.fqe.ds.api.to.plan.DependencyRuleToImpl;
import edu.utah.further.fqe.ds.api.to.plan.ExecutionRuleToImpl;
import edu.utah.further.fqe.ds.api.to.plan.PlanToImpl;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;

/**
 * A unit test of marshalling and unmarshalling {@link QueryContext} to XML using JAXB.
 * <p>
 * The child ID is not part of the XML and is <code>null</code> in all entities in this
 * test suite, because we use TOs for simplicity. Otherwise, we would need to control for
 * the random generation of child IDs.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UTestMarshalQueryContext extends FqeDsApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * Test file name.
	 */
	private static final String QUERY_BASIC_XML = "query-context-basic.xml";

	/**
	 * Test file name.
	 */
	private static final String QUERY_BASIC_WITH_NUM_RECORDS_XML = "query-context-basic-with-num-records.xml";

	/**
	 * Test file name.
	 */
	private static final String QUERY_BASIC_WITH_RESULT_VIEWS_XML = "query-context-basic-with-result-views.xml";

	/**
	 * Test file name.
	 */
	private static final String QUERY_DISJUNCTION_XML = "query-context-disjunction.xml";

	/**
	 * Test file name - a QC with multiple SQ's.
	 */
	private static final String QUERY_MULTIPLE_SEARCH_QUERIES_XML = "query-context-multiple-search-queries.xml";

	/**
	 * Test file name - a QC with a query plan.
	 */
	private static final String QUERY_WITH_PLAN_XML = "query-context-with-plan.xml";

	/**
	 * Test file name.
	 */
	private static final String QUERY_STATUS_XML = "query-context-status.xml";

	/**
	 * The JAXB Config
	 */
	private static final Map<String, Object> FQE_JAXB_CONFIG = JaxbConfigurationFactoryBean.DEFAULT_JAXB_CONFIG;

	// ========================= FIELDS ====================================

	/**
	 * Stale date & time for the test QC.
	 */
	private Date staleDateTime;

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	/**
	 * Set up test objects, configure JAXB.
	 */
	@Before
	public void setup()
	{
		// Overwrite and therefore override the base JAXB configuration
		FQE_JAXB_CONFIG.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, XmlNamespace.FQE);
		TimeService.fixSystemTime(10000);
		staleDateTime = TimeService.getDate();
	}

	/**
	 * Reset environment.
	 */
	@Before
	public void tearDown()
	{
		// Reset time source so that all subsequent dates in the code are "normal"
		TimeService.reset();
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a query context object with no query set, just a state field is set.
	 */
	@Test
	public void marshalQueryContextBasic() throws Exception
	{
		marshallingTest(QUERY_BASIC_XML, newQueryContextToBasic(false));
	}

	/**
	 * Marshal a query context object with no query set, just the state field and the
	 * nested resultData.numRecords field are set.
	 */
	@Test
	public void marshalQueryContextBasicWithNumRecords() throws Exception
	{
		marshallingTest(QUERY_BASIC_WITH_NUM_RECORDS_XML,
				newQueryContextToBasicWithNumRecords());
	}

	/**
	 * Marshal a query context object with a result view map.
	 */
	@Test
	public void marshalQueryContextBasicWithResultViews() throws Exception
	{
		marshallingTest(QUERY_BASIC_WITH_RESULT_VIEWS_XML,
				newQueryContextToBasicWithResultViews());
	}

	/**
	 * Marshal a query context object with a {@link StatusMetaData} association.
	 */
	@Test
	public void marshalQueryContextStatus() throws Exception
	{
		marshallingTest(QUERY_STATUS_XML, newQueryContextToStatus());
	}

	/**
	 * Marshal a query context object with a {@link SearchQuery} association. Compare with
	 * a pre-cooked output XML file.
	 */
	@Test
	public void marshalQueryContextDisjunction() throws Exception
	{
		marshallingTest(QUERY_DISJUNCTION_XML, newQueryContextToDisjunction());
	}

	/**
	 * Marshal a query context object with multiple {@link SearchQuery}s.
	 */
	@Test
	public void marshalQueryContextMultipleSearchQueries() throws Exception
	{
		marshallingTest(QUERY_MULTIPLE_SEARCH_QUERIES_XML, newQueryContextToWithMultSqs());
	}

	/**
	 * Marshal a query context object with a plan of execution
	 */
	@Test
	public void marshalQueryContextWithPlan() throws Exception
	{
		marshallingTest(QUERY_WITH_PLAN_XML, newQueryContextToWithPlan());
	}

	/**
	 * Unmarshal a query context object with a {@link SearchQuery} association from an XML
	 * file.
	 */
	@Test
	public void unmarshalQueryDisjunction() throws Exception
	{
		unmarshallingTest(QUERY_DISJUNCTION_XML, newQueryContextToDisjunction());
	}

	/**
	 * Unmarshal a query context object with multiple {@link SearchQuery} association from
	 * an XML file.
	 */
	@Test
	public void unmarshalQueryContextMultipleSearchQueries() throws Exception
	{
		unmarshallingTest(QUERY_MULTIPLE_SEARCH_QUERIES_XML,
				newQueryContextToWithMultSqs());
	}

	/**
	 * Unmarshal a query context object with a plan of execution.
	 */
	@Test
	public void unmarshalQueryContextWithPlan() throws Exception
	{
		unmarshallingTest(QUERY_WITH_PLAN_XML, newQueryContextToWithPlan());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param fileName
	 * @param expected
	 * @throws JAXBException
	 * @throws IOException
	 */
	private void unmarshallingTest(final String fileName, final QueryContextTo expected)
			throws JAXBException, IOException
	{
		final QueryContextTo actual = xmlService.unmarshalResource(fileName,
				QueryContextTo.class);
		assertQueryContextEquals(actual, expected);
	}

	/**
	 * @param actual
	 * @param expected
	 */
	private void assertQueryContextEquals(final QueryContextTo actual,
			final QueryContextTo expected)
	{
		final SearchQuery expectedQuery = expected.getQuery();
		final SearchQuery actualQuery = actual.getQuery();

		final SearchCriterion expectedRoot = expectedQuery.getRootCriterion();
		final SearchCriterion actualRoot = actualQuery.getRootCriterion();

		assertEquals(expectedRoot.getSearchType(), actualRoot.getSearchType());
		assertEquals(expectedRoot.getCriteria().size(), actualRoot.getCriteria().size());
	}

	/**
	 * @param query
	 * @return
	 */
	private QueryContextTo newQueryContextToBasic(final boolean withExecutionId)
	{
		final QueryContextTo queryContextTo = QueryContextToImpl.newInstance();
		//Set this fixed for asserting the XML
		if (withExecutionId) {
			queryContextTo.setExecutionId("3c0c8360-09f7-11e0-81e0-0800200c9a66");
		}
		queryContextTo.setState(QueryState.QUEUED);
		queryContextTo.setStaleDateTime(staleDateTime);
		return queryContextTo;
	}

	/**
	 * @param query
	 * @return
	 */
	private QueryContextTo newQueryContextToBasicWithNumRecords()
	{
		final QueryContextTo queryContextTo = newQueryContextToBasic(false);
		queryContextTo.setNumRecords(123L);
		queryContextTo.setStaleDateTime(staleDateTime);
		return queryContextTo;
	}

	/**
	 * @param query
	 * @return
	 */
	@SuppressWarnings("boxing")
	private QueryContextTo newQueryContextToBasicWithResultViews()
	{
		final QueryContextTo queryContextTo = newQueryContextToBasic(false);
		queryContextTo.setQuery(newSimpleSearchQuery(123));
		FqeDsQueryContextUtil.addResultViewTo(queryContextTo, SUM, null, 123l);
		FqeDsQueryContextUtil.addResultViewTo(queryContextTo, INTERSECTION, 1, 456l);
		queryContextTo.setStaleDateTime(staleDateTime);
		return queryContextTo;
	}

	/**
	 * @param query
	 * @return
	 */
	private QueryContextTo newQueryContextToDisjunction()
	{
		final SearchQuery query = newSearchQueryWithConjunction();
		final QueryContextTo queryContextTo = QueryContextToImpl.newInstance();
		queryContextTo.setNumRecords(123L);
		queryContextTo.setState(QueryState.QUEUED);
		queryContextTo.setStaleDateTime(staleDateTime);
		queryContextTo.setQuery(query);
		return queryContextTo;
	}

	/**
	 * @param query
	 * @return
	 */
	private QueryContextTo newQueryContextToStatus()
	{
		final QueryContextTo queryContextTo = QueryContextToImpl.newInstance();
		queryContextTo.setState(QueryState.EXECUTING);
		queryContextTo.setStaleDateTime(staleDateTime);
		final StatusMetaData statusMetaData = new StatusMetaDataToImpl();
		statusMetaData.setStatus("Translating query...");
		TimeService.fixSystemTime(10000);
		final Date curr = TimeService.getDate();
		statusMetaData.setStatusDate(curr);
		queryContextTo.setCurrentStatus(statusMetaData);
		return queryContextTo;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private SearchQueryTo newSimpleAgeSearchQueryWithId(final long id, final int age)
	{
		return SearchQueryTo.newCopy(queryBuilder(simpleExpression(EQ, "age", age))
				.setFirstResult(1)
				.setId(id)
				.setMaxResults(2)
				.addSortCriterion(SearchCriteria.sort("age", SortType.ASCENDING))
				.build());
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private SearchQuery newSearchQueryWithConjunction()
	{
		final SearchCriterion orCrit = junction(DISJUNCTION);
		orCrit.addCriterion(simpleExpression(EQ, "age", 40));
		final SearchCriterion andCrit = junction(CONJUNCTION);
		andCrit.addCriterion(simpleExpression(GT, "age", 50));
		andCrit.addCriterion(simpleExpression(LT, "age", 60));
		andCrit.addCriterion(stringExpression(LIKE, "comment", "keyword", CONTAINS));
		orCrit.addCriterion(andCrit);

		return queryBuilder(orCrit)
				.setFirstResult(1)
				.setMaxResults(2)
				.addSortCriterion(SearchCriteria.sort("age", SortType.ASCENDING))
				.build();
	}

	/**
	 * A {@link SearchQuery} test object factory method.
	 *
	 * @param qid
	 *            search query ID
	 * @return simple-criterion-search-query instance
	 */
	@SuppressWarnings("boxing")
	private static SearchQuery newSimpleSearchQuery(final long qid)
	{
		final SearchCriterion andCrit = junction(CONJUNCTION);
		andCrit.addCriterion(simpleExpression(Relation.EQ, "age", 30));

		final SearchQuery query = queryBuilder(andCrit).setId(qid).build();
		return query;
	}

	/**
	 * Returns a QueryContext with multiple SearchQuery's
	 *
	 * @return
	 */
	private QueryContextTo newQueryContextToWithMultSqs()
	{
		final QueryContextTo queryContextTo = newQueryContextToBasic(true);

		final SearchQueryTo searchQueryToOne = newSimpleAgeSearchQueryWithId(1L, 40);
		final SearchQueryTo searchQueryToTwo = newSimpleAgeSearchQueryWithId(2L, 50);

		queryContextTo.addQuery(searchQueryToOne);
		queryContextTo.addQuery(searchQueryToTwo);

		return queryContextTo;
	}

	/**
	 * Creates a QueryContext with multiple SearchQuery's and a plan.
	 *
	 * @param query
	 * @return
	 */
	@SuppressWarnings("boxing")
	private QueryContextTo newQueryContextToWithPlan()
	{
		final QueryContextTo queryContextTo = newQueryContextToWithMultSqs();

		final ExecutionRule executionRuleOne = new ExecutionRuleToImpl();
		executionRuleOne.setId("34c32ae0-09f8-11e0-81e0-0800200c9a66");
		executionRuleOne.setSearchQueryId(1L);
		executionRuleOne.setDataSourceId("datasource1");

		final ExecutionRule executionRuleTwo = new ExecutionRuleToImpl();
		executionRuleTwo.setId("3d525460-09f8-11e0-81e0-0800200c9a66");
		executionRuleTwo.setSearchQueryId(2L);
		executionRuleTwo.setDataSourceId("datasource2");

		final DependencyRule dependencyRuleOne = new DependencyRuleToImpl();
		dependencyRuleOne.setDependencyExecutionId(executionRuleOne.getId());
		dependencyRuleOne.setOutcomeExecutionId(executionRuleTwo.getId());

		final Plan plan = new PlanToImpl();
		plan.addExecutionRule(executionRuleOne);
		plan.addExecutionRule(executionRuleTwo);
		plan.addDependencyRule(dependencyRuleOne);

		queryContextTo.setPlan(plan);

		return queryContextTo;
	}
}
