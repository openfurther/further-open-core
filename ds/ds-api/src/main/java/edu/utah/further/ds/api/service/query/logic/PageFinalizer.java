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
package edu.utah.further.ds.api.service.query.logic;

import java.util.List;

import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Finalizes any query operations such as recording statistics at the end of a result set
 * page processing.
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
 * @version Jul 29, 2010
 */
public interface PageFinalizer
{
	// ========================= METHODS ===================================

	/**
	 * Finalizes the query doing any operations necessary to consider the query complete.
	 *
	 * @param queryContext
	 *            the current QueryContext
	 * @param resultPage
	 *            last processed result page
	 * @param controller
	 *            paging loop controller, contains statistics on number of results read
	 *            and written to the virtual repository so far
	 * @return a new {@link QueryContext}
	 * @throws ApplicationException
	 *             if finalization of the query fails
	 */
	QueryContext finalizePage(QueryContext queryContext, List<?> resultPage,
			PagingLoopController controller);
}
