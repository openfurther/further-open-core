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
package edu.utah.further.ds.impl.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.utah.further.ds.api.lifecycle.LifeCycle;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;

/**
 * A secure query {@link LifeCycle} requiring authentication. The list of request handlers
 * will have access to the authenticated object providing handles with the ability to
 * authorize requests and retrieve credentials if necessary.
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
public class AuthenticatedQueryLifeCycle extends DataQueryLifeCycle
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(AuthenticatedQueryLifeCycle.class);

	// ========================= FIELDS =================================

	/**
	 * An {@link AuthenticationProvider} to authenticate and subsequently provide
	 * authorization for this {@link LifeCycle}
	 */
	private PreAuthenticatedFederatedAuthenticationProvider<Integer> authenticationProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.impl.lifecycle.DataQueryLifeCycle#triggerCommand(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext triggerCommand(final QueryContext queryContext)
	{
		// Set the context we'll authenticate in
		authenticationProvider.setContext(new Integer(getMetadataRetriever()
				.getMetaData()
				.getNamespaceId()
				.intValue()));

		// Authenticate the user
		final Authentication authentication = authenticationProvider
				.authenticate(new FederatedAuthenticationToken<EnhancedUserDetails>(
						queryContext.getUserId()));
		// Set the authentication object for use by the framework
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return super.triggerCommand(queryContext);
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the authenticationProvider property.
	 * 
	 * @param authenticationProvider
	 *            the authenticationProvider to set
	 */
	public void setAuthenticationProvider(
			final PreAuthenticatedFederatedAuthenticationProvider<Integer> authenticationProvider)
	{
		this.authenticationProvider = authenticationProvider;
	}

}
