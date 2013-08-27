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
package edu.utah.further.core.query.domain;

/**
 * A {@link SearchQuery} alias which is an association to another object
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 23, 2013
 */
public interface SearchQueryAlias
{

	/**
	 * Return the associationObject property.
	 * 
	 * @return the associationObject
	 */
	String getAssociationObject();

	/**
	 * Set a new value for the object property.
	 * 
	 * @param associationObject
	 *            the object to set
	 */
	void setAssociationObject(String associationObject);

	/**
	 * Return the key property.
	 * 
	 * @return the key
	 */
	String getKey();

	/**
	 * Set a new value for the key property.
	 * 
	 * @param key
	 *            the key to set
	 */
	void setKey(String key);

	/**
	 * Return the value property.
	 * 
	 * @return the value
	 */
	String getValue();

	/**
	 * Set a new value for the value property.
	 * 
	 * @param value
	 *            the value to set
	 */
	void setValue(String value);

}