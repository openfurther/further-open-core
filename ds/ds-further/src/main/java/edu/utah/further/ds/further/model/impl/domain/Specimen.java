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
package edu.utah.further.ds.further.model.impl.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version May 9, 2012
 */
@Entity
@Implementation
@Table(name = "FSPECIMEN")
@XmlRootElement(name = "Specimen")
@XmlAccessorType(XmlAccessType.FIELD)
public class Specimen implements PersistentEntity<SpecimenId>
{
	// ========================= CONSTANTS ===================================

	private static final long serialVersionUID = 372399211253271355L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private SpecimenId id;

	@Column(name = "PARENT_FSPECIMEN_ID")
	private Long parentSpecimenId;

	@Column(name = "SPECIMEN_NAME")
	private String specimenName;

	@Column(name = "FPERSON_ID")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "CATEGORY_NMSPC_ID")
	private Long categoryNamespaceId;

	@Column(name = "CATEGORY_CID")
	private String categoryConceptId;

	@Column(name = "TYPE_NMSPC_ID")
	private Long typeNamespaceId;

	@Column(name = "TYPE_CID")
	private String typeConceptId;

	@Column(name = "STATUS_NMSPC_ID")
	private Long statusNamespaceId;

	@Column(name = "STATUS_CID")
	private String statusConceptId;

	@Column(name = "AMOUNT_UOM_NMSPC_ID")
	private Long amountNamespaceId;

	@Column(name = "AMOUNT_UOM_CID")
	private String amountConceptId;

	@Column(name = "AMOUNT")
	private Long amount;

	@Column(name = "SPECIMEN_ALIAS")
	private String specimenAlias;

	@Column(name = "SPECIMEN_ALIAS_TYPE")
	private String specimenAliasType;

	@Column(name = "FSTORAGE_ID")
	private Long storageId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public SpecimenId getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(SpecimenId id)
	{
		this.id = id;
	}

	/**
	 * Return the parentSpecimenId property.
	 * 
	 * @return the parentSpecimenId
	 */
	public Long getParentSpecimenId()
	{
		return parentSpecimenId;
	}

	/**
	 * Set a new value for the parentSpecimenId property.
	 * 
	 * @param parentSpecimenId
	 *            the parentSpecimenId to set
	 */
	public void setParentSpecimenId(Long parentSpecimenId)
	{
		this.parentSpecimenId = parentSpecimenId;
	}

	/**
	 * Return the specimenName property.
	 * 
	 * @return the specimenName
	 */
	public String getSpecimenName()
	{
		return specimenName;
	}

	/**
	 * Set a new value for the specimenName property.
	 * 
	 * @param specimenName
	 *            the specimenName to set
	 */
	public void setSpecimenName(String specimenName)
	{
		this.specimenName = specimenName;
	}

	/**
	 * Return the personId property.
	 * 
	 * @return the personId
	 */
	public Long getPersonId()
	{
		return personId;
	}

	/**
	 * Set a new value for the personId property.
	 * 
	 * @param personId
	 *            the personId to set
	 */
	public void setPersonId(Long personId)
	{
		this.personId = personId;
	}

	/**
	 * Return the personCompositeId property.
	 * 
	 * @return the personCompositeId
	 */
	public String getPersonCompositeId()
	{
		return personCompositeId;
	}

	/**
	 * Set a new value for the personCompositeId property.
	 * 
	 * @param personCompositeId
	 *            the personCompositeId to set
	 */
	public void setPersonCompositeId(String personCompositeId)
	{
		this.personCompositeId = personCompositeId;
	}

	/**
	 * Return the categoryNamespaceId property.
	 * 
	 * @return the categoryNamespaceId
	 */
	public Long getCategoryNamespaceId()
	{
		return categoryNamespaceId;
	}

	/**
	 * Set a new value for the categoryNamespaceId property.
	 * 
	 * @param categoryNamespaceId
	 *            the categoryNamespaceId to set
	 */
	public void setCategoryNamespaceId(Long categoryNamespaceId)
	{
		this.categoryNamespaceId = categoryNamespaceId;
	}

	/**
	 * Return the categoryConceptId property.
	 * 
	 * @return the categoryConceptId
	 */
	public String getCategoryConceptId()
	{
		return categoryConceptId;
	}

	/**
	 * Set a new value for the categoryConceptId property.
	 * 
	 * @param categoryConceptId
	 *            the categoryConceptId to set
	 */
	public void setCategoryConceptId(String categoryConceptId)
	{
		this.categoryConceptId = categoryConceptId;
	}

	/**
	 * Return the typeNamespaceId property.
	 * 
	 * @return the typeNamespaceId
	 */
	public Long getTypeNamespaceId()
	{
		return typeNamespaceId;
	}

	/**
	 * Set a new value for the typeNamespaceId property.
	 * 
	 * @param typeNamespaceId
	 *            the typeNamespaceId to set
	 */
	public void setTypeNamespaceId(Long typeNamespaceId)
	{
		this.typeNamespaceId = typeNamespaceId;
	}

	/**
	 * Return the typeConceptId property.
	 * 
	 * @return the typeConceptId
	 */
	public String getTypeConceptId()
	{
		return typeConceptId;
	}

	/**
	 * Set a new value for the typeConceptId property.
	 * 
	 * @param typeConceptId
	 *            the typeConceptId to set
	 */
	public void setTypeConceptId(String typeConceptId)
	{
		this.typeConceptId = typeConceptId;
	}

	/**
	 * Return the statusNamespaceId property.
	 * 
	 * @return the statusNamespaceId
	 */
	public Long getStatusNamespaceId()
	{
		return statusNamespaceId;
	}

	/**
	 * Set a new value for the statusNamespaceId property.
	 * 
	 * @param statusNamespaceId
	 *            the statusNamespaceId to set
	 */
	public void setStatusNamespaceId(Long statusNamespaceId)
	{
		this.statusNamespaceId = statusNamespaceId;
	}

	/**
	 * Return the statusConceptId property.
	 * 
	 * @return the statusConceptId
	 */
	public String getStatusConceptId()
	{
		return statusConceptId;
	}

	/**
	 * Set a new value for the statusConceptId property.
	 * 
	 * @param statusConceptId
	 *            the statusConceptId to set
	 */
	public void setStatusConceptId(String statusConceptId)
	{
		this.statusConceptId = statusConceptId;
	}

	/**
	 * Return the amountNamespaceId property.
	 * 
	 * @return the amountNamespaceId
	 */
	public Long getAmountNamespaceId()
	{
		return amountNamespaceId;
	}

	/**
	 * Set a new value for the amountNamespaceId property.
	 * 
	 * @param amountNamespaceId
	 *            the amountNamespaceId to set
	 */
	public void setAmountNamespaceId(Long amountNamespaceId)
	{
		this.amountNamespaceId = amountNamespaceId;
	}

	/**
	 * Return the amountConceptId property.
	 * 
	 * @return the amountConceptId
	 */
	public String getAmountConceptId()
	{
		return amountConceptId;
	}

	/**
	 * Set a new value for the amountConceptId property.
	 * 
	 * @param amountConceptId
	 *            the amountConceptId to set
	 */
	public void setAmountConceptId(String amountConceptId)
	{
		this.amountConceptId = amountConceptId;
	}

	/**
	 * Return the amount property.
	 * 
	 * @return the amount
	 */
	public Long getAmount()
	{
		return amount;
	}

	/**
	 * Set a new value for the amount property.
	 * 
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Long amount)
	{
		this.amount = amount;
	}

	/**
	 * Return the specimenAlias property.
	 * 
	 * @return the specimenAlias
	 */
	public String getSpecimenAlias()
	{
		return specimenAlias;
	}

	/**
	 * Set a new value for the specimenAlias property.
	 * 
	 * @param specimenAlias
	 *            the specimenAlias to set
	 */
	public void setSpecimenAlias(String specimenAlias)
	{
		this.specimenAlias = specimenAlias;
	}

	/**
	 * Return the specimenAliasType property.
	 * 
	 * @return the specimenAliasType
	 */
	public String getSpecimenAliasType()
	{
		return specimenAliasType;
	}

	/**
	 * Set a new value for the specimenAliasType property.
	 * 
	 * @param specimenAliasType
	 *            the specimenAliasType to set
	 */
	public void setSpecimenAliasType(String specimenAliasType)
	{
		this.specimenAliasType = specimenAliasType;
	}

	/**
	 * Return the storageId property.
	 * 
	 * @return the storageId
	 */
	public Long getStorageId()
	{
		return storageId;
	}

	/**
	 * Set a new value for the storageId property.
	 * 
	 * @param storageId
	 *            the storageId to set
	 */
	public void setStorageId(Long storageId)
	{
		this.storageId = storageId;
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final Specimen that = (Specimen) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

}
