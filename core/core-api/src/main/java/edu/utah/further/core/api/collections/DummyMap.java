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

/**
 * An abstract class used by java server faces to pass parameter to a method as map key.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 13, 2010
 */
public abstract class DummyMap<K, V> implements Map<K, V>
{
	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Subclasses should override this method call their method with <code>obj</code> as
	 * the parameter
	 * 
	 * @param obj
	 *            method parameter
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public abstract V get(Object obj);

	// ========================= IMPL: Map<K,V> ============================

	@Override
	public Collection<V> values()
	{
		return null;
	}

	@Override
	public V put(final K key, final V value)
	{
		return null;
	}

	@Override
	public Set<K> keySet()
	{
		return null;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> t)
	{
	}

	@Override
	public void clear()
	{
	}

	@Override
	public boolean containsValue(final Object value)
	{
		return false;
	}

	@Override
	public V remove(final Object key)
	{
		return null;
	}

	@Override
	public boolean containsKey(final Object key)
	{
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		return null;
	}
}