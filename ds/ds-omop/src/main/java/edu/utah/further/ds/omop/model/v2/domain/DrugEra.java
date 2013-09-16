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
 * The persistent class for the DRUG_ERA database table.
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
@Table(name = "DRUG_ERA")
@XmlRootElement(name="DrugEra")
@XmlAccessorType(XmlAccessType.FIELD)
public class DrugEra implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DRUG_ERA_ID")
	private Long drugEraId;

	@Column(name = "DRUG_CONCEPT_ID")
	private BigDecimal drugConceptId;

	@Temporal(TemporalType.DATE)
	@Column(name = "DRUG_ERA_END_DATE")
	private Date drugEraEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DRUG_ERA_START_DATE")
	private Date drugEraStartDate;

	@Column(name = "DRUG_EXPOSURE_COUNT")
	private BigDecimal drugExposureCount;

	// bi-directional many-to-one association to DrugExposureRef
	@ManyToOne
	@JoinColumn(name = "DRUG_EXPOSURE_TYPE")
	private DrugExposureRef drugExposureRef;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "PERSON_ID")
	private Person person;

	public DrugEra()
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
		return this.drugEraId;
	}

	protected void setId(final Long drugEraId)
	{
		this.drugEraId = drugEraId;
	}

	public BigDecimal getDrugConceptId()
	{
		return this.drugConceptId;
	}

	public void setDrugConceptId(final BigDecimal drugConceptId)
	{
		this.drugConceptId = drugConceptId;
	}

	public Date getDrugEraEndDate()
	{
		return this.drugEraEndDate;
	}

	public void setDrugEraEndDate(final Date drugEraEndDate)
	{
		this.drugEraEndDate = drugEraEndDate;
	}

	public Date getDrugEraStartDate()
	{
		return this.drugEraStartDate;
	}

	public void setDrugEraStartDate(final Date drugEraStartDate)
	{
		this.drugEraStartDate = drugEraStartDate;
	}

	public BigDecimal getDrugExposureCount()
	{
		return this.drugExposureCount;
	}

	public void setDrugExposureCount(final BigDecimal drugExposureCount)
	{
		this.drugExposureCount = drugExposureCount;
	}

	public DrugExposureRef getDrugExposureRef()
	{
		return this.drugExposureRef;
	}

	public void setDrugExposureRef(final DrugExposureRef drugExposureRef)
	{
		this.drugExposureRef = drugExposureRef;
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