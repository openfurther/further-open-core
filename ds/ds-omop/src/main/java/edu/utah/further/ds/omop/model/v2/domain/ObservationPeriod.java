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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the OBSERVATION_PERIOD database table.
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
@Table(name = "OBSERVATION_PERIOD")
@XmlRootElement(name = "ObservationPeriod")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObservationPeriod implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBSERVATION_PERIOD_ID")
	private Long observationPeriodId;

	private String confidence;

	@Column(name = "DX_DATA_AVAILABILITY")
	private String dxDataAvailability;

	@Column(name = "HOSPITAL_DATA_AVAILABILITY")
	private String hospitalDataAvailability;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_PERIOD_END_DATE")
	private Date observationPeriodEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_PERIOD_START_DATE")
	private Date observationPeriodStartDate;

	@Column(name = "PERSON_STATUS_CONCEPT_ID")
	private BigDecimal personStatusConceptId;

	@Column(name = "RX_DATA_AVAILABILITY")
	private String rxDataAvailability;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "PERSON_ID")
	private Person person;

	public ObservationPeriod()
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
		return this.observationPeriodId;
	}

	protected void setId(final Long observationPeriodId)
	{
		this.observationPeriodId = observationPeriodId;
	}

	public String getConfidence()
	{
		return this.confidence;
	}

	public void setConfidence(final String confidence)
	{
		this.confidence = confidence;
	}

	public String getDxDataAvailability()
	{
		return this.dxDataAvailability;
	}

	public void setDxDataAvailability(final String dxDataAvailability)
	{
		this.dxDataAvailability = dxDataAvailability;
	}

	public String getHospitalDataAvailability()
	{
		return this.hospitalDataAvailability;
	}

	public void setHospitalDataAvailability(final String hospitalDataAvailability)
	{
		this.hospitalDataAvailability = hospitalDataAvailability;
	}

	public Date getObservationPeriodEndDate()
	{
		return this.observationPeriodEndDate;
	}

	public void setObservationPeriodEndDate(final Date observationPeriodEndDate)
	{
		this.observationPeriodEndDate = observationPeriodEndDate;
	}

	public Date getObservationPeriodStartDate()
	{
		return this.observationPeriodStartDate;
	}

	public void setObservationPeriodStartDate(final Date observationPeriodStartDate)
	{
		this.observationPeriodStartDate = observationPeriodStartDate;
	}

	public BigDecimal getPersonStatusConceptId()
	{
		return this.personStatusConceptId;
	}

	public void setPersonStatusConceptId(final BigDecimal personStatusConceptId)
	{
		this.personStatusConceptId = personStatusConceptId;
	}

	public String getRxDataAvailability()
	{
		return this.rxDataAvailability;
	}

	public void setRxDataAvailability(final String rxDataAvailability)
	{
		this.rxDataAvailability = rxDataAvailability;
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