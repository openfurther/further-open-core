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

import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;

/**
 * XQuery test assertions and utilities.
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
 * @version May 6, 2010
 */
public final class XmlAssertUtil
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	// ========================= METHODS ===================================

	/**
	 * Assert that two XML resources are identical.
	 *
	 * @param expectedResourceName
	 *            XML resource #1
	 * @param actualResourceName
	 *            XML resource #2
	 * @param ignoredElements
	 *            local names of XML elements to be excluded from the comparison
	 */
	public static void assertStreamEquals(final String expectedResourceName,
			final String actualResourceName, final String... ignoredElements)
	{
		xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
				.actualResourceName(actualResourceName)
				.expectedResourceName(expectedResourceName)
				.ignoredElements(ignoredElements)
				.doAssert();
	}

}
