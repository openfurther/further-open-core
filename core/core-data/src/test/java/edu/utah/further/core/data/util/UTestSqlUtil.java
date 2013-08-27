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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Tests related to the Sql Utility class.
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
public final class UTestSqlUtil
{
	@Test
	public void unlimitedInValues()
	{
		final List<Integer> list = CollectionUtil.newList();
		for (int i = 0; i < MAX_IN + 1; i++)
		{
			list.add(new Integer(i));
		}
		final String inStatement = SqlUtil.unlimitedInValues(list, "DEAD_BEEF");
		assertThat(new Integer(StringUtils.countMatches(inStatement, "or")),
				is(new Integer(1)));
		assertThat(new Integer(StringUtils.countMatches(inStatement, "?")),
				is(new Integer(MAX_IN + 1)));

	}
}
