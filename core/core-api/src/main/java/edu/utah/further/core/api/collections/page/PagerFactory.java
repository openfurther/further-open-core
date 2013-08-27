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

import edu.utah.further.core.api.context.Labeled;

/**
 * Instantiates pager objects. Supports only iterators for list objects (@{link
 * Labeled#LIST{). Override {@link #pager(Labeled, Object)} in other modules to support
 * other iterable types.
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
public interface PagerFactory
{
	// ========================= METHODS ===================================

	/**
	 * Return a paging iterator with the default page strategy and page size
	 * {@link #DEFAULT_PAGE_SIZE}.
	 *
	 * @param <E>
	 * @param iterableType
	 * @param iterable
	 * @return
	 */
	Pager<?> pager(Object iterable, Labeled iterableType);

	/**
	 * Return a paging iterator with a default page strategy and a custom page size.
	 *
	 * @param <E>
	 * @param iterableType
	 * @param iterable
	 * @param pageSize
	 * @return
	 */
	Pager<?> pager(Object iterable, Labeled iterableType, int pageSize);

	/**
	 * Return a paging iterator of some type of an iterable object. Only selected object
	 * types (suitable for result set paging in the FQE/DS flow) are supported.
	 *
	 * @param iterable
	 *            an iterable object (e.g. List, StAX stream)
	 * @param pagingStrategy
	 *            determines iterant (page) size
	 * @return page iterator
	 */
	Pager<?> pager(Object iterable, PagingStrategy pagingStrategy);

	// ========================= PRIVATE METHODS ===========================
}
