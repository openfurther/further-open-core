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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the PROCEDURE_OCCURRENCE database table.
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
@Table(name = "PROCEDURE_OCCURRENCE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcedureOccurrence implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCEDURE_OCCURRENCE_ID", unique = true, nullable = false)
	private Long procedureOccurrenceId;

	@Column(name = "ASSOCIATED_PROVIDER_ID")
	private BigDecimal associatedProviderId;

	@Column(name = "PROCEDURE_CONCEPT_ID", nullable = false)
	private BigDecimal procedureConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "PROCEDURE_DATE", nullable = false)
	private Date procedureDate;

	@Column(name = "PROCEDURE_SOURCE_VALUE", length = 50)
	private String procedureSourceValue;

	@Column(name = "PROCEDURE_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal procedureTypeConceptId;

	@Column(name = "RELEVANT_CONDITION_CONCEPT_ID")
	private BigDecimal relevantConditionConceptId;

	@Column(name = "VISIT_OCCURRENCE_ID")
	private BigDecimal visitOccurrenceId;

	// bi-directional many-to-one association to ProcedureCostEntity
	@OneToMany(mappedBy = "procedureOccurrence")
	@XmlTransient
	private List<ProcedureCost> procedureCosts;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public ProcedureOccurrence()
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
		return this.procedureOccurrenceId;
	}

	public BigDecimal getAssociatedProviderId()
	{
		return this.associatedProviderId;
	}

	public void setAssociatedProviderId(final BigDecimal associatedProviderId)
	{
		this.associatedProviderId = associatedProviderId;
	}

	public BigDecimal getProcedureConceptId()
	{
		return this.procedureConceptId;
	}

	public void setProcedureConceptId(final BigDecimal procedureConceptId)
	{
		this.procedureConceptId = procedureConceptId;
	}

	public Date getProcedureDate()
	{
		return this.procedureDate;
	}

	public void setProcedureDate(final Date procedureDate)
	{
		this.procedureDate = procedureDate;
	}

	public String getProcedureSourceValue()
	{
		return this.procedureSourceValue;
	}

	public void setProcedureSourceValue(final String procedureSourceValue)
	{
		this.procedureSourceValue = procedureSourceValue;
	}

	public BigDecimal getProcedureTypeConceptId()
	{
		return this.procedureTypeConceptId;
	}

	public void setProcedureTypeConceptId(final BigDecimal procedureTypeConceptId)
	{
		this.procedureTypeConceptId = procedureTypeConceptId;
	}

	public BigDecimal getRelevantConditionConceptId()
	{
		return this.relevantConditionConceptId;
	}

	public void setRelevantConditionConceptId(final BigDecimal relevantConditionConceptId)
	{
		this.relevantConditionConceptId = relevantConditionConceptId;
	}

	public BigDecimal getVisitOccurrenceId()
	{
		return this.visitOccurrenceId;
	}

	public void setVisitOccurrenceId(final BigDecimal visitOccurrenceId)
	{
		this.visitOccurrenceId = visitOccurrenceId;
	}

	public List<ProcedureCost> getProcedureCosts()
	{
		return this.procedureCosts;
	}

	public void setProcedureCosts(final List<ProcedureCost> procedureCosts)
	{
		this.procedureCosts = procedureCosts;
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