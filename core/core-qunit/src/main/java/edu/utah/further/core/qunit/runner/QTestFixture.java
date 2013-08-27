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
package edu.utah.further.core.qunit.runner;

import static edu.utah.further.core.api.text.StringUtil.pluralForm;
import static edu.utah.further.core.qunit.runner.SpecialAction.EXPECT_FAILURE;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.text.StringUtil;

/**
 * A test fixture that loads an XML test suite from a Spring context file and runs it.
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
 * @version May 13, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class QTestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QTestFixture.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * For printing method names.
	 */
	@Rule
	public TestName testName = new TestName();

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= TEST METHODS ==============================

	/**
	 * Run all XML tests in the test suite loaded from Spring context files.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testSuite() throws Throwable
	{
		final QTestSuite xmlTestSuite = getQTestSuite();
		final QTestContext context = xmlTestSuite.getContext();
		final int numTests = xmlTestSuite.getSize();
		context.setNumTests(numTests);
		if (log.isInfoEnabled())
		{
			log.info("Running test suite " + getClass().getSimpleName() + ": " + numTests
					+ " " + pluralForm("test", numTests));
		}

		int testCount = 0;
		for (final QTestData testData : xmlTestSuite)
		{
			testCount++;
			context.setCurrentTestNumber(testCount);
			final SpecialAction specialAction = testData.getSpecialAction();
			if (log.isInfoEnabled())
			{
				final String actionTitle = getActionTitle(specialAction);
				log.info(String.format("%-8s test %2d/%2d: %s", actionTitle, new Integer(
						testCount), new Integer(numTests), testData));
			}
			if (specialAction != SpecialAction.IGNORE)
			{
				printTestMethodName();
				final QTestResult result = getQTestRunner().run(testData, context);
				if (log.isInfoEnabled())
				{
					log.info(context.getTestInfoMessage() + " " + result.getStatus());
				}
				final Throwable thrownDuringTest = result.getThrowable();
				if (thrownDuringTest != null && specialAction != EXPECT_FAILURE)
				{
					throw thrownDuringTest;
				}
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the XML test suite to run. A sub-class hook.
	 * 
	 * @return the XML test to run
	 */
	abstract protected QTestSuite getQTestSuite();

	/**
	 * Return the function pointer containing the business logic of running XML tests in
	 * the test suite. A sub-class hook.
	 * 
	 * @return the XML test to run
	 */
	abstract protected QTestRunner getQTestRunner();

	/**
	 * It would be nicer to do it in a {@link Before} method, but {@link #testName} is not
	 * yet populated then.
	 */
	private void printTestMethodName()
	{
		if (log.isDebugEnabled())
		{
			log.debug(StringUtil.repeat("=", 70));
			log.debug(testName.getMethodName());
			log.debug(StringUtil.repeat("=", 70));
		}
	}

	/**
	 * @param specialAction
	 * @return
	 */
	private static String getActionTitle(final SpecialAction specialAction)
	{
		switch (specialAction)
		{
			case IGNORE:
			{
				return "Ignoring";
			}
			case EXPECT_FAILURE:
			{
				return "Expecting failure";
			}
			default:
			{
				return "Running";
			}
		}
	}
}
