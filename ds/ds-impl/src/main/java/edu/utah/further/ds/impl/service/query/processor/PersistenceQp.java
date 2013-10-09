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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.concurrent.Semaphore;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.chain.AbstractUtilityTransformer;
import edu.utah.further.ds.api.results.ResultList;
import edu.utah.further.ds.api.results.ResultListUtil;
import edu.utah.further.ds.api.service.query.logic.Persister;
import edu.utah.further.ds.api.util.AttributeName;

/**
 * A {@link RequestProcessor} that persists the entity(ies) available in the request.
 * Acceptable inputs are:
 * <ul>
 * <li>
 * {@code List<PersistentEntity<?>>}</li>
 * </ul>
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
 * @version May 5, 2010
 */
// Non-final for @Transactional proxy-able
public class PersistenceQp extends AbstractUtilityTransformer<Persister>
{
	// ========================= CONSTANTS =============================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PersistenceQp.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(getDelegate(), "A persister delegate must be set");
	}

	// ========================= IMPL: RequestProcessor ====================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Persist to database
		// --------------------------------------------------------------------------------
		// FUR-1274: must run this in a parallel thread so that scrollable result sets in
		// the current transaction are not closed when it is suspended. They cannot be
		// reopened afterwards.
		// --------------------------------------------------------------------------------
		// FUR-1289: block execution of process() until persistence is complete, otherwise
		// we can't safely continue the processing chain and say we finished persisting
		// results. First persist, then continue. In the future this might be parallelized
		// using Camel, but not right now, to ensure front-end real-time report
		// correctness and for deterministic query flow testing.

		final Semaphore semaphore = new Semaphore();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final PagingLoopController controller = request
							.getAttribute(AttributeName.PAGING_LOOP_CONTROLLER);
					final ResultList entities = request.getAttribute(getSourceAttr());
					final List<PersistentEntity<?>> rawEntities = ResultListUtil
							.getResultListAsEntities(entities);
					final List<PersistentEntity<?>> persistedEntities = getDelegate()
							.persist(rawEntities, controller, request);

					// Update paging loop controls
					controller.setResultCount(controller.getResultCount()
							+ persistedEntities.size());

					// Save result in the request (NOT the original raw entity
					// list!)
					request.setAttribute(getResultAttr(), persistedEntities);

					// Report to main thread that we completed execution, so that the
					// main thread can continue running
					semaphore.take();
				}
				catch (final Exception e)
				{
					log
							.error("An exception occurred in the entities persisting thread that could not be rethrown",
									e);
					// Report to main thread that we completed execution, so that the
					// main thread can continue running
					semaphore.take();
				}
			}
		}).start();

		// Block until newInnerThread completes
		try
		{
			semaphore.release();
		}
		catch (final InterruptedException e)
		{
			throw new ApplicationException("Semaphore lock release failed", e);
		}

		return false;
	}
}
