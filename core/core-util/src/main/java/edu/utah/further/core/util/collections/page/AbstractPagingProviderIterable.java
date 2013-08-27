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
import edu.utah.further.core.api.collections.page.PagingProvider;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;

/**
 * Contains paging factory methods for collection-type iterable objects. In principle, we
 * could lump all its sub-classes into one factory method for general iterable objects,
 * but we restrict our support to specific <i>disjoint</i> types to avoid looping over
 * non-sensical collections with non-deterministic iteration orders, and to avoid
 * conflicts with our paging providers until our paging provider framework becomes smarter
 * about conflict resolution.
 * <p>
 * TOOD: make paging provider framework becomes smarter about conflict resolution: if
 * multiple providers found, invoke the more type-specific one (i.e. if an iterable
 * implements A, A extends B and both A and B have providers, invoke A).
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 20, 2010
 */
abstract class AbstractPagingProviderIterable<T extends Iterable<?>> implements
		PagingProvider<T>
{
	// ========================= FIELDS ====================================

	/**
	 * Supported iterable type label.
	 */
	private final Labeled iterableType;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a generic iterable paging provider.
	 *
	 * @param iterableType
	 *            supported iterable type label
	 * @param objectType
	 *            supported iterable object type
	 */
	protected AbstractPagingProviderIterable(final Labeled iterableType)
	{
		super();
		this.iterableType = iterableType;
	}

	// ========================= IMPL: PagingProvider ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.collections.page.PagingProvider#getLabel()
	 */
	@Override
	public final Labeled getIterableType()
	{
		return iterableType;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A helper method to capture the wild-card of a list pageing iterator.
	 *
	 * @param <E>
	 * @param iterable
	 * @param pagingStrategy
	 * @return
	 */
	protected static final <E> Pager<List<E>> newIterablePager(
			final Iterable<E> iterable, final PagingStrategy pagingStrategy)
	{
		return IterablePager.newInstance(iterable, pagingStrategy);
	}
}
