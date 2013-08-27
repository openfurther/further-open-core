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
package edu.utah.further.core.util.concurrent;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Thread factory for providing useful thread names.
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
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Feb 9, 2011
 */
public final class NamedThreadFactory implements ThreadFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(NamedThreadFactory.class);

	// ========================= FIELDS ======================================

	/**
	 * The name of the threads for this factory
	 */
	private final String name;

	/**
	 * The thread num
	 */
	private int threadNum = 1;

	/**
	 * Lock
	 */
	private final Object lock = new Object();

	// ========================= CONSTRUCTORS ================================

	/**
	 * @param name
	 */
	public NamedThreadFactory(final String name)
	{
		super();
		this.name = name;
	}

	// ========================= IMPL: ThreadFactory =========================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(final Runnable r)
	{
		final Thread thread = new Thread(r, name + threadNum);
		synchronized (lock)
		{
			// Critical section
			threadNum++;
		}
		return thread;
	}

	// ========================= METHODS =====================================

	/**
	 * Adds a future to the {@link List} of Link {@link Future}'s kept in a request.
	 *
	 * @param future
	 * @param request
	 * @param futuresAttributeName
	 *            name of request attribute that keeps future object list
	 */
	public static void addFuture(final Future<?> future, final ChainRequest request,
			final String futuresAttributeName)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Adding Link Future to request");
		}
		List<Future<?>> futures = request.getAttribute(futuresAttributeName);
		if (futures == null)
		{
			futures = CollectionUtil.newList();
		}
		futures.add(future);
		request.setAttribute(futuresAttributeName, futures);
		if (log.isDebugEnabled())
		{
			log.debug("Current Link futures: " + futures);
		}
	}
}
