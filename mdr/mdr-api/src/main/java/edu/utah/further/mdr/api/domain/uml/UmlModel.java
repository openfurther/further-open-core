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
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.ACTIVE;
import static edu.utah.further.mdr.api.domain.uml.ElementType.MODEL;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.context.Api;

/**
 * A UML model root element. It is a package with extra capabilities of caching all
 * classes in a flat representation, which is easier to access.
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
public class UmlModel extends UmlPackage
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Class element collection of the entire model. Keyed by the class' fully qualified
	 * name.
	 */
	private final Map<String, UmlClass> classCollection = newMap();

	/**
	 * A child package designated as the default model package. Classes that are not under
	 * any other package are to be added under this default package.
	 */
	private UmlPackage defaultPackage;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a package from a full name.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param modelName
	 *            model name
	 */
	public UmlModel(final String xmiId, final String modelName)
	{
		super(xmiId, modelName, MODEL);
		// By default, a model is in active state
		setStatus(ACTIVE);
	}

	// ========================= IMPLEMENTATION: Object ====================


	// ========================= IMPLEMENTATION: UmlElement ================

	/**
	 * Set a new value for the parent property. Update the package names accordingly.
	 *
	 * @param parent
	 *            the parent to set
	 */
	@Override
	public void setParent(final UmlElement parent)
	{
		setParent(parent, false);
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

	// ========================= METHODS ===================================

	/**
	 * Add a class to the model's class collection.
	 *
	 * @param clazz
	 *            class to add
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public void addClass(final UmlClass clazz)
	{
		classCollection.put(clazz.getQualifiedName(), clazz);
	}

	/**
	 * Return a class that matches a fully qualified name.
	 *
	 * @param classQualifiedName
	 *            class fully qualified name
	 *
	 * @return the corresponding class. If not found, returns <code>null</code>
	 */
	public UmlClass getClassByQualifiedName(final String classQualifiedName)
	{
		return classCollection.get(classQualifiedName);
	}

	/**
	 * Return the list of classes that match a class name.
	 *
	 * @param className
	 *            class name (NOT fully qualified)
	 *
	 * @return the list of matching classes. If not found, returns an empty list
	 */
	public List<UmlClass> getClassByName(final String className)
	{
		List<UmlClass> classList = newList();
		if (!isBlank(className))
		{
			for (UmlClass clazz : classCollection.values())
			{
				if (className.equals(clazz.getName()))
				{
					classList.add(clazz);
				}
			}
		}
		return classList;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the classCollection property.
	 *
	 * @return the classCollection
	 */
	public Map<String, UmlClass> getClassCollection()
	{
		return unmodifiableMap(classCollection);
	}

	/**
	 * Return the defaultPackage property.
	 *
	 * @return the defaultPackage
	 */
	public UmlPackage getDefaultPackage()
	{
		return defaultPackage;
	}

	/**
	 * Set a new value for the defaultPackage property. If the argument is not yet a child
	 * of this object, it is added as a child.
	 *
	 * @param defaultPackage
	 *            the defaultPackage to set
	 */
	public void setDefaultPackage(UmlPackage defaultPackage)
	{
		addChild(defaultPackage, false);
		this.defaultPackage = defaultPackage;
	}

	// ========================= PRIVATE METHODS ===========================
}
