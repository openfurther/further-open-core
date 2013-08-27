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
package edu.utah.further.core.util.collections.page;

import static edu.utah.further.core.api.collections.CollectionUtil.EMPTY_INTEGER_ARRAY;
import static edu.utah.further.core.api.collections.page.PagingStrategy.NO_LIMIT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.fixture.CoreUtilFixture;

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
public abstract class PagerFixture extends CoreUtilFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A test list.
	 */
	@SuppressWarnings("boxing")
	protected static final List<Integer> TEST_LIST = Arrays.asList(new Integer[]
	{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates pager objects.
	 */
	@Autowired
	private PagerFactory pagerFactory;

	// ========================= TESTING METHODS ===========================

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void listSizeUnitPageSize()
	{
		final int size = TEST_LIST.size();
		final Integer[][] pages = new Integer[size][];
		for (int i = 0; i < size; i++)
		{
			pages[i] = new Integer[]
			{ TEST_LIST.get(i) };
		}
		testPagingIteratorNoLimit(1, pages);
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
		testPagingIteratorNoLimit(3, new Integer[][]
		{
		{ 1, 2, 3 },
		{ 4, 5, 6 },
		{ 7, 8, 9 },
		{ 10 } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void maxResultsIsSetSingleOutputPage()
	{
		testPagingIterator(3, 3, new Integer[][]
		{
		{ 1, 2, 3 }, });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void maxResultsIsSet()
	{
		testPagingIterator(3, 5, new Integer[][]
		{
		{ 1, 2, 3 },
		{ 4, 5 } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@Test
	public void maxResultsIsZero()
	{
		testPagingIterator(3, 0, new Integer[][] {});
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
		testPagingIteratorNoLimit(7, new Integer[][]
		{
		{ 1, 2, 3, 4, 5, 6, 7 },
		{ 8, 9, 10 } });
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
		testPagingIteratorNoLimit(2, new Integer[][]
		{
		{ 1, 2 },
		{ 3, 4 },
		{ 5, 6 },
		{ 7, 8 },
		{ 9, 10 } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>a</code>.
	 */
	@Test
	public void listSizeEqualsPageSize()
	{
		testPagingIteratorNoLimit(10, new Integer[][]
		{ TEST_LIST.toArray(EMPTY_INTEGER_ARRAY) });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b< &gt; a</code>.
	 */
	@Test
	public void listSizeGreaterThanPageSize()
	{
		testPagingIteratorNoLimit(11, new Integer[][]
		{ TEST_LIST.toArray(EMPTY_INTEGER_ARRAY) });
		testPagingIteratorNoLimit(100000, new Integer[][]
		{ TEST_LIST.toArray(EMPTY_INTEGER_ARRAY) });
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
		final Iterator<List<Integer>> iterator = newPager(0, NO_LIMIT);
	}

	/**
	 * Test iterating over a list with an invalid page size.
	 */
	@Test(expected = BusinessRuleException.class)
	public void pageSizeMustBePositive2()
	{
		@SuppressWarnings("unused")
		final Iterator<List<Integer>> iterator = newPager(-1, NO_LIMIT);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param pages
	 * @param i
	 * @param values
	 */
	@SuppressWarnings("boxing")
	static final <E> void assertPagesEqual(final List<List<E>> pages,
			final List<E[]> values, final E[] emptyArray)
	{
		assertThat(pages.size(), is(values.size()));
		for (int i = 0; i < pages.size(); i++)
		{
			assertThat(pages.get(i).toArray(emptyArray), is(values.get(i)));
		}
	}

	/**
	 * @param pageSize
	 * @param expectedPages
	 */
	private void testPagingIteratorNoLimit(final int pageSize,
			final Integer[][] expectedPages)
	{
		testPagingIterator(pageSize, NO_LIMIT, expectedPages);
	}

	/**
	 * @param pageSize
	 * @param maxResults
	 * @param expectedPages
	 */
	private void testPagingIterator(final int pageSize, final int maxResults,
			final Integer[][] expectedPages)
	{
		final int expectedNumPages = (int) Math.ceil((1.0d * maxResults) / pageSize);
		final List<Integer[]> pageList = Arrays.<Integer[]> asList(expectedPages);
		final List<Integer[]> expectedPageListTruncated = StringUtil
				.isValidInteger(maxResults) ? pageList.subList(0, expectedNumPages)
				: pageList;
		assertPagesEqual(PagerUtil.getAllPages(newPager(pageSize, maxResults)),
				expectedPageListTruncated, EMPTY_INTEGER_ARRAY);
	}

	/**
	 * A pager factory method hook.
	 *
	 * @param pageSize
	 *            page size
	 * @param maxResults
	 *            maximum number of iterants to retrieve
	 * @return pager instance of the specific implementation being tested in a sub-class
	 *         of this class
	 */
	protected abstract Iterator<List<Integer>> newPager(int pageSize, int maxResults);

	/**
	 * @return
	 */
	protected PagerFactory getPagerFactory()
	{
		return pagerFactory;
	}
}
