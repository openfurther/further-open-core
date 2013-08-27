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
package edu.utah.further.security.impl.ldap;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

import edu.utah.further.security.api.UserDetails;
import edu.utah.further.security.api.UserDetailsBuilder;

/**
 * Implementation of the <code>UserDetails<code> interface.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}</code>
 * @version May 18, 2010
 */
final class UuLdapUserDetailsImpl implements UserDetails
{
	// =========================================Constants====================================================

	/**
	 * Serializable version identifier
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// =========================================Fields=======================================================

	private String username;
	private String displayName;
	private String email;
	private Collection<String> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	// ========================================Constructor======================================================

	/**
	 * Instantiate this class only throw the {@link Builder} builder.
	 */
	private UuLdapUserDetailsImpl()
	{
		super();
	}

	// ========================================Methods==========================================================

	@Override
	public String getDisplayName()
	{
		return displayName;
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public String getId()
	{
		return getUsername();
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.security.api.UserDetails#getUserAuthorities()
	 */
	@Override
	public Collection<String> getUserAuthorities()
	{
		// TODO Auto-generated method stub
		return authorities;
	}

	public String getPassword()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// ============================= NESTED TYPES ===============================

	/**
	 * Builds an UU-LDAP {@link UserDetails} object.
	 */
	public static class Builder implements UserDetailsBuilder
	{
		// ========================= Constants ==================================

		// ========================== Fields ====================================

		private final UuLdapUserDetailsImpl instance = new UuLdapUserDetailsImpl();
		private final List<String> mutableAuthorities = newList();

		// ============================== Methods =================================

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public UserDetails build()
		{
			// Validation rules
			// Assert.notNull(instance,
			// "Essence can only be used to create a single instance");

			// Make defensive copies to ensure the target object's immutability
			instance.authorities = Collections.unmodifiableList(mutableAuthorities);

			return instance;
		}

		// public Collection<String> getGrantedAuthorities()
		// {
		// return mutableAuthorities;
		// }

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setAccountNonExpired(boolean)
		 */
		@Override
		public void setAccountNonExpired(final boolean accountNonExpired)
		{
			instance.accountNonExpired = accountNonExpired;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setAccountNonLocked(boolean)
		 */
		@Override
		public void setAccountNonLocked(final boolean accountNonLocked)
		{
			instance.accountNonLocked = accountNonLocked;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setAuthorities(java.util.
		 * Collection)
		 */
		@Override
		public void setAuthorities(final Collection<String> authorities)
		{
			mutableAuthorities.clear();
			mutableAuthorities.addAll(authorities);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#addAuthority(java.lang.String
		 * )
		 */
		@Override
		public void addAuthority(final String a)
		{
			if (!hasAuthority(a))
			{
				mutableAuthorities.add(a);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setCredentialsNonExpired(
		 * boolean)
		 */
		@Override
		public void setCredentialsNonExpired(final boolean credentialsNonExpired)
		{
			instance.credentialsNonExpired = credentialsNonExpired;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.utah.further.security.impl.UserDetailsBuilder#setEnabled(boolean)
		 */
		@Override
		public void setEnabled(final boolean enabled)
		{
			instance.enabled = enabled;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setUsername(java.lang.String)
		 */
		@Override
		public void setUsername(final String username)
		{
			Validate.notNull(username, "username must not be null");
			instance.username = username;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setDisplayName(java.lang.
		 * String)
		 */
		@Override
		public void setDisplayName(final String displayName)
		{
			instance.displayName = displayName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.security.impl.UserDetailsBuilder#setEmail(java.lang.String)
		 */
		@Override
		public void setEmail(final String email)
		{
			instance.email = email;
		}

		private boolean hasAuthority(final String a)
		{
			for (final String authority : mutableAuthorities)
			{
				if (authority.equals(a))
				{
					return true;
				}
			}
			return false;
		}

	}

}
