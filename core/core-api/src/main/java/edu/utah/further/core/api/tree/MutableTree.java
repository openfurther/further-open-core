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

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.tree.ImmutableTree;
import edu.utah.further.core.api.tree.Printer;

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
public interface MutableTree<D extends Serializable, T extends MutableTree<D, T>> extends
		ImmutableTree<D, T>/* , PubliclyCloneable<MutableTree<D, T>> */
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Add a child to the children list.
	 *
	 * @param child
	 *            The child to be added.
	 */
	void addChild(T child);

	/**
	 * Add all children in a list to this tree. This operation is delegated to the
	 * specific implementation of <code>children</code>.
	 *
	 * @param newChildren
	 *            collection to be added
	 */
	void addChilds(Collection<? extends T> newChildren);

	/**
	 * Remove this node from its parent's children list. This should be done before
	 * deleting a node from the database.
	 */
	void removeFromParent();

	/**
	 * Set the parent of this node to <code>null</code>.
	 * <p>
	 * WARNING: use this method only if you know that there is indeed no parent.
	 * Otherwise, a parent still might have a child reference to this node whereas this
	 * node doesn't point to it any longer.
	 */
	void removeParentReference();

	/**
	 * Remove a child from the children list.
	 *
	 * @param child
	 *            The child to be removed.
	 */
	void removeChild(T child);

	/**
	 * Remove a child node under this node; all grandchildren (children of this child) are
	 * added under this node, at position <code>indexOf(child)</code>.
	 *
	 * @param child
	 *            the child to remove
	 */
	void removeChildNode(T child);

	/**
	 * Remove all children.
	 */
	void removeAllChilds();

	/**
	 * Set child at position <code>children.indexOf(oldChild)</code> with
	 * <code>newChild</code>. <code>newChild</code>'s parent is set to <code>this</code>.
	 * If <code>old</code> is not found, this method does nothing.
	 *
	 * @param oldChild
	 *            the old child in the children list
	 * @param newChild
	 *            The new child
	 */
	void replaceChild(T oldChild, T newChild);

	// ========================= PRINTOUT METHODS ==========================

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * @param data
	 *            The data to set.
	 */
	void setData(D data);

	/**
	 * @return the printer
	 */
	Printer<T> getPrinter();

	/**
	 * @param printer
	 *            the printer to set
	 */
	void setPrinter(Printer<T> printer);
}
