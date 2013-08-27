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

import java.util.Iterator;
import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.message.ValidationUtil;

/**
 * A pager of a general iterable object. Loops over the object using its natural iteration
 * order.
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
 * @version Sep 22, 2010
 */
public class IterablePager<E> extends AbstractListPager<E>
{
	// ========================= FIELDS ====================================

	/**
	 * Element iterator delegate.
	 */
	private final Iterator<E> iterator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A factory method - instantiate pager from an iterable object.
	 *
	 * @param iterable
	 * @param pagingStrategy
	 */
	public static <E> IterablePager<E> newInstance(final Iterable<E> iterable,
			final PagingStrategy pagingStrategy)
	{
		ValidationUtil.validateNotNull("iterable", iterable);
		return newInstance(iterable.iterator(), pagingStrategy);
	}

	/**
	 * A factory method - instantiate pager from an iterable object's iterator.
	 *
	 * @param iterable
	 * @param pagingStrategy
	 */
	public static <E> IterablePager<E> newInstance(final Iterator<E> iterator,
			final PagingStrategy pagingStrategy)
	{
		return new IterablePager<>(iterator, pagingStrategy);
	}

	/**
	 * @param iterable
	 * @param pagingStrategy
	 */
	protected IterablePager(final Iterator<E> iterator,
			final PagingStrategy pagingStrategy)
	{
		super(pagingStrategy);
		ValidationUtil.validateNotNull("iterator", iterator);
		this.iterator = iterator;
	}

	// ========================= IMPL: Iterator<List<E>> ===================

	/**
	 * @return
	 * @see edu.utah.further.core.util.collections.page.AbstractPager#hasNextInternal()
	 */
	@Override
	protected boolean hasNextInternal()
	{
		return iterator.hasNext();
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
	@Override
	protected List<E> getPage(final int currentPageSize)
	{
		final List<E> page = CollectionUtil.newList();
		// Allow sub-classes to override -- don't use our private field here
		final Iterator<E> iter = getIterator();
		for (int i = 0; !isEndOfIteration() && (i < currentPageSize) && iter.hasNext(); i++)
		{
			incrementTotalIterantCounter();
			if (getTotalIterantCounter() > getNumHeaderRows())
			{
				page.add(iter.next());
				addSingleIterant();
			}
		}
		return page;
	}

	/**
	 * Return the iterator property.
	 *
	 * @return the iterator
	 */
	protected Iterator<E> getIterator()
	{
		return iterator;
	}
}
