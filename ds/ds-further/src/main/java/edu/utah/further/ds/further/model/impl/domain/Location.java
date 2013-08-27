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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Persistent entity implementation of {@link Location}
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
 * @version Aug 4, 2010
 */
@Entity
@Implementation
@Table(name = "FPERSON_LCTN")
@XmlRootElement(name = "Location")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Location implements PersistentEntity<LocationId>
{
	// ========================= CONSTANTS ===================================
	/**
	 * Generated SUID
	 */
	private static final long serialVersionUID = -4566890844457489875L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private LocationId id;

	@Column(name = "fperson_id")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "person_lctn_type_cd")
	private String personLocationType;

	@Column(name = "lctn_type_cd")
	private String locationType;

	@Column(name = "lctn_nmspc_id")
	private Long locationNamespaceId;

	@Column(name = "lctn_cid")
	private String location;

	@Column(name = "lctn_txt")
	private String locationText;

	@Column(name = "start_dt")
	private Date startDateTime;

	@Column(name = "end_dt")
	private Date endDateTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public LocationId getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(LocationId id)
	{
		this.id = id;
	}

	/**
	 * Return the personId property.
	 * 
	 * @return the personId
	 */
	public Long getPersonId()
	{
		return personId;
	}

	/**
	 * Set a new value for the personId property.
	 * 
	 * @param personId
	 *            the personId to set
	 */
	public void setPersonId(Long personId)
	{
		this.personId = personId;
	}

	/**
	 * Return the personCompositeId property.
	 * 
	 * @return the personCompositeId
	 */
	public String getPersonCompositeId()
	{
		return personCompositeId;
	}

	/**
	 * Set a new value for the personCompositeId property.
	 * 
	 * @param personCompositeId
	 *            the personCompositeId to set
	 */
	public void setPersonCompositeId(String personCompositeId)
	{
		this.personCompositeId = personCompositeId;
	}

	/**
	 * Return the personLocationType property.
	 * 
	 * @return the personLocationType
	 */
	public String getPersonLocationType()
	{
		return personLocationType;
	}

	/**
	 * Set a new value for the personLocationType property.
	 * 
	 * @param personLocationType
	 *            the personLocationType to set
	 */
	public void setPersonLocationType(String personLocationType)
	{
		this.personLocationType = personLocationType;
	}

	/**
	 * Return the locationType property.
	 * 
	 * @return the locationType
	 */
	public String getLocationType()
	{
		return locationType;
	}

	/**
	 * Set a new value for the locationType property.
	 * 
	 * @param locationType
	 *            the locationType to set
	 */
	public void setLocationType(String locationType)
	{
		this.locationType = locationType;
	}

	/**
	 * Return the locationNamespaceId property.
	 * 
	 * @return the locationNamespaceId
	 */
	public Long getLocationNamespaceId()
	{
		return locationNamespaceId;
	}

	/**
	 * Set a new value for the locationNamespaceId property.
	 * 
	 * @param locationNamespaceId
	 *            the locationNamespaceId to set
	 */
	public void setLocationNamespaceId(Long locationNamespaceId)
	{
		this.locationNamespaceId = locationNamespaceId;
	}

	/**
	 * Return the location property.
	 * 
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Set a new value for the location property.
	 * 
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
	 * Return the locationText property.
	 * 
	 * @return the locationText
	 */
	public String getLocationText()
	{
		return locationText;
	}

	/**
	 * Set a new value for the locationText property.
	 * 
	 * @param locationText
	 *            the locationText to set
	 */
	public void setLocationText(String locationText)
	{
		this.locationText = locationText;
	}

	/**
	 * Return the startDateTime property.
	 * 
	 * @return the startDateTime
	 */
	public Date getStartDateTime()
	{
		return startDateTime;
	}

	/**
	 * Set a new value for the startDateTime property.
	 * 
	 * @param startDateTime
	 *            the startDateTime to set
	 */
	public void setStartDateTime(Date startDateTime)
	{
		this.startDateTime = startDateTime;
	}

	/**
	 * Return the endDateTime property.
	 * 
	 * @return the endDateTime
	 */
	public Date getEndDateTime()
	{
		return endDateTime;
	}

	/**
	 * Set a new value for the endDateTime property.
	 * 
	 * @param endDateTime
	 *            the endDateTime to set
	 */
	public void setEndDateTime(Date endDateTime)
	{
		this.endDateTime = endDateTime;
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.model.api.domain.Location#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final Location that = (Location) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.model.api.domain.Location#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.model.api.domain.Location#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

}
