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
package edu.utah.further.fqe.ds.test;

import static org.junit.Assert.assertEquals;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;

/**
 * A stateless test utility class related to {@link QueryContext}s.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@Utility
public final class QueryContextTestUtil
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	// ========================= METHODS ===================================

	/**
	 * @param queryContext
	 * @param expected
	 */
	public static void assertQueryContextStateEquals(final QueryContext queryContext,
			final QueryState expected)
	{
		assertEquals("Wrong query state", expected, queryContext.getState());
	}

	/**
	 * @param queryContext
	 */
	public static void assertQueryContextFields(final QueryContext queryContext)
	{
		assertEquals(FqeDsQueryContextUtil.NUM_RECORDS, queryContext.getNumRecords());
	}

	/**
	 * @param queryContext
	 */
	public static void assertTransientContext(final QueryContext queryContext)
	{
		FqeDsQueryContextUtil.print(queryContext);
		assertQueryContextStateEquals(queryContext, QueryState.SUBMITTED);
		assertQueryContextFields(queryContext);
	}

	/**
	 * @return
	 */
	public static QueryContextTo newQueryContextTo()
	{
		final QueryContextTo queryContext = FqeDsQueryContextUtil.newQueryContextToInstance();
		assertTransientContext(queryContext);
		return queryContext;
	}

	// ========================= PRIVATE METHODS ===========================
}
