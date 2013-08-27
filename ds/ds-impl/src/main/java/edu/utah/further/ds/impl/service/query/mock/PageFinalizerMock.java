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
package edu.utah.further.ds.impl.service.query.mock;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.chain.UtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.PageFinalizer;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Mock SearchQuery finalizer.
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
 * @version Feb 2, 2010
 */
@Mock
@Service("pageFinalizerMock")
@UtilityProcessor
public class PageFinalizerMock implements PageFinalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(PageFinalizerMock.class);

	// ========================= FIELDS =================================

	/**
	 * Common to mock query flow logical components.
	 */
	private MockUtilService common;

	// ========================= CONSTRUCTORS ===========================

	/**
	 * Initialize debugging fields.
	 */
	public PageFinalizerMock()
	{
		common = new MockUtilService("Page Finalization");
	}

	// ========================= Impl: PageFinalizer ====================

	/**
	 * @param queryContext
	 * @param resultPage
	 * @param controller
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.PageFinalizer#finalizePage(edu.utah.further.fqe.ds.api.domain.QueryContext,
	 *      java.util.List,
	 *      edu.utah.further.core.api.collections.page.PagingLoopController)
	 */
	@Override
	public QueryContext finalizePage(final QueryContext queryContext, final List<?> resultPage,
			final PagingLoopController controller)
	{
		common.printTitle();
		queryContext.finish();
		return QueryContextToImpl.newCopy(queryContext);
	}

}
