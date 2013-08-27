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
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.message.Messages.cannotAddMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.tree.Printer;
import edu.utah.further.core.api.tree.TraversalOrder;

/**
 * A tree node implementation that uses an array list for the children list. This class is
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
public final class ListTree<D extends Serializable> implements
		AbstractListTree<D, ListTree<D>>, Iterable<ListTree<D>>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Data held at this tree node.
	 */
	private D data;

	/**
	 * The parent node of this node.
	 */
	private ListTree<D> parent;

	/**
	 * Children nodes of this node.
	 */
	private List<ListTree<D>> children;

	/**
	 * Comparator according to which children are sorted.
	 */
	private Comparator<? super ListTree<D>> comparator;

	/**
	 * A tree printer of this node.
	 */
	private Printer<ListTree<D>> printer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Prevent no-argument construction.
	 */
	protected ListTree()
	{

	}

	/**
	 * Construct a node from basic data.
	 * 
	 * @param data
	 */
	public ListTree(final D data)
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
	public ListTree<D> getParent()
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
	public ListTree<D> getSuperParent(final int height)
	{
		ListTree<D> node = this;
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
	public List<ListTree<D>> getSiblings()
	{
		return (parent == null) ? null : parent.getChildren();
	}

	/**
	 * @return the printer
	 */
	@Override
	public Printer<ListTree<D>> getPrinter()
	{
		return printer;
	}

	/**
	 * @param printer
	 *            the printer to set
	 */
	@Override
	public void setPrinter(final Printer<ListTree<D>> printer)
	{
		this.printer = printer;
	}

	/**
	 * @return the children
	 */
	@Override
	public List<ListTree<D>> getChildren()
	{
		return children;
	}

	/**
	 * Add a child to the children list. The child won't be added if it equals the parent.
	 * 
	 * @param child
	 *            The child to be added
	 */
	@Override
	public void addChild(final ListTree<D> child)
	{
		if (child == this)
		{
			throw new ApplicationException(cannotAddMessage(child, "children",
					"Cannot add a node under itself! this = " + this));
		}
		children.add(child);
		child.setParent(this);
		refresh();
	}

	/**
	 * Add a child node created from data to the children list.
	 * 
	 * @param childData
	 *            The child to be added
	 */
	public ListTree<D> addChild(final D childData)
	{
		final ListTree<D> child = new ListTree<>(childData);
		addChild(child);
		return child;
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#addChild(int,
	 *      edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public void addChild(final int index, final ListTree<D> child)
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
	public ListTree<D> getChild(final int index)
	{
		return children.get(index);
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#indexOf(edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public int indexOf(final ListTree<D> child)
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
		// for (ListTree<D> child : children)
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
	public void setChildAt(final int id, final ListTree<D> child)
	{
		children.set(id, child);
		child.setParent(this);
	}

	/**
	 * @see edu.utah.further.core.api.tree.AbstractListTree#replaceChild(edu.utah.further.core.api.tree.AbstractListTree,
	 *      edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	public void replaceChild(final ListTree<D> oldChild, final ListTree<D> newChild)
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
	public void removeChildNode(final ListTree<D> child)
	{
		final int index = children.indexOf(child);
		if (index >= 0)
		{
			// Get a reference to grandchildren
			final List<ListTree<D>> grandChildren = child.getChildren();
			// Remove the child
			children.remove(index);
			// Add grandchildren in place of the child
			children.addAll(index, grandChildren);
			// Set the grandchildren's new parent
			for (final ListTree<D> grandChild : grandChildren)
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
	public void removeChild(final ListTree<D> child)
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
	public void addChilds(final Collection<? extends ListTree<D>> newChildren)
	{
		children.addAll(newChildren);
		for (final ListTree<D> child : newChildren)
		{
			child.setParent(this);
		}
		refresh();
	}

	/**
	 * Add children nodes created from a list of data to the children list.
	 * 
	 * @param childDataList
	 *            An array of child data to be added
	 */
	@SafeVarargs
	public final void addChilds(final D... childDataList)
	{
		for (final D childData : childDataList)
		{
			addChild(childData);
		}
	}

	/**
	 * Remove all children from this tree. This operation is delegated to the specific
	 * implementation of <code>children</code>.
	 */
	@Override
	public void removeAllChilds()
	{
		for (final ListTree<D> child : getChildren())
		{
			child.setParent(null);
		}
		children = newList();
	}

	/**
	 * Returns the size of the tree, which is the total number of nodes in the tree.
	 * 
	 * @return the total number of nodes in the tree
	 */
	@Override
	public int getSize()
	{
		return TreeCounter.<D, ListTree<D>, ListTree<D>> countNodes(this);
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
	public List<ListTree<D>> toNodeList(final TraversalOrder traversalOrder)
	{
		return new CompositeTraverser<>(traversalOrder, this).build();
	}

	/**
	 * Return the list of children data values.
	 * 
	 * @return list of children node data values. Sorted by child node order under this
	 *         node
	 */
	public List<D> getChildsData()
	{
		final List<D> childValues = CollectionUtil.newList();
		for (final ListTree<D> child : getChildren())
		{
			childValues.add(child.getData());
		}
		return childValues;
	}

	/**
	 * Return a depth-first node iterator of this tree node.
	 * 
	 * @return depth-first node iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ListTree<D>> iterator()
	{
		return new DepthFirstNodeIterator<>(this);
	}

	/**
	 * Return a depth-first node data iterator of this tree node.
	 * 
	 * @return a depth-first node data iterator of this tree node
	 */
	public Iterator<D> valueIterator()
	{
		return new DataIterator<>(iterator());
	}

	// ========================= METHODS ===================================

	/**
	 * Perform initializations: initialize the data structure holding the collection of
	 * children, set a default printer, etc.
	 */
	private void initialize()
	{
		// Initialize children
		children = newList();

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
	private void setParent(final ListTree<D> parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the comparator
	 */
	public Comparator<? super ListTree<D>> getComparator()
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
	public void setComparator(final Comparator<? super ListTree<D>> comparator)
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
	private void setComparator(final Comparator<? super ListTree<D>> comparator,
			final boolean doRefresh)
	{
		this.comparator = comparator;
		if (doRefresh)
		{
			refresh();
		}
	}
}
