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

import edu.utah.further.security.api.domain.User;


/**
 * General security services
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
 * @version May 8, 2012
 */
public interface SecurityService
{
	
	/**
	 * Finds a user by the supplied federated username
	 * 
	 * @param federatedUsername
	 * @return
	 */
	User findUserByFederatedUsername(Long federatedUsername);
	
	/**
	 * Return the federated username by searching for an alias of it. (e.g. unid u0405293)
	 * 
	 * @param aliasName the name of the alias - e.g. unid
	 * @param aliasValue the value of the alias - e.g. u0405293
	 * @return
	 */
	Long getFederatedUsernameByAlias(String aliasName, String aliasValue);
	
	/**
	 * Returns the username alias for the given namespace
	 * 
	 * @param federatedUsername
	 * @param namespaceId
	 * @return
	 */
	String getUsernameAlias(Long federatedUsername, int namespaceId);
	
	/**
	 * Returns the username alias using the default namespace
	 * 
	 * @param federatedUsername
	 * @return
	 */
	String getUsernameAlias(Long federatedUsername);
	
	/**
	 * Returns whether or not the user exists
	 * 
	 * @param federatedUsername
	 * @return
	 */
	boolean userExists(Long federatedUsername);
}
