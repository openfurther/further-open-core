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
package edu.utah.further.fqe.impl.scheduler.jobs;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.fqe.api.service.query.AggregationService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.util.FqeEndpointNames;

/**
 * "Seals" queries by putting them into a failed or completed state. Determines if a query
 * is stale by checking if the stale date & time of the query is past the current stale
 * date & time. If the query is stale and the number of data sources responded is less
 * than the minimum, the query is transitioned to a failed state. If the query is stale
 * and the number of data sources >= the minimum, the query is transitioned to the
 * completed state. Determines if a query is completed by checking if the number of
 * children of the parent query in the completed state is equal to the number of children
 * in the parent query. This class is typically ran as a scheduled job which creates a new
 * thread for each invocation and runs at a configured interval.
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
 * @version Mar 25, 2010
 */
@Service("querySealer")
// Non-final to allow AOP addition in the future, e.g. performance timers
public class QuerySealer implements Runnable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QuerySealer.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Provides {@link QueryContext} CRUD operations.
	 */
	@Autowired
	private QueryContextService qcService;

	/**
	 * The Fqe Service for sending messages to data sources
	 */
	@Autowired
	private FqeService fqeService;

	/**
	 * Federated result set aggregation service.
	 */
	@Autowired
	private AggregationService aggregationService;

	/**
	 * Data access object, uses Dao directly as to avoid the details of
	 * QueryContextService update method.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	/**
	 * Used for producing camel messages.
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	// ========================= Impl: Runnable ============================

	/**
	 * Run the sealer.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		final Date now = TimeService.getDate();

		if (log.isTraceEnabled())
		{
			log.trace("Looking for stale queries @ " + now);
		}

		// Consider function pointer if number of cases grows beyond 2 or 3
		handleStaleQueries(now);
		handleExecutingNotStaleQueries(now);
	}

	// ========================= GET/SET ===================================

	/**
	 * @return the qcService
	 */
	public QueryContextService getQcService()
	{
		return qcService;
	}

	/**
	 * @param qcService
	 *            the qcService to set
	 */
	public void setQcService(final QueryContextService qcService)
	{
		this.qcService = qcService;
	}

	/**
	 * Return the fqeService property.
	 * 
	 * @return the fqeService
	 */
	public FqeService getFqeService()
	{
		return fqeService;
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
	 * Return the dao property.
	 * 
	 * @return the dao
	 */
	public Dao getDao()
	{
		return dao;
	}

	/**
	 * Set a new value for the dao property.
	 * 
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Handles stale queries. All states except EXECUTING transition to FAILED state.
	 * Executing transitions to FAILED if the minimum responded is less than the minimum
	 * desired otherwise it transitions to COMPLETED.
	 * 
	 * @param now
	 *            The current date/time
	 */
	private void handleStaleQueries(final Date now)
	{
		final List<QueryContext> staleQueries = qcService.findStaleQueries(now);
		if (log.isTraceEnabled())
		{
			log.trace("Found " + staleQueries.size() + " stale queries.");
		}

		for (final QueryContext queryContext : staleQueries)
		{

			// Stop the query if it's running
			if (queryContext.isStarted())
			{
				fqeService.stopQuery(queryContext);
			}

			// Transition to an appropriate state
			advanceStaleQueryContextToProperState(queryContext);

			// Mark this QueryContext as stale
			queryContext.setStale();

			// Persist the changes
			dao.update(queryContext);

			// If this is a completed FQC, send it to the aggregation route for
			// post-processing
			if (getProducerTemplate() != null)
			{
				getProducerTemplate().sendBody(FqeEndpointNames.FEDERATED_RESULT,
						queryContext);
			}
		}
	}

	/**
	 * Handle executing but not stale queries. These queries are transitioned to COMPLETED
	 * if all children have responded. This handles the case where all of the data sources
	 * that are going to respond, have responded.
	 * 
	 * @param now
	 *            The current date/time
	 */
	private void handleExecutingNotStaleQueries(final Date now)
	{
		final List<QueryContext> executingQueries = qcService
				.findExecutingNotStaleQueries(now);

		if (log.isTraceEnabled())
		{
			log.trace("Found " + executingQueries.size()
					+ " EXECUTING but not stale queries.");
		}

		for (final QueryContext queryContext : executingQueries)
		{
			final int children = qcService.findChildren(queryContext).size();
			if (children > 0)
			{
				final int completed = qcService
						.findCompletedChildren(queryContext)
						.size();
				final int failed = qcService.findFailedChildren(queryContext).size();

				if ((completed == children) || (failed == children)
						|| (completed + failed) == children)
				{
					finishAndPostProcess(queryContext);
					dao.update(queryContext);
				}
			}

		}

	}

	/**
	 * Depending on the given state, the QueryContext may have to move through
	 * intermediate states in order to enter the failed state otherwise, an exception will
	 * be thrown due to the design of the state machine. Refer to the diagram on the wiki
	 * for a visual representation of the possible transitions.
	 * 
	 * @param queryContext
	 */
	private void advanceStaleQueryContextToProperState(final QueryContext queryContext)
	{
		final QueryState state = queryContext.getState();
		switch (state)
		{
			case SUBMITTED:
			{
				queryContext.queue();
				queryContext.start();
				queryContext.fail();
				break;
			}

			case QUEUED:
			{
				queryContext.start();
				queryContext.fail();
				break;
			}

			case EXECUTING:
			{
				updateExecutingQueryState(queryContext);
				break;
			}

			case STOPPED:
			{
				queryContext.fail();
				break;
			}

			case FAILED:
			{
				break;
			}

			default:
			{
				if (log.isWarnEnabled())
				{
					log.warn("We shouldn't be here: stale query but has state " + state);
				}
			}
		}

		if (log.isDebugEnabled())
		{
			log.debug("Changing stale query ID " + queryContext.getId() + " state from "
					+ state + " to " + queryContext.getState() + ".");
		}
	}

	/**
	 * Update an executing parent state according to children completion states (FUR-575):
	 * <ul>
	 * <li>Transition query to COMPLETED state if At least maxRespondingDataSources DS's
	 * have responded. NOTE: this is not yet implemented for simplicity, and to allow even
	 * more data sources to respond within the timeout period. Timeout occurred and at
	 * least minRespondingDataSources DS's have responded.</li>
	 * <li>Transition to FAILED state if If timeout occurred and less than
	 * minRespondingDataSources DS's have responded, transition query to FAILED state.</li>
	 * </ul>
	 * 
	 * @param parent
	 *            federated query contexts to update
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#updateStateUponChildCompletion(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	private void updateExecutingQueryState(final QueryContext parent)
	{
		final int numRespondingDs = qcService.findCompletedChildren(parent).size();
		final int minRespondingDs = parent.getMinRespondingDataSources();
		if (numRespondingDs >= minRespondingDs)
		{
			if (log.isDebugEnabled())
			{
				log.debug(numRespondingDs + " DS's responded >= minimum required ("
						+ minRespondingDs + "). Finishing query.");
			}
			finishAndPostProcess(parent);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug(numRespondingDs + " DS's responded < minimum required ("
						+ minRespondingDs + "). Failing query.");
			}
			parent.fail();
		}
	}

	/**
	 * Finish query and run federation post-processing operations.
	 * 
	 * @param queryContext
	 *            FQC
	 */
	private void finishAndPostProcess(final QueryContext queryContext)
	{
		queryContext.finish();
		if (queryContext.getState() != QueryState.FAILED)
		{
			aggregationService.generateResultViews(queryContext);
		}
	}

	/**
	 * Return the producerTemplate property.
	 * 
	 * @return the producerTemplate
	 */
	public ProducerTemplate getProducerTemplate()
	{
		return producerTemplate;
	}

	/**
	 * Set a new value for the producerTemplate property.
	 * 
	 * @param producerTemplate
	 *            the producerTemplate to set
	 */
	public void setProducerTemplate(final ProducerTemplate producerTemplate)
	{
		this.producerTemplate = producerTemplate;
	}

}
