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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.chain.AbstractRequestProcessor;
import edu.utah.further.ds.api.service.metadata.MetaDataService;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;
import edu.utah.further.security.impl.domain.DefaultUserDetailsImpl;

/**
 * Unit test for an authenticated query lifecycle which makes the authenticated object
 * available for querying.
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
 * @version May 7, 2012
 */
public class UTestAuthenticatedQueryLifeCycle
{
	/**
	 * The authenticated lifecycle we're testing
	 */
	private final AuthenticatedQueryLifeCycle lifeCycle = new AuthenticatedQueryLifeCycle();

	/**
	 * A mocked authentication provider
	 */
	private final PreAuthenticatedFederatedAuthenticationProvider<Integer> authenticationProvider = EasyMock
			.createNiceMock(PreAuthenticatedFederatedAuthenticationProvider.class);

	/**
	 * A mocked Meta Data Service
	 */
	private final MetaDataService metaDataService = EasyMock
			.createNiceMock(MetaDataService.class);

	/**
	 * Tests the the authentication object is properly set and available within the
	 * lifecycle.
	 */
	@Test
	public void triggerAuthenticatedCommand()
	{
		final QueryContextToImpl queryContext = new QueryContextToImpl();
		queryContext.setId(new Long(1L));
		queryContext.setUserId("12345");
		queryContext.setStaleDateTime(new Date());
		queryContext.queue();

		final DefaultUserDetailsImpl userDetails = new DefaultUserDetailsImpl();
		userDetails.setUsername(queryContext.getUserId());
		userDetails.setAccountNonExpired(false);
		userDetails.setAccountNonLocked(false);
		userDetails.setAuthorities(CollectionUtil.<GrantedAuthority> newList());
		userDetails.setEnabled(true);
		userDetails.setProperties(CollectionUtil.<String, String> newMap());
		userDetails.setPassword("Password");

		final FederatedAuthenticationToken<EnhancedUserDetails> populatedAuthentication = new FederatedAuthenticationToken<>();
		populatedAuthentication.setPrincipal(userDetails);

		final DsMetaData dsMetaData = new DsMetaData();
		dsMetaData.setNamespaceId(new Long(1));
		expect(authenticationProvider.authenticate(anyObject(Authentication.class)))
				.andStubReturn(populatedAuthentication);
		expect(metaDataService.getMetaData()).andStubReturn(dsMetaData);
		replay(authenticationProvider, metaDataService);

		lifeCycle.setAuthenticationProvider(authenticationProvider);
		lifeCycle.setMetadataRetriever(metaDataService);

		final List<RequestProcessor> requestProcessors = CollectionUtil.newList();
		requestProcessors.add(new TestProcessor());
		lifeCycle.setRequestProcessors(requestProcessors);

		lifeCycle.triggerCommand(queryContext);

		assertThat(SecurityContextHolder.getContext().getAuthentication(), notNullValue());
	}

	/**
	 * Private tester class for ensuring that we have access to the {@link Authentication}
	 * object during request processing.
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 * 
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version May 7, 2012
	 */
	private static final class TestProcessor extends AbstractRequestProcessor
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core
		 * .api.chain.ChainRequest)
		 */
		@Override
		public boolean process(final ChainRequest request)
		{
			final Authentication authentication = SecurityContextHolder
					.getContext()
					.getAuthentication();
			assertThat(authentication, notNullValue());
			final EnhancedUserDetails userDetails = (EnhancedUserDetails) authentication
					.getPrincipal();
			assertThat(userDetails.getUsername(), is("12345"));
			return true;
		}

	}
}
