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

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A single handler in a Classic CoR (Chain of Responsibility Pattern), i.e., the request
 * is handled by only one of the handlers in the chain at a time, and it decides where to
 * forward the request next.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 28, 2009
 */
final class RequestHandlerSimpleImpl extends AbstractRequestHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RequestHandlerSimpleImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Handles the request within this node.
	 */
	private final RequestProcessor processor;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a chain node.
	 * 
	 * @param processor
	 *            executes request processing business logic on behalf of this node
	 */
	RequestHandlerSimpleImpl(final RequestProcessor processor)
	{
		super();
		Validate.notNull(processor, "A processor must be specified");
		this.processor = processor;
		processor.setRequestHandler(this);
	}

	// ========================= IMPLEMENTATION: Visitable =================

	/**
	 * Let a visitor process this item. Part of the Visitor pattern. This calls back the
	 * visitor's <code>visit()</code> method with this item type.
	 * 
	 * @param visitor
	 *            item data visitor whose <code>visit()</code> method is invoked
	 * @see edu.utah.further.core.api.visitor.Visitable#accept(edu.utah.further.core.api.visitor.Visitor)
	 */
	@Override
	public void accept(final RequestHandlerVisitor visitor)
	{
		visitor.visit(this);
	}

	// ========================= IMPL: RequestHandler ======================

	/**
	 * Starting point of chain processing from this node's viewpoint, called by client or
	 * a predecessor node. Calls {@link #process(ChainRequest)} on this node, and decide
	 * whether to continue the chain. If the next node is not <code>null</code> and this
	 * node did not handle the request, calls {@link #handle(ChainRequest)} on next node
	 * to continue the chain.
	 * <p>
	 * Note that this default implementation forces a linear request processing flow of
	 * handlers. To generalize to branching, before/after decoration like Servlet's
	 * doFilter() or Struts 2 interceptors, overriding this method in sub-classes that
	 * need this functionality.
	 * 
	 * @param request
	 *            the request object
	 * @return a boolean indicates whether this handler fully handled the request
	 */
	@Override
	public final boolean handle(final ChainRequest request)
	{
		// At the beginning of each handle, check to see if we are canceled. By checking
		// before, we don't need to handleInterrupt on this handler since it was never
		// ran.
		if (request.isCanceled())
		{
			log.debug("Execution has been canceled, terminating.");
			// Walk backwards giving each handler a chance to handle this interrupt
			RequestHandler current = this;
			RequestHandler prev = null;
			while ((prev = current.getPrevNode()) != null)
			{
				prev.handleInterrupt();
				current = prev;
			}
			return true;
		}

		if (log.isDebugEnabled())
		{
			log.debug("");
			log.debug(StringUtil.repeat("=", 70));
			log.debug("Handling request: " + this);
			log.debug(StringUtil.repeat("=", 70));
		}

		// Add a message about our handling of the request
		addMessage(request, Severity.INFO, "Begin handling");

		// Forces a linear request processing flow until nextNode is null or this
		// handler returns true from handle().
		final boolean handledByThisNode = processor.process(request);
		if (log.isDebugEnabled())
		{
			log.debug("Finished processing of node: " + this);
			if (handledByThisNode) {
				log.debug("No next node, processing has been terminated. Last node: " + this);
			} else {
				log.debug("Next node: " + getNextNode());

			}
		}

		return (getNextNode() != null && !handledByThisNode) ? getNextNode().handle(
				request) : handledByThisNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#handleInterrupt()
	 */
	@Override
	public void handleInterrupt()
	{
		processor.handleInterrupt();
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the processor property.
	 * 
	 * @return the processor
	 */
	final RequestProcessor getProcessor()
	{
		return processor;
	}
}
