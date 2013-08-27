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
package edu.utah.further.i2b2.query.model;

/**
 * Represents and individual query item such as 'Age 0-9' or 'Anesthesia Procedures'
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
 * @version Aug 19, 2009
 */
public interface I2b2QueryItem
{
	/**
	 * Return the hlevel property.
	 * 
	 * @return the hlevel
	 */
	int getHlevel();

	/**
	 * Return the itemName property.
	 * 
	 * @return the itemName
	 */
	String getItemName();

	/**
	 * Return the itemKey property.
	 * 
	 * @return the itemKey
	 */
	String getItemKey();

	/**
	 * Return the toolTip property.
	 * 
	 * @return the toolTip
	 */
	String getToolTip();

	/**
	 * Return the clazz property.
	 * 
	 * @return the clazz
	 */
	String getClazz();

	/**
	 * Return the constrainByDate property.
	 * 
	 * @return the constrainByDate
	 */
	I2b2QueryDateConstraint getConstrainByDate();

	/**
	 * Return the itemIcon property.
	 * 
	 * @return the itemIcon
	 */
	String getItemIcon();

	/**
	 * Return the itemIsSynonym property.
	 * 
	 * @return the itemIsSynonym
	 */
	String getItemIsSynonym();

	/**
	 * Return the constrainByValue property.
	 * 
	 * @return the constrainByValue
	 */
	I2b2QueryValueConstraint getConstrainByValue();
}
