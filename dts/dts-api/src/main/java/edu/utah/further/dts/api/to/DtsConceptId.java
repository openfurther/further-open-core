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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A JavaBean that holds a representation (identifier) of a concept. This is useful when
 * searching for a concept by property or translating its property.
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
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptId.ENTITY_NAME, propOrder =
{ "namespace", "propertyName", "propertyValue", "qualifierName", "qualifierValue",
		"associationType", "superConceptName", "superConceptAssociationTypeName" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptId.ENTITY_NAME)
public class DtsConceptId implements Serializable
{
	// ========================= CONSTANTS =================================

	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "conceptId";

	// ========================= FIELDS ====================================

	/**
	 * DTS namespace.
	 */
	@XmlElement(name = "namespace", required = false)
	private String namespace;

	/**
	 * DTS concept property name.
	 */
	@XmlElement(name = "propertyName", required = false)
	private String propertyName;

	/**
	 * DTS concept property value.
	 */
	@XmlElement(name = "propertyValue", required = false)
	private String propertyValue;

	/**
	 * DTS qualifier property name
	 */
	@XmlElement(name = "qualifierName", required = false)
	private String qualifierName;

	/**
	 * DTS qualifier property value
	 */
	@XmlElement(name = "qualifierValue", required = false)
	private String qualifierValue;

	/**
	 * DTS association type
	 */
	@XmlElement(name = "associationType", required = false)
	private String associationType;

	/**
	 * SuperConcept name used to narrow search results
	 */
	@XmlElement(name = "superConceptName", required = false)
	private String superConceptName;

	/**
	 * SuperConcept association type name
	 */
	@XmlElement(name = "superConceptAssociationTypeName", required = false)
	private String superConceptAssociationTypeName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Set no fields. Required for JAXB.
	 */
	public DtsConceptId()
	{
		super();
	}

	/**
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 */
	public DtsConceptId(final String namespace, final String propertyName,
			final String propertyValue)
	{
		this(namespace, propertyName, propertyValue, null, null);
	}

	/**
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 * @param qualifierName
	 * @param qualifierValue
	 */
	public DtsConceptId(final String namespace, final String propertyName,
			final String propertyValue, final String qualifierName,
			final String qualifierValue)
	{
		super();
		this.namespace = namespace;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.qualifierName = qualifierName;
		this.qualifierValue = qualifierValue;
	}

	/**
	 * @param namespace
	 * @param propertyName
	 * @param propertyValue
	 * @param qualifierName
	 * @param qualifierValue
	 * @param superConceptName
	 */
	public DtsConceptId(final String namespace, final String propertyName,
			final String propertyValue, final String qualifierName,
			final String qualifierValue, final String superConceptName,
			final String superConceptAssociationTypeName)
	{
		super();
		this.namespace = namespace;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.qualifierName = qualifierName;
		this.qualifierValue = qualifierValue;
		this.superConceptName = superConceptName;
		this.superConceptAssociationTypeName = superConceptAssociationTypeName;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("namespace", namespace)
				.append("propertyName", propertyName)
				.append("propertyValue", propertyValue)
				.append("qualifierName", qualifierName)
				.append("qualifierValue", qualifierValue)
				.append("associationType", associationType)
				.append("superConceptName", superConceptName)
				.append("superConceptAssociationTypeName",
						superConceptAssociationTypeName)
				.toString();
	}

	/**
	 * Return the hash code of this object. includes all fields.
	 * 
	 * @return hash code of this object
	 * @see edu.utah.further.audit.api.domain.AbstractMessage#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(namespace)
				.append(propertyName)
				.append(propertyValue)
				.append(qualifierName)
				.append(qualifierValue)
				.append(associationType)
				.append(superConceptName)
				.append(superConceptAssociationTypeName)
				.toHashCode();
	}

	/**
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
		if (getClass() != o.getClass())
			return false;

		final DtsConceptId that = (DtsConceptId) o;
		return new EqualsBuilder()
				.append(namespace, that.namespace)
				.append(propertyName, that.propertyName)
				.append(propertyValue, that.propertyValue)
				.append(qualifierName, that.qualifierName)
				.append(qualifierValue, that.qualifierValue)
				.append(associationType, that.associationType)
				.append(superConceptName, that.superConceptName)
				.append(superConceptAssociationTypeName,
						that.superConceptAssociationTypeName)
				.isEquals();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getNamespace()
	 */
	public String getNamespace()
	{
		return namespace;
	}

	/**
	 * @param namespace
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setNamespace(java.lang.String)
	 */
	public void setNamespace(final String namespace)
	{
		this.namespace = namespace;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getPropertyName()
	 */
	public String getPropertyName()
	{
		return propertyName;
	}

	/**
	 * @param propertyName
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setPropertyName(java.lang.String)
	 */
	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getPropertyValue()
	 */
	public String getPropertyValue()
	{
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(final String propertyValue)
	{
		this.propertyValue = propertyValue;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getQualifierName()
	 */
	public String getQualifierName()
	{
		return qualifierName;
	}

	/**
	 * @param qualifierName
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setQualifierName(java.lang.String)
	 */
	public void setQualifierName(final String qualifierName)
	{
		this.qualifierName = qualifierName;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getQualifierValue()
	 */
	public String getQualifierValue()
	{
		return qualifierValue;
	}

	/**
	 * @param qualifierValue
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setQualifierValue(java.lang.String)
	 */
	public void setQualifierValue(final String qualifierValue)
	{
		this.qualifierValue = qualifierValue;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsConceptId#getAssociationType()
	 */
	public String getAssociationType()
	{
		return associationType;
	}

	/**
	 * @param associationType
	 * @see edu.utah.further.dts.api.to.DtsConceptId#setAssociationType(java.lang.String)
	 */
	public void setAssociationType(final String associationType)
	{
		this.associationType = associationType;
	}

	/**
	 * @return the superConceptName
	 */
	public String getSuperConceptName()
	{
		return superConceptName;
	}

	/**
	 * @param superConceptName
	 *            the superConceptName to set
	 */
	public void setSuperConceptName(final String parentConceptName)
	{
		this.superConceptName = parentConceptName;
	}

	/**
	 * @return the superConceptAssociationTypeName
	 */
	public String getSuperConceptAssociationTypeName()
	{
		return superConceptAssociationTypeName;
	}

	/**
	 * @param superConceptAssociationTypeName
	 *            the superConceptAssociationTypeName to set
	 */
	public void setSuperConceptAssociationTypeName(
			final String superConceptAssociationTypeName)
	{
		this.superConceptAssociationTypeName = superConceptAssociationTypeName;
	}

}
