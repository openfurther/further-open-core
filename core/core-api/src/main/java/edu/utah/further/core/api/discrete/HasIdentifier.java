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
package edu.utah.further.core.api.discrete;

import java.io.Serializable;

import edu.utah.further.core.api.context.Api;

/**
 * An identifiable object. Parameterized by the identifier type.
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
 * @version Oct 13, 2008
 */
@Api
public interface HasIdentifier<ID extends Comparable<ID> & Serializable>
{
	// ========================= METHODS ===================================

	/**
	 * Returns the identifier of this entity. In most cases, it is {@link Long}. Also, in
	 * most cases, this is the <i>unique</i> identifier of this object, although this is
	 * not mandatory.
	 * 
	 * @return the identifier of this entity. Must be {@link Comparable}
	 */
	ID getId();
}
