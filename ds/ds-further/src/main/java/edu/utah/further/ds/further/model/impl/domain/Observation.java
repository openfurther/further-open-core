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
import org.hibernate.annotations.Formula;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Persistent entity implementation of {@link Observation}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 2, 2009
 */
@Entity
@Implementation
@Table(name = "FOBSERVATION_FACT")
@XmlRootElement(name = "Observation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation implements PersistentEntity<ObservationId>
{
	// ========================= CONSTANTS ===================================

	@Transient
	private static final long serialVersionUID = -5026449094636014186L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private ObservationId id;

	@Column(name = "forder_id")
	private Long orderId;

	@Column(name = "fperson_id")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "person_age_at_obs")
	private Long personAgeAtObservation;

	@Column(name = "fencounter_id")
	private Long encounterId;

	@Column(name = "fobservation_period_id")
	private Long observationPeriod;

	@Column(name = "observation_type_cid")
	private String observationType;

	@Column(name = "observation_nmspc_id")
	private Long observationNamespaceId;

	@Column(name = "observation_cid")
	private String observation;

	@Column(name = "observation_mod_nmspc_id")
	private Long observationModNamespaceId;

	@Column(name = "observation_mod_cid")
	private String observationMod;

	@Column(name = "observation_flag")
	private String observationFlag;

	@Column(name = "observation_flag_nmspc_id")
	private Long observationFlagNamespaceId;

	@Column(name = "method_nmspc_id")
	private Long methodNamespaceId;

	@Column(name = "method_cid")
	private String method;

	@Column(name = "value_nmpsc_id")
	private Long valueNamespaceId;

	@Column(name = "value_cid")
	private String value;

	@Column(name = "value_type_cd")
	private String valueType;

	@Column(name = "value_string")
	private String valueString;

	@Column(name = "value_number")
	private Long valueNumber;

	@Column(name = "value_units_nmspc_id")
	private Long unitOfMeasureNamespaceId;

	@Column(name = "value_units_cid")
	private String unitOfMeasure;

	@Column(name = "observation_seq")
	private Long observationSeq;

	/* This is Oracle specific */
	@Formula("to_char(start_dts, 'YYYY')")
	private Integer startDateYear;

	@Column(name = "start_dts")
	private Date startDateTime;

	@Column(name = "end_dts")
	private Date endDateTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public ObservationId getId()
	{
		return id;
	}

	/**
	 * Return the orderId property.
	 * 
	 * @return the orderId
	 */
	public Long getOrderId()
	{
		return orderId;
	}

	/**
	 * Set a new value for the orderId property.
	 * 
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
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
	 * Return the personAgeAtObservation property.
	 * 
	 * @return the personAgeAtObservation
	 */
	public Long getPersonAgeAtObservation()
	{
		return personAgeAtObservation;
	}

	/**
	 * Set a new value for the personAgeAtObservation property.
	 * 
	 * @param personAgeAtObservation
	 *            the personAgeAtObservation to set
	 */
	public void setPersonAgeAtObservation(Long personAgeAtObservation)
	{
		this.personAgeAtObservation = personAgeAtObservation;
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
	 * Return the observationPeriod property.
	 * 
	 * @return the observationPeriod
	 */
	public Long getObservationPeriod()
	{
		return observationPeriod;
	}

	/**
	 * Set a new value for the observationPeriod property.
	 * 
	 * @param observationPeriod
	 *            the observationPeriod to set
	 */
	public void setObservationPeriod(Long observationPeriod)
	{
		this.observationPeriod = observationPeriod;
	}

	/**
	 * Return the observationType property.
	 * 
	 * @return the observationType
	 */
	public String getObservationType()
	{
		return observationType;
	}

	/**
	 * Set a new value for the observationType property.
	 * 
	 * @param observationType
	 *            the observationType to set
	 */
	public void setObservationType(String observationType)
	{
		this.observationType = observationType;
	}

	/**
	 * Return the observationNamespaceId property.
	 * 
	 * @return the observationNamespaceId
	 */
	public Long getObservationNamespaceId()
	{
		return observationNamespaceId;
	}

	/**
	 * Set a new value for the observationNamespaceId property.
	 * 
	 * @param observationNamespaceId
	 *            the observationNamespaceId to set
	 */
	public void setObservationNamespaceId(Long observationNamespaceId)
	{
		this.observationNamespaceId = observationNamespaceId;
	}

	/**
	 * Return the observation property.
	 * 
	 * @return the observation
	 */
	public String getObservation()
	{
		return observation;
	}

	/**
	 * Set a new value for the observation property.
	 * 
	 * @param observation
	 *            the observation to set
	 */
	public void setObservation(String observation)
	{
		this.observation = observation;
	}

	/**
	 * Return the observationModNamespaceId property.
	 * 
	 * @return the observationModNamespaceId
	 */
	public Long getObservationModNamespaceId()
	{
		return observationModNamespaceId;
	}

	/**
	 * Set a new value for the observationModNamespaceId property.
	 * 
	 * @param observationModNamespaceId
	 *            the observationModNamespaceId to set
	 */
	public void setObservationModNamespaceId(Long observationModNamespaceId)
	{
		this.observationModNamespaceId = observationModNamespaceId;
	}

	/**
	 * Return the observationMod property.
	 * 
	 * @return the observationMod
	 */
	public String getObservationMod()
	{
		return observationMod;
	}

	/**
	 * Set a new value for the observationMod property.
	 * 
	 * @param observationMod
	 *            the observationMod to set
	 */
	public void setObservationMod(String observationMod)
	{
		this.observationMod = observationMod;
	}

	/**
	 * Return the observationFlag property.
	 * 
	 * @return the observationFlag
	 */
	public String getObservationFlag()
	{
		return observationFlag;
	}

	/**
	 * Set a new value for the observationFlag property.
	 * 
	 * @param observationFlag
	 *            the observationFlag to set
	 */
	public void setObservationFlag(String observationFlag)
	{
		this.observationFlag = observationFlag;
	}

	/**
	 * Return the observationFlagNamespaceId property.
	 * 
	 * @return the observationFlagNamespaceId
	 */
	public Long getObservationFlagNamespaceId()
	{
		return observationFlagNamespaceId;
	}

	/**
	 * Set a new value for the observationFlagNamespaceId property.
	 * 
	 * @param observationFlagNamespaceId
	 *            the observationFlagNamespaceId to set
	 */
	public void setObservationFlagNamespaceId(Long observationFlagNamespaceId)
	{
		this.observationFlagNamespaceId = observationFlagNamespaceId;
	}

	/**
	 * Return the methodNamespaceId property.
	 * 
	 * @return the methodNamespaceId
	 */
	public Long getMethodNamespaceId()
	{
		return methodNamespaceId;
	}

	/**
	 * Set a new value for the methodNamespaceId property.
	 * 
	 * @param methodNamespaceId
	 *            the methodNamespaceId to set
	 */
	public void setMethodNamespaceId(Long methodNamespaceId)
	{
		this.methodNamespaceId = methodNamespaceId;
	}

	/**
	 * Return the method property.
	 * 
	 * @return the method
	 */
	public String getMethod()
	{
		return method;
	}

	/**
	 * Set a new value for the method property.
	 * 
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method)
	{
		this.method = method;
	}

	/**
	 * Return the valueNamespaceId property.
	 * 
	 * @return the valueNamespaceId
	 */
	public Long getValueNamespaceId()
	{
		return valueNamespaceId;
	}

	/**
	 * Set a new value for the valueNamespaceId property.
	 * 
	 * @param valueNamespaceId
	 *            the valueNamespaceId to set
	 */
	public void setValueNamespaceId(Long valueNamespaceId)
	{
		this.valueNamespaceId = valueNamespaceId;
	}

	/**
	 * Return the value property.
	 * 
	 * @return the value
	 */
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
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Return the valueType property.
	 * 
	 * @return the valueType
	 */
	public String getValueType()
	{
		return valueType;
	}

	/**
	 * Set a new value for the valueType property.
	 * 
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(String valueType)
	{
		this.valueType = valueType;
	}

	/**
	 * Return the valueString property.
	 * 
	 * @return the valueString
	 */
	public String getValueString()
	{
		return valueString;
	}

	/**
	 * Set a new value for the valueString property.
	 * 
	 * @param valueString
	 *            the valueString to set
	 */
	public void setValueString(String valueString)
	{
		this.valueString = valueString;
	}

	/**
	 * Return the valueNumber property.
	 * 
	 * @return the valueNumber
	 */
	public Long getValueNumber()
	{
		return valueNumber;
	}

	/**
	 * Set a new value for the valueNumber property.
	 * 
	 * @param valueNumber
	 *            the valueNumber to set
	 */
	public void setValueNumber(Long valueNumber)
	{
		this.valueNumber = valueNumber;
	}

	/**
	 * Return the unitOfMeasureNamespaceId property.
	 * 
	 * @return the unitOfMeasureNamespaceId
	 */
	public Long getUnitOfMeasureNamespaceId()
	{
		return unitOfMeasureNamespaceId;
	}

	/**
	 * Set a new value for the unitOfMeasureNamespaceId property.
	 * 
	 * @param unitOfMeasureNamespaceId
	 *            the unitOfMeasureNamespaceId to set
	 */
	public void setUnitOfMeasureNamespaceId(Long unitOfMeasureNamespaceId)
	{
		this.unitOfMeasureNamespaceId = unitOfMeasureNamespaceId;
	}

	/**
	 * Return the unitOfMeasure property.
	 * 
	 * @return the unitOfMeasure
	 */
	public String getUnitOfMeasure()
	{
		return unitOfMeasure;
	}

	/**
	 * Set a new value for the unitOfMeasure property.
	 * 
	 * @param unitOfMeasure
	 *            the unitOfMeasure to set
	 */
	public void setUnitOfMeasure(String unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
	}

	/**
	 * Return the observationSeq property.
	 * 
	 * @return the observationSeq
	 */
	public Long getObservationSeq()
	{
		return observationSeq;
	}

	/**
	 * Set a new value for the observationSeq property.
	 * 
	 * @param observationSeq
	 *            the observationSeq to set
	 */
	public void setObservationSeq(Long observationSeq)
	{
		this.observationSeq = observationSeq;
	}

	/**
	 * Return the startDateYear property.
	 * 
	 * @return the startDateYear
	 */
	public Integer getStartDateYear()
	{
		return startDateYear;
	}

	/**
	 * Set a new value for the startDateYear property.
	 * 
	 * @param startDateYear
	 *            the startDateYear to set
	 */
	public void setStartDateYear(Integer startDateYear)
	{
		this.startDateYear = startDateYear;
	}

	/**
	 * Return the startDateTime property.
	 * 
	 * @return the startDateTime
	 */
	public Date getStartDateTime()
	{
		return startDateTime;
	}

	/**
	 * Set a new value for the startDateTime property.
	 * 
	 * @param startDateTime
	 *            the startDateTime to set
	 */
	public void setStartDateTime(Date startDateTime)
	{
		this.startDateTime = startDateTime;
	}

	/**
	 * Return the endDateTime property.
	 * 
	 * @return the endDateTime
	 */
	public Date getEndDateTime()
	{
		return endDateTime;
	}

	/**
	 * Set a new value for the endDateTime property.
	 * 
	 * @param endDateTime
	 *            the endDateTime to set
	 */
	public void setEndDateTime(Date endDateTime)
	{
		this.endDateTime = endDateTime;
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

		final Observation that = (Observation) obj;
		return new EqualsBuilder()
				.append(getId(), that.getId())
				.append(getId(), that.getId())
				.isEquals();
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
		return ReflectionToStringBuilder.toStringExclude(this, new String[]
		{ "person", "order", "encounter" });
	}

}
