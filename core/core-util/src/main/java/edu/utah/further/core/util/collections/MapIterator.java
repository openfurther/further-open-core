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
package edu.utah.further.core.util.collections;

import static java.util.Collections.synchronizedSortedSet;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.PublicMapEntry;
import edu.utah.further.core.api.collections.PublicMapEntryImpl;

/**
 * Iterates over entries in a general map according to the key's natural ordering
 * (ascending order of keys). A {@link Comparator} must be passed in; keys need not be
 * {@link Comparable}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Feb 4, 2009
 */
public final class MapIterator<K, V> implements Iterator<PublicMapEntry<K, V>>
{
	// ========================= FIELDS ====================================

	/**
	 * The map to iterate on.
	 */
	private final Map<K, V> map;

	/**
	 * Internal iterator.
	 */
	private final Iterator<K> iterator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a map iterator.
	 *
	 * @param map
	 *            The map to iterate on
	 * @param comparator
	 *            Key comparator to use for key ordering
	 */
	public MapIterator(final Map<K, V> map, final Comparator<K> comparator)
	{
		this.map = map;
		// This is where we guarantee that the variables are always returned
		// in the order specified by the comparator. Note: the used TreeSet is
		// synchronized.
		final SortedSet<K> keySet = synchronizedSortedSet(CollectionUtil
				.<K> newSortedSet(comparator));
		keySet.addAll(map.keySet());
		iterator = keySet.iterator();
	}

	// ========================= IMPLEMENTATION: Iterator ==================

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	@Override
	public PublicMapEntry<K, V> next()
	{
		final K name = iterator.next();
		return new PublicMapEntryImpl<>(name, map.get(name));
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		iterator.remove();
	}

}
