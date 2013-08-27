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
import java.util.Iterator;

/**
 * An depth-first (pre-traversal) iterator of node data values in a tree.
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
public final class DataIterator<D extends Serializable, T extends AbstractListTree<D, T> & Iterable<T>>
		extends FunctionalIterator<T, D>
{
	// ========================= FIELDS =====================================

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Construct an iterator that outputs a function of iterants.
	 *
	 * @param node
	 *            iterator that produces raw iterants
	 */
	public DataIterator(final Iterator<T> node)
	{
		super(node);
	}

	// ========================= IMPLEMENTATION: FunctionalIterator =========

	/**
	 * @param iterant
	 * @return
	 * @see edu.utah.further.core.math.tree.FunctionalIterator#function(java.lang.Iterable)
	 */
	@Override
	protected D function(final T iterant)
	{
		return iterant.getData();
	}

}