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
package edu.utah.further.fqe.ds.api.results;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.query.domain.SearchQuery;

/**
 * Retrieves results and processes results from completed queries.
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
 * @version Nov 2, 2010
 */
public interface ResultService
{
	// ========================= METHODS ===================================

	/**
	 * Generate a union result from the list of query identifiers. This is used to display
	 * the overall results depending on the {@link ResultType}
	 * 
	 * @param queryIds
	 *            list of DQC IDs to join
	 * @param resultType
	 *            join result type
	 * @param intersectionIndex
	 *            if <code>resultType = INTERESECTION</code>, the intersection index of
	 *            the join, or <code>null</code>, if inapplicable to this join type. Use
	 *            <code>null</code> for a classical intersection (n-intersection of
	 *            n-sets)
	 * @return union result
	 */
	Long join(List<String> queryIds, ResultType resultType, Integer intersectionIndex);

	/**
	 * Generate a union result from the list of query identifiers by attribute name. This
	 * is used to display results per attribute depending on the {@link ResultType}
	 * 
	 * @param queryIds
	 *            list of DQC IDs to join
	 * @param attributeName
	 *            logical model demographics category attribute name (under the
	 *            <code>Person</code> class)
	 * @param resultType
	 *            join result type
	 * @param intersectionIndex
	 *            if <code>resultType = INTERESECTION</code>, the intersection index of
	 *            the join
	 * @return union result, broken down by category value
	 */
	Map<String, Long> join(List<String> queryIds, String attributeName,
			ResultType resultType, int intersectionIndex);

	/**
	 * Returns the result of a previous query as a list of identifiers. Typically patient
	 * identifiers but they could be other identifiers as long as they could be related in
	 * some fashion.
	 * 
	 * @param a
	 *            list of query ids
	 * @return a list of identifiers
	 */
	List<Long> getQueryResultIdentifiers(List<String> queryIds);

	/**
	 * Return all the results of a query. A typical implementation of this method returns
	 * a list of root entity objects (e.g. a List of Person objects)
	 * 
	 * @param ids a list of query ids
	 * @return a list of results
	 */
	<T> List<T> getQueryResults(final List<String> queryIds);
	
	/**
	 * Fetch the results of a query by using another query
	 * 
	 * @param query the query to execute
	 * @return a list of results
	 */
	<T> List<T> getQueryResults(final SearchQuery query);
}
