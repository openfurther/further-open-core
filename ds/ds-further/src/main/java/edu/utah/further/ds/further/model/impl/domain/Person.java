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
package edu.utah.further.ds.further.model.impl.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Formula;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Persistent entity implementation of {@link Person}
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
 * @version Sep 1, 2009
 */
@Entity
@Implementation
@Table(name = "FPERSON")
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements PersistentEntity<PersonId>
{
	// ========================= CONSTANTS ===================================

	@Transient
	private static final long serialVersionUID = -695456808008390254L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private PersonId id;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String compositeId;

	@Column(name = "administrative_gender_nmspc_id")
	private Long administrativeGenderNamespaceId;

	@Column(name = "administrative_gender_cid")
	private String administrativeGender;

	@Column(name = "race_nmspc_id")
	private Long raceNamespaceId;

	@Column(name = "race_cid")
	private String race;

	@Column(name = "ethnicity_nmspc_id")
	private Long ethnicityNamespaceId;

	@Column(name = "ethnicity_cid")
	private String ethnicity;

	@Column(name = "birth_dt")
	private Date dateOfBirth;

	@Column(name = "birth_yr")
	private Long birthYear;

	@Column(name = "birth_mon")
	private Long birthMonth;

	@Column(name = "birth_day")
	private Long birthDay;

	@Column(name = "education_level")
	private String educationLevel;

	@Column(name = "primary_language_nmspc_id")
	private Long primaryLanguageNamespaceId;

	@Column(name = "primary_language_cid")
	private String primaryLanguage;

	@Column(name = "marital_status_nmspc_id")
	private Long maritalStatusNamespaceId;

	@Column(name = "marital_status_cid")
	private String maritalStatus;

	@Column(name = "religion_nmspc_id")
	private Long religionNamespaceId;

	@Column(name = "religion_cid")
	private String religion;

	@Column(name = "multiple_birth_indicator")
	private Boolean multipleBirthIndicator;

	@Column(name = "multiple_birth_order_number")
	private Integer multipleBirthIndicatorOrderNumber;

	@Column(name = "vital_status_nmspc_id")
	private Long vitalStatusNamespaceId;

	@Column(name = "vital_status")
	private String vitalStatus;

	@Column(name = "cause_of_death_nmspc_id")
	private Long causeOfDeathNamespaceId;

	@Column(name = "cause_of_death_cid")
	private String causeOfDeath;

	@Column(name = "death_dt")
	private Date dateOfDeath;

	@Column(name = "death_yr")
	private Long deathYear;

	@Column(name = "pedigree_quality")
	private Long pedigreeQuality;

	/* This IS Oracle specific */
	@Formula(value = "floor(months_between(current_date, birth_dt)/12)")
	private Integer age;

	@OneToMany(targetEntity = Observation.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fperson_id", referencedColumnName = "fperson_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Observation> observations;

	@OneToMany(targetEntity = Provider.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fperson_id", referencedColumnName = "fperson_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Provider> providers;

	@OneToMany(targetEntity = Order.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fperson_id", referencedColumnName = "fperson_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Order> orders;

	@OneToMany(targetEntity = Encounter.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fperson_id", referencedColumnName = "fperson_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Encounter> encounters;

	@OneToMany(targetEntity = Location.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fperson_id", referencedColumnName = "fperson_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Location> locations;

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public PersonId getId()
	{
		return id;
	}
	
	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(PersonId id)
	{
		this.id = id;
	}

	/**
	 * Return the compositeId property.
	 * 
	 * @return the compositeId
	 */
	public String getCompositeId()
	{
		return compositeId;
	}

	/**
	 * Set a new value for the compositeId property.
	 * 
	 * @param compositeId
	 *            the compositeId to set
	 */
	public void setCompositeId(String compositeId)
	{
		this.compositeId = compositeId;
	}

	/**
	 * Return the administrativeGenderNamespaceId property.
	 * 
	 * @return the administrativeGenderNamespaceId
	 */
	public Long getAdministrativeGenderNamespaceId()
	{
		return administrativeGenderNamespaceId;
	}

	/**
	 * Set a new value for the administrativeGenderNamespaceId property.
	 * 
	 * @param administrativeGenderNamespaceId
	 *            the administrativeGenderNamespaceId to set
	 */
	public void setAdministrativeGenderNamespaceId(Long administrativeGenderNamespaceId)
	{
		this.administrativeGenderNamespaceId = administrativeGenderNamespaceId;
	}

	/**
	 * Return the administrativeGender property.
	 * 
	 * @return the administrativeGender
	 */
	public String getAdministrativeGender()
	{
		return administrativeGender;
	}

	/**
	 * Set a new value for the administrativeGender property.
	 * 
	 * @param administrativeGender
	 *            the administrativeGender to set
	 */
	public void setAdministrativeGender(String administrativeGender)
	{
		this.administrativeGender = administrativeGender;
	}

	/**
	 * Return the raceNamespaceId property.
	 * 
	 * @return the raceNamespaceId
	 */
	public Long getRaceNamespaceId()
	{
		return raceNamespaceId;
	}

	/**
	 * Set a new value for the raceNamespaceId property.
	 * 
	 * @param raceNamespaceId
	 *            the raceNamespaceId to set
	 */
	public void setRaceNamespaceId(Long raceNamespaceId)
	{
		this.raceNamespaceId = raceNamespaceId;
	}

	/**
	 * Return the race property.
	 * 
	 * @return the race
	 */
	public String getRace()
	{
		return race;
	}

	/**
	 * Set a new value for the race property.
	 * 
	 * @param race
	 *            the race to set
	 */
	public void setRace(String race)
	{
		this.race = race;
	}

	/**
	 * Return the ethnicityNamespaceId property.
	 * 
	 * @return the ethnicityNamespaceId
	 */
	public Long getEthnicityNamespaceId()
	{
		return ethnicityNamespaceId;
	}

	/**
	 * Set a new value for the ethnicityNamespaceId property.
	 * 
	 * @param ethnicityNamespaceId
	 *            the ethnicityNamespaceId to set
	 */
	public void setEthnicityNamespaceId(Long ethnicityNamespaceId)
	{
		this.ethnicityNamespaceId = ethnicityNamespaceId;
	}

	/**
	 * Return the ethnicity property.
	 * 
	 * @return the ethnicity
	 */
	public String getEthnicity()
	{
		return ethnicity;
	}

	/**
	 * Set a new value for the ethnicity property.
	 * 
	 * @param ethnicity
	 *            the ethnicity to set
	 */
	public void setEthnicity(String ethnicity)
	{
		this.ethnicity = ethnicity;
	}

	/**
	 * Return the dateOfBirth property.
	 * 
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	/**
	 * Set a new value for the dateOfBirth property.
	 * 
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Return the birthYear property.
	 * 
	 * @return the birthYear
	 */
	public Long getBirthYear()
	{
		return birthYear;
	}

	/**
	 * Set a new value for the birthYear property.
	 * 
	 * @param birthYear
	 *            the birthYear to set
	 */
	public void setBirthYear(Long birthYear)
	{
		this.birthYear = birthYear;
	}

	/**
	 * Return the birthMonth property.
	 * 
	 * @return the birthMonth
	 */
	public Long getBirthMonth()
	{
		return birthMonth;
	}

	/**
	 * Set a new value for the birthMonth property.
	 * 
	 * @param birthMonth
	 *            the birthMonth to set
	 */
	public void setBirthMonth(Long birthMonth)
	{
		this.birthMonth = birthMonth;
	}

	/**
	 * Return the birthDay property.
	 * 
	 * @return the birthDay
	 */
	public Long getBirthDay()
	{
		return birthDay;
	}

	/**
	 * Set a new value for the birthDay property.
	 * 
	 * @param birthDay
	 *            the birthDay to set
	 */
	public void setBirthDay(Long birthDay)
	{
		this.birthDay = birthDay;
	}

	/**
	 * Return the educationLevel property.
	 * 
	 * @return the educationLevel
	 */
	public String getEducationLevel()
	{
		return educationLevel;
	}

	/**
	 * Set a new value for the educationLevel property.
	 * 
	 * @param educationLevel
	 *            the educationLevel to set
	 */
	public void setEducationLevel(String educationLevel)
	{
		this.educationLevel = educationLevel;
	}

	/**
	 * Return the primaryLanguageNamespaceId property.
	 * 
	 * @return the primaryLanguageNamespaceId
	 */
	public Long getPrimaryLanguageNamespaceId()
	{
		return primaryLanguageNamespaceId;
	}

	/**
	 * Set a new value for the primaryLanguageNamespaceId property.
	 * 
	 * @param primaryLanguageNamespaceId
	 *            the primaryLanguageNamespaceId to set
	 */
	public void setPrimaryLanguageNamespaceId(Long primaryLanguageNamespaceId)
	{
		this.primaryLanguageNamespaceId = primaryLanguageNamespaceId;
	}

	/**
	 * Return the primaryLanguage property.
	 * 
	 * @return the primaryLanguage
	 */
	public String getPrimaryLanguage()
	{
		return primaryLanguage;
	}

	/**
	 * Set a new value for the primaryLanguage property.
	 * 
	 * @param primaryLanguage
	 *            the primaryLanguage to set
	 */
	public void setPrimaryLanguage(String primaryLanguage)
	{
		this.primaryLanguage = primaryLanguage;
	}

	/**
	 * Return the maritalStatusNamespaceId property.
	 * 
	 * @return the maritalStatusNamespaceId
	 */
	public Long getMaritalStatusNamespaceId()
	{
		return maritalStatusNamespaceId;
	}

	/**
	 * Set a new value for the maritalStatusNamespaceId property.
	 * 
	 * @param maritalStatusNamespaceId
	 *            the maritalStatusNamespaceId to set
	 */
	public void setMaritalStatusNamespaceId(Long maritalStatusNamespaceId)
	{
		this.maritalStatusNamespaceId = maritalStatusNamespaceId;
	}

	/**
	 * Return the maritalStatus property.
	 * 
	 * @return the maritalStatus
	 */
	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	/**
	 * Set a new value for the maritalStatus property.
	 * 
	 * @param maritalStatus
	 *            the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus)
	{
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Return the religionNamespaceId property.
	 * 
	 * @return the religionNamespaceId
	 */
	public Long getReligionNamespaceId()
	{
		return religionNamespaceId;
	}

	/**
	 * Set a new value for the religionNamespaceId property.
	 * 
	 * @param religionNamespaceId
	 *            the religionNamespaceId to set
	 */
	public void setReligionNamespaceId(Long religionNamespaceId)
	{
		this.religionNamespaceId = religionNamespaceId;
	}

	/**
	 * Return the religion property.
	 * 
	 * @return the religion
	 */
	public String getReligion()
	{
		return religion;
	}

	/**
	 * Set a new value for the religion property.
	 * 
	 * @param religion
	 *            the religion to set
	 */
	public void setReligion(String religion)
	{
		this.religion = religion;
	}

	/**
	 * Return the multipleBirthIndicator property.
	 * 
	 * @return the multipleBirthIndicator
	 */
	public Boolean getMultipleBirthIndicator()
	{
		return multipleBirthIndicator;
	}

	/**
	 * Set a new value for the multipleBirthIndicator property.
	 * 
	 * @param multipleBirthIndicator
	 *            the multipleBirthIndicator to set
	 */
	public void setMultipleBirthIndicator(Boolean multipleBirthIndicator)
	{
		this.multipleBirthIndicator = multipleBirthIndicator;
	}

	/**
	 * Return the multipleBirthIndicatorOrderNumber property.
	 * 
	 * @return the multipleBirthIndicatorOrderNumber
	 */
	public Integer getMultipleBirthIndicatorOrderNumber()
	{
		return multipleBirthIndicatorOrderNumber;
	}

	/**
	 * Set a new value for the multipleBirthIndicatorOrderNumber property.
	 * 
	 * @param multipleBirthIndicatorOrderNumber
	 *            the multipleBirthIndicatorOrderNumber to set
	 */
	public void setMultipleBirthIndicatorOrderNumber(
			Integer multipleBirthIndicatorOrderNumber)
	{
		this.multipleBirthIndicatorOrderNumber = multipleBirthIndicatorOrderNumber;
	}

	/**
	 * Return the vitalStatusNamespaceId property.
	 * 
	 * @return the vitalStatusNamespaceId
	 */
	public Long getVitalStatusNamespaceId()
	{
		return vitalStatusNamespaceId;
	}

	/**
	 * Set a new value for the vitalStatusNamespaceId property.
	 * 
	 * @param vitalStatusNamespaceId
	 *            the vitalStatusNamespaceId to set
	 */
	public void setVitalStatusNamespaceId(Long vitalStatusNamespaceId)
	{
		this.vitalStatusNamespaceId = vitalStatusNamespaceId;
	}

	/**
	 * Return the vitalStatus property.
	 * 
	 * @return the vitalStatus
	 */
	public String getVitalStatus()
	{
		return vitalStatus;
	}

	/**
	 * Set a new value for the vitalStatus property.
	 * 
	 * @param vitalStatus
	 *            the vitalStatus to set
	 */
	public void setVitalStatus(String vitalStatus)
	{
		this.vitalStatus = vitalStatus;
	}

	/**
	 * Return the causeOfDeathNamespaceId property.
	 * 
	 * @return the causeOfDeathNamespaceId
	 */
	public Long getCauseOfDeathNamespaceId()
	{
		return causeOfDeathNamespaceId;
	}

	/**
	 * Set a new value for the causeOfDeathNamespaceId property.
	 * 
	 * @param causeOfDeathNamespaceId
	 *            the causeOfDeathNamespaceId to set
	 */
	public void setCauseOfDeathNamespaceId(Long causeOfDeathNamespaceId)
	{
		this.causeOfDeathNamespaceId = causeOfDeathNamespaceId;
	}

	/**
	 * Return the causeOfDeath property.
	 * 
	 * @return the causeOfDeath
	 */
	public String getCauseOfDeath()
	{
		return causeOfDeath;
	}

	/**
	 * Set a new value for the causeOfDeath property.
	 * 
	 * @param causeOfDeath
	 *            the causeOfDeath to set
	 */
	public void setCauseOfDeath(String causeOfDeath)
	{
		this.causeOfDeath = causeOfDeath;
	}

	/**
	 * Return the dateOfDeath property.
	 * 
	 * @return the dateOfDeath
	 */
	public Date getDateOfDeath()
	{
		return dateOfDeath;
	}

	/**
	 * Set a new value for the dateOfDeath property.
	 * 
	 * @param dateOfDeath
	 *            the dateOfDeath to set
	 */
	public void setDateOfDeath(Date dateOfDeath)
	{
		this.dateOfDeath = dateOfDeath;
	}

	/**
	 * Return the deathYear property.
	 * 
	 * @return the deathYear
	 */
	public Long getDeathYear()
	{
		return deathYear;
	}

	/**
	 * Set a new value for the deathYear property.
	 * 
	 * @param deathYear
	 *            the deathYear to set
	 */
	public void setDeathYear(Long deathYear)
	{
		this.deathYear = deathYear;
	}

	/**
	 * Return the pedigreeQuality property.
	 * 
	 * @return the pedigreeQuality
	 */
	public Long getPedigreeQuality()
	{
		return pedigreeQuality;
	}

	/**
	 * Set a new value for the pedigreeQuality property.
	 * 
	 * @param pedigreeQuality
	 *            the pedigreeQuality to set
	 */
	public void setPedigreeQuality(Long pedigreeQuality)
	{
		this.pedigreeQuality = pedigreeQuality;
	}

	/**
	 * Return the age property.
	 * 
	 * @return the age
	 */
	public Integer getAge()
	{
		return age;
	}

	/**
	 * Set a new value for the age property.
	 * 
	 * @param age
	 *            the age to set
	 */
	public void setAge(Integer age)
	{
		this.age = age;
	}

	/**
	 * Return the observations property.
	 * 
	 * @return the observations
	 */
	public Collection<Observation> getObservations()
	{
		return observations;
	}

	/**
	 * Set a new value for the observations property.
	 * 
	 * @param observations
	 *            the observations to set
	 */
	public void setObservations(Collection<Observation> observations)
	{
		this.observations = observations;
	}

	/**
	 * Return the providers property.
	 * 
	 * @return the providers
	 */
	public Collection<Provider> getProviders()
	{
		return providers;
	}

	/**
	 * Set a new value for the providers property.
	 * 
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(Collection<Provider> providers)
	{
		this.providers = providers;
	}

	/**
	 * Return the orders property.
	 * 
	 * @return the orders
	 */
	public Collection<Order> getOrders()
	{
		return orders;
	}

	/**
	 * Set a new value for the orders property.
	 * 
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(Collection<Order> orders)
	{
		this.orders = orders;
	}

	/**
	 * Return the encounters property.
	 * 
	 * @return the encounters
	 */
	public Collection<Encounter> getEncounters()
	{
		return encounters;
	}

	/**
	 * Set a new value for the encounters property.
	 * 
	 * @param encounters
	 *            the encounters to set
	 */
	public void setEncounters(Collection<Encounter> encounters)
	{
		this.encounters = encounters;
	}

	/**
	 * Return the locations property.
	 * 
	 * @return the locations
	 */
	public Collection<Location> getLocations()
	{
		return locations;
	}

	/**
	 * Set a new value for the locations property.
	 * 
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(Collection<Location> locations)
	{
		this.locations = locations;
	}
	
	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final Person that = (Person) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ReflectionToStringBuilder.toStringExclude(this, new String[]
		{ "orders", "providers", "observations", "encounters", "locations" }).toString();
	}
}
