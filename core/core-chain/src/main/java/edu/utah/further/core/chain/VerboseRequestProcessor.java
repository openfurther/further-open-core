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

/**
 * A utility decorator of a {@link AbstractRequestHandler} that prints the request before
 * and after handling it.
 * <p>
 * TODO: move to an AOP aspect in the future.
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
 * @version Sep 28, 2009
 */
public final class VerboseRequestProcessor implements RequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(VerboseRequestProcessor.class);

	// ========================= FIELDS ====================================

	/**
	 * The request processor object to decorate.
	 */
	private final RequestProcessor processor;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Decorate a request processor.
	 * 
	 * @param processor
	 *            processor to wrap
	 */
	public VerboseRequestProcessor(final RequestProcessor processor)
	{
		super();
		this.processor = processor;
	}

	// ========================= IMPLEMENTATION: RequestProcessor ==========

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public final boolean process(final ChainRequest request)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Before handling request by " + getName() + ": " + request);
		}
		final boolean handledByThisNode = this.process(request);
		if (log.isDebugEnabled())
		{
			log.debug("After  handling request by " + getName() + ": " + request);
		}
		return handledByThisNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestProcessor#handleInterrupt()
	 */
	@Override
	public void handleInterrupt()
	{
		log.debug("Request processor has been interrupted, terminating");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestProcessor#getRequestHandler()
	 */
	@Override
	public RequestHandler getRequestHandler()
	{
		/* Delegate to wrapped processor */
		return processor.getRequestHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#setRequestHandler(edu.utah.further
	 * .core.api.chain.RequestHandler)
	 */
	@Override
	public void setRequestHandler(final RequestHandler requestHandler)
	{
		/* Delegate to wrapped processor */
		processor.setRequestHandler(requestHandler);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return processor.getName() + " - verbose";
	}

	/**
	 * @param depth
	 * @return
	 * @see edu.utah.further.core.api.chain.IndentedPrintable#toString(int)
	 */
	@Override
	public String toString(final int depth)
	{
		return processor.toString(depth);
	}

	// ========================= GETTERS & SETTERS =========================
}
