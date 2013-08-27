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
package edu.utah.further.core.math.tree;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.tree.MutableTree;

/**
 * An abstraction of a mutable tree node. The node has a collection of children. It
 * depends on the following generic parameters:<br>
 * D = data type<br>
 * T = this tree type<br>
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
 * @version Dec 11, 2008
 */
@Api
public interface AbstractSortedSetTree<D extends Serializable, T extends AbstractSortedSetTree<D, T>>
		extends MutableTree<D, T>
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Return the sorted set of children of this node.
	 *
	 * @return the children of this node
	 */
	@Override
	SortedSet<T> getChildren();

	/**
	 * Return the list of children of this node.
	 *
	 * @return the children of this node
	 */
	List<T> getChildrenAsList();

	/**
	 * Return the list of siblings of this node. This will return <code>null</code> if the
	 * this node's parent field is <code>null</code>.
	 *
	 * @return list of siblings of this node
	 */
	@Override
	SortedSet<T> getSiblings();
}
