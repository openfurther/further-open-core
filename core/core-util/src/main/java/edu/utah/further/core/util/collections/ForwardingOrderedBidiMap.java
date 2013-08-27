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

import java.util.Set;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.MapIterator;
import org.apache.commons.collections15.OrderedBidiMap;
import org.apache.commons.collections15.OrderedMapIterator;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingObject;

/**
 * A sorted bi-directional map which forwards all its method calls to another sorted
 * bi-directional map. Subclasses should override one or more methods to modify the
 * behavior of the backing sorted map as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @see ForwardingObject
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 27, 2009
 */
public abstract class ForwardingOrderedBidiMap<K, V> extends ForwardingMap<K, V>
		implements OrderedBidiMap<K, V>
{
	// ========================= IMPLEMENTATION: ForwardingMap =============

	/**
	 * Return the delegate map. Meant to be overridden by sub-classes.
	 * 
	 * @return the delegate map
	 */
	@Override
	protected abstract OrderedBidiMap<K, V> delegate();

	/**
	 * @return
	 * @see org.apache.commons.collections15.OrderedMap#firstKey()
	 */
	@Override
	public K firstKey()
	{
		return delegate().firstKey();
	}

	/**
	 * @param value
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#getKey(java.lang.Object)
	 */
	@Override
	public K getKey(final Object value)
	{
		return delegate().getKey(value);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.OrderedBidiMap#inverseBidiMap()
	 */
	@Override
	public BidiMap<V, K> inverseBidiMap()
	{
		return delegate().inverseBidiMap();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.OrderedBidiMap#inverseOrderedBidiMap()
	 */
	@Override
	public OrderedBidiMap<V, K> inverseOrderedBidiMap()
	{
		return delegate().inverseOrderedBidiMap();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.OrderedMap#lastKey()
	 */
	@Override
	public K lastKey()
	{
		return delegate().lastKey();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#mapIterator()
	 */
	@Override
	public MapIterator<K, V> mapIterator()
	{
		return delegate().mapIterator();
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.collections15.OrderedMap#nextKey(java.lang.Object)
	 */
	@Override
	public K nextKey(final K key)
	{
		return delegate().nextKey(key);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.OrderedMap#orderedMapIterator()
	 */
	@Override
	public OrderedMapIterator<K, V> orderedMapIterator()
	{
		return delegate().orderedMapIterator();
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.collections15.OrderedMap#previousKey(java.lang.Object)
	 */
	@Override
	public K previousKey(final K key)
	{
		return delegate().previousKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#removeValue(java.lang.Object)
	 */
	@Override
	public K removeValue(final Object value)
	{
		return delegate().removeValue(value);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#values()
	 */
	@Override
	public Set<V> values()
	{
		return delegate().values();
	}

}
