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

import edu.utah.further.core.api.collections.page.Pager;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;

/**
 * A list pager factory.
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
final class PagingProviderList extends AbstractPagingProviderIterable<List<?>>
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterableType
	 */
	PagingProviderList(final Labeled iterableType)
	{
		super(iterableType);
	}

	// ========================= IMPL: PagingProvider ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.collections.page.PagingProvider#getObjectType()
	 */
	@Override
	public final Class<?> getObjectType()
	{
		return List.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.collections.page.PagingProvider#newPager(java.lang.Object
	 * , edu.utah.further.core.api.collections.page.PagingStrategy)
	 */
	@Override
	public Pager<?> newPager(final List<?> iterable, final PagingStrategy pagingStrategy)
	{
		return newIterablePager(iterable, pagingStrategy);
	}
}
