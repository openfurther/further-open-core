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

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.Labeled;

/**
 * Determines a page size while iterating over an object with a paging iterator.
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
 * @version Jul 28, 2010
 */
public interface PagingStrategy
{
	// ========================= CONSTANTS =================================

	/**
	 * Default page size, an arbitrary value for now.
	 */
	int DEFAULT_PAGE_SIZE = 10;

	/**
	 * Value of maxResults that allows an unlimited number of iterants to be retrieved.
	 */
	int NO_LIMIT = Constants.INVALID_VALUE_INTEGER;

	// ========================= METHODS ===================================

	/**
	 * Return the size of the next page in the iteration.
	 *
	 * @param <E>
	 *            iterable object's element type
	 * @param pageNumber
	 *            next page's number
	 * @param pageIndex
	 *            next page's first element's index in the entire iteration
	 * @param oldPageSize
	 *            previous page size
	 * @return next page size
	 */
	<E> int getNextPageSize(int pageNumber, int pageIndex, int oldPageSize);

	/**
	 * Return the maximum number of iterants to retrieve. If set to
	 * {@link PagerFactory#NO_LIMIT}, all iterants are retrieved.
	 *
	 * @return the maximum number of iterants to retrieve
	 */
	int getMaxIterants();

	/**
	 * Return the target iterable object's type.
	 *
	 * @return the target iterable object's type
	 */
	Labeled getIterableType();
}
