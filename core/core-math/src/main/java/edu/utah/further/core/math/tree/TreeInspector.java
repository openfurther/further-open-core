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

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.tree.ImmutableTree;

/**
 * Determines whether a structure is a tree that can be traversed so that every node is
 * visited only once.
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
public class TreeInspector<D extends Serializable, T extends ImmutableTree<D, T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(TreeInspector.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Use factory method instead.
	 */
	private TreeInspector()
	{
		
	}

	/**
	 * A generic factory method.
	 * 
	 * @param <D>
	 *            data type
	 * @param <T>
	 *            tree node type
	 * @return tree inspector instance
	 */
	public static <D extends Serializable, T extends ImmutableTree<D, T>> TreeInspector<D, T> newInstance()
	{
		return new TreeInspector<>();
	}

	// ========================= METHODS ===================================

	/**
	 * Return <code>true</code> if and only if the tree can be traversed so that every
	 * node is visited only once.
	 * 
	 * @param rootNode
	 *            the tree to be inspected
	 * @return is {@link #rootNode} a tree
	 */
	public boolean isTree(final T rootNode)
	{
		return isTree(rootNode, CollectionUtil.<T> newSet());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return <code>true</code> if and only if the tree can be traversed so that every
	 * node is visited only once.
	 * 
	 * @param node
	 *            the tree to be inspected
	 * @param visited
	 *            all nodes traversed so far. Updated in this method to include
	 *            <code>node</code>
	 * @return is {@link #rootNode} a tree
	 */
	private boolean isTree(final T node, final Set<T> visited)
	{
		// Don't print anything in this method because the toString() methods
		// might cause a stack overflow for trees with cycles.

		// log.debug("Currently at node " + node.getData()
		// + ", previously visited nodes " + visited);
		// Add node to the list of nodes visited so far
		visited.add(node);

		// A composite node is a tree if and only if its children are trees,
		// and it they haven't yet been visited.
		final Collection<T> children = node.getChildren();
		for (final T child : children)
		{
			if (visited.contains(child) || !isTree(child, visited))
			{
				if (log.isDebugEnabled())
				{
					if (visited.contains(child))
					{
						log.debug("node " + node.getData()
								+ " is not a tree because its child " + child.getData()
								+ " was already visited");
					}
					else
					{
						log.debug("node " + node.getData()
								+ " is not a tree because its child " + child.getData()
								+ " is not a tree");
					}
				}
				return false;
			}
		}

		return true;
	}

	// ========================= GETTERS & SETTERS =========================
}
