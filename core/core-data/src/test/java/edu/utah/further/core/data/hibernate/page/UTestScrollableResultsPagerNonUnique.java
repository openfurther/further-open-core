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
package edu.utah.further.core.data.hibernate.page;

import java.util.Arrays;
import java.util.List;

/**
 * A unit test of {@link ScrollableResultsPager} with an entity list whose elements
 * are repeated.
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
 * @version Sep 26, 2010
 */
public final class UTestScrollableResultsPagerNonUnique extends ScrollableResultsPagerFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A test list of entity IDs.
	 */
	@SuppressWarnings("boxing")
	protected static final List<Long> TEST_LIST = Arrays.asList(new Long[]
	{ 1l, 2l, 3l, 3l, 3l, 4l, 5l, 5l, 5l, 5l, 6l, 7l, 8l, 9l, 10l });

	// ========================= IMPL: ScrollableResultsPagerFixture =======

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.data.hibernate.page.ScrollableResultsPagerFixture#getTestList
	 * ()
	 */
	@Override
	protected List<Long> getTestList()
	{
		return TEST_LIST;
	}
}