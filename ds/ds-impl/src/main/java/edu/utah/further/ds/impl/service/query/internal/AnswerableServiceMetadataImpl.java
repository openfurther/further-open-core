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
package edu.utah.further.ds.impl.service.query.internal;

import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.ds.api.service.query.AnswerableService;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A implementation of {@link AnswerableService} which calls a stored procedure (which in
 * turn looks up metadata) and determines whether or not the query can be answered. A
 * stored procedure implementation was chosen as some of the same logic for query
 * translation can be utilized to determine whether a data source can answer a query. If
 * needed, an alternative implementation could be developed which implements the same
 * stored procedure logic in combination with a metadata database for lookup.
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
 * @version Nov 24, 2010
 */
@Service("metadataAnswerableService")
public class AnswerableServiceMetadataImpl implements AnswerableService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AnswerableServiceMetadataImpl.class);

	/**
	 * The name of the function
	 */
	private static final String FUNCTION_NAME = "CAN_QUERY";

	/**
	 * Oracle function parameter names
	 */
	private static final String[] parameterNames = new String[]
	{ "p_query_context_id", "p_namespace_str" };

	// ========================= DEPENDENCIES ==============================

	/**
	 * JdbcTemplate for executing the query
	 */
	@Autowired
	@Qualifier("virtualRepoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	// =============== IMPL: AnswerableServiceMetadataSpImpl ===============

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.AnswerableService#canAnswer(edu.utah
	 * .further.fqe.ds.api.domain.QueryContext,
	 * edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public boolean canAnswer(final QueryContext queryContext, final DsMetaData dsMetaData)
	{
		final Long queryContextId = queryContext.getId();

		final SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
		call.withFunctionName(FUNCTION_NAME);
		call.withReturnValue();
		call.compile();

		final Map<String, Object> args = CollectionUtil.newMap();
		args.put(parameterNames[1], String.valueOf(dsMetaData.getNamespaceId()));
		args.put(parameterNames[0], queryContextId);

		boolean canAnswer = false;

		try
		{
			canAnswer = call.executeFunction(BigDecimal.class, args).intValue() == 1 ? true
					: false;
		}
		catch (final Exception reason)
		{
			// Could propagate a message to user here if needed
			log.info(
					"Data source is unable to answer the given query: "
							+ queryContext.getQuery(), reason);
		}

		return canAnswer;
	}

	// ========================= PRIVATE METHODS ============================

	// ========================= GET & SET =======================================

	/**
	 * Set a new value for the jdbcTemplate property.
	 * 
	 * @param jdbcTemplate
	 *            the jdbcTemplate to set
	 */
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

}
