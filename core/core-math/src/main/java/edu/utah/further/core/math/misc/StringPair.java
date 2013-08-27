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
package edu.utah.further.core.math.misc;

import edu.utah.further.core.api.context.Api;

/**
 * A pair of strings. Useful for hash tables (e.g. OSGi service locator tables) that
 * depend on two hash columns as the table's composite key.
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
 * @version May 29, 2009
 */
@Api
public final class StringPair extends ComparablePair<String, String>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * 
	 */
	private static final long serialVersionUID = -4399491620166307603L;

	/**
	 * Create a pair of strings.
	 * 
	 * @param left
	 *            left member
	 * @param right
	 *            right member
	 */
	public StringPair(final String left, final String right)
	{
		super(left, right);
	}

	// ========================= GETTERS & SETTERS =========================

}
