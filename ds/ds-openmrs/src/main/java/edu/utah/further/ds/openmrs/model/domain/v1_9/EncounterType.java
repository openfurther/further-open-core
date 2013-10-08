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
package edu.utah.further.ds.openmrs.model.domain.v1_9;

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

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the encounter_type database table.
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
@Table(name = "encounter_type")
@XmlRootElement(name = "EncounterType")
@XmlAccessorType(XmlAccessType.FIELD)
public class EncounterType implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "encounter_type_id")
	private Integer encounterTypeId;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_retired")
	private Date dateRetired;

	@Lob
	private String description;

	private String name;

	@Column(name = "retire_reason")
	private String retireReason;

	private byte retired;

	@Column(name = "retired_by")
	private int retiredBy;

	private String uuid;

	// bi-directional many-to-one association to Encounter
	@OneToMany(mappedBy = "encounterType")
	@XmlTransient
	private List<Encounter> encounters;

	public EncounterType()
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
		return this.encounterTypeId;
	}

	public void setId(final Integer encounterTypeId)
	{
		this.encounterTypeId = encounterTypeId;
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

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
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
		encounter.setEncounterTypeBean(this);

		return encounter;
	}

	public Encounter removeEncounter(final Encounter encounter)
	{
		getEncounters().remove(encounter);
		encounter.setEncounterTypeBean(null);

		return encounter;
	}

}