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
package edu.utah.further.mdr.impl.uml;

import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.math.tree.TraversalPrinter;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlPackage;
import edu.utah.further.mdr.impl.service.util.UmlUtil;

/**
 * Test UML utilities.
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
 * @version Dec 5, 2008
 * @see http://www.ibm.com/developerworks/library/x-javaxpathapi.html#changed
 */
@UnitTest
public final class UTestUmlUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestUmlUtil.class);

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 *
	 */
	@Before
	public void setUp()
	{
	}

	/**
	 *
	 */
	@After
	public void tearDown()
	{
	}

	// ========================= METHODS ===================================

	/**
	 * Find all UML tagged elements with a "DTSNamespace" tag.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buildPackageTree()
	{
		final SortedSet<UmlPackage> packages = nameListToPackageList(asList("a.b",
				"a.b.c", "a.b.d", "a.e"));
		final UmlPackage root = newUmlPackage("a");
		buildAndPrintTree(packages, root);
	}

	/**
	 * Find all UML tagged elements with a "DTSNamespace" tag.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buildPackageTreeDiscrepancyCommonPrefix()
	{
		final SortedSet<UmlPackage> packages = nameListToPackageList(asList("Model.a.b",
				"Model.a.b.c", "Model.a.b.d", "Model.a.e"));
		final UmlPackage root = newUmlPackage("Model.a");
		buildAndPrintTree(packages, root);
	}

	/**
	 * Find all UML tagged elements with a "DTSNamespace" tag.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buildPackageTreeDiscrepancyBetweenRootLevelAndChildrenLevels()
	{
		final SortedSet<UmlPackage> packages = nameListToPackageList(asList("Model.a.b",
				"Model.a.b.c", "Model.a.b.d", "Model.a.e"));
		final UmlPackage root = newUmlPackage("Model");
		buildAndPrintTree(packages, root);
	}

	/**
	 * EA_Model XMI file example of package structure.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buildPackageTreeEaModelExample()
	{
		final SortedSet<UmlPackage> packages = nameListToPackageList(asList(
				"EA_Model.Model.Java.lang", "EA_Model.Model.Java.util",
				"EA_Model.Model.ModelsInDevelopment.Encounter",
				"EA_Model.Model.ModelsInDevelopment.biospecimen",
				"EA_Model.Model.ValueDomains.ValueDomains",
				"EA_Model.Model.edu.utah.Person", "EA_Model.Model.edu.utah.order",
				"EA_Model.defaultpackage"));
		final UmlPackage root = newUmlPackage("EA_Model");
		buildAndPrintTree(packages, root);
	}

	/**
	 * EA_Model XMI file example of package structure.
	 * 
	 * @throws Exception
	 */
	@Test
	public void buildPackageTreeEaModelExampleCaseStudy()
	{
		final SortedSet<UmlPackage> packages = nameListToPackageList(asList("a.b.c",
				"a.b.d", "a.b.d.e", "a.b.d.e.f", "a.g", "a.g.h"));
		final UmlPackage root = newUmlPackage("a");
		buildAndPrintTree(packages, root);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param packages
	 * @param root
	 */
	private void buildAndPrintTree(final SortedSet<UmlPackage> packages,
			final UmlPackage root)
	{
		UmlUtil.buildPackageTree(root, packages);
		log.debug("Set : " + packages);
		final TraversalPrinter<UmlElement> printer = new TraversalPrinter<UmlElement>(
				root);
		printer.setLineFeed("\n");
		// printer.setIndent(0);
		// printer.setSpace(new StringBuilder(""));
		log.debug("Tree:\n" + printer.toString());
	}

	/**
	 * Convert a collection of package names to a sorted set of package objects.
	 * 
	 * @param names
	 *            package name collection
	 */
	private SortedSet<UmlPackage> nameListToPackageList(final Collection<String> names)
	{
		final SortedSet<UmlPackage> packages = newSortedSet();
		for (final String name : names)
		{
			packages.add(newUmlPackage(name));
		}
		return packages;
	}

	/**
	 * @param name
	 * @return
	 */
	private static UmlPackage newUmlPackage(String name)
	{
		return new UmlPackage("PKG_" + name, "a");
	}
}