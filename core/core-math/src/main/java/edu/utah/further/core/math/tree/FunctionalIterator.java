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

import java.util.Iterator;

import edu.utah.further.core.api.lang.UnmodifiableIterator;

/**
 * An iterator abstraction that delegates the iteration to an iterator and returns a
 * custom object, i.e. some function of the iterant.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Original code http://blog.subterfusion.net/2008/tree-iterator-in-java/
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 13, 2009
 */
public abstract class FunctionalIterator<T, R> extends UnmodifiableIterator<R>
{
	// ========================= FIELDS =====================================

	/**
	 * We delegate node iteration to this object.
	 */
	private final Iterator<T> treeIterator;

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Construct an iterator of a tree node.
	 * 
	 * @param iterator
	 *            iterator to delegate iteration process to
	 */
	protected FunctionalIterator(final Iterator<T> treeIterator)
	{
		this.treeIterator = treeIterator;
	}

	// ========================= ABSTRACT METHODS ===========================

	/**
	 * Return the custom function of the iterant.
	 * 
	 * @param iterant
	 *            an iterant. Can be assumed to be non-<code>null</code>
	 * @return some function of the iterant
	 */
	protected abstract R function(T iterant);

	// ========================= IMPLEMENTATION: Iterator<T> ================

	/**
	 * @return
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return treeIterator.hasNext();
	}

	/**
	 * @return
	 * @see java.util.Iterator#next()
	 */
	@Override
	public R next()
	{
		final T nextNode = treeIterator.next();
		return (nextNode == null) ? null : function(nextNode);
	}

}