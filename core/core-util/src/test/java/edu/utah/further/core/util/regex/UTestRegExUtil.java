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
package edu.utah.further.core.util.regex;

import static edu.utah.further.core.test.util.AssertUtil.assertSizeEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.regex.Matcher;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.regex.RegExUtil;

/**
 * Unit tests for {@link RegExUtil}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 26, 2009
 */
public class UTestRegExUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestRegExUtil.class);

	// ========================= TESTING METHODS ===========================

	/**
	 * Test capturing regular expression groups.
	 */
	@Test
	public final void captureGroups()
	{
		final String input = "1 and 2";
		final String regex = "(\\d) and (\\d)";
		final List<String> captured = RegExUtil.capture(input, regex);
		assertThat(captured, notNullValue());
		assertSizeEquals(captured, 2);
		if (log.isDebugEnabled())
		{
			for (int i = 0; i < captured.size(); i++)
			{
				log.debug(captured.get(i));
			}
		}
		assertThat(captured.get(0), is("1"));
		assertThat(captured.get(1), is("2"));
	}

	/**
	 * Test replacing backslashes in a string using a regular expression.
	 */
	@Test
	public final void replaceBackSlashByDollar()
	{
		testBackSlashReplacement("\\\\FURTHeR\\a\\b\\c", "\\$", "$$FURTHeR$a$b$c");
	}

	/**
	 * Test replacing backslashes in a string using a regular expression.
	 */
	@Test
	public final void replaceBackSlashByDoubleForwardSlash()
	{
		testBackSlashReplacement("\\\\FURTHeR\\a\\b\\c", "//", "////FURTHeR//a//b//c");
	}

	/**
	 * Test replacing backslashes in a string using a regular expression. Note that we
	 * need to use {@link Matcher#quoteReplacement(String)} to avoid problems.
	 *
	 * @see String#replaceAll(String, String)
	 */
	@Test
	public final void replaceSlashByDoubleBackSlash()
	{
		testBackSlashQuotedReplacement("\\\\FURTHeR\\a\\b\\c", "\\\\", "\\\\\\\\FURTHeR\\\\a\\\\b\\\\c");
	}

	// ========================= PRIAVET METHPDS ===========================

	/**
	 * @param original
	 * @param regex
	 * @param expected
	 */
	private void testBackSlashReplacement(final String original,
			final String replacement, final String expected)
	{
		final String actual = original.replaceAll("\\\\", replacement);
		assertReplacementResult(original, expected, actual);
	}

	/**
	 * @param original
	 * @param regex
	 * @param expected
	 */
	private void testBackSlashQuotedReplacement(final String original,
			final String replacement, final String expected)
	{
		final String actual = original.replaceAll("\\\\",
				Matcher.quoteReplacement(replacement));
		assertReplacementResult(original, expected, actual);
	}

	/**
	 * @param original
	 * @param expected
	 * @param actual
	 */
	private void assertReplacementResult(final String original, final String expected,
			final String actual)
	{
		if (log.isDebugEnabled())
		{
			log.debug("original = " + original);
			log.debug("expected = " + expected);
			log.debug("actual   = " + actual);
		}
		assertEquals("Unexpected slash regex replacement result", expected, actual);
	}
}
