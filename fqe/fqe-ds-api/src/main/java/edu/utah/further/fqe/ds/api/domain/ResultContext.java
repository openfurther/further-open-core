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
package edu.utah.further.fqe.ds.api.domain;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * A {@link QueryContext}'s result set information placeholder. Allows to decouple the
 * details of the query context and result set data objects.
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
 * @version Dec 7, 2009
 */
public interface ResultContext extends PersistentEntity<Long>,
		CopyableFrom<ResultContext, ResultContext>, HasNumRecords
{
	// ========================= CONSTANTS ===================================

	public static int ACCESS_DENIED = -0xBAD;
	
	// ========================= METHODS ===================================

	/**
	 * Return the rootEntityClass property.
	 *
	 * @return the rootEntityClass
	 */
	String getRootEntityClass();

	/**
	 * Returns a String representing the fully qualified transfer object class
	 *
	 * @return a string representing this result set's transfer object.
	 */
	String getTransferObjectClass();

	/**
	 * Return the raw query result type.
	 *
	 * @return the result
	 */
	Object getResult();

	/**
	 * Set a new value for the resultEntity property.
	 *
	 * @param resultEntity
	 *            the resultEntity to set
	 */
	void setResult(Object result);
}