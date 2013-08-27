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
package edu.utah.further.security.impl.authentication;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;
import edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService;

/**
 * A pre-authenticated federated authentication provider which populates an
 * {@link Authentication} object based on the context
 * {@link ContextualAuthenticationUserDetailsService} and context provided
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
 * @version May 1, 2012
 */
@Service("authenticationProvider")
public class PreAuthenticatedFederatedAuthenticationProviderImpl implements
		PreAuthenticatedFederatedAuthenticationProvider<Integer>
{
	/**
	 * Loads {@link UserDetails} depending on the data source
	 */
	@Autowired
	private ContextualAuthenticationUserDetailsService<Integer> caUserDetailsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#authenticate
	 * (org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException
	{
		if (!supports(authentication.getClass()))
		{
			throw new ApplicationException("Unsupported authentication class "
					+ authentication.getClass().getCanonicalName());
		}
		final UserDetails userDetails = caUserDetailsService
				.loadUserDetails(authentication);
		final FederatedAuthenticationToken<UserDetails> authenticatedToken = new FederatedAuthenticationToken<>();
		authenticatedToken.addAllGrantedAuthorities(userDetails.getAuthorities());
		authenticatedToken.setPrincipal(userDetails);
		authenticatedToken.setName(userDetails.getUsername());
		return authenticatedToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#supports(java
	 * .lang.Class)
	 */
	@Override
	public boolean supports(final Class<? extends Object> authentication)
	{
		if (authentication.equals(FederatedAuthenticationToken.class))
		{
			return true;
		}
		return false;
	}

	/**
	 * Set a new value for the contextualAuthenticationUserDetailsService property.
	 * 
	 * @param contextualAuthenticationUserDetailsService
	 *            the contextualAuthenticationUserDetailsService to set
	 */
	@Override
	public void setContextualAuthenticationUserDetailsService(
			final ContextualAuthenticationUserDetailsService<Integer> contextualAuthenticationUserDetailsService)
	{
		this.caUserDetailsService = contextualAuthenticationUserDetailsService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.security.api.authentication.
	 * PreAuthenticatedFederatedAuthenticationProvider#setContext(java.lang.Object)
	 */
	@Override
	public void setContext(final Integer context)
	{
		Validate.notNull(this.caUserDetailsService);
		this.caUserDetailsService.setContext(context);
	}

}
