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

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Persistent entity implementation of {@link Provider}
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
@Table(name = "FPROVIDER")
@XmlRootElement(name = "Provider")
@XmlAccessorType(XmlAccessType.FIELD)
public class Provider implements PersistentEntity<ProviderId>
{
	// ========================= CONSTANTS =================================

	@Transient
	private static final long serialVersionUID = 5275573688090895157L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private ProviderId id;

	@Column(name = "fperson_id")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "provider_name")
	private String providerName;

	@Column(name = "specialty_nmspc_id")
	private Long specialtyNamespaceId;

	@Column(name = "specialty_cid")
	private String specialty;

	@Column(name = "specialty_mod_nmspc_id")
	private Long specialtyModNamespaceId;

	@Column(name = "specialty_mod_cid")
	private String specialtyMod;

	@Column(name = "start_dts")
	private Date stateDate;

	@Column(name = "end_dts")
	private Date endDate;

	@OneToMany(targetEntity = Order.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fprovider_id", referencedColumnName = "fprovider_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Order> orders;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public ProviderId getId()
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
	 * Return the providerName property.
	 * 
	 * @return the providerName
	 */
	public String getProviderName()
	{
		return providerName;
	}

	/**
	 * Set a new value for the providerName property.
	 * 
	 * @param providerName
	 *            the providerName to set
	 */
	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	/**
	 * Return the specialtyNamespaceId property.
	 * 
	 * @return the specialtyNamespaceId
	 */
	public Long getSpecialtyNamespaceId()
	{
		return specialtyNamespaceId;
	}

	/**
	 * Set a new value for the specialtyNamespaceId property.
	 * 
	 * @param specialtyNamespaceId
	 *            the specialtyNamespaceId to set
	 */
	public void setSpecialtyNamespaceId(Long specialtyNamespaceId)
	{
		this.specialtyNamespaceId = specialtyNamespaceId;
	}

	/**
	 * Return the specialty property.
	 * 
	 * @return the specialty
	 */
	public String getSpecialty()
	{
		return specialty;
	}

	/**
	 * Set a new value for the specialty property.
	 * 
	 * @param specialty
	 *            the specialty to set
	 */
	public void setSpecialty(String specialty)
	{
		this.specialty = specialty;
	}

	/**
	 * Return the specialtyModNamespaceId property.
	 * 
	 * @return the specialtyModNamespaceId
	 */
	public Long getSpecialtyModNamespaceId()
	{
		return specialtyModNamespaceId;
	}

	/**
	 * Set a new value for the specialtyModNamespaceId property.
	 * 
	 * @param specialtyModNamespaceId
	 *            the specialtyModNamespaceId to set
	 */
	public void setSpecialtyModNamespaceId(Long specialtyModNamespaceId)
	{
		this.specialtyModNamespaceId = specialtyModNamespaceId;
	}

	/**
	 * Return the specialtyMod property.
	 * 
	 * @return the specialtyMod
	 */
	public String getSpecialtyMod()
	{
		return specialtyMod;
	}

	/**
	 * Set a new value for the specialtyMod property.
	 * 
	 * @param specialtyMod
	 *            the specialtyMod to set
	 */
	public void setSpecialtyMod(String specialtyMod)
	{
		this.specialtyMod = specialtyMod;
	}

	/**
	 * Return the stateDate property.
	 * 
	 * @return the stateDate
	 */
	public Date getStateDate()
	{
		return stateDate;
	}

	/**
	 * Set a new value for the stateDate property.
	 * 
	 * @param stateDate
	 *            the stateDate to set
	 */
	public void setStateDate(Date stateDate)
	{
		this.stateDate = stateDate;
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
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(ProviderId id)
	{
		this.id = id;
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

		final Provider that = (Provider) obj;
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
		return ReflectionToStringBuilder.toStringExclude(this, new String[]
		{ "orders", "person" });
	}

}
