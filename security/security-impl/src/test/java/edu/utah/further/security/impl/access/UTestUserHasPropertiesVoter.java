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
package edu.utah.further.security.impl.access;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.impl.domain.DefaultUserDetailsImpl;

/**
 * Tests The {@link UserHasPropertiesVoter}
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
public class UTestUserHasPropertiesVoter
{
	private UserHasPropertiesVoter decisionVoter;
	private DefaultUserDetailsImpl userDetails;
	private FederatedAuthenticationToken<EnhancedUserDetails> token;

	@Before
	public void setup()
	{
		decisionVoter = new UserHasPropertiesVoter();
		final List<String> requiredProperties = CollectionUtil.newList();
		requiredProperties.add("USERNAME");
		requiredProperties.add("PASSWORD");
		decisionVoter.setRequiredProperties(requiredProperties);
		userDetails = new DefaultUserDetailsImpl();

		token = new FederatedAuthenticationToken<>();
		token.setPrincipal(userDetails);
	}

	/**
	 * Ensures that a user who has all the properties is granted ACCESS_GRANTED
	 */
	@Test
	public void userHasProperties()
	{

		final Map<String, String> properties = CollectionUtil.newMap();
		properties.put("USERNAME", "username");
		properties.put("PASSWORD", "password");
		userDetails.setProperties(properties);

		final int decision = decisionVoter.vote(token, this,
				CollectionUtil.<ConfigAttribute> newList());

		assertThat(new Integer(decision), is(new Integer(
				AccessDecisionVoter.ACCESS_GRANTED)));

	}

	/**
	 * Ensures that a user who doesn't have all the properties is granted ACCESS_DENIED
	 */
	@Test
	public void userDoesNotHaveProperties()
	{
		final Map<String, String> properties = CollectionUtil.newMap();
		properties.put("USERNAME", "username");
		userDetails.setProperties(properties);

		final int decision = decisionVoter.vote(token, this,
				CollectionUtil.<ConfigAttribute> newList());

		assertThat(new Integer(decision), is(new Integer(
				AccessDecisionVoter.ACCESS_DENIED)));

	}
}
