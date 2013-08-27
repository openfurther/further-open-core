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
package edu.utah.further.mdr.api.domain.uml;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.Strings.PROPERTY_SCOPE_CHAR;
import static edu.utah.further.core.api.text.StringUtil.newStringBuilder;
import static edu.utah.further.mdr.api.domain.uml.ElementType.PACKAGE;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Iterator;
import java.util.List;

import edu.utah.further.core.api.context.Api;

/**
 * A UML package name. Wraps a list of strings and can be read from a standard
 * dot-delimited package name string. Immutable.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Api
public class UmlPackage extends UmlElementImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * Default package.
	 */
	public static final String DEFAULT_PACKAGE = "defaultpackage";

	// ========================= FIELDS ====================================

	/**
	 * Class package names, sorted by the Java nesting order, e.g. {"Java","util"}.
	 */
	private List<String> packageNames;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a package from a full name.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param fullName
	 *            fully qualified package name, e.g. "java.util.lang"
	 */
	public UmlPackage(final String xmiId, final String fullName)
	{
		this(xmiId, fullName, PACKAGE);
	}

	/**
	 * Initialize a package from a full name.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param fullName
	 *            fully qualified package name, e.g. "java.util.lang"
	 * @param type
	 *            element's type
	 */
	protected UmlPackage(final String xmiId, final String fullName, final ElementType type)
	{
		super(xmiId, fullName, type);
		if (isBlank(fullName))
		{
			setName(DEFAULT_PACKAGE);
		}
		final String[] parts = getName().split("\\" + PROPERTY_SCOPE_CHAR);
		this.packageNames = newList(parts);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the fully qualified name of the package, e.g. "java.util.lang"
	 *
	 * @return the fully qualified name of the package, e.g. "java.util.lang"
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return delimitPackageNames(packageNames);
	}

	// ========================= IMPLEMENTATION: Visitable<ItemVisitor> ===

	/**
	 * Let a visitor process this item. Part of the Visitor pattern. This calls back the
	 * visitor's <code>visit()</code> method with this item type.
	 *
	 * @param visitor
	 *            item data visitor whose <code>visit()</code> method is invoked
	 */
	@Override
	public void accept(final  UmlElementVisitor visitor)
	{
		visitor.visit(this);
	}

	// ========================= IMPLEMENTATION: UmlElement ================

	/**
	 * Convert a package list to a delimited string
	 *
	 * @param packageNames
	 *            Class package names, sorted by the Java nesting order, e.g.
	 *            {"Java","util"}.
	 * @return delimited string, e.g. "java.util"
	 */
	public static String delimitPackageNames(final List<String> packageNames)
	{
		final StringBuilder s = newStringBuilder();
		final int depth = packageNames.size();
		final int depthMinusOne = depth - 1;
		for (int i = 0; i < depth; i++)
		{
			s.append(packageNames.get(i));
			if (i < depthMinusOne)
			{
				s.append(PROPERTY_SCOPE_CHAR);
			}
		}
		return s.toString();
	}

	/**
	 * Set a new value for the parent property. Update the package names accordingly.
	 *
	 * @param parent
	 *            the parent to set
	 */
	@Override
	public void setParent(final UmlElement parent)
	{
		setParent(parent, true);
	}

	/**
	 * Set a new value for the parent property. Update the package names accordingly.
	 *
	 * @param parent
	 *            the parent to set
	 * @param updateName
	 *            if <code>true</code>, prepends the parent's name at the beginning of the
	 *            package name list
	 */
	protected void setParent(final UmlElement parent, boolean updateName)
	{
		super.setParent(parent);
		if (updateName)
		{
			// Update package list
			final UmlPackage parentPackage = (UmlPackage) parent;
			this.packageNames = newList(parentPackage.getPackageNames());
			packageNames.add(getName());

			// Update name
			setName(delimitPackageNames(packageNames));
		}
	}

	/**
	 * Return the simple name of this package, which is the last element of the package
	 * name list.
	 *
	 * @return the simple name of this package
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getSimpleName()
	 */
	@Override
	public String getSimpleName()
	{
		return packageNames.get(packageNames.size() - 1);
	}

	// ========================= METHODS ===================================

	/**
	 * Return the packageNames property.
	 *
	 * @return the packageNames
	 */
	public List<String> getPackageNames()
	{
		return unmodifiableList(packageNames);
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<String> iterator()
	{
		return packageNames.iterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public String getPackageName(final int index)
	{
		return packageNames.get(index);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty()
	{
		return packageNames.isEmpty();
	}

	/**
	 * Return the package tree level, which is the size of the package list.
	 *
	 * @return the package tree level, which is the size of the package list
	 * @see java.util.List#size()
	 */
	public int getLevel()
	{
		return packageNames.size();
	}

}
