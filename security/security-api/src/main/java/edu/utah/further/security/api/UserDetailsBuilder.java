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

import edu.utah.further.core.api.lang.Builder;

/**
 * An abstraction of {@link UserDetails} builders.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 25, 2010
 */
public interface UserDetailsBuilder extends Builder<UserDetails>
{
	// ========================= METHODS ===================================

	/**
	 * @param accountNonExpired
	 */
	void setAccountNonExpired(boolean accountNonExpired);

	/**
	 * @param accountNonLocked
	 */
	void setAccountNonLocked(boolean accountNonLocked);

	/**
	 * @param authorities
	 */
	void setAuthorities(Collection<String> authorities);

	/**
	 * Adds the authority to the list, unless it is already there, in which case it is
	 * ignored
	 */
	void addAuthority(String a);

	/**
	 * @param credentialsNonExpired
	 */
	void setCredentialsNonExpired(boolean credentialsNonExpired);

	/**
	 * @param enabled
	 */
	void setEnabled(boolean enabled);

	/**
	 * @param username
	 */
	void setUsername(String username);

	/**
	 * @param displayName
	 */
	void setDisplayName(String displayName);

	/**
	 * @param email
	 */
	void setEmail(String email);
}