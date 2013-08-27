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
 * The persistent class for the DRUG_EXPOSURE database table.
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
@Table(name = "DRUG_EXPOSURE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DrugExposure implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DRUG_EXPOSURE_ID", unique = true, nullable = false)
	private Long drugExposureId;

	@Column(name = "DAYS_SUPPLY", precision = 4)
	private BigDecimal daysSupply;

	@Column(name = "DRUG_CONCEPT_ID", nullable = false)
	private BigDecimal drugConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "DRUG_EXPOSURE_END_DATE")
	private Date drugExposureEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DRUG_EXPOSURE_START_DATE", nullable = false)
	private Date drugExposureStartDate;

	@Column(name = "DRUG_SOURCE_VALUE", length = 50)
	private String drugSourceValue;

	@Column(name = "DRUG_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal drugTypeConceptId;

	@Column(name = "PRESCRIBING_PROVIDER_ID")
	private BigDecimal prescribingProviderId;

	@Column(precision = 4)
	private BigDecimal quantity;

	@Column(precision = 3)
	private BigDecimal refills;

	@Column(name = "RELEVANT_CONDITION_CONCEPT_ID")
	private BigDecimal relevantConditionConceptId;

	@Column(length = 500)
	private String sig;

	@Column(name = "STOP_REASON", length = 20)
	private String stopReason;

	@Column(name = "VISIT_OCCURRENCE_ID")
	private BigDecimal visitOccurrenceId;

	// bi-directional many-to-one association to DrugCostEntity
	@OneToMany(mappedBy = "drugExposure")
	@XmlTransient
	private List<DrugCost> drugCosts;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public DrugExposure()
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
		return this.drugExposureId;
	}

	public BigDecimal getDaysSupply()
	{
		return this.daysSupply;
	}

	public void setDaysSupply(final BigDecimal daysSupply)
	{
		this.daysSupply = daysSupply;
	}

	public BigDecimal getDrugConceptId()
	{
		return this.drugConceptId;
	}

	public void setDrugConceptId(final BigDecimal drugConceptId)
	{
		this.drugConceptId = drugConceptId;
	}

	public Date getDrugExposureEndDate()
	{
		return this.drugExposureEndDate;
	}

	public void setDrugExposureEndDate(final Date drugExposureEndDate)
	{
		this.drugExposureEndDate = drugExposureEndDate;
	}

	public Date getDrugExposureStartDate()
	{
		return this.drugExposureStartDate;
	}

	public void setDrugExposureStartDate(final Date drugExposureStartDate)
	{
		this.drugExposureStartDate = drugExposureStartDate;
	}

	public String getDrugSourceValue()
	{
		return this.drugSourceValue;
	}

	public void setDrugSourceValue(final String drugSourceValue)
	{
		this.drugSourceValue = drugSourceValue;
	}

	public BigDecimal getDrugTypeConceptId()
	{
		return this.drugTypeConceptId;
	}

	public void setDrugTypeConceptId(final BigDecimal drugTypeConceptId)
	{
		this.drugTypeConceptId = drugTypeConceptId;
	}

	public BigDecimal getPrescribingProviderId()
	{
		return this.prescribingProviderId;
	}

	public void setPrescribingProviderId(final BigDecimal prescribingProviderId)
	{
		this.prescribingProviderId = prescribingProviderId;
	}

	public BigDecimal getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(final BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public BigDecimal getRefills()
	{
		return this.refills;
	}

	public void setRefills(final BigDecimal refills)
	{
		this.refills = refills;
	}

	public BigDecimal getRelevantConditionConceptId()
	{
		return this.relevantConditionConceptId;
	}

	public void setRelevantConditionConceptId(final BigDecimal relevantConditionConceptId)
	{
		this.relevantConditionConceptId = relevantConditionConceptId;
	}

	public String getSig()
	{
		return this.sig;
	}

	public void setSig(final String sig)
	{
		this.sig = sig;
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

	public List<DrugCost> getDrugCosts()
	{
		return this.drugCosts;
	}

	public void setDrugCosts(final List<DrugCost> drugCosts)
	{
		this.drugCosts = drugCosts;
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