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
package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.ds.api.util.AttributeName.META_DATA;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.Initializer;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * The {@link InitializerQp}processor is responsible for executing initializing the
 * QueryContext for a data source data query flow, and executing any pre-processing such
 * as loading artifacts from the MDR, or modifying the QueryContext for execution of the
 * given data source.
 * <p>
 * Unlike other Query Processors, the initializer is somewhat special in that it has a
 * required behavior. {@link InitializerQp} provides that required behavior and hooks in
 * order for the data source flow to execute.
 * <p>
 * This class was built for extension.
 * <ul>
 * <li>call canAnswer to determine whether or not this data source can answer this query</li>
 * <li>check if the data source is INACTIVE, if so, terminate</li>
 * <li>create a child QueryContext from the federated QueryContext and set the parent to
 * the federated QueryContext</li>
 * <li>inject any attributes for future Query Processors</li>
 * </ul>
 * <p>
 * Clients should be aware that implementations of this class may be used by multiple
 * threads as multiple data sources are processed and will likely need to consider a
 * thread safe implementation or a new instantiation for each use.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 13, 2010
 */
public class InitializerQp extends AbstractDelegatingUtilityProcessor<Initializer>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(InitializerQp.class);

	// ========================= FIELDS ====================================

	/**
	 * Meta data service to retrieve meta data about the data source.
	 */
	private DsMetaData metaData;

	// ========================= IMPL: RequestProcessor ====================

	/**
	 * A template method of data query initialization.
	 * 
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext federatedQc = request.getAttribute(QUERY_CONTEXT);
		final DsMetaData dsMetaData = request.getAttribute(META_DATA);

		this.metaData = dsMetaData;
		// Create our copy of the federated context
		final QueryContext childQc = createChildContext(federatedQc);
		if (log.isDebugEnabled())
		{
			log.debug("########### Initializing: FQC ExecID "
					+ federatedQc.getExecutionId() + " DQC ExecID "
					+ childQc.getExecutionId() + " DS " + metaData.getName()
					+ " ###########");
		}

		// Validations. Perform inexpensive tests first
		if (!isDsReady(dsMetaData.getState()))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Terminating processing chain, DS not ready");
			}

			// Transition to failure state. We haven't initialized yet so call
			// intermediate states
			childQc.queue();
			childQc.start();
			childQc.fail();

			// Terminate
			return true;
		}

		// place child context in the request instead of the parent only if the datasource
		// is ready to process
		request.setAttribute(QUERY_CONTEXT, childQc);

		// Inject initialization attributes
		injectAttributes(request, getDelegate().getInjectedAttributes());

		// Finish initializing the child query context
		getDelegate().initialize(childQc, metaData);

		// Authorize this request if needed
		getDelegate().authorize(SecurityContextHolder.getContext().getAuthentication(),
				childQc, dsMetaData);

		if (!getDelegate().canAnswer(federatedQc, dsMetaData))
		{
			if (log.isTraceEnabled())
			{
				log.trace("Terminating processing chain, DS cannot answer this query");
			}

			// Already initialized, just fail
			childQc.fail();

			// Terminate
			return true;
		}

		if (log.isTraceEnabled())
		{
			log.trace("Completed initialization for data source " + metaData);
		}

		return false;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Check if DsState is invalid.
	 * 
	 * @param dsState
	 * @return readiness code (<code>true</code> if and only if OK to process request)
	 */
	private boolean isDsReady(final DsState dsState)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Data source " + metaData.getName() + " is currently in " + dsState
					+ " state");
		}
		if (dsState == DsState.INACTIVE)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Data source is NOT ready to process requests");
			}
			// DS not ready
			return false;
		}

		if (log.isDebugEnabled())
		{
			log.debug("Data source is ready to process requests");
		}
		// DS is ready
		return true;
	}

	/**
	 * Inject attributes into the chain request.
	 * 
	 * @param request
	 *            chain request
	 * @param attributes
	 *            attributes to inject into the request
	 */
	private void injectAttributes(final ChainRequest request,
			final Map<String, Object> attributes)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Injecting attributes " + attributes);
		}
		for (final Entry<String, Object> attribute : attributes.entrySet())
		{
			request.setAttribute(attribute.getKey(), attribute.getValue());
		}
	}

	/**
	 * Create a copy of the parent query context and assign our data source ID to it.
	 * 
	 * @param parentQueryContext
	 *            parent (federated) QC
	 * @return this DS's copy of the QC, to be passed through its data query flow
	 */
	private QueryContext createChildContext(final QueryContext parentQueryContext)
	{
		// Create our own child context for the duration of the query flow.
		final QueryContext newChild = QueryContextToImpl
				.newChildInstance(parentQueryContext);

		newChild.setDataSourceId(metaData.getName());
		newChild.setTargetNamespaceId(metaData.getNamespaceId());

		return newChild;
	}
}
