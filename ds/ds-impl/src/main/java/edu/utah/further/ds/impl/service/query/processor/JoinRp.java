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

import static edu.utah.further.ds.api.util.AttributeName.FUTURES_LINK;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;

/**
 * A request processor to join asynchronous threads. This would typically be used as a
 * final processor (although it doesn't terminate the chain) or in a post loop. Currently
 * for use with {@link LinkerPersistentQp}
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
 * @version Oct 20, 2010
 */
public final class JoinRp extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS ===================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(JoinRp.class);

	// ========================= FIELDS ====================================

	/**
	 * Name of request attribute holding the future objects.
	 */
	private String attributeName = FUTURES_LINK.getLabel();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		Validate.notNull(attributeName, "A futures' request attribute name must be set");
	}

	// ========================= Impl: RequestHandler ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		join(request, attributeName);
		return false;
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the attributeName property.
	 *
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(final String attributeName)
	{
		this.attributeName = attributeName;
	}

	// ========================= PRIVATE STATIC ==============================

	/**
	 * Private helper method to join an asynchronous thread based on no result
	 * {@link Future}'s. This ensures synchronous
	 *
	 * @param request
	 * @param attributeName
	 */
	private static final void join(final ChainRequest request, final String attributeName)
	{
		final List<Future<?>> futures = request.getAttribute(attributeName);
		if (futures == null)
		{
			log.warn("Encountered empty future list in Join Request Processor");
			return;
		}

		for (final Future<?> future : futures)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Found future in Joining Request Processor in state "
						+ future.isDone());
			}

			if (!future.isDone())
			{
				log.debug("Future object is not done, will wait");
				boolean done = false;
				while (!done)
				{
					try
					{
						// Get will block until it is complete
						future.get();
						done = true;
					}
					catch (final InterruptedException e)
					{
						log.error("Interrupted!", e);
					}
					catch (final ExecutionException e)
					{
						log.error("Execution exeception on future object", e);
						throw new ApplicationException(
								"Future execution failed due to exeception will executing future",
								e);
					}
				}

			}
		}

		// Clean up
		request.removeAttribute(attributeName);
	}
}
