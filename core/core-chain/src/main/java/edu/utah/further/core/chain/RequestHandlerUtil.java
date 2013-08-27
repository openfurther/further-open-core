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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * Utility class for common {@link RequestHandler} operations.
 * 
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
 * @version Feb 4, 2010
 */
public final class RequestHandlerUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(RequestHandlerUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Private constructor
	 */
	private RequestHandlerUtil()
	{
		preventUtilityConstruction();
	}

	/**
	 * Injects a new processor into an already built {@link RequestHandler} so that the
	 * next processor is the one injected and the previous next will follow after the
	 * injected.
	 * 
	 * @param requestHandler
	 *            The RequestHandler to inject too
	 * @param processor
	 *            the processor to inject.
	 */
	public static final void injectNext(final RequestHandler requestHandler,
			final RequestProcessor processor)
	{
		final RequestHandler oldNextRequestHandler = requestHandler.getNextNode();
		final RequestHandler newNextRequestHandler = RequestHandlerBuilder
				.simple(processor);

		if (log.isTraceEnabled())
		{
			debugPrintAndCenter(log, "injectNext()");
			log.trace("requestHandler     " + requestHandler);
			log.trace("Old next node      " + requestHandler.getNextNode());
			log.trace("New next node      " + newNextRequestHandler);
		}

		/* Inject the processor */
		requestHandler.setNextNode(newNextRequestHandler);
		newNextRequestHandler.setNextNode(oldNextRequestHandler);

		if (log.isTraceEnabled())
		{
			log.trace("INJECTING");
			log.trace("New next node      " + requestHandler.getNextNode());
			Validate.isTrue(requestHandler.getNextNode() == newNextRequestHandler);
			Validate.isTrue(requestHandler.getNextNode() != requestHandler);
		}
	}

	/**
	 * Injects a new processor into an already built {@link RequestHandler} so that the
	 * current processor is the one injected and the next processor will be the previous
	 * current processor.
	 * 
	 * @param handler
	 *            The RequestHandler to inject too
	 * @param processor
	 *            the processor to inject.
	 * @return the new RequestHandler representing the current with a pointer to the next
	 */
	public static final RequestHandler injectCurrent(final RequestHandler requestHandler,
			final RequestProcessor processor)
	{
		final RequestHandler newCurrentRequestHandler = RequestHandlerBuilder
				.simple(processor);

		newCurrentRequestHandler.setNextNode(requestHandler);

		return newCurrentRequestHandler;
	}

}
