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
import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.tree.ListComposite;
import edu.utah.further.core.api.tree.MutableTree;

/**
 * An abstraction of a mutable tree node. The node has a list of children. It depends on
 * the following generic parameters:<br>
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
public interface AbstractListTree<D extends Serializable, T extends AbstractListTree<D, T>>
		extends MutableTree<D, T>, ListComposite<T>
{
	// ========================= CONSTANTS =================================

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * Return the list of siblings of this node. This will return <code>null</code> if the
	 * this node's parent field is <code>null</code>.
	 *
	 * @return list of siblings of this node
	 */
	@Override
	List<T> getSiblings();

	/**
	 * Add a child at a specified index.
	 *
	 * @param index
	 *            index to add the child at
	 * @param child
	 *            The child to be added.
	 */
	void addChild(int index, T child);

	/**
	 * Returns a child by index.<br>
	 * Note: we restore this method but pay attention to a possible Java reflection
	 * IntrospectionException: "type mismatch between indexed read and indexed write
	 * methods". This can be thrown when using JSTL tags that try to access the "child"
	 * property and use this method to get a wrong getter return type. The reason for this
	 * exception is not fully understood. An old relevant reference is:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4253627 The private method
	 * throwing this exception appears in:
	 * http://www.imn.htwk-leipzig.de/~waldmann/edu/ss04
	 * /oo/j2sdk1.5.0/src/java/beans/IndexedPropertyDescriptor.java It is possible that
	 * the only way to get around this is to set the name of this method to be *different*
	 * than the following setter method. That's why it's not called setChild(int, T).
	 *
	 * @param index
	 *            index in the children list
	 * @return corresponding child
	 */
	T getChild(int index);

	/**
	 * Return the index of a child in the children list. If not found, returns
	 * <code>-1</code>.
	 *
	 * @param child
	 *            child object to be found (using the <code>equals()</code> method)
	 * @return child index or <code>-1</code>
	 */
	int indexOf(T child);

	/**
	 * Return the index of a child in the children list that matches this Id. If not
	 * found, returns <code>-1</code>.
	 *
	 * @param childId
	 *            ID to be found in a child object to be found (using the
	 *            <code>Long.equals()</code> method)
	 * @return child index or <code>-1</code>
	 */
	int indexOf(Long childId);

	/**
	 * Set child at position <code>id</code>. The index has to be within the children
	 * vector bounds. <code>child</code>'s parent is set to <code>this</code>.
	 *
	 * @param index
	 *            Child index in the children vector.
	 * @param child
	 *            The child to set.
	 */
	void setChildAt(int index, T child);
}
