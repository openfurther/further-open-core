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
 * The persistent class for the CONDITION_OCCURENCE database table.
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
@Table(name = "CONDITION_OCCURRENCE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionOccurrence implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONDITION_OCCURRENCE_ID")
	private Long conditionOccurrenceId;

	@Column(name = "CONDITION_CONCEPT_ID")
	private BigDecimal conditionConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_END_DATE")
	private Date conditionEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "CONDITION_START_DATE")
	private Date conditionStartDate;

	@Column(name = "DX_QUALIFIER")
	private String dxQualifier;

	@Column(name = "SOURCE_CONDITION_CODE")
	private String sourceConditionCode;

	@Column(name = "STOP_REASON")
	private String stopReason;

	// bi-directional many-to-one association to ConditionOccurrenceRef
	@ManyToOne
	@JoinColumn(name = "CONDITION_OCCURRENCE_TYPE")
	private ConditionOccurrenceRef conditionOccurrenceRef;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "PERSON_ID")
	private Person person;

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

	protected void setId(final Long conditionOccurrenceId)
	{
		this.conditionOccurrenceId = conditionOccurrenceId;
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

	public Date getConditionStartDate()
	{
		return this.conditionStartDate;
	}

	public void setConditionStartDate(final Date conditionStartDate)
	{
		this.conditionStartDate = conditionStartDate;
	}

	public String getDxQualifier()
	{
		return this.dxQualifier;
	}

	public void setDxQualifier(final String dxQualifier)
	{
		this.dxQualifier = dxQualifier;
	}

	public String getSourceConditionCode()
	{
		return this.sourceConditionCode;
	}

	public void setSourceConditionCode(final String sourceConditionCode)
	{
		this.sourceConditionCode = sourceConditionCode;
	}

	public String getStopReason()
	{
		return this.stopReason;
	}

	public void setStopReason(final String stopReason)
	{
		this.stopReason = stopReason;
	}

	public ConditionOccurrenceRef getConditionOccurrenceRef()
	{
		return this.conditionOccurrenceRef;
	}

	public void setConditionOccurrenceRef(final ConditionOccurrenceRef conditionOccurrenceRef)
	{
		this.conditionOccurrenceRef = conditionOccurrenceRef;
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