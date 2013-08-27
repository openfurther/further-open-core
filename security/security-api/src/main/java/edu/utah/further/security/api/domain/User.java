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

import java.util.Collection;
import java.util.Date;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Representation of a user within the FURTHeR system.
 * 
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
public interface User extends PersistentEntity<Long>
{	
	/**
	 * Return the firstname property.
	 * 
	 * @return the firstname
	 */
	String getFirstname();
	
	/**
	 * Set a new value for the firstname property.
	 * 
	 * @param firstname
	 *            the firstname to set
	 */
	void setFirstname(final String firstname);
	
	/**
	 * Return the lastname property.
	 * 
	 * @return the lastname
	 */
	String getLastname();
	
	/**
	 * Set a new value for the lastname property.
	 * 
	 * @param lastname
	 *            the lastname to set
	 */
	void setLastname(final String lastname);
	
	/**
	 * Return the email property.
	 * 
	 * @return the email
	 */
	String getEmail();
	
	/**
	 * Set a new value for the email property.
	 * 
	 * @param email
	 *            the email to set
	 */
	void setEmail(final String email);
	
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
	 * Return the createdBy property.
	 * 
	 * @return the createdBy
	 */
	User getCreatedBy();
	
	/**
	 * Set a new value for the createdBy property.
	 * 
	 * @param createdBy
	 *            the createdBy to set
	 */
	void setCreatedBy(final User createdBy);
	
	/**
	 * Return the expireDate property.
	 * 
	 * @return the expireDate
	 */
	Date getExpireDate();
	
	/**
	 * Set a new value for the expireDate property.
	 * 
	 * @param expireDate
	 *            the expireDate to set
	 */
	void setExpireDate(final Date expireDate);
	
	/**
	 * Return the roles property.
	 * 
	 * @return the roles
	 */
	Collection<UserRole> getRoles();
	
	/**
	 * Set a new value for the roles property.
	 * 
	 * @param roles
	 *            the roles to set
	 */
	void setRoles(final Collection<UserRole> roles);
	
	/**
	 * Return the properties property.
	 * 
	 * @return the properties
	 */
	Collection<UserProperty> getProperties();
	
	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	void setProperties(final Collection<UserProperty> properties);

}
