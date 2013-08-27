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
package edu.utah.further.core.qunit.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestSuite;
import edu.utah.further.core.qunit.runner.SpecialAction;

/**
 * Test our custom Spring schema "dst".
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-qunit-test-schema-context.xml" })
public final class UTestSchemaQunit
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestSchemaQunit.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Spring application context.
	 */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/**
	 * Spring application context.
	 */
	@Resource(name = "testSuite")
	private QTestSuite testSuite;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * Test constructing an XQuery test suite from the corresponding Spring XML element.
	 *
	 * @throws Exception
	 */
	@Test
	public void loadXmlTestSuite() throws Exception
	{
		assertNotNull("XML test suite bean was not created by Spring", testSuite);

		final QTestContext context = testSuite.getContext();
		assertEquals(new Long(1234L), context.getTargetNamespaceId());
		assertEquals("5678", context.getServiceUserId());
		assertEquals("input/", context.getInputClassPath());
		assertEquals("expected/", context.getExpectedClassPath());

		assertEquals(testSuite.getSize(), 2);
		assertXqueryTesterEquals(testSuite.get(0), "program1", "in1", "out1",
				SpecialAction.ALWAYS_FAIL, CollectionUtil.<String, String> newMap(),
				"ignored11", "ignored12");
		assertXqueryTesterEquals(testSuite.get(1), "program2", "in2", "out2",
				SpecialAction.NONE, getParameters(), "ignored21", "ignored22");
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param xqueryTester
	 * @param programKey
	 * @param inputKey
	 * @param expectedKey
	 * @param ignoredElementsKeys
	 */
	private void assertXqueryTesterEquals(final QTestData xqueryTester,
			final String programKey, final String inputKey, final String expectedKey,
			final SpecialAction specialAction,
			final Map<String, String> expectedParameters,
			final String... ignoredElementsKeys)
	{
		assertFieldValueEquals("transformer", programKey, xqueryTester.getTransformer());
		assertFieldValueEquals("input", inputKey, xqueryTester.getInput());
		assertFieldValueEquals("expected", expectedKey, xqueryTester.getExpected());
		assertFieldValueEquals("specialAction", specialAction,
				xqueryTester.getSpecialAction());

		assertEquals("parameters were not correctly set by Spring", expectedParameters,
				xqueryTester.getParameters());

		final Set<String> expectedElements = CollectionUtil.newSet(CoreUtil
				.getPropertyValues(contextProperties, ignoredElementsKeys));
		assertEquals(
				"XQuery tester ignored elements field was not properly set by Spring",
				expectedElements, xqueryTester.getIgnoredElements());
	}

	/**
	 * @param fieldName
	 * @param key
	 * @param actualValue
	 */
	private <T> void assertFieldValueEquals(final String fieldName,
			final T expectedValue, final T actualValue)
	{
		assertNotNull("XQuery tester " + fieldName + " field was not set by Spring",
				actualValue);
		assertEquals("XQuery tester " + fieldName
				+ " field value was not correctly set by Spring", expectedValue,
				actualValue);
	}

	/**
	 * @param fieldName
	 * @param key
	 * @param actualValue
	 */
	private void assertFieldValueEquals(final String fieldName, final String key,
			final String actualValue)
	{
		assertNotNull("XQuery tester " + fieldName + " field was not set by Spring",
				actualValue);
		assertEquals("XQuery tester " + fieldName
				+ " field value was not correctly set by Spring",
				contextProperties.getProperty(key), actualValue);
	}

	/**
	 * @return
	 */
	private Map<String, String> getParameters()
	{
		final Map<String, String> parameters = CollectionUtil.<String, String> newMap();
		parameters.put("name1", "value1");
		parameters.put("name2", "value2");
		return parameters;
	}
}
