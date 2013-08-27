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
package edu.utah.further.ds.api.advice;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * A generic decorator of any {@link RequestProcessor} that require advising.
 * <p>
 * To advice a request processor:
 * <ol>
 * <li>Extend this class for each {@link RequestProcessor} method annotation type that is
 * used by {@link RequestProcessor} aspects.</li>
 * <li>Add the necessary annotation required by the advising aspect to your original
 * request processor.</li>
 * <li>Add a bean definition of this decorator's sub-class from step 1 that wraps the
 * original request processor to the Spring context.</li>
 * </ol>
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
 * @version Apr 16, 2010
 */
@Deprecated
public abstract class AbstractAdvisableDelegatingRequestProcessor implements
		RequestProcessor
{
	// ========================= FIELDS =================================

	/**
	 * The delegate chain request processor.
	 */
	private RequestProcessor requestProcessor;

	// ========================= CONSTRUCTORS =================================

	/**
	 * @param requestProcessor
	 */
	public AbstractAdvisableDelegatingRequestProcessor(
			final RequestProcessor requestProcessor)
	{
		this.requestProcessor = requestProcessor;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return requestProcessor.getName();
	}

	// ========================= GETTERS & SETTERS ============================

	/**
	 * @return the requestProcessor
	 */
	public RequestProcessor getRequestProcessor()
	{
		return requestProcessor;
	}

	/**
	 * @param requestProcessor
	 *            the requestProcessor to set
	 */
	public void setRequestProcessor(final RequestProcessor requestProcessor)
	{
		this.requestProcessor = requestProcessor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestProcessor#getRequestHandler()
	 */
	@Override
	public RequestHandler getRequestHandler()
	{
		return getRequestProcessor().getRequestHandler();
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
		getRequestProcessor().setRequestHandler(requestHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return getRequestProcessor().getName();
	}

	/**
	 * @param depth
	 * @return
	 * @see edu.utah.further.core.api.chain.IndentedPrintable#toString(int)
	 */
	@Override
	public String toString(final int depth)
	{
		return getRequestProcessor().toString(depth);
	}

}
