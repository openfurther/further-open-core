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
package edu.utah.further.i2b2.query.criteria.service.impl;

import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.fromPattern;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.i2b2.query.criteria.key.I2b2KeyType;
import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;
import edu.utah.further.i2b2.query.model.I2b2QueryItem;

/**
 * Sometimes criteria needs to be handles as a group instead of being handled
 * individually. This object allows you to add the domain of several criteria so they can
 * be handled as a group.
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
 * @version Oct 14, 2011
 */
public final class GroupedCriteria
{
	// ========================= CONSTANTS ===================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(I2b2SearchQueryBuilder.class);

	// ========================= FIELDS ===================================

	/**
	 * A map to hold the {@link I2b2KeyType} for the criteria and a list of all it's
	 * criteria to be grouped.
	 */
	private final Map<I2b2KeyType, List<String>> groupedCriteria = CollectionUtil
			.newMap();

	/**
	 * Adds criteria to the given {@link I2b2KeyType}
	 * 
	 * @param keyType
	 * @param queryItem
	 */
	public void addCriteria(final I2b2QueryItem queryItem)
	{
		final I2b2KeyType keyType = fromPattern(queryItem.getItemKey());
		final List<String> domain = getI2b2QueryService().findDomain(queryItem);

		final List<String> existingDomain = (groupedCriteria.get(keyType) != null) ? groupedCriteria
				.get(keyType) : CollectionUtil.<String>newList();
		existingDomain.addAll(domain);
		groupedCriteria.put(keyType, existingDomain);
	}

	// ========================= PRIVATE METHODS ==========================

	/**
	 * Return the groupedCriteria property.
	 * 
	 * @return the groupedCriteria
	 */
	public Map<I2b2KeyType, List<String>> getGroupedCriteria()
	{
		return groupedCriteria;
	}

	/**
	 * Gets the current {@link I2b2QueryService}
	 * 
	 * @return
	 */
	private I2b2QueryService getI2b2QueryService()
	{
		return I2b2QueryResourceLocator.getInstance().getI2b2QueryService();
	}

}
