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
package edu.utah.further.fqe.ds.api.service.results;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.query.domain.SearchQuery;

/**
 * Result service responsible for serving and processing actual data results instead of
 * just summary results.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 16, 2013
 */
public interface ResultDataService
{
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
	 * Return all the results of a query. A typical implementation of this method returns
	 * a list of root entity objects (e.g. a List of Person objects)
	 * 
	 * @param ids
	 *            a list of query ids
	 * @return a list of results
	 */
	<T> List<T> getQueryResults(final List<String> queryIds);

	/**
	 * Fetch the results of a query by using another query
	 * 
	 * @param query
	 *            the query to execute
	 * @return a list of results
	 */
	<T> List<T> getQueryResults(final SearchQuery query);
}
