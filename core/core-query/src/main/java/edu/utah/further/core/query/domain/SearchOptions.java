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
package edu.utah.further.core.query.domain;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * Extra options passed to a {@link SearchCriterion} beyond its basic search type.
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
 * @version Apr 21, 2009
 */
@Api
public interface SearchOptions extends CopyableFrom<SearchOptions, SearchOptions>
{
	// ========================= IMPL: Object ==============================

	/**
	 * Two {@link SearchOptions} objects must be equal <i>if and only if</i> their hash
	 * codes are equal. This is important because a dependent object (e.g. a query
	 * context) may decide whether to deep-copy its search query field based on hash code
	 * equality.
	 *
	 * @return hash code
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	int hashCode();

	// ========================= METHODS ===================================

	/**
	 * Return the matchType property.
	 *
	 * @return the matchType
	 */
	MatchType getMatchType();

	/**
	 * Set a new value for the matchType property.
	 *
	 * @param matchType
	 *            the matchType to set
	 */
	SearchOptions setMatchType(MatchType matchType);

	/**
	 * Return the escapeChar property.
	 *
	 * @return the escapeChar
	 */
	Character getEscapeChar();

	/**
	 * Set a new value for the escapeChar property.
	 *
	 * @param escapeChar
	 *            the escapeChar to set
	 */
	SearchOptions setEscapeChar(Character escapeChar);

	/**
	 * Return the ignoreCase property.
	 *
	 * @return the ignoreCase
	 */
	Boolean isIgnoreCase();

	/**
	 * Set a new value for the ignoreCase property.
	 *
	 * @param ignoreCase
	 *            the ignoreCase to set
	 */
	SearchOptions setIgnoreCase(Boolean ignoreCase);
}