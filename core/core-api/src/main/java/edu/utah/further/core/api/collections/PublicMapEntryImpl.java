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

import java.util.Map;

/**
 * A publicly-visible map entry (key-value pair). The <tt>Map.entrySet</tt> method returns
 * a collection-view of the map, whose elements are of this class. The <i>only</i> way to
 * obtain a reference to a map entry is from the iterator of this collection-view. These
 * <tt>Map.Entry</tt> objects are valid <i>only</i> for the duration of the iteration;
 * more formally, the behavior of a map entry is undefined if the backing map has been
 * modified after the entry was returned by the iterator, except through the
 * <tt>setValue</tt> operation on the map entry.
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
 * @see Map#entrySet()
 */
public final class PublicMapEntryImpl<K, V> implements PublicMapEntry<K, V>
{
	// ========================= CONSTANTS =================================

	/**
	 * For parsing a string into a parameter map.
	 */
	protected static final String DELIMITER = " ";

	// ========================= FIELDS ====================================

	/**
	 * Entry key.
	 */
	protected final K key;

	/**
	 * Entry value.
	 */
	protected V value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a new map entry.
	 *
	 * @param key
	 *            entry key
	 * @param value
	 *            entry value
	 */
	public PublicMapEntryImpl(final K key, final V value)
	{
		super();
		this.key = key;
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Compares the specified object with this entry for equality. Returns <tt>true</tt>
	 * if the given object is also a map entry and the two entries represent the same
	 * mapping. More formally, two entries <tt>e1</tt> and <tt>e2</tt> represent the same
	 * mapping if
	 *
	 * <pre>
	 * (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(e2.getKey()))
	 * 		&amp;&amp; (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(
	 * 				e2.getValue()))
	 * </pre>
	 *
	 * This ensures that the <tt>equals</tt> method works properly across different
	 * implementations of the <tt>Map.Entry</tt> interface.
	 *
	 * @param o
	 *            object to be compared for equality with this map entry
	 * @return <tt>true</tt> if the specified object is equal to this map entry
	 */
	@Override
	public boolean equals(final Object obj)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		final PublicMapEntryImpl<?, ?> other = (PublicMapEntryImpl<?, ?>) obj;

		return (key == null ? other.key == null : key.equals(other.key))
				&& (value == null ? other.value == null : value.equals(other.value));

	}

	/**
	 * Returns the hash code value for this map entry. The hash code of a map entry
	 * <tt>e</tt> is defined to be:
	 *
	 * <pre>
	 * (e.getKey() == null ? 0 : e.getKey().hashCode())
	 * 		&circ; (e.getValue() == null ? 0 : e.getValue().hashCode())
	 * </pre>
	 *
	 * This ensures that <tt>e1.equals(e2)</tt> implies that
	 * <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries <tt>e1</tt> and
	 * <tt>e2</tt>, as required by the general contract of <tt>Object.hashCode</tt>.
	 *
	 * @return the hash code value for this map entry
	 * @see Object#hashCode()
	 * @see Object#equals(Object)
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode()
	{
		return (key == null ? 0 : key.hashCode())
				^ (value == null ? 0 : value.hashCode());

	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return key + "=" + value;
	}

	// ========================= IMPLEMENTATION: PublicMapEntry ============

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.PublicMapEntry#getKey()
	 */
	@Override
	public K getKey()
	{
		return key;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.PublicMapEntry#getValue()
	 */
	@Override
	public V getValue()
	{
		return value;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Replaces the value corresponding to this entry with the specified value (optional
	 * operation).
	 *
	 * @param value
	 *            new value to be stored in this entry
	 * @return old value corresponding to the entry
	 */
	V setValue(final V value)
	{
		final V oldValue = this.value;
		this.value = value;
		return oldValue;
	}
}
