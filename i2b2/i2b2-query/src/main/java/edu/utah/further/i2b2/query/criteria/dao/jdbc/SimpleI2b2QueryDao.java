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
package edu.utah.further.i2b2.query.criteria.dao.jdbc;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.StringUtil.newStringBuilder;
import static org.apache.commons.lang.Validate.notNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import edu.utah.further.i2b2.query.criteria.dao.I2b2QueryDao;

/**
 * A simple implementation of the {@link I2b2QueryDao} which uses native SQL.
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
@Repository("i2b2QueryDao")
public class SimpleI2b2QueryDao implements I2b2QueryDao
{
	// ========================= CONSTANTS ===========================

	private static final String CODE = "C_BASECODE";

	// ========================= DEPENDENCIES ==============================

	/**
	 * The Spring jdbc template used for data access.
	 */
	@Autowired
	private SimpleJdbcTemplate jdbcTemplate;

	/**
	 * The name of the table to search in.
	 */
	private String tableName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies.
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		notNull(jdbcTemplate);
		notNull(tableName);
	}

	// ================ IMPLEMENTATION: I2b2QueryDao ==================

	/**
	 * @param itemKey
	 * @return
	 * @see edu.utah.further.i2b2.query.criteria.dao.I2b2QueryDao#findDomain(java.lang.String)
	 */
	@Override
	public List<String> findDomain(final String itemKey)
	{
		final List<Map<String, Object>> childrenListMap = queryForChildren(itemKey);
		final List<String> childrenList = newList();
		for (final Map<String, Object> childrenMap : childrenListMap)
		{
			final String basecode = (String) childrenMap.get(CODE);
			if (basecode != null)
			{
				childrenList.add(String.valueOf(basecode));
			}
		}
		return childrenList;
	}

	// ========================= SETTERS ==============================

	/**
	 * Set a new value for the jdbcTemplate property.
	 * 
	 * @param jdbcTemplate
	 *            the jdbcTemplate to set
	 */
	public void setJdbcTemplate(final SimpleJdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Set a new value for the tableName property.
	 * 
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(final String tableName)
	{
		this.tableName = tableName;
	}

	// ========================= PRIVATE METHODS ==============================

	/**
	 * Private method to query for the children
	 * 
	 * @param itemKey
	 * @return
	 */
	private List<Map<String, Object>> queryForChildren(final String itemKey)
	{
		final String sql = newStringBuilder()
				.append("SELECT DISTINCT c_basecode, c_hlevel FROM ")
				.append(tableName)
				.append(" WHERE trim(upper(c_fullname)) LIKE trim(upper(:fullname))")
				.append(" order by c_hlevel")
				.toString();
		return jdbcTemplate.queryForList(sql,
				Collections.singletonMap("fullname", itemKey));
	}

}
