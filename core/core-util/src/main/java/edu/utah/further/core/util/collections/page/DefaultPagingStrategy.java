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

import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.message.ValidationUtil;

/**
 * A paging strategy that outputs a constant page size.
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
public final class DefaultPagingStrategy implements PagingStrategy
{
	// ========================= FIELDS ====================================

	/**
	 * Iterable object type.
	 */
	private Labeled iterableType;

	/**
	 * Constant page size.
	 */
	private int pageSize;

	/**
	 * Maximum results to retrieve.
	 */
	private int maxResults;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterableType
	 * @param pageSize
	 */
	public DefaultPagingStrategy(final Labeled iterableType, final int pageSize)
	{
		this(iterableType, pageSize, PagingStrategy.NO_LIMIT);
	}

	/**
	 * @param iterableType
	 * @param pageSize
	 * @param maxResults
	 */
	public DefaultPagingStrategy(final Labeled iterableType, final int pageSize,
			final int maxResults)
	{
		super();
		ValidationUtil.validateNotNull("iterableType", iterableType);
		ValidationUtil.validateIsTrue(pageSize > 0, "page size must be positive");
		this.iterableType = iterableType;
		this.pageSize = pageSize;
		this.maxResults = maxResults;
	}

	// ========================= IMPL: PagingStrategy ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.collections.page.PagingStrategy#getNextPageSize(int,
	 * int, int)
	 */
	@Override
	public <E> int getNextPageSize(final int pageNumber, final int pageIndex,
			final int oldPageSize)
	{
		return pageSize;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the iterableType property.
	 *
	 * @return the iterableType
	 */
	@Override
	public Labeled getIterableType()
	{
		return iterableType;
	}

	/**
	 * Return the maxResults property.
	 *
	 * @return the maxResults
	 */
	@Override
	public int getMaxIterants()
	{
		return maxResults;
	}

	/**
	 * Set a new value for the iterableType property.
	 *
	 * @param iterableType
	 *            the iterableType to set
	 */
	public void setIterableType(final Labeled iterableType)
	{
		this.iterableType = iterableType;
	}

	/**
	 * Set a new value for the pageSize property.
	 *
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * Set a new value for the maxResults property.
	 *
	 * @param maxResults
	 *            the maxResults to set
	 */
	public void setMaxResults(final int maxResults)
	{
		this.maxResults = maxResults;
	}

}
