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

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.utah.further.core.xml.jaxb.adapter.BooleanIntegerAdapter;

/**
 * The persistent class for the person_attribute database table.
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
@Table(name = "person_attribute")
@XmlRootElement(name = "PersonAttribute")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonAttribute implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "person_attribute_id")
	private Long personAttributeId;

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

	private String uuid;

	private String value;

	@Column(name = "void_reason")
	private String voidReason;

	@XmlJavaTypeAdapter(BooleanIntegerAdapter.class)
	private Boolean voided;

	@Column(name = "voided_by")
	private Long voidedBy;

	// do not join, just use id
	@Column(name = "person_attribute_type_id")
	private Long personAttributeType;

	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "person_id")
	@XmlTransient
	private Person person;

	public PersonAttribute()
	{
	}

	public Long getPersonAttributeId()
	{
		return this.personAttributeId;
	}

	public void setPersonAttributeId(final Long personAttributeId)
	{
		this.personAttributeId = personAttributeId;
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

	public Long getPersonAttributeType()
	{
		return this.personAttributeType;
	}

	public void setPersonAttributeType(final Long personAttributeType)
	{
		this.personAttributeType = personAttributeType;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(final String value)
	{
		this.value = value;
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

	public Person getPerson()
	{
		return this.person;
	}

	public void setPerson(final Person person)
	{
		this.person = person;
	}

}