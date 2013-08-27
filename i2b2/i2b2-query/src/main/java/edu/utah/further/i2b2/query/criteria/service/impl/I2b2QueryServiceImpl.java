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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.fromPattern;
import static edu.utah.further.i2b2.query.util.I2b2QueryUtil.clean;
import static org.apache.commons.lang.Validate.notNull;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.i2b2.query.criteria.dao.I2b2QueryDao;
import edu.utah.further.i2b2.query.criteria.key.I2b2KeyType;
import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;
import edu.utah.further.i2b2.query.model.I2b2Query;
import edu.utah.further.i2b2.query.model.I2b2QueryGroup;
import edu.utah.further.i2b2.query.model.I2b2QueryItem;

/**
 * Implementation of the {@link I2b2QueryService}.
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
@Service("i2b2QueryService")
public class I2b2QueryServiceImpl implements I2b2QueryService
{
	// ========================= CONSTANTS ===========================

	/**
	 * Base code string format separator.
	 */
	private static final String BASE_CODE_KEY_SCOPE_SEPARATOR = ":";

	// ========================= FIELDS ==============================

	// ========================= DEPENDENCIES ========================

	/**
	 * Performs query entity CRUD operations.
	 */
	@Autowired
	private I2b2QueryDao queryDao;

	/**
	 * i2b2-raw-to-I2b2-query converter.
	 */
	@Resource(name = "rawToI2b2QueryConverter")
	private RawToI2b2QueryConverter rawToI2b2QueryConverter;

	// ============= IMPLEMENTATION: I2b2QueryService ================

	/**
	 * Executes minor pre-processing of item key and finds the domain of given item key.
	 * 
	 * @param itemKey
	 * @return
	 * @see edu.utah.further.i2b2.query.criteria.service.I2b2QueryService#findDomain(java.lang.String)
	 */
	@Override
	public List<String> findDomain(final String itemKey)
	{
		final String preProcessedKey = keyPreProcessing(itemKey);
		final List<String> childrenBaseCodes = queryDao.findDomain(preProcessedKey);
		return baseCodePostProcessing(childrenBaseCodes);
	}

	/**
	 * A convenience method which delegates to {@link #findDomain(String)}
	 * 
	 * @param queryItem
	 * @return
	 * @see edu.utah.further.i2b2.query.criteria.service.I2b2QueryService#findDomain(edu.utah.further.i2b2.query.model.I2b2QueryItem)
	 */
	@Override
	public List<String> findDomain(final I2b2QueryItem queryItem)
	{
		notNull(queryItem.getItemKey());
		return findDomain(queryItem.getItemKey());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.i2b2.query.criteria.service.I2b2QueryService#toI2b2Query(java.
	 * lang.String)
	 */
	@Override
	public String toI2b2Query(final String i2b2MessageXml)
	{
		return rawToI2b2QueryConverter.toI2b2Query(i2b2MessageXml);
	}

	/**
	 * Adapt an i2b2 query to a FURTHeR logical query.
	 * 
	 * @param i2b2Query
	 *            an i2b2 query
	 * @return corresponding {@link SearchQuery}
	 */
	@Override
	public SearchQuery getAsSearchQuery(final I2b2Query i2b2Query)
	{
		return new I2b2SearchQueryBuilder(i2b2Query).build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.i2b2.query.criteria.service.I2b2QueryService#getKeysInGrp(edu.
	 * utah.further.i2b2.query.model.I2b2QueryGroup)
	 */
	@Override
	public List<I2b2KeyType> getNonDemKeysInGrp(final I2b2QueryGroup i2b2QueryGroup)
	{
		final Set<I2b2KeyType> keys = newSet();
		for (final I2b2QueryItem i2b2QueryItem : i2b2QueryGroup.getQueryItems())
		{
			final I2b2KeyType keyType = fromPattern(i2b2QueryItem.getItemKey());
			// Only concerned with non demographic keys
			if (!keyType.isDemographic())
			{
				keys.add(keyType);
			}
		}
		return newList(keys);
	}

	// ========================= SETTERS ==============================

	/**
	 * Set a new value for the queryDao property.
	 * 
	 * @param queryDao
	 *            the queryDao to set
	 */
	public void setQueryDao(final I2b2QueryDao queryDao)
	{
		this.queryDao = queryDao;
	}

	// ========================= PRIVATE METHODS ==============================

	/**
	 * Item key pre-processing which removes formatting and adds an SQL ANSI wildcard to
	 * the end of the String.
	 * 
	 * @param itemKey
	 * @return
	 */
	private String keyPreProcessing(final String itemKey)
	{
		final String cleaned = clean(itemKey);
		final String wildCardedItemKey = StringUtil.addSqlWildcardToEnd(cleaned);
		return wildCardedItemKey;
	}

	/**
	 * Basecode post-processing which removes the terminology acronym such as ICD-9, in
	 * the string &quot;ICD-9:12345&quot;.
	 * 
	 * @param baseCodes
	 *            a list of basecodes to apply post processing too
	 * @return a list of basecodes with terminology acryonm's removed
	 */
	private List<String> baseCodePostProcessing(final List<String> baseCodes)
	{
		final List<String> postProcessedBaseCodes = newList();
		for (final String baseCode : baseCodes)
		{
			if (baseCode.indexOf(BASE_CODE_KEY_SCOPE_SEPARATOR) > 0)
			{
				postProcessedBaseCodes.add(baseCode.substring(baseCode
						.indexOf(BASE_CODE_KEY_SCOPE_SEPARATOR) + 1));
			}
		}
		return postProcessedBaseCodes;
	}

}
