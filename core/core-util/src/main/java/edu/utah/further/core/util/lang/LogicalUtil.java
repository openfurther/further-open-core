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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;

/**
 * Utilities related to logical expressions and assertions.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Utility
@Api
public final class LogicalUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private LogicalUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Does <code>a</code> imply <code>b</code>. This is equivalent to
	 * <code>!a || b</code>.
	 * 
	 * @param a
	 *            reason
	 * @param b
	 *            result
	 * @return true if and only if code>a</code> implies <code>b</code>
	 */
	public static boolean implies(final boolean a, final boolean b)
	{
		return !a || b;
	}
}
