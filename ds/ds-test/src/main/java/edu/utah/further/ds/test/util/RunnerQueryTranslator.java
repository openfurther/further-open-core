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
package edu.utah.further.ds.test.util;

import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.core.qunit.runner.AbstractQTestRunner;
import edu.utah.further.core.qunit.runner.OutputFormatterXmlImpl;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestResult;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.ds.api.service.query.logic.QueryTranslator;
import edu.utah.further.ds.fqe.test.util.FQEDataSourceTestUtil;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * A service that runs individual query translation test cases.
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
 * @version Aug 27, 2010
 */
// @Service("qTestRunnerQueryTranslator") // Manually deployed in a context file
public final class RunnerQueryTranslator extends AbstractQTestRunner
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The {@link QueryTranslator} service.
	 */
	@Autowired
	@Qualifier("impl")
	private QueryTranslator queryTranslator;

	/**
	 * XML Service for marshalling and unmarshalling.
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * 
	 * FQE Data Source testing service
	 */
	@Autowired
	private FQEDataSourceTestUtil fqeDataSourceTestUtil;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initializes all internal fields for XML test processing.
	 */
	public RunnerQueryTranslator()
	{
		super(new OutputFormatterXmlImpl());
	}

	// ========================= IMPL: AbstractXmlTestRunner ================

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to StAX streams and converted. (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.qunit.runner.AbstractQTestRunner#runInternal(edu.utah.further.core.qunit.runner.QTestData,
	 *      edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@Override
	protected QTestResult runInternal(final QTestData testData, final QTestContext context)
			throws Throwable
	{
		// Unmarshal input XML to a logical query
		final SearchQuery logicalQuery = xmlService.unmarshalResource(
				context.getInputClassPath() + testData.getInput(), SearchQueryTo.class);

		final QueryContextTo queryContext = QueryContextToImpl
				.newInstanceWithExecutionId();
		queryContext.setQuery(logicalQuery);
		queryContext.setTargetNamespaceId(context.getTargetNamespaceId());
		queryContext.setUserId(context.getServiceUserId());
		queryContext.setParent(fqeDataSourceTestUtil.queueNewParent(logicalQuery,
				context.getServiceUserId()));

		// Translate logical->physical query
		final SearchQuery physicalQuery = queryTranslator.translate(queryContext,
				new HashMap<String, Object>());
		assertThat(physicalQuery, notNullValue());

		// Assert that the translation result is the expected XML
		final String actualResourceString = xmlService.marshal(physicalQuery, xmlService
				.options()
				.addClass(SearchQueryTo.class)
				.buildContext());

		final boolean result = xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
				.actualResourceString(actualResourceString)
				.expectedResourceName(
						context.getExpectedClassPath() + testData.getExpected())
				.ignoredElements(testData.getIgnoredElements())
				.die(false)
				.doAssert();

		return new QTestResult(result, actualResourceString);
	}
}
