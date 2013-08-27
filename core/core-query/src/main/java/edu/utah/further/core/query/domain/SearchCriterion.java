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

import java.util.Collection;
import java.util.List;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.Api;

/**
 * An object that encapsulates a single search criterion. uses the Composite pattern.
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
 * @version Oct 13, 2008
 */
@Api
public interface SearchCriterion
{
	// ========================= IMPL: Object ==============================

	/**
	 * Two {@link SearchCriterion} objects must be equal <i>if and only if</i> their hash
	 * codes are equal. This is important because a dependent object (e.g. a query
	 * context) may decide whether to deep-copy its search query field based on hash code
	 * equality.
	 * 
	 * @return hash code
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	int hashCode();

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Return the list of criteria contained by this criterion, if this is a composite
	 * criterion. Returns an empty list for a leaf criterion (<code>SimpleCriterion</code>
	 * ). Defensively copied.
	 * 
	 * @return the list of criteria contained by this criterion, if this is a composite
	 *         criterion. Returns an empty list for a leaf criterion (
	 *         <code>SimpleCriterion</code>).
	 */
	List<SearchCriterion> getCriteria();

	/**
	 * Return the list of sub-queries.
	 * 
	 * @return the list of sub-queries of this criterion, if applicable
	 * @see edu.utah.further.core.query.domain.SearchCriterion#getQueries()
	 */
	List<SearchQuery> getQueries();

	/**
	 * Returns the searchType property.
	 * 
	 * @return the searchType
	 */
	SearchType getSearchType();

	/**
	 * Return the expected number of parameters passed to the construction/builder of this
	 * {@link SearchCriterion} object. A value of {@link Constants#INVALID_VALUE_INTEGER}
	 * signifies that an arbitrary number of parameters.
	 * 
	 * @return the expected number of parameters of this search criterion
	 */
	int getNumParameters();

	/**
	 * Return the parameters property.
	 * 
	 * @return the parameters
	 */
	List<?> getParameters();

	/**
	 * Return a parameter by index.
	 * 
	 * @param index
	 *            parameter index in the parameters list
	 * @return corresponding parameter value
	 */
	Object getParameter(int index);

	/**
	 * Set the parameter at position <code>index</code> to a new value.
	 * 
	 * @param index
	 *            parameter index in the parameters list
	 * @param value
	 *            new parameter value
	 */
	void setParameter(int index, Object value);

	/**
	 * Return the options property.
	 * 
	 * @return the options
	 */
	SearchOptions getOptions();

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getEscapeChar()
	 */
	Character getEscapeChar();

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getMatchType()
	 */
	MatchType getMatchType();

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#isIgnoreCase()
	 */
	Boolean isIgnoreCase();

	/**
	 * Add a sub-criterion.
	 * 
	 * @param subCriterion
	 *            sub-criterion to add
	 * @return this (for method chaining)
	 */
	SearchCriterion addCriterion(SearchCriterion subCriterion);

	/**
	 * Add a collection of sub-criteria.
	 * 
	 * @param subCriteria
	 *            sub-criteria collection to add
	 * @return this (for method chaining)
	 */
	SearchCriterion addCriteria(Collection<? extends SearchCriterion> subCriteria);

	/**
	 * Add a sub-query.
	 * 
	 * @param subQuery
	 *            sub-query to add
	 * @return this (for method chaining)
	 */
	SearchCriterion addQueries(Collection<? extends SearchQuery> subCriteria);

	/**
	 * Add a collection of sub-queries.
	 * 
	 * @param subCriteria
	 *            sub-queries collection to add
	 * @return this (for method chaining)
	 */
	SearchCriterion addQuery(SearchQuery subQuery);
}
