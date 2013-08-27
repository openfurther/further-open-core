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

import static edu.utah.further.core.query.domain.Relation.EQ;
import static edu.utah.further.core.query.domain.SearchCriteria.queryBuilder;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchType.CONJUNCTION;
import static edu.utah.further.core.test.util.AssertUtil.assertSizeEquals;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.data.domain.ComplexPersonEntity;
import edu.utah.further.core.data.fixture.CoreDataFixture;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.query.QueryBuilderHibernateImpl;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;

/**
 * {@link QueryBuilderHibernateImpl} tests.
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
 * @version Sep 22, 2009
 */
// @Transactional class must not be final
public class UTestCriteriaBuilderHibernateImpl extends CoreDataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestCriteriaBuilderHibernateImpl.class);

	// ========================= METHODS ===================================

	/**
	 * Converts a {@link SearchQuery} and executes the resulting Hibernate
	 * {@link GenericCriteria}.
	 */
	@Test
	@Transactional
	@SuppressWarnings("boxing")
	public void convertAndExecuteCriteria()
	{
		final SearchCriterion searchCriterion = SearchCriteria.junction(CONJUNCTION);
		searchCriterion.addCriterion(simpleExpression(EQ, "name", "John Doe"));
		final SearchQuery searchQuery = SearchCriteria.query(searchCriterion,
				"ComplexPerson");
		final List<ComplexPersonEntity> personEntities = runCriteria(searchQuery);
		assertThat(personEntities.size(), greaterThan(1));
	}

	/**
	 * Tests setting distinct and executes the resulting Hibernate {@link GenericCriteria}
	 * .
	 */
	@Test
	@Transactional
	public void isDistinct()
	{
		final SearchCriterion searchCriterion = SearchCriteria.junction(CONJUNCTION);
		searchCriterion.addCriterion(simpleExpression(EQ, "name", "John Doe"));
		searchCriterion.addCriterion(simpleExpression(EQ, "Event.eventName", "Event 1"));
		final SearchQuery searchQuery = queryBuilder(searchCriterion)
				.addAlias("Event", "Event", "events")
				.setRootObject("ComplexPerson")
				.build();
		final List<ComplexPersonEntity> complexPersonEntities = personCriteriaBuilder()
				.setQuery(searchQuery)
				.distinct(true)
				.build()
				.list();

		assertSizeEquals(complexPersonEntities, 1);
	}

	/**
	 * Tests setting distinct and executes the resulting Hibernate {@link GenericCriteria}
	 * .
	 */
	@Test
	@Transactional
	public void isNotDistinct()
	{
		final SearchCriterion searchCriterion = SearchCriteria
				.junction(SearchType.CONJUNCTION);
		searchCriterion.addCriterion(simpleExpression(EQ, "name", "John Doe"));
		searchCriterion.addCriterion(simpleExpression(EQ, "Event.eventName", "Event 1"));
		final SearchQuery searchQuery = queryBuilder(searchCriterion)
				.addAlias("Event", "Event", "events")
				.setRootObject("ComplexPerson")
				.build();
		final List<ComplexPersonEntity> complexPersonEntities = runCriteria(searchQuery);
		assertSizeEquals(complexPersonEntities, 4);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param criteriaBuilder
	 * @return
	 */
	private List<ComplexPersonEntity> runCriteria(final SearchQuery searchQuery)
	{
		return newPersonCriteria(searchQuery).list();
	}
}
