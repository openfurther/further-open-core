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

import edu.utah.further.core.qunit.runner.AbstractQTestRunner;
import edu.utah.further.core.qunit.runner.OutputFormatterSimpleImpl;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestResult;

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
// @Service("qTestRunnerSimple")
public final class RunnerSimple extends AbstractQTestRunner
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initializes all internal fields for XML test processing.
	 */
	public RunnerSimple()
	{
		super(new OutputFormatterSimpleImpl());
	}

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPL: AbstractXmlTestRunner ================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.qunit.runner.AbstractQTestRunner#runInternal(edu.utah.further
	 * .core.qunit.runner.QTestData, edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@Override
	protected QTestResult runInternal(final QTestData testData, final QTestContext context)
			throws Throwable
	{
		// Unmarshal input XML to a logical query
		final Integer inputInt = new Integer(testData.getInput());
		final Integer expectedInt = new Integer(testData.getExpected());

		final boolean result = (inputInt.compareTo(expectedInt) < 0);
		return new QTestResult(result, testData.getInput());
	}
}
