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

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.utah.further.core.api.context.Api;

/**
 * An iterator over a collection. Iterator takes the place of <code>Enumeration</code> in
 * the Java collections framework. Iterators differ from enumerations in two ways:
 * <ul>
 * <li>Iterators allow the caller to remove elements from the underlying collection during
 * the iteration with well-defined semantics.
 * <li>Method names have been improved.
 * </ul>
 * <p>
 * The difference between a classical Java iterator and a rich iterator is the additional
 * <code>begin()</code> method, borrowed from C++ syntax for easier iteration syntax.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 5, 2009
 */
@Api
public interface RichIterator<E> extends Iterator<E>
{
	// ========================= METHODS ===================================

	/**
	 * Returns the first element in the iteration.
	 *
	 * @return the first element in the iteration
	 * @exception NoSuchElementException
	 *                iteration has no elements
	 */
	E begin();

	/**
	 * Returns <code>false</code> if and only if the element returned by the last
	 * {@link #next()} call is the last element in the iteration.
	 *
	 * @return loop-not-yet-terminated condition
	 */
	boolean notEnd();
}
