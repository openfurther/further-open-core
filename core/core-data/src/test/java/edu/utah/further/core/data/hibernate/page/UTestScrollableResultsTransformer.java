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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.data.domain.ComplexPersonEntity;
import edu.utah.further.core.data.fixture.CoreDataFixture;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.query.domain.MatchType;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.core.util.collections.page.PagerUtil;

/**
 * Unit test for testing scrollable results distinct_root_entity implementation
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
 * @version Oct 6, 2010
 */
public class UTestScrollableResultsTransformer extends CoreDataFixture
{
	/**
	 * Tests that associations are populated
	 */
	@Test
	@Transactional
	@SuppressWarnings("boxing")
	public void testAssociations()
	{
		final GenericCriteria hibernateCriteria = createLikeCriteria();

		final List<List<? extends PersistentEntity<Long>>> allPages = PagerUtil
				.getAllPages(newPager(1, hibernateCriteria.scroll()));
		for (final List<? extends PersistentEntity<Long>> pages : allPages)
		{
			for (final PersistentEntity<Long> page : pages)
			{
				assertThat(page, instanceOf(ComplexPersonEntity.class));
				final ComplexPersonEntity personEntity = (ComplexPersonEntity) page;
				assertThat(personEntity.getEvents(), notNullValue());
				assertThat(personEntity.getEvents().size(), is(greaterThan(0)));
			}
		}

	}

	/**
	 * Creates a LIKE 'Event' query to retrieve all of the events.
	 * 
	 * @return
	 */
	private GenericCriteria createLikeCriteria()
	{
		final SearchQuery searchQuery = SearchCriteria
				.queryBuilder(
						SearchCriteria.stringExpression(SearchType.LIKE,
								"Event.eventName", "Event", MatchType.CONTAINS))
				.addAlias("Event", "Event", "events")
				.setRootObject("ComplexPerson")
				.build();

		final GenericCriteria hibernateCriteria = personCriteriaBuilder()
				.setQuery(searchQuery)
				.distinct(true)
				.build();
		return hibernateCriteria;
	}
}
