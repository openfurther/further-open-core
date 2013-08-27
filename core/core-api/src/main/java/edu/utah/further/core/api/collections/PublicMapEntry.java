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

import edu.utah.further.core.api.context.Valued;

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
public interface PublicMapEntry<K, V> extends Valued<V>
{
	// ========================= METHODS ===================================

	/**
	 * Returns the key corresponding to this entry.
	 * 
	 * @return the key corresponding to this entry
	 */
	K getKey();

	// ========================= IMPLEMENTATION: Valued ====================

	/**
	 * Returns the value corresponding to this entry. If the mapping has been removed from
	 * the backing map (by the iterator's <tt>remove</tt> operation), the results of this
	 * call are undefined.
	 * 
	 * @return the value corresponding to this entry
	 */
	@Override
	V getValue();
}