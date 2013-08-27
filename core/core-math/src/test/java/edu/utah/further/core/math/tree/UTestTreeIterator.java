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

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Tests tree iterator. Taken from
 * http://blog.subterfusion.net/2008/tree-iterator-in-java/
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
public final class UTestTreeIterator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestTreeIterator.class);

	/**
	 * Assertion error messages.
	 */
	private static final String MESSAGE_UNEXPECTED_NODE_VALUE = "Unexpected node value.";
	private static final String MESSAGE_UNEXPECTED_CHILD_VALUE = "Unexpected child value.";

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test tree node's data value.
	 */
	@Test
	public void dataValue()
	{
		final String value = "one";
		final ListTree<String> tree = newListTree(value);
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, tree.getData(), value);
	}

	/**
	 * Test children value iterator.
	 */
	@Test
	public void childrenValues()
	{
		final ListTree<String> tree = newListTree("one");
		final String[] values =
		{ "two", "three", "four" };
		tree.addChilds(values);

		final List<String> childsData = tree.getChildsData();
		for (int i = 0; i < childsData.size(); i++)
		{
			assertEquals(MESSAGE_UNEXPECTED_CHILD_VALUE, childsData.get(i), values[i]);
		}
	}

	/**
	 * Test a tree hierarchy iterator.
	 */
	@Test
	public void treeValueIterator()
	{
		final ListTree<String> tree = newListTree("one");
		final ListTree<String> two = tree.addChild("two");
		final ListTree<String> three = tree.addChild("three");
		two.addChilds("four", "five");
		three.addChilds("six", "seven");

		final List<String> values = CollectionUtil.newList();
		for (final Iterator<String> i = tree.valueIterator(); i.hasNext();)
		{
			values.add(i.next());
		}

		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(0), "one");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(1), "two");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(2), "four");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(3), "five");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(4), "three");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(5), "six");
		assertEquals(MESSAGE_UNEXPECTED_NODE_VALUE, values.get(6), "seven");
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * @param value
	 * @return
	 */
	private ListTree<String> newListTree(final String value)
	{
		return new ListTree<>(value);
	}
}
