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
package edu.utah.further.fqe.ds.api.domain.plan;

import edu.utah.further.core.api.discrete.HasSettableIdentifier;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A query plan send rule. Specifies the target data source to send a {@link SearchQuery}
 * to. If no data source ID is specified, the query is sent to all data sources. The plan
 * is represented as a graph whose nodes are {@link ExecutionRule}s and its edges are
 * {@link DependencyRule}s.
 * <p>
 * Only the search query ID is stored here; the parent {@link QueryContext} of this object
 * can be used to look up the {@link SearchQuery} objects if necessary.
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
 * @version Nov 18, 2010
 */
public interface ExecutionRule extends Comparable<ExecutionRule>, HasSettableIdentifier<String>
{
	// ========================= METHODS ===================================

	/**
	 * Return the searchQueryId property.
	 *
	 * @return the searchQueryId
	 */
	Long getSearchQueryId();

	/**
	 * Set a new value for the searchQueryId property.
	 *
	 * @param searchQueryId
	 *            the searchQueryId to set
	 */
	void setSearchQueryId(Long searchQueryId);

	/**
	 * Return the dataSourceId property.
	 *
	 * @return the dataSourceId
	 */
	String getDataSourceId();

	/**
	 * Set a new value for the dataSourceId property.
	 *
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	void setDataSourceId(String dataSourceId);
}