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
package edu.utah.further.ds.omop.model.v4.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the PROVIDER database table.
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
 * @version Apr 17, 2013
 */
@Entity
@Table(name = "PROVIDER")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Provider implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROVIDER_ID", unique = true, nullable = false)
	private Long providerId;

	@Column(name = "CARE_SITE_ID")
	private BigDecimal careSiteId;

	@Column(length = 20)
	private String dea;

	@Column(length = 20)
	private String npi;

	@Column(name = "PROVIDER_SOURCE_VALUE", nullable = false, length = 50)
	private String providerSourceValue;

	@Column(name = "SPECIALTY_CONCEPT_ID")
	private BigDecimal specialtyConceptId;

	@Column(name = "SPECIALTY_SOURCE_VALUE", length = 50)
	private String specialtySourceValue;

	// bi-directional many-to-one association to PersonEntity
	@OneToMany(mappedBy = "provider")
	@XmlTransient
	private List<Person> persons;

	/**
	 * Default constructor
	 */
	public Provider()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.providerId;
	}

	protected void setId(final Long providerId)
	{
		this.providerId = providerId;
	}

	public BigDecimal getCareSiteId()
	{
		return this.careSiteId;
	}

	public void setCareSiteId(final BigDecimal careSiteId)
	{
		this.careSiteId = careSiteId;
	}

	public String getDea()
	{
		return this.dea;
	}

	public void setDea(final String dea)
	{
		this.dea = dea;
	}

	public String getNpi()
	{
		return this.npi;
	}

	public void setNpi(final String npi)
	{
		this.npi = npi;
	}

	public String getProviderSourceValue()
	{
		return this.providerSourceValue;
	}

	public void setProviderSourceValue(final String providerSourceValue)
	{
		this.providerSourceValue = providerSourceValue;
	}

	public BigDecimal getSpecialtyConceptId()
	{
		return this.specialtyConceptId;
	}

	public void setSpecialtyConceptId(final BigDecimal specialtyConceptId)
	{
		this.specialtyConceptId = specialtyConceptId;
	}

	public String getSpecialtySourceValue()
	{
		return this.specialtySourceValue;
	}

	public void setSpecialtySourceValue(final String specialtySourceValue)
	{
		this.specialtySourceValue = specialtySourceValue;
	}

	public List<Person> getPersons()
	{
		return this.persons;
	}

	public void setPersons(final List<Person> persons)
	{
		this.persons = persons;
	}

}