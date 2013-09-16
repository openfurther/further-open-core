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
package edu.utah.further.ds.omop.model.v2.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @version Apr 24, 2013
 */
@Entity
@XmlRootElement(name = "Observation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBS_OCCURRENCE_ID")
	private Long obsOccurrenceId;

	@Column(name = "OBS_CONCEPT_ID")
	private BigDecimal obsConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBS_DATE")
	private Date obsDate;

	@Column(name = "OBS_RANGE_HIGH")
	private BigDecimal obsRangeHigh;

	@Column(name = "OBS_RANGE_LOW")
	private BigDecimal obsRangeLow;

	@Column(name = "OBS_UNITS_CONCEPT_ID")
	private BigDecimal obsUnitsConceptId;

	@Column(name = "OBS_VALUE_AS_CONCEPT_ID")
	private BigDecimal obsValueAsConceptId;

	@Column(name = "OBS_VALUE_AS_NUMBER")
	private BigDecimal obsValueAsNumber;

	@Column(name = "OBS_VALUE_AS_STRING")
	private String obsValueAsString;

	@Column(name = "SOURCE_OBS_CODE")
	private String sourceObsCode;

	// bi-directional many-to-one association to ObservationTypeRef
	@ManyToOne
	@JoinColumn(name = "OBS_TYPE")
	private ObservationTypeRef observationTypeRef;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "PERSON_ID")
	private Person person;

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
		return this.obsOccurrenceId;
	}

	protected void setId(final Long obsOccurrenceId)
	{
		this.obsOccurrenceId = obsOccurrenceId;
	}

	public BigDecimal getObsConceptId()
	{
		return this.obsConceptId;
	}

	public void setObsConceptId(final BigDecimal obsConceptId)
	{
		this.obsConceptId = obsConceptId;
	}

	public Date getObsDate()
	{
		return this.obsDate;
	}

	public void setObsDate(final Date obsDate)
	{
		this.obsDate = obsDate;
	}

	public BigDecimal getObsRangeHigh()
	{
		return this.obsRangeHigh;
	}

	public void setObsRangeHigh(final BigDecimal obsRangeHigh)
	{
		this.obsRangeHigh = obsRangeHigh;
	}

	public BigDecimal getObsRangeLow()
	{
		return this.obsRangeLow;
	}

	public void setObsRangeLow(final BigDecimal obsRangeLow)
	{
		this.obsRangeLow = obsRangeLow;
	}

	public BigDecimal getObsUnitsConceptId()
	{
		return this.obsUnitsConceptId;
	}

	public void setObsUnitsConceptId(final BigDecimal obsUnitsConceptId)
	{
		this.obsUnitsConceptId = obsUnitsConceptId;
	}

	public BigDecimal getObsValueAsConceptId()
	{
		return this.obsValueAsConceptId;
	}

	public void setObsValueAsConceptId(final BigDecimal obsValueAsConceptId)
	{
		this.obsValueAsConceptId = obsValueAsConceptId;
	}

	public BigDecimal getObsValueAsNumber()
	{
		return this.obsValueAsNumber;
	}

	public void setObsValueAsNumber(final BigDecimal obsValueAsNumber)
	{
		this.obsValueAsNumber = obsValueAsNumber;
	}

	public String getObsValueAsString()
	{
		return this.obsValueAsString;
	}

	public void setObsValueAsString(final String obsValueAsString)
	{
		this.obsValueAsString = obsValueAsString;
	}

	public String getSourceObsCode()
	{
		return this.sourceObsCode;
	}

	public void setSourceObsCode(final String sourceObsCode)
	{
		this.sourceObsCode = sourceObsCode;
	}

	public ObservationTypeRef getObservationTypeRef()
	{
		return this.observationTypeRef;
	}

	public void setObservationTypeRef(final ObservationTypeRef observationTypeRef)
	{
		this.observationTypeRef = observationTypeRef;
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