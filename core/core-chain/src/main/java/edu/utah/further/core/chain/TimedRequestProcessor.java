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
import org.springframework.util.StopWatch;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * A utility decorator of a {@link RequestProcessor} that times the request processing
 * time.
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
public final class TimedRequestProcessor implements RequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(TimedRequestProcessor.class);

	// ========================= FIELDS ====================================

	/**
	 * The request processor object to decorate.
	 */
	private final RequestProcessor processor;

	/**
	 * Records processing time.
	 */
	private double elapsedTime;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param processor
	 */
	public TimedRequestProcessor(final RequestProcessor processor)
	{
		super();
		this.processor = processor;
	}

	// ========================= IMPLEMENTATION: RequestProcessor ==========

	/**
	 * Starting point of the chain, called by client or pre-node. Call
	 * <code>handle()</code> on this node, and decide whether to continue the chain. If
	 * the next node is not null and this node did not handle the request, call
	 * <code>start()</code> on next node to handle request.
	 * 
	 * @param request
	 *            the request object
	 * @return a boolean indicates whether this handler fully handled the request
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public final boolean process(final ChainRequest request)
	{
		final StopWatch watch = new StopWatch();
		watch.start();
		final boolean handledByThisNode = processor.process(request);
		watch.stop();

		// Returns the average elapsed time of all calls to the handle() code
		// since the application comes up
		// this.elapsedTime = report.getAverageTime(); // report.getTotalTime();
		// logger.debug("Average analysis time (Stopwatch): "
		// + report.getAverageTime());

		this.elapsedTime = watch.getTotalTimeSeconds();
		return handledByThisNode;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return processor.getName() + " - timed";
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

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime()
	{
		return elapsedTime;
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
	 * @param depth
	 * @return
	 * @see edu.utah.further.core.api.chain.IndentedPrintable#toString(int)
	 */
	@Override
	public String toString(final int depth)
	{
		return processor.toString(depth);
	}

}
