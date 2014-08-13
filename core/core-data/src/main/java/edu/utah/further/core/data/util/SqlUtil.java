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
package edu.utah.further.core.data.util;

import static edu.utah.further.core.api.constant.Constants.MAX_IN;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Sql Utilities
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2011 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 4, 2012
 */
@Utility
@Api
public final class SqlUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private SqlUtil()
	{
		preventUtilityConstruction();
	}

	/**
	 * Returns an a string that can be used for an unlimited number of values. This is
	 * used to circumvent limitations on the number of values that can be within an IN
	 * statement.
	 * 
	 * @param values
	 *            the values for use in the IN
	 * @return a string for use within an SQL statement
	 */
	public static final String unlimitedInValues(final List<?> values, final String propertyName)
	{
		final StringBuilder inString = StringUtil.newStringBuilder();

		// values -- in ( ?, ?, ?...)
		inString.append("( " + propertyName + " in ").append('(');
		for (int i = 0; i < values.size(); i++)
		{
			if ((i != 0) && (i % MAX_IN == 0))
			{
				inString.append(')');
				inString.append(" or " + propertyName + " in ");
				inString.append('(');
			}

			inString.append('?');

			if ((i + 1 <= (values.size() - 1)) && ((i + 1) % MAX_IN != 0))
			{
				inString.append(", ");
			}
		}
		inString.append(") )");

		return inString.toString();
	}

}
