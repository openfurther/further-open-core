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

/**
 * Defines a natural of {@link Double}s.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 26, 2009
 */
public final class DoubleComparator implements Comparator<Double>
{
	// ========================= FIELDS ====================================

	/**
	 * Desired sorting order.
	 */
	private final SortingOrder sortingOrder;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param sortingOrder
	 */
	public DoubleComparator(final SortingOrder sortingOrder)
	{
		super();
		this.sortingOrder = sortingOrder;
	}

	// ========================= IMPLEMENTATION: Comparator<Double> ========

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
	public int compare(final Double o1, final Double o2)
	{
		final int comparison = o1.compareTo(o2);
		return (sortingOrder == SortingOrder.ASCENDING) ? comparison : -comparison;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the sortingOrder property.
	 * 
	 * @return the sortingOrder
	 */
	public SortingOrder getSortingOrder()
	{
		return sortingOrder;
	}
}
