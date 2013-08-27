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
package edu.utah.further.core.api.lang;

import edu.utah.further.core.api.context.Api;

/**
 * A builder of an object. Follows the builder pattern on an abstract scale.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see http://en.wikipedia.org/wiki/Builder_pattern
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Api
public interface Builder<T>
{
	// ========================= METHODS ===================================

	/**
	 * Builds an object. Methods previously called on the builder are used to set
	 * parameters, validate them (including validation rules that involve multiple
	 * parameters), and then this method is called to construct the target object. This
	 * method may throw an exception if construction fails (e.g. due to a validation rule
	 * that involves ALL parameters).
	 *
	 * @return the target object. Must be fully constructed and valid. Note that the
	 *         returned object may be a sub-class of <T>
	 */
	T build();
}
