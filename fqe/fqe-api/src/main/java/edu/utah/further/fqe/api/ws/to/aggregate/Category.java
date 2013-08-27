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
package edu.utah.further.fqe.api.ws.to.aggregate;

import java.util.Map;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * Holds a histogram of counts for a single demographic category.
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
 * @version Mar 23, 2011
 */
public interface Category extends Named, PubliclyCloneable<Category>,
		CopyableFrom<Category, Category>, Comparable<Category>
{
	// ========================= METHODS ===================================

	/**
	 * Return the categories property.
	 *
	 * @return the categories
	 */
	Map<String, Long> getEntries();

	/**
	 * @param aKey
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	Long getEntry(String aKey);

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	Long addEntry(String aKey, Long value);

	/**
	 * @param aKey
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	Long removeEntry(String aKey);

	/**
	 *
	 * @see java.util.Map#clear()
	 */
	void clear();

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	int getSize();
}