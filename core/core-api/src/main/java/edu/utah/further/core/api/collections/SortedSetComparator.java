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
package edu.utah.further.core.api.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import org.apache.commons.lang.builder.CompareToBuilder;

import edu.utah.further.core.api.message.ValidationUtil;

/**
 * Compares sorted sets that are assumed to have natural-ordering comparator by
 * lexicographic ordering.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3713<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 23, 2009
 */
public final class SortedSetComparator<E> implements Comparator<SortedSet<E>>
{
	// ========================= CONSTANTS ==================================

	/**
	 * Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= IMPLEMENTATION: Comparator<SortedSet<E>> ===

	/**
	 * Compare two {@link Double}s. If {@link #sortingOrder} is
	 * {@link SortingOrder#ASCENDING}, this is their natural ordering. If
	 * {@link #sortingOrder} is {@link SortingOrder#DESCENDING}, the order is reversed.
	 * 
	 * @param o1
	 *            left operand
	 * @param o2
	 *            right operand
	 * @return result of comparison of <code>o1</code> and <code>o2</code>
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final SortedSet<E> o1, final SortedSet<E> o2)
	{
		ValidationUtil.validateIsTrue(o1.comparator() == null,
				"First set must have natural ordering");
		ValidationUtil.validateIsTrue(o2.comparator() == null,
				"Second set must have natural ordering");

		final CompareToBuilder builder = new CompareToBuilder();

		// Compare the first corresponding min(o1.size(),o2.size()) element pairs
		final Iterator<E> iterator2 = o2.iterator();
		for (final E element1 : o1)
		{
			if (!iterator2.hasNext())
			{
				// o2.size() < o1.size()
				break;
			}
			// Pair exists, add to comparison
			builder.append(element1, iterator2.next());
		}
		
		// If we're still tied, compare by set sizes
		return builder.append(o1.size(), o2.size()).toComparison();
	}

	// ========================= PRIVATE METHODS ============================
}
