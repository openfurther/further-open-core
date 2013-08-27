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

import static edu.utah.further.core.api.collections.CollectionUtil.toSortedSet;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.util.math.random.RandomUtilService;
import edu.utah.further.fqe.ds.api.domain.QueryAction;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;

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
public abstract class QueryContextStateFixture extends FqeImplUtestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextStateFixture.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Random number source.
	 */
	@Autowired
	private RandomUtilService randomUtilService;

	// ========================= FIELDS ====================================

	/**
	 * A query context to play with.
	 */
	protected QueryContext qc;

	// ========================= SETUP METHODS =============================

	/**
	 * Set up an initial {@link QueryContext}.
	 */
	@Before
	public void setup()
	{
		qc = QueryContextToImpl.newInstanceWithExecutionId();
	}

	/**
	 * Tear down {@link QueryContext}.
	 */
	@After
	public void tearDown()
	{
		qc = null;
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * A random test of {@link QueryState#SUBMITTED} unsupported transitional actions.
	 */
	@Test(expected = BusinessRuleException.class)
	public void unsupportedAction()
	{
		testRandomUnsupportedAction();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A random test of the current <code>qc</code>'s state unsupported transitional
	 * actions.
	 */
	protected final void testRandomUnsupportedAction()
	{
		final List<QueryAction> unsupportedActions = CollectionUtil
				.<QueryAction> toList(QueryAction.values());
		unsupportedActions.removeAll(qc.getActions());
		final QueryAction randomUnsupportedAction = randomUtilService
				.randomInList(unsupportedActions);
		if (log.isDebugEnabled())
		{
			log.debug("State " + qc.getState() + " , chosen unsupported action: "
					+ randomUnsupportedAction);
		}
		randomUnsupportedAction.execute(qc);
	}

	/**
	 * @param qc
	 * @param action
	 */
	protected static final void assertActionSetEquals(final QueryContext qc,
			final QueryAction... action)
	{
		assertEquals("Wrong permissible action set", toSortedSet(action), qc.getActions());
	}
}
