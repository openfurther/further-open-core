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
 * The relationship between a role and a privilege.
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
public interface RolePrivilege extends PersistentEntity<Long>
{
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
	 * Return the privilege property.
	 * 
	 * @return the privilege
	 */
	Privilege getPrivilege();

	/**
	 * Set a new value for the privilege property.
	 * 
	 * @param privilege
	 *            the privilege to set
	 */
	void setPrivilege(final Privilege privilege);

	/**
	 * Return the grantedObjectType property.
	 * 
	 * @return the grantedObjectType
	 */
	String getGrantedObjectType();

	/**
	 * Set a new value for the grantedObjectType property.
	 * 
	 * @param grantedObjectType
	 *            the grantedObjectType to set
	 */
	void setGrantedObjectType(final String grantedObjectType);

	/**
	 * Return the grantedObject property.
	 * 
	 * @return the grantedObject
	 */
	String getGrantedObject();

	/**
	 * Set a new value for the grantedObject property.
	 * 
	 * @param grantedObject
	 *            the grantedObject to set
	 */
	void setGrantedObject(final String grantedObject);

	/**
	 * Return the grantedBy property.
	 * 
	 * @return the grantedBy
	 */
	User getGrantedBy();

	/**
	 * Set a new value for the grantedBy property.
	 * 
	 * @param grantedBy
	 *            the grantedBy to set
	 */
	void setGrantedBy(final User grantedBy);

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
	 * Return the parent property.
	 * 
	 * @return the parent
	 */
	RolePrivilege getParent();
	
	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parent
	 *            the parent to set
	 */
	void setParent(final RolePrivilege parent);
	
}
