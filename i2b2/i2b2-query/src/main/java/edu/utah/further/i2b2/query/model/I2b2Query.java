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

import edu.utah.further.core.api.security.HasSettableUserIdentifier;

/**
 * An I2b2 query in the format of the i2b2 query tool.
 *
 * I2b2 queries are logically constructed as follows:
 *
 * <code>
 * I2b2Query {
 * 	QueryGroup1{ QueryItem1 OR QueryItem2 }
 * 	AND
 * 	QueryGroup2{ QueryItem3 OR QueryItem4 }
 * 	AND
 * 	QueryGroup3{ QueryItem5 OR QueryItem6 }
 * }
 * </code>
 *
 * with the ability to invert a whole query group such as:
 *
 * <code>
 * I2b2Query {
 * 	QueryGroup1{ QueryItem1 OR QueryItem2 }
 * 	AND
 * 	QueryGroup2{ QueryItem3 OR QueryItem4 }
 * 	AND NOT
 * 	QueryGroup3{ QueryItem5 OR QueryItem6 }
 * }
 * </code>
 *
 * There is an implicit and'ing of {@link I2b2QueryGroup}'s held in this query. For
 * instance, this query group AND this query group AND NOT this query group.
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
public interface I2b2Query extends HasSettableUserIdentifier<String>
{
	// ========================= METHODS ===================================

	/**
	 * Gets the query groups for this query
	 *
	 * @return the query groups
	 */
	List<I2b2QueryGroup> getQueryGroups();
}
