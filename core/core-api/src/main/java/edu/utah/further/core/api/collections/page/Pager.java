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
package edu.utah.further.core.api.collections.page;

import java.util.Iterator;

/**
 * A pager that loops over an iterable objects and output pages instead of individual
 * elements.
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
 * @version Oct 1, 2010
 */
public interface Pager<E> extends Iterator<E>
{
	// ========================= METHODS ===================================

	/**
	 * Return the pagingStrategy property.
	 *
	 * @return the pagingStrategy
	 */
	PagingStrategy getPagingStrategy();

	/**
	 * Return the number of valid iterants output from this pager so far.
	 *
	 * @return iterant counter
	 */
	int getIterantCounter();

	/**
	 * Return the totalIterantCounter property.
	 *
	 * @return the totalIterantCounter
	 */
	int getTotalIterantCounter();

	/**
	 * Set a new value for the numHeaderRows property.
	 *
	 * @param numHeaderRows the numHeaderRows to set
	 */
	void setNumHeaderRows( int numHeaderRows);
	
	/**
	 * Clean up and free any resources allocated by this pager
	 */
	void close();
}
