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
package edu.utah.further.core.api.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.utah.further.core.api.context.Api;

/**
 * A wrapper of a generic map that has some useful chaining methods.
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
 * @version Oct 13, 2008
 */
@Api
public final class ChainedMap<K, V> implements Map<K, V>
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The wrapped map.
	 */
	private final Map<K, V> map;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param map
	 */
	public ChainedMap(final Map<K, V> map)
	{
		super();
		this.map = map;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toV()
	 */
	@Override
	public String toString()
	{
		return map.toString();
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return map.hashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		return map.equals(obj);
	}

	// ========================= IMPLEMENTATION: Map =======================

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear()
	{
		map.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key)
	{
		return map.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value)
	{
		return map.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return map.entrySet();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(final Object key)
	{
		return map.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet()
	{
		return map.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(final K key, final V value)
	{
		return map.put(key, value);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends V> m)
	{
		map.putAll(m);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(final Object key)
	{
		return map.remove(key);
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int size()
	{
		return map.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values()
	{
		return map.values();
	}

	// ========================= METHODS ===================================

	/**
	 * Put an key-value pair into a map, and return the map. Useful for chaining.
	 *
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param value
	 * @return this
	 */
	public ChainedMap<K, V> putAndReturn(K key, V value)
	{
		put(key, value);
		return this;
	}
}
