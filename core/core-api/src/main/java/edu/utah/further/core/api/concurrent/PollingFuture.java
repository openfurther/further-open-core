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
package edu.utah.further.core.api.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.utah.further.core.api.context.Api;

/**
 * A Java Concurrency Library {@link Future} represents the result of an asynchronous
 * computation. {@link PollingFuture} adds functionality to periodically poll the result.
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
 * @version Feb 26, 2010
 */
@Api
public interface PollingFuture<V> extends Future<V>
{
	// ========================= METHODS ===================================

	/**
	 * Polls the tasks's result every so-often up to a timeout. The result is returned
	 * from the first polling period in which it is available, or <code>null</code> if it
	 * is not available. Similar to {@link Future#get(long, TimeUnit)} but does not throw
	 * any checked exceptions.
	 * 
	 * @param period
	 *            time to wait between consecutive polling
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @return the computed result, or <code>null</code> if it is not yet available or
	 *         there was a failure to get it
	 */
	V poll(long period, long timeout, TimeUnit unit);
}
