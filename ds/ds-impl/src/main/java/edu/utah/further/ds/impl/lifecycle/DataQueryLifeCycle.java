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
package edu.utah.further.ds.impl.lifecycle;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.ds.api.util.AttributeName.META_DATA;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.fqe.ds.api.domain.QueryState.QUEUED;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.ds.api.service.metadata.MetaDataService;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.CommandCancel;

/**
 * The data Query Life cycle which is defined by the list of request handlers. This life
 * cycle is cancel-able and execution is interrupted if a cancel is requested.
 * <p>
 * Implementations have access to {@link AttributeName#DATA_QUERY} request header, and
 * should set an instance of this object as a result of any query.
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
 * @version Feb 2, 2010
 */
// Must not be final to allow transactional AOP in database-type data sources
public class DataQueryLifeCycle extends AbstractLifeCycle<QueryContext, QueryContext>
		implements CommandCancel<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(DataQueryLifeCycle.class);

	// ========================= FIELDS ====================================

	/**
	 * Metadata about this data source
	 */
	private MetaDataService metadataRetriever;

	/**
	 * The current running life cycles.
	 */
	private final Map<Long, ChainRequest> lifeCycles = newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param inputType
	 */
	public DataQueryLifeCycle()
	{
		super(QueryContext.class);
	}

	// ========================= Impl: CommandTrigger ======================

	/**
	 * @param queryContext
	 * @return
	 * @see edu.utah.further.fqe.ds.api.service.CommandTrigger#triggerCommand(java.lang.Object)
	 */
	@Override
	@Transactional
	public QueryContext triggerCommand(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Starting life cycle, input " + queryContext);
		}
		// Can only process federated queries in initial state
		isTrue(queryContext.getState() == QUEUED,
				"QueryState must be in QUEUED but current state is "
						+ queryContext.getState());

		final RequestHandler requestHandlerChain = buildChain();
		final ChainRequest chainRequest = new ChainRequestImpl();

		// Inject the QueryContext into the request
		chainRequest.setAttribute(QUERY_CONTEXT, queryContext);

		// Inject meta data about this data source
		notNull(metadataRetriever);
		chainRequest.setAttribute(META_DATA, metadataRetriever.getMetaData());

		// Invoke the chain
		if (log.isDebugEnabled())
		{
			log.debug("Starting processing chain for data source "
					+ metadataRetriever.getId() + " " + requestHandlerChain);
		}

		lifeCycles.put(queryContext.getId(), chainRequest);

		requestHandlerChain.handle(chainRequest);
		final QueryContext finalQueryContext = chainRequest.getAttribute(QUERY_CONTEXT);

		// Clear the request to help the garbage collector
		chainRequest.removeAllAttributes();

		if (lifeCycles.get(queryContext.getId()) != null)
		{
			// If it wasn't canceled, remove reference
			lifeCycles.remove(queryContext.getId());
		}

		// Extract the results from the request
		return finalQueryContext;
		// // No return value required
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.service.CommandCancel#cancel(java.io.Serializable)
	 */
	@Override
	public void cancel(final Long id)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Cancellation requested for data source "
					+ metadataRetriever.getId());
		}
		final ChainRequest request = lifeCycles.get(id);
		if (request != null)
		{
			log.debug("Interrupting data query life cycle with id " + id
					+ " for data source " + metadataRetriever.getId());
			final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);

			// We have to start it to stop it :-/
			if (!queryContext.isStarted())
			{
				queryContext.start();
			}

			if (!queryContext.isFailed() || !queryContext.isStopped())
			{
				queryContext.stop();
			}

			request.cancel();
			// Clean up, remove reference
			lifeCycles.remove(id);
		}
		else
		{
			log.debug("Do not have handle to request for id " + id + " at data source "
					+ metadataRetriever.getId() + ". Ignorning request");
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @param metadataRetriever
	 *            the metadataRetriever to set
	 */
	public void setMetadataRetriever(final MetaDataService metadataRetriever)
	{
		this.metadataRetriever = metadataRetriever;
	}

	/**
	 * Return the metadataRetriever property.
	 * 
	 * @return the metadataRetriever
	 */
	public MetaDataService getMetadataRetriever()
	{
		return metadataRetriever;
	}

}
