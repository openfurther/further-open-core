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

package edu.utah.further.security.api;

import java.util.Collection;

import edu.utah.further.core.api.discrete.HasIdentifier;

/**
 * An interface providing user information. This interface was adapted from
 * <code>org.springframework.security.core.userdetails.UserDetails</code>.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}
 * @version May 16, 2010
 */
public interface UserDetails extends HasIdentifier<String>
{
	// ========================= METHODS ===================================

	/**
	 * Returns the authorities (e.g. Roles) represented as <code>String</code> granted to
	 * the user. Cannot return <code>null</code>.
	 * 
	 * @return the authorities, sorted by natural key (never <code>null</code>)
	 */
	Collection<String> getUserAuthorities();

	// /**
	// * Returns the password used to authenticate the user. Cannot return
	// <code>null</code>.
	// *
	// * @return the password (never <code>null</code>)
	// */
	// String getPassword();

	/**
	 * Returns the username used to authenticate the user. Cannot return <code>null</code>
	 * .
	 * 
	 * @return the username (never <code>null</code>)
	 */
	String getUsername();

	/**
	 * Returns the unique ID for the authenticated the user. Cannot return
	 * <code>null</code>.
	 * 
	 * @return the username (never <code>null</code>)
	 */
	@Override
	String getId();

	/**
	 * Returns the display name of the user.
	 * 
	 * @return the display name
	 */
	String getDisplayName();

	/**
	 * Returns the email address of the user.
	 * 
	 * @return the email
	 */
	String getEmail();

	/**
	 * Indicates whether the user's account has expired. An expired account cannot be
	 * authenticated.
	 * 
	 * @return <code>true</code> if the user's account is valid (ie non-expired),
	 *         <code>false</code> if no longer valid (ie expired)
	 */
	boolean isAccountNonExpired();

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be
	 * authenticated.
	 * 
	 * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
	 */
	boolean isAccountNonLocked();

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired
	 * credentials prevent authentication.
	 * 
	 * @return <code>true</code> if the user's credentials are valid (ie non-expired),
	 *         <code>false</code> if no longer valid (ie expired)
	 */
	boolean isCredentialsNonExpired();

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be
	 * authenticated.
	 * 
	 * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
	 */
	boolean isEnabled();
}
