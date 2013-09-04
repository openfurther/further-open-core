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
package edu.utah.further.ds.openmrs.model.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the person_attribute_type database table.
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
@Table(name = "person_attribute_type")
@XmlRootElement(name = "PersonAttributeType")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonAttributeType implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "person_attribute_type_id")
	private int personAttributeTypeId;

	@Column(name = "changed_by")
	private int changedBy;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_changed")
	private Date dateChanged;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_retired")
	private Date dateRetired;

	@Lob
	private String description;

	@Column(name = "edit_privilege")
	private String editPrivilege;

	@Column(name = "foreign_key")
	private int foreignKey;

	private String format;

	private String name;

	@Column(name = "retire_reason")
	private String retireReason;

	private byte retired;

	@Column(name = "retired_by")
	private int retiredBy;

	private byte searchable;

	@Column(name = "sort_weight")
	private double sortWeight;

	private String uuid;

	// bi-directional many-to-one association to PersonAttribute
	@OneToMany(mappedBy = "personAttributeType")
	@XmlTransient
	private List<PersonAttribute> personAttributes;

	public PersonAttributeType()
	{
	}

	public int getPersonAttributeTypeId()
	{
		return this.personAttributeTypeId;
	}

	public void setPersonAttributeTypeId(final int personAttributeTypeId)
	{
		this.personAttributeTypeId = personAttributeTypeId;
	}

	public int getChangedBy()
	{
		return this.changedBy;
	}

	public void setChangedBy(final int changedBy)
	{
		this.changedBy = changedBy;
	}

	public int getCreator()
	{
		return this.creator;
	}

	public void setCreator(final int creator)
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

	public Date getDateRetired()
	{
		return this.dateRetired;
	}

	public void setDateRetired(final Date dateRetired)
	{
		this.dateRetired = dateRetired;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getEditPrivilege()
	{
		return this.editPrivilege;
	}

	public void setEditPrivilege(final String editPrivilege)
	{
		this.editPrivilege = editPrivilege;
	}

	public int getForeignKey()
	{
		return this.foreignKey;
	}

	public void setForeignKey(final int foreignKey)
	{
		this.foreignKey = foreignKey;
	}

	public String getFormat()
	{
		return this.format;
	}

	public void setFormat(final String format)
	{
		this.format = format;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getRetireReason()
	{
		return this.retireReason;
	}

	public void setRetireReason(final String retireReason)
	{
		this.retireReason = retireReason;
	}

	public byte getRetired()
	{
		return this.retired;
	}

	public void setRetired(final byte retired)
	{
		this.retired = retired;
	}

	public int getRetiredBy()
	{
		return this.retiredBy;
	}

	public void setRetiredBy(final int retiredBy)
	{
		this.retiredBy = retiredBy;
	}

	public byte getSearchable()
	{
		return this.searchable;
	}

	public void setSearchable(final byte searchable)
	{
		this.searchable = searchable;
	}

	public double getSortWeight()
	{
		return this.sortWeight;
	}

	public void setSortWeight(final double sortWeight)
	{
		this.sortWeight = sortWeight;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public List<PersonAttribute> getPersonAttributes()
	{
		return this.personAttributes;
	}

	public void setPersonAttributes(final List<PersonAttribute> personAttributes)
	{
		this.personAttributes = personAttributes;
	}

	public PersonAttribute addPersonAttribute(final PersonAttribute personAttribute)
	{
		getPersonAttributes().add(personAttribute);
		personAttribute.setPersonAttributeType(this);

		return personAttribute;
	}

	public PersonAttribute removePersonAttribute(final PersonAttribute personAttribute)
	{
		getPersonAttributes().remove(personAttribute);
		personAttribute.setPersonAttributeType(null);

		return personAttribute;
	}

}