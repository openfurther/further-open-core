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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version May 9, 2012
 */
@Entity
@Implementation
@Table(name = "FPROVIDER_LCTN")
@XmlRootElement(name = "ProviderLocation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderLocation implements
		PersistentEntity<ProviderLocationId>
{

	// ========================= CONSTANTS =================================

	@Transient
	private static final long serialVersionUID = 5920573688090895157L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private ProviderLocationId id;

	@Column(name = "FPERSON_ID")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "PERSON_LCTN_TYPE_CD")
	private String personLocationTypeCd;

	@Column(name = "LCTN_TYPE_CD")
	private String locationTypeCd;

	@Column(name = "LCTN_NMSPC_ID")
	private Long locationNamespaceId;

	@Column(name = "LCTN_CID")
	private String locationCid;

	@Column(name = "LCTN_TXT")
	private String locationTxt;

	@Column(name = "START_DT")
	private Date startDate;

	@Column(name = "END_DT")
	private Date endDate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public ProviderLocationId getId()
	{
		return id;
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
	 * Return the personLocationTypeCd property.
	 * 
	 * @return the personLocationTypeCd
	 */
	public String getPersonLocationTypeCd()
	{
		return personLocationTypeCd;
	}

	/**
	 * Set a new value for the personLocationTypeCd property.
	 * 
	 * @param personLocationTypeCd
	 *            the personLocationTypeCd to set
	 */
	public void setPersonLocationTypeCd(String personLocationTypeCd)
	{
		this.personLocationTypeCd = personLocationTypeCd;
	}

	/**
	 * Return the locationTypeCd property.
	 * 
	 * @return the locationTypeCd
	 */
	public String getLocationTypeCd()
	{
		return locationTypeCd;
	}

	/**
	 * Set a new value for the locationTypeCd property.
	 * 
	 * @param locationTypeCd
	 *            the locationTypeCd to set
	 */
	public void setLocationTypeCd(String locationTypeCd)
	{
		this.locationTypeCd = locationTypeCd;
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
	 * Return the locationCid property.
	 * 
	 * @return the locationCid
	 */
	public String getLocationCid()
	{
		return locationCid;
	}

	/**
	 * Set a new value for the locationCid property.
	 * 
	 * @param locationCid
	 *            the locationCid to set
	 */
	public void setLocationCid(String locationCid)
	{
		this.locationCid = locationCid;
	}

	/**
	 * Return the locationTxt property.
	 * 
	 * @return the locationTxt
	 */
	public String getLocationTxt()
	{
		return locationTxt;
	}

	/**
	 * Set a new value for the locationTxt property.
	 * 
	 * @param locationTxt
	 *            the locationTxt to set
	 */
	public void setLocationTxt(String locationTxt)
	{
		this.locationTxt = locationTxt;
	}

	/**
	 * Return the startDate property.
	 * 
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Set a new value for the startDate property.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Return the endDate property.
	 * 
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * Set a new value for the endDate property.
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
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

		final ProviderLocation that = (ProviderLocation) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

}
