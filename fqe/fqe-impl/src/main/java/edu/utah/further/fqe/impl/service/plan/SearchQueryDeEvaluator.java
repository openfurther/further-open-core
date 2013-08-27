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
package edu.utah.further.fqe.impl.service.plan;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.CollectionUtil.MapType;
import edu.utah.further.core.math.misc.Pair;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SimpleSearchQueryVisitor;
import edu.utah.further.fqe.impl.util.FqeImplResourceLocator;

/**
 * Parses search query criterion expressions and decides whether they are dependency
 * expressions (DEs), which are evaluated to result set values from previous queries.
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
 * @version Dec 23, 2010
 */
final class SearchQueryDeEvaluator extends SimpleSearchQueryVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SearchQueryDeEvaluator.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * A map of an expression location (a search criterion + parameter index) to the
	 * corresponding {@link DependencyExpression}.
	 */
	private final Map<Pair<SearchCriterion, Integer>, DependencyExpression> expressionLocations = CollectionUtil
			.newMap(MapType.LINKED_HASH_MAP);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param searchQuery
	 */
	public SearchQueryDeEvaluator(final SearchQuery searchQuery)
	{
		super();
		// Visit the root query and build the expression location map
		visit(searchQuery);
	}

	// ========================= METHODS ===================================

	/**
	 * Evaluate all expressions (TODO: replace by a cross-product iterator API).
	 */
	public void evaluate()
	{
		for (final Map.Entry<Pair<SearchCriterion, Integer>, DependencyExpression> entry : expressionLocations
				.entrySet())
		{
			final Pair<SearchCriterion, Integer> key = entry.getKey();
			final SearchCriterion searchCriterion = key.getLeft();
			final Integer index = key.getRight();
			final DependencyExpression expression = entry.getValue();
			final Object value = getDeEvaluator().evaluate(expression);
			searchCriterion.setParameter(index.intValue(), value);
		}
	}

	// ========================= IMPL: SimpleSearchQueryVisitor ============

	/**
	 * @see edu.utah.further.core.query.domain.SimpleSearchQueryVisitor#preProcessQuery(edu.utah.further.core.query.domain.SearchQuery)
	 * @param query
	 */
	@Override
	protected void preProcessQuery(final SearchQuery query)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Processing " + query);
		}
	}

	/**
	 * @see edu.utah.further.core.query.domain.SimpleSearchQueryVisitor#preProcessCriterion(edu.utah.further.core.query.domain.SearchCriterion)
	 * @param searchCriterion
	 */
	@Override
	protected void preProcessCriterion(final SearchCriterion searchCriterion)
	{
		for (int i = 0; i < searchCriterion.getNumParameters(); i++)
		{
			final Object parameter = searchCriterion.getParameter(i);
			if (log.isTraceEnabled())
			{
				log.trace("Processing parameter " + parameter);
			}
			if (DependencyExpression.matches(parameter))
			{
				// Replace searchCriterion expression by its evaluated value
				final DependencyExpression expression = new DependencyExpression(
						(String) parameter);
				expressionLocations.put(new Pair<>(searchCriterion, new Integer(i)),
						expression);
				if (log.isDebugEnabled())
				{
					log.debug("Evaluated " + parameter + " to " + expression);
				}
			}
		}
	}

	/**
	 * @return
	 */
	private static DeEvaluator getDeEvaluator()
	{
		return FqeImplResourceLocator.getInstance().getDeEvaluator();
	}
}
