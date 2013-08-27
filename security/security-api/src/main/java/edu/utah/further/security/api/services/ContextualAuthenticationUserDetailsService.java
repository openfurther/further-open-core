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
package edu.utah.further.security.api.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.utah.further.security.api.EnhancedUserDetails;

/**
 * A federated user details service which provides the ability to load {@link UserDetails}
 * based on the context.
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
public interface ContextualAuthenticationUserDetailsService<C> extends
		AuthenticationUserDetailsService
{

	/**
	 * 
	 * @param token
	 *            The pre-authenticated authentication token
	 * @return UserDetails for the given authentication token, never null.
	 * @throws UsernameNotFoundException
	 *             if no user details can be found for the given authentication token
	 */
	@Override
	EnhancedUserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException;

	/**
	 * Set a new value for the context property.
	 * 
	 * @param defaultContext
	 *            the defaultContext to set
	 */
	void setContext(final C context);

	/**
	 * Return the defaultContext property.
	 * 
	 * @return the defaultContext
	 */
	C getContext();

}
