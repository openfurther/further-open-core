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
	 * Return the root class which represents the results based on the query identifiers;
	 * this will largely depend on the root object that was chosen.
	 * 
	 * @param queryIds
	 * @return
	 */
	Class<?> getRootResultClass(final List<String> queryIds);

	/**
	 * Fetch the results of a query by using another query
	 * 
	 * @param query
	 *            the query to execute
	 * @return a list of results
	 */
	<T> List<T> getQueryResults(final SearchQuery query);

	/**
	 * Execute a query against results using an object query language like HQL binding any
	 * values in the order they are passed.
	 * 
	 * @param hql
	 * @param orderedParameterValues
	 * @return
	 */
	<T> T getQueryResults(final String hql, final List<Object> orderedParameterValues);
}
