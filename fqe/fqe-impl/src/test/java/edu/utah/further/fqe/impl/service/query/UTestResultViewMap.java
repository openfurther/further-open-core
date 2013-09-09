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

import static edu.utah.further.fqe.ds.api.service.results.ResultType.SUM;
import static edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil.NUM_RESULTS_IN_VIEW;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;
import edu.utah.further.fqe.impl.domain.ResultContextKeyEntity;

/**
 * A unit test of the query context result view map.
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
 * @version Oct 28, 2010
 */
@UnitTest
public final class UTestResultViewMap
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestResultViewMap.class);

	/**
	 * Test map.
	 */
	private final Map<ResultContextKey, ResultContext> resultViews = CollectionUtil
			.newMap();

	/**
	 * Test QC entity.
	 */
	private QueryContext queryContextEntity;

	/**
	 * Test QC TO.
	 */
	private QueryContext queryContextTo;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 * Set up test objects.
	 */
	@Before
	public void setupTestObjects()
	{
		resultViews.clear();
		queryContextEntity = new QueryContextEntity();
		queryContextTo = new QueryContextToImpl();
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * A unit test of adding a result view to the map and retrieving it.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void addResultViewDirectlyToMap()
	{
		final ResultContext resultContext = FqeDsQueryContextUtil.newResultContextTo();
		final ResultContextKeyEntity key = new ResultContextKeyEntity(SUM, null);
		resultViews.put(key, resultContext);

		final ResultContext reloadedRc = resultViews.get(key);
		assertNotNull("Result view was not saved", reloadedRc);
		assertThat(reloadedRc.getNumRecords(), is(NUM_RESULTS_IN_VIEW));
	}

	/**
	 * A unit test of adding a result view to the map and retrieving it via
	 * {@link QueryContextEntity} methods.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void addViewToQcEntity()
	{
		addRetrieveAndRetrieveResultView(SUM, 1, queryContextEntity);
	}

	/**
	 * A unit test of adding a result view to the map and retrieving it via
	 * {@link QueryContextEntity} methods.
	 */
	@Test
	public void addViewToQcEntityNullIndex()
	{
		addRetrieveAndRetrieveResultView(SUM, null, queryContextEntity);
	}


	/**
	 * A unit test of adding a result view to the map and retrieving it via
	 * {@link QueryContextToImpl} methods.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void addViewToQcTo()
	{
		addRetrieveAndRetrieveResultView(SUM, 1, queryContextTo);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param queryContext
	 */
	@SuppressWarnings("boxing")
	private static void addRetrieveAndRetrieveResultView(final ResultType type,
			final Integer intersectionIndex, final QueryContext queryContext)
	{
		final ResultContext resultContext = FqeDsQueryContextUtil.newResultContextTo();
		queryContext.addResultView(type, intersectionIndex, resultContext);

		final Map<ResultContextKey, ResultContext> qcViews = queryContext.getResultViews();
		assertThat(qcViews.size(), is (1));

		final ResultContext rc = queryContext.getResultView(type, intersectionIndex);
		assertNotNull("Result view was not saved", rc);
		assertThat(rc.getNumRecords(), is(NUM_RESULTS_IN_VIEW));
	}
}
