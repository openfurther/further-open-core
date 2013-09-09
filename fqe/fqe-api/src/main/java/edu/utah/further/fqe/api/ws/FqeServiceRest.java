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
package edu.utah.further.fqe.api.ws;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultTo;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultsTo;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.ExportFormat;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ExportContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextIdentifierTo;
import edu.utah.further.fqe.ds.api.to.QueryContextStateTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.StatusMetaDataToImpl;
import edu.utah.further.fqe.ds.api.to.StatusesMetaDataToImpl;
import edu.utah.further.i2b2.query.model.I2b2Query;
import edu.utah.further.i2b2.query.model.I2b2QueryTo;

/**
 * FQE RESTful web service interface.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jun 29, 2009
 */
@Path("/fqe")
@Produces("application/xml")
@Documentation(name = "FQE WS-REST", description = "Restful federated query engine web service")
public interface FqeServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * A prefix appended to i2b2 queries before they are sent to the FQE for processing.
	 */
	// String I2B2_USER_PREFIX = "i2b2:";

	String I2B2_ALL_DS = "ALL";

	// ========================= METHODS: Query CRUD Operations ============

	/**
	 * Trigger the execution of a search query against this data source.
	 * 
	 * @param logicalQueryContext
	 *            a FURTHeR logical query context. Contains the search criteria, user
	 *            credentials, etc.
	 * @return an updated query context with the query state, updated right after its
	 *         submission (not at the end of its execution)
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/trigger/logical")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Trigger query", description = "Triggers a query flow from a logical query object.")
	QueryContextToImpl triggerQuery(
			@Documentation(description = "FURTHeR logical query context XML") QueryContextToImpl logicalQueryContext);

	/**
	 * An i2b2 adapter: trigger the execution of an i2b2 query against this data source.
	 * The i2b2 query is converted to a {@link SearchQuery}, which is placed in a new
	 * instance of {@link QueryContextToImpl}. Then
	 * {@link #triggerQuery(QueryContextToImpl)} is invoked.
	 * 
	 * @param i2b2Query
	 *            an i2b2 query
	 * @return a query context with the query state, updated right after its submission
	 *         (not at the end of its execution)
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/trigger/i2b2")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Trigger query from i2b2", description = "Triggers a query flow from a i2b2 query object.")
	QueryContextIdentifierTo triggerQueryFromI2b2(
			@Documentation(description = "i2b2 query XML") I2b2QueryTo i2b2Query);

	/**
	 * An i2b2 adapter: trigger the execution of an i2b2 query against this data source.
	 * The i2b2 query is converted to a {@link SearchQuery}, which is placed in a new
	 * instance of {@link QueryContextToImpl}. Then
	 * {@link #triggerQuery(QueryContextToImpl)} is invoked.
	 * <p>
	 * Additionally, the i2b2 query ID is passed in, so that subsequent fqe-ws calls from
	 * the i2b2 front end to fetch results sets can refer to it.
	 * 
	 * 
	 * @param i2b2Query
	 *            an i2b2 query (POST body)
	 * @param i2b2QueryId
	 *            i2b2 query id
	 * @return a query context with the query state, updated right after its submission
	 *         (not at the end of its execution)
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/trigger/i2b2/{id}")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Trigger query from i2b2", description = "Triggers a query flow from a i2b2 query object.")
	QueryContextIdentifierTo triggerQueryFromI2b2WithId(
			@Documentation(description = "i2b2 query XML") I2b2QueryTo i2b2Query,
			@PathParam("id") @Documentation(description = "Query context ID") long i2b2QueryId);

	/**
	 * An i2b2 adapter: trigger the execution of a query from raw i2b2 XML message against
	 * this data source. The XML is first converted to {@link I2b2Query}, and then to a
	 * {@link SearchQuery}, which is placed in a new instance of
	 * {@link QueryContextToImpl}. Then {@link #triggerQuery(QueryContextToImpl)} is
	 * invoked.
	 * 
	 * @param rawI2b2Xml
	 *            i2b2 message XML
	 * @return a query context with the query state, updated right after its submission
	 *         (not at the end of its execution)
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/trigger/i2b2/raw")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Trigger query from raw i2b2 message", description = "Triggers a query flow from a raw i2b2 message.")
	QueryContextIdentifierTo triggerQueryFromI2b2Raw(
			@Documentation(description = "i2b2 query XML") String rawI2b2Xml);

	/**
	 * An i2b2 adapter: trigger the execution of a query from raw i2b2 XML message against
	 * this data source. The XML is first converted to {@link I2b2Query}, and then to a
	 * {@link SearchQuery}, which is placed in a new instance of
	 * {@link QueryContextToImpl}. Then {@link #triggerQuery(QueryContextToImpl)} is
	 * invoked.
	 * 
	 * @param rawI2b2Xml
	 *            i2b2 message XML
	 * @param i2b2QueryId
	 *            i2b2 query id
	 * @return a query context with the query state, updated right after its submission
	 *         (not at the end of its execution)
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/trigger/i2b2/raw/{id}")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Trigger query from raw i2b2 message", description = "Triggers a query flow from a raw i2b2 message.")
	QueryContextIdentifierTo triggerQueryFromI2b2Raw(
			@Documentation(description = "i2b2 query XML") String rawI2b2Xml,
			@PathParam("id") @Documentation(description = "Query context ID") long i2b2QueryId);

	/**
	 * Stop a query. Has an effect only if the query is running.
	 * 
	 * @param id
	 *            query context ID
	 * @return query stopping response
	 */
	@GET
	@Path("/query/stop/{id}")
	@ExamplePath("query/stop/1")
	@Documentation(name = "Stop query", description = "Stops a query. Query must be running.")
	Response stopQuery(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

	/**
	 * Stops all running queries, and deletes all queries.
	 * 
	 * @return acknowledgment code
	 * @see edu.utah.further.fqe.api.ws.FqeServiceRest#deleteAllQueries()
	 */
	@GET
	@Path("/query/delete")
	@ExamplePath("query/delete")
	@Documentation(name = "Stop and delete all queries", description = "Stops all running queries and deletes all queries.")
	Response deleteAllQueries();

	/**
	 * Stop and delete all queries submitted by a user.
	 * 
	 * @param userId
	 *            unique user ID
	 * @return query deletion response
	 */
	@GET
	@Path("/query/delete/user/{userId}")
	@ExamplePath("query/deleteQueriesByUser/u1234567")
	@Documentation(name = "Delete all queries from a user", description = "Deletes all queries from the current user. Query must not be running???")
	Response deleteQueriesByUser(
			@PathParam("userId") @Documentation(description = "user ID") String userId);

	/**
	 * Delete a query. If the query is running, this method will first stop the query and
	 * then delete it.
	 * 
	 * @param id
	 *            query context ID
	 * @return query cancellation response
	 */
	@GET
	@Path("/query/delete/{id}")
	@ExamplePath("query/delete/1")
	@Documentation(name = "Delete query", description = "Deletes a query. Query must not be running.")
	Response deleteQuery(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

	// ========================= METHODS: Query Status Reports =============

	/**
	 * Return the list of queries, wrapped by a parent QC object.
	 * 
	 * @return query context containing the list of query contexts in the system
	 */
	@GET
	@Path("/query/list")
	@ExamplePath("query/list")
	@Documentation(name = "List queries", description = "Return the list of query contexts in the system, wrapped within a dummy parent query context object.")
	QueryContextToImpl queries();

	/**
	 * Return the list of queries by a specified user, wrapped by a parent QC object.
	 * 
	 * @return query context containing the list of query contexts from a specified user
	 */
	@GET
	@Path("/query/list/{currentUserId}")
	@ExamplePath("query/list/u1234567")
	@Documentation(name = "List queries by user", description = "Return the list of query contexts by user in the system, wrapped within a dummy parent query context object.")
	QueryContextToImpl queriesByUser(
			@PathParam("currentUserId") @Documentation(description = "Current User ID") String currentUserId);

	/**
	 * Return a list of ALL statuses. Plural of status:
	 * http://www.merriam-webster.com/dictionary/status
	 * 
	 * @return statuses
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/statuses")
	@ExamplePath("query/statuses")
	@Documentation(name = "Intermediate & final status results of all data queries", description = "Returns all status results")
	StatusesMetaDataToImpl queryStatuses();

	/**
	 * Return a list of ALL statuses for a given query context id.
	 * 
	 * @return all statuses for the given query context id
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/statuses/id/{id}")
	@ExamplePath("query/statuses/id/1")
	@Documentation(name = "Intermediate & final status results of a data query by query context id", description = "Returns all status results for the given id")
	StatusesMetaDataToImpl queryStatusesById(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

	/**
	 * Return a list of ALL statuses past a given date
	 * 
	 * @return all statuses for the given query context id
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/statuses/date/{date}")
	@ExamplePath("query/statuses/date/1969-12-31T17:00:10-07:00")
	@Documentation(name = "Intermediate & final status results of data query past a given date", description = "Returns all status results past the given date")
	StatusesMetaDataToImpl queryStatusesByDate(
			@PathParam("date") @Documentation(description = "Date") Date date);

	/**
	 * Get the query state of the given id
	 * 
	 * @return the current state
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/state/id/{id}")
	@ExamplePath("query/state/id/1")
	@Documentation(name = "State of a query (e.g. EXECUTING, FAILED, RUNNING, etc)", description = "Returns the state of the given query identifier")
	QueryContextStateTo queryStateById(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

	/**
	 * Return the current status for a given QueryContext id
	 * 
	 * @return status
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/status/{id}")
	@ExamplePath("query/status/1")
	@Documentation(name = "Current status of data query for a given query context id", description = "Returns the current status for given query context id")
	StatusMetaDataToImpl queryStatus(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

	/**
	 * Get query context by id
	 * 
	 * @return the query context represented by id
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/id/{id}")
	@ExamplePath("query/id/1")
	@Documentation(name = "Query context by identifier", description = "Returns the query context given by the query identifier")
	QueryContextToImpl queryById(
			@PathParam("id") @Documentation(description = "Query context ID") long id);

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
	@GET
	@Produces("application/xml")
	@Path("/query/count/{id}")
	@ExamplePath("query/count/3")
	@Documentation(name = "Update data source activation state", description = "Demographic histograms for a federated query.")
	AggregatedResultsTo aggregatedResultsById(
			@PathParam("id") @Documentation(description = "Federated Query ID") long id);

	/**
	 * Return a result set of aggregated counts (broken down by demographic categories)
	 * for a query with a specified origin ID (e.g. i2b2 ID). Includes all federated join
	 * types.
	 * 
	 * @param originId
	 *            query origin ID (e.g. i2b2 query ID)
	 * @return an object holding histograms of aggregated counts for each of several
	 *         demographic category
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/count/origin/{originId}")
	@ExamplePath("query/count/origin/2416")
	@Documentation(name = "Update data source activation state", description = "Demographic histograms for an i2b2 query or another remote query.")
	AggregatedResultsTo aggregatedResultsByOriginId(
			@PathParam("originId") @Documentation(description = "Query origin ID") long originId);

	/**
	 * Return a result set of aggregated counts (broken down by demographic categories)
	 * for a query with a specified origin ID (e.g. i2b2 ID).
	 * 
	 * @param originId
	 *            query origin ID (e.g. i2b2 query ID)
	 * @param resultType
	 *            federated join type (<code>SUM/INTERSECTION/...</code>)
	 * @param intersectionIndex
	 *            intersection index (required for <code>INTERSECTION</code>)
	 * @return an object holding histograms of aggregated counts for each of several
	 *         demographic category
	 */
	@GET
	@Produces("application/xml")
	@Path("/query/count/origin/{resultType}/{intersectionIndex}/{originId}")
	@ExamplePath("query/count/origin/INTERSECTION/1/2416")
	@Documentation(name = "Update data source activation state", description = "A single demographic histogram for an i2b2 query or another remote query.")
	AggregatedResultTo aggregatedResultByOriginId(
			@PathParam("originId") @Documentation(description = "Query origin ID") long originId,
			@PathParam("resultType") @Documentation(description = "Federated join type") ResultType resultType,
			@PathParam("intersectionIndex") @Documentation(description = "Federated intersection index") int intersectionIndex);

	// ========================= METHODS: export methods ======

	/**
	 * Export the results of a query, by default including the results of the
	 * "root entity," with additional related data filtered.
	 * 
	 * @param format
	 *            the format of the export
	 * @param exportContext
	 *            the {@link ExportContext} for this export.
	 */
	@POST
	@Consumes(
	{ "application/xml" })
	@Path("/query/export/{format}")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Result export with optional filters", description = "Export the results of a query with some optional level of filtering")
	Response export(
			@PathParam("format") @Documentation(description = "format of the query") ExportFormat format,
			@Documentation(description = "Export context xml with filters") ExportContextTo exportContext) throws WsException;

	/**
	 * Export the results of a query, by default including the results of the
	 * "root entity," with additional related data filtered.
	 * 
	 * @param format
	 *            the format of the export
	 * @param exportContext
	 *            string of ExportContext XML
	 */
	@POST
	@Consumes(
	{ "application/x-www-form-urlencoded" })
	@Path("/query/export/{format}")
	// @ExamplePath not needed - POST method
	@Documentation(name = "Result export with optional filters", description = "Export the results of a query with some optional level of filtering")
	Response export(
			@PathParam("format") @Documentation(description = "format of the query") ExportFormat format,
			@Documentation(description = "Form encoded export context xml with filters") @FormParam("exportContext") String exportContext) throws WsException;

	// ========================= METHODS: remote-control data sources ======

	/**
	 * Request all data sources' status meta data.
	 * 
	 * @return a composite result set containing a list of meta data objects for each of
	 *         the registered data sources
	 */
	@GET
	@Produces("application/xml")
	@Path("/ds/status")
	@ExamplePath("ds/status")
	@Documentation(name = "Data source status", description = "Returns the statuses of all data sources registered with the FQE.")
	Data status();

	/**
	 * Request a single data sources' status meta data.
	 * 
	 * @param dataSourceId
	 *            data source unique identifier, usually its name/standard symbol
	 * @return data source's status meta data object
	 */
	@GET
	@Produces("application/xml")
	@Path("/ds/status/{dataSourceId}")
	@ExamplePath("ds/status/UUEDW")
	@Documentation(name = "Data source status", description = "Returns the status of a single data source registered with the FQE.")
	Data status(
			@PathParam("dataSourceId") @Documentation(description = "Data Source name (unique identifier)") String dataSourceId);

	/**
	 * Requests the data source to enter a new activation state.
	 * 
	 * @param dataSourceId
	 *            data source unique identifier, usually its name/standard symbol
	 * @param newState
	 *            data source state to set
	 * @return updated data source meta data object
	 */
	@GET
	@Produces("application/xml")
	@Path("/ds/update/{dataSourceId}/{newState}")
	@ExamplePath("ds/update/UUEDW/ACTIVE")
	@Documentation(name = "Update data source activation state", description = "Remote control: requests the data source to enter a new activation state.")
	DsMetaData updateState(
			@PathParam("dataSourceId") @Documentation(description = "Data Source name (unique identifier)") String dataSourceId,
			@PathParam("newState") @Documentation(description = "State to set data source to") DsState newState);
}
