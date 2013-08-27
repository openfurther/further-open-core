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
package edu.utah.further.fqe.impl.service.query;

import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertQueryContextStateEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.fqe.ds.api.domain.QueryAction;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;

/**
 * A unit test class of all {@link QueryContext} {@link QueryState}s and transitional
 * actions.
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
 * @version Sep 21, 2009
 */
@UnitTest
public final class UTestQueryContextStateExecuting extends QueryContextStateFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestQueryContextStateExecuting.class);

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 * Get to {@link QueryState#EXECUTING} state.
	 */
	@Before
	public void getToDesiredState()
	{
		qc.queue();
		qc.start();
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * A unit test of the initial state of a {@link QueryContext} in this test.
	 */
	@Test
	public void initialState()
	{
		assertQueryContextStateEquals(qc, QueryState.EXECUTING);
	}

	/**
	 * A unit test of obtaining {@link QueryState#EXECUTING}'s permissible transitional
	 * actions.
	 */
	@Test
	public void actionSet()
	{
		assertActionSetEquals(qc, QueryAction.STOP, QueryAction.FINISH, QueryAction.FAIL);
	}

	/**
	 * A unit test of {@link QueryState#EXECUTING} and its transitional actions.
	 */
	@Test
	public void actionStop()
	{
		qc.stop();
		assertQueryContextStateEquals(qc, QueryState.STOPPED);
	}

	/**
	 * A unit test of {@link QueryState#EXECUTING} and its transitional actions.
	 */
	@Test
	public void actionFinish()
	{
		qc.finish();
		assertQueryContextStateEquals(qc, QueryState.COMPLETED);
	}

	/**
	 * A unit test of {@link QueryState#EXECUTING} and its transitional actions.
	 */
	@Test
	public void actionFail()
	{
		qc.fail();
		assertQueryContextStateEquals(qc, QueryState.FAILED);
	}

	// ========================= PRIVATE METHODS ===========================
}
