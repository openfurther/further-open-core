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

import static edu.utah.further.core.api.collections.CollectionUtil.EMPTY_LONG_ARRAY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.ScrollableResults;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.data.domain.SimpleEntity;
import edu.utah.further.core.data.fixture.CoreDataFixture;
import edu.utah.further.core.util.collections.page.PagerUtil;

/**
 * A unit test of {@link ListPager}s - iterating over list chunks (pages) instead of on
 * individual elements.
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
 * @version Jul 27, 2010
 */
public abstract class ScrollableResultsPagerFixture extends CoreDataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ScrollableResultsPagerFixture.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void listSizeUnitPageSize()
	{
		final Collection<Long> uniqueEntityList = getTestListUniqueElements();
		final int size = uniqueEntityList.size();
		final Long[][] pages = new Long[size][];
		int i = 0;
		for (final Long id : uniqueEntityList)
		{
			pages[i++] = new Long[]
			{ id };
		}
		testPagingIterator(1, pages);
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void listSizeNotDivisibleByPageSize()
	{
		testPagingIterator(3, new Long[][]
		{
		{ 1l, 2l, 3l },
		{ 4l, 5l, 6l },
		{ 7l, 8l, 9l },
		{ 10l } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void listSizeNotDivisibleByPageSize2()
	{
		testPagingIterator(7, new Long[][]
		{
		{ 1l, 2l, 3l, 4l, 5l, 6l, 7l },
		{ 8l, 9l, 10l } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void listSizeDivisibleByPageSize()
	{
		testPagingIterator(2, new Long[][]
		{
		{ 1l, 2l },
		{ 3l, 4l },
		{ 5l, 6l },
		{ 7l, 8l },
		{ 9l, 10l } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>a</code>.
	 */
	@Test
	public void listSizeEqualsPageSize()
	{
		testPagingIterator(10, new Long[][]
		{ getTestListUniqueElements().toArray(EMPTY_LONG_ARRAY) });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b< &gt; a</code>.
	 */
	@Test
	public void listSizeGreaterThanPageSize()
	{
		final Set<Long> uniqueElements = getTestListUniqueElements();
		testPagingIterator(uniqueElements.size() + 1, new Long[][]
		{ uniqueElements.toArray(EMPTY_LONG_ARRAY) });
		testPagingIterator(100000, new Long[][]
		{ uniqueElements.toArray(EMPTY_LONG_ARRAY) });
	}

	/**
	 * Test iterating over a <code>null</code> list.
	 */
	@Test(expected = BusinessRuleException.class)
	public void nullArgumentNotAllowed()
	{
		getPagerFactory().pager(null, IterableType.LIST);
	}

	/**
	 * Test iterating over a list with an invalid page size.
	 */
	@Test(expected = BusinessRuleException.class)
	public void pageSizeMustBePositive1()
	{
		@SuppressWarnings("unused")
		final Iterator<List<? extends PersistentEntity<Long>>> iterator = newPagerFromTestList(0);
	}

	/**
	 * Test iterating over a list with an invalid page size.
	 */
	@Test(expected = BusinessRuleException.class)
	public void pageSizeMustBePositive2()
	{
		@SuppressWarnings("unused")
		final Iterator<List<? extends PersistentEntity<Long>>> iterator = newPagerFromTestList(-1);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the test entity ID list. A sub-class hook.
	 * 
	 * @return the testList
	 */
	abstract protected List<Long> getTestList();

	/**
	 * @return
	 */
	private Set<Long> getTestListUniqueElements()
	{
		return CollectionUtil.getUniqueElements(getTestList());
	}

	/**
	 * @param pageSize
	 * @param expectedPages
	 */
	private void testPagingIterator(final int pageSize, final Long[][] expectedPages)
	{
		final List<List<? extends PersistentEntity<Long>>> allPages = PagerUtil
				.getAllPages(newPagerFromTestList(pageSize));
		PagerUtil.printPages(allPages);
		assertPagesEqual(allPages, expectedPages);
	}

	/**
	 * @param pages
	 * @param i
	 * @param values
	 */
	@SuppressWarnings("boxing")
	private static void assertPagesEqual(
			final List<List<? extends PersistentEntity<Long>>> pages,
			final Long[][] values)
	{
		assertThat(pages.size(), is(values.length));
		for (int i = 0; i < pages.size(); i++)
		{
			final List<PersistentEntity<Long>> page = (List<PersistentEntity<Long>>) pages
					.get(i);
			final List<Long> pageIds = CollectionUtil.toIdList(page);
			assertThat(pageIds.toArray(EMPTY_LONG_ARRAY), is(values[i]));
		}
	}

	/**
	 * A pager factory method hook that creates a pager from the test list.
	 * 
	 * @param pageSize
	 *            page size
	 * @return pager instance of the specific implementation being tested in a sub-class
	 *         of this class
	 */
	private Iterator<List<? extends PersistentEntity<Long>>> newPagerFromTestList(
			final int pageSize)
	{
		// Convert list of IDs to a list of entities to pass to the pager
		return newPager(pageSize, toScrollableResults(getTestList()));
	}

	/**
	 * Convert list of IDs to a list of entities to pass to a scrollable results pager.
	 * 
	 * @param idList
	 * @param entityList
	 * @return scrollable results
	 */
	private static ScrollableResults toScrollableResults(final List<Long> idList)
	{
		final List<PersistentEntity<?>> entityList = CollectionUtil.newList();
		for (final Long id : idList)
		{
			final SimpleEntity entity = new SimpleEntity();
			entity.setId(id);
			entityList.add(entity);
		}
		final ScrollableResults resultSet = new ScrollableResultsMockImpl(entityList);
		return resultSet;
	}
}