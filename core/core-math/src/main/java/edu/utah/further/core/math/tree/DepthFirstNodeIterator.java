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

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;

import java.util.Iterator;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.lang.UnmodifiableIterator;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.tree.Composite;

/**
 * A depth-first (pre-traversal) iterator of a composite object. This class is not
 * thread-safe.
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
public final class DepthFirstNodeIterator<T extends Composite<T>> extends
		UnmodifiableIterator<T>
{
	// ========================= FIELDS =====================================

	/**
	 * Current node in the iteration.
	 */
	private T currentNode;

	/**
	 * Does the root node have children. Cached for efficiency.
	 */
	private final boolean hasChildren;

	/**
	 * Iterator of the children of <code>currentNode</code>.
	 */
	private final Iterator<T> childIterator;

	/**
	 * Iterator of the the children of <code>currentNode</code>.
	 */
	private Iterator<T> currentIterator;

	/**
	 * Maximum number of iterations to perform. A value of
	 * {@link Constants#INVALID_VALUE_INTEGER} number means no limit.
	 */
	private int maxIterations;

	/**
	 * Keeps track of how many iterations have been performed.
	 */
	private int counter = 0;

	/**
	 * Caches part of the the iteration condition -- an optimization.
	 */
	private boolean noIterationLimit;

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Construct an iterator of a tree node.
	 * 
	 * @param node
	 *            tree node
	 */
	public DepthFirstNodeIterator(final T node)
	{
		setMaxIterations(INVALID_VALUE_INTEGER);
		this.currentNode = node;
		this.childIterator = node.getChildren().iterator();
		this.hasChildren = this.childIterator.hasNext();
		if (this.hasChildren)
		{
			this.currentIterator = newInstance(this.childIterator.next());
		}
	}

	// ========================= IMPLEMENTATION: Iterator<T> ================

	/**
	 * @return
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return (noIterationLimit || (counter < maxIterations)) && (currentNode != null);
	}

	/**
	 * @return
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next()
	{
		final T result = currentNode;
		if (hasChildren)
		{
			// Try to advance the current-tree-level iterator until there are no more
			// elements.
			if (currentIterator.hasNext())
			{
				currentNode = currentIterator.next();
			}
			else
			{
				if (childIterator.hasNext())
				{
					currentIterator = newInstance(childIterator.next());
					currentNode = currentIterator.next();
				}
				else
				{
					currentNode = null;
				}
			}
		}
		else
		{
			currentNode = null;
		}

		counter++;
		return result;
	}

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * Return the maximum number of iterations to perform. A value of
	 * {@link Constants#INVALID_VALUE_INTEGER} number means no limit.
	 * 
	 * @return the maxIterations
	 */
	public int getMaxIterations()
	{
		return maxIterations;
	}

	/**
	 * Set a new value for the maximum number of iterations to perform. A value of
	 * {@link Constants#INVALID_VALUE_INTEGER} number means no limit.
	 * 
	 * @param maxIterations
	 *            the maxIterations to set
	 */
	public void setMaxIterations(final int maxIterations)
	{
		this.maxIterations = maxIterations;
		this.noIterationLimit = StringUtil.isInvalidInteger(this.maxIterations);
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * A factory method for this class.
	 * 
	 * @param node
	 *            tree node
	 * @return iterator instance
	 */
	private static <T extends Composite<T>> DepthFirstNodeIterator<T> newInstance(
			final T node)
	{
		return new DepthFirstNodeIterator<>(node);
	}

}