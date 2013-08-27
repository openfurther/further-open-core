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

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

/**
 * An XQuery program exact match assertion. Both the XQuery output and the expected output
 * resource are converted to strings and compared
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
final class XmlAssertionExactMatch extends XmlAssertion
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmlAssertionExactMatch.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor - call {@link Builder#build()} instead.
	 * </p>
	 */
	protected XmlAssertionExactMatch()
	{
		// Can only be invoked by the builder.
		super();
	}

	// ========================= METHODS ===================================

	/**
	 * Print actual vs. expected. Useful for testing.
	 *
	 * @return assertion result
	 */
	@Override
	public void printToCompare()
	{
		if (log.isDebugEnabled())
		{
			final String actualFiltered = getActual().getFilteredString(
					isStripNewLinesAndTabs());
			final String expectedFiltered = getExpected().getFilteredString(
					isStripNewLinesAndTabs());
			log.debug("Actual   : " + actualFiltered);
			log.debug("Expected : " + expectedFiltered);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.qunit.runner.XmlAssertion#doAssert()
	 */
	@Override
	public boolean doAssert()
	{
		final String actualFiltered = getActual().getFilteredString(
				isStripNewLinesAndTabs());
		final String expectedFiltered = getExpected().getFilteredString(
				isStripNewLinesAndTabs());

		if (isDie())
		{
			assertEquals("XML output (as string) does not exactly match expectations",
					expectedFiltered, actualFiltered);
			return false;
		}
		return expectedFiltered.equals(actualFiltered);
	}

	// ========================= BUILDER ===================================

	// XQueryAssertion.Builder may be extended if this sub-class adds properties

	// ========================= PRIVATE METHODS ===========================
}
