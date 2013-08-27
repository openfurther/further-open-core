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

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.tree.Printer;
import edu.utah.further.core.api.tree.TraversalOrder;

/**
 * A tree node implementation that uses a linked list for the children list. This class is
 * independent of the persistent layer.
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
public final class LinkedListTree<D extends Serializable> implements
		AbstractListTree<D, LinkedListTree<D>>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	// Data held at this tree node
	protected D data;

	// The parent node
	protected LinkedListTree<D> parent;

	protected List<LinkedListTree<D>> children;

	// Comparator according to which children are sorted
	protected Comparator<? super LinkedListTree<D>> comparator;

	protected Printer<LinkedListTree<D>> printer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Prevent no-argument construction.
	 */
	protected LinkedListTree()
	{

	}

	/**
	 * Construct a node from basic data.
	 *
	 * @param data
	 */
	public LinkedListTree(final D data)
	{
		initialize();
		this.data = data;
	}

	// ========================= IMPLEMENTATION: ListTree ==========

	/**
	 * Print the data and other properties of this node.
	 *
	 * @return data and properties of this node, represented as a string
	 */
	@Override
	public String printData()
	{
		// return "[" + getClass().getSimpleName() + getData() +
		// CommonNames.TAB_CHAR + "(" +
		// getNumChildren() + ")";
		return EMPTY_STRING + getData();
	}

	/**
	 * @return parent node of this node
	 */
	@Override
	public LinkedListTree<D> getParent()
	{
		return parent;
	}

	/**
	 * Returns the [grand-...-grand-] parent node of this node.
	 *
	 * @param height
	 *            number of tree levels to go up. {@link AbstractListTree#getParent()}
	 *            means <code>height = 1</code> (the direct parent), and so on. A
	 *            non-positive <code>height</code> will return this node.
	 * @return [grand-...-grand-] parent node of this node
	 * @see edu.utah.further.core.api.tree.AbstractListTree#getSuperParent(int)
	 */
	@Override
	public LinkedListTree<D> getSuperParent(final int height)
	{
		LinkedListTree<D> node = this;
		for (int i = 1; i <= height; i++)
		{
			if (node == null)
			{
				return null;
			}
			node = node.parent;
		}
		return node;
	}

	/**
	 * Return the list of siblings of this node. This will return <code>null</code> if the
	 * this node's parent field is <code>null</code>.
	 *
	 * @return list of siblings of this node
	 * @see edu.utah.further.core.api.tree.AbstractListTree#getSiblings()
	 */
	@Override
	public List<LinkedListTree<D>> getSiblings()
	{
		return (parent == null) ? null : parent.getChildren();
	}

	/**
	 * @return the printer
	 */
	@Override
	public Printer<LinkedListTree<D>> getPrinter()
	{
		return printer;
	}

	/**
	 * @param printer
	 *            the printer to set
	 */
	@Override
	public void setPrinter(final Printer<LinkedListTree<D>> printer)
	{
		this.printer = printer;
	}

	/**
	 * @return the children
	 */
	@Override
	public List<LinkedListTree<D>> getChildren()
	{
		return children;
	}

	/**
	 * Add a child to the children list.
	 *
	 * @param child
	 *            The child to be added.
	 */
	@Override
	public void addChild(final LinkedListTree<D> child)
	{
		children.add(child);
		child.setParent(this);
		refresh();
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#addChild(int,
	 *      edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public void addChild(final int index, final LinkedListTree<D> child)
	{
		// May be slow: we are not using a linked list
		children.add(index, child);
		child.setParent(this);
		refresh();
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#getChild(int)
	 */
	@Override
	public LinkedListTree<D> getChild(final int index)
	{
		return children.get(index);
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#indexOf(edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public int indexOf(final LinkedListTree<D> child)
	{
		return children.indexOf(child);
	}

	/**
	 * @param childId
	 * @return
	 * @see edu.utah.further.core.api.tree.AbstractListTree#indexOf(java.lang.Long)
	 */
	@Override
	public int indexOf(final Long childId)
	{
		throw new BusinessRuleException(
				"Cannot be implemented until this tree node type has a unique identifier of type Long");
		// int i = 0;
		// for (LinkedListTree<D> child : children)
		// {
		// if (childId.equals(child.getId()))
		// {
		// return i;
		// }
		// i++;
		// }
		// return INVALID_VALUE_INTEGER;
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#getData()
	 */
	@Override
	public D getData()
	{
		return data;
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#setChildAt(int,
	 *      java.lang.Object)
	 */
	@Override
	public void setChildAt(final int id, final LinkedListTree<D> child)
	{
		children.set(id, child);
		child.setParent(this);
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#replaceChild(edu.utah.further.core.api.tree.AbstractListTree,
	 *      edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public void replaceChild(final LinkedListTree<D> oldChild,
			final LinkedListTree<D> newChild)
	{
		final int index = children.indexOf(oldChild);
		if (index >= 0)
		{
			children.set(index, newChild);
			newChild.setParent(this);
		}
	}

	/**
	 * Remove a child node under this node; all grandchildren (children of this child) are
	 * added under this node, at position <code>indexOf(child)</code>.
	 *
	 * @param child
	 *            the child to remove
	 * @see edu.utah.further.core.api.tree.AbstractListTree#removeChildNode(edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public void removeChildNode(final LinkedListTree<D> child)
	{
		final int index = children.indexOf(child);
		if (index >= 0)
		{
			// Get a reference to grandchildren
			final List<LinkedListTree<D>> grandChildren = child.getChildren();
			// Remove the child
			children.remove(index);
			// Add grandchildren in place of the child
			children.addAll(index, grandChildren);
			// Set the grandchildren's new parent
			for (final LinkedListTree<D> grandChild : grandChildren)
			{
				grandChild.setParent(this);
			}
		}
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#setData(java.lang.Comparable)
	 */
	@Override
	public void setData(final D data)
	{
		this.data = data;
	}

	/**
	 * Set the parent of this node to <code>null</code>.
	 * <p>
	 * WARNING: use this method only if you know that there is indeed no parent.
	 * Otherwise, a parent still might have a child reference to this node whereas this
	 * node doesn't point to it any longer.
	 */
	@Override
	public void removeParentReference()
	{
		parent = null;
	}

	/**
	 * Remove a child from the children list.
	 *
	 * @param child
	 *            The child to be removed.
	 */
	@Override
	public void removeChild(final LinkedListTree<D> child)
	{
		child.setParent(null);
		children.remove(child);
		refresh();
	}

	/**
	 * Add all children in a list to this tree. This operation is delegated to the
	 * specific implementation of <code>children</code>.
	 *
	 * @param newChildren
	 *            collection to be added
	 */
	@Override
	public void addChilds(final Collection<? extends LinkedListTree<D>> newChildren)
	{
		children.addAll(newChildren);
		for (final LinkedListTree<D> child : newChildren)
		{
			child.setParent(this);
		}
		refresh();
	}

	/**
	 * Remove all children from this tree. This operation is delegated to the specific
	 * implementation of <code>children</code>.
	 */
	@Override
	public void removeAllChilds()
	{
		for (final LinkedListTree<D> child : getChildren())
		{
			child.setParent(null);
		}
		children = new LinkedList<>();
	}

	/**
	 * Returns the size of the tree, which is the total number of nodes in the tree.
	 *
	 * @return the total number of nodes in the tree
	 */
	@Override
	public int getSize()
	{
		return TreeCounter.<D, LinkedListTree<D>, LinkedListTree<D>> countNodes(this);
	}

	/**
	 * Returns the number of children (branches of this tree node).
	 *
	 * @return Returns the number of children.
	 */
	@Override
	public int getNumChildren()
	{
		return children.size();
	}

	/**
	 * Returns <code>true</code> if and only if the number of children is positive.
	 *
	 * @return <code>true</code> if and only if this node has children
	 * @see edu.utah.further.core.api.tree.ImmutableTree#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return children.size() > 0;
	}

	/**
	 * Remove this node from its parent's children list. This should be done before
	 * deleting a node from the database.
	 *
	 * @param comparator
	 *            the comparator to set
	 */
	@Override
	public void removeFromParent()
	{
		if (parent != null)
		{
			parent.removeChild(this);
		}
	}

	/**
	 * Return the list of nodes in the syntax tree.
	 *
	 * @param traversalOrder
	 *            node traversal order (e.g. pre/post)
	 * @return list of tree nodes
	 */
	@Override
	public List<LinkedListTree<D>> toNodeList(final TraversalOrder traversalOrder)
	{
		return new CompositeTraverser<>(traversalOrder, this).build();
	}

	// ========================= METHODS ===================================

	/**
	 * Perform initializations: initialize the data structure holding the collection of
	 * children, set a default printer, etc.
	 */
	private void initialize()
	{
		// Initialize children
		children = new LinkedList<>();

		// Default printout style
		setPrinter(new TraversalPrinter<>(this));

		// Default children ordering
		// setComparator(new TreeComparator());
	}

	/**
	 * Refresh this tree node. This re-sorts children.
	 */
	public void refresh()
	{
		if ((comparator != null) && (children != null))
		{
			// Sort children
			Collections.sort(children, comparator);
		}
	}

	// ========================= PRINTOUT METHODS ==========================

	/**
	 * Print a tree in pre-traversal order.
	 *
	 * @return a string with this tree in pre-traversal order.
	 */
	@Override
	public String toString()
	{
		return printer.toString();
	}

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * Set the parent node of this node. The depth of this node is set to the parent's
	 * depth plus one. Depth updates are not cascaded to the children of this node; use
	 * refreshAll() to achieve that.<br>
	 * To keep the integrity of parent-child bi-directional references, child cannot stay
	 * under both the old parent (this.parent) and new parent (parent). Clone it and put
	 * the clone under the old parent, and move it under the new parent.
	 *
	 * @param parent
	 *            parent node of this node to set
	 */
	private void setParent(final LinkedListTree<D> parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the comparator
	 */
	public Comparator<? super LinkedListTree<D>> getComparator()
	{
		return comparator;
	}

	/**
	 * Set a new comparator to define a new children ordering and refresh the node.
	 * Setting the comparator to <code>null</code> will keep the current children
	 * ordering.
	 *
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(final Comparator<? super LinkedListTree<D>> comparator)
	{
		setComparator(comparator, true);
	}

	/**
	 * Set a new comparator to define a new children ordering and refresh the node.
	 * Setting the comparator to <code>null</code> will keep the current children
	 * ordering.
	 *
	 * @param comparator
	 *            the comparator to set
	 * @param doRefresh
	 *            refresh tree if and only if this flag is true
	 */
	private void setComparator(final Comparator<? super LinkedListTree<D>> comparator,
			final boolean doRefresh)
	{
		this.comparator = comparator;
		if (doRefresh)
		{
			refresh();
		}
	}
}
