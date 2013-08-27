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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

import edu.utah.further.security.api.domain.Role;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserRole;

/**
 * A many to many relationship between a user and role.
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
 * @version May 2, 2012
 */
@Entity
@Table(name = "APP_USER_ROLE")
@Immutable
public class UserRoleEntity implements UserRole
{
	/**
	 * Generated Serial UID
	 */
	private static final long serialVersionUID = 3596358660298005185L;

	@Id
	@GeneratedValue
	@Column(name = "app_user_role_id")
	private Long id;

	@ManyToOne(targetEntity = UserEntity.class)
	@JoinColumn(name = "app_user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = RoleEntity.class)
	@JoinColumn(name = "app_role_id")
	private Role role;

	@Column(name = "created_dts")
	private Date createdDate;

	@Column(name = "expire_dt")
	private Date expiredDate;

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
	 * Return the user property.
	 * 
	 * @return the user
	 */
	@Override
	public User getUser()
	{
		return user;
	}

	/**
	 * Set a new value for the user property.
	 * 
	 * @param user
	 *            the user to set
	 */
	@Override
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * Return the role property.
	 * 
	 * @return the role
	 */
	@Override
	public Role getRole()
	{
		return role;
	}

	/**
	 * Set a new value for the role property.
	 * 
	 * @param role
	 *            the role to set
	 */
	@Override
	public void setRole(final Role role)
	{
		this.role = role;
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
	 * Return the expiredDate property.
	 * 
	 * @return the expiredDate
	 */
	@Override
	public Date getExpiredDate()
	{
		return expiredDate;
	}

	/**
	 * Set a new value for the expiredDate property.
	 * 
	 * @param expiredDate
	 *            the expiredDate to set
	 */
	@Override
	public void setExpiredDate(final Date expiredDate)
	{
		this.expiredDate = expiredDate;
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

		final UserRoleEntity that = (UserRoleEntity) object;
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
