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

import edu.utah.further.core.api.lang.Builder;

/**
 * Builds a {@link SearchQuery} object.
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
 * @version Jul 21, 2010
 */
public interface SearchQueryBuilder extends Builder<SearchQuery>
{
	// ========================= METHODS ===================================

	/**
	 * Add a sortOrder to the list of sort orders. If a sortOrder is <code>null</code> it
	 * is not added to the list.
	 * 
	 * @param sortOrder
	 *            sortOrder to add
	 * @return this (for method chaining)
	 * @see java.util.List#add(java.lang.Object)
	 */
	SearchQueryBuilder addSortCriterion(SortCriterion sortOrder);
	
	/**
	 * Add a list of sort orders.
	 * 
	 * @param sortOrders
	 *            sortOrders to add
	 * @return this (for method chaining)
	 * @see java.util.List#add(java.lang.Object)
	 */
	SearchQueryBuilder addSortCriteriones(List<SortCriterion> sortOrders);

	/**
	 * Join an association, assigning an alias to the joined association.
	 * <p/>
	 * Functionally equivalent to the Hibernate {@link #createAlias(String, String, int)}
	 * using {@link #INNER_JOIN} for the joinType, <i>with the first two arguments
	 * reversed</i>.
	 * 
	 * @param objectType
	 *            the object type of the association
	 * @param alias
	 *            The alias to assign to the joined association (for later reference).
	 * @param associationPath
	 *            A dot-separated property path
	 * @return this (for method chaining)
	 */
	SearchQueryBuilder addAlias(String objectType, String alias, String associationPath);

	/**
	 * Add a map of association aliases. If the map is <code>null</code>, clears all
	 * existing aliases.
	 * 
	 * @param aliases
	 *            alias-to-dot-separated property path map
	 * @return this (for method chaining)
	 */
	SearchQueryBuilder addAliases(List<SearchQueryAlias> aliases);

	/**
	 * Set the first result to be retrieved, numbered from <tt>0</tt>.
	 * 
	 * @param firstResult
	 *            the first result to retrieve, numbered from <tt>0</tt>
	 * @return this (for method chaining)
	 */
	SearchQueryBuilder setFirstResult(int firstResult);

	/**
	 * Set a limit upon the number of objects to be retrieved.
	 * 
	 * @param maxResults
	 *            the maximum number of results
	 * @return this (for method chaining)
	 */
	SearchQueryBuilder setMaxResults(int maxResults);

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	SearchQueryBuilder setId(Long id);

	/**
	 * Sets a new value for the root object which is the class name or the class name
	 * minus 'Entity'
	 * 
	 * @param rootObject
	 *            a String representing the rootObject to set
	 * @return this
	 */
	SearchQueryBuilder setRootObject(String rootObject);
}