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

import edu.utah.further.security.api.domain.Privilege;
import edu.utah.further.security.api.domain.Role;
import edu.utah.further.security.api.domain.RolePrivilege;
import edu.utah.further.security.api.domain.User;

/**
 * A many to many relationship between a role and privilege.
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
@Table(name = "APP_ROLE_PRIV")
@Immutable
public final class RolePrivilegeEntity
{
	@Id
	@GeneratedValue
	@Column(name = "app_role_priv_id")
	private Long id;

	@ManyToOne(targetEntity = RoleEntity.class)
	@JoinColumn(name = "app_role_id")
	private Role role;

	@ManyToOne(targetEntity = PrivilegeEntity.class)
	@JoinColumn(name = "app_priv_id")
	private Privilege privilege;

	@Column(name = "obj_type_cd")
	private String grantedObjectType;

	@Column(name = "obj")
	private String grantedObject;

	@JoinColumn(name = "granted_by_user_id")
	@ManyToOne(targetEntity = UserEntity.class)
	private User grantedBy;

	@Column(name = "created_dts")
	private Date createdDate;

	@ManyToOne(targetEntity = RolePrivilegeEntity.class)
	@JoinColumn(name = "parent_app_role_priv_id")
	private RolePrivilege parent;

	/**
	 * Return the id property.
	 * 
	 * @return the id
	 */
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
	 * Return the role property.
	 * 
	 * @return the role
	 */
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
	public void setRole(final Role role)
	{
		this.role = role;
	}

	/**
	 * Return the privilege property.
	 * 
	 * @return the privilege
	 */
	public Privilege getPrivilege()
	{
		return privilege;
	}

	/**
	 * Set a new value for the privilege property.
	 * 
	 * @param privilege
	 *            the privilege to set
	 */
	public void setPrivilege(final Privilege privilege)
	{
		this.privilege = privilege;
	}

	/**
	 * Return the grantedObjectType property.
	 * 
	 * @return the grantedObjectType
	 */
	public String getGrantedObjectType()
	{
		return grantedObjectType;
	}

	/**
	 * Set a new value for the grantedObjectType property.
	 * 
	 * @param grantedObjectType
	 *            the grantedObjectType to set
	 */
	public void setGrantedObjectType(final String grantedObjectType)
	{
		this.grantedObjectType = grantedObjectType;
	}

	/**
	 * Return the grantedObject property.
	 * 
	 * @return the grantedObject
	 */
	public String getGrantedObject()
	{
		return grantedObject;
	}

	/**
	 * Set a new value for the grantedObject property.
	 * 
	 * @param grantedObject
	 *            the grantedObject to set
	 */
	public void setGrantedObject(final String grantedObject)
	{
		this.grantedObject = grantedObject;
	}

	/**
	 * Return the grantedBy property.
	 * 
	 * @return the grantedBy
	 */
	public User getGrantedBy()
	{
		return grantedBy;
	}

	/**
	 * Set a new value for the grantedBy property.
	 * 
	 * @param grantedBy
	 *            the grantedBy to set
	 */
	public void setGrantedBy(final User grantedBy)
	{
		this.grantedBy = grantedBy;
	}

	/**
	 * Return the createdDate property.
	 * 
	 * @return the createdDate
	 */
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
	public void setCreatedDate(final Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * Return the parent property.
	 * 
	 * @return the parent
	 */
	public RolePrivilege getParent()
	{
		return parent;
	}

	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(final RolePrivilege parent)
	{
		this.parent = parent;
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

		final RolePrivilegeEntity that = (RolePrivilegeEntity) object;
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
