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

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import edu.utah.further.core.api.collections.page.Pager;
import edu.utah.further.core.api.collections.page.PagingStrategy;

/**
 * A convenient base class for all pagers.
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
public abstract class AbstractPager<E> implements Pager<E>
{
	// ========================= FIELDS ====================================

	/**
	 * Valid iterant counter (since the beginning of the entire paging iteration).
	 */
	private int iterantCounter = 0;

	/**
	 * Valid iterant counter (since the beginning of the entire paging iteration; both bad
	 * and valid iterants).
	 */
	private int totalIterantCounter = 0;

	/**
	 * Determines page size at each iteration.
	 */
	private final PagingStrategy pagingStrategy;

	/**
	 * A flag indicating whether we are at the end of the iteration or not.
	 */
	private boolean endOfIteration = false;

	/**
	 * Number of header lines / entities to be ignored at the beginning of the input
	 * stream iterated over.
	 */
	private int numHeaderRows = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterable
	 * @param pagingStrategy
	 */
	protected AbstractPager(final PagingStrategy pagingStrategy)
	{
		super();
		this.pagingStrategy = pagingStrategy;
		if (pagingStrategy.getMaxIterants() == 0)
		{
			this.endOfIteration = true;
		}
	}

	// ========================= IMPL: Iterator<List<E>> ===================

	/**
	 * Has next determines whether or not another page exists <em>without</em> altering
	 * page state. If hasNext() returns true, the subsequent page can be retrieved from
	 * next().
	 *
	 * @return
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public final boolean hasNext()
	{
		return (isEndOfIteration() ? false : hasNextInternal());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public final void remove()
	{
		throw new UnsupportedOperationException(
				unsupportedMessage("Iterator is read-only"));
	}

	// ========================= IMPL: Pager<E> ============================

	/**
	 * Return the pagingStrategy property.
	 *
	 * @return the pagingStrategy
	 * @see edu.utah.further.core.api.collections.page.Pager#getPagingStrategy()
	 */
	@Override
	public PagingStrategy getPagingStrategy()
	{
		return pagingStrategy;
	}

	/**
	 * Return the iterantCounter property.
	 *
	 * @return the iterantCounter
	 * @see edu.utah.further.core.api.collections.page.Pager#getIterantCounter()
	 */
	@Override
	public int getIterantCounter()
	{
		return iterantCounter;
	}

	/**
	 * Return the totalIterantCounter property.
	 *
	 * @return the totalIterantCounter
	 * @see edu.utah.further.core.api.collections.page.Pager#getTotalIterantCounter()
	 */
	@Override
	public int getTotalIterantCounter()
	{
		return totalIterantCounter;
	}

	/**
	 * Set a new value for the numHeaderRows property.
	 *
	 * @param numHeaderRows
	 *            the numHeaderRows to set
	 * @see edu.utah.further.core.api.collections.page.Pager#setNumHeaderRows(int)
	 */
	@Override
	public void setNumHeaderRows(final int numHeaderRows)
	{
		this.numHeaderRows = (numHeaderRows < 0) ? 0 : numHeaderRows;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.collections.page.Pager#close()
	 */
	@Override
	public void close()
	{
		// Default nothing
	}
	

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set a new value for the totalIterantCounter property.
	 *
	 * @param totalIterantCounter
	 *            the totalIterantCounter to set
	 */
	public void setTotalIterantCounter(final int totalIterantCounter)
	{
		this.totalIterantCounter = totalIterantCounter;
	}

	/**
	 * A hook that returns the raw hasNext result before applying end-of-iteration to it
	 * in {@link #hasNext()}.
	 *
	 * @return does the backing iterable object have another iterant to offer
	 */
	abstract protected boolean hasNextInternal();

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.page.PagingStrategy#getMaxIterants()
	 */
	protected final int getMaxResults()
	{
		return pagingStrategy.getMaxIterants();
	}

	/**
	 * Return the endOfIteration property.
	 *
	 * @return the endOfIteration
	 */
	protected final boolean isEndOfIteration()
	{
		return endOfIteration;
	}

	/**
	 * Set a new value for the endOfIteration property.
	 *
	 * @param endOfIteration
	 *            the endOfIteration to set
	 */
	protected final void setEndOfIteration(final boolean endOfIteration)
	{
		this.endOfIteration = endOfIteration;
	}

	/**
	 * Increment the valid iterant counter.
	 *
	 * @param iterantCounter
	 *            the iterantCounter to set
	 */
	protected final void incrementIterantCounter()
	{
		this.iterantCounter++;
	}

	/**
	 * Increment the total iterant counter.
	 *
	 * @param iterantCounter
	 *            the iterantCounter to set
	 */
	protected final void incrementTotalIterantCounter()
	{
		this.totalIterantCounter++;
	}

	/**
	 * Return the numHeaderRows property.
	 *
	 * @return the numHeaderRows
	 */
	protected final int getNumHeaderRows()
	{
		return numHeaderRows;
	}

	// /**
	// * Set a new value for the iterantCounter property.
	// *
	// * @param iterantCounter
	// * the iterantCounter to set
	// */
	// protected final void setIterantCounter(final int iterantCounter)
	// {
	// Validate.isTrue(iterantCounter >= 0, "Iterant counter must be non-negative");
	// this.iterantCounter = iterantCounter;
	// }

	/**
	 * Update internal state: increment iterant counter and update end-of-iteration flag.
	 */
	protected final void addSingleIterant()
	{
		incrementIterantCounter();
		// Note that if maxResults = INVALID_VALUE_INTEGER, the following condition is
		// always false, so maxResults is effectively ignored, as it should be
		if (getIterantCounter() == getMaxResults())
		{
			setEndOfIteration(true);
		}
	}
}
