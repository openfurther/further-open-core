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
package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.query.domain.SearchCriteria.addSimpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.query;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchType.CONJUNCTION;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.ds.impl.fixture.DsImplFixture;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.query.AssociatedResultService;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;

/**
 * Tests attaching previous results to queries.
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
 * @version Mar 27, 2012
 */
public class UTestAssociatedResultsAttacher extends DsImplFixture
{
	/**
	 * Attaches previous results to a query
	 */
	private final AssociatedResultAttacherImpl resultsAttacher = new AssociatedResultAttacherImpl();

	/**
	 * Mock {@link AssociatedResultService}
	 */
	private final AssociatedResultService associatedResultService = createNiceMock(AssociatedResultService.class);

	/**
	 * Mock {@link AssetServiceRest}
	 */
	private final AssetServiceRest assetService = createNiceMock(AssetServiceRest.class);

	/**
	 * The logical property to translate.
	 */
	private final String property = "id";

	/**
	 * The logical property's type
	 */
	private final Class<?> type = BigDecimal.class;

	/**
	 * The logical namespace to translate from.
	 */
	private final String namespace = "FURTHeR";

	/**
	 * The target namespace to translate to.
	 */
	private final String target = "@DSCUSTOM-26@";

	/**
	 * Mock results list.
	 */
	private final List<Long> results = newList();

	/**
	 * Sets up the test mocks
	 */
	@Before
	public void setup()
	{
		resultsAttacher.setAssociatedResultService(associatedResultService);

		results.add(new Long(1L));
		results.add(new Long(2L));
		results.add(new Long(3L));

		final AttributeTranslationResultTo result = new AttributeTranslationResultTo();
		result.setTranslatedAttribute("id");

		final Map<String, String> properties = new HashMap<>();
		properties.put(AssetAssociationProperty.JAVA_DATA_TYPE, "java.lang.Long");
		result.setAttributeProperties(properties);

		expect(
				associatedResultService.getAssociatedResult(new Long(anyLong()),
						eq(target))).andStubReturn(results);
		expect(assetService.getUniqueAttributeTranslation(property, namespace, target))
				.andStubReturn(result);
		replay(associatedResultService, assetService);
	}

	/**
	 * Attach previous results to a simple query.
	 */
	@Test
	public void attachResultsToSimple()
	{
		final QueryContext federatedContext = createFederatedContext(createSimpleQuery());
		final SearchQuery attachedSearchQuery = resultsAttacher.attachAssociatedResult(
				federatedContext, target, property, type);
		assertThat(attachedSearchQuery.getRootCriterion().getSearchType(),
				is(SearchType.CONJUNCTION));

		final List<SearchCriterion> children = attachedSearchQuery
				.getRootCriterion()
				.getCriteria();
		assertThat(new Integer(children.size()), is(new Integer(2)));
		assertThat(children.get(1).getSearchType(), is(SearchType.IN));
		assertThat((BigDecimal) children.get(1).getParameter(1), is(new BigDecimal(1L)));
		assertThat((BigDecimal) children.get(1).getParameter(2), is(new BigDecimal(2L)));
		assertThat((BigDecimal) children.get(1).getParameter(3), is(new BigDecimal(3L)));

	}

	/**
	 * Attach previous results to a junction type query.
	 */
	@Test
	public void attachResultsToJunction()
	{
		final QueryContext federatedContext = createFederatedContext(createJunctionQuery());
		final SearchQuery attachedSearchQuery = resultsAttacher.attachAssociatedResult(
				federatedContext, target, property, type);
		assertThat(attachedSearchQuery.getRootCriterion().getSearchType(),
				is(SearchType.CONJUNCTION));

		final List<SearchCriterion> children = attachedSearchQuery
				.getRootCriterion()
				.getCriteria();
		assertThat(new Integer(children.size()), is(new Integer(3)));
		assertThat(children.get(2).getSearchType(), is(SearchType.IN));
		assertThat((String) children.get(2).getParameter(0), is("id"));
		assertThat((BigDecimal) children.get(2).getParameter(1), is(new BigDecimal(1L)));
		assertThat((BigDecimal) children.get(2).getParameter(2), is(new BigDecimal(2L)));
		assertThat((BigDecimal) children.get(2).getParameter(3), is(new BigDecimal(3L)));
	}

	@Test
	public void attachLargeResults()
	{
		results.clear();
		for (int i = 0; i < 2000; i++)
		{
			results.add(new Long(i));
		}
		final QueryContext federatedContext = createFederatedContext(createSimpleQuery());
		final SearchQuery attachedSearchQuery = resultsAttacher.attachAssociatedResult(
				federatedContext, target, property, type);
		assertThat(attachedSearchQuery.getRootCriterion().getSearchType(),
				is(SearchType.CONJUNCTION));

		final List<SearchCriterion> children = attachedSearchQuery
				.getRootCriterion()
				.getCriteria();
		assertThat(new Integer(children.size()), is(new Integer(2)));
		assertThat(children.get(1).getSearchType(), is(SearchType.DISJUNCTION));
		assertThat(new Integer(children.get(1).getCriteria().size()), is(new Integer(2)));
		assertThat(children.get(1).getCriteria().get(0).getSearchType(),
				is(SearchType.IN));
		assertThat(children.get(1).getCriteria().get(1).getSearchType(),
				is(SearchType.IN));
	}

	/**
	 * Creates a stubbed out federated context with the given search query
	 * 
	 * @param query
	 * @return
	 */
	private QueryContext createFederatedContext(final SearchQuery query)
	{
		final QueryContextToImpl federatedContext = new QueryContextToImpl();
		federatedContext.setId(new Long(1L));
		// This really isn't valid but works for testing purposes
		federatedContext.setAssociatedResult(federatedContext);
		federatedContext.setQuery(query);
		return federatedContext;
	}

	/**
	 * Creates a simple query that only has root criterion and does not start with a
	 * JUNCTION of some sort.
	 * 
	 * @return
	 */
	private SearchQuery createSimpleQuery()
	{
		return query(simpleExpression(Relation.EQ, "age", "50"), "Person");
	}

	/**
	 * Returns a SearchQuery that starts with a JUNCTION.
	 * 
	 * @return
	 */
	private SearchQuery createJunctionQuery()
	{
		final SearchCriterion criterion = junction(CONJUNCTION);
		addSimpleExpression(criterion, Relation.EQ, "age", "50");
		addSimpleExpression(criterion, Relation.EQ, "sex", 263495000L);
		return query(criterion, "Person");
	}
}
