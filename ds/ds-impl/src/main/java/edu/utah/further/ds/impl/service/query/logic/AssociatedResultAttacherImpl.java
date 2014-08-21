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
import static edu.utah.further.core.query.domain.SearchCriteria.collection;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryBuilder;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.core.query.domain.SortCriterion;
import edu.utah.further.ds.api.service.query.logic.AssociatedResultAttacher;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.query.AssociatedResultService;

/**
 * Attaches associated results (already completed results) to an existing
 * {@link SearchQuery}
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
 * @version Mar 22, 2012
 */
@Service("associatedResultsAttacher")
public class AssociatedResultAttacherImpl implements AssociatedResultAttacher
{
	// ========================= CONSTANTS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssociatedResultAttacherImpl.class);

	// ========================= DEPENDENCIES =================================

	/**
	 * Retrieves the results that are associated with this {@link QueryContext} in the
	 * chain request.
	 */
	@Autowired
	private AssociatedResultService associatedResultService;

	// ========================= IMPL:AssociatedResultsAttacher ===============

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.service.query.logic.AssociatedResultAttacher#
	 * attachAssociatedResult(edu.utah.further.fqe.ds.api.domain.QueryContext,
	 * edu.utah.further.core.query.domain.SearchQuery, java.lang.Long, java.lang.Long)
	 */
	@Override
	public SearchQuery attachAssociatedResult(final QueryContext federatedContext,
			final String datasourceId, final String propertyName,
			final Class<?> propertyType)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Retreiving previous results for query with QueryContext id of "
					+ federatedContext.getAssociatedResult().getId());
		}
		final List<Long> resultsList = associatedResultService.getAssociatedResult(
				federatedContext.getAssociatedResult().getId(), datasourceId);

		final SearchQuery searchQuery = federatedContext.getQuery();

		SearchCriterion newRootCriterion = null;
		if (searchQuery.getRootCriterion().getSearchType() == SearchType.CONJUNCTION)
		{
			// We can add to a junction
			newRootCriterion = searchQuery.getRootCriterion();
		}
		else
		{
			// We create a new junction and add the old query to it
			newRootCriterion = SearchCriteria.junction(SearchType.CONJUNCTION);
			newRootCriterion.addCriterion(searchQuery.getRootCriterion());
		}

		final Object[] results = getResultsAsType(resultsList, propertyType);
		final SearchCriterion additionalCriterion = collection(SearchType.IN,
				propertyName, results);

		// Add our new associated result criteria
		newRootCriterion.addCriterion(additionalCriterion);

		return constructNewSearchQuery(searchQuery, newRootCriterion);
	}

	/**
	 * @param resultsList
	 * @param propertyType
	 * @return
	 */
	private Object[] getResultsAsType(final List<Long> resultsList,
			final Class<?> propertyType)
	{
		if (propertyType == Long.class)
		{
			return resultsList.toArray(new Long[resultsList.size()]);
		}
		final List<Object> results = newList();
		for (final Long result : resultsList)
		{
			if (propertyType == BigDecimal.class)
			{
				results.add(new BigDecimal(result.longValue()));
			}
			else if (propertyType == BigInteger.class)
			{
				results.add(new BigInteger(result.toString()));
			}
			else if (propertyType == Integer.class)
			{
				results.add(new Integer(result.toString()));
			}
			else
			{
				throw new ApplicationException(
						"No known strategy for converting from result type Long to "
								+ propertyType.getName());
			}
		}
		return results.toArray(new Object[results.size()]);
	}

	/**
	 * Constructs a completely new {@link SearchQuery} from an old {@link SearchQuery},
	 * ensuring that aliases and sortcriterion, etc is copied over.
	 * 
	 * @param searchQuery
	 * @param newRootCriterion
	 * @return
	 */
	private SearchQuery constructNewSearchQuery(final SearchQuery searchQuery,
			final SearchCriterion newRootCriterion)
	{
		final SearchQueryBuilder builder = SearchCriteria.queryBuilder(newRootCriterion);
		builder.addAliases(searchQuery.getAliases());
		for (final SortCriterion sortCriterion : searchQuery.getSortCriteria())
		{
			builder.addSortCriterion(sortCriterion);
		}
		if (searchQuery.getFirstResult() != null)
		{
			builder.setFirstResult(searchQuery.getFirstResult().intValue());
		}
		if (searchQuery.getMaxResults() != null)
		{
			builder.setMaxResults(searchQuery.getMaxResults().intValue());
		}
		builder.setId(searchQuery.getId());
		return builder.build();
	}

	// ========================= SETTERS =================================

	/**
	 * Set a new value for the associatedResultService property.
	 * 
	 * @param associatedResultService
	 *            the associatedResultService to set
	 */
	public void setAssociatedResultService(
			final AssociatedResultService associatedResultService)
	{
		this.associatedResultService = associatedResultService;
	}

}
