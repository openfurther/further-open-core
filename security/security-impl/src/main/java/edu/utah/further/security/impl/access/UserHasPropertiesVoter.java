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

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import edu.utah.further.security.api.EnhancedUserDetails;

/**
 * An {@link AccessDecisionVoter} that votes whether or a user is granted or denied
 * depending on whether or not they have all of the required properties.
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
public class UserHasPropertiesVoter implements AccessDecisionVoter
{
	/**
	 * The list of required properties this user must have in order to be voted
	 * {@link AccessDecisionVoter#ACCESS_GRANTED}
	 */
	private List<String> requiredProperties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.AccessDecisionVoter#supports(org.springframework
	 * .security.access.ConfigAttribute)
	 */
	@Override
	public boolean supports(final ConfigAttribute attribute)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.AccessDecisionVoter#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> clazz)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.AccessDecisionVoter#vote(org.springframework
	 * .security.core.Authentication, java.lang.Object, java.util.Collection)
	 */
	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes)
	{
		final EnhancedUserDetails userDetails = (EnhancedUserDetails) authentication
				.getPrincipal();

		for (final String property : requiredProperties)
		{
			if (!userDetails.getProperties().containsKey(property))
			{
				return ACCESS_DENIED;
			}
		}

		return ACCESS_GRANTED;
	}

	/**
	 * Return the requiredProperties property.
	 * 
	 * @return the requiredProperties
	 */
	public List<String> getRequiredProperties()
	{
		return requiredProperties;
	}

	/**
	 * Set a new value for the requiredProperties property.
	 * 
	 * @param requiredProperties
	 *            the requiredProperties to set
	 */
	public void setRequiredProperties(final List<String> requiredProperties)
	{
		this.requiredProperties = requiredProperties;
	}

}
