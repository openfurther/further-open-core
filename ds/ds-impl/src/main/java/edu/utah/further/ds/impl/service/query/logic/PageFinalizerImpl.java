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
package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.constant.Constants.Scope.PROTOTYPE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.ds.api.service.query.logic.PageFinalizer;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Finalizes any query operations such as recording statistics at the end of a result set
 * page processing. In particular, increments the result counter of the result context.
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
@Service("pageFinalizer")
@Scope(PROTOTYPE)
public class PageFinalizerImpl implements PageFinalizer
{
	// ========================= CONSTANTS ======================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PageFinalizerImpl.class);

	// ========================= IMPL: PageFinalizer ============================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.PageFinalizer#finalizePage(edu.utah
	 * .further.fqe.ds.api.domain.QueryContext, java.lang.Iterable)
	 */
	@Override
	public QueryContext finalizePage(final QueryContext queryContext,
			final List<?> resultPage, final PagingLoopController controller)
	{
		// Increment number results by this page size
		final long oldNumRecords = queryContext.getNumRecords();
		final long newNumRecords = controller.getResultCount();
		if (log.isInfoEnabled())
		{
			log.info("Updating #records from " + oldNumRecords + " to " + newNumRecords);
		}
		queryContext.setNumRecords(newNumRecords);

		return queryContext;
	}
}
