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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.xml.jaxb.adapter.BooleanIntegerAdapter;

/**
 * The persistent class and data transfer object for the obs database table.
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
@Table(name = "obs")
@XmlRootElement(name = "Observation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "obs_id")
	private Integer obsId;

	@Column(name = "accession_number")
	private String accessionNumber;

	private String comments;

	@Column(name = "concept_id")
	private Long conceptId;

	private Long creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided")
	private Date dateVoided;

	@Column(name = "location_id")
	private Long locationId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "obs_datetime")
	private Date obsDatetime;

	private String uuid;

	@Column(name = "value_boolean")
	@XmlJavaTypeAdapter(BooleanIntegerAdapter.class)
	private Boolean valueBoolean;

	@Column(name = "value_coded")
	private Long valueCoded;

	@Column(name = "value_coded_name_id")
	private Long valueCodedNameId;

	@Column(name = "value_complex")
	private String valueComplex;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "value_datetime")
	private Date valueDatetime;

	@Column(name = "value_drug")
	private Long valueDrug;

	@Column(name = "value_group_id")
	private Long valueGroupId;

	@Column(name = "value_modifier")
	private String valueModifier;

	@Column(name = "value_numeric")
	private Double valueNumeric;

	@Lob
	@Column(name = "value_text")
	private String valueText;

	@Column(name = "void_reason")
	private String voidReason;

	@XmlJavaTypeAdapter(BooleanIntegerAdapter.class)
	private Boolean voided;

	@Column(name = "voided_by")
	private Long voidedBy;

	// bi-directional many-to-one association to Encounter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	@XmlTransient
	private Encounter encounter;

	// bi-directional many-to-one association to Order
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@XmlTransient
	private Order order;

	// bi-directional many-to-one association to Person
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	@XmlTransient
	private Person person;

	public Observation()
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
		return this.obsId;
	}

	public void setId(final Integer obsId)
	{
		this.obsId = obsId;
	}

	public String getAccessionNumber()
	{
		return this.accessionNumber;
	}

	public void setAccessionNumber(final String accessionNumber)
	{
		this.accessionNumber = accessionNumber;
	}

	public String getComments()
	{
		return this.comments;
	}

	public void setComments(final String comments)
	{
		this.comments = comments;
	}

	public Long getConceptId()
	{
		return this.conceptId;
	}

	public void setConceptId(final Long conceptId)
	{
		this.conceptId = conceptId;
	}

	public Long getCreator()
	{
		return this.creator;
	}

	public void setCreator(final Long creator)
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

	public Long getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(final Long locationId)
	{
		this.locationId = locationId;
	}

	public Date getObsDatetime()
	{
		return this.obsDatetime;
	}

	public void setObsDatetime(final Date obsDatetime)
	{
		this.obsDatetime = obsDatetime;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public Boolean getValueBoolean()
	{
		return this.valueBoolean;
	}

	public void setValueBoolean(final Boolean valueBoolean)
	{
		this.valueBoolean = valueBoolean;
	}

	public Long getValueCoded()
	{
		return this.valueCoded;
	}

	public void setValueCoded(final Long valueCoded)
	{
		this.valueCoded = valueCoded;
	}

	public Long getValueCodedNameId()
	{
		return this.valueCodedNameId;
	}

	public void setValueCodedNameId(final Long valueCodedNameId)
	{
		this.valueCodedNameId = valueCodedNameId;
	}

	public String getValueComplex()
	{
		return this.valueComplex;
	}

	public void setValueComplex(final String valueComplex)
	{
		this.valueComplex = valueComplex;
	}

	public Date getValueDatetime()
	{
		return this.valueDatetime;
	}

	public void setValueDatetime(final Date valueDatetime)
	{
		this.valueDatetime = valueDatetime;
	}

	public Long getValueDrug()
	{
		return this.valueDrug;
	}

	public void setValueDrug(final Long valueDrug)
	{
		this.valueDrug = valueDrug;
	}

	public Long getValueGroupId()
	{
		return this.valueGroupId;
	}

	public void setValueGroupId(final Long valueGroupId)
	{
		this.valueGroupId = valueGroupId;
	}

	public String getValueModifier()
	{
		return this.valueModifier;
	}

	public void setValueModifier(final String valueModifier)
	{
		this.valueModifier = valueModifier;
	}

	public Double getValueNumeric()
	{
		return this.valueNumeric;
	}

	public void setValueNumeric(final Double valueNumeric)
	{
		this.valueNumeric = valueNumeric;
	}

	public String getValueText()
	{
		return this.valueText;
	}

	public void setValueText(final String valueText)
	{
		this.valueText = valueText;
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

	public Encounter getEncounter()
	{
		return this.encounter;
	}

	public void setEncounter(final Encounter encounter)
	{
		this.encounter = encounter;
	}

	public Order getOrder()
	{
		return this.order;
	}

	public void setOrder(final Order order)
	{
		this.order = order;
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