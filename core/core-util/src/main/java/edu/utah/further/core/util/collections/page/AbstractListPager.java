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
package edu.utah.further.core.util.collections.page;

import java.util.List;

import edu.utah.further.core.api.collections.page.PagingStrategy;

/**
 * A convenient base class for all pagers that output element lists as their page type.
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
 * @version Sep 29, 2010
 */
public abstract class AbstractListPager<E> extends AbstractPager<List<E>>
{
	// ========================= FIELDS ====================================

	/**
	 * Beginning list index of the next iterant (sub-list).
	 */
	private int pageIndex = 0;

	/**
	 * Page counter.
	 */
	private int pageNumber = 0;

	/**
	 * Holds the size of the next page to retrieve.
	 */
	private int pageSize;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterable
	 * @param pagingStrategy
	 */
	protected AbstractListPager(final PagingStrategy pagingStrategy)
	{
		super(pagingStrategy);
	}

	// ========================= IMPL: Iterator<List<E>> ===================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.util.collections.page.AbstractPager#next()
	 */
	@Override
	public final List<E> next()
	{
		pageSize = getPagingStrategy().getNextPageSize(pageNumber, pageIndex, pageSize);
		final int nextPageIndex = pageIndex + pageSize;
		final List<E> page = getPage(pageSize);
		pageIndex = nextPageIndex;
		return page;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a view of the page of the iterable object starting at the current iterant
	 * returned from the object's iterator and of size <code>currentPageSize</code> (or
	 * smaller, if the object iteration ends before the page size).
	 *
	 * @param currentPageSize
	 *            maximum page size
	 */
	abstract protected List<E> getPage(final int currentPageSize);
}
