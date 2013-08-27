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

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.tree.Composite;
import edu.utah.further.core.api.visitor.ResultVisitor;

/**
 * A simple composite structure (e.g. a tree) visitor implementation with hooks for
 * pre-traversal and post-traversal execution.
 * <p>
 * Warning: this class is not thread-safe.
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
@Implementation
public class SimpleCompositeVisitor<T extends Composite<T>> implements
		ResultVisitor<T, Object>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SimpleCompositeVisitor.class);

	// ========================= FIELDS ====================================

	/**
	 * Convenient local variable holding the current node's depth in the tree.
	 */
	protected int depth;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Do this operation at a node before operating on its children. This is a hook.
	 * 
	 * @param thisNode
	 *            the root node of the tree.
	 * @return an optional object containing intermediate processing results
	 */
	protected Object executePre(final T thisNode)
	{
		return null;
	}

	/**
	 * Do this operation at a node after operating on its children. This is a hook.
	 * 
	 * @param thisNode
	 *            the root node of the tree.
	 * @return an optional object containing intermediate processing results
	 */
	protected Object executePost(final T thisNode)
	{
		return null;
	}

	// ========================= METHODS ===================================

	/**
	 * Apply the operation <code>execute()</code> at every node of a tree.
	 * 
	 * @param rootNode
	 *            the root node of the tree.
	 */
	protected void executeOnTree(final T rootNode)
	{
		// Tracks absolute depth in the visited tree
		depth = 0;

		visit(rootNode);
	}

	/**
	 * Decides whether to process the children of this node or not. By default, this hook
	 * returns <code>true</code>.
	 * 
	 * @param thisNode
	 *            currently visited tree node
	 * @return <code>true</code> if and only if children of this node are processed in
	 *         thie visitor
	 */
	protected boolean isProcessChildren(final T thisNode)
	{
		return true;
	}

	// ========================= IMPLEMENTATION: TreeVisitor ===============

	/**
	 * Process tree and execute a function at every node. Do not call this function
	 * directly; used <code>executeOnTree</code> instead.
	 * 
	 * @param thisNode
	 *            the root node of the tree to be printed.
	 * @return null, this should be called as a void method.
	 */
	@Override
	public Object visit(final T thisNode)
	{
		// -----------------------------------
		// Pre-traversal node processing
		// -----------------------------------
		executePre(thisNode);

		// -----------------------------------
		// Process child nodes
		// -----------------------------------
		depth++;
		if (isProcessChildren(thisNode))
		{
			// Process children if hook permits
			for (final T child : thisNode.getChildren())
			{
				visit(child);
			}
		}
		depth--;

		// -----------------------------------
		// Post-traversal node processing
		// -----------------------------------
		executePost(thisNode);

		return null;
	}

	// ========================= GETTERS & SETTERS =========================

}
