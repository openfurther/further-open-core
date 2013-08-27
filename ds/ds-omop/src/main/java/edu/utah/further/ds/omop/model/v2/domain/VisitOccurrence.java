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
 * The persistent class for the VISIT_OCCURENCE database table.
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
@Table(name="VISIT_OCCURRENCE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitOccurrence implements PersistentEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="VISIT_OCCURRENCE_ID")
	private Long visitOccurrenceId;

	@Column(name="SOURCE_VISIT_CODE")
	private String sourceVisitCode;

	@Column(name="VISIT_CONCEPT_ID")
	private BigDecimal visitConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name="VISIT_END_DATE")
	private Date visitEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VISIT_START_DATE")
	private Date visitStartDate;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="PERSON_ID")
	private Person person;

	public VisitOccurrence() {
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId() {
		return this.visitOccurrenceId;
	}

	public void setId(final Long visitOccurrenceId) {
		this.visitOccurrenceId = visitOccurrenceId;
	}

	public String getSourceVisitCode() {
		return this.sourceVisitCode;
	}

	public void setSourceVisitCode(final String sourceVisitCode) {
		this.sourceVisitCode = sourceVisitCode;
	}

	public BigDecimal getVisitConceptId() {
		return this.visitConceptId;
	}

	public void setVisitConceptId(final BigDecimal visitConceptId) {
		this.visitConceptId = visitConceptId;
	}

	public Date getVisitEndDate() {
		return this.visitEndDate;
	}

	public void setVisitEndDate(final Date visitEndDate) {
		this.visitEndDate = visitEndDate;
	}

	public Date getVisitStartDate() {
		return this.visitStartDate;
	}

	public void setVisitStartDate(final Date visitStartDate) {
		this.visitStartDate = visitStartDate;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

}