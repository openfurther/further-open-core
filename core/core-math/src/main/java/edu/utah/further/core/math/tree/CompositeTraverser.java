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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.tree.TraversalOrder.POST;
import static edu.utah.further.core.api.tree.TraversalOrder.PRE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.tree.Composite;
import edu.utah.further.core.api.tree.TraversalOrder;

/**
 * Traverses a composite structure (e.g. a tree) into a list of nodes. Each node is in
 * effect a reference to the sub-tree rooted at that node.
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
@Implementation
public class CompositeTraverser<T extends Composite<T>> implements Builder<List<T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CompositeTraverser.class);

	// ========================= FIELDS =====================================

	// ====================
	// Inputs
	// ====================

	/**
	 * Node traversal order identifier.
	 */
	private final TraversalOrder traversalOrder;

	/**
	 * Composite object to traverse.
	 */
	private final T root;

	// ====================
	// Outputs
	// ====================

	/**
	 * Sorted node list.
	 */
	private final List<T> nodes = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a syntax tree node traverser.
	 * 
	 * @param traversalOrder
	 *            node traversal order (e.g. pre/post)
	 * @param root
	 *            composite object to traverse
	 */
	public CompositeTraverser(final TraversalOrder traversalOrder, T root)
	{
		this.traversalOrder = traversalOrder;
		this.root = root;
	}

	// ========================= IMPL: Builder =============================

	/**
	 * Traverses the root's tree into a list of nodes.
	 * 
	 * @return list of root tree nodes in the order specified by {@link #traversalOrder}
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public List<T> build()
	{
		// Traverse the tree into a node list
		this.visit(root);
		return nodes;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePre(edu.utah.further.core.api.tree.MutableTree)
	 */
	protected Object executePre(final T thisNode)
	{
		if (traversalOrder == PRE)
		{
			// Add token of this node to global node token list
			nodes.add(thisNode);
		}
		return null;
	}

	/**
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePost(edu.utah.further.core.api.tree.MutableTree)
	 */
	protected Object executePost(final T thisNode)
	{
		if (traversalOrder == POST)
		{
			// Add token of this node to global node token list
			nodes.add(thisNode);
		}
		return null;
	}

	/**
	 * Process tree and execute a function at every node. Do not call this function
	 * directly; used <code>executeOnTree</code> instead.
	 * 
	 * @param thisNode
	 *            the root node of the tree to be printed.
	 * @return null, this should be called as a void method.
	 */
	private Object visit(final T thisNode)
	{
		// -----------------------------------
		// Pre-traversal node processing
		// -----------------------------------
		executePre(thisNode);

		// -----------------------------------
		// Process child nodes
		// -----------------------------------
		// Process children if hook permits
		for (final T child : thisNode.getChildren())
		{
			if (!nodes.contains(child))
			{
				visit(child);
			}
		}

		// -----------------------------------
		// Post-traversal node processing
		// -----------------------------------
		executePost(thisNode);

		return null;
	}
}
