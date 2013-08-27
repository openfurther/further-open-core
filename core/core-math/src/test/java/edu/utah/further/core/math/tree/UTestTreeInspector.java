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

import static edu.utah.further.core.math.tree.TreeUtil.newTree;
import static edu.utah.further.core.math.tree.TreeUtil.newTreeNonCyclicWithMultipleParent;
import static edu.utah.further.core.math.tree.TreeUtil.newTreeWithCycle;
import static edu.utah.further.core.math.tree.TreeUtil.newTreeWithCycleOfLength1;
import static edu.utah.further.core.math.tree.TreeUtil.newTreeWithCycleOfLength2;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Tests tree inspection (determining whether a tree is cyclic or not traversable).
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
public final class UTestTreeInspector
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestTreeInspector.class);

	// ========================= TESTING METHODS ===========================

	/**
	 * Test that a tree is indeed a tree.
	 */
	@Test
	public void treeIsTree()
	{
		assertIsTree(newTree(), Boolean.TRUE);
	}

	/**
	 * Test that cyclic graph is not a tree.
	 */
	@Test
	public void cyclicGraphIsNotTree()
	{
		assertIsTree(newTreeWithCycle(), Boolean.FALSE);
	}

	/**
	 * Test a cyclic tree with cycle of size 1. Could be handled by {@link TreeInspector},
	 * but {@link ListTree} prevents such a construction.
	 */
	@Test(expected = ApplicationException.class)
	public void createTreeWithCycleOfLength1()
	{
		assertIsTree(newTreeWithCycleOfLength1(), Boolean.FALSE);
	}

	/**
	 * Test a cyclic tree with cycle of size 2.
	 */
	@Test
	public void createTreeWithCycleOfLength2()
	{
		assertIsTree(newTreeWithCycleOfLength2(), Boolean.FALSE);
	}

	/**
	 * Test a tree that doesn't have a cycle, but has a node with multiple parents.
	 */
	@Test
	public void multipleParentButNotCyclicTree()
	{
		// The following assertion is expected because while our implementation has a
		// bi-directional parent-child association, which should in principle not let a
		// child be under more than one parent, in practice the ListTree#setParent()
		// implementation allows that.
		assertIsTree(newTreeNonCyclicWithMultipleParent(), Boolean.FALSE);
	}

	// ========================= TESTING ====================================

	/**
	 * @param node0
	 * @param expectedTreeness
	 */
	private static void assertIsTree(final ListTree<Integer> node0,
			final Boolean expectedTreeness)
	{
		final TreeInspector<Integer, ListTree<Integer>> inspector = TreeInspector
				.newInstance();
		Assert.assertEquals(expectedTreeness, new Boolean(inspector.isTree(node0)));
	}
}
