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

/**
 * An iterator that does not support {@link #remove}. Similar to Google collections'
 * corresponding class.
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
 * @version Sep 22, 2010
 */
public abstract class UnmodifiableIterator<E> implements Iterator<E>
{
	// ========================= IMPL: Iterator ============================

	/**
	 * Guaranteed to throw an exception and leave the underlying data unmodified.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public final void remove()
	{
		throw new UnsupportedOperationException();
	}
}
