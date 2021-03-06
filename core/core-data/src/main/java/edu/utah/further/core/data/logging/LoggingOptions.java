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
package edu.utah.further.core.data.logging;

import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A JavaBean that holds logging options used by multiple objects in this package.
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
 * @version Jun 29, 2011
 */
public final class LoggingOptions extends ImmutableLoggingOptions
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= GET/SET ===================================

	/**
	 * Set a new value for the escapeQuotes property.
	 *
	 * @param escapeQuotes
	 *            the escapeQuotes to set
	 */
	public void setEscapeQuotes(final boolean escapeQuotes)
	{
		this.escapeQuotes = escapeQuotes;
	}

	/**
	 * Set a new value for the connectionUrls property.
	 *
	 * @param connectionUrls
	 *            the connectionUrls to set
	 */
	public void setConnectionUrls(final List<String> connectionUrls)
	{
		CollectionUtil.setListElements(this.connectionUrls, connectionUrls);
	}
}
