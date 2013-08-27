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
package edu.utah.further.core.data.service;

import static edu.utah.further.core.query.domain.SearchCriteria.collection;
import static edu.utah.further.core.query.domain.SearchCriteria.count;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.queryBuilder;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.data.fixture.CoreDataFixture;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;

/**
 * Learning test of the search query COUNT operation (sub-criterion + group-by-having
 * count([distinct] id)).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 26, 2009
 */
// Non-final due to transactionality
public class UTestHibernateCount extends CoreDataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestHibernateCount.class);

	/**
	 * Event names subset to use in contains-count queries.
	 */
	private static final Object[] EVENT_NAMES = new String[]
	{ "Event 1", "Event 2", "Event 3" };

	// ========================= DEPENDENCIES ==============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Tests an collection-property-contains query. In this case, return all persons that
	 * have a count of at least 2 out of 3 events.
	 * <p>
	 * This also illustrates the limitation of this function: the count is always
	 * non-distinct (for a general sub-query; COUNT_ANY, COUNT_ALL do support the distinct
	 * flag). In this case, one of the four returned person (5) IDs has only "Event 1" in
	 * its even list, yet the event is repeated four times and still satisfies
	 * count(event) >= 2.
	 */
	@Test
	@Transactional
	public void countUsingSearchQuery()
	{
		final SearchQuery subQuery = SearchCriteria
				.queryBuilder(collection(SearchType.IN, "Event.eventName", EVENT_NAMES))
				.addAlias("Event", "Event", "this.events")
				.setRootObject("ComplexPerson")
				.build();

		testCountEventExpression(4, count(Relation.GE, new Integer(2), subQuery));
	}

	/**
	 * Test multiple COUNT criteria in the same query. Same as
	 * {@link #countUsingSearchQuery()}, but excludes person ID 5.
	 */
	@Test
	@Transactional
	public void multipleSubSelectsUsingSearchQuery()
	{
		final SearchQuery subQuery = queryBuilder(
				collection(SearchType.IN, "Event.eventName", new Object[]
				{ "Event 1", "Event 2", "Event 3" }))
				.addAlias("Event", "Event", "this.events")
				.setRootObject("ComplexPerson")
				.build();

		testCountEventExpression(3, count(Relation.GE, new Integer(2), subQuery),
				count(Relation.LE, new Integer(3), subQuery));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A test template of COUNT-type criteria (also CONTAINS_ALL, etc.) COUNT property
	 * should be qualified by "Event." to match the main query alias set in this method.
	 * 
	 * @param countCriteria
	 * @param expectedCount
	 *            expected result set size
	 */
	private void testCountEventExpression(final int expectedCount,
			final SearchCriterion... countCriteria)
	{
		final SearchQuery searchQuery = queryBuilder(
				junction(SearchType.CONJUNCTION)
						.addCriteria(Arrays.asList(countCriteria)))
				.addAlias("Event", "Event", "this.events")
				.setRootObject("ComplexPerson")
				.build();

		// Note the distinct flag setting
		final GenericCriteria hibernateCriteria = personCriteriaBuilder()
				.setQuery(searchQuery)
				.distinct(true)
				.build();

		final List<?> result = list(hibernateCriteria);
		assertEquals(expectedCount, result.size());

		// TODO: test that we get the same result set as
		// containsAllEventsMultipleSubselectsCriteria
	}

	/**
	 * @param criteria
	 * @return
	 */
	private List<?> list(final GenericCriteria criteria)
	{
		final List<?> result = criteria.list();
		if (log.isDebugEnabled())
		{
			log.debug("Result set size: " + result.size());
			log.debug("Result set: " + result);
		}
		return result;
	}
}
