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
package edu.utah.further.core.chain;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.page.Pager;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.collections.page.DefaultPagingStrategy;

/**
 * A generic request processor that pages its input attribute and sends each page to a
 * processing sub-chain. Pages are uniformly-sized for now.
 * <p>
 * Depends on a {@link PagerFactory} implementation, left for sub-classes to provide via a
 * hook.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 28, 2010
 */
@Qualifier("pagingRequestProcessor")
public class PagingRequestProcessorImpl extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PagingRequestProcessorImpl.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The name of the attribute to get the Object to be marshalled. Not an AttributeName
	 * in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled sourceAttr;

	/**
	 * The iterable object type.
	 */
	private Labeled iterableType;

	/**
	 * The name of the attribute to get the pager implementation type from.
	 */
	private Labeled pagingControllerAttr;

	/**
	 * The name of the attribute to get the number of header lines from.
	 */
	private Labeled numHeaderRowsAttr;

	/**
	 * Page size.
	 */
	private int pageSize;

	/**
	 * Overrides the default paging request processor's page size ({@link #pageSize}) for
	 * a particular request, if found in it.
	 */
	private Labeled pagingPageSizeAttr;

//	/**
//	 * The thread pool size.
//	 */
//	private int poolSize = 10;
//
//	/**
//	 * Overrides the default thread pool size if found in the request.
//	 */
//	private Labeled poolSizeAttr;

	/**
	 * List of handlers to run once before the main loop, in their order of invocation.
	 */
	private RequestHandler preLoopSubChain;

	/**
	 * Main loop: list of sub-chain handlers in their order of invocation.
	 */
	private RequestHandler mainLoopSubChain;

	/**
	 * List of handlers to run once after the main loop, in their order of invocation.
	 */
	private RequestHandler postLoopSubChain;

	/**
	 * Instantiates pager objects.
	 */
	private PagerFactory pagerFactory;

	// /**
	// * The thread pool.
	// */
	// private ExecutorService pool = Executors.newFixedThreadPool(poolSize,
	// new NamedThreadFactory("Paging-"));

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		Validate.notNull(sourceAttr, "A source attribute must be set");
		Validate.notNull(iterableType, "An iterable object type attribute must be set");
		Validate.notNull(pagingControllerAttr,
				"A paging loop controller attribute must be set");
		Validate.isTrue(pageSize > 0, "Page size must be set to some positive number");
	}

	// ========================= IMPL: RequestProcessor =====================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read input parameters from the request
		final Object input = request.getAttribute(sourceAttr);
		if (log.isDebugEnabled())
		{
			log.debug("Paging result set of type " + input.getClass());
			log.debug("Processing sub-chain " + mainLoopSubChain);
		}

		// Main loop initialization
		final PagingLoopController controller = initializeLoop(request, input);

		// Main loop over pages and send each page separately to the sub-chain
		boolean handled = false; // FUR-1331
		for (; !handled && controller.hasNext();)
		{
			// Get the next page
			final Pager<?> pager = controller.getPager();
			final Object page = pager.next();
			controller.incrementPageCount();
			final int pageNumber = controller.getPageCount();

			// Process the page
			if (log.isInfoEnabled())
			{
				log.info("Processing page #" + pageNumber);
			}
			// Replace input by current page and send it to the appropriate sub-chains
			request.setAttribute(sourceAttr, page);
			handled = processPage(request, controller);
		}
		// Run post-loop sub-chain on last page
		if (!handled)
		{
			handled = doPostLoop(request, postLoopSubChain);
		}

		if (log.isInfoEnabled())
		{
			final int pageCount = controller.getPageCount();
			log.debug("Finished processing " + pageCount + " "
					+ StringUtil.pluralForm("page", pageCount));
		}

		// Clean up
		request.setAttribute(sourceAttr, input);
		// Keep controller for result reporting
		// request.setAttribute(pagingControllerAttr, null);

		// OK to continue processing chain
		return handled;
	}

	// ========================= IMPL: MarshallRequestProcessor ==========

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the sourceAttr property.
	 *
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	public void setSourceAttr(final Labeled sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * Set a new value for the iterableType property.
	 *
	 * @param iterableType
	 *            the iterableType to set
	 */
	public void setIterableType(final Labeled iterableType)
	{
		this.iterableType = iterableType;
	}

	/**
	 * Set a new value for the pageSize property.
	 *
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * Set a new value for the pre-loop list processor list.
	 *
	 * @param processors
	 *            list of processors
	 */
	public void setPreLoop(final List<? extends RequestProcessor> processors)
	{
		this.preLoopSubChain = RequestHandlerBuilder.chain(processors);
	}

	/**
	 * Set a new value for the main loop list processor list.
	 *
	 * @param processors
	 *            list of processors
	 */
	public void setMainLoop(final List<? extends RequestProcessor> processors)
	{
		this.mainLoopSubChain = RequestHandlerBuilder.chain(processors);
	}

	/**
	 * Set a new value for the pre-loop list processor list.
	 *
	 * @param processors
	 *            list of processors
	 */
	public void setPostLoop(final List<? extends RequestProcessor> processors)
	{
		this.postLoopSubChain = RequestHandlerBuilder.chain(processors);
	}

	/**
	 * Set a new value for the pagerFactory property.
	 *
	 * @param pagerFactory
	 *            the pagerFactory to set
	 */
	public void setPagerFactory(final PagerFactory pagerFactory)
	{
		this.pagerFactory = pagerFactory;
	}

	/**
	 * Set a new value for the pagingPageSizeAttr property.
	 *
	 * @param pagingPageSizeAttr
	 *            the pagingPageSizeAttr to set
	 */
	public void setPagingPageSizeAttr(final Labeled pagingPageSizeAttr)
	{
		this.pagingPageSizeAttr = pagingPageSizeAttr;
	}

	/**
	 * Set a new value for the pagingControllerAttr property.
	 *
	 * @param pagingControllerAttr
	 *            the pagingControllerAttr to set
	 */
	public void setPagingControllerAttr(final Labeled pagingControllerAttr)
	{
		this.pagingControllerAttr = pagingControllerAttr;
	}

	/**
	 * Set a new value for the numHeaderRowsAttr property.
	 *
	 * @param numHeaderRowsAttr
	 *            the numHeaderRowsAttr to set
	 */
	public void setNumHeaderRowsAttr(final Labeled numHeaderRowsAttr)
	{
		this.numHeaderRowsAttr = numHeaderRowsAttr;
	}

	// ========================= PRIVATE METHODS =========================

	/**
	 * Main paging loop initialization.
	 *
	 * @param request
	 * @param input
	 * @return paging loop controller
	 */
	private PagingLoopController initializeLoop(final ChainRequest request,
			final Object input)
	{
		// Build a paging controller that enforces page size, as well as maxResults on the
		// number of output results, not pager, which would cap the number of input
		// iterants
		final Integer pageSizeOverride = (pagingPageSizeAttr == null) ? null
				: (Integer) request.getAttribute(pagingPageSizeAttr);
		final int actualPageSize = (pageSizeOverride == null) ? pageSize
				: pageSizeOverride.intValue();
		final PagingStrategy pagingStrategy = new DefaultPagingStrategy(iterableType,
				actualPageSize, PagingStrategy.NO_LIMIT);
		final Pager<?> pager = pagerFactory.pager(input, pagingStrategy);

		// Set the optional # header rows
		if (numHeaderRowsAttr != null)
		{
			final Integer numHeaderRows = (Integer) request
					.getAttribute(numHeaderRowsAttr);
			if (numHeaderRows != null)
			{
				pager.setNumHeaderRows(numHeaderRows.intValue());
			}
		}

		// Attach pager to paging loop controller; create a new controller if it hasn't
		// yet been placed by another processor
		PagingLoopController controller = request.getAttribute(pagingControllerAttr);
		if (controller == null)
		{
			if (log.isInfoEnabled())
			{
				log.info("No paging loop controller found, creating new");
			}
			controller = new PagingLoopController();
			request.setAttribute(pagingControllerAttr, controller);
		}
		controller.setPager(pager);
		return controller;
	}

	/**
	 * Process a single page (the body of the paging loop) - send it to the appropriate
	 * sub-chains.
	 *
	 * @param request
	 *            chain request
	 * @param controller
	 *            paging loop controller
	 * @return a boolean indicates whether the paging handler fully handled the request
	 */
	private boolean processPage(final ChainRequest request,
			final PagingLoopController controller)
	{
		// Before first iteration
		if ((controller.getPageCount() == 1) && (preLoopSubChain != null))
		{
			preLoopSubChain.handle(request);
			final boolean handledByPre = request.hasException();
			if (handledByPre)
			{
				return true;
			}
		}

		// Any iteration
		final boolean handledByMain = request.hasException()
				|| mainLoopSubChain.handle(request);
		if (handledByMain)
		{
			return true;
		}

		// Request was not fully handled by any of the sub-chains
		return false;
	}

	/**
	 * Execute the post-loop sub-chain on the last page.
	 *
	 * @param request
	 * @param postLoopSubChain
	 *            post-loop sub-chain
	 * @return was request fully handled by this method
	 */
	private static boolean doPostLoop(final ChainRequest request,
			final RequestHandler postLoopSubChain)
	{
		if (postLoopSubChain != null)
		{
			postLoopSubChain.handle(request);
			final boolean handledByPost = request.hasException();
			if (handledByPost)
			{
				return true;
			}
		}

		// Request was not fully handled by this method
		return false;
	}
}
