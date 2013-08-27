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
package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.constant.Constants.Scope.PROTOTYPE;
import static org.slf4j.LoggerFactory.getLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.data.oracle.type.OracleXmlTypeAsString;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.api.domain.PhysicalQuery;
import edu.utah.further.ds.api.service.query.logic.QueryTranslator;
import edu.utah.further.ds.data.domain.PhysicalQueryEntity;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;

/**
 * A {@link SearchQuery} translation service which takes as input, a {@link SearchQuery}
 * and a data source model identifier (<code>targetNamespaceId</code>), and translates the
 * {@link SearchQuery} into the data source model, so that it can run against the data
 * source.
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
 * @version Sep 9, 2009
 */
@Service("queryTranslatorSp")
@Qualifier("impl")
@Scope(PROTOTYPE)
public final class QueryTranslatorSpImpl implements QueryTranslator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryTranslatorSpImpl.class);

	// ========================= FIELDS =====================================

	// ========================= DEPENDENCIES ===============================

	/**
	 * Marshaller/Unmarshaller
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * JdbcTemplate for executing the query
	 */
	@Autowired
	@Qualifier("virtualRepoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	// ========================= IMPL: QueryTranslatorSpImpl ============

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.QueryTranslator#translate(edu.utah.
	 * further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public <T> T translate(final QueryContext queryContext, final Map<String, Object> attributes)
	{
		if (log.isTraceEnabled())
		{
			log.trace("Translating query " + queryContext.getQuery() + " ... ");
		}

		// This is the response -- is a list but we only currently expect one result
		final List<PhysicalQuery> physicalQueryEntities = executeTranslation(
				queryContext.getQuery(), queryContext.getTargetNamespaceId(),
				queryContext.getParent().getId());

		final String physicalQueryXml = physicalQueryEntities.get(0).getQueryXml();
		if (log.isDebugEnabled())
		{
			log.debug("Physical Query XML: " + physicalQueryXml);
		}

		return (T) FqeDsQueryContextUtil.unmarshalSearchQuery(xmlService, physicalQueryXml);
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * Execute query translation. Throws an {@link ApplicationException} if an error
	 * occurs or no response is received from the stored procedure.
	 * 
	 * @param searchQuery
	 *            logical query = translation input
	 * @param targetNamespaceId
	 * @param queryId
	 * @return translation result: list of physical queries
	 */
	private List<PhysicalQuery> executeTranslation(final SearchQuery searchQuery,
			final Long targetNamespaceId, final Long queryContextId)
	{
		final List<PhysicalQuery> physicalQueryEntities = executeProcedure(queryContextId,
				targetNamespaceId);
		if (physicalQueryEntities.isEmpty())
		{
			throw new ApplicationException(
					"An error occurred during query translation, no physical queries returned: [SearchQuery = "
							+ searchQuery
							+ "], [targetNamespaceId = "
							+ targetNamespaceId
							+ "]. If query translation did not return with stored procedure errors, "
							+ "it's possible there may be a problem with the stored procedure call.");
		}
		return physicalQueryEntities;
	}

	/**
	 * A helper method to execute the procedure in a transaction with a commit.
	 * 
	 * @param queryId
	 * @param targetNamespaceId
	 * @return
	 */
	private List<PhysicalQuery> executeProcedure(final Long queryContextId,
			final Long targetNamespaceId)
	{
		final SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName(
				"GET_PHYSICAL_QUERY").returningResultSet("p_sys_refcur",
				new RowMapper<PhysicalQueryEntity>()
				{
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.springframework.jdbc.core.simple.ParameterizedRowMapper#mapRow
					 * (java.sql.ResultSet, int)
					 */
					@SuppressWarnings("deprecation")
					@Override
					public PhysicalQueryEntity mapRow(final ResultSet rs, final int rowNum)
							throws SQLException
					{
						final PhysicalQueryEntity entity = new PhysicalQueryEntity();
						final UserType xmlType = new OracleXmlTypeAsString();
						entity.setQueryXml((String) xmlType.nullSafeGet(rs, new String[]
						{ "query_xml" }, entity));
						return entity;
					}

				});

		final Map<String, Object> args = CollectionUtil.newMap();
		args.put("p_namespace_str", targetNamespaceId);
		args.put("p_query_context_id", queryContextId);
		final Map<String, Object> queries = call.execute(args);

		return (List<PhysicalQuery>) queries.get("p_sys_refcur");

	}

	/**
	 * Set a new value for the xmlService property.
	 * 
	 * @param xmlService
	 *            the xmlService to set
	 */
	public void setXmlService(final XmlService xmlService)
	{
		this.xmlService = xmlService;
	}

}
