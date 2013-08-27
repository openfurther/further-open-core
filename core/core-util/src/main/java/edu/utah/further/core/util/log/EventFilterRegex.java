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
package edu.utah.further.core.util.log;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.spi.LoggingEvent;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.regex.RegExUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A useful decorator that filters messages based on regular expression matching.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 28, 2011
 */
public final class EventFilterRegex implements EventFilter
{
	// ========================= FIELDS ====================================

	/**
	 * List of regular expressions of fully-qualified bean class names to be auto-proxied.
	 */
	private final List<Pattern> includePatterns = CollectionUtil.newList();

	// ========================= IMPL: EventFilter =========================

	/**
	 * @param event
	 * @return
	 * @see edu.utah.further.core.util.log.EventFilter#isLoggable(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public boolean isLoggable(final LoggingEvent event)
	{
		return RegExUtil.matchesOneOf(StringUtil.getNullSafeToString(event.getMessage()),
				includePatterns);
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the includePatterns property.
	 *
	 * @param includePatterns
	 *            the includePatterns to set
	 */
	public void setIncludePatterns(final List<Pattern> includePatterns)
	{
		CollectionUtil.setListElements(this.includePatterns, includePatterns);
	}
}
