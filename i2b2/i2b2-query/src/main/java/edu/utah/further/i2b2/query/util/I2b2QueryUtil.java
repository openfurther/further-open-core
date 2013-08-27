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
package edu.utah.further.i2b2.query.util;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.regex.RegExUtil.capture;

import java.util.List;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.i2b2.query.model.I2b2Query;

/**
 * Static utility methods and strings related to the {@link I2b2Query}
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
 * @version Aug 24, 2009
 */
// Here the "final" keyword helps the compiler inline the static methods, making them
// faster.
public final class I2b2QueryUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * The value of the table_access c_fullname field for FURTHER
	 */
	public static String TABLE_ACCESS_C_FULLNAME = "^\\\\\\\\([A-Za-z0-9_])*";

	/**
	 * The value of the table_access c_fullname field for I2B2
	 */
	public static String I2B2_TABLE_ACCESS_C_FULLNAME = "\\\\I2B2";

	// ========================= METHODS ===================================

	/**
	 * A util method for removing formating such as tabs, new lines, extra whitespace, etc
	 *
	 * @param itemKey
	 * @return
	 */
	public static String clean(final String itemKey)
	{
		return itemKey
				.replaceFirst(TABLE_ACCESS_C_FULLNAME, Strings.EMPTY_STRING)
				.replace(Strings.NEW_LINE_STRING, Strings.EMPTY_STRING)
				// This replaces all occurrences of 1 or more tabs with a space
				.replaceAll("\t{1,}", " ")
				.trim();
	}

	/**
	 * Uses regular expressions to capture the two values used in a BETWEEN query
	 * 
	 * @param value
	 * @return
	 */
	public static List<Integer> parseBetweenValue(final String value)
	{
		final List<String> capList = capture(value, "(\\d) and (\\d)");
		final List<Integer> values = newList();
		for (final String cap : capList)
		{
			values.add(Integer.valueOf(cap));
		}
		return values;
	}
}
