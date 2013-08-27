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
package edu.utah.further.ds.test.authentication;

import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;
import edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService;

/**
 * Test class similiar to {@link TestingAuthenticationProvider}. NOT FOR USE IN
 * PRODUCTION.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jun 12, 2012
 */
public class TestPreAuthenticationFederatedAuthenticationProvider implements
		PreAuthenticatedFederatedAuthenticationProvider<Integer>
{
	/**
	 * The principal.
	 */
	private EnhancedUserDetails userDetails;

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
		final FederatedAuthenticationToken<EnhancedUserDetails> token = new FederatedAuthenticationToken<>(
				userDetails.getUsername());
		token.setPrincipal(userDetails);
		return token;
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.security.api.authentication.
	 * PreAuthenticatedFederatedAuthenticationProvider
	 * #setContextualAuthenticationUserDetailsService
	 * (edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService)
	 */
	@Override
	public void setContextualAuthenticationUserDetailsService(
			final ContextualAuthenticationUserDetailsService<Integer> contextualAuthenticationUserDetailsService)
	{

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

	}

	/**
	 * Return the userDetails property.
	 * 
	 * @return the userDetails
	 */
	public EnhancedUserDetails getUserDetails()
	{
		return userDetails;
	}

	/**
	 * Set a new value for the userDetails property.
	 * 
	 * @param userDetails
	 *            the userDetails to set
	 */
	public void setUserDetails(final EnhancedUserDetails userDetails)
	{
		this.userDetails = userDetails;
	}

}
