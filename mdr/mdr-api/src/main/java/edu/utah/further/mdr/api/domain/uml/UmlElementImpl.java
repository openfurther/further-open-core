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
import static edu.utah.further.core.api.message.Messages.cannotAddMessage;
import static edu.utah.further.core.api.message.Severity.NONE;
import static edu.utah.further.core.api.message.ValidationUtil.validateNotEmpty;
import static edu.utah.further.core.api.text.StringUtil.getNameNullSafe;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.NOT_LOADED;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A UML model element base class. Uses the composite pattern to model arbitrarily complex
 * class hierarchies.
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
public abstract class UmlElementImpl implements UmlElement
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UmlElementImpl.class);

	// ========================= FIELDS ====================================

	// /**
	// * A unique identifier. May be later replaced/aided by a database key.
	// */
	// private final UUID id = UUID.randomUUID();

	/**
	 * A unique identifier of the element within the XMI file. May be later replaced/aided
	 * by a database key. If <code>null</code>, this is a synthetic element that does not
	 * appear in the original XMI file.
	 */
	private final String xmiId;

	/**
	 * Element name.
	 */
	private String name;

	/**
	 * A description of the element.
	 */
	private String documentation;

	/**
	 * Element type.
	 */
	private final ElementType elementType;

	/**
	 * List of children elements, identified by their XMI IDs.
	 */
	private final Map<String, UmlElement> children = newMap();

	/**
	 * Parent element of this element. Set by the parent when this object is added as its
	 * child.
	 */
	private UmlElement parent;

	/**
	 * Tracks the element loading status.
	 */
	private ElementStatus status = NOT_LOADED;

	/**
	 * This flag keeps track of whether this element or one of its descendants have
	 * associated element loading errors.
	 */
	private Severity marker = NONE;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an element.
	 *
	 * @param xmiId
	 *            XMI file unique identifier of the element
	 * @param name
	 *            element's name
	 * @param type
	 *            element's type
	 */
	protected UmlElementImpl(final String xmiId, final String name, final ElementType type)
	{
		super();
		validateNotEmpty("XMI ID", xmiId);
		this.xmiId = xmiId;
		this.name = name;
		this.elementType = type;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
		// XMI ID too long for human readability
				/* .append("xmiId", xmiId) */
				.append("name", name).append("elementType", elementType).append("status",
						status).append("parent", getNameNullSafe(parent)).toString();
	}

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(name).append(elementType).toHashCode();
	}

	/**
	 * Element equality is based on name and type. Note that this slightly restricts our
	 * UML modeling scope.
	 *
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (getClass() != o.getClass()) // Very important!
			return false;

		final UmlElementImpl that = (UmlElementImpl) o;
		return new EqualsBuilder().append(name, that.name).append(elementType,
				that.elementType).isEquals();
	}

	// ========================= IMPLEMENTATION: Comparable<UmlElement> ====

	/**
	 * Compare two pairs by lexicographic full name order.
	 *
	 * @param other
	 *            the other {@link UmlElementImpl} to be compared with this one.
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final UmlElement other)
	{
		return new CompareToBuilder().append(this.getName(), other.getName())
				.toComparison();
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= IMPLEMENTATION: PrintableComposite<UmlE.> =

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#printData()
	 */
	@Override
	public String printData()
	{
		return toString();
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
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getSimpleName()
	 */
	@Override
	public String getSimpleName()
	{
		return name;
	}

	/**
	 * @param child
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#addListener(edu.utah.further.mdr.api.domain.uml.UmlElementImpl)
	 */
	@Override
	public void addChild(final UmlElement child)
	{
		addChild(child, true);
	}

	/**
	 * @param child
	 * @param failIfChildExists
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#addChild(edu.utah.further.mdr.api.domain.uml.UmlElementImpl,
	 *      boolean)
	 */
	@Override
	public void addChild(final UmlElement child, final boolean failIfChildExists)
	{
		// Only add valid children -- automatically guarantees hierarchy's integrity
		validateChildType(child);

		final String childKey = child.getXmiId();
		if (children.containsKey(childKey))
		{
			if (failIfChildExists)
			{
				throw new BusinessRuleException(cannotAddMessage(quote(childKey),
						toString(), "Child with XMI ID " + quote(childKey)
								+ " already exists under " + quote(name)));
			}
		}
		else
		{
			children.put(child.getXmiId(), child);
			child.setParent(this);
		}
	}

	/**
	 * @param c
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#addChildren(java.util.Collection)
	 */
	@Override
	public void addChildren(final Collection<? extends UmlElement> c)
	{
		for (final UmlElement child : c)
		{
			addChild(child);
		}
	}

	/**
	 * @param o
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#removeChild(edu.utah.further.core.dts.domain.uml.UmlElementI)
	 */
	@Override
	public void removeChild(final UmlElement o)
	{
		children.remove(o);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getElementType()
	 */
	@Override
	public ElementType getElementType()
	{
		return elementType;
	}

	/**
	 * Return the classType property.
	 *
	 * @return returns <code>null</code> here; override by class-type sub-classes
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getClassType()
	 */
	@Override
	public ClassType getClassType()
	{
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getChildren()
	 */
	@Override
	public List<UmlElement> getChildren()
	{
		return newList(children.values());
	}

	/**
	 * @param child
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#containsChild(edu.utah.further.core.dts.domain.uml.UmlElementI)
	 */
	@Override
	public boolean containsChild(final UmlElement child)
	{
		final String childKey = child.getXmiId();
		return children.containsKey(childKey);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return !children.isEmpty();
	}

	/**
	 * @param childXmiId
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getChildByXmiId(java.lang.String)
	 */
	@Override
	public UmlElement getChildByXmiId(final String childXmiId)
	{
		return children.get(childXmiId);
	}

	/**
	 * @param childName
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getChildByName(java.lang.String)
	 */
	@Override
	public UmlElement getChildByName(final String childName)
	{
		if (childName == null)
		{
			return null;
		}
		for (final UmlElement child : children.values())
		{
			if (childName.equals(child.getName()))
			{
				return child;
			}
		}
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getParent()
	 */
	@Override
	public UmlElement getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#setParent(edu.utah.further.mdr.api.domain.uml.UmlElementImpl)
	 */
	@Override
	public void setParent(final UmlElement parent)
	{
		this.parent = parent;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getStatus()
	 */
	@Override
	public ElementStatus getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#setStatus(edu.utah.further.mdr.api.domain.uml.ElementStatus)
	 */
	@Override
	public void setStatus(final ElementStatus status)
	{
		this.status = status;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElementI#getXmiId()
	 */
	@Override
	public String getXmiId()
	{
		return xmiId;
	}

	// /**
	// * Return the unique identifier of this object.
	// *
	// * @return the UUID
	// */
	// public final UUID getId()
	// {
	// return id;
	// }

	/**
	 * Return the valueSet property of the member's class or the class, if this is such an
	 * element. Otherwise, returns an empty list.
	 *
	 * @return the valueSet, or an empty list if this is not a CADSR-LVD or a
	 *         CADSR-LVD-type member
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElement#getValueSet()
	 */
	@Override
	public List<DtsConcept> getValueSet()
	{
		return newList();
	}

	/**
	 * Return the documentation property.
	 *
	 * @return the documentation
	 */
	@Override
	public String getDocumentation()
	{
		return documentation;
	}

	/**
	 * Set a new value for the documentation property.
	 *
	 * @param documentation
	 *            the documentation to set
	 */
	@Override
	public void setDocumentation(String documentation)
	{
		this.documentation = documentation;
	}

	/**
	 * Return the marker of this icon.
	 *
	 * @return does this element or one of its descendants have associated element loading
	 *         errors.
	 */
	@Override
	public Severity getMarker()
	{
		return marker;
	}

	/**
	 * Set a new value for the marker property.
	 *
	 * @param marker
	 *            the marker to set
	 */
	@Override
	public void setMarker(Severity marker)
	{
		// log.warn("Setting marker from " + this.marker + " to " + marker + " for " +
		// this);
		this.marker = marker;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set a new value for the name property.
	 *
	 * @param name
	 *            the name to set
	 */
	protected void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Validate: is a child of a valid for insertion under this element.
	 *
	 * @param child
	 *            prospective child element
	 */
	private void validateChildType(final UmlElement child)
	{
		if (!elementType.getChildren().contains(child.getElementType()))
		{
			throw new BusinessRuleException(cannotAddMessage(child.getElementType(),
					elementType, "incompatible parent/child item types"));
		}
	}
}
