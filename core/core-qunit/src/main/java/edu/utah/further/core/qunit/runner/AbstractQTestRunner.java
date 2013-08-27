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

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;

/**
 * A base class of XML test runners. Uses a template method to run tests.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 31, 2010
 */
public abstract class AbstractQTestRunner implements QTestRunner
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmlAssertUtil.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Formats test outputs.
	 */
	private final OutputFormatter outputFormatter;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param outputFormatter
	 */
	protected AbstractQTestRunner(final OutputFormatter outputFormatter)
	{
		super();
		this.outputFormatter = outputFormatter;
	}

	// ========================= IMPL: QTestRunner =========================

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to StAX streams and converted. (non-Javadoc)
	 *
	 * @see edu.utah.further.core.qunit.runner.QTestRunner#run(edu.utah.further.core.qunit.runner.QTestData,
	 *      edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@Override
	public final QTestResult run(final QTestData testData, final QTestContext context)
			throws Throwable
	{
		QTestResult result = new QTestResult();
		try
		{
			result = runInternal(testData, context);
		}
		catch (final Throwable thrownDuringTest)
		{
			result.setThrowable(thrownDuringTest);
		}
		formatOutputs(testData, context, result);
		runAssertions(testData, context, result);
		return result;
	}

	// ========================= PRIVATE METHODS ===================================

	/**
	 * A sub-class hook that performs the test logic and returns the results as an
	 * {@link QTestResult} struct.
	 *
	 * @param testData
	 *            test data
	 * @param context
	 *            test context
	 * @return test results
	 * @throws Throwable
	 *             an exception thrown during the test (indicates failure)
	 */
	abstract protected QTestResult runInternal(QTestData testData,
			final QTestContext context) throws Throwable;

	/**
	 * Run final XML test case assertions.
	 * <p>
	 * Assumes that <code>testData.input</code> is the actual response string, not a
	 * reference to a classpath resource.
	 *
	 * @param testData
	 *            test case data
	 * @param context
	 *            test context
	 * @param result
	 *            test results (set by the {@link QTestRunner} calling this method)
	 */
	private void formatOutputs(final QTestData testData, final QTestContext context,
			final QTestResult result)
	{
		if (result.getActualResourceString() != null)
		{
			result.setFormattedExpectedOutput(outputFormatter.formatExpectedOutput(
					testData, context));
			result.setFormattedActualOutput(outputFormatter.formatActualOutput(result,
					context));

			if (log.isDebugEnabled())
			{
				final String s = "Printing outputs to be compared" + NEW_LINE_STRING
						+ NEW_LINE_STRING + "----- EXPECTED -----" + NEW_LINE_STRING
						+ result.getFormattedExpectedOutput() + NEW_LINE_STRING
						+ NEW_LINE_STRING + "----- ACTUAL -----" + NEW_LINE_STRING
						+ result.getFormattedActualOutput();
				log.debug(s);
			}
		}
	}

	/**
	 * @param testData
	 * @param context
	 * @param result
	 */
	private static void runAssertions(final QTestData testData,
			final QTestContext context, final QTestResult result)
	{
		final Throwable throwable = result.getThrowable();
		final String expectedOutput = result.getFormattedExpectedOutput();
		final String actualOutput = result.getFormattedActualOutput();
		try
		{
			switch (testData.getSpecialAction())
			{
				case EXPECT_FAILURE:
				{
					if (throwable == null)
					{
						throw new AssertionError(getErrorMessagePrefix(testData, context)
								+ "Expected exception but didn't encounter any for test "
								+ testData);
					}
					else if (result.isComarisonResult())
					{
						throw new AssertionError(getErrorMessagePrefix(testData, context)
								+ "Expected failure but test passed " + testData);
					}
					break;
				}

				case ALWAYS_FAIL:
				{
					// Pad actual output with some invisible space to induce failure
					// without compromising the diff editor view
					assertEquals(getErrorMessagePrefix(testData, context)
							+ "XML test was induced to always fail", expectedOutput,
							actualOutput + Strings.EMPTY_STRING);
					break;
				}

				default:
				{
					// Test threw exception, die
					if (throwable != null)
					{
						// Exception already saved
						break;
					}
					else if (result.getActualResourceString() == null)
					{
						throw new AssertionError(
								getErrorMessagePrefix(testData, context)
										+ "No exception occurred during test, but actual output is null, so we can't run XML comparison");
					}

					// Test went through, die in accordance with the comparison result
					if (result.isComarisonResult())
					{
						result.setStatus(QTestStatus.OK);
					}
					else
					{
						assertEquals(getErrorMessagePrefix(testData, context)
								+ "XML output does not match expectations",
								expectedOutput, actualOutput);
					}
				}
			}
		}
		// catch (final AssertionError error)
		// {
		// // Save assertion errors and propagate them to callers such as test fixtures
		// result.assertionError = error;
		// }
		catch (final Throwable t)
		{
			// Save exceptions and propagate them to callers such as test fixtures
			result.setThrowable(t);
		}
	}

	/**
	 * @param context
	 * @return
	 */
	private static String getErrorMessagePrefix(final QTestData testData,
			final QTestContext context)
	{
		return context.getTestInfoMessage() + " " + testData + ": ";
	}
}
