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
package edu.utah.further.core.math.misc;

import edu.utah.further.core.api.context.Api;

/**
 * A pair of comparable objects.
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
@Api
public class ComparablePair<A extends Comparable<A>, B extends Comparable<B>> extends
		Pair<A, B> implements Comparable<ComparablePair<A, B>>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * 
	 */
	private static final long serialVersionUID = -2535137422305569138L;

	/**
	 * @param left
	 * @param right
	 */
	public ComparablePair(final A left, final B right)
	{
		super(left, right);
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= IMPLEMENTATION: Comparable<Pair> ==========

	/**
	 * Compare two pairs by lexicographic order (first in, left, then in right).
	 * Compatible with the <code>equals()</code> method if and only if the
	 * <code>compareTo()</code> method in types <code>A</code> and <code>B</code> are
	 * Compatible with the corresponding <code>equals()</code> methods therein.
	 *
	 * @param other
	 *            the other <code>Pair</code> to be compared with this one.
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final ComparablePair<A, B> other)
	{
		// Compare lefts
		final int compareLeft = (left == null) ? ((other.left == null) ? 0 : -1) : left
				.compareTo(other.left);
		if (compareLeft != 0)
		{
			return compareLeft;
		}

		// Compare rights
		final int compareRight = (right == null) ? ((other.right == null) ? 0 : -1)
				: right.compareTo(other.right);
		if (compareRight != 0)
		{
			return compareRight;
		}

		// Objects are equal
		return 0;
	}
}
