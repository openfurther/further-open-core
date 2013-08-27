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

import edu.utah.further.security.api.domain.Property;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserProperty;

/**
 * A many to many relationship between a user and property.
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
@Table(name = "APP_USER_PROP")
@Immutable
public class UserPropertyEntity implements UserProperty
{
	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = -8346629928787932968L;

	@Id
	@GeneratedValue
	@Column(name = "app_user_prop_id")
	private Long id;

	@ManyToOne(targetEntity = UserEntity.class)
	@JoinColumn(name = "app_user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = PropertyEntity.class)
	@JoinColumn(name = "app_prop_id")
	private Property property;

	@Column(name = "app_prop_val")
	private String propertyValue;

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
	 * Return the property property.
	 * 
	 * @return the property
	 */
	@Override
	public Property getProperty()
	{
		return property;
	}

	/**
	 * Set a new value for the property property.
	 * 
	 * @param property
	 *            the property to set
	 */
	@Override
	public void setProperty(final Property property)
	{
		this.property = property;
	}

	/**
	 * Return the propertyValue property.
	 * 
	 * @return the propertyValue
	 */
	@Override
	public String getPropertyValue()
	{
		return propertyValue;
	}

	/**
	 * Set a new value for the propertyValue property.
	 * 
	 * @param propertyValue
	 *            the propertyValue to set
	 */
	@Override
	public void setPropertyValue(final String propertyValue)
	{
		this.propertyValue = propertyValue;
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

		final UserPropertyEntity that = (UserPropertyEntity) object;
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
