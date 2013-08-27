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
package edu.utah.further.ds.omop.model.v4.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the OBSERVATION database table.
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
 * @version Apr 17, 2013
 */
@Entity
@Table(name = "OBSERVATION")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBSERVATION_ID", unique = true, nullable = false)
	private Long observationId;

	@Column(name = "ASSOCIATED_PROVIDER_ID")
	private BigDecimal associatedProviderId;

	@Column(name = "OBSERVATION_CONCEPT_ID", nullable = false)
	private BigDecimal observationConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_DATE", nullable = false)
	private Date observationDate;

	@Column(name = "OBSERVATION_SOURCE_VALUE", length = 50)
	private String observationSourceValue;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_TIME")
	private Date observationTime;

	@Column(name = "OBSERVATION_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal observationTypeConceptId;

	@Column(name = "RANGE_HIGH", precision = 14, scale = 3)
	private BigDecimal rangeHigh;

	@Column(name = "RANGE_LOW", precision = 14, scale = 3)
	private BigDecimal rangeLow;

	@Column(name = "RELEVANT_CONDITION_CONCEPT_ID")
	private BigDecimal relevantConditionConceptId;

	@Column(name = "UNIT_CONCEPT_ID")
	private BigDecimal unitConceptId;

	@Column(name = "UNITS_SOURCE_VALUE", length = 50)
	private String unitsSourceValue;

	@Column(name = "VALUE_AS_CONCEPT_ID")
	private BigDecimal valueAsConceptId;

	@Column(name = "VALUE_AS_NUMBER", precision = 14, scale = 3)
	private BigDecimal valueAsNumber;

	@Column(name = "VALUE_AS_STRING", length = 60)
	private String valueAsString;

	@Column(name = "VISIT_OCCURRENCE_ID")
	private BigDecimal visitOccurrenceId;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public Observation()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.observationId;
	}

	public BigDecimal getAssociatedProviderId()
	{
		return this.associatedProviderId;
	}

	public void setAssociatedProviderId(final BigDecimal associatedProviderId)
	{
		this.associatedProviderId = associatedProviderId;
	}

	public BigDecimal getObservationConceptId()
	{
		return this.observationConceptId;
	}

	public void setObservationConceptId(final BigDecimal observationConceptId)
	{
		this.observationConceptId = observationConceptId;
	}

	public Date getObservationDate()
	{
		return this.observationDate;
	}

	public void setObservationDate(final Date observationDate)
	{
		this.observationDate = observationDate;
	}

	public String getObservationSourceValue()
	{
		return this.observationSourceValue;
	}

	public void setObservationSourceValue(final String observationSourceValue)
	{
		this.observationSourceValue = observationSourceValue;
	}

	public Date getObservationTime()
	{
		return this.observationTime;
	}

	public void setObservationTime(final Date observationTime)
	{
		this.observationTime = observationTime;
	}

	public BigDecimal getObservationTypeConceptId()
	{
		return this.observationTypeConceptId;
	}

	public void setObservationTypeConceptId(final BigDecimal observationTypeConceptId)
	{
		this.observationTypeConceptId = observationTypeConceptId;
	}

	public BigDecimal getRangeHigh()
	{
		return this.rangeHigh;
	}

	public void setRangeHigh(final BigDecimal rangeHigh)
	{
		this.rangeHigh = rangeHigh;
	}

	public BigDecimal getRangeLow()
	{
		return this.rangeLow;
	}

	public void setRangeLow(final BigDecimal rangeLow)
	{
		this.rangeLow = rangeLow;
	}

	public BigDecimal getRelevantConditionConceptId()
	{
		return this.relevantConditionConceptId;
	}

	public void setRelevantConditionConceptId(final BigDecimal relevantConditionConceptId)
	{
		this.relevantConditionConceptId = relevantConditionConceptId;
	}

	public BigDecimal getUnitConceptId()
	{
		return this.unitConceptId;
	}

	public void setUnitConceptId(final BigDecimal unitConceptId)
	{
		this.unitConceptId = unitConceptId;
	}

	public String getUnitsSourceValue()
	{
		return this.unitsSourceValue;
	}

	public void setUnitsSourceValue(final String unitsSourceValue)
	{
		this.unitsSourceValue = unitsSourceValue;
	}

	public BigDecimal getValueAsConceptId()
	{
		return this.valueAsConceptId;
	}

	public void setValueAsConceptId(final BigDecimal valueAsConceptId)
	{
		this.valueAsConceptId = valueAsConceptId;
	}

	public BigDecimal getValueAsNumber()
	{
		return this.valueAsNumber;
	}

	public void setValueAsNumber(final BigDecimal valueAsNumber)
	{
		this.valueAsNumber = valueAsNumber;
	}

	public String getValueAsString()
	{
		return this.valueAsString;
	}

	public void setValueAsString(final String valueAsString)
	{
		this.valueAsString = valueAsString;
	}

	public BigDecimal getVisitOccurrenceId()
	{
		return this.visitOccurrenceId;
	}

	public void setVisitOccurrenceId(final BigDecimal visitOccurrenceId)
	{
		this.visitOccurrenceId = visitOccurrenceId;
	}

	public Person getPerson()
	{
		return this.person;
	}

	public void setPerson(final Person person)
	{
		this.person = person;
	}

}