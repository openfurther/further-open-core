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
package edu.utah.further.fqe.impl.service.export;

import static edu.utah.further.core.api.constant.ErrorCode.INVALID_EXPORT_CONTEXT;
import static edu.utah.further.core.api.constant.ErrorCode.INVALID_RESULTS;
import static edu.utah.further.core.api.constant.ErrorCode.QUERY_NOT_EXIST;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.data.util.SqlUtil;
import edu.utah.further.fqe.api.service.export.ExportService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.ExportFormat;
import edu.utah.further.fqe.ds.api.domain.Exporter;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.security.api.exceptions.AuthorizationException;
import edu.utah.further.security.api.services.AuditService;
import edu.utah.further.security.api.services.SecurityService;

/**
 * A generic export service which exports data based on the {@link ExportFormat}
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
 * @version Sep 26, 2012
 */
@Service("exportService")
public class ExportServiceImpl implements ExportService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ExportServiceImpl.class);

	/**
	 * The boundary at which results are no allowed to be exported
	 */
	private static final int RESULT_MASK_BOUNDARY = 5;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Retrieves query results
	 */
	@Autowired
	private ResultDataService resultService;

	/**
	 * Audit service for logging export requests.
	 */
	@Autowired
	private AuditService auditService;

	/**
	 * Retrieves information about the query
	 */
	@Autowired
	private QueryContextService queryContextService;

	/**
	 * A key-value pair of exporters which fulfill a given {@link ExportFormat}
	 */
	@Resource(name = "exporters")
	private Map<ExportFormat, Exporter> exporters;

	/**
	 * Security service for checking usernames
	 */
	@Autowired
	private SecurityService securityService;

	/**
	 * A list of allowed data source ids that results can be exported. By default, this
	 * list is empty and no data sources are allowed.
	 */
	@Resource(name = "exportWhitelist")
	private final List<String> exportWhitelist = CollectionUtil.newList();

	// ========================= IMPLEMENTATION: ExportService =======

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.export.ExportService#export(edu.utah.further.fqe
	 * .api.export.ExportFormat, edu.utah.further.fqe.api.export.ExportContext)
	 */
	@Override
	public <F> F export(final ExportFormat format, final ExportContext exportContext)
	{
		// Update the username from a unid to a federated username
		final Long federatedUsername = securityService.getFederatedUsernameByAlias(
				"USERNAME", exportContext.getUserId());
		exportContext.setUserId(federatedUsername.toString());

		auditService.logExportRequest(exportContext);

		if (exportContext == null || exportContext.getQueryId() == null
				|| exportContext.getUserId() == null)
		{
			throw new ApplicationException(INVALID_EXPORT_CONTEXT,
					"Invalid ExportContext");
		}

		// Get the query
		final QueryContext queryContext = queryContextService
				.findQueryContextWithOriginId(exportContext.getQueryId());

		if (queryContext == null)
		{
			throw new ApplicationException(QUERY_NOT_EXIST,
					"Query does not exist. Queries only exist "
							+ "temporarily for data security reasons");
		}

		// Simple security check, not exhaustive
		if (!Long.valueOf(queryContext.getUserId()).equals(federatedUsername))
		{
			throw new AuthorizationException("Username set in ExportContext ("
					+ securityService.getFederatedUsernameByAlias("USERNAME",
							exportContext.getUserId())
					+ ") does not equal username for this query ("
					+ queryContext.getUserId() + ")");
		}

		if (queryContext.getResultContext().getNumRecords() != 0
				&& queryContext.getResultContext().getNumRecords() < RESULT_MASK_BOUNDARY)
		{
			throw new ApplicationException(INVALID_RESULTS,
					"Query contains no results or results are less than minimum results.");
		}

		final List<QueryContext> queries = queryContextService
				.findCompletedChildren(queryContext);

		// A list of query ids to get results for
		final List<Object> queryIds = CollectionUtil.newList();

		// Only add query ids that are have a whitelisted data source id
		for (final QueryContext context : queries)
		{
			if (exportWhitelist.contains(context.getDataSourceId()))
			{
				queryIds.add(context.getExecutionId());
			}
		}

		// This is the root object which by default is always fetched and not filtered
		// E.g. you don't want to filter the number of patients you have but you may want
		// to filter which diagnosis they have
		final List<Object> results = resultService.getQueryResults(
				"from " + queryContext.getResultContext().getRootEntityClass()
						+ " where id.datasetId "
						+ SqlUtil.unlimitedInValues(queryIds, "id.datasetId"), queryIds);

		// for (final SearchQuery query : context.getFilters())
		// {
		// // Filter each "root" into a string and then zip all the files together.
		// final List<Object> filteredResults = resultService.getQueryResults(query);
		// formatters.get(format).format(filteredResults, context);
		// }

		return (F) exporters.get(format).format(results, exportContext);
	}

	// ========================= GET/SET METHODS ===========================

	/**
	 * Return the resultService property.
	 * 
	 * @return the resultService
	 */
	public ResultDataService getResultService()
	{
		return resultService;
	}

	/**
	 * Set a new value for the resultService property.
	 * 
	 * @param resultService
	 *            the resultService to set
	 */
	public void setResultService(final ResultDataService resultService)
	{
		this.resultService = resultService;
	}

	/**
	 * Return the queryContextService property.
	 * 
	 * @return the queryContextService
	 */
	public QueryContextService getQueryContextService()
	{
		return queryContextService;
	}

	/**
	 * Set a new value for the queryContextService property.
	 * 
	 * @param queryContextService
	 *            the queryContextService to set
	 */
	public void setQueryContextService(final QueryContextService queryContextService)
	{
		this.queryContextService = queryContextService;
	}

	/**
	 * Return the exporters property.
	 * 
	 * @return the exporters
	 */
	public Map<ExportFormat, Exporter> getExporters()
	{
		return exporters;
	}

	/**
	 * Set a new value for the exporters property.
	 * 
	 * @param exporters
	 *            the exporters to set
	 */
	public void setExporters(final Map<ExportFormat, Exporter> exporters)
	{
		this.exporters = exporters;
	}

	/**
	 * Return the securityService property.
	 * 
	 * @return the securityService
	 */
	public SecurityService getSecurityService()
	{
		return securityService;
	}

	/**
	 * Set a new value for the securityService property.
	 * 
	 * @param securityService
	 *            the securityService to set
	 */
	public void setSecurityService(final SecurityService securityService)
	{
		this.securityService = securityService;
	}

	/**
	 * Return the exportWhitelist property.
	 * 
	 * @return the exportWhitelist
	 */
	public List<String> getExportWhitelist()
	{
		return exportWhitelist;
	}

}
