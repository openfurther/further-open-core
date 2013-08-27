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
package edu.utah.further.core.util.lang;

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * Testing JVM memory utilities.
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
 * @version Aug 26, 2010
 */
public final class UTestMemoryUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestMemoryUtil.class);

	// ========================= METHODS ===================================

	/**
	 * Print basic memory statistics using {@link Runtime}.
	 */
	@Test
	public void printBasicMemoryStats()
	{
		if (log.isInfoEnabled())
		{
			log.info("Basic memory statistics:" + MemoryUtil.printBasicMemoryStats());
		}
	}

	/**
	 * Print more elaborate memory management statistics.
	 */
	@Test
	public void printMemoryManagementStats()
	{
		if (log.isInfoEnabled())
		{
			log.info("Before:" + MemoryUtil.printBasicMemoryStats());
			log.info("Before:" + MemoryUtil.printMemoryManagementStats());
		}
		MemoryUtil.printMemoryManagementStats();

		// Tweak memory a little bit
		for (int i = 0; i < 1000000; i++)
		{
			@SuppressWarnings("unused")
			final String s = "My String " + i;
		}

		if (log.isInfoEnabled())
		{
			log.info("After:" + MemoryUtil.printBasicMemoryStats());
			log.info("After:" + MemoryUtil.printMemoryManagementStats());
		}
	}

}
