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
package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.Finalizer;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Query finalization processor.
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
 * @version Apr 21, 2010
 */
public class FinalizerQp extends AbstractDelegatingUtilityProcessor<Finalizer>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(FinalizerQp.class);

	// ========================= Impl: RequestHandler ======================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);

		// Perform finalization logic
		final QueryContext newQueryContext = getDelegate().finalize(queryContext);

		// Save results in the chain request
		request.setAttribute(QUERY_CONTEXT, newQueryContext);
		return true;
	}
}
