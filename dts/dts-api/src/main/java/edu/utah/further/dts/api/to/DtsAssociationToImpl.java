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
package edu.utah.further.dts.api.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.dts.api.domain.association.EnumAssociationType.ASSOCIATION;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsAssociationType;
import edu.utah.further.dts.api.domain.association.EnumAssociationType;
import edu.utah.further.dts.api.domain.attribute.DtsQualifier;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS association implementation and a transfer object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------<br>
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsAssociationToImpl.ENTITY_NAME, propOrder =
{ "name", "value", "associationType" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsAssociationToImpl.ENTITY_NAME)
public class DtsAssociationToImpl implements DtsAssociationTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "associationTo";

	// ========================= FIELDS ====================================

	/**
	 * Type-safe association type.
	 */
	@XmlTransient
	@Final
	private EnumAssociationType enumAssociationType;

	/**
	 * Data fields: Association.
	 */

	@XmlElement(name = "name", nillable = false)
	private String name;

	@XmlElement(name = "value", nillable = false)
	private String value;

	/**
	 * Associations of this object with other entities.
	 */

	@XmlElement(name = "associationType", required = false)
	private DtsAssociationTypeToImpl associationType;

	@XmlTransient
	private DtsConceptUniqueId fromItemId;

	@XmlTransient
	private DtsConceptUniqueId toItemId;

	/**
	 * If <code>true</code>, this is an association for which we need to output only the
	 * "fromItem" link. Otherwise, we output only the "toItem" link.
	 */
	@XmlTransient
	private final boolean fromItemAssociation;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * An explicit no-argument constructor is required for JAXB.
	 */
	public DtsAssociationToImpl()
	{
		this(true);
	}

	/**
	 * Initialize an association TO.
	 * 
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is an association for which we need to output
	 *            only the "fromItem" link. Otherwise, we output only the "toItem" link
	 */
	public DtsAssociationToImpl(boolean fromItemAssociation)
	{
		this(ASSOCIATION, fromItemAssociation);
	}

	/**
	 * Initialize an association TO.
	 * 
	 * @param enumAssociationType
	 *            type-safe association type of this object
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is an association for which we need to output
	 *            only the "fromItem" link. Otherwise, we output only the "toItem" link
	 */
	protected DtsAssociationToImpl(final EnumAssociationType enumAssociationType,
			boolean fromItemAssociation)
	{
		super();
		this.enumAssociationType = enumAssociationType;
		this.fromItemAssociation = fromItemAssociation;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @param o
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		// return namespace.equals(arg0);
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}

		// Works only because this method is final!!
		// if (getClass() != o.getClass())
		if (!ReflectionUtil.instanceOf(o, DtsAssociationToImpl.class))
		{
			return false;
		}

		final DtsAssociationToImpl that = (DtsAssociationToImpl) o;
		return new EqualsBuilder().append(this.name, that.name).append(this.value,
				that.value).append(this.associationType, that.associationType).isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder()
				.append(name)
				.append(value)
				.append(associationType)
				.toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("name", name)
				.append("value", value)
				.append("associationType", associationType)
				.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsAssociationToImpl copyFrom(final DtsAssociation other)
	{
		if (other == null)
		{
			return this;
		}
		this.name = other.getName();
		this.value = other.getValue();

		// Deep-copy associations
		this.associationType = new DtsAssociationTypeToImpl().copyFrom(other
				.getAssociationType());
		if (fromItemAssociation)
		{
			final DtsConcept fromItem = other.getFromItem();
			setFromItemId((fromItem == null) ? null : fromItem.getAsUniqueId());
			setToItemId(null);
		}
		else
		{
			final DtsConcept toItem = other.getToItem();
			setToItemId((toItem == null) ? null : toItem.getAsUniqueId());
			setFromItemId(null);
		}
		return this;
	}

	// ========================= IMPLEMENTATION: DtsAssociation ============

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getEnumAssociationType()
	 */
	@Override
	public EnumAssociationType getEnumAssociationType()
	{
		return enumAssociationType;
	}

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Return the associationType property.
	 * 
	 * @return the associationType
	 */
	@Override
	public DtsAssociationType getAssociationType()
	{
		return associationType;
	}

	/**
	 * @param associationType
	 * @see com.apelon.dts.client.association.Association#setAssociationType(com.apelon.dts.client.association.AssociationType)
	 */
	public void setAssociationType(final DtsAssociationType associationType)
	{
		this.associationType = new DtsAssociationTypeToImpl().copyFrom(associationType);
	}

	/**
	 * Return the value property.
	 * 
	 * @return the value
	 */
	@Override
	public String getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 * 
	 * @param value
	 *            the value to set
	 */
	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getFromItem()
	 */
	@Override
	public DtsConcept getFromItem()
	{
		throw new UnsupportedOperationException("Use REST link method instead");
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getToItem()
	 */
	@Override
	public DtsConcept getToItem()
	{
		throw new UnsupportedOperationException("Use REST link method instead");
	}

	// ========================= IMPLEMENTATION: DtsAssociationTo ==========

	/**
	 * Return the fromItemId property.
	 * 
	 * @return the fromItemId
	 */
	@Override
	public DtsConceptUniqueId getFromItemId()
	{
		return fromItemId;
	}

	/**
	 * Set a new value for the fromItemId property.
	 * 
	 * @param fromItemId
	 *            the fromItemId to set
	 */
	@Override
	public void setFromItemId(final DtsConceptUniqueId fromItemId)
	{
		this.fromItemId = fromItemId;
	}

	/**
	 * Return the toItemId property.
	 * 
	 * @return the toItemId
	 */
	@Override
	public DtsConceptUniqueId getToItemId()
	{
		return toItemId;
	}

	/**
	 * Set a new value for the toItemId property.
	 * 
	 * @param toItemId
	 *            the toItemId to set
	 */
	@Override
	public void setToItemId(final DtsConceptUniqueId toItemId)
	{
		this.toItemId = toItemId;
	}

	/**
	 * TODO: NOT IMPLEMENTED
	 */
	@Override
	public boolean containsQualifier(DtsQualifier qualifier)
	{
		// TODO Auto-generated method stub
		return false;
	}

	// ========================= PRIVATE METHODS ===========================
}
