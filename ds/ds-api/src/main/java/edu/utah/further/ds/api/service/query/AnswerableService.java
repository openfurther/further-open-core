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
package edu.utah.further.ds.api.service.query;

import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A service for determining whether or not a query can be answered by a given data
 * source.
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
 * @version Nov 24, 2010
 */
public interface AnswerableService
{
	// ========================= METHODS ===================================

	/**
	 * Returns whether or not a data source can answer this query.
	 *
	 * @param queryContext
	 *            the {@link QueryContext} which includes the query and additional
	 *            information
	 * @param dsMetaData
	 *            meta data information about the data source that is answering
	 * @param container
	 *            a container of attributes if needed for getting or setting attributes
	 * @return true if it can, false otherwise
	 */
	boolean canAnswer(QueryContext queryContext, DsMetaData dsMetaData);
}
