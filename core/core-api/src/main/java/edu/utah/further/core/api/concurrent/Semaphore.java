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


/**
 * A semaphore for sending signals between threads. See
 * http://tutorials.jenkov.com/java-concurrency/semaphores.html
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
 * @version Sep 25, 2010
 */
public final class Semaphore
{
	// ========================= CONSTANTS =================================

	// /**
	// * A logger that helps identify this class' printouts.
	// */
	// private static final Logger log = getLogger(Semaphore.class);

	// ========================= FIELDS ====================================

	/**
	 * Stores the information of when we send the signal.
	 */
	private boolean signal = false;

	// ========================= METHODS ===================================

	/**
	 * Send signal.
	 */
	public synchronized void take()
	{
		// if (log.isDebugEnabled())
		// {
		// log.debug("Sending signal");
		// }
		this.signal = true;
		this.notify();
	}

	/**
	 * Receive signal.
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void release() throws InterruptedException
	{
		while (!this.signal)
		{
			// if (log.isDebugEnabled())
			// {
			// log.debug("Waiting");
			// }
			wait();
		}
		// if (log.isDebugEnabled())
		// {
		// log.debug("Received signal");
		// }
		this.signal = false;
	}

}
