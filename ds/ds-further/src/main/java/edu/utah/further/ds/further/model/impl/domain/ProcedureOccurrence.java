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
@Table(name = "FPROCEDURE_OCCURRENCE")
@XmlRootElement(name = "ProcedureOccurrence")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcedureOccurrence implements PersistentEntity<Long>
{
	// ========================= CONSTANTS ===================================

	private static final long serialVersionUID = 7966310850424656019L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private ProcedureOccurrenceId id;

	@Column(name = "FPERSON_ID")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "FPROVIDER_ID")
	private Long providerId;

	@Column(name = "FENCOUNTER_ID")
	private Long encounterId;

	@Column(name = "PROCEDURE_TYPE_CID")
	private String procedureTypeConceptId;

	@Column(name = "PROCEDURE_NAMESPACE_ID")
	private Long procedureNamespaceId;

	@Column(name = "PROCEDURE_CID")
	private String procedureConceptId;

	@Column(name = "PROCEDURE_DTS")
	private Date procedureDate;

	@Column(name = "PROCEDURE_YR")
	private Long procedureYear;

	@Column(name = "PROCEDURE_MON")
	private Long procedureMonth;

	@Column(name = "PROCEDURE_DAY")
	private Long procedureDay;

	@Column(name = "RELEVANT_COND_NMSPC_ID")
	private Long relevantConditionNamespaceId;

	@Column(name = "RELEVANT_CONDITION_CID")
	private String relevantConditinConceptId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return personId;
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
	 * Return the providerId property.
	 * 
	 * @return the providerId
	 */
	public Long getProviderId()
	{
		return providerId;
	}

	/**
	 * Set a new value for the providerId property.
	 * 
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(Long providerId)
	{
		this.providerId = providerId;
	}

	/**
	 * Return the encounterId property.
	 * 
	 * @return the encounterId
	 */
	public Long getEncounterId()
	{
		return encounterId;
	}

	/**
	 * Set a new value for the encounterId property.
	 * 
	 * @param encounterId
	 *            the encounterId to set
	 */
	public void setEncounterId(Long encounterId)
	{
		this.encounterId = encounterId;
	}

	/**
	 * Return the procedureTypeConceptId property.
	 * 
	 * @return the procedureTypeConceptId
	 */
	public String getProcedureTypeConceptId()
	{
		return procedureTypeConceptId;
	}

	/**
	 * Set a new value for the procedureTypeConceptId property.
	 * 
	 * @param procedureTypeConceptId
	 *            the procedureTypeConceptId to set
	 */
	public void setProcedureTypeConceptId(String procedureTypeConceptId)
	{
		this.procedureTypeConceptId = procedureTypeConceptId;
	}

	/**
	 * Return the procedureNamespaceId property.
	 * 
	 * @return the procedureNamespaceId
	 */
	public Long getProcedureNamespaceId()
	{
		return procedureNamespaceId;
	}

	/**
	 * Set a new value for the procedureNamespaceId property.
	 * 
	 * @param procedureNamespaceId
	 *            the procedureNamespaceId to set
	 */
	public void setProcedureNamespaceId(Long procedureNamespaceId)
	{
		this.procedureNamespaceId = procedureNamespaceId;
	}

	/**
	 * Return the procedureConceptId property.
	 * 
	 * @return the procedureConceptId
	 */
	public String getProcedureConceptId()
	{
		return procedureConceptId;
	}

	/**
	 * Set a new value for the procedureConceptId property.
	 * 
	 * @param procedureConceptId
	 *            the procedureConceptId to set
	 */
	public void setProcedureConceptId(String procedureConceptId)
	{
		this.procedureConceptId = procedureConceptId;
	}

	/**
	 * Return the procedureDate property.
	 * 
	 * @return the procedureDate
	 */
	public Date getProcedureDate()
	{
		return procedureDate;
	}

	/**
	 * Set a new value for the procedureDate property.
	 * 
	 * @param procedureDate
	 *            the procedureDate to set
	 */
	public void setProcedureDate(Date procedureDate)
	{
		this.procedureDate = procedureDate;
	}

	/**
	 * Return the procedureYear property.
	 * 
	 * @return the procedureYear
	 */
	public Long getProcedureYear()
	{
		return procedureYear;
	}

	/**
	 * Set a new value for the procedureYear property.
	 * 
	 * @param procedureYear
	 *            the procedureYear to set
	 */
	public void setProcedureYear(Long procedureYear)
	{
		this.procedureYear = procedureYear;
	}

	/**
	 * Return the procedureMonth property.
	 * 
	 * @return the procedureMonth
	 */
	public Long getProcedureMonth()
	{
		return procedureMonth;
	}

	/**
	 * Set a new value for the procedureMonth property.
	 * 
	 * @param procedureMonth
	 *            the procedureMonth to set
	 */
	public void setProcedureMonth(Long procedureMonth)
	{
		this.procedureMonth = procedureMonth;
	}

	/**
	 * Return the procedureDay property.
	 * 
	 * @return the procedureDay
	 */
	public Long getProcedureDay()
	{
		return procedureDay;
	}

	/**
	 * Set a new value for the procedureDay property.
	 * 
	 * @param procedureDay
	 *            the procedureDay to set
	 */
	public void setProcedureDay(Long procedureDay)
	{
		this.procedureDay = procedureDay;
	}

	/**
	 * Return the relevantConditionNamespaceId property.
	 * 
	 * @return the relevantConditionNamespaceId
	 */
	public Long getRelevantConditionNamespaceId()
	{
		return relevantConditionNamespaceId;
	}

	/**
	 * Set a new value for the relevantConditionNamespaceId property.
	 * 
	 * @param relevantConditionNamespaceId
	 *            the relevantConditionNamespaceId to set
	 */
	public void setRelevantConditionNamespaceId(Long relevantConditionNamespaceId)
	{
		this.relevantConditionNamespaceId = relevantConditionNamespaceId;
	}

	/**
	 * Return the relevantConditinConceptId property.
	 * 
	 * @return the relevantConditinConceptId
	 */
	public String getRelevantConditinConceptId()
	{
		return relevantConditinConceptId;
	}

	/**
	 * Set a new value for the relevantConditinConceptId property.
	 * 
	 * @param relevantConditinConceptId
	 *            the relevantConditinConceptId to set
	 */
	public void setRelevantConditinConceptId(String relevantConditinConceptId)
	{
		this.relevantConditinConceptId = relevantConditinConceptId;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(ProcedureOccurrenceId id)
	{
		this.id = id;
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final ProcedureOccurrence that = (ProcedureOccurrence) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

}
