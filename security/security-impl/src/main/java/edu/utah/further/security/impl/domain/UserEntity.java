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
package edu.utah.further.security.impl.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserProperty;
import edu.utah.further.security.api.domain.UserRole;

/**
 * A database backed user implementation
 * 
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
 * @version Apr 30, 2012
 */
@Entity
@Table(name = "APP_USER")
@Immutable
public class UserEntity implements User
{
	/**
	 * Generated Serial UID
	 */
	private static final long serialVersionUID = -4020211968036020871L;

	@Id
	@GeneratedValue
	@Column(name = "app_user_id")
	private Long id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "email")
	private String email;

	@Column(name = "create_dts")
	private Date createdDate;

	@JoinColumn(name = "created_by_user_id")
	@ManyToOne(targetEntity = UserEntity.class)
	private User createdBy;

	@Column(name = "expire_dt")
	private Date expireDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", targetEntity = UserRoleEntity.class)
	private Collection<UserRole> roles;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", targetEntity = UserPropertyEntity.class)
	private Collection<UserProperty> properties;

	/**
	 * Return the id property.
	 * 
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Return the firstname property.
	 * 
	 * @return the firstname
	 */
	@Override
	public String getFirstname()
	{
		return firstname;
	}

	/**
	 * Set a new value for the firstname property.
	 * 
	 * @param firstname
	 *            the firstname to set
	 */
	@Override
	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * Return the lastname property.
	 * 
	 * @return the lastname
	 */
	@Override
	public String getLastname()
	{
		return lastname;
	}

	/**
	 * Set a new value for the lastname property.
	 * 
	 * @param lastname
	 *            the lastname to set
	 */
	@Override
	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
	}

	/**
	 * Return the email property.
	 * 
	 * @return the email
	 */
	@Override
	public String getEmail()
	{
		return email;
	}

	/**
	 * Set a new value for the email property.
	 * 
	 * @param email
	 *            the email to set
	 */
	@Override
	public void setEmail(final String email)
	{
		this.email = email;
	}

	/**
	 * Return the createdDate property.
	 * 
	 * @return the createdDate
	 */
	@Override
	public Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * Set a new value for the createdDate property.
	 * 
	 * @param createdDate
	 *            the createdDate to set
	 */
	@Override
	public void setCreatedDate(final Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * Return the createdBy property.
	 * 
	 * @return the createdBy
	 */
	@Override
	public User getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Set a new value for the createdBy property.
	 * 
	 * @param createdBy
	 *            the createdBy to set
	 */
	@Override
	public void setCreatedBy(final User createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Return the expireDate property.
	 * 
	 * @return the expireDate
	 */
	@Override
	public Date getExpireDate()
	{
		return expireDate;
	}

	/**
	 * Set a new value for the expireDate property.
	 * 
	 * @param expireDate
	 *            the expireDate to set
	 */
	@Override
	public void setExpireDate(final Date expireDate)
	{
		this.expireDate = expireDate;
	}

	/**
	 * Return the roles property.
	 * 
	 * @return the roles
	 */
	@Override
	public Collection<UserRole> getRoles()
	{
		return roles;
	}

	/**
	 * Set a new value for the roles property.
	 * 
	 * @param roles
	 *            the roles to set
	 */
	@Override
	public void setRoles(final Collection<UserRole> roles)
	{
		this.roles = roles;
	}

	/**
	 * Return the properties property.
	 * 
	 * @return the properties
	 */
	@Override
	public Collection<UserProperty> getProperties()
	{
		return properties;
	}

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	@Override
	public void setProperties(final Collection<UserProperty> properties)
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final UserEntity that = (UserEntity) object;
		return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

}
