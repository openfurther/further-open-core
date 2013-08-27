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
 * The relationship between a user and a property.
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
 * @version May 2, 2012
 */
public interface UserProperty extends PersistentEntity<Long>
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
	 * Return the property property.
	 * 
	 * @return the property
	 */
	Property getProperty();

	/**
	 * Set a new value for the property property.
	 * 
	 * @param property
	 *            the property to set
	 */
	void setProperty(final Property property);

	/**
	 * Return the propertyValue property.
	 * 
	 * @return the propertyValue
	 */
	String getPropertyValue();

	/**
	 * Set a new value for the propertyValue property.
	 * 
	 * @param propertyValue
	 *            the propertyValue to set
	 */
	void setPropertyValue(final String propertyValue);

}
