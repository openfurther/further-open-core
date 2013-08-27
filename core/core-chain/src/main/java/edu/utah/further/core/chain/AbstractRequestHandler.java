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

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessage;
import edu.utah.further.core.api.visitor.Visitable;

/**
 * A base class for handlers in a Classic CoR (Chain of Responsibility Pattern), i.e., the
 * request is handled by only one of the handlers in the chain at a time, and it decides
 * where to forward the request next. This class cannot be instantiated and should be used
 * in conjunction with {@link RequestHandlerChainImpl}.
 * <p>
 * This class' sole purpose is to deal with chain processing logic (linear flow,
 * branching, looping, etc.); it delegates to a {@link RequestProcessor} to do the
 * business logic of processing the request.
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
public abstract class AbstractRequestHandler implements RequestHandler,
		Visitable<AbstractRequestHandler, RequestHandlerVisitor>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractRequestHandler.class);

	// ========================= FIELDS ====================================

	/**
	 * The current node in the chain
	 */
	private RequestHandler currNode = null;

	/**
	 * The next node in the chain..
	 */
	private RequestHandler nextNode = null;

	/**
	 * The prev node in the chain..
	 */
	private RequestHandler prevNode = null;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return RequestHandlerPrinter.print(this);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestHandler#toString(int)
	 */
	@Override
	public final String toString(final int depth)
	{
		return RequestHandlerPrinter.print(this, depth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestHandler#getCurrentNode()
	 */
	@Override
	public synchronized RequestHandler getCurrentNode()
	{
		return currNode;
	}

	/**
	 * Sets the current node in the chain
	 * 
	 * @param currNode
	 */
	protected synchronized void setCurrentNode(final RequestHandler currNode)
	{
		this.currNode = currNode;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestHandler#getNextNode()
	 */
	@Override
	public synchronized RequestHandler getNextNode()
	{
		currNode = nextNode;
		return nextNode;
	}

	/**
	 * Set the next node in the chain
	 * 
	 * @param nextNode
	 *            the nextNode to set
	 */
	@Override
	public final synchronized void setNextNode(final RequestHandler nextNode)
	{
		this.nextNode = nextNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestHandler#getPrevNode()
	 */
	@Override
	public synchronized RequestHandler getPrevNode()
	{
		return prevNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestHandler#setPrevNode(edu.utah.further.core
	 * .api.chain.RequestHandler)
	 */
	@Override
	public synchronized void setPrevNode(final RequestHandler prevNode)
	{
		this.prevNode = prevNode;
	}

	/**
	 * Returns the name of this handler. Defaults to class name. (non-Javadoc)
	 * 
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestHandler#handleInterrupt()
	 */
	@Override
	public void handleInterrupt()
	{
		// Override this method if you need to handle an interrupt
		log.debug("Handler " + getName() + " has been interrupted");
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a string that is prefixed to all message texts created by this handler.
	 * 
	 * @return
	 */
	protected String getMessagePrefix()
	{
		return getName() + " ";
	}

	/**
	 * Add a message to the list of messages of this object. This is the preferred way of
	 * adding messages to a request in this handler.
	 * 
	 * @param request
	 *            request to add message to
	 * @param severity
	 *            message severity level
	 * @param text
	 *            message text
	 */
	protected void addMessage(final ChainRequest request, final Severity severity,
			final String text)
	{
		request.addMessage(new SeverityMessage(severity, getMessagePrefix() + text));
	}
}
