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
package edu.utah.further.fqe.api.service.route;

import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A client that sends data source commands as part of the FQE camel route.
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
 * @version Aug 14, 2009
 */
public interface FqeService
{
	// ========================= METHODS: Queries ==========================

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
	 * @return the parent {@link QueryContext} with a populated identifier.
	 * @see edu.utah.further.fqe.ds.api.service.CommandTrigger#deleteQuery(java.lang.Object)
	 */
	QueryContext triggerQuery(QueryContext logicalQuery);

	/**
	 * Trigger a query stopping request. If the QueryContext is a federated context, then
	 * it is stopped and so are it's children. If the QueryContext is a data source
	 * context, only this data source is canceled.This method is asynchronous and returns
	 * immediately after calling.
	 * <p>
	 * An asynchronous call.
	 *
	 * @param queryId
	 *            the {@link QueryContext} identifier of the parent (or federated context)
	 *            to cancel - not the child context.
	 */
	void stopQuery(QueryContext queryContext);

	// ========================= METHODS: remote control data sources ======

	/**
	 * Send a status request to all data sources. Note: the result set entries are not
	 * guaranteed to be sorted in any way.
	 *
	 * @return aggregated result set containing a list of meta data objects for each of
	 *         the registered data sources
	 */
	Data status();

	/**
	 * Request a single data sources' status meta data.
	 *
	 * @param dataSourceId
	 *            data source unique identifier, usually its name/standard symbol
	 * @return data source's status meta data object
	 */
	Data status(String dataSourceId);

	/**
	 * Requests the data source to enter a new activation state.
	 *
	 * @param dataSourceId
	 *            Data source unique identifier, usually its name/standard symbol
	 * @param newState
	 *            data source state to set
	 * @return updated data source meta data object
	 */
	DsMetaData updateState(String dataSourceId, DsState newState);
}