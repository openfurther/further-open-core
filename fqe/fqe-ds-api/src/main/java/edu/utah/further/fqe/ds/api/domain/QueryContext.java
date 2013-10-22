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
package edu.utah.further.fqe.ds.api.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.lang.PubliclyCloneable;
import edu.utah.further.core.api.security.HasUserIdentifier;
import edu.utah.further.core.api.state.StateMachine;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;

/**
 * Keeps track of the search criteria, progress and results of a federated query. This is
 * the main object passed between the FQE and the data sources. Each data source keeps
 * track of their progress in their own child {@link QueryContext}, which has a reference
 * to its federated parent {@link QueryContext}. That is, children have a many-to-one
 * association to parents.
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
 * @version Sep 10, 2009
 */
public interface QueryContext extends PersistentEntity<Long>,
		CopyableFrom<QueryContext, QueryContext>, PubliclyCloneable<QueryContext>,
		StateMachine<QueryState, QueryAction, QueryContext>, QueryActor, HasNumRecords,
		HasUserIdentifier<String>, HasResultContext, HasResultViews
{
	
	
	// ========================= METHODS ===================================

	/**
	 * Return the unique identifier of the originating query that was used to form this
	 * query (e.g. i2b2 query ID), if applicable.
	 * 
	 * @return the originating query Id
	 */
	Long getOriginId();

	/**
	 * Set a new value for the originId property.
	 * 
	 * @param originId
	 *            the originId to set
	 */
	void setOriginId(Long originId);

	/**
	 * Return the executionId property. In addition to the primary identifier, this serves
	 * as a local identifier of a data source's own {@link QueryContext} within a data
	 * source query flow. It is also used by the FQE to look up and save/update/retrieve
	 * the same child {@link QueryContext} under its federated parent when the data source
	 * sends status messages. This way, the data source never needs to know about the
	 * database-managed identifier and can be decoupled from the virtual repository. For
	 * completeness, this identifier is generated for all query context ids, not just
	 * children.
	 * <p>
	 * It is the responsibility of the client code creating the {@link QueryContext}
	 * instance to set a child ID. It should not be set by a CRUD service.
	 * 
	 * @return the child ID
	 * @see https://jira.chpc.utah.edu/browse/FUR-609
	 */
	String getExecutionId();

	/**
	 * Return the unique identifier of the data source that generated this this message.
	 * 
	 * @return the dataSourceId
	 */
	String getDataSourceId();

	/**
	 * Set a new value for the dataSourceId property.
	 * 
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	void setDataSourceId(String dataSourceId);

	/**
	 * Return the parent {@link QueryContext} of this object, if this is a child
	 * {@link QueryContext}. Otherwise returns <code>null</code>.
	 * 
	 * @return the parent
	 */
	QueryContext getParent();

	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parentId
	 *            the parent to set
	 */
	void setParent(QueryContext parent);

	/**
	 * Return the {@link QueryContext} with results for which this {@link QueryContext}
	 * should include as additional criteria to queries in this {@link QueryContext}
	 * 
	 * @return the {@link QueryContext} of the associated result
	 */
	QueryContext getAssociatedResult();
	
	/**
	 * Sets the completed {@link QueryContext} with results that should be included as
	 * additional criteria for queries in this {@link QueryContext}
	 * 
	 * @param queryContext the {@link QueryContext} of the associated result
	 */
	void setAssociatedResult(QueryContext queryContext);
	
	/**
	 * Return a string which describes type of result this query will be requesting.
	 */
	QueryType getQueryType();
	
	/**
	 * Sets the string which describes type of result this query will be requesting.
	 */
	void setQueryType(QueryType queryType);
	
	/**
	 * Return the type of identity resolution that will be used for this query.
	 *
	 * @return the identityResolutionType
	 */
	public IdentityResolutionType getIdentityResolutionType();

	/**
	 * Set the type of identity resolution that will be used for this query.
	 *
	 * @param identityResolutionType the identityResolutionType to set
	 */
	public void setIdentityResolutionType(IdentityResolutionType identityResolutionType);


	/**
	 * Returns the SearchQuery
	 * 
	 * @return SearchQuery
	 */
	SearchQuery getQuery();

	/**
	 * Return a search query with a specified QID (unique SQ identifier within this QC).
	 * 
	 * @param qid
	 *            search query ID within this QC
	 * @return the query
	 */
	SearchQuery getQueryByQid(Long qid);

	/**
	 * Return the queries property.
	 * 
	 * @return the queries
	 */
	List<SearchQuery> getQueries();

	/**
	 * Set a new single query on this object. This means that the list of queries will now
	 * consist of one element.
	 * 
	 * @param searchQuery
	 */
	void setQuery(SearchQuery searchQuery);

	/**
	 * Set the query list of this context.
	 * 
	 * @param searchQueries
	 *            list of queries to set
	 */
	void setQueries(Collection<? extends SearchQuery> searchQueries);

	/**
	 * Add a query to this context.
	 * 
	 * @param query
	 */
	void addQuery(SearchQuery query);

	/**
	 * Add multiple queries to this context.
	 * 
	 * @param searchQueryToAdd
	 */
	void addQueries(Collection<? extends SearchQuery> searchQueryToAdd);

	/**
	 * Return the number of queries in this context.
	 * 
	 * @return number of queries in this context
	 */
	int getNumQueries();

	/**
	 * Return the date at which query was queued in the FQE.
	 * 
	 * @return the queue date
	 */
	Date getQueueDate();

	/**
	 * Return the time at which query was queued in the FQE.
	 * 
	 * @return the queue time
	 */
	Long getQueueTime();

	/**
	 * Return the date at which query execution began.
	 * 
	 * @return query starting point in time
	 */
	Date getStartDate();

	/**
	 * Return the time at which query execution began.
	 * 
	 * @return query starting time
	 */
	Long getStartTime();

	/**
	 * Return the date & time at which this query is stale.
	 * 
	 * @return date & time at which this query is stale.
	 */
	Date getStaleDateTime();

	/**
	 * Is this QueryContext stale or not.
	 * 
	 * @return
	 */
	boolean isStale();

	/**
	 * Marks this QueryContext as stale.
	 */
	void setStale();

	/**
	 * Return the date & time at which this query is stale.
	 * 
	 * @return date & time at which this query is stale.
	 */
	void setStaleDateTime(Date staleDateTime);

	/**
	 * Return the date at which query execution ended.
	 * 
	 * @return query ending point in time
	 */
	Date getEndDate();

	/**
	 * Return the time at which query execution ended.
	 * 
	 * @return query end time
	 */
	Long getEndTime();

	/**
	 * Return a time interval [query_start_date,query_end_date]. A useful view method.
	 * 
	 * @return query execution time interval
	 */
	ImmutableTimeInterval getTimeInterval();

	/**
	 * Returns the status metadata about the current context if applicable.
	 * 
	 * @return meta data about the status of the context.
	 */
	StatusMetaData getCurrentStatus();

	/**
	 * Sets the status metadata about the current context.
	 * 
	 * @param currentStatus
	 */
	void setCurrentStatus(StatusMetaData currentStatus);

	/**
	 * Return the minRespondingDataSources property. See FUR-575.
	 * 
	 * @return the minRespondingDataSources
	 */
	int getMinRespondingDataSources();

	/**
	 * Set a new value for the minRespondingDataSources property.
	 * 
	 * @param minRespondingDataSources
	 *            the minRespondingDataSources to set
	 */
	void setMinRespondingDataSources(int minRespondingDataSources);

	/**
	 * Return the maxRespondingDataSources property. See FUR-575. Currently not in use.
	 * 
	 * @return the maxRespondingDataSources
	 */
	int getMaxRespondingDataSources();

	/**
	 * Set a new value for the maxRespondingDataSources property.
	 * 
	 * @param maxRespondingDataSources
	 *            the maxRespondingDataSources to set
	 */
	public void setMaxRespondingDataSources(int maxRespondingDataSources);

	/**
	 * Returns all statuses for this {@link QueryContext}
	 * 
	 * @return
	 */
	List<StatusMetaData> getStatuses();

	/**
	 * Sets the target namespace id if applicable.
	 * 
	 * @param namespaceId
	 *            a long representing the target namespace id.
	 */
	void setTargetNamespaceId(Long namespaceId);

	/**
	 * Returns the target namespace id
	 * 
	 * @return a long representing the namespace id
	 */
	Long getTargetNamespaceId();

	/**
	 * Is this entity persistent or not.
	 * 
	 * @return <code>true</code> if and only if this is a persistent {@link QueryContext}
	 *         implementation
	 */
	boolean isPersistent();

	/**
	 * Is this query currently in a failed state.
	 * 
	 * @return <code>true</code> if and only if this object is currently in failed state
	 */
	boolean isFailed();

	/**
	 * Is the query in a state (that cannot transition to any other state).
	 * 
	 * @return <code>true</code> if and only if query is in state
	 */
	boolean isInFinalState();

	/**
	 * Return the plan property.
	 * 
	 * @return the plan
	 */
	Plan getPlan();

	/**
	 * Set a new value for the plan property. No defensive copy is made at the moment.
	 * 
	 * @param plan
	 *            the plan to set
	 */
	void setPlan(Plan plan);
}
