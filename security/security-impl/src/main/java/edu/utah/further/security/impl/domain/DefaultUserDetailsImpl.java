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
import java.util.Map;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import edu.utah.further.security.api.EnhancedUserDetails;

/**
 * A basic default implementation of user details that holds the user's information
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
 * @version May 3, 2012
 */
public class DefaultUserDetailsImpl implements EnhancedUserDetails, CredentialsContainer
{

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 2277175133091418790L;

	/**
	 * The granted authorities or roles
	 */
	private Collection<GrantedAuthority> authorities;

	/**
	 * The user password
	 */
	private String password;

	/**
	 * The username
	 */
	private String username;

	/**
	 * Whether or not the account is not expired
	 */
	private boolean accountNonExpired;

	/**
	 * Whether or not the account is not locked
	 */
	private boolean accountNonLocked;

	/**
	 * Whether or not the credentials are not expired
	 */
	private boolean credentialsNonExpired;

	/**
	 * Whether or not the account is enabled.
	 */
	private boolean enabled;

	/**
	 * A map of user properties
	 */
	private Map<String, String> properties;

	/**
	 * Return the authorities property.
	 * 
	 * @return the authorities
	 */
	@Override
	public Collection<GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	/**
	 * Set a new value for the authorities property.
	 * 
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(final Collection<GrantedAuthority> authorities)
	{
		this.authorities = authorities;
	}

	/**
	 * Return the password property.
	 * 
	 * @return the password
	 */
	@Override
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set a new value for the password property.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * Return the username property.
	 * 
	 * @return the username
	 */
	@Override
	public String getUsername()
	{
		return username;
	}

	/**
	 * Set a new value for the username property.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username)
	{
		this.username = username;
	}

	/**
	 * Return the accountNonExpired property.
	 * 
	 * @return the accountNonExpired
	 */
	@Override
	public boolean isAccountNonExpired()
	{
		return accountNonExpired;
	}

	/**
	 * Set a new value for the accountNonExpired property.
	 * 
	 * @param accountNonExpired
	 *            the accountNonExpired to set
	 */
	public void setAccountNonExpired(final boolean accountNonExpired)
	{
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * Return the accountNonLocked property.
	 * 
	 * @return the accountNonLocked
	 */
	@Override
	public boolean isAccountNonLocked()
	{
		return accountNonLocked;
	}

	/**
	 * Set a new value for the accountNonLocked property.
	 * 
	 * @param accountNonLocked
	 *            the accountNonLocked to set
	 */
	public void setAccountNonLocked(final boolean accountNonLocked)
	{
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * Return the credentialsNonExpired property.
	 * 
	 * @return the credentialsNonExpired
	 */
	@Override
	public boolean isCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}

	/**
	 * Set a new value for the credentialsNonExpired property.
	 * 
	 * @param credentialsNonExpired
	 *            the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(final boolean credentialsNonExpired)
	{
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * Return the enabled property.
	 * 
	 * @return the enabled
	 */
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Set a new value for the enabled property.
	 * 
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.CredentialsContainer#eraseCredentials()
	 */
	@Override
	public void eraseCredentials()
	{
		this.password = null;
		this.properties = null;
	}

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Map<String, String> properties)
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.security.api.EnhancedUserDetails#getProperties()
	 */
	@Override
	public Map<String, String> getProperties()
	{
		return this.properties;
	}

}
