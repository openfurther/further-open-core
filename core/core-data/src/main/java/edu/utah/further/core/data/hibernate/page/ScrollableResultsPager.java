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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;

import org.hibernate.ScrollableResults;
import org.hibernate.transform.DistinctResultTransformer;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.util.collections.page.AbstractListPager;
import edu.utah.further.core.util.io.LoggingUtil;

/**
 * A pager of a Hibernate scrollable result set. Need to be careful about calling
 * <code>next()</code> in order to not inadvertently close the result set.
 * <p>
 * Unfortunately we currently have to invoke Hibernate's {@link DistinctResultTransformer}
 * 's logic manually here to ensure root entity collocation, and tweak it so that the
 * output page size is not smaller than the requested size as a result.
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
 * @see https://jira.chpc.utah.edu/browse/FUR-1274
 */
final class ScrollableResultsPager extends AbstractListPager<PersistentEntity<?>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ScrollableResultsPager.class);

	// ========================= FIELDS ====================================

	/**
	 * Hibernate results set.
	 */
	private final ScrollableResults iterable;

	/**
	 * Is there another row in the result set.
	 */
	private boolean hasNext = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param iterable
	 * @param pagingStrategy
	 */
	protected ScrollableResultsPager(final ScrollableResults iterable,
			final PagingStrategy pagingStrategy)
	{
		super(pagingStrategy);
		ValidationUtil.validateNotNull("iterable", iterable);
		this.iterable = iterable;
		// Point to the first result set row
		this.hasNext = iterable.next();
	}

	// ========================= IMPL: Iterator<List<E>> ===================

	/**
	 * @return
	 * @see edu.utah.further.core.util.collections.page.AbstractPager#hasNextInternal()
	 */
	@Override
	protected boolean hasNextInternal()
	{
		return hasNext;
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
	protected List<PersistentEntity<?>> getPage(final int currentPageSize)
	{
		// ################################################################################
		// Until FUR-1274 is resolved, we call transformer explicitly to collocate root
		// entities. This is only a temporary solution and does not cascade to
		// sub-queries: if a sub-select also requires a DISTINCT_ROOT_ENTITY, it will not
		// be collocated!!!
		// ################################################################################

		// Raw page read from the result set
		final List<PersistentEntity<?>> page = CollectionUtil.newList();
		// Uniquely distinct each tuple row here, as in Hibernate's
		// DistrinctResultTransformer
		final List<PersistentEntity<?>> collocatedPage = CollectionUtil.newList();
		final Set<Object> distinct = CollectionUtil.newSet();

		// Main loop over result set rows
		int count = 0;
		while (hasNext && !isEndOfIteration())
		{
			// Read the next row
			final PersistentEntity<?> entity = (PersistentEntity<?>) iterable.get(0);
			final Object entityId = entity.getId();
			if (log.isTraceEnabled())
			{
				log.trace("Current row entity ID " + entity.getId() + " distinct "
						+ distinct);
			}

			// count = # *distinct* results encountered so far
			if (!distinct.contains(entityId))
			{
				// Newly-encountered entity
				if (count == currentPageSize)
				{
					// We are already pointing to the first result of the next page
					break;
				}
				// Add entity to collocated page
				distinct.add(entityId);
				collocatedPage.add(entity);
				count++;
			}

			// Add entity to raw page
			if (getTotalIterantCounter() > getNumHeaderRows())
			{
				page.add(entity);
				addSingleIterant();
			}
			hasNext = iterable.next();
		}
		if (log.isTraceEnabled())
		{
			log.trace("-- Reached end of page --");
			log.trace("Raw page:");
			LoggingUtil.printEntityList(log, page);
			log
					.trace("Collocated page (after manually mimicing Hibernate's DistinctResultTransformer.transformList()):");
			LoggingUtil.printEntityList(log, collocatedPage);
		}

		return collocatedPage;
	}
}
