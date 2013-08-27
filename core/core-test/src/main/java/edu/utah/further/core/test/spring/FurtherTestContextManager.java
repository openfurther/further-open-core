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

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;

/**
 * Required to be overridden for the FURTHeR {@link TestContext} customization to work.
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
 * @version Oct 21, 2009
 */
public final class FurtherTestContextManager extends TestContextManager
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param testClass
	 */
	public FurtherTestContextManager(final Class<?> testClass)
	{
		super(testClass);
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the {@link TestContext} managed by this <code>TestContextManager</code>.
	 * Normally the test context is not exposed by the JUnit context manager so we had to
	 * add this method.
	 * 
	 * @return the {@link TestContext} managed by this <code>TestContextManager</code>
	 */
	TestContext getInternalTestContext()
	{
		return getTestContext();
	}

}
