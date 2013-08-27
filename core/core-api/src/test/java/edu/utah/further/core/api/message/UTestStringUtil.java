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
package edu.utah.further.core.api.message;

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.text.StringUtil.centerWithSpace;
import static edu.utah.further.core.api.text.StringUtil.isCamelCaseString;
import static edu.utah.further.core.api.text.StringUtil.isClassNameString;
import static edu.utah.further.core.api.text.StringUtil.isPackageNameString;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.SortedSet;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Test string utilities.
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
 * @version Nov 6, 2008
 */
public final class UTestStringUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestStringUtil.class);

	// ========================= METHODS ===================================

	/**
	 * Test that any string starts with an empty string.
	 */
	@Test
	public void startsWithEmptyString()
	{
		assertTrue("abcde".startsWith(EMPTY_STRING));
		assertTrue(EMPTY_STRING.startsWith(EMPTY_STRING));
	}

	/**
	 * Test the camel case regular expression.
	 */
	@Test
	public void camelCasePatternMatchingEmpty()
	{
		assertFalse(isCamelCaseString(null));
		assertFalse(isCamelCaseString(""));
	}

	/**
	 * Test the camel case regular expression.
	 */
	@Test
	public void camelCasePatternMatchingPositive()
	{
		assertTrue(isCamelCaseString("camelCase"));
		assertTrue(isCamelCaseString("camelCase1234"));
	}

	/**
	 * Test the camel case regular expression.
	 */
	@Test
	public void camelCasePatternMatchingNegative()
	{
		assertFalse(isCamelCaseString("1camelCase"));
		assertFalse(isCamelCaseString("1CamelCase"));
		assertFalse(isCamelCaseString("CamelCase"));
		assertFalse(isCamelCaseString("CamelCase1234"));
	}

	/**
	 * Test the package name regular expression.
	 */
	@Test
	public void packagePatternMatchingEmpty()
	{
		assertFalse(isPackageNameString(null));
		assertFalse(isPackageNameString(""));
	}

	/**
	 * Test the package name regular expression.
	 */
	@Test
	public void packagePatternMatchingPositive()
	{
		assertTrue(isPackageNameString("package"));
		assertTrue(isPackageNameString("package1234"));
	}

	/**
	 * Test the package name regular expression.
	 */
	@Test
	public void packagePatternMatchingNegative()
	{
		assertFalse(isPackageNameString("1CamelCase"));
		assertFalse(isPackageNameString("1camelCase"));
		assertFalse(isPackageNameString("CamelCase"));
		assertFalse(isPackageNameString("CamelCase1234"));
	}

	/**
	 * Test the class name regular expression.
	 */
	@Test
	public void classPatternMatchingEmpty()
	{
		assertFalse(isClassNameString(null));
		assertFalse(isClassNameString(""));
	}

	/**
	 * Test the class name regular expression.
	 */
	@Test
	public void classPatternMatchingPositive()
	{
		assertTrue(isClassNameString("Clazz"));
		assertTrue(isClassNameString("Clazz1234"));
	}

	/**
	 * Test the class name regular expression.
	 */
	@Test
	public void classPatternMatchingNegative()
	{
		assertFalse(isClassNameString("1clazz"));
		assertFalse(isClassNameString("clazz"));
		assertFalse(isClassNameString("clazzCamelCase1234"));
	}

	/**
	 * Test chaining collections into a delimited string.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void chainList()
	{
		final List<Integer> collection = CollectionUtil.newList();
		collection.add(3);
		collection.add(2);
		collection.add(1);
		assertEquals("3:2:1", StringUtil.chain(collection, ":"));
	}

	/**
	 * Test chaining collections into a delimited string.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void chainSortedSet()
	{
		final SortedSet<Integer> collection = CollectionUtil.newSortedSet();
		collection.add(3);
		collection.add(2);
		collection.add(1);
		assertEquals("1:2:3", StringUtil.chain(collection, ":"));
	}

	/**
	 * Test chaining collections into a delimited string.
	 */
	@Test
	public void chainNoElements()
	{
		final SortedSet<Integer> collection = CollectionUtil.newSortedSet();
		assertEquals("", StringUtil.chain(collection, ":"));
	}

	/**
	 * Test chaining collections into a delimited string.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void chainSingleElement()
	{
		final SortedSet<Integer> collection = CollectionUtil.newSortedSet();
		collection.add(3);
		assertEquals("3", StringUtil.chain(collection, ":"));
	}

	/**
	 * Various padding using Apache commons.
	 */
	@Test
	public void coreApicenterWithSpace()
	{
		assertEquals("====== Header ======", centerWithSpace("Header", 20, "="));
		assertEquals("==== Big Header ====", centerWithSpace("Big Header", 20, "="));
		assertEquals("=== Even Header ====", centerWithSpace("Even Header", 20, "="));
	}

	/**
	 * Test escaping HTML text.
	 * 
	 * @see https://jira.chpc.utah.edu/browse/FUR-964
	 */
	@Test
	public void escapeHtmlWithSpecialCharacters()
	{
		final String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>";
		final String escapedString = "&#x003C;?xml version=&#x0022;1.0&#x0022; encoding=&#x0022;utf-8&#x0022; standalone=&#x0022;yes&#x0022;?&#x003E;";
		assertEquals(escapedString, StringUtil.escapeHtml(xmlString));
	}

	// ========================= PRIVATE METHODS ===========================
}
