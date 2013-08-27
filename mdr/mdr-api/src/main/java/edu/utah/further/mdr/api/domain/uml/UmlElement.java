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

import java.util.Collection;
import java.util.List;

import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.tree.PrintableComposite;
import edu.utah.further.core.api.visitor.Visitable;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A UML model element, e.g. a Uml class or package. Uses the composite pattern to model
 * arbitrarily complex class hierarchies.
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
 * @version Jan 30, 2009
 */
public interface UmlElement extends Comparable<UmlElement>, Named,
		Visitable<UmlElement, UmlElementVisitor>, PrintableComposite<UmlElement>
{
	// ========================= METHODS ===================================

	/**
	 * Return the simple name of this element. A hook for sub-classes. By default, the
	 * element's name is returned
	 *
	 * @return the simple name of this element
	 */
	String getSimpleName();

	/**
	 * Add a new child element. If the child is already in the children collection, a
	 * {@link BusinessRuleException} is thrown.
	 *
	 * @param child
	 *            child element to add
	 */
	void addChild(UmlElement child);

	/**
	 * Add a new child element.
	 *
	 * @param child
	 *            child element to add
	 * @param boolean validateIfExists If <code>true</code> and the child is already in
	 *        the children collection, a {@link BusinessRuleException} is thrown.
	 *        Otherwise, this method has no effect
	 */
	void addChild(UmlElement child, boolean failIfChildExists);

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	void addChildren(Collection<? extends UmlElement> c);

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	void removeChild(UmlElement o);

	/**
	 * Return the element's type.
	 *
	 * @return the UML element type
	 */
	ElementType getElementType();

	/**
	 * Return the element's class type.
	 *
	 * @return the element's class type. Returns <code>null</code> if the element is not
	 *         of class type
	 */
	ClassType getClassType();

	/**
	 * Return the children property.
	 *
	 * @return the children
	 * @see edu.utah.further.core.math.tree.Composite#getChildren()
	 */
	@Override
	List<UmlElement> getChildren();

	/**
	 * Does this element contain this child in its children collection.
	 *
	 * @param child
	 *            child
	 * @return <code>true</code> iff child is already a child of this object
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	boolean containsChild(UmlElement child);

	/**
	 * Does this node have children.
	 *
	 * @return <code>true</code> if and only if the children collection is non-empty
	 * @see edu.utah.further.core.math.tree.Composite#getHasChildren()
	 */
	@Override
	boolean getHasChildren();

	/**
	 * Return a child that matches an XMI ID.
	 *
	 * @param childXmiId
	 *            child element XMI ID
	 *
	 * @return the corresponding child. If not found, returns <code>null</code>
	 */
	UmlElement getChildByXmiId(String childXmiId);

	/**
	 * Return the first child that matches a name.
	 *
	 * @param childName
	 *            child name
	 *
	 * @return the first corresponding child found. Do not rely on the ordering of the
	 *         children's map entries. If not found, returns <code>null</code>
	 */
	UmlElement getChildByName(String childName);

	/**
	 * Return the parent property.
	 *
	 * @return the parent
	 */
	UmlElement getParent();

	/**
	 * Set a new value for the parent property.
	 *
	 * @param parent
	 *            the parent to set
	 */
	void setParent(UmlElement parent);

	/**
	 * Return the status property.
	 *
	 * @return the status
	 */
	ElementStatus getStatus();

	/**
	 * Set a new value for the status property. Also sets the element's marker to the corresponding severity level
	 *
	 * @param status
	 *            the status to set
	 */
	void setStatus(ElementStatus status);

	/**
	 * Return the xmiId property.
	 *
	 * @return the xmiId
	 */
	String getXmiId();

	/**
	 * Return the valueSet property of the member's class or the class, if this is such an
	 * element. Otherwise, returns an empty list.
	 *
	 * @return the valueSet, or an empty list if this is not a CADSR-LVD or a
	 *         CADSR-LVD-type member
	 */
	List<DtsConcept> getValueSet();

	/**
	 * Return the documentation property.
	 *
	 * @return the documentation
	 */
	String getDocumentation();

	/**
	 * Set a new value for the documentation property.
	 *
	 * @param documentation
	 *            the documentation to set
	 */
	void setDocumentation(String documentation);

	/**
	 * Return the marker of this icon.
	 *
	 * @return does this element or one of its descendants have associated element loading
	 *         errors.
	 */
	Severity getMarker();

	/**
	 * Set a new value for the marker property.
	 *
	 * @param marker
	 *            the marker to set
	 */
	void setMarker(Severity marker);
}