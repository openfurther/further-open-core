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
package edu.utah.further.fqe.ws;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.ErrorCode.INVALID_RESULTS;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.service.export.ExportService;
import edu.utah.further.fqe.api.service.query.AggregationService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.api.ws.FqeServiceRest;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultsTo;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.ExportFormat;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.QueryType;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ExportContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextIdentifierTo;
import edu.utah.further.fqe.ds.api.to.QueryContextStateTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.StatusMetaDataToImpl;
import edu.utah.further.fqe.ds.api.to.StatusesMetaDataToImpl;
import edu.utah.further.fqe.ds.api.to.plan.ExecutionRuleToImpl;
import edu.utah.further.fqe.ds.api.to.plan.PlanToImpl;
import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;
import edu.utah.further.i2b2.query.model.I2b2FurtherConfigTo;
import edu.utah.further.i2b2.query.model.I2b2QueryTo;
import edu.utah.further.security.api.services.SecurityService;

/**
 * A RESTful web service that exposes internal services. The <code>etc/fqe-client</code>
 * bash script in this maven module is a CLI example of using this class after it is
 * deployed to the FUSE ESB.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 29, 2009
 */
@Service("fqeServiceRest")
public class FqeServiceRestImpl implements FqeServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(FqeServiceRestImpl.class);

	// ========================= DEPENEDENCIES =============================

	/**
	 * Converts i2b2 queries to FURTHeR logical queries.
	 */
	private QueryContextService queryContextService;

	/**
	 * Executes FQE business operations.
	 */
	private FqeService fqeService;

	/**
	 * Creates and manages query plans.
	 */
	private AggregationService aggregationService;

	/**
	 * Converts i2b2 queries to FURTHeR logical queries.
	 */
	private I2b2QueryService i2b2QueryService;

	/**
	 * Marshals/unmarshals to/from XML.
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * Security service for getting the federated username.
	 */
	@Autowired
	private SecurityService securityService;

	/**
	 * Data exporter service
	 */
	@Autowired
	private ExportService exportService;

	// ========================= METHODS ===================================

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing the federated query front-end web service");
		}
	}

	/**
	 * @throws Exception
	 */
	@PreDestroy
	public void destroy()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Shutting down the federated query front-end web service");
		}
	}

	// ========================= IMPLEMENTATION: FqeServiceRest ============

	/**
	 * Send a status request to all data sources.
	 * 
	 * @return aggregated result set containing a list of meta data objects for each of
	 *         the registered data sources
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#status()
	 */
	@Override
	public Data status()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Requesting status from all data sources ");
		}
		return fqeService.status();
	}

	/**
	 * @param dataSourceId
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#status(java.lang.String)
	 */
	@Override
	public Data status(final String dataSourceId)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Requesting status from data source with ID " + dataSourceId);
		}
		return fqeService.status(dataSourceId);
	}

	/**
	 * Trigger the execution of a search query against this data source.
	 * 
	 * @param logicalQueryContext
	 *            a FURTHeR logical query
	 * @return an acknowledgment object that thereafter tracks the query status
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#stopQuery(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public QueryContextToImpl triggerQuery(final QueryContextToImpl logicalQueryContext)
	{
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("triggerQuery(query context = " + logicalQueryContext + ")");
				final String queryContextXml = xmlService.marshal(
						logicalQueryContext,
						xmlService
								.options()
								.setFormat(true)
								.setClasses(QueryContextToImpl.class));
				log.debug("QueryContext XML:" + NEW_LINE_STRING + queryContextXml);
			}

			// Must have query XML field set to be able to save and trigger query
			// final String searchQueryXml = xmlService.marshal(logicalQueryContext
			// .getQuery());
			// logicalQueryContext.setQueryXml(searchQueryXml);

			QueryContext queryContext = QueryContextToImpl
					.newCopyWithGeneratedExecutionId(logicalQueryContext);
			queryContext = fqeService.triggerQuery(queryContext);
			// Trigger the query and return a TO copy of it
			return QueryContextToImpl.newCopy(queryContext);
		}
		catch (final Exception e)
		{
			// TODO: Notify the appropriate component that queuing this query failed
			throw new ApplicationException(ErrorCode.INVALID_INPUT_ARGUMENT_VALUE,
					"Invalid query", e);
		}
	}

	/**
	 * An i2b2 adapter: trigger the execution of an i2b2 query against this data source.
	 * The i2b2 query is converted to a {@link SearchQuery} and then
	 * {@link #stopQuery(SearchQuery)} is invoked.
	 * 
	 * @param i2b2Query
	 *            an i2b2 query
	 * @return an acknowledgment object that thereafter tracks the query status
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#triggerQueryFromI2b2(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public QueryContextIdentifierTo triggerQueryFromI2b2(final I2b2QueryTo i2b2Query)
	{
		return triggerQueryFromI2b2(i2b2Query, null, null);
	}

	/**
	 * @param i2b2Query
	 * @param i2b2QueryId
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#triggerQueryFromI2b2WithId(edu.utah.further.i2b2.query.model.I2b2QueryTo,
	 *      long)
	 */
	@Override
	public QueryContextIdentifierTo triggerQueryFromI2b2WithId(
			final I2b2QueryTo i2b2Query, final long i2b2QueryId)
	{
		if (log.isDebugEnabled())
		{
			log.debug("triggerQueryFromI2b2() with i2b2 query ID = " + i2b2QueryId);
		}
		return triggerQueryFromI2b2(i2b2Query, null, new Long(i2b2QueryId));
	}

	/**
	 * @param rawI2b2Xml
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#triggerQueryFromI2b2Raw(java.lang.String)
	 */
	@Override
	public QueryContextIdentifierTo triggerQueryFromI2b2Raw(final String rawI2b2Xml)
	{
		return triggerQueryFromI2b2Raw(rawI2b2Xml, null);
	}

	/**
	 * @param rawI2b2Xml
	 * @param i2b2QueryId
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#triggerQueryFromI2b2Raw(java.lang.String,
	 *      long)
	 */
	@Override
	public QueryContextIdentifierTo triggerQueryFromI2b2Raw(final String rawI2b2Xml,
			final long i2b2QueryId)
	{
		return triggerQueryFromI2b2Raw(rawI2b2Xml, new Long(i2b2QueryId));
	}

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#stopQuery(long)
	 */
	@Override
	public Response stopQuery(final long id)
	{
		if (log.isDebugEnabled())
		{
			log.debug("stopQuery(" + id + ")");
		}

		final QueryContext queryContext = queryContextService.findById(new Long(id));
		if (queryContext == null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		else if (queryContext.isStopped())
		{
			return Response.status(Status.NOT_MODIFIED).build();
		}
		else
		{
			fqeService.stopQuery(queryContext);
			return Response.status(Status.ACCEPTED).build();
		}
	}

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#deleteQuery(long)
	 */
	@Override
	public Response deleteQuery(final long id)
	{
		final QueryContext queryContext = queryContextService.findById(new Long(id));
		if (queryContext == null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		if (queryContext.isStarted())
		{
			// This will also stop children queries
			queryContextService.stop(queryContext);
			return Response.status(Status.NOT_MODIFIED).build();
		}
		queryContextService.delete(queryContext);
		return Response.status(Status.ACCEPTED).build();
	}

	/**
	 * @param userId
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#deleteQueriesByUser(java.lang.String)
	 */
	@Override
	public Response deleteQueriesByUser(final String userId)
	{
		queryContextService.deleteByUser(userId);
		return Response.status(Status.ACCEPTED).build();
	}

	/**
	 * Stops all running queries and deletes all queries.
	 * 
	 * @return acknowledgment code
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#deleteAllQueries()
	 */
	@Override
	public Response deleteAllQueries()
	{
		queryContextService.deleteAll();
		return Response.status(Status.ACCEPTED).build();
	}

	/**
	 * Return the list of queries, wrapped by a parent QC object.
	 * 
	 * @return query context containing the list of query contexts in the system
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queries()
	 */
	@Override
	public QueryContextToImpl queries()
	{
		final QueryContextToImpl result = QueryContextToImpl.newInstance();
		result.addChildren(queryContextService.findAll());
		return result;
	}

	/**
	 * Return the list of queries submitted by a specified user, wrapped by a parent QC
	 * object.
	 * 
	 * @return query context containing the list of query contexts of a specified user
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queriesByUser()
	 */
	@Override
	// public QueryContextToImpl queriesByUser(final String currentUserId)
	// {
	// final QueryContextToImpl result = QueryContextToImpl.newInstance();
	//
	// // Only return queries submitted by the current user
	// final List<QueryContextTo> allQueries = queries().getChildren();
	// final Set<Long> parentIds = new HashSet<Long>();
	// for (final QueryContextTo child : allQueries)
	// {
	// if (child.getParentId() == null && currentUserId.equals(child.getUserId()))
	// {// Parent query
	// parentIds.add(child.getId());
	// result.addChild(child);
	// }
	// }
	// for (final QueryContextTo child : allQueries)
	// {
	// // The code below does not work since the parent query element does not refer
	// // to the children elements, only the other way.
	// // result.addChildren(element.getChildren());
	// if (parentIds.contains(child.getParentId()))
	// result.addChild(child);
	// }
	// return result;
	// }
	public QueryContextToImpl queriesByUser(final String currentUserId)
	{
		final QueryContextToImpl result = QueryContextToImpl.newInstance();
		result.addChildren(queryContextService.findAllByUser(currentUserId));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryStatuses()
	 */
	@Override
	public StatusesMetaDataToImpl queryStatuses()
	{
		final List<StatusMetaData> statuses = queryContextService.findAllStatuses();
		return wrapStatuses(statuses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryStatusesById(long)
	 */
	@Override
	public StatusesMetaDataToImpl queryStatusesById(final long id)
	{
		final List<StatusMetaData> statuses = queryContextService
				.findAllStatuses(new Long(id));
		return wrapStatuses(statuses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryStatusesByDate(java.util.Date)
	 */
	@Override
	public StatusesMetaDataToImpl queryStatusesByDate(final Date date)
	{
		final List<StatusMetaData> statuses = queryContextService
				.findAllStatusesLaterThan(date);
		return wrapStatuses(statuses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryStatus(long)
	 */
	@Override
	public StatusMetaDataToImpl queryStatus(final long id)
	{
		return StatusMetaDataToImpl.newCopy(queryContextService
				.findCurrentStatusById(new Long(id)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryStateById(long)
	 */
	@Override
	public QueryContextStateTo queryStateById(final long id)
	{
		final QueryContext queryContext = queryContextService.findById(new Long(id));
		final QueryContextStateTo queryContextState = new QueryContextStateTo();
		queryContextState.setState(queryContext == null ? QueryState.INVALID
				: queryContext.getState());
		return queryContextState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#queryById(long)
	 */
	@Override
	public QueryContextToImpl queryById(final long id)
	{
		final QueryContext queryContext = queryContextService.findById(new Long(id));
		final QueryContextToImpl qcTo = QueryContextToImpl.newCopy(queryContext);

		// Find DQCs and copy them to the TO, as they are not loaded by findById()
		final List<QueryContext> children = queryContextService
				.findChildren(queryContext);
		if (children != null)
		{
			qcTo.addChildren(children);
		}
		return qcTo;
	}

	/**
	 * @param dataSourceId
	 * @param newState
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#updateState(java.lang.String,
	 *      edu.utah.further.fqe.ds.api.domain.DsState)
	 */
	@Override
	public DsMetaData updateState(final String dataSourceId, final DsState newState)
	{
		return fqeService.updateState(dataSourceId, newState);
	}

	/**
	 * Return a result set of aggregated counts (broken down by demographic categories)
	 * for a query with a specified origin ID (e.g. i2b2 ID). Includes all federated join
	 * types.
	 * 
	 * @param id
	 *            federated query ID
	 * @return an object holding histograms of aggregated counts for each of several
	 *         demographic category
	 */
	@Override
	public AggregatedResultsTo aggregatedResultsById(final long id)
	{
		final QueryContext queryContext = queryContextService.findById(new Long(id));
		return aggregatedResults(queryContext);
	}

	/**
	 * @param originId
	 * @return
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#aggregatedResultsByOriginId(long)
	 */
	@Override
	public AggregatedResultsTo aggregatedResultsByOriginId(final long originId)
	{
		final QueryContext queryContext = queryContextService
				.findQueryContextWithOriginId(new Long(originId));
		return aggregatedResults(queryContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.ws.FqeServiceRest#export(edu.utah.further.fqe.api.export
	 * .ExportFormat, edu.utah.further.fqe.api.export.ExportContext)
	 */
	@Override
	public Response export(final ExportFormat format, final ExportContextTo exportContext)
			throws WsException
	{
		switch (format)
		{
			case CSV:
			{
				String data = null;

				try
				{
					data = exportService.export(format, exportContext);
				}
				catch (final Exception e)
				{
					if (ReflectionUtil.classIsInstanceOf(ApplicationException.class,
							e.getClass()))
					{
						final ApplicationException ae = (ApplicationException) e;
						if (ae.getCode() != null)
						{
							throw new WsException(ae.getCode(), ae.getMessage());
						}
					}

					throw new WsException(e.getMessage());
				}

				if (data == null)
				{
					throw new WsException(INVALID_RESULTS, "No data to export.");
				}

				return Response
						.ok(data)
						.header("Content-Disposition",
								"attachment; filename=further-export-"
										+ new SimpleDateFormat("dd-MM-yy-HH-mm-ss")
												.format(new Date()) + ".csv")
						.build();
			}
			default:
			{
				throw new WsException("Unsupported export format " + format);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.ws.FqeServiceRest#export(edu.utah.further.fqe.ds.api.domain
	 * .ExportFormat, java.lang.String)
	 */
	@Override
	public Response export(final ExportFormat format, final String exportContext)
			throws WsException
	{
		ExportContextTo exportContextTo = null;
		try
		{
			exportContextTo = xmlService.unmarshal(exportContext, ExportContextTo.class);
		}
		catch (final JAXBException e)
		{
			throw new ApplicationException("Invalid ExportContext XML");
		}

		if (exportContext == null)
		{
			// Shouldn't get here but just in case
			throw new ApplicationException("Problem resolving ExportContext");
		}

		return export(format, exportContextTo);
	}

	// ========================= DEPENDENCY INJECTION ====================

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
	 * Set a new value for the fqeService property.
	 * 
	 * @param fqeService
	 *            the fqeService to set
	 */
	public void setFqeService(final FqeService fqeService)
	{
		this.fqeService = fqeService;
	}

	/**
	 * Set a new value for the aggregationService property.
	 * 
	 * @param aggregationService
	 *            the aggregationService to set
	 */
	public void setAggregationService(final AggregationService aggregationService)
	{
		this.aggregationService = aggregationService;
	}

	/**
	 * Set a new value for the i2b2QueryService property.
	 * 
	 * @param i2b2QueryService
	 *            the i2b2QueryService to set
	 */
	public void setI2b2QueryService(final I2b2QueryService i2b2QueryService)
	{
		this.i2b2QueryService = i2b2QueryService;
	}

	// ========================= PRIVATE METHODS =========================

	/**
	 * Private helper method to wrap statuses with wrapper transfer objects. Also sets the
	 * returned statuses to point to the federated {@link QueryContext} parent instead of
	 * child contexts.
	 * 
	 * @param statuses
	 * @return statuses result set
	 */
	private StatusesMetaDataToImpl wrapStatuses(final List<StatusMetaData> statuses)
	{
		final List<StatusMetaDataToImpl> statusesTos = newList();
		for (final StatusMetaData status : statuses)
		{
			final StatusMetaDataToImpl wrappedStatus = StatusMetaDataToImpl
					.newCopy(status);
			final QueryContext parent = status.getQueryContext().getParent();
			if (parent != null)
			{
				wrappedStatus.setQueryContext(parent);
			}
			statusesTos.add(wrappedStatus);
		}
		return new StatusesMetaDataToImpl(statusesTos);
	}

	/**
	 * Trigger the execution of an i2b2 query against one or more data sources specified
	 * in the FURTHeR configuration object. The i2b2 query is converted to a
	 * {@link SearchQuery} and then {@link #stopQuery(SearchQuery)} is invoked.
	 * 
	 * @param i2b2Query
	 *            an i2b2 query
	 * @param I2b2FurtherConfigTo
	 *            FURTHeR configuration object populated by the i2b2 front end
	 * @return an acknowledgment object that thereafter tracks the query status
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#triggerQueryFromI2b2(edu.utah.further.core.query.domain.SearchQuery)
	 */
	private QueryContextIdentifierTo triggerQueryFromI2b2(final I2b2QueryTo i2b2Query,
			final I2b2FurtherConfigTo furtherConfig, final Long i2b2QueryId)
	{
		// Convert query to our format

		final QueryContextToImpl queryContext = toLogicalQuery(i2b2Query, furtherConfig);
		queryContext.setOriginId(i2b2QueryId);

		// Convert UNID to federated username
		final Long federatedUsername = securityService.getFederatedUsernameByAlias(
				"USERNAME", queryContext.getUserId());

		if (federatedUsername == null)
		{
			throw new AccessDeniedException("Unable to find federated username for unid "
					+ queryContext.getUserId());
		}

		queryContext.setUserId(String.valueOf(federatedUsername));

		// Run query
		final QueryContext context = triggerQuery(queryContext);

		// Form acknowledgment object
		final QueryContextIdentifierTo identifierTo = new QueryContextIdentifierTo();
		identifierTo.setId(context.getId());
		return identifierTo;
	}

	/**
	 * @param rawI2b2Xml
	 * @param i2b2QueryId
	 * @return
	 */
	private QueryContextIdentifierTo triggerQueryFromI2b2Raw(final String rawI2b2Xml,
			final Long i2b2QueryId)
	{
		if (log.isDebugEnabled())
		{
			log.debug("triggerQueryFromI2b2Raw() i2b2QueryId " + i2b2QueryId);
			log.debug("rawI2b2Xml:" + Strings.NEW_LINE_STRING + rawI2b2Xml);
		}
		final String i2b2QueryXml = i2b2QueryService.toI2b2Query(rawI2b2Xml);

		try
		{
			final I2b2FurtherConfigTo furtherConfig = xmlService.unmarshal(rawI2b2Xml,
					I2b2FurtherConfigTo.class);
			final I2b2QueryTo i2b2Query = xmlService.unmarshal(
					StringUtil.stripNewLinesAndTabs(i2b2QueryXml), I2b2QueryTo.class);
			return triggerQueryFromI2b2(i2b2Query, furtherConfig, i2b2QueryId);
		}
		catch (final JAXBException e)
		{
			throw new ApplicationException(ErrorCode.INVALID_INPUT_ARGUMENT_VALUE,
					"Invalid i2b2 query XML", e);
		}
	}

	/**
	 * An i2b2 object adapter: trigger the execution of an i2b2 query against this data
	 * source. The i2b2 query is converted to a {@link SearchQuery} and then
	 * {@link #stopQuery(SearchQuery)} is invoked.
	 * 
	 * @param i2b2Query
	 * @param furtherConfig
	 * @return
	 */
	private QueryContextToImpl toLogicalQuery(final I2b2QueryTo i2b2Query,
			final I2b2FurtherConfigTo furtherConfig)
	{
		if (log.isDebugEnabled())
		{
			try
			{
				final String queryContextXml = xmlService.marshal(i2b2Query, xmlService
						.options()
						.setFormat(true));
				log.debug("triggerQueryFromI2b2()");
				log.debug("I2b2Query XML:" + Strings.NEW_LINE_STRING + queryContextXml);
			}
			catch (final JAXBException e)
			{
				throw new ApplicationException(ErrorCode.INVALID_INPUT_ARGUMENT_VALUE,
						"Invalid i2b2 query", e);
			}
		}

		// Translate search criteria
		final SearchQuery searchQuery = i2b2QueryService.getAsSearchQuery(i2b2Query);
		final Long qid = new Long(1);
		searchQuery.setId(qid);

		// Translate/set query meta data
		final QueryContextToImpl queryContext = QueryContextToImpl
				.newInstanceWithExecutionId();
		// queryContext.setUserId(I2B2_USER_PREFIX + i2b2Query.getUserId());
		// The i2b2 user id is the same as the uNID
		queryContext.setUserId(i2b2Query.getUserId());
		queryContext.setQuery(searchQuery);

		final Plan plan = new PlanToImpl();
		if (furtherConfig != null)
		{
			final List<String> dataSources = furtherConfig.getDatasources();

			if (dataSources.size() == 0)
			{
				throw new ApplicationException(ErrorCode.INVALID_INPUT_ARGUMENT_VALUE,
						"Invalid i2b2 query, no data sources specified");
			}

			if (!dataSources.get(0).equals(I2B2_ALL_DS))
			{
				int ds = 0;
				for (final String dataSource : dataSources)
				{
					final ExecutionRule executionRule = new ExecutionRuleToImpl();
					executionRule.setId(UUID.randomUUID().toString());
					executionRule.setDataSourceId(dataSource);
					executionRule.setSearchQueryId(qid);
					plan.addExecutionRule(executionRule);
					ds++;
				}
				queryContext.setMaxRespondingDataSources(ds);
				queryContext.setMinRespondingDataSources(ds);
			}
			else
			{
				// If DS = ALL, must be size 1
				Validate.isTrue(dataSources.size() == 1);
			}

			final Long associatedOrginId = furtherConfig.getAssociatedResult();
			if (associatedOrginId != null)
			{
				final QueryContext assocQueryContext = queryContextService
						.findQueryContextWithOriginId(associatedOrginId);
				if (assocQueryContext == null)
				{
					throw new ApplicationException(
							ErrorCode.INVALID_INPUT_ARGUMENT_VALUE,
							"Unable to find querycontext with orgin id of "
									+ associatedOrginId);
				}
				queryContext.setAssociatedResult(assocQueryContext);
			}

			if ("DATA_QUERY".equals(furtherConfig.getQueryType()))
			{
				queryContext.setQueryType(QueryType.DATA_QUERY);
			}
			else if ("COUNT_QUERY".equals(furtherConfig.getQueryType()))
			{
				queryContext.setQueryType(QueryType.COUNT_QUERY);
			}
			else
			{
				throw new ApplicationException("Unknow query type "
						+ furtherConfig.getQueryType());
			}

		}
		queryContext.setPlan(plan);

		return queryContext;
	}

	/**
	 * @param federatedQueryContext
	 * @return
	 */
	private AggregatedResultsTo aggregatedResults(final QueryContext federatedQueryContext)
	{
		if (federatedQueryContext == null)
		{
			// Query not found, return empty object
			return AggregatedResultsTo.EMPTY_INSTANCE;
		}
		// Build histograms (counts broken down by demographic categories)
		final AggregatedResults result = aggregationService
				.generateAggregatedResults(federatedQueryContext);

		// Attach federated join total counts
		final QueryContextToImpl qcTo = QueryContextToImpl.newCopy(federatedQueryContext);
		final Map<ResultType, ResultContext> resultViews = qcTo.getResultViews();
		if (result.getNumDataSources() == 1)
		{
			// Single data source, retain only the SUM view
			final ResultContext sumView = resultViews.get(ResultType.SUM);
			result.setResultViews(Collections.<ResultType, ResultContext> singletonMap(
					ResultType.SUM, sumView));
		}
		else
		{
			result.setResultViews(resultViews);
		}

		// Scrub small counts
		final AggregatedResults scrubbedResult = aggregationService
				.scrubResults(result);

		// More generally, one would deep-copy the domain object implementation of
		// AggregationResult into the TO. Here we know they're the same.
		return (AggregatedResultsTo) scrubbedResult;
	}
}
