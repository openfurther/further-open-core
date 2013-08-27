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
package edu.utah.further.core.data.hibernate.page;

import java.util.Iterator;

import org.hibernate.ScrollableResults;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.UnmodifiableIterator;

/**
 * Adapts an {@link ScrollableResults} to a paging {@link Iterator}. Assumes that a single
 * root entity returned from {@link ScrollableResults#get()}.
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
@Deprecated
final class ScrollableResultsIterator extends UnmodifiableIterator<PersistentEntity<?>>
{
	// ========================= CONSTANTS =============================

	// ========================= FIELDS ====================================

	/**
	 * Hibernate result set to iterate on.
	 */
	private final ScrollableResults iterable;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterable
	 * @param pagingStrategy
	 */
	public ScrollableResultsIterator(final ScrollableResults iterable)
	{
		super();
		this.iterable = iterable;
	}

	// ========================= Impl: Iterator<Object> ====================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		try
		{
			final boolean hasNext = iterable.next();
			if (hasNext)
			{
				iterable.previous();
			}
			return hasNext;
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public PersistentEntity<?> next()
	{
		iterable.next();
		// Return a single root entity
		return (PersistentEntity<?>) iterable.get(0);
	}
}
