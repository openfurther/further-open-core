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
package edu.utah.further.core.util.lang;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import edu.utah.further.core.api.lang.Stack;

/**
 * Similar to {@link java.util.Stack}, but works correctly with generic types. The
 * original implementation of {@link #@see
 * edu.utah.further.core.api.misc.PubliclyCloneable#copy()} returns a raw
 * {@link java.util.Stack} type, which causes some unchecked warnings.
 * <p>
 * Also fixed the {@link Collection#size()} method name to a JavaBean getter method name.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jan 8, 2009
 */
public final class StackImpl<E> implements Stack<E>
{
	// ========================= CONSTANTS =================================

	/**
	 * Use serialVersionUID from JDK 1.0.2 for interoperability.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1224463164541339165L;

	// ========================= FIELDS ====================================

	/**
	 * Decorated Java language stack object.
	 */
	private final java.util.Stack<E> stack = new java.util.Stack<>();

	// ========================= METHODS ===================================

	/**
	 * Creates an empty generic stack.
	 */
	public StackImpl()
	{

	}

	/**
	 * Creates a generic stack. Push each element in the collection argument onto the
	 * stack in the order of iteration.
	 *
	 * @param colllection
	 *            collection to push onto the stack
	 */
	public StackImpl(final Iterable<E> collection)
	{
		for (final E element : collection)
		{
			push(element);
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Returns a clone of this stack. Stack elements are not cloned, so this is a shallow
	 * copy, not a deep copy.
	 *
	 * @return a clone of this stack
	 */
	@Override
	public StackImpl<E> copy()
	{
		final StackImpl<E> copy = new StackImpl<>();
		for (final E element : this.stack)
		{
			copy.stack.add(element);
		}
		return copy;
	}

	/**
	 * Returns a list of the elements on the stack that appear before a specified fence.
	 * <p>
	 * Sometimes a parser will recognize a list from within a pair of parentheses or
	 * brackets. The parser can mark the beginning of the list with a fence, and then
	 * retrieve all the items that come after the fence with this method.
	 *
	 * @param fence
	 *            the fence, a marker of where to stop popping the stack
	 * @return the list the elements above the specified fence
	 */
	@Override
	public List<E> elementsAbove(final E fence)
	{
		final List<E> items = newList();

		while (!this.isEmpty())
		{
			final E top = this.pop();
			if (top.equals(fence))
			{
				break;
			}
			items.add(top);
		}

		return items;
	}

	/**
	 * Returns a list of the elements on the stack that appear before a specified fence.
	 * <p>
	 * Sometimes a parser will recognize a list from within a pair of parentheses or
	 * brackets. The parser can mark the beginning of the list with a fence, and then
	 * retrieve all the items that come after the fence with this method.
	 *
	 * @param fence
	 *            the fence, a marker of where to stop popping the stack
	 * @param comparator
	 *            a comparator that is really used as an equalizer. Objects will be popped
	 *            until the comparison result with the fence returns 0
	 * @return the list the elements above the specified fence
	 */
	@Override
	public List<E> elementsAbove(final E fence, final Comparator<E> comparator)
	{
		final List<E> items = newList();

		while (!this.isEmpty())
		{
			final E top = this.pop();
			if (comparator.compare(top, fence) == 0)
			{
				break;
			}
			items.add(top);
		}

		return items;
	}

	// ========================= IMPLEMENTATION: Stack<E> ==================

	/**
	 * @param obj
	 * @return
	 * @see java.util.Vector#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		return stack.equals(obj);
	}

	/**
	 * @return
	 * @see java.util.Vector#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return stack.hashCode();
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this vector,
	 * or {@link CommonConstants#INVALID_VALUE_INTEGER} if this vector does not contain
	 * the element. More formally, returns the lowest index {@code i} such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>, or
	 * {@link CommonConstants#INVALID_VALUE_INTEGER} if there is no such index.
	 *
	 * @param o
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in this vector,
	 *         or {@link CommonConstants#INVALID_VALUE_INTEGER} if this vector does not
	 *         contain the element
	 * @see edu.utah.further.core.api.lang.Stack#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(final E o)
	{
		return stack.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.Vector#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Stack#peek()
	 */
	@Override
	public E peek()
	{
		return stack.peek();
	}

	/**
	 * @return
	 * @see java.util.Stack#pop()
	 */
	@Override
	public E pop()
	{
		return stack.pop();
	}

	/**
	 * @param item
	 * @return
	 * @see java.util.Stack#push(java.lang.Object)
	 * @see edu.utah.further.core.api.lang.Stack#push(java.lang.Object)
	 */
	@Override
	public E push(final E item)
	{
		return stack.push(item);
	}

	/**
	 * Return the number of elements in the stack.
	 *
	 * @return the number of elements in the stack
	 * @see java.util.Vector#size()
	 */
	@Override
	public int getSize()
	{
		return stack.size();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * @return
	 * @see java.util.Vector#toString()
	 */
	@Override
	public String toString()
	{
		return stack.toString();
	}
}
