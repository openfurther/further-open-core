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
package edu.utah.further.ds.openmrs.model.v1_9.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the person database table.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 3, 2013
 */
@Table(name="person")
@Entity
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "person_id")
	private Integer personId;

	@Temporal(TemporalType.DATE)
	private Date birthdate;

	@Column(name = "birthdate_estimated")
	private Boolean birthdateEstimated;

	@Column(name = "cause_of_death")
	private Long causeOfDeath;

	@Column(name = "changed_by")
	private Long changedBy;

	private Long creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_changed")
	private Date dateChanged;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided")
	private Date dateVoided;

	private Boolean dead;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "death_date")
	private Date deathDate;

	private String gender;

	private String uuid;

	@Column(name = "void_reason")
	private String voidReason;

	private Boolean voided;

	@Column(name = "voided_by")
	private Long voidedBy;
	
	@Formula("extract(year from birthdate)")
	private Long birthYear;

	// bi-directional many-to-one association to Observation
	@OneToMany(mappedBy = "person")
	@XmlTransient
	private List<Observation> observations = new ArrayList<>();

	// bi-directional one-to-one association to Patient
	@OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
	@XmlTransient
	private Patient patient;

	// bi-directional many-to-one association to PersonAttribute
	@OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
	@XmlElementWrapper(name="personAttributes")
	@XmlElement(name="personAttribute")
	private List<PersonAttribute> personAttributes = new ArrayList<>();

	public Person()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Integer getId()
	{
		return this.personId;
	}

	public void setId(final Integer personId)
	{
		this.personId = personId;
	}

	public Date getBirthdate()
	{
		return this.birthdate;
	}

	public void setBirthdate(final Date birthdate)
	{
		this.birthdate = birthdate;
	}

	public Boolean getBirthdateEstimated()
	{
		return this.birthdateEstimated;
	}

	public void setBirthdateEstimated(final Boolean birthdateEstimated)
	{
		this.birthdateEstimated = birthdateEstimated;
	}

	public Long getCauseOfDeath()
	{
		return this.causeOfDeath;
	}

	public void setCauseOfDeath(final Long causeOfDeath)
	{
		this.causeOfDeath = causeOfDeath;
	}

	public Long getChangedBy()
	{
		return this.changedBy;
	}

	public void setChangedBy(final Long changedBy)
	{
		this.changedBy = changedBy;
	}

	public Long getCreator()
	{
		return this.creator;
	}

	public void setCreator(final Long creator)
	{
		this.creator = creator;
	}

	public Date getDateChanged()
	{
		return this.dateChanged;
	}

	public void setDateChanged(final Date dateChanged)
	{
		this.dateChanged = dateChanged;
	}

	public Date getDateCreated()
	{
		return this.dateCreated;
	}

	public void setDateCreated(final Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public Date getDateVoided()
	{
		return this.dateVoided;
	}

	public void setDateVoided(final Date dateVoided)
	{
		this.dateVoided = dateVoided;
	}

	public Boolean getDead()
	{
		return this.dead;
	}

	public void setDead(final Boolean dead)
	{
		this.dead = dead;
	}

	public Date getDeathDate()
	{
		return this.deathDate;
	}

	public void setDeathDate(final Date deathDate)
	{
		this.deathDate = deathDate;
	}

	public String getGender()
	{
		return this.gender;
	}

	public void setGender(final String gender)
	{
		this.gender = gender;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public String getVoidReason()
	{
		return this.voidReason;
	}

	public void setVoidReason(final String voidReason)
	{
		this.voidReason = voidReason;
	}

	public Boolean getVoided()
	{
		return this.voided;
	}

	public void setVoided(final Boolean voided)
	{
		this.voided = voided;
	}

	public Long getVoidedBy()
	{
		return this.voidedBy;
	}

	public void setVoidedBy(final Long voidedBy)
	{
		this.voidedBy = voidedBy;
	}

	public List<Observation> getObservations()
	{
		return this.observations;
	}

	public void setObservations(final List<Observation> obs)
	{
		this.observations = obs;
	}

	public Observation addObservation(final Observation observation)
	{
		getObservations().add(observation);
		observation.setPerson(this);

		return observation;
	}

	public Observation removeObservation(final Observation observation)
	{
		getObservations().remove(observation);
		observation.setPerson(null);

		return observation;
	}

	public Patient getPatient()
	{
		return this.patient;
	}

	public List<PersonAttribute> getPersonAttributes()
	{
		return this.personAttributes;
	}

	public void setPatient(final Patient patient)
	{
		this.patient = patient;
	}

	public void setPersonAttributes(final List<PersonAttribute> personAttributes)
	{
		this.personAttributes = personAttributes;
	}

	public PersonAttribute addPersonAttribute(final PersonAttribute personAttribute)
	{
		getPersonAttributes().add(personAttribute);
		personAttribute.setPerson(this);

		return personAttribute;
	}

	public PersonAttribute removePersonAttribute(final PersonAttribute personAttribute)
	{
		getPersonAttributes().remove(personAttribute);
		personAttribute.setPerson(null);

		return personAttribute;
	}

}