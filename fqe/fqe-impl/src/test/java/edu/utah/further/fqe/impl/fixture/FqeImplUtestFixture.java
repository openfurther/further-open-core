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
package edu.utah.further.fqe.impl.fixture;

import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertQueryContextFields;
import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertQueryContextStateEquals;
import static edu.utah.further.fqe.ds.test.QueryContextTestUtil.assertTransientContext;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.QueryType;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;

/**
 * Unit test fixture.
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/fqe-impl-utest-annotation-context.xml", "/fqe-impl-utest-datasource-context.xml",
		"/fqe-impl-utest-services-context.xml" })
public abstract class FqeImplUtestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * Fictitious number of records to set on a {@link QueryContext} test object.
	 */
	protected static final long NUM_RECORDS = 50L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(FqeImplUtestFixture.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles {@link QueryContext} CRUD operations.
	 */
	@Autowired
	protected QueryContextService queryContextService;

	// ========================= SETUP METHODS =============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	public static QueryContext newQueryContextEntity()
	{
		final QueryContext entity = new QueryContextEntity();
		entity.setQueryType(QueryType.DATA_QUERY);
		// entity.setStaleDateTime(staleDateTime);
		entity.setNumRecords(NUM_RECORDS);
		entity
				.setQuery(SearchCriteria
						.queryBuilder(
								SearchCriteria.simpleExpression(Relation.EQ, "property",
										"value")).build());

		assertTransientContext(entity);
		return entity;
	}

	/**
	 * @param expected
	 */
	protected final void assertNumberQueryContextsInDatabase(final int expected)
	{
		final List<QueryContext> list = queryContextService.findAll();
		if (log.isDebugEnabled())
		{
			log.debug("Entities in database: " + list);
		}
		assertEquals("Wrong number of query contexts in the database", expected,
				list.size());
	}

	/**
	 * @param entity
	 */
	protected final void assertSingleEntityInDatabase(final QueryContext entity)
	{
		FqeDsQueryContextUtil.print(entity);
		assertNotNull(entity.getId());
		assertQueryContextStateEquals(entity, QueryState.QUEUED);
		assertQueryContextFields(entity);
		assertNumberQueryContextsInDatabase(1);
	}

}
