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
package edu.utah.further.core.api.time;

import static edu.utah.further.core.api.time.DurationBuilder.toMillis;
import static org.apache.commons.lang.time.DurationFormatUtils.formatDuration;
import static org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords;
import static org.apache.commons.lang.time.DurationFormatUtils.*;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.text.StringUtil;

/**
 * Test time conversion and formatting utilities.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 6, 2008
 */
public final class UTestTimeUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestTimeUtil.class);

	// ========================= METHODS ===================================

	/**
	 * Format a time duration in period ISO format.
	 */
	@Test
	public void formatDurationInVariousFormats()
	{
		printInVariousFormats(toMillis(1l, 2, 3, 4, 5));
		printInVariousFormats(toMillis(0l, 2, 3, 4, 0));
		printInVariousFormats(toMillis(0l, 2, 3, 0, 0));
		printInVariousFormats(toMillis(0l, 2, 80, 0, 0));
		printInVariousFormats(toMillis(0l, 0, 0, 3, 0));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param durationMillis
	 */
	private void printInVariousFormats(final long durationMillis)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Duration [ms]: " + durationMillis);
			log.debug("Period ISO: " + formatDurationISO(durationMillis));
			log.debug("Period: " + formatDuration(durationMillis, "HH:mm:ss"));
			log.debug("Period: " + formatDuration(durationMillis, "HH:mm:ss", false));
			log.debug("Words: " + formatDurationWords(durationMillis, true, true));
			log.debug(StringUtil.repeat("=", 60));
		}
	}
}
