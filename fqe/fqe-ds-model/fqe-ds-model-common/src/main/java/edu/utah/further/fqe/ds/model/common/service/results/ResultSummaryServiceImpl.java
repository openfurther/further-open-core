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
package edu.utah.further.fqe.ds.model.common.service.results;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedSingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.fqe.ds.api.service.results.ResultSummaryService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * ...
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
 * @version Sep 9, 2013
 */
@Service("resultSummaryService")
public class ResultSummaryServiceImpl implements ResultSummaryService
{

	/**
	 * Simple JDBC Template for easily executing SQL
	 */
	@Autowired
	private SimpleJdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.results.ResultService#join(java.util.List,
	 * edu.utah.further.fqe.ds.api.results.ResultType)
	 */
	@Override
	public Long join(final List<String> queryIds, final ResultType resultType)
	{
		if (queryIds == null || queryIds.size() == 0)
		{
			throw new ApplicationException("Missing query identifiers");
		}

		long result = 0;

		switch (resultType)
		{
			case SUM:
			{
				long sum = 0;

				// each resultset must be unique before computing the sum
				for (final String queryId : queryIds)
				{
					sum += jdbcTemplate.queryForLong("SELECT COUNT(distinct fed_obj_id) "
							+ "FROM virtual_obj_id_map WHERE query_id = ?", queryId);
				}

				result = sum;

				break;
			}

			case UNION:
			{
				result = jdbcTemplate.queryForLong("SELECT COUNT(distinct fed_obj_id) "
						+ "FROM virtual_obj_id_map WHERE query_id IN (:ids)",
						Collections.singletonMap("ids", queryIds));
				break;
			}

			case INTERSECTION:
			{

				Set<Long> intersection = new HashSet<>();

				boolean first = true;

				// each resultset must be unique before computing the sum
				for (final String queryId : queryIds)
				{
					final List<Long> ids = jdbcTemplate
							.query("SELECT fed_obj_id "
									+ "FROM virtual_obj_id_map WHERE query_id = ?",
									(RowMapper<Long>) new ParameterizedSingleColumnRowMapper<Long>(),
									queryId);

					final HashSet<Long> idSet = new HashSet<>(ids);

					if (first)
					{
						intersection = idSet;
						first = false;
					}
					else
					{
						intersection.retainAll(idSet);
					}
				}
				
				result = intersection.size();
				break;

			}

			default:
				throw new ApplicationException("Unknown result type: " + resultType);
		}

		return new Long(result);
	}

	/**
	 * Return the jdbcTemplate property.
	 * 
	 * @return the jdbcTemplate
	 */
	public SimpleJdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

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

}
