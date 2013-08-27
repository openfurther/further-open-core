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
 * The persistent class for the COHORT database table.
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
@Table(name = "COHORT")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Cohort implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COHORT_ID", unique = true, nullable = false)
	private Long cohortId;

	@Column(name = "COHORT_CONCEPT_ID", nullable = false)
	private BigDecimal cohortConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "COHORT_END_DATE")
	private Date cohortEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "COHORT_START_DATE", nullable = false)
	private Date cohortStartDate;

	@Column(name = "STOP_REASON", length = 20)
	private String stopReason;

	@Column(name = "SUBJECT_ID", nullable = false)
	private BigDecimal subjectId;

	/**
	 * Default constructor
	 */
	public Cohort()
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
		return this.cohortId;
	}

	public BigDecimal getCohortConceptId()
	{
		return this.cohortConceptId;
	}

	public void setCohortConceptId(final BigDecimal cohortConceptId)
	{
		this.cohortConceptId = cohortConceptId;
	}

	public Date getCohortEndDate()
	{
		return this.cohortEndDate;
	}

	public void setCohortEndDate(final Date cohortEndDate)
	{
		this.cohortEndDate = cohortEndDate;
	}

	public Date getCohortStartDate()
	{
		return this.cohortStartDate;
	}

	public void setCohortStartDate(final Date cohortStartDate)
	{
		this.cohortStartDate = cohortStartDate;
	}

	public String getStopReason()
	{
		return this.stopReason;
	}

	public void setStopReason(final String stopReason)
	{
		this.stopReason = stopReason;
	}

	public BigDecimal getSubjectId()
	{
		return this.subjectId;
	}

	public void setSubjectId(final BigDecimal subjectId)
	{
		this.subjectId = subjectId;
	}

}