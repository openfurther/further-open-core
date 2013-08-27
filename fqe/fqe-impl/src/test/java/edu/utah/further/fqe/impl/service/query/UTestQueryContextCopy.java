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

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.query.domain.MatchType;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;

/**
 * A unit test class of {@link QueryContext}'s copy constructors and
 * {@link QueryContext#copyFrom(QueryContext)}.
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
 * @version Jan 28, 2009
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestQueryContextCopy extends FqeImplUtestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestQueryContextCopy.class);

	// ========================= FIELDS ====================================

	/**
	 * A {@link QueryContext} to copy from.
	 */
	private QueryContext original;

	/**
	 * A {@link QueryContext} to copy into.
	 */
	private QueryContext copy;

	// ========================= SETUP METHODS =============================

	/**
	 * Set up {@link QueryContext} to copy from.
	 */
	@Before
	public void setupOriginal()
	{
		original = newEntity();
	}

	/**
	 * Common assertions at the end of all tests.
	 */
	@After
	public void assertionsAfterCopying()
	{
		assertStateEquals(original, copy);
		original = null;
		copy = null;
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Test copying a {@link QueryContext} into a {@link QueryContextEntity} using the
	 * latter's copy constructor.
	 */
	@Test
	public void entityCopyConstructor()
	{
		copy = original.copy();
	}

	/**
	 * Test copying a {@link QueryContext} into a {@link QueryContextEntity} using the
	 * {@link QueryContext#copyFrom(QueryContext)} method.
	 */
	@Test
	public void entityCopyUsingCopyMethod()
	{
		copy = new QueryContextEntity();
		copy.copyFrom(original);
	}

	/**
	 * Test copying a {@link QueryContext} into a {@link QueryContextToImpl} using the
	 * latter's copy constructor.
	 */
	@Test
	public void toCopyConstructor()
	{
		copy = original.copy();
	}

	/**
	 * Test copying a {@link QueryContext} into a {@link QueryContextToImpl} using the
	 * {@link QueryContext#copyFrom(QueryContext)} method.
	 */
	@Test
	public void toCopyUsingCopyMethod()
	{
		copy = QueryContextToImpl.newInstanceWithExecutionId();
		copy.copyFrom(original);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private static QueryContext newEntity()
	{
		final QueryContext entity = new QueryContextEntity();
		entity.setNumRecords(50L);
		final SearchQuery searchQuery = SearchCriteria.query(SearchCriteria
				.stringExpression(SearchType.LIKE, "propertyName", "value",
						MatchType.CONTAINS), "Person");
		entity.setQuery(searchQuery);
		// entity
		// .setQueryXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><query xmlns=\"http://further.utah.edu/core/query\"/>");
		// Change state to make the assertion that follows copying non-trivial
		entity.queue();
		return entity;
	}

	/**
	 * @param original
	 * @param copy
	 */
	private static void assertStateEquals(final QueryContext original,
			final QueryContext copy)
	{
		assertEquals("Wrong state after copying", original.getState(), copy.getState());
	}
}
