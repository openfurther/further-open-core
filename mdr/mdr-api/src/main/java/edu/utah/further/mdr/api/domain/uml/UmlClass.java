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

import static edu.utah.further.core.api.constant.Strings.PROPERTY_SCOPE_CHAR;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.mdr.api.domain.uml.ElementType.CLASS;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * A UML class model element. Supports a single inheritance with no interfaces for now.
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
public class UmlClass extends UmlElementImpl
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Class type. Required.
	 */
	private final ClassType classType;

	/**
	 * Super-class name. Required.
	 */
	private final String superClassName;

	/**
	 * Optional super-class.
	 */
	private UmlClass superClass;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an element.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param name
	 *            element's name
	 * @param classType
	 *            class type
	 * @param superClassName
	 *            super class name
	 */
	protected UmlClass(final String xmiId, final String name, final ClassType classType,
			final String superClassName)
	{
		super(xmiId, name, CLASS);
		this.classType = classType;
		this.superClassName = superClassName;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).appendSuper(
				super.toString()).append("classType", classType).append("superClass",
				superClass).toString();
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

	// ========================= IMPLEMENTATION: UmlElement ===============

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getParent()
	 */
	@Override
	public UmlPackage getParent()
	{
		return (UmlPackage) super.getParent();
	}

	// ========================= METHODS ===================================

	/**
	 * Return all members of this class and all its super-classes. This class' members
	 * appear first on the list, then its super-class members, then its super-super-class,
	 * and so on. If name collisions are
	 *
	 * @return list of all members of this class
	 */
	public List<UmlElement> getAllMembers()
	{
		final List<UmlElement> members = getChildren();
		UmlClass someSuperClass = getSuperClass();
		// Climb up the class hierarchy, add members and check for name collisions
		while (someSuperClass != null)
		{
			final List<UmlElement> superMembers = someSuperClass.getChildren();
			for (final UmlElement superMember : superMembers)
			{
				// TODO: change this inefficient way of checking for name collisions. Does
				// it scale or is it quadratic?
				if (members.contains(superMember))
				{
					throw new BusinessRuleException("Member name collision: "
							+ quote(((UmlMember) superMember).getQualifiedName())
							+ " appears twice in " + quote(getName())
							+ " and its super-classes");
				}
				members.add(superMember);
			}
			someSuperClass = someSuperClass.getSuperClass();
		}
		return members;
	}

	/**
	 * Return the qualified class name in the format "packageQualifiedName.className".
	 *
	 * @return the qualified class name
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getName()
	 */
	public String getQualifiedName()
	{
		return getParent().toString() + PROPERTY_SCOPE_CHAR + getName();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the classType property.
	 *
	 * @return the classType
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementImpl#getClassType()
	 */
	@Override
	public ClassType getClassType()
	{
		return classType;
	}

	/**
	 * Return the superClass property.
	 *
	 * @return the superClass
	 */
	public UmlClass getSuperClass()
	{
		return superClass;
	}

	/**
	 * Set a new value for the superClass property.
	 *
	 * @param superClass
	 *            the superClass to set
	 */
	public void setSuperClass(final UmlClass superClass)
	{
		this.superClass = superClass;
	}

	/**
	 * Return the superClassName property.
	 *
	 * @return the superClassName
	 */
	public String getSuperClassName()
	{
		return superClassName;
	}

	// ========================= PRIVATE METHODS ===========================
}
