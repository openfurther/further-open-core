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

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.MapIterator;

import edu.utah.further.core.api.context.Api;

/**
 * A wrapper of a generic bi-directional map. The wrapped map can be any
 * implementation of the {@link BidiMap} interface.
 * <p>
 * It would be nice to autowire this class in Spring, but that doesn't work. For
 * each generic map with specific parameters, extends this class to a dedicated
 * type.
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2008 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
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
public class BidiMapWrapper<K, V> implements BidiMap<K, V>
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The wrapped map.
	 */
	private final BidiMap<K, V> map;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param map
	 */
	public BidiMapWrapper(final BidiMap<K, V> map)
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

	// ========================= IMPLEMENTATION: BidiMap ===================

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

	/**
	 *
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
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value)
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
	public V get(Object key)
	{
		return map.get(key);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#getKey(java.lang.Object)
	 */
	@Override
	public K getKey(Object arg0)
	{
		return map.getKey(arg0);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#inverseBidiMap()
	 */
	@Override
	public BidiMap<V, K> inverseBidiMap()
	{
		return map.inverseBidiMap();
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
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#mapIterator()
	 */
	@Override
	public MapIterator<K, V> mapIterator()
	{
		return map.mapIterator();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#put(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public V put(K arg0, V arg1)
	{
		return map.put(arg0, arg1);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		map.putAll(m);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key)
	{
		return map.remove(key);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.commons.collections15.BidiMap#removeValue(java.lang.Object)
	 */
	@Override
	public K removeValue(Object arg0)
	{
		return map.removeValue(arg0);
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
	 * @see org.apache.commons.collections15.BidiMap#values()
	 */
	@Override
	public Set<V> values()
	{
		return map.values();
	}

}
