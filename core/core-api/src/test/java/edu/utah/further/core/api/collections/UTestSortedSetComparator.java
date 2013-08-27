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

import static edu.utah.further.core.api.collections.CollectionUtil.toSortedSet;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Comparator;
import java.util.SortedSet;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * Unit tests of {@link SortedSetComparator}.
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
 * @version Dec 20, 2009
 */
public final class UTestSortedSetComparator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestSortedSetComparator.class);

	/**
	 * Used for integer comparison and sorting tests.
	 */
	private static final Comparator<SortedSet<Integer>> COMPARATOR = new SortedSetComparator<>();

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	// ========================= TESTING METHODS ===========================

	/**
	 * An example of comparing sets with no ties.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void comparisonWithNoTies() throws IOException
	{
		// Same size
		assertLessThan(toSortedSet(1, 2, 3), toSortedSet(4, 5, 6));
		// Different sizes
		assertLessThan(toSortedSet(1, 2, 3), toSortedSet(4, 5, 6, 7));
		assertLessThan(toSortedSet(1, 2, 3, 4), toSortedSet(5, 6, 7));
	}

	/**
	 * An example of comparing sets with some element ties.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void comparisonWithSomeTies() throws IOException
	{
		assertLessThan(toSortedSet(1, 2, 3), toSortedSet(1, 3, 4));
		assertLessThan(toSortedSet(1, 2, 3), toSortedSet(1, 2, 4));
	}

	/**
	 * An example of comparing a subset to a set.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void comparisonTieBrokenBySetSize() throws IOException
	{
		assertLessThan(toSortedSet(1, 2, 3), toSortedSet(1, 2, 3, 4));
	}

	/**
	 * An example of comparing equal sets.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void comparisonOfEqualSets() throws IOException
	{
		assertEqualComparison(toSortedSet(1), toSortedSet(1));
		assertEqualComparison(toSortedSet(1, 3), toSortedSet(1, 3));
		assertEqualComparison(toSortedSet(1, 2, 3), toSortedSet(1, 2, 3));
		assertEqualComparison(toSortedSet(1, 2, 3), toSortedSet(2, 3, 1));
	}

	/**
	 * An example of comparisons involving the empty set.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void emptySetComparisons() throws IOException
	{
		// Two empty sets
		assertEqualComparison(newEmptySet(), newEmptySet());

		// Empty set < any set
		assertLessThan(newEmptySet(), toSortedSet(1, 2, 3));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private static SortedSet<Integer> newEmptySet()
	{
		return CollectionUtil.<Integer> toSortedSet();
	}

	/**
	 * @param set1
	 * @param set2
	 */
	private static void assertLessThan(final SortedSet<Integer> set1,
			final SortedSet<Integer> set2)
	{
		assertReflexiveComparison(set1, set2);
		assertEquals(-1, COMPARATOR.compare(set1, set2));
		assertEquals(1, COMPARATOR.compare(set2, set1));
	}

	/**
	 * @param set1
	 * @param set2
	 */
	private static void assertEqualComparison(final SortedSet<Integer> set1,
			final SortedSet<Integer> set2)
	{
		assertReflexiveComparison(set1, set2);
		assertEquals(0, COMPARATOR.compare(set1, set2));
		assertEquals(0, COMPARATOR.compare(set2, set1));
	}

	/**
	 * @param set1
	 * @param set2
	 */
	private static void assertReflexiveComparison(final SortedSet<Integer> set1,
			final SortedSet<Integer> set2)
	{
		assertReflexiveComparison(set1);
		assertReflexiveComparison(set2);
	}

	/**
	 * @param set1
	 */
	private static void assertReflexiveComparison(final SortedSet<Integer> set1)
	{
		assertEquals(0, COMPARATOR.compare(set1, set1));
	}
}
