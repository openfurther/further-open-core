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
package edu.utah.further.mdr.api.service.uml;

import edu.utah.further.core.api.context.Api;

/**
 * A JavaBean that centralizes XMI parser options.
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
 * @version Dec 5, 2008
 * @see XmiParser
 */
@Api
public interface XmiParserOptions
{
	// ========================= METHODS ===================================

	/**
	 * Return the checkNamingConventions property. If <code>true</code>, information
	 * messages are added for UML elements that do not adhere to naming conventions. By
	 * default, this flag is <code>true</code>.
	 *
	 * @return the checkNamingConventions
	 */
	boolean isCheckNamingConventions();

	/**
	 * Set a new value for the checkNamingConventions property. If <code>true</code>,
	 * information messages are added for UML elements that do not adhere to naming
	 * conventions.
	 *
	 * @param checkNamingConventions
	 *            the checkNamingConventions to set
	 */
	void setCheckNamingConventions(boolean checkNamingConventions);

	/**
	 * Return the debug property. If true, results are printed to the screen, otherwise
	 * the model is constructed. By default, this flag is <code>false</code>.
	 *
	 * @return the debug
	 */
	boolean isDebug();

	/**
	 * Set a new value for the debug property.
	 *
	 * @param debug
	 *            the debug to set. If true, results are printed to the screen, otherwise
	 *            the model is constructed.
	 */
	void setDebug(boolean debug);
}