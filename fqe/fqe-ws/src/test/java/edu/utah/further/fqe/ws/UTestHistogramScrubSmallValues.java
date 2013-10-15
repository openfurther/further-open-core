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
package edu.utah.further.fqe.ws;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.service.query.AggregationService;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResult;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultTo;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultsTo;
import edu.utah.further.fqe.api.ws.to.aggregate.Category;
import edu.utah.further.fqe.api.ws.to.aggregate.CategoryTo;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ws.fixture.FqeWsFixture;

/**
 * FUR-1745: test lumping small counts into a single category in demographics histograms.
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
 * @version Apr 11, 2011
 */
public final class UTestHistogramScrubSmallValues extends FqeWsFixture
{
	// ========================= COSNTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestHistogramScrubSmallValues.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * The DAO to be tested.
	 */
	@Autowired
	private ResultDataService resultDataService;

	/**
	 * Creates and manages query plans.
	 */
	@Autowired
	private AggregationService aggregationService;

	// ========================= SETUP METHODS =============================

	@Before
	public void setup()
	{
		EasyMock.reset(resultDataService);
		EasyMock.expect(
				resultDataService.join(EasyMock.<List<String>> anyObject(),
						anyObject(String.class), anyObject(ResultType.class), anyInt()))
				.andStubDelegateTo(new ResultDataService()
				{
					
					@Override
					@SuppressWarnings("boxing")
					public Map<String, Long> join(final List<String> queryIds,
							final String attributeName, final ResultType resultType,
							final int intersectionIndex)
					{
						final Map<String, Long> histogram = CollectionUtil.newMap();
						if ("Sex".equals(attributeName))
						{
							histogram.put("Female", 12l);
							histogram.put("Male", 8l);
							histogram.put("Other", 3l);
						}
						else if ("Religion".equals(attributeName))
						{
							histogram.put("LDS", 50l);
							histogram.put("Catholic", 2l);
							histogram.put("Baptist", 4l);
						}
						else if ("Language".equals(attributeName))
						{
							histogram.put("English", 50l);
							histogram.put("Spanish", 10l);
							histogram.put("Chinese", 6l);
						}
						else
						{
							final String queryDependentPrefix = " "
									+ queryIds.get(0).subSequence(0, 5);
							histogram.put("Value" + queryDependentPrefix, 10l);
							histogram.put("Some Other Value " + queryDependentPrefix,
									new Long(25));
						}
						return histogram;
					}
					
					@Override
					public <T> List<T> getQueryResults(final SearchQuery query)
					{
						fail();
						return null;
					}
					
					@Override
					public <T> List<T> getQueryResults(final List<String> queryIds)
					{
						fail();
						return null;
					}
				});
		replay(resultDataService);
	}

	// ========================= METHODS ===================================

	/**
	 * No small values present in the histogram.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void noScrubbingRequired()
	{
		final Map<String, Long> actual = getHistogram("Language");
		final Map<String, Long> expected = CollectionUtil.newMap();
		expected.put("English", 50l);
		expected.put("Spanish", 10l);
		expected.put("Chinese", 6l);
		assertThat(actual, is(expected));
	}

	/**
	 * Lumping small counts into a single category.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void scrubbingRequired()
	{
		final Map<String, Long> actual = getHistogram("Religion");
		final Map<String, Long> expected = CollectionUtil.newMap();
		expected.put("LDS", 50l);
		expected.put("Other", -1l);
		assertThat(actual, is(expected));
	}

	/**
	 * No small values present in the histogram but an "Other" category happens to be
	 * there already. So it will be replaced by a scrubbed "Other" category if and only if
	 * its value is small.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void noScrubbingButOtherCategoryAlreadyPresentSmall()
	{
		final Map<String, Long> actual = getHistogram("Sex");
		final Map<String, Long> expected = CollectionUtil.newMap();
		expected.put("Other", -1l); // scrubbed in-place!
		expected.put("Male", 8l);
		expected.put("Female", 12l);
		assertThat(actual, is(expected));
	}

	/**
	 * No small values present in the histogram but an "Other" category happens to be
	 * there already. So it will be replaced by a scrubbed "Other" category if and only if
	 * its value is small.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void noScrubbingButOtherCategoryAlreadyPresentLarge()
	{
		final Map<String, Long> actual = getHistogram("Sex");
		actual.put("Other", 100l); // not small
		final Map<String, Long> expected = CollectionUtil.newMap();
		expected.put("Other", 100l);
		expected.put("Male", 8l);
		expected.put("Female", 12l);
		assertThat(actual, is(expected));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return a hard-coded histogram by category title.
	 * 
	 * @param category
	 *            title of category (e.g., "Sex")
	 * @return mock histogram
	 */
	private Map<String, Long> getHistogram(final String category)
	{
		// Compute a raw histogram
		final ResultType type = ResultType.SUM;
		final Map<String, Long> histogram = resultDataService.join(
				Arrays.asList("12345", "67890"), category, type, 0);
		if (log.isDebugEnabled())
		{
			log.debug("Original Histogram: " + histogram);
		}

		// Create a place-holder for the raw histogram
		final AggregatedResults results = new AggregatedResultsTo();
		final AggregatedResult result = new AggregatedResultTo(type);
		final Category categoryTo = new CategoryTo(category, histogram);
		result.addCategory(categoryTo);
		results.addResult(result);

		// Retrieve the scrubbed results
		final AggregatedResults scrubbedResults = aggregationService
				.scrubResults(results);
		final AggregatedResult scrubbedResult = scrubbedResults
				.getResults()
				.iterator()
				.next();
		final Map<String, Long> scrubbedHistogram = scrubbedResult
				.getCategories()
				.iterator()
				.next()
				.getEntries();
		if (log.isDebugEnabled())
		{
			log.debug("Scrubbed Histogram: " + scrubbedHistogram);
		}
		return scrubbedHistogram;
	}
}
