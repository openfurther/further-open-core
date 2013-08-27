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
package edu.utah.further.fqe.ds.api.domain;

import java.util.List;

import edu.utah.further.core.query.domain.SearchQuery;

/**
 * Keeps track of all information related to exporting a result from within the FQE.
 * 
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
 * @version Sep 26, 2012
 */
public interface ExportContext
{
	/**
	 * Get the query id that represents this export
	 * 
	 * @return the queryId
	 */
	public Long getQueryId();

	/**
	 * Sets the query id to export
	 * 
	 * @param queryId
	 */
	public void setQueryId(Long queryId);
	
	/**
	 * Return the user who requested the export
	 *
	 * @return the userId
	 */
	public String getUserId();

	/**
	 * Set the user executing the export
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(String userId);

	/**
	 * Return the filters property.
	 * 
	 * @return the filters
	 */
	public List<SearchQuery> getFilters();

	/**
	 * Adds an export filter. Filters reduce the amount of data returned without reducing
	 * the cohort size.
	 * 
	 * @param filter
	 */
	public void addFilter(SearchQuery filter);
}
