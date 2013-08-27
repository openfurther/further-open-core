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
package edu.utah.further.core.api.collections;

import java.util.Map;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.context.Valued;

/**
 * A name-value pair. Useful in terminology servers and web applications (a request
 * parameter).
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
 * @version Feb 4, 2009
 * @see Map#entrySet()
 */
public interface Parameter extends Named, Valued<String>
{
	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * Returns the name (key) of this parameter.
	 * 
	 * @return parameter name
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	String getName();

	// ========================= IMPLEMENTATION: Valued ====================

	/**
	 * Returns the value of this parameter.
	 * 
	 * @return parameter value
	 * @see edu.utah.further.core.api.context.Valued#getValue()
	 */
	@Override
	String getValue();
}