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
import javax.persistence.Id;
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
 * @version Apr 17, 2013
 */
@Entity
@Table(name = "OBSERVATION_PERIOD")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ObservationPeriod implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBSERVATION_PERIOD_ID", unique = true, nullable = false)
	private Long observationPeriodId;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_PERIOD_END_DATE", nullable = false)
	private Date observationPeriodEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "OBSERVATION_PERIOD_START_DATE", nullable = false)
	private Date observationPeriodStartDate;

	@Column(name = "PERSON_ID", nullable = false)
	private BigDecimal personId;

	/**
	 * Default constructor
	 */
	public ObservationPeriod()
	{
	}

	@Override
	public Long getId()
	{
		return this.observationPeriodId;
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

	public BigDecimal getPersonId()
	{
		return this.personId;
	}

	public void setPersonId(final BigDecimal personId)
	{
		this.personId = personId;
	}

}