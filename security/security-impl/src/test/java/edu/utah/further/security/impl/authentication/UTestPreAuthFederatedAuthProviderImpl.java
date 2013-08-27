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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;
import edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService;
import edu.utah.further.security.impl.domain.DefaultUserDetailsImpl;

/**
 * ...
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
 * @version May 6, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/security-impl-test-context-annotation.xml",
		"/security-impl-test-context-datasource.xml" })
public class UTestPreAuthFederatedAuthProviderImpl
{
	/**
	 * The {@link PreAuthenticatedFederatedAuthenticationProvider} we are testing.
	 */
	@Autowired
	private PreAuthenticatedFederatedAuthenticationProvider<Integer> authenticationProvider;

	/**
	 * A mocked out {@link ContextualAuthenticationUserDetailsService}
	 */
	@SuppressWarnings("unchecked")
	private final ContextualAuthenticationUserDetailsService<Integer> userDetailsService = EasyMock
			.createMock(ContextualAuthenticationUserDetailsService.class);

	/**
	 * Tests authenticating a user and returning a populated authentication object
	 */
	@Test
	public void authentication()
	{

		final Authentication token = new FederatedAuthenticationToken<EnhancedUserDetails>(
				"12345");

		final DefaultUserDetailsImpl userDetails = new DefaultUserDetailsImpl();
		userDetails.setUsername("Dustin");
		userDetails.setAccountNonExpired(false);
		userDetails.setAccountNonLocked(false);
		userDetails.setAuthorities(CollectionUtil.<GrantedAuthority> newList());
		userDetails.setEnabled(true);
		userDetails.setProperties(CollectionUtil.<String, String> newMap());
		userDetails.setPassword("Password");

		expect(userDetailsService.loadUserDetails(token)).andStubReturn(userDetails);
		replay(userDetailsService);

		authenticationProvider
				.setContextualAuthenticationUserDetailsService(userDetailsService);

		final Authentication populatedAuthentication = authenticationProvider
				.authenticate(token);

		assertThat(populatedAuthentication, notNullValue());
		assertThat((DefaultUserDetailsImpl) populatedAuthentication.getPrincipal(),
				is(userDetails));
	}
}
