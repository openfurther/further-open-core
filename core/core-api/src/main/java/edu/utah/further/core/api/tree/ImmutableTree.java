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
package edu.utah.further.core.api.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.tree.TraversalOrder;

/**
 * An abstraction of a basic immutable tree node type. Its generic parameters are:<br>
 * D = data type<br>
 * T = this tree type<br>
 * All this tree knows is that it has a list of children of the same type, and a data
 * type.
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
public interface ImmutableTree<D extends Serializable, T extends ImmutableTree<D, T>>
		extends Serializable, PrintableComposite<T>
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Returns the size of the tree, which is the total number of nodes in the tree.
	 *
	 * @return the total number of nodes in the tree
	 */
	int getSize();

	/**
	 * Returns the number of children (branches of this tree node).
	 *
	 * @return Returns the number of children.
	 */
	int getNumChildren();

	// ========================= PRINTOUT METHODS ==========================

	/**
	 * Return the list of nodes in the syntax tree.
	 *
	 * @param traversalOrder
	 *            node traversal order (e.g. pre/post)
	 * @return list of tree nodes
	 */
	List<T> toNodeList(TraversalOrder traversalOrder);

	// ========================= METHODS ====================================

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * @return Returns the data.
	 */
	D getData();

	/**
	 * Returns the parent node of this node.
	 *
	 * @return parent node of this node
	 */
	T getParent();

	/**
	 * Returns the [grand-...-grand-] parent node of this node.
	 *
	 * @param height
	 *            number of tree levels to go up.
	 *            {@link ImmutableTree#getParent()} means <code>height = 1</code>
	 *            (the direct parent), and so on. A non-positive <code>height</code> will
	 *            return this node.
	 * @return [grand-...-grand-] parent node of this node
	 */
	T getSuperParent(int height);

	/**
	 * Return the list of siblings of this node. This will return <code>null</code> if the
	 * this node's parent field is <code>null</code>.
	 *
	 * @return list of siblings of this node
	 */
	Collection<T> getSiblings();
}
