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
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.mdr.api.domain.uml.ClassType.CADSR_LOCAL_VALUE_DOMAIN;
import static edu.utah.further.mdr.api.domain.uml.ElementType.MEMBER;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A UML class member element. Has a name and a {@link UmlClass} type.
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
public class UmlMember extends UmlElementImpl
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Name of this member's class (type).
	 */
	private final String memberClassName;

	/**
	 * Owning class.
	 */
	private final UmlClass parentClass;

	/**
	 * Member type. Set after this object is constructed.
	 */
	private UmlClass memberClass;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a member element.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param name
	 *            member name
	 * @param memberClassName
	 *            member class name
	 */
	public UmlMember(final String xmiId, final String name, final String memberClassName,
			final UmlClass parentClass)
	{
		super(xmiId, name, MEMBER);
		this.memberClassName = memberClassName;
		this.parentClass = parentClass;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).appendSuper(
				super.toString()).append("superClass", memberClass).toString();
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
	 * Return the qualified member name in the format "ClassName.memberName".
	 *
	 * @return the qualified member name
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getName()
	 */
	public String getQualifiedName()
	{
		return getParentClass().getQualifiedName() + PROPERTY_SCOPE_CHAR + getName();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the memberClassName property.
	 *
	 * @return the memberClassName
	 */
	public String getMemberClassName()
	{
		return memberClassName;
	}

	/**
	 * Return the memberClass property.
	 *
	 * @return the memberClass
	 */
	public UmlClass getMemberClass()
	{
		return memberClass;
	}

	/**
	 * Return the parentClass property.
	 *
	 * @return the parentClass
	 */
	public UmlClass getParentClass()
	{
		return parentClass;
	}

	/**
	 * Set a new value for the memberClass property.
	 *
	 * @param memberClass
	 *            the memberClass to set
	 */
	public void setMemberClass(final UmlClass memberClass)
	{
		this.memberClass = memberClass;
	}

	/**
	 * Return the valueSet property of the member's class.
	 *
	 * @return the valueSet, or an empty list if this is not a CADSR-LVD-type member
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementImpl#getValueSet()
	 */
	@Override
	public List<DtsConcept> getValueSet()
	{
		if ((memberClass == null)
				|| (memberClass.getClassType() != CADSR_LOCAL_VALUE_DOMAIN))
		{
			return newList();
		}
		final CaDsrLocalValueDomain domain = (CaDsrLocalValueDomain) memberClass;
		return domain.getValueSet();
	}

	// ========================= PRIVATE METHODS ===========================
}
