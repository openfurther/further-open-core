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
 * The persistent class for the LOCATION database table.
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
@Table(name = "LOCATION")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOCATION_ID", unique = true, nullable = false)
	private Long locationId;

	@Column(name = "ADDRESS_1", length = 50)
	private String address1;

	@Column(name = "ADDRESS_2", length = 50)
	private String address2;

	@Column(length = 50)
	private String city;

	@Column(length = 20)
	private String county;

	@Column(name = "LOCATION_SOURCE_VALUE", length = 50)
	private String locationSourceValue;

	@Column(name = "\"STATE\"", length = 2)
	private String state;

	@Column(length = 9)
	private String zip;

	/**
	 * Default constructor
	 */
	public Location()
	{
	}

	// bi-directional many-to-one association to OrganizationEntity
	@OneToMany(mappedBy = "location")
	@XmlTransient
	private List<Organization> organizations;

	// bi-directional many-to-one association to PersonEntity
	@OneToMany(mappedBy = "location")
	@XmlTransient
	private List<Person> persons;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.locationId;
	}

	protected void setId(final Long locationId)
	{
		this.locationId = locationId;
	}

	public String getAddress1()
	{
		return this.address1;
	}

	public void setAddress1(final String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2()
	{
		return this.address2;
	}

	public void setAddress2(final String address2)
	{
		this.address2 = address2;
	}

	public String getCity()
	{
		return this.city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getCounty()
	{
		return this.county;
	}

	public void setCounty(final String county)
	{
		this.county = county;
	}

	public String getLocationSourceValue()
	{
		return this.locationSourceValue;
	}

	public void setLocationSourceValue(final String locationSourceValue)
	{
		this.locationSourceValue = locationSourceValue;
	}

	public String getState()
	{
		return this.state;
	}

	public void setState(final String state)
	{
		this.state = state;
	}

	public String getZip()
	{
		return this.zip;
	}

	public void setZip(final String zip)
	{
		this.zip = zip;
	}

	public List<Organization> getOrganizations()
	{
		return this.organizations;
	}

	public void setOrganizations(final List<Organization> organizations)
	{
		this.organizations = organizations;
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