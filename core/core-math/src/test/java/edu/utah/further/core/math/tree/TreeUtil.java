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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Utility;

/**
 * Tree test utilities.
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
@Utility
final class TreeUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(TreeUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private TreeUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= TESTING ====================================

	/**
	 * @param data
	 * @return
	 */
	public static ListTree<Integer> newNode(final int data)
	{
		return new ListTree<>(new Integer(data));
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	public static List<Integer> newTreeNodeDataListInPreTraversalOrder()
	{
		return Arrays.<Integer> asList(1, 2, 3, 4, 5, 6, 7);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	public static List<Integer> newTreeNodeDataListInPostTraversalOrder()
	{
		return Arrays.<Integer> asList(3, 4, 2, 6, 7, 5, 1);
	}

	/**
	 * @return
	 */
	public static ListTree<Integer> newTree()
	{
		final ListTree<Integer> node0 = newNode(1);
		final ListTree<Integer> node00 = newNode(2);
		final ListTree<Integer> node000 = newNode(3);
		final ListTree<Integer> node001 = newNode(4);
		final ListTree<Integer> node01 = newNode(5);
		final ListTree<Integer> node010 = newNode(6);
		final ListTree<Integer> node011 = newNode(7);

		node00.addChild(node000);
		node00.addChild(node001);
		node01.addChild(node010);
		node01.addChild(node011);
		node0.addChild(node00);
		node0.addChild(node01);
		return node0;
	}

	/**
	 * @param node0
	 * @return
	 */
	public static ListTree<Integer> getNode011(final ListTree<Integer> node0)
	{
		return node0.getChild(1).getChild(1);
	}

	/**
	 * @return
	 */
	public static ListTree<Integer> newTreeWithCycle()
	{
		final ListTree<Integer> node0 = newTree();
		// Create a cycle
		TreeUtil.getNode011(node0).addChild(node0);
		return node0;
	}

	/**
	 * @return
	 */
	public static ListTree<Integer> newTreeWithCycleOfLength1()
	{
		final ListTree<Integer> node0 = newNode(1);
		node0.addChild(node0);
		return node0;
	}

	/**
	 * @return
	 */
	public static ListTree<Integer> newTreeWithCycleOfLength2()
	{
		final ListTree<Integer> node0 = newNode(1);
		final ListTree<Integer> node1 = newNode(2);
		node0.addChild(node1);
		node1.addChild(node0);
		return node0;
	}

	/**
	 * @return
	 */
	public static ListTree<Integer> newTreeNonCyclicWithMultipleParent()
	{
		final ListTree<Integer> node0 = newNode(1);
		final ListTree<Integer> node00 = newNode(2);
		final ListTree<Integer> node000 = newNode(3);
		final ListTree<Integer> node001 = newNode(4);
		final ListTree<Integer> node01 = newNode(5);
		final ListTree<Integer> node010 = newNode(6);
		final ListTree<Integer> node011 = newNode(7);

		node00.addChild(node000);
		node00.addChild(node001);
		node01.addChild(node010);
		node01.addChild(node011);
		node0.addChild(node00);
		node0.addChild(node01);
		// This is where we made a node be under a grand-parent in addition to
		// its parent
		node0.addChild(node011);
		return node0;
	}
}
