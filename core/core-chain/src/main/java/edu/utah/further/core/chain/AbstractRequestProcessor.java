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

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * A convenient base class for processors which provides default implementations of
 * {@link RequestProcessor} methods which subclasses may or may not choose to override.
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
 * @version Feb 3, 2010
 */
public abstract class AbstractRequestProcessor implements RequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractRequestProcessor.class);

	// ========================= FIELDS ====================================

	/**
	 * The request handler this processor is attached to.
	 */
	protected RequestHandler requestHandler;

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

	// ========================= IMPLEMENTATION: RequestProcessor ==========

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestHandler#toString(int)
	 */
	@Override
	public String toString(final int depth)
	{
		return toString();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		// Default to class name
		// return getClass().getName();
		// Shorter
		return getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestProcessor#getRequestHandler()
	 */
	@Override
	public RequestHandler getRequestHandler()
	{
		return requestHandler;
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
		this.requestHandler = requestHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.chain.RequestProcessor#handleInterrupt()
	 */
	@Override
	public void handleInterrupt()
	{
		log.debug("handleInterrupt() for processor " + getName());
	}

	// ========================= PRIVATE METHODS ===========================
}
