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
package edu.utah.further.security.api.domain;

import java.util.Date;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The relationship between a user and a role.
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
 * @version May 2, 2012
 */
public interface UserRole extends PersistentEntity<Long>
{
	/**
	 * Return the user property.
	 * 
	 * @return the user
	 */
	User getUser();

	/**
	 * Set a new value for the user property.
	 * 
	 * @param user
	 *            the user to set
	 */
	void setUser(final User user);
	
	/**
	 * Return the role property.
	 * 
	 * @return the role
	 */
	Role getRole();
	
	/**
	 * Set a new value for the role property.
	 * 
	 * @param role
	 *            the role to set
	 */
	void setRole(final Role role);
	
	/**
	 * Return the createdDate property.
	 * 
	 * @return the createdDate
	 */
	Date getCreatedDate();
	
	/**
	 * Set a new value for the createdDate property.
	 * 
	 * @param createdDate
	 *            the createdDate to set
	 */
	void setCreatedDate(final Date createdDate);
	
	/**
	 * Return the expiredDate property.
	 * 
	 * @return the expiredDate
	 */
	Date getExpiredDate();
	
	/**
	 * Set a new value for the expiredDate property.
	 * 
	 * @param expiredDate
	 *            the expiredDate to set
	 */
	void setExpiredDate(final Date expiredDate);
	
}
