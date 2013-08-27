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
package edu.utah.further.security.api.authentication;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * An {@link Authentication} token that for federated authentication. This is used as both
 * an input and an output in {@link AuthenticationProvider}. For input, it is used to
 * carry the non-federated username and possibly roles and as output it is used to carry
 * the federated username and roles. All other methods throw
 * {@link UnsupportedOperationException}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2011 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 29, 2012
 */
public class FederatedAuthenticationToken<P> implements Authentication
{
	// ========================= FIELDS ===================================

	/**
	 * Generated serial uid
	 */
	private static final long serialVersionUID = 7296304004834233621L;

	/**
	 * The username
	 */
	private String username;

	/**
	 * The principal
	 */
	private P principal;

	/**
	 * A federated authentication token is always authenticated since it is
	 * preauthentication.
	 */
	private final boolean authenticated = true;

	/**
	 * The authorities that have been granted;
	 */
	private Collection<GrantedAuthority> authorities = newList();

	// ========================= CONSTRUCTORS ===================================

	/**
	 * Default constructor
	 */
	public FederatedAuthenticationToken()
	{
		super();
	}

	/**
	 * @param username
	 */
	public FederatedAuthenticationToken(final String username)
	{
		super();
		this.username = username;
	}

	// ========================= IMPL + GET/SET ===================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName()
	{
		return username;
	}

	/**
	 * Set a new value for the username property.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setName(final String username)
	{
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getAuthorities()
	 */
	@Override
	public Collection<GrantedAuthority> getAuthorities()
	{
		// Returns an empty list instead of null
		return CollectionUtil.newList(authorities);
	}

	/**
	 * Adds an authority to the granted authorities collection.
	 * 
	 * @param authority
	 *            the authority to add.
	 */
	public void addGrantedAuthority(final String authority)
	{
		authorities.add(new GrantedAuthorityImpl(authority));
	}

	/**
	 * Add all of the granted authorities in a collection.
	 * 
	 * @param authoritiez
	 */
	public void addAllGrantedAuthorities(
			final Collection<? extends GrantedAuthority> authoritiez)
	{
		if (authoritiez != null && authoritiez.size() > 0)
		{
			this.authorities.addAll(authoritiez);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials()
	{
		throw new UnsupportedOperationException(
				"UsernameAuthenticationToken does not contain credentials");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	@Override
	public Object getDetails()
	{
		throw new UnsupportedOperationException(
				"UsernameAuthenticationToken does not contain details");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public P getPrincipal()
	{
		return principal;
	}

	/**
	 * Set a new value for the principal property.
	 * 
	 * @param principal
	 *            the principal to set
	 */
	public void setPrincipal(final P principal)
	{
		this.principal = principal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated()
	{
		return authenticated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
	 */
	@Override
	public void setAuthenticated(final boolean isAuthenticated)
			throws IllegalArgumentException
	{
		throw new UnsupportedOperationException(
				"UsernameAuthenticationToken does not support setting authenticated");
	}

}
