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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Contains pager-related utilities.
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
public final class PagerUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PagerUtil.class);

	/**
	 * Appended to the beginning and end of a CSV line before it is parsed.
	 */
	public static final char CSV_GUARD_CHARACTER = 'a';
	
	// ========================= METHODS ===================================

	/**
	 * Return an iterable object that wraps a pager, for use in for-each loops. Example of
	 * usage:
	 * 
	 * <pre>
	 * final List&lt;List&lt;E&gt;&gt; pages = CollectionUtil.newList();
	 * for (final List&lt;E&gt; page : PagerFactory.pages(iterator))
	 * {
	 * 	pages.add(page);
	 * }
	 * return pages;
	 * </pre>
	 * 
	 * @param <E>
	 *            element type
	 * @param iterator
	 *            paging iterator
	 * @return corresponding iterable object for use in for-each loops
	 */
	public static <E> Iterable<E> pages(final Iterator<E> iterator)
	{
		return new Iterable<E>()
		{
			@Override
			public Iterator<E> iterator()
			{
				return iterator;
			}
		};
	}

	/**
	 * Iterate over an iterable object with a paging iterator and return all pages.
	 * 
	 * @param <E>
	 *            element type
	 * @param iterator
	 *            paging iterator
	 * @return list of all pages that the iterator outputs
	 */
	public static <E> List<E> getAllPages(final Iterator<? extends E> iterator)
	{
		final List<E> pages = CollectionUtil.newList();
		for (final E page : PagerUtil.pages(iterator))
		{
			pages.add(page);
		}
		return pages;
	}

	/**
	 * @param pages
	 */
	public static void printPages(final List<List<? extends PersistentEntity<Long>>> pages)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Pages:");
			for (int i = 0; i < pages.size(); i++)
			{
				final List<? extends PersistentEntity<Long>> page = pages.get(i);
				final List<Long> pageIds = CollectionUtil.toIdList(page);
				log.debug("Page #" + i + ": " + pageIds);
			}
		}
	}
}
