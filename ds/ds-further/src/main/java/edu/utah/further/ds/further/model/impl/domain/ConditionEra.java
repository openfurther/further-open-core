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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * /** ...
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
@Table(name = "FCONDITION_ERA")
@XmlRootElement(name = "ConditionEra")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionEra implements PersistentEntity<ConditionEraId>
{

	// ========================= CONSTANTS ===================================

	@Transient
	private static final long serialVersionUID = -8269250744282142380L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private ConditionEraId id;

	@Column(name = "FPERSON_ID")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "CONDITION_TYPE_CID")
	private String conditionTypeConceptId;

	@Column(name = "CONDITION_NMSPC_ID")
	private Long conditiionNamespaceId;

	@Column(name = "CONDITION_CID")
	private String conditionConceptId;

	@Column(name = "CONDITION_ERA_START_DT")
	private Date conditionEraStartDate;

	@Column(name = "CONDITION_ERA_END_DT")
	private Date conditionEraEndDate;

	@Column(name = "CONDITION_OCCURRENCE_CNT")
	private Long conditionOccurrenceCount;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public ConditionEraId getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(ConditionEraId id)
	{
		this.id = id;
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
	 * Return the conditionTypeConceptId property.
	 * 
	 * @return the conditionTypeConceptId
	 */
	public String getConditionTypeConceptId()
	{
		return conditionTypeConceptId;
	}

	/**
	 * Set a new value for the conditionTypeConceptId property.
	 * 
	 * @param conditionTypeConceptId
	 *            the conditionTypeConceptId to set
	 */
	public void setConditionTypeConceptId(String conditionTypeConceptId)
	{
		this.conditionTypeConceptId = conditionTypeConceptId;
	}

	/**
	 * Return the conditiionNamespaceId property.
	 * 
	 * @return the conditiionNamespaceId
	 */
	public Long getConditiionNamespaceId()
	{
		return conditiionNamespaceId;
	}

	/**
	 * Set a new value for the conditiionNamespaceId property.
	 * 
	 * @param conditiionNamespaceId
	 *            the conditiionNamespaceId to set
	 */
	public void setConditiionNamespaceId(Long conditiionNamespaceId)
	{
		this.conditiionNamespaceId = conditiionNamespaceId;
	}

	/**
	 * Return the conditionConceptId property.
	 * 
	 * @return the conditionConceptId
	 */
	public String getConditionConceptId()
	{
		return conditionConceptId;
	}

	/**
	 * Set a new value for the conditionConceptId property.
	 * 
	 * @param conditionConceptId
	 *            the conditionConceptId to set
	 */
	public void setConditionConceptId(String conditionConceptId)
	{
		this.conditionConceptId = conditionConceptId;
	}

	/**
	 * Return the conditionEraStartDate property.
	 * 
	 * @return the conditionEraStartDate
	 */
	public Date getConditionEraStartDate()
	{
		return conditionEraStartDate;
	}

	/**
	 * Set a new value for the conditionEraStartDate property.
	 * 
	 * @param conditionEraStartDate
	 *            the conditionEraStartDate to set
	 */
	public void setConditionEraStartDate(Date conditionEraStartDate)
	{
		this.conditionEraStartDate = conditionEraStartDate;
	}

	/**
	 * Return the conditionEraEndDate property.
	 * 
	 * @return the conditionEraEndDate
	 */
	public Date getConditionEraEndDate()
	{
		return conditionEraEndDate;
	}

	/**
	 * Set a new value for the conditionEraEndDate property.
	 * 
	 * @param conditionEraEndDate
	 *            the conditionEraEndDate to set
	 */
	public void setConditionEraEndDate(Date conditionEraEndDate)
	{
		this.conditionEraEndDate = conditionEraEndDate;
	}

	/**
	 * Return the conditionOccurrenceCount property.
	 * 
	 * @return the conditionOccurrenceCount
	 */
	public Long getConditionOccurrenceCount()
	{
		return conditionOccurrenceCount;
	}

	/**
	 * Set a new value for the conditionOccurrenceCount property.
	 * 
	 * @param conditionOccurrenceCount
	 *            the conditionOccurrenceCount to set
	 */
	public void setConditionOccurrenceCount(Long conditionOccurrenceCount)
	{
		this.conditionOccurrenceCount = conditionOccurrenceCount;
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

		final ConditionEra that = (ConditionEra) obj;
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
