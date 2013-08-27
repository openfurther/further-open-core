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
package edu.utah.further.fqe.impl.service.route;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.api.service.query.QueryValidationService;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.FqeEndpointNames;
import edu.utah.further.fqe.ds.api.util.MessageHeader;
import edu.utah.further.fqe.impl.service.plan.QueryPlanService;
import edu.utah.further.fqe.impl.service.route.SynchronousCommandRunner.Builder;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.authentication.PreAuthenticatedFederatedAuthenticationProvider;

/**
 * An FQE service implementation that sends requests to the FQE camel route and either
 * waits for a response on synchronous commands, or triggers-and-forgets asynchronous
 * commands.
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
 * @version Aug 7, 2009
 */
public class FqeServiceImpl implements FqeService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(FqeServiceImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles {@link QueryContext} DAO operations and searches.
	 */
	@Autowired
	private QueryContextService queryContextService;

	/**
	 * Creates and manages query plans.
	 */
	@Autowired
	private QueryPlanService queryPlanService;

	/**
	 * The request endpoint.
	 */
	@EndpointInject(uri = FqeEndpointNames.REQUEST)
	private Endpoint request;

	/**
	 * Used for producing camel messages.
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	/**
	 * Validation service
	 */
	@Autowired
	private QueryValidationService<ValidationRule> queryValidationService;

	/**
	 * An authentication provider
	 */
	@Autowired
	private PreAuthenticatedFederatedAuthenticationProvider<Integer> authenticationProvider;

	// ========================= CONSTRUCTORS ===========================

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing the federated query client");
		}
	}

	/**
	 * Shut down.
	 */
	@PreDestroy
	public void destroy()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Shutting down the federated query client");
		}
	}

	// ========================= IMPLEMENTATION: FqeService ===============

	/**
	 * Send a synchronous status request to all data sources.
	 * 
	 * @see edu.utah.further.fqe.api.service.route.FqeService#status()
	 */
	@Override
	public Data status()
	{
		return newStatusBuilder()
		// TODO: add body & headers here
		// .setHeader(MessageHeader.DATA_SOURCE_ID, dataSourceId)
				.build()
				.run();
	}

	/**
	 * Request a single data sources' status meta data.
	 * 
	 * @param dataSourceId
	 *            data source unique identifier, usually its name/standard symbol
	 * @return data source's status meta data object
	 */
	@Override
	public Data status(final String dataSourceId)
	{
		return newStatusBuilder()
				.setHeader(MessageHeader.DATA_SOURCE_ID, dataSourceId)
				.build()
				.run();
	}

	/**
	 * Trigger the execution of a search query against this data source. This method is
	 * asynchronous and returns immediately after calling. Results are stored in the
	 * virtual repository and can be referenced using the {@link QueryContext#getId()}
	 * identifier to locate QueryContexts which are children of the federated context.
	 * <p>
	 * An asynchronous call.
	 * 
	 * @param logicalQuery
	 *            a FURTHeR logical query context. When passed into this method, this
	 *            should contain the search criteria and other information related to the
	 *            query (e.g. user credentials).
	 * @return an acknowledgment code: the parent {@link QueryContext} with a populated
	 *         identifier
	 * @see edu.utah.further.fqe.ds.api.service.CommandTrigger#deleteQuery(java.lang.Object)
	 */
	@Override
	public QueryContext triggerQuery(final QueryContext logicalQuery)
	{
		// Authenticate this user within the FURTHeR namespace
		authenticationProvider.setContext(new Integer(32769));
		final Authentication authentication = authenticationProvider
				.authenticate(new FederatedAuthenticationToken<Integer>(logicalQuery
						.getUserId()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Save query in database; also marks the query as queued.
		final QueryContext queryContext = queryContextService.queue(logicalQuery);
		if (log.isInfoEnabled())
		{
			log.info("Queued query " + queryContext);
		}
		if (queryValidationService.validateQuery(queryContext))
		{

			// Create and start executing a plan
			queryPlanService.createPlan(queryContext);
		}
		else
		{

			// fail query
			queryContext.fail();
			queryContextService.update(queryContext);
		}

		// Return acknowledgment object
		return queryContext;
	}

	/**
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.route.FqeService#stopQuery(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void stopQuery(final QueryContext queryContext)
	{
		if (queryContext.getParent() == null)
		{
			queryContextService.stop(queryContext);
			// Tell the children to die
			stopQuery(queryContext.getId().longValue(), null);
		}
		else
		{
			// Stop a DQC
			stopQuery(queryContext.getParent().getId().longValue(),
					queryContext.getDataSourceId());
		}
	}

	/**
	 * Sends a synchronous command to a specific data source to update its activation
	 * status.
	 * 
	 * @param dataSourceId
	 * @param newState
	 * @return
	 * @see edu.utah.further.fqe.api.service.route.FqeService#updateState(java.lang.String,
	 *      edu.utah.further.fqe.ds.api.domain.DsState)
	 */
	@Override
	public DsMetaData updateState(final String dataSourceId, final DsState newState)
	{
		// Create a message body with remote control command instructions
		final DsMetaData body = new DsMetaData();
		body.setState(newState);

		// Trigger the command
		final Data result = new SynchronousCommandRunner.Builder<Data>()
				.commandType(CommandType.REMOTE_CONTROL)
				.setHeader(MessageHeader.DATA_SOURCE_ID, dataSourceId)
				.body(body)
				.requestEndpoint(request)
				.producerTemplate(producerTemplate)
				.poll(50L, 2000L, TimeUnit.SECONDS)
				.returnType(Data.class)
				.fallbackValue(newEmptyData())
				.build()
				.run();

		// Result is an aggregation of a single data source's reply; extract the data
		// source's reply and return it
		if (log.isDebugEnabled())
		{
			log.debug("updateState(): received result " + result);
		}
		final List<Data> children = result.getChildren();
		if (children.isEmpty())
		{
			log.warn("updateState(): no result children received");
			return null;
		}
		return (DsMetaData) children.get(0);
	}

	// ========================= PRIVATE METHODS =========================

	/**
	 * @return
	 */
	private Data newEmptyData()
	{
		return new Data();
	}

	/**
	 * @return
	 */
	private DsMetaData newEmptyDsMetaData()
	{
		return new DsMetaData();
	}

	/**
	 * @return
	 */
	private Builder<Data> newStatusBuilder()
	{
		return new SynchronousCommandRunner.Builder<Data>()
				.commandType(CommandType.META_DATA)
				.requestEndpoint(request)
				.producerTemplate(producerTemplate)
				.poll(50L, 2000L, TimeUnit.SECONDS)
				.returnType(Data.class)
				.fallbackValue(new Data())
				.body(newEmptyDsMetaData());
	}

	/**
	 * Sends a cancellation message to a data query life cycle. if the data source
	 * identifier is empty, it's sent to all data sources.
	 * 
	 * @param parentQueryId
	 * @param dataSourceId
	 */
	private void stopQuery(final long parentQueryId, final String dataSourceId)
	{
		if (log.isInfoEnabled())
		{
			log.info("Requesting cancelation of federated query id " + parentQueryId
					+ " for data source " + dataSourceId);
		}
		
		final QueryContext queryContext = queryContextService.findById(new Long(parentQueryId));
	
		// Trigger a cancellation to a specific data source
		new AsynchronousCommandRunner.Builder()
				.commandType(queryContext.getQueryType().getCommandType())
				.setHeader(MessageHeader.DATA_SOURCE_ID, dataSourceId)
				.setHeader(MessageHeader.CANCELED, Boolean.TRUE)
				.producerTemplate(producerTemplate)
				.body(new Long(parentQueryId))
				.requestEndpoint(request)
				.build()
				.run();
	}

}
