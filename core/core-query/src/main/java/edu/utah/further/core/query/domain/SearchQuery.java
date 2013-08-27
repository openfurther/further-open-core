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

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.discrete.HasSettableIdentifier;

/**
 * An immutable search query object holding a list of search criteria, their filtering
 * logic and result set controls.
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
public interface SearchQuery extends PersistentEntity<Long>, HasSettableIdentifier<Long>
{
	// ========================= IMPL: Object ==============================

	/**
	 * Two {@link SearchQuery} objects must be equal <i>if and only if</i> their hash
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
	 * Return the class which this query is based on; this is with with all query
	 * attributes pivot off of.
	 * 
	 * @return
	 */
	Class<? extends PersistentEntity<?>> getRootObject(final String domainClassPackage);

	/**
	 * Returns the name of root object which this {@link SearchQuery} is based on.
	 * 
	 * The root object is the class name or the class name minus 'Entity' and every
	 * SearchQuery object can get it's root object class given the package name of where
	 * it resides.
	 * 
	 * The package name is configured within the module that uses the SearchQuery and thus
	 * the only potential for user provided input is the class name. This limits the
	 * potential to load arbitrary classes.
	 * 
	 * @return
	 */
	String getRootObjectName();

	/**
	 * Return the list of specified sort criteria. Defensively copied.
	 * 
	 * @return the list of criteria.
	 */
	List<SortCriterion> getSortCriteria();

	/**
	 * Return the root criterion of this criteria tree.
	 * 
	 * @return the root criterion of this criteria tree
	 * @see edu.utah.further.core.query.domain.search.SearchCriterionTo#getCriteria()
	 */
	SearchCriterion getRootCriterion();

	/**
	 * Return a list of the defined aliases. The keys are aliases and the associated
	 * values are the corresponding association paths and the object is the association
	 * path type.
	 * 
	 * @return a list of the defined aliases
	 * @see #createAlias(String, String)
	 */
	List<SearchQueryAlias> getAliases();

	/**
	 * Return the first result number in the result set. Returns <code>null</code> if the
	 * result set is not set a lower bound.
	 * 
	 * @return the first result number in the result set, if set
	 */
	Integer getFirstResult();

	/**
	 * Return the limit upon the number of objects to be retrieved. Returns
	 * <code>null</code> if the result set is not set a size limit.
	 * 
	 * @returnthe limit upon the number of objects to be retrieved
	 */
	Integer getMaxResults();

	/**
	 * Set a new value for the maxResults property.
	 * 
	 * @param maxResults
	 *            the maxResults to set
	 */
	void setMaxResults(Integer maxResults);
}
