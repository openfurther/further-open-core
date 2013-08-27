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
 * The persistent class for the CONDITION_ERA database table.
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
@Table(name = "CONDITION_ERA")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionEra implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONDITION_ERA_ID", unique = true, nullable = false)
	private Long conditionEraId;

	@Column(name = "CONDITION_CONCEPT_ID", nullable = false)
	private BigDecimal conditionConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_ERA_END_DATE", nullable = false)
	private Date conditionEraEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_ERA_START_DATE", nullable = false)
	private Date conditionEraStartDate;

	@Column(name = "CONDITION_OCCURRENCE_COUNT", precision = 4)
	private BigDecimal conditionOccurrenceCount;

	@Column(name = "CONDITION_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal conditionTypeConceptId;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public ConditionEra()
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
		return this.conditionEraId;
	}

	public BigDecimal getConditionConceptId()
	{
		return this.conditionConceptId;
	}

	public void setConditionConceptId(final BigDecimal conditionConceptId)
	{
		this.conditionConceptId = conditionConceptId;
	}

	public Date getConditionEraEndDate()
	{
		return this.conditionEraEndDate;
	}

	public void setConditionEraEndDate(final Date conditionEraEndDate)
	{
		this.conditionEraEndDate = conditionEraEndDate;
	}

	public Date getConditionEraStartDate()
	{
		return this.conditionEraStartDate;
	}

	public void setConditionEraStartDate(final Date conditionEraStartDate)
	{
		this.conditionEraStartDate = conditionEraStartDate;
	}

	public BigDecimal getConditionOccurrenceCount()
	{
		return this.conditionOccurrenceCount;
	}

	public void setConditionOccurrenceCount(final BigDecimal conditionOccurrenceCount)
	{
		this.conditionOccurrenceCount = conditionOccurrenceCount;
	}

	public BigDecimal getConditionTypeConceptId()
	{
		return this.conditionTypeConceptId;
	}

	public void setConditionTypeConceptId(final BigDecimal conditionTypeConceptId)
	{
		this.conditionTypeConceptId = conditionTypeConceptId;
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