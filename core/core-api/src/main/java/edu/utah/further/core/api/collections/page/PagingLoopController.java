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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Controls a paging loop per FUR-1311. Contains both the pager (which fetches iterant
 * pages from the iterable object during the loop) and other parameters (e.g. the true
 * maximum number of results to be output from the loop; can be smaller than the total
 * number of iterants).
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
 * @version Oct 4, 2010
 * @see https://jira.chpc.utah.edu/browse/FUR-1311
 */
public final class PagingLoopController
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Outputs iterant pages from the iterable object.
	 */
	private Pager<?> pager;

	/**
	 * Number of results output from the loop so far.
	 */
	private int resultCount;

	/**
	 * Number of pages processed by the loop so far.
	 */
	private int pageCount = 0;

	/**
	 * Maximum number of results to be output from the loop.
	 */
	private int maxResults = PagingStrategy.NO_LIMIT;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public PagingLoopController()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final PagingLoopController that = (PagingLoopController) obj;
		return new EqualsBuilder()
				.append(this.pager, that.pager)
				.append(this.maxResults, that.maxResults)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(pager).append(maxResults).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("pager", pager)
				.append("maxResults", maxResults)
				.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Is there a next iterant to be be processed by the paging loop.
	 *
	 * @return <code>true</code> if and only the pager has another iterant, and if the
	 *         iterant count is less than the max results parameter or when the latter is
	 *         set to {@link PagingStrategy#NO_LIMIT}.
	 */
	public boolean hasNext()
	{
		return pager.hasNext()
				&& ((maxResults == PagingStrategy.NO_LIMIT) || (resultCount < maxResults));
	}

	/**
	 * Increment the pageCount property.
	 */
	public void incrementPageCount()
	{
		pageCount++;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the pager property.
	 *
	 * @return the pager
	 */
	public Pager<?> getPager()
	{
		return pager;
	}

	/**
	 * Set a new value for the pager property.
	 *
	 * @param pager
	 *            the pager to set
	 */
	public void setPager(final Pager<?> pager)
	{
		this.pager = pager;
	}

	/**
	 * Return the maxResults property.
	 *
	 * @return the maxResults
	 */
	public int getMaxResults()
	{
		return maxResults;
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

	/**
	 * Return the resultCount property.
	 *
	 * @return the resultCount
	 */
	public int getResultCount()
	{
		return resultCount;
	}

	/**
	 * Set a new value for the resultCount property.
	 *
	 * @param resultCount
	 *            the resultCount to set
	 */
	public void setResultCount(final int resultCount)
	{
		this.resultCount = resultCount;
	}

	/**
	 * Return the pageCount property.
	 *
	 * @return the pageCount
	 */
	public int getPageCount()
	{
		return pageCount;
	}

	/**
	 * Set a new value for the pageCount property.
	 *
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(final int pageCount)
	{
		this.pageCount = pageCount;
	}
}
