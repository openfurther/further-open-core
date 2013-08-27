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

import static edu.utah.further.core.api.collections.page.PagingStrategy.NO_LIMIT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.fixture.CoreUtilFixture;

/**
 * Unit test of {@link CsvStreamPager} - loading a CSV file into an entity list.
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
 * @version Jan 27, 2011
 */
public final class UTestCsvStreamPager extends CoreUtilFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * Make sure that we don't create a lot of useless objects when we call
	 * <code>toArray()</code> methods.
	 */
	public static final Person[] EMPTY_PERSON_ARRAY = new Person[0];

	/**
	 * A test list.
	 */
	private static final Person PERSON_0 = new Person("John", "Smith");
	private static final Person PERSON_1 = new Person("Johnny", "Doe");
	private static final Person PERSON_2 = new Person("Ann", "Baley");

	protected static final List<Person> TEST_LIST = Arrays.asList(new Person[]
	{ PERSON_0, PERSON_1, PERSON_2 });

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates pager objects.
	 */
	@Autowired
	private PagerFactory pagerFactory;

	/**
	 * Context resource locator.
	 */
	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * {@link Person} list input file.
	 */
	private InputStream inputFile;

	// ========================= SETUP METHODS =============================

	/**
	 * Read input file.
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException
	{
		inputFile = resourceLoader.getResource("persons.txt").getInputStream();
	}

	/**
	 * Read input file.
	 */
	@After
	public void tearDown()
	{
		inputFile = null;
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * A learning test of the {@link Scanner} API for loading a {@link Person} from a
	 * file.
	 */
	@Test
	public void scannerApi()
	{
		// Note that the pipe character has to be escaped, because useDelimiter() accepts
		// a regular expression
		try (@SuppressWarnings("resource")
		//useDelimiter returns the same scanner but Eclipse thinks it's 2 different scanners
		final Scanner scanner = new Scanner("John|Doe").useDelimiter("\\|")) {
			final Person person = new Person();
			person.setFirstName(scanner.next());
			person.setLastName(scanner.next());
			assertThat(person, is(new Person("John", "Doe")));
		}
	}

	/**
	 * Test loading a simple person file in one page.
	 */
	@Test
	public void loadFileOneEntityPerPage()
	{
		final int size = 3; // inputFile.size();
		final Person[][] pages = new Person[size][];
		for (int i = 0; i < size; i++)
		{
			pages[i] = new Person[]
			{ TEST_LIST.get(i) };
		}
		testPagingIteratorNoLimit(1, pages);
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b</code>, and where <code>a</code> is not divisible by <code>b</code>,
	 * leaving a smaller last page.
	 */
	@Test
	public void loadFileMultipleEntitiesPerPage()
	{
		testPagingIteratorNoLimit(2, new Person[][]
		{
		{ PERSON_0, PERSON_1 },
		{ PERSON_2 } });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b< &gt; a</code>.
	 */
	@Test
	public void listSizeGreaterThanPageSize()
	{
		testPagingIteratorNoLimit(11, new Person[][]
		{ TEST_LIST.toArray(EMPTY_PERSON_ARRAY) });
	}

	/**
	 * Test iterating over a list of size <code>a</code> with a paging iterator with page
	 * size <code>b< &gt; a</code>.
	 */
	@Test
	public void listSizeWayGreaterThanPageSize()
	{
		testPagingIteratorNoLimit(100000, new Person[][]
		{ TEST_LIST.toArray(EMPTY_PERSON_ARRAY) });
	}

	/**
	 * Test iterating over a <code>null</code> list.
	 */
	@Test(expected = BusinessRuleException.class)
	public void nullArgumentNotAllowed()
	{
		pagerFactory.pager(null, IterableType.LIST);
	}

	/**
	 * Test iterating over a list with an invalid page size.
	 */
	@Test(expected = BusinessRuleException.class)
	public void pageSizeMustBePositive1()
	{
		@SuppressWarnings("unused")
		final Iterator<List<Person>> iterator = newPager(0, NO_LIMIT);
	}

	/**
	 * Test iterating over a list with an invalid page size.
	 */
	@Test(expected = BusinessRuleException.class)
	public void pageSizeMustBePositive2()
	{
		@SuppressWarnings("unused")
		final Iterator<List<Person>> iterator = newPager(-1, NO_LIMIT);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param pageSize
	 * @param expectedPages
	 */
	private void testPagingIteratorNoLimit(final int pageSize,
			final Person[][] expectedPages)
	{
		testPagingIterator(pageSize, NO_LIMIT, expectedPages);
	}

	/**
	 * @param pageSize
	 * @param maxResults
	 * @param expectedPages
	 */
	private void testPagingIterator(final int pageSize, final int maxResults,
			final Person[][] expectedPages)
	{
		final int expectedNumPages = (int) Math.ceil((1.0d * maxResults) / pageSize);
		final List<Person[]> pageList = Arrays.<Person[]> asList(expectedPages);
		final List<Person[]> expectedPageListTruncated = StringUtil
				.isValidInteger(maxResults) ? pageList.subList(0, expectedNumPages)
				: pageList;
		final List<List<Person>> allPages = PagerUtil.getAllPages(newPager(pageSize,
				maxResults));
		PagerFixture.<Person> assertPagesEqual(allPages, expectedPageListTruncated,
				EMPTY_PERSON_ARRAY);
	}

	/**
	 * @param pageSize
	 * @return
	 */
	protected Iterator<List<Person>> newPager(final int pageSize, final int maxResults)
	{
		return (Iterator<List<Person>>) pagerFactory.pager(inputFile,
				new DefaultPagingStrategy(IterableType.CSV_STREAM, pageSize, maxResults));
	}
}
