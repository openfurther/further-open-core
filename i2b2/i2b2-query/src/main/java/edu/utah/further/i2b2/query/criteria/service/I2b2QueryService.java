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
package edu.utah.further.i2b2.query.criteria.service;

import java.util.List;

import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.i2b2.query.criteria.key.I2b2KeyType;
import edu.utah.further.i2b2.query.criteria.service.impl.RawToI2b2QueryConverter;
import edu.utah.further.i2b2.query.model.I2b2Query;
import edu.utah.further.i2b2.query.model.I2b2QueryGroup;
import edu.utah.further.i2b2.query.model.I2b2QueryItem;

/**
 * I2b2 query service to encapsulate service type methods of an {@link I2b2Query}.
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
 * @version Aug 24, 2009
 */
public interface I2b2QueryService extends RawToI2b2QueryConverter
{
	// ========================= METHODS ===================================

	/**
	 * Find the domain of a given key (c_fullname) (typically children), the list contains
	 * the item represented by the key.
	 *
	 * @param itemKey
	 *            the itemKey to find domain for (e.g. /Demographics/Gender/Sex/)
	 * @return a list of basecodes which represent the domain of this item key, includeing
	 *         the basecode of the item key itself
	 */
	List<String> findDomain(String itemKey);

	/**
	 * Finds all children of a given key (c_fullname).
	 *
	 * @param queryItem
	 *            the query item which contains the item key to find domain for
	 * @return a list of basecodes which represent the domain of this query item,
	 *         including the basecode of the item key itself
	 */
	List<String> findDomain(I2b2QueryItem queryItem);

	/**
	 * Translate an i2b2 query to a FURTHeR logical query.
	 *
	 * @param i2b2Query
	 *            an i2b2 query
	 * @return corresponding {@link SearchQuery}
	 */
	SearchQuery getAsSearchQuery(I2b2Query i2b2Query);

	/**
	 * Returns the non demographics {@link I2b2KeyType}'s in this {@link I2b2QueryGroup}
	 *
	 * @param i2b2QueryGroup
	 * @return
	 */
	List<I2b2KeyType> getNonDemKeysInGrp(I2b2QueryGroup i2b2QueryGroup);
}
