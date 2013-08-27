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
package edu.utah.further.core.api.lang;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Consists of the methods of {@link Stack} methods without the {@link Vector} interface
 * methods, with some additional useful pop/push methods.
 * <p>
 * Similar to {@link java.util.Stack}, but works correctly with generic types. The
 * original implementation of {@link PubliclyCloneable#copy()} returns a raw
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
public interface Stack<E> extends PubliclyCloneable<Stack<E>>
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

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
	 */
	int indexOf(E o);

	/**
	 * Pop an element from the stack
	 *
	 * @return the top element on the stack
	 * @see java.util.Stack#pop()
	 */
	E pop();

	/**
	 * @param item
	 * @return
	 * @see java.util.Stack#push(java.lang.Object)
	 */
	E push(E item);

	/**
	 * @return
	 * @see java.util.Vector#isEmpty()
	 */
	boolean isEmpty();

	/**
	 * @return
	 * @see java.util.Stack#peek()
	 */
	E peek();

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
	List<E> elementsAbove(E fence);

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
	List<E> elementsAbove(E fence, Comparator<E> comparator);

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Returns a clone of this stack. Stack elements are not cloned, so this is a shallow
	 * copy, not a deep copy.
	 *
	 * @return a clone of this stack
	 */
	@Override
	Stack<E> copy();

	/**
	 * Return the number of elements in the stack.
	 *
	 * @return the number of elements in the stack
	 * @see java.util.Vector#size()
	 */
	int getSize();
}
