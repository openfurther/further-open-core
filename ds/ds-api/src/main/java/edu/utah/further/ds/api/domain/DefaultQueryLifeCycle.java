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
package edu.utah.further.ds.api.domain;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Represents the default life cycle of data source participating in the federated query
 * of FURTHeR. Data sources which need additional processing are free to extend this
 * interface.
 * <p>
 * The default life cycle consists of:
 *
 * <pre>
 * 1.) Initialize <br/>
 * 2.) Validate <br/>
 * 3.) Destination Translation <br/>
 * 4.) Query Execution <br/>
 * 5.) Result Translation <br/>
 * 6.) Finalize <br/>
 * </pre>
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
 * @version Jan 25, 2010
 */
public interface DefaultQueryLifeCycle
{
	// ========================= METHODS ===================================

	/**
	 * Initializes the query and does any pre-processing such as loading information about
	 * a data source and instantiating the child {@link QueryContext}
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext initialize(QueryContext queryContext);

	/**
	 * Validates the {@link QueryContext#getQueryXml()}
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext validate(QueryContext queryContext);

	/**
	 * Translates the {@link QueryContext#getQueryXml()} to the destination data source.
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext destinationTranslate(QueryContext queryContext)
			throws ApplicationException;

	/**
	 * Execute the query
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext execute(QueryContext queryContext);

	/**
	 * Translate the result into the FURTHeR Analytical or Logical Model
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext resultTranslate(QueryContext queryContext);

	/**
	 * Finalize the query such as recording statistics about the data source or the
	 * execution time.
	 *
	 * @param queryContext
	 * @return
	 * @throws ApplicationException
	 */
	QueryContext finalize(QueryContext queryContext);
}
