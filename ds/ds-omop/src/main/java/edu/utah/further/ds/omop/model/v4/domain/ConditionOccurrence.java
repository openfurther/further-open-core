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
 * The persistent class for the CONDITION_OCCURRENCE database table.
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
@Table(name = "CONDITION_OCCURRENCE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionOccurrence implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONDITION_OCCURRENCE_ID", unique = true, nullable = false)
	private Long conditionOccurrenceId;

	@Column(name = "ASSOCIATED_PROVIDER_ID")
	private BigDecimal associatedProviderId;

	@Column(name = "CONDITION_CONCEPT_ID", nullable = false)
	private BigDecimal conditionConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_END_DATE")
	private Date conditionEndDate;

	@Column(name = "CONDITION_SOURCE_VALUE", length = 50)
	private String conditionSourceValue;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_START_DATE", nullable = false)
	private Date conditionStartDate;

	@Column(name = "CONDITION_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal conditionTypeConceptId;

	@Column(name = "STOP_REASON", length = 20)
	private String stopReason;

	@Column(name = "VISIT_OCCURRENCE_ID")
	private BigDecimal visitOccurrenceId;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default Constructor
	 */
	public ConditionOccurrence()
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
		return this.conditionOccurrenceId;
	}

	public BigDecimal getAssociatedProviderId()
	{
		return this.associatedProviderId;
	}

	public void setAssociatedProviderId(final BigDecimal associatedProviderId)
	{
		this.associatedProviderId = associatedProviderId;
	}

	public BigDecimal getConditionConceptId()
	{
		return this.conditionConceptId;
	}

	public void setConditionConceptId(final BigDecimal conditionConceptId)
	{
		this.conditionConceptId = conditionConceptId;
	}

	public Date getConditionEndDate()
	{
		return this.conditionEndDate;
	}

	public void setConditionEndDate(final Date conditionEndDate)
	{
		this.conditionEndDate = conditionEndDate;
	}

	public String getConditionSourceValue()
	{
		return this.conditionSourceValue;
	}

	public void setConditionSourceValue(final String conditionSourceValue)
	{
		this.conditionSourceValue = conditionSourceValue;
	}

	public Date getConditionStartDate()
	{
		return this.conditionStartDate;
	}

	public void setConditionStartDate(final Date conditionStartDate)
	{
		this.conditionStartDate = conditionStartDate;
	}

	public BigDecimal getConditionTypeConceptId()
	{
		return this.conditionTypeConceptId;
	}

	public void setConditionTypeConceptId(final BigDecimal conditionTypeConceptId)
	{
		this.conditionTypeConceptId = conditionTypeConceptId;
	}

	public String getStopReason()
	{
		return this.stopReason;
	}

	public void setStopReason(final String stopReason)
	{
		this.stopReason = stopReason;
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