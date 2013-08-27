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

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * A user property which can represent a username, a password, a token, or anything about
 * a user.
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
public interface Property extends PersistentEntity<Long>
{
	/**
	 * Return the namespace property.
	 * 
	 * @return the namespace
	 */
	Long getNamespace();
	
	/**
	 * Set a new value for the namespace property.
	 * 
	 * @param namespace
	 *            the namespace to set
	 */
	void setNamespace(final Long namespace);
	
	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	String getName();
	
	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(final String name);
	
	/**
	 * Return the description property.
	 * 
	 * @return the description
	 */
	String getDescription();
	
	/**
	 * Set a new value for the description property.
	 * 
	 * @param description
	 *            the description to set
	 */
	void setDescription(final String description);
}
