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
package edu.utah.further.dts.api.to;

import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;

/**
 * A generic DTS data transfer object.
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
 * @version May 13, 2009
 */
public interface DtsDataTo extends DtsData, CopyableFrom<DtsData, DtsDataTo>
{
	// ========================= METHODS ===================================

	/**
	 * Set a new value for the id property.
	 *
	 * @param id
	 *            the id to set
	 */
	@Override
	void setId(int id);

	/**
	 * Set a new value for the name property.
	 *
	 * @param name
	 *            the name to set
	 */
	@Override
	void setName(String name);

	/**
	 * Set a new value for the code property.
	 *
	 * @param code
	 *            the code to set
	 */
	@Override
	void setCode(String code);

	/**
	 * Set a new value for the type property.
	 *
	 * @param type
	 *            the type to set
	 */
	void setType(DtsDataType type);
}