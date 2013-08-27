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
package edu.utah.further.mdr.impl.service.util;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.collections.CollectionUtil.startsWith;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.mdr.api.domain.uml.UmlPackage.delimitPackageNames;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessage;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.core.math.tree.TraversalPrinter;
import edu.utah.further.mdr.api.domain.uml.ClassType;
import edu.utah.further.mdr.api.domain.uml.UmlClass;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.domain.uml.UmlPackage;
import edu.utah.further.mdr.api.service.uml.XmiParser;

/**
 * UML/XMI model utilities.
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
 * @version Mar 23, 2009
 */
@Utility
public final class UmlUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UmlUtil.class);

	// ========================= PUBLIC CONSTANTS ==========================

	/**
	 * Compare UML elements by name.
	 */
	public static final Comparator<UmlElement> comparatorByName = new Comparator<UmlElement>()
	{
		/**
		 * @param o1
		 * @param o2
		 * @return
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final UmlElement o1, final UmlElement o2)
		{
			return o1.getName().compareTo(o2.getName());
		}

	};

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private UmlUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the list of classes in a model by class type.
	 * <p>
	 * Warning: works only for flat models (all classes are under the root model node).
	 *
	 * @param model
	 *            UML model
	 * @param classType
	 *            class type. If <code>null</code>, returns all classes
	 * @param pkg
	 *            only classes in this package or any of its sub-packages will be
	 *            returned, if this parameter is non-<code>null</code>
	 * @return list of classes of type <code>classType</code> under the model
	 */
	public static Map<String, UmlClass> getClasses(final UmlModel model,
			final ClassType classType, final UmlPackage pkg)
	{
		return getClasses(model.getClassCollection(), classType, pkg);
	}

	/**
	 * Return the sub-set of classes in a collection of classes by class type.
	 *
	 * @param classSet
	 *            class type. If <code>null</code>, returns all classes
	 * @param pkg
	 *            only classes in this package or any of its sub-packages will be
	 *            returned, if this parameter is non-<code>null</code>
	 * @return list of classes of type <code>classType</code> under the model
	 */
	public static Map<String, UmlClass> getClasses(final Map<String, UmlClass> classSet,
			final ClassType classType, final UmlPackage pkg)
	{
		final Map<String, UmlClass> classCollection = newMap();
		final boolean isClassType = (classType != null);
		final boolean isPackage = (pkg != null) && !pkg.isEmpty();
		for (final Map.Entry<String, UmlClass> entry : classSet.entrySet())
		{
			final UmlClass clazz = entry.getValue();
			// Negative checks to eliminate irrelevant classes
			if (isClassType && (classType != clazz.getClassType()))
			{
				continue;
			}
			if (isPackage && !isInPackage(clazz, pkg))
			{
				continue;
			}
			classCollection.put(clazz.getQualifiedName(), clazz);
		}
		return classCollection;
	}

	/**
	 * Print an XMI parser messages after parsing a file.
	 *
	 * @param parser
	 *            XMI parser
	 */
	public static void printMessages(final XmiParser parser)
	{
		if (log.isDebugEnabled())
		{
			final SeverityMessageContainer messages = parser.getMessages();
			String s = "===== " + messages.getSize() + " parser messages: ===== "
					+ (messages.isEmpty() ? "None" : "") + NEW_LINE_STRING;
			for (final SeverityMessage message : messages)
			{
				s = s + message + NEW_LINE_STRING;
			}
			log.debug("\n\n" + s);
		}
	}

	/**
	 * Is class in a package. That is, does the class package list start with the
	 * <code>packageNames</code> sub-list.
	 *
	 * @param clazz
	 *            UML class
	 * @param pkg
	 *            package
	 * @return is class in the package or any of its sub-packages
	 */
	public static boolean isInPackage(final UmlClass clazz, final UmlPackage pkg)
	{
		final UmlPackage classPackage = clazz.getParent();
		return (classPackage == null) ? false : startsWith(
				classPackage.getPackageNames(), pkg.getPackageNames());
	}

	/**
	 * Convert a sorted list of packages to a hierarchical element tree.
	 *
	 * @param root
	 *            root element of the tree to add packages under
	 * @param packages
	 *            package list
	 */
	public static void buildPackageTree(final UmlPackage root,
			final SortedSet<UmlPackage> packages)
	{
		for (final UmlPackage pkg : packages)
		{
			final List<String> packageNames = pkg.getPackageNames();
			if (log.isDebugEnabled())
			{
				log.debug("=== Processing package: " + pkg + " level " + pkg.getLevel());
				log.debug("Package children = " + pkg.getChildren());
			}
			UmlPackage node = root;
			final List<String> intermediatePackageNames = newList();
			final int last = packageNames.size() - 1;
			for (int i = 0; i < packageNames.size(); i++)
			{
				final String packageName = packageNames.get(i);
				if (log.isDebugEnabled())
				{
					log.debug("packageName " + packageName + " level ");
				}
				intermediatePackageNames.add(packageName);
				final String intermediatePackage = delimitPackageNames(intermediatePackageNames);
				if (i == 0)
				{
					// Ignore root level, root already exists
					continue;
				}

				// Create intermediate packages between the root and pkg if needed
				if (i < last)
				{
					UmlPackage childPkg = (UmlPackage) node
							.getChildByName(intermediatePackage);
					if (childPkg == null)
					{
						childPkg = new UmlPackage("PKG_" + intermediatePackage,
								intermediatePackage);
						if (log.isDebugEnabled())
						{
							log.debug("Adding intermediate " + childPkg + " under "
									+ node);
						}
						if (node.getChildByName(intermediatePackage) == null)
						{
							node.addChild(childPkg);
						}
					}
					node = childPkg;
				}
				else
				{
					// Add the package as a new leaf in the tree
					if (log.isDebugEnabled())
					{
						log.debug("Adding leaf " + pkg + " under " + node);
					}
					node.addChild(pkg);
				}
			}
		}
	}

	/**
	 * @param root
	 */
	public static void printModelTree(final UmlPackage root)
	{
		final TraversalPrinter<UmlElement> printer = new TraversalPrinter<UmlElement>(
				root);
		printer.setLineFeed("\n");
		log.debug("Model Tree:\n" + printer.toString());
	}

	/**
	 * If the UML element has errors, mark the entire element ancestry (including this
	 * object) as having errors of at least the same severity level as the element's
	 * marker. (The marker severity level of a parent is the maximum over the levels of
	 * its descendants markers).
	 * <p>
	 * Then we get an eclipse-like explorer display where an error is easy to find because
	 * it propagates up the hierarchy -- up to the very top.
	 *
	 * @param element
	 *            UML element
	 */
	public static void updateAncestryMarks(final UmlElement element)
	{
		final Severity elementMarker = element.getStatus().getSeverity();
		// log.warn("Updating ancenstry of " + element + " according to marker "
		// + elementMarker);
		UmlElement node = element;
		while (node != null)
		{
			// parent.marker <- max(parent.marker, element.marker)
			if (elementMarker.compareTo(node.getMarker()) > 0)
			{
				node.setMarker(elementMarker);
			}
			node = node.getParent();
		}
	}
}
