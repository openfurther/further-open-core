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
package edu.utah.further.core.api.regex;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expression utility class.
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
public final class RegExUtil
{

	// ========================= CONSTRUCTORS ==============================
	/**
	 * Private constructor
	 */
	private RegExUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Returns a {@link List} of capturing groups within the the first
	 * regular-expression-matched region of an input string. This method removes group 0
	 * from the returned list, which always represents the entire expression.
	 *
	 * @param input
	 *            the input to operate on
	 * @param regex
	 *            the regular expression
	 * @return a list of capturing groups for the first matched region, or the empty list
	 *         if no matches are found
	 * @see http://java.sun.com/docs/books/tutorial/essential/regex/groups.html
	 * @see http://www.exampledepot.com/egs/java.util.regex/Group.html
	 */
	public static List<String> capture(final String input, final String regex)
	{
		final List<String> captureGroup = newList();
		if (input == null)
		{
			return captureGroup;
		}

		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(input);
		if (matcher.find() & matcher.groupCount() > 0)
		{
			// Starts at 1 to remove group 0
			for (int i = 1; i < matcher.groupCount() + 1; i++)
			{
				captureGroup.add(matcher.group(i));
			}
		}
		return captureGroup;
	}

	/**
	 * Test whether a string matches at least one of several regular expression patterns.
	 *
	 * @param s
	 *            string for which the patterns are matches
	 * @param patterns
	 *            regular expression pattern collection to test. String is tested against
	 *            each one in the iteration order of this collection.
	 * @return <code>true</code> if and only if s matches one of the patterns. If
	 *         <code>patterns = null</code>, this method returns <code>true</code>. If
	 *         <code>s = null</code>, this method returns <code>false</code>
	 */
	public static boolean matchesOneOf(final String s,
			final Collection<? extends Pattern> patterns)
	{
		if (s == null)
		{
			return false;
		}
		if (patterns == null)
		{
			return true;
		}
		for (final Pattern pattern : patterns)
		{
			if (pattern.matcher(s).matches())
			{
				return true;
			}
		}
		return false;
	}
}
