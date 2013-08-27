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
import static edu.utah.further.core.math.tree.TreeUtil.newTreeWithCycleOfLength2;
import static edu.utah.further.core.test.util.AssertUtil.assertSizeEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.tree.TraversalOrder;

/**
 * Tests tree and non-tree graph traversal.
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
public final class UTestCompositeTraverser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestCompositeTraverser.class);

	// ========================= TESTING METHODS ===========================

	/**
	 * Test that tree pre-traversal.
	 */
	@Test
	public void treePreTraversal()
	{
		assertTraversedNodesEqual(newTree(), TraversalOrder.PRE, TreeUtil
				.newTreeNodeDataListInPreTraversalOrder());
	}

	/**
	 * Test that tree post-traversal.
	 */
	@Test
	public void treePostTraversal()
	{
		assertTraversedNodesEqual(newTree(), TraversalOrder.POST, TreeUtil
				.newTreeNodeDataListInPostTraversalOrder());
	}

	/**
	 * Test that cyclic graph is not a tree.
	 */
	@Test
	public void countNonTreeGraphNodes()
	{
		assertNumNodesEqual(newTreeWithCycle(), 7);
		assertNumNodesEqual(newTreeWithCycleOfLength2(), 2);
		assertNumNodesEqual(newTreeNonCyclicWithMultipleParent(), 7);
	}

	// ========================= TESTING ====================================

	/**
	 * @param root
	 * @param traversalOrder
	 * @param expectedNodeDataList
	 */
	private static void assertTraversedNodesEqual(final ListTree<Integer> root,
			final TraversalOrder traversalOrder, final List<Integer> expectedNodeDataList)
	{
		final Builder<List<ListTree<Integer>>> traverser = new CompositeTraverser<>(
				traversalOrder, root);
		final List<ListTree<Integer>> nodeList = traverser.build();
		Assert.assertEquals(expectedNodeDataList, getNodeListData(nodeList));
	}

	/**
	 * @param root
	 * @param expectedNumNodes
	 */
	private static void assertNumNodesEqual(final ListTree<Integer> root,
			final int expectedNumNodes)
	{
		final Builder<List<ListTree<Integer>>> traverser = new CompositeTraverser<>(
				TraversalOrder.PRE, root);
		assertSizeEquals(traverser.build(), expectedNumNodes);
	}

	/**
	 * @param nodes
	 */
	private static <D extends Serializable> List<D> getNodeListData(
			final List<ListTree<D>> nodes)
	{
		final List<D> dataList = CollectionUtil.newList();
		for (final ListTree<D> node : nodes)
		{
			dataList.add(node.getData());
		}
		return dataList;
	}
}
