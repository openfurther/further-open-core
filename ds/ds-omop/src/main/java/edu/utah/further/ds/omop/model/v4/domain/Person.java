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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the PERSON database table.
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
@Table(name = "PERSON")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PERSON_ID", unique = true, nullable = false)
	private Long personId;

	@Column(name = "CARE_SITE_ID")
	private BigDecimal careSiteId;

	@Column(name = "DAY_OF_BIRTH", precision = 2)
	private BigDecimal dayOfBirth;

	@Column(name = "ETHNICITY_CONCEPT_ID")
	private BigDecimal ethnicityConceptId;

	@Column(name = "ETHNICITY_SOURCE_VALUE", length = 50)
	private String ethnicitySourceValue;

	@Column(name = "GENDER_CONCEPT_ID", nullable = false)
	private BigDecimal genderConceptId;

	@Column(name = "GENDER_SOURCE_VALUE", length = 50)
	private String genderSourceValue;

	@Column(name = "MONTH_OF_BIRTH", precision = 2)
	private BigDecimal monthOfBirth;

	@Column(name = "PERSON_SOURCE_VALUE", length = 50)
	private String personSourceValue;

	@Column(name = "RACE_CONCEPT_ID")
	private BigDecimal raceConceptId;

	@Column(name = "RACE_SOURCE_VALUE", length = 50)
	private String raceSourceValue;

	@Column(name = "YEAR_OF_BIRTH", nullable = false, precision = 4)
	private BigDecimal yearOfBirth;

	// bi-directional many-to-one association to ConditionEraEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<ConditionEra> conditionEras;

	// bi-directional many-to-one association to ConditionOccurrenceEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<ConditionOccurrence> conditionOccurrences;

	// bi-directional one-to-one association to DeathEntity
	@OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
	private Death death;

	// bi-directional many-to-one association to DrugEraEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<DrugEra> drugEras;

	// bi-directional many-to-one association to DrugExposureEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<DrugExposure> drugExposures;

	// bi-directional many-to-one association to ObservationEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<Observation> observations;

	// bi-directional many-to-one association to PayerPlanPeriodEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<PayerPlanPeriod> payerPlanPeriods;

	// bi-directional many-to-one association to LocationEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID")
	private Location location;

	// bi-directional many-to-one association to ProviderEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROVIDER_ID")
	private Provider provider;

	// bi-directional many-to-one association to ProcedureOccurrenceEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<ProcedureOccurrence> procedureOccurrences;

	// bi-directional many-to-one association to VisitOccurrenceEntity
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<VisitOccurrence> visitOccurrences;

	/**
	 * Default constructor
	 */
	public Person()
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
		return this.personId;
	}

	protected void setId(final Long personId)
	{
		this.personId = personId;
	}

	public BigDecimal getCareSiteId()
	{
		return this.careSiteId;
	}

	public void setCareSiteId(final BigDecimal careSiteId)
	{
		this.careSiteId = careSiteId;
	}

	public BigDecimal getDayOfBirth()
	{
		return this.dayOfBirth;
	}

	public void setDayOfBirth(final BigDecimal dayOfBirth)
	{
		this.dayOfBirth = dayOfBirth;
	}

	public BigDecimal getEthnicityConceptId()
	{
		return this.ethnicityConceptId;
	}

	public void setEthnicityConceptId(final BigDecimal ethnicityConceptId)
	{
		this.ethnicityConceptId = ethnicityConceptId;
	}

	public String getEthnicitySourceValue()
	{
		return this.ethnicitySourceValue;
	}

	public void setEthnicitySourceValue(final String ethnicitySourceValue)
	{
		this.ethnicitySourceValue = ethnicitySourceValue;
	}

	public BigDecimal getGenderConceptId()
	{
		return this.genderConceptId;
	}

	public void setGenderConceptId(final BigDecimal genderConceptId)
	{
		this.genderConceptId = genderConceptId;
	}

	public String getGenderSourceValue()
	{
		return this.genderSourceValue;
	}

	public void setGenderSourceValue(final String genderSourceValue)
	{
		this.genderSourceValue = genderSourceValue;
	}

	public BigDecimal getMonthOfBirth()
	{
		return this.monthOfBirth;
	}

	public void setMonthOfBirth(final BigDecimal monthOfBirth)
	{
		this.monthOfBirth = monthOfBirth;
	}

	public String getPersonSourceValue()
	{
		return this.personSourceValue;
	}

	public void setPersonSourceValue(final String personSourceValue)
	{
		this.personSourceValue = personSourceValue;
	}

	public BigDecimal getRaceConceptId()
	{
		return this.raceConceptId;
	}

	public void setRaceConceptId(final BigDecimal raceConceptId)
	{
		this.raceConceptId = raceConceptId;
	}

	public String getRaceSourceValue()
	{
		return this.raceSourceValue;
	}

	public void setRaceSourceValue(final String raceSourceValue)
	{
		this.raceSourceValue = raceSourceValue;
	}

	public BigDecimal getYearOfBirth()
	{
		return this.yearOfBirth;
	}

	public void setYearOfBirth(final BigDecimal yearOfBirth)
	{
		this.yearOfBirth = yearOfBirth;
	}

	public List<ConditionEra> getConditionEras()
	{
		return this.conditionEras;
	}

	public void setConditionEras(final List<ConditionEra> conditionEras)
	{
		this.conditionEras = conditionEras;
	}

	public List<ConditionOccurrence> getConditionOccurrences()
	{
		return this.conditionOccurrences;
	}

	public void setConditionOccurrences(
			final List<ConditionOccurrence> conditionOccurrences)
	{
		this.conditionOccurrences = conditionOccurrences;
	}

	public Death getDeath()
	{
		return this.death;
	}

	public void setDeath(final Death death)
	{
		this.death = death;
	}

	public List<DrugEra> getDrugEras()
	{
		return this.drugEras;
	}

	public void setDrugEras(final List<DrugEra> drugEras)
	{
		this.drugEras = drugEras;
	}

	public List<DrugExposure> getDrugExposures()
	{
		return this.drugExposures;
	}

	public void setDrugExposures(final List<DrugExposure> drugExposures)
	{
		this.drugExposures = drugExposures;
	}

	public List<Observation> getObservations()
	{
		return this.observations;
	}

	public void setObservations(final List<Observation> observations)
	{
		this.observations = observations;
	}

	public List<PayerPlanPeriod> getPayerPlanPeriods()
	{
		return this.payerPlanPeriods;
	}

	public void setPayerPlanPeriods(final List<PayerPlanPeriod> payerPlanPeriods)
	{
		this.payerPlanPeriods = payerPlanPeriods;
	}

	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(final Location location)
	{
		this.location = location;
	}

	public Provider getProvider()
	{
		return this.provider;
	}

	public void setProvider(final Provider provider)
	{
		this.provider = provider;
	}

	public List<ProcedureOccurrence> getProcedureOccurrences()
	{
		return this.procedureOccurrences;
	}

	public void setProcedureOccurrences(
			final List<ProcedureOccurrence> procedureOccurrences)
	{
		this.procedureOccurrences = procedureOccurrences;
	}

	public List<VisitOccurrence> getVisitOccurrences()
	{
		return this.visitOccurrences;
	}

	public void setVisitOccurrences(final List<VisitOccurrence> visitOccurrences)
	{
		this.visitOccurrences = visitOccurrences;
	}

}