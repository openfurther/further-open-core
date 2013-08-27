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
package edu.utah.further.core.test.spring;

import static org.junit.Assert.assertNotNull;

import org.junit.experimental.categories.Category;
import org.springframework.stereotype.Service;

/**
 * Consumes JUnit {@link Category} annotations and decides which tests to run.
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
 * @version Jul 30, 2010
 */
@Service("categorySuiteExecutionListener")
public class CategorySuiteExecutionListener extends AbstractSuiteExecutionListener
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPLEMENTATION: SuiteExecutionListener ====

	/**
	 * @param suiteContext
	 * @throws Exception
	 * @see edu.utah.further.core.api.spring.AbstractSuiteExecutionListener#beforeClass(edu.utah.further.core.test.spring.TSuiteContext)
	 */
	@Override
	public void beforeClass(final TSuiteContext suiteContext) throws Exception
	{
		assertNotNull("Test context manager not set in suite context",
				suiteContext.getFurtherTestContextManager());
	}

	/**
	 * @param suiteContext
	 * @throws Exception
	 * @see edu.utah.further.core.api.spring.AbstractSuiteExecutionListener#afterClass(edu.utah.further.core.test.spring.TSuiteContext)
	 */
	@Override
	public void afterClass(final TSuiteContext suiteContext) throws Exception
	{
		// A hook
	}
}
