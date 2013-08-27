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

import edu.utah.further.core.api.tree.MutableTree;

/**
 * Computes the number of nodes in a tree.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 29, 2009
 */
public class TreeCounter<D extends Serializable, T extends MutableTree<D, T>> extends
		SimpleCompositeVisitor<T>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS =====================================

	/**
	 * Convenient class-local variable holding the node count.
	 */
	private int numNodes = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor. Use the static facade method {@link #countNodes(MutableTree)}
	 * instead.
	 */
	private TreeCounter()
	{
		super();
	}

	/**
	 * A facade to be called instead of constructing this object. Refreshes a tree.
	 *
	 * @param thisNode
	 *            tree root node
	 * @return number of nodes in the tree
	 */
	public static <D extends Serializable, T extends MutableTree<D, T>, S extends T> int countNodes(
			final S thisNode)
	{
		final TreeCounter<D, T> counter = new TreeCounter<>();
		counter.executeOnTree(thisNode);
		return counter.getNumNodes();
	}

	// ========================= METHODS ===================================

	// ========================= IMPLEMENTATION: TreeCommutativeDepth =========

	/**
	 * @param thisNode
	 * @return
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePre(edu.utah.further.core.api.tree.MutableTree)
	 */
	@Override
	protected Object executePre(final T thisNode)
	{
		numNodes++;
		return null;
	}

	/**
	 * @param thisNode
	 * @return
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePost(edu.utah.further.core.api.tree.MutableTree)
	 */
	@Override
	protected Object executePost(final T thisNode)
	{
		return null;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the numNodes
	 */
	public int getNumNodes()
	{
		return numNodes;
	}

}
