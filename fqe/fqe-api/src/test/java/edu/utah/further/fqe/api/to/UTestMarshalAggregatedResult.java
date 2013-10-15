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
package edu.utah.further.fqe.api.to;

import static edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil.newResultContextTo;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.xml.bind.api.JAXBRIContext;

import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.JaxbConfigurationFactoryBean;
import edu.utah.further.fqe.api.fixture.FqeApiFixture;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResult;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultTo;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultsTo;
import edu.utah.further.fqe.api.ws.to.aggregate.CategoryTo;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

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
public class UTestMarshalAggregatedResult extends FqeApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * Test file name of a basic example of an aggregated result object graph.
	 */
	private static final String AGGREGATED_RESULTS_BASIC = "aggregated-results-basic.xml";

	/**
	 * Test file name of a basic example of an aggregated result object graph.
	 */
	private static final String AGGREGATED_RESULT_BASIC = "aggregated-result-basic.xml";

	/**
	 * Test file name of a basic example of an aggregated result with ages in the correct
	 * order.
	 */
	private static final String AGGREGATED_RESULT_AGES_ORDERING = "aggregated-result-ages-ordering.xml";

	/**
	 * JAXB Configuration.
	 */
	private static final Map<String, Object> JAXB_CONFIG = JaxbConfigurationFactoryBean.DEFAULT_JAXB_CONFIG;

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	/**
	 * Set up test objects, configure JAXB.
	 */
	@Before
	public void setup()
	{
		// Overwrite and therefore override the base JAXB configuration
		// TimeService.fixSystemTime(10000);
		JAXB_CONFIG.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, "");
		xmlService.setDefaultJaxbConfig(JAXB_CONFIG);

	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a query context object with a {@link StatusMetaData} association.
	 */
	@Test
	public void marshalAllFederatedResults() throws Exception
	{
		marshallingTest(AGGREGATED_RESULTS_BASIC, newAggregatedResultsBasic());
	}

	/**
	 * Marshal a query context object with a {@link StatusMetaData} association.
	 */
	@Test
	public void marshalSingleFederatedResult() throws Exception
	{
		marshallingTest(AGGREGATED_RESULT_BASIC, newAggregatedResultBasic());
	}

	/**
	 * Tests that an aggregation result is marshalled with natural ordering
	 * 
	 * @throws Exception
	 */
	@Test
	public void marshalAgesWithCorrectOrdering() throws Exception
	{
		marshallingTest(AGGREGATED_RESULT_AGES_ORDERING,
				newAggregatedResultWithOrderingImportance());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param query
	 * @return
	 */
	private AggregatedResult newAggregatedResultBasic()
	{
		return newAggregatedResultBasic(ResultType.UNION);
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	@SuppressWarnings("boxing")
	private AggregatedResult newAggregatedResultBasic(final ResultType type)
	{
		final AggregatedResult result = new AggregatedResultTo(type);

		final CategoryTo ageCategory = new CategoryTo("age");
		ageCategory.addEntry("10-19", 2l);
		ageCategory.addEntry("20-29", 343434l);
		result.addCategory(ageCategory);

		final CategoryTo raceCategory = new CategoryTo("race");
		raceCategory.addEntry("White", 123l);
		raceCategory.addEntry("Black", 456l);
		result.addCategory(raceCategory);

		return result;
	}

	/**
	 * @param query
	 * @return
	 */
	private AggregatedResults newAggregatedResultsBasic()
	{
		final AggregatedResults results = new AggregatedResultsTo();
		results.setNumDataSources(2);
		results.addResult(newAggregatedResultBasic(ResultType.UNION));
		results.addResult(newAggregatedResultBasic(ResultType.INTERSECTION));
		results.addResult(newAggregatedResultBasic(ResultType.SUM));
		results.addResultView(ResultType.SUM, newResultContextTo(789l));
		results.addResultView(ResultType.UNION, newResultContextTo(1023l));
		return results;
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	@SuppressWarnings("boxing")
	private AggregatedResult newAggregatedResultWithOrderingImportance()
	{
		final AggregatedResult result = new AggregatedResultTo(ResultType.UNION);

		final CategoryTo ageCategory = new CategoryTo("age");
		ageCategory.addEntry("5-9", 2l);
		ageCategory.addEntry("45-49", 343434l);
		result.addCategory(ageCategory);

		return result;
	}
}
