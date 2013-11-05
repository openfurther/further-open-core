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
package edu.utah.further.fqe.api.service.query;

import java.util.Date;
import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;

/**
 * Provides CRUD operations for query contexts, so that we can monitor their states for
 * display, say, in a query monitor page.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 23, 2009
 */
@Api
public interface QueryContextService
{
	// ========================= CRUD METHODS ==============================

	/**
	 * Persist a query context object to the database. This is useful for transient
	 * entities with embedded IDs that can be non-<code>null</code>.
	 * 
	 * @param queryContext
	 *            a transient entity or a transfer object
	 * @return persisted entity; if an entity was passed in, it is returned; if a transfer
	 *         object was passed in, a new entity is created, persisted and returned
	 */
	QueryContext queue(QueryContext queryContext);

	/**
	 * Performs a persistent (if this is a transient object) or merge (if this is a
	 * persistent object) operation.
	 * 
	 * @param queryContext
	 *            a detached query context instance with state to be persisted/updated in
	 *            the database, or a transfer object
	 * @return persisted entity; if an entity was passed in, it is returned; if a transfer
	 *         object was passed in, a new entity is created, persisted and returned
	 */
	QueryContext update(QueryContext queryContext);

	/**
	 * Stop a query. Stops its children too. Changes are saved to the databsae.
	 * 
	 * @param queryContext
	 *            queryContext entity to be stopped
	 */
	void stop(QueryContext queryContext);

	/**
	 * Delete a query context entity from database. Deletes all its children as well.
	 * 
	 * @param queryContext
	 *            queryContext entity to be deleted
	 */
	void delete(QueryContext queryContext);

	/**
	 * Delete all query context instances from database.
	 */
	void deleteAll();

	/**
	 * Delete all query contexts initiated by a single user.
	 */

	void deleteByUser(String userId);

	// ========================= FINDER METHODS ============================

	/**
	 * Find a query context by a unique identifier.
	 * 
	 * @param id
	 *            a unique identifier to look for
	 * @return persistent queryContext entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	QueryContext findById(Long queryId);

	/**
	 * Find a query by origin ID.
	 * 
	 * @param originId
	 *            query origin ID (e.g. i2b2 ID)
	 * @return FQE query, or <code>null</code>, if not found
	 */
	QueryContext findQueryContextWithOriginId(Long originId);

	/**
	 * Load a DQC's parent QC.
	 * 
	 * @param childQueryContext
	 *            child query context
	 * @param forceReload
	 *            if true, forces a reload of the parent entity from the database
	 * @return parent entity, freshly loaded from the database
	 */
	QueryContext findParent(QueryContext childQueryContext, boolean forceReload);

	/**
	 * Find all query contexts in the database.
	 * 
	 * @return a list of all entity of this type
	 */
	List<QueryContext> findAll();

	/**
	 * Find all query contexts initiated by a user
	 * 
	 * @return a list of query contexts
	 */

	List<QueryContext> findAllByUser(String userId);

	/**
	 * Finds all stale queries.
	 * 
	 * @param now
	 *            The date and time as of now
	 * @return a list of stale queries
	 */
	List<QueryContext> findStaleQueries(final Date now);

	/**
	 * Finds all EXECUTING queries which are not stale.
	 * 
	 * @param now
	 *            The date and time as of now
	 * 
	 * @return a list of stale queries
	 */
	List<QueryContext> findExecutingNotStaleQueries(final Date now);

	/**
	 * Find all {@link QueryContext}s whose parent is the passed argument.
	 * 
	 * @param parent
	 *            the parent QueryContext
	 * @return all children
	 */
	List<QueryContext> findChildren(QueryContext parent);

	/**
	 * Find all {@link QueryContext}s in {@link QueryState#COMPLETED} whose parent is the
	 * passed argument.
	 * 
	 * @param parent
	 *            the parent QueryContext
	 * @return all completed children
	 */
	List<QueryContext> findCompletedChildren(QueryContext parent);

	/**
	 * Find all {@link QueryContext}s in {@link QueryState#FAILED} whose parent is the
	 * passed argument.
	 * 
	 * @param parent
	 *            the parent QueryContext
	 * @return all completed children
	 */
	List<QueryContext> findFailedChildren(QueryContext parent);

	/**
	 * Returns all statuses from all data queries
	 * 
	 * @return a list of statuses
	 */
	List<StatusMetaData> findAllStatuses();

	/**
	 * Returns all statuses for a given query context identifier
	 * 
	 * @param queryContextId
	 *            the query context identifier to find status for
	 */
	List<StatusMetaData> findAllStatuses(Long queryContextId);

	/**
	 * Returns all statuses past a given date.
	 * 
	 * @param date
	 *            cut-off date
	 * @return a list of statuses more current than <code>date</code>
	 */
	List<StatusMetaData> findAllStatusesLaterThan(Date date);

	/**
	 * Finds the status (if any) of a given query context identifier.
	 * 
	 * @param queryContextId
	 *            the query context identifier to find status for
	 * @return status
	 */
	StatusMetaData findCurrentStatusById(Long queryContextId);
}
