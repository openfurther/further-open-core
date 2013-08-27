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
package edu.utah.further.core.query.domain;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;

/**
 * Traverses a query and processes its criterion tree and sub-queries. Contains hooks for
 * pre- and post-processing tree nodes.
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
@Api
public abstract class SimpleSearchQueryVisitor implements SearchQueryVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SimpleSearchQueryVisitor.class);

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

	/**
	 * Visit an entire search query.
	 * 
	 * @param searchQuery
	 *            search query
	 */
	@Override
	public final void visit(final SearchQuery searchQuery)
	{
		// Pre-process this query
		preProcessQuery(searchQuery);

		// Process criterion tree
		visitTree(searchQuery.getRootCriterion());

		// Post-process this query
		postProcessQuery(searchQuery);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Visit the criterion tree rooted at searchCriterion
	 * 
	 * @param searchCriterion
	 *            root of the current criterion tree
	 */
	private void visitTree(final SearchCriterion searchCriterion)
	{
		// Pre-process this criterion
		preProcessCriterion(searchCriterion);

		// Process sub-criteria
		for (final SearchCriterion childCriterion : searchCriterion.getCriteria())
		{
			visitTree(childCriterion);
		}

		// Process sub-queries
		for (final SearchQuery childQuery : searchCriterion.getQueries())
		{
			visit(childQuery);
		}

		// Post-process this criterion
		postProcessCriterion(searchCriterion);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Do this operation at a node before operating on its children. This is a hook.
	 * 
	 * @param query
	 *            the currently processed [sub-] query
	 * @return an optional object containing intermediate processing results
	 */
	protected void preProcessQuery(final SearchQuery query)
	{
		// Method stub
	}

	/**
	 * Do this operation at a node after operating on its children. This is a hook.
	 * 
	 * @param query
	 *            the currently processed [sub-] query
	 * @return an optional object containing intermediate processing results
	 */
	protected void postProcessQuery(final SearchQuery query)
	{
		// Method stub
	}

	/**
	 * Do this operation at a node before operating on its children. This is a hook.
	 * 
	 * @param searchCriterion
	 *            the root node of the current criterion tree
	 * @return an optional object containing intermediate processing results
	 */
	protected void preProcessCriterion(final SearchCriterion searchCriterion)
	{
		// Method stub
	}

	/**
	 * Do this operation at a node after operating on its children. This is a hook.
	 * 
	 * @param searchCriterion
	 *            the root node of the current criterion tree
	 * @return an optional object containing intermediate processing results
	 */
	protected void postProcessCriterion(final SearchCriterion searchCriterion)
	{
		// Method stub
	}
}
