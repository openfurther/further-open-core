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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
 * The persistent class and data transfer object for the orders database table.
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
@Entity
@Table(name = "orders")
@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "accession_number")
	private String accessionNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "auto_expire_date")
	private Date autoExpireDate;

	@Column(name = "concept_id")
	private int conceptId;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided")
	private Date dateVoided;

	private byte discontinued;

	@Column(name = "discontinued_by")
	private int discontinuedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "discontinued_date")
	private Date discontinuedDate;

	@Column(name = "discontinued_reason")
	private int discontinuedReason;

	@Column(name = "discontinued_reason_non_coded")
	private String discontinuedReasonNonCoded;

	@Lob
	private String instructions;

	private int orderer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	private Date startDate;

	private String urgency;

	private String uuid;

	@Column(name = "void_reason")
	private String voidReason;

	private byte voided;

	@Column(name = "voided_by")
	private int voidedBy;

	// bi-directional one-to-one association to DrugOrder
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	@XmlTransient
	private DrugOrder drugOrder;

	// bi-directional many-to-one association to Observation
	@OneToMany(mappedBy = "order")
	@XmlTransient
	private List<Observation> observations;

	// bi-directional many-to-one association to Encounter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	@XmlTransient
	private Encounter encounter;

	// bi-directional many-to-one association to Patient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	@XmlTransient
	private Patient patient;

	// bi-directional many-to-one association to OrderType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_type_id")
	@XmlTransient
	private OrderType orderType;

	public Order()
	{
	}

	@Override
	public Integer getId()
	{
		return this.orderId;
	}

	public void setOrderId(final Integer orderId)
	{
		this.orderId = orderId;
	}

	public String getAccessionNumber()
	{
		return this.accessionNumber;
	}

	public void setAccessionNumber(final String accessionNumber)
	{
		this.accessionNumber = accessionNumber;
	}

	public Date getAutoExpireDate()
	{
		return this.autoExpireDate;
	}

	public void setAutoExpireDate(final Date autoExpireDate)
	{
		this.autoExpireDate = autoExpireDate;
	}

	public int getConceptId()
	{
		return this.conceptId;
	}

	public void setConceptId(final int conceptId)
	{
		this.conceptId = conceptId;
	}

	public int getCreator()
	{
		return this.creator;
	}

	public void setCreator(final int creator)
	{
		this.creator = creator;
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

	public byte getDiscontinued()
	{
		return this.discontinued;
	}

	public void setDiscontinued(final byte discontinued)
	{
		this.discontinued = discontinued;
	}

	public int getDiscontinuedBy()
	{
		return this.discontinuedBy;
	}

	public void setDiscontinuedBy(final int discontinuedBy)
	{
		this.discontinuedBy = discontinuedBy;
	}

	public Date getDiscontinuedDate()
	{
		return this.discontinuedDate;
	}

	public void setDiscontinuedDate(final Date discontinuedDate)
	{
		this.discontinuedDate = discontinuedDate;
	}

	public int getDiscontinuedReason()
	{
		return this.discontinuedReason;
	}

	public void setDiscontinuedReason(final int discontinuedReason)
	{
		this.discontinuedReason = discontinuedReason;
	}

	public String getDiscontinuedReasonNonCoded()
	{
		return this.discontinuedReasonNonCoded;
	}

	public void setDiscontinuedReasonNonCoded(final String discontinuedReasonNonCoded)
	{
		this.discontinuedReasonNonCoded = discontinuedReasonNonCoded;
	}

	public String getInstructions()
	{
		return this.instructions;
	}

	public void setInstructions(final String instructions)
	{
		this.instructions = instructions;
	}

	public int getOrderer()
	{
		return this.orderer;
	}

	public void setOrderer(final int orderer)
	{
		this.orderer = orderer;
	}

	public Date getStartDate()
	{
		return this.startDate;
	}

	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	public String getUrgency()
	{
		return this.urgency;
	}

	public void setUrgency(final String urgency)
	{
		this.urgency = urgency;
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

	public byte getVoided()
	{
		return this.voided;
	}

	public void setVoided(final byte voided)
	{
		this.voided = voided;
	}

	public int getVoidedBy()
	{
		return this.voidedBy;
	}

	public void setVoidedBy(final int voidedBy)
	{
		this.voidedBy = voidedBy;
	}

	public DrugOrder getDrugOrder()
	{
		return this.drugOrder;
	}

	public void setDrugOrder(final DrugOrder drugOrder)
	{
		this.drugOrder = drugOrder;
	}

	public List<Observation> getObservations()
	{
		return this.observations;
	}

	public void setObservations(final List<Observation> obs)
	{
		this.observations = obs;
	}

	public Observation addOb(final Observation ob)
	{
		getObservations().add(ob);
		ob.setOrder(this);

		return ob;
	}

	public Observation removeOb(final Observation ob)
	{
		getObservations().remove(ob);
		ob.setOrder(null);

		return ob;
	}

	public Encounter getEncounter()
	{
		return this.encounter;
	}

	public void setEncounter(final Encounter encounter)
	{
		this.encounter = encounter;
	}

	public Patient getPatient()
	{
		return this.patient;
	}

	public void setPatient(final Patient patient)
	{
		this.patient = patient;
	}

	public OrderType getOrderType()
	{
		return this.orderType;
	}

	public void setOrderType(final OrderType orderType)
	{
		this.orderType = orderType;
	}

}