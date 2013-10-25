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

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the patient database table.
 * 
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
@Table(name="patient")
@Entity
@XmlRootElement(name = "Patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "patient_id")
	private Integer patientId;

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

	private Long tribe;

	@Column(name = "void_reason")
	private String voidReason;

	private Boolean voided;

	@Column(name = "voided_by")
	private Long voidedBy;

	// bi-directional many-to-one association to Encounter
	@OneToMany(mappedBy = "patient")
	@XmlTransient
	private List<Encounter> encounters;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "patient")
	@XmlTransient
	private List<Order> orders;

	// bi-directional one-to-one association to Person
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	@XmlTransient
	private Person person;

	public Patient()
	{
	}

	@Override
	public Integer getId()
	{
		return this.patientId;
	}

	public void setId(final Integer patientId)
	{
		this.patientId = patientId;
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

	public Long getTribe()
	{
		return this.tribe;
	}

	public void setTribe(final Long tribe)
	{
		this.tribe = tribe;
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

	public List<Encounter> getEncounters()
	{
		return this.encounters;
	}

	public void setEncounters(final List<Encounter> encounters)
	{
		this.encounters = encounters;
	}

	public Encounter addEncounter(final Encounter encounter)
	{
		getEncounters().add(encounter);
		encounter.setPatient(this);

		return encounter;
	}

	public Encounter removeEncounter(final Encounter encounter)
	{
		getEncounters().remove(encounter);
		encounter.setPatient(null);

		return encounter;
	}

	public List<Order> getOrders()
	{
		return this.orders;
	}

	public void setOrders(final List<Order> orders)
	{
		this.orders = orders;
	}

	public Order addOrder(final Order order)
	{
		getOrders().add(order);
		order.setPatient(this);

		return order;
	}

	public Order removeOrder(final Order order)
	{
		getOrders().remove(order);
		order.setPatient(null);

		return order;
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