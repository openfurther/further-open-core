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

import java.util.List;

/**
 * Represents an I2b2 query group using the i2b2 query tool. There is an implicit or'ing
 * of {@link I2b2QueryItem}'s held by this query group. For instance, this query item OR
 * this query item OR that query item.
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
public interface I2b2QueryGroup
{
	// ========================= METHODS ===================================

	/**
	 * Gets the query items in this query group.
	 * 
	 * @return the query items
	 */
	List<I2b2QueryItem> getQueryItems();

	/**
	 * A flag indicating whether or not this query group is inverted (NOT'ed).
	 * 
	 * @return true if this query group is inverted, false otherwise
	 */
	boolean isInverted();

	/**
	 * The number of occurrences of this query group.
	 * 
	 * @return an integer representing the number of occurrences of this query group
	 */
	int getOccurrences();
}
