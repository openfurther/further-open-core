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
package edu.utah.further.core.qunit.page;

import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;

import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlUtil;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.util.collections.page.DefaultPagingStrategy;
import edu.utah.further.core.util.collections.page.PagerUtil;

/**
 * A unit test of paging XML documents.
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
@SuppressWarnings("restriction")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/page/core-qunit-test-page-context.xml" })
public final class UTestXmlPaging
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestXmlPaging.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates pager objects.
	 */
	@Autowired
	private PagerFactory pagerFactory;

	/**
	 * The complete result set XML document.
	 */
	@javax.annotation.Resource(name = "resultSet")
	private Resource resultSet;

	/**
	 * Expected XML page resources.
	 */
	@javax.annotation.Resource(name = "expectedPageResourceNames")
	private List<String> expectedPages;

	/**
	 * The complete result set XML document.
	 */
	@javax.annotation.Resource(name = "resultSetEmpty")
	private Resource resultSetEmpty;

	/**
	 * Expected XML page resources for a single-iterant test.
	 */
	@javax.annotation.Resource(name = "expectedPageResourceNamesSingleIterant")
	private List<String> expectedPagesSingleIterant;

	/**
	 * Expected pages for empty - one page with the root elements
	 */
	@javax.annotation.Resource(name = "expectedPagesEmpty")
	private List<String> expectedPagesEmpty;

	// ========================= TESTING METHODS ===========================

	/**
	 * Ensure that the SUN StAX implementation is recognized as implementing the StAX API.
	 */
	@Test
	public void xmlTypeHierarchy()
	{
		assertTrue("SUN StAX implementation does not implement StAX API",
				ReflectionUtil.isGeneralizationOf(XMLStreamReaderImpl.class,
						XMLStreamReader.class));
	}

	/**
	 * Test iterating over an XML document with a paging iterator with page size
	 * <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void xmlPagingNoMaxResults()
	{
		testXmlPagingIteratorNoLimit(resultSet, 3, expectedPages);
	}

	/**
	 * Test iterating over an XML document with a paging iterator with page size
	 * <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void xmlPagingZeroMaxResults()
	{
		testXmlPagingIterator(resultSet, 3, 0, expectedPages);
	}

	/**
	 * Test iterating over an XML document with a paging iterator with page size
	 * <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void xmlPagingWithMaxResultsSingleOutputPage()
	{
		testXmlPagingIterator(resultSet, 3, 3, expectedPages);
	}

	/**
	 * Test iterating over an XML document with a paging iterator with page size
	 * <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void xmlPagingWithMaxResultsMultipleOutputPages()
	{
		testXmlPagingIterator(resultSet, 3, 7, expectedPages);
	}

	/**
	 * Test iterating over an XML document with a paging iterator with page size
	 * <code>1</code>, which is identical to a standard element-wise iteration.
	 */
	@Test
	public void xmlPagingWithMaxResultsExactlyDivisibleByPageSize()
	{
		testXmlPagingIterator(resultSet, 3, 6, expectedPages);
	}

	/**
	 * Test iterating over an XML document with a paging iterator with a sufficiently
	 * larger page size so that there's a single iterant in the file.
	 */
	@Test
	public void xmlPagingPageSizeGreaterThanNumberOfEntities()
	{
		testXmlPagingIteratorNoLimit(resultSet, 100, expectedPagesSingleIterant);
	}

	/**
	 * Test iterating over an document with just root tags. Should return 1 page.
	 */
	@Test
	public void xmlPagingOfEmptyDocument()
	{
		testXmlPagingIteratorNoLimit(resultSetEmpty, 100, expectedPagesEmpty);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param actualPages
	 * @param i
	 * @param expectedPages
	 */
	@SuppressWarnings("boxing")
	private static void assertPagesEqual(final List<String> actualPages,
			final List<String> expectedPages)
	{
		assertThat(actualPages.size(), is(expectedPages.size()));
		for (int i = 0; i < actualPages.size(); i++)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Comparing page " + i + " actual " + actualPages.get(i));
			}
			xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
					.actualResourceString(actualPages.get(i))
					.expectedResourceName(expectedPages.get(i))
					.die(true)
					.doAssert();
		}
	}

	/**
	 * @param xmlResource
	 * @param pageSize
	 * @param expectedPageList
	 */
	private void testXmlPagingIteratorNoLimit(final Resource xmlResource,
			final int pageSize, final List<String> expectedPageList)
	{
		testXmlPagingIterator(xmlResource, pageSize, PagingStrategy.NO_LIMIT,
				expectedPageList);
	}

	/**
	 * @param pageSize
	 * @param expectedPageList
	 */
	private void testXmlPagingIterator(final Resource xmlResource, final int pageSize,
			final int maxResults, final List<String> expectedPageList)
	{
		final Iterator<String> pager = (Iterator<String>) pagerFactory.pager(
				newXmlStreamReader(xmlResource), new DefaultPagingStrategy(
						IterableType.XML_STREAM, pageSize, maxResults));
		final List<String> actualPageList = PagerUtil.<String> getAllPages(pager);
		if (log.isDebugEnabled())
		{
			log.debug("pages: " + actualPageList);
		}
		final int expectedNumPages = (int) Math.ceil((1.0d * maxResults) / pageSize);
		final List<String> expectedPageListTruncated = StringUtil
				.isValidInteger(maxResults) ? expectedPageList.subList(0,
				expectedNumPages) : expectedPageList;
		assertPagesEqual(actualPageList, expectedPageListTruncated);
	}

	/**
	 * @param xmlResource
	 * @return
	 * @throws IOException
	 */
	private static XMLStreamReader newXmlStreamReader(final Resource xmlResource)
	{
		try
		{
			return XmlUtil.newXmlStreamReader(xmlResource.getInputStream());
		}
		catch (final IOException e)
		{
			throw new IllegalArgumentException("Could not open resource " + xmlResource);
		}
	}
}
