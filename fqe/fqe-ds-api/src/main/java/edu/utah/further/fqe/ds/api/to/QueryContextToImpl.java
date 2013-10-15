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
package edu.utah.further.fqe.ds.api.to;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static org.slf4j.LoggerFactory.getLogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.fqe.ds.api.ResultContextMapAdapter;
import edu.utah.further.fqe.ds.api.domain.AbstractQueryContext;
import edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.QueryType;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.plan.PlanToImpl;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.FqeDsApiResourceLocator;

/**
 * A {@link QueryContext} transfer object implementation. Serializable to- and from XML.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz
 *         {@code <dustin.schultz@utah.edu>}
 * @version Mar 19, 2009
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = QueryContextToImpl.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "id", "originId", "executionId", "userId", "dataSourceId", "queryType", "state",
		"queueDate", "staleDateTime", "isStale", "minRespondingDataSources",
		"maxRespondingDataSources", "startDate", "endDate", "parentId",
		"associatedResultId", "targetNamespaceId", "queries", "children",
		"resultContext", "currentStatus", "statuses", "resultViews", "plan" })
public final class QueryContextToImpl extends AbstractQueryContext implements
		QueryContextTo
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(QueryContextToImpl.class);

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "queryContext";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@XmlElement(name = "query_id", required = false, namespace = XmlNamespace.FQE)
	private Long id;

	/**
	 * The unique identifier of the originating query that was used to form this query
	 * (e.g. i2b2 query ID), if applicable.
	 */
	@XmlElement(name = "origin_id", required = false, namespace = XmlNamespace.FQE)
	private Long originId;

	/**
	 * Our own identifier within the federated context. Matches the corresponding plan
	 * execution rules and dependency rules execution ID. See also
	 * https://jira.chpc.utah.edu/browse/FUR-609
	 * https://jira.chpc.utah.edu/browse/FUR-1420
	 */
	@XmlElement(name = "execution_id", required = false, namespace = XmlNamespace.FQE)
	@Final
	private String executionId;

	/**
	 * Identifier of the user that created this query.
	 */
	@XmlElement(name = "user_id", required = false, namespace = XmlNamespace.FQE)
	@Final
	private String userId;

	/**
	 * The unique identifier of the data source generating this status message.
	 */
	@XmlElement(name = "dataSourceId", required = false, namespace = XmlNamespace.FQE)
	@Final
	private String dataSourceId;

	/**
	 * The string corresponding to the {@link CommandType} that this query will be sending
	 * to the data sources.
	 */
	@XmlElement(name = "queryType", required = false, namespace = XmlNamespace.FQE)
	@Final
	private QueryType queryType = QueryType.DATA_QUERY;

	/**
	 * Link to this asset's namespace entity's ID. TOs do not support deep copy of an
	 * Asset entity because it is expensive, and the entity tree may have cycles.
	 */
	@XmlElement(name = "state", required = false, namespace = XmlNamespace.FQE)
	private QueryState state = QueryState.SUBMITTED;

	/**
	 * Query queuing time in the FQE.
	 */
	@XmlElement(name = "queued_date", required = false, namespace = XmlNamespace.FQE)
	private Date queueDate;

	/**
	 * Query stale date & time.
	 */
	@XmlElement(name = "stale_date", required = true, namespace = XmlNamespace.FQE)
	private Date staleDateTime = null;

	/**
	 * Whether or not this QueryContextTo is stale
	 */
	@XmlElement(name = "is_stale", required = true, namespace = XmlNamespace.FQE)
	private boolean isStale = false;

	/**
	 * Query execution starting time.
	 */
	@XmlElement(name = "start_date", required = false, namespace = XmlNamespace.FQE)
	private Date startDate;

	/**
	 * Query execution ending time.
	 */
	@XmlElement(name = "end_date", required = false, namespace = XmlNamespace.FQE)
	private Date endDate;

	/**
	 * 
	 * Minimum number of data sources sought. Sufficient to complete the federated query
	 * if it times out.
	 */
	@XmlElement(name = "minRespondingDataSources", required = false, namespace = XmlNamespace.FQE)
	private int minRespondingDataSources = 2;

	/**
	 * 
	 * Maximum number of data sources sought. Sufficient to complete the federated query
	 * even before it times out.
	 */
	@XmlElement(name = "maxRespondingDataSources", required = false, namespace = XmlNamespace.FQE)
	private int maxRespondingDataSources = 100;

	// ========================= FIELDS - ASSOCIATIONS =====================

	/**
	 * A link to the federated parent {@link QueryContext}, if this is a child
	 * {@link QueryContext} produced by a data source.
	 */
	@XmlTransient
	private QueryContextTo parent;

	/**
	 * The associated result {@link QueryContext}
	 */
	@XmlTransient
	private QueryContextTo associatedResult;

	/**
	 * Link to this asset's federated parent {@link QueryContext}'s ID, if this is a child
	 * {@link QueryContext} produced by a data source. TOs do not support deep copy of an
	 * {@link QueryContext} entity because it is expensive (and in principle, the entity
	 * tree may have cycles).
	 */
	@XmlElement(name = "parentId", required = false, namespace = XmlNamespace.FQE)
	private Long parentId;

	/**
	 * Link to a completed {@link QueryContext}'s ID who's results should be included as
	 * criteria for this query
	 */
	@XmlElement(name = "associatedResultId", required = false, namespace = XmlNamespace.FQE)
	private Long associatedResultId;

	/**
	 * A link to the search query (the query part of this object). Because there will not
	 * be many queries, we don't use an adapter like for {@link #resultViews}, although in
	 * principle we could (map: search query ID (QID) -> search query).
	 */
	@XmlElementRef(name = "query", namespace = XmlNamespace.CORE_QUERY, type = SearchQueryTo.class)
	private final List<SearchQuery> queries = CollectionUtil.newList();

	/**
	 * children context collection.
	 */
	@XmlElementRef(namespace = XmlNamespace.FQE, type = QueryContextToImpl.class)
	private final List<QueryContextTo> children = newList();

	/**
	 * Holds information on where to find the result set and what type it is.
	 */
	@XmlElement(name = "resultContext", required = false, namespace = XmlNamespace.FQE)
	private ResultContextToImpl resultContext;

	/**
	 * Holds information about the current status of the context
	 */
	@XmlElement(name = "currentStatus", required = false, namespace = XmlNamespace.FQE)
	private StatusMetaDataToImpl currentStatus;

	/**
	 * List of statuses associated with this query.
	 */
	@XmlElement(name = "statuses", namespace = XmlNamespace.FQE)
	private List<StatusMetaDataToImpl> statuses;

	/**
	 * Target namespace id for query translation.
	 */
	@XmlElement(name = "targetNamespaceId", required = false, namespace = XmlNamespace.FQE)
	private Long targetNamespaceId;

	/**
	 * A set of result views, parameterized by {@link ResultContextKey}s. Concept's
	 * property map. Keyed and sorted by the {@link ResultContextKeyToImpl} natural
	 * ordering.
	 */
	@XmlJavaTypeAdapter(ResultContextMapAdapter.class)
	@XmlElement(name = "resultViews", required = false, namespace = XmlNamespace.FQE)
	private final Map<ResultType, ResultContext> resultViews = CollectionUtil
			.newMap();

	/**
	 * Query execution plan (an instrumentation piece).
	 */
	@XmlElement(name = "plan", required = false, namespace = XmlNamespace.FQE)
	private PlanToImpl plan;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set. Provided for JAXB only; do not use outside this
	 * class - use the provided factory methods instead.
	 */
	public QueryContextToImpl()
	{
		super();
	}

	/**
	 * Generate a new query context transfer object with a child ID. Unlike persistent
	 * entities, TOs do not have a generated child ID by default because it might be
	 * copied over from an entity. Call this method to signify that this TO is the
	 * beginning of a query flow and therefore a child ID is to be generated at this
	 * point.
	 * <p>
	 * Always prefer this factory method to {@link #newInstance()}, unless the returned
	 * instance is meant to wrap other TO instances that are true {@link QueryContext}
	 * transfer objects, e.g. a web service method that returns a composite
	 * {@link QueryContextToImpl}.
	 * 
	 * @return a new query context instance with a child ID
	 */
	public static QueryContextToImpl newInstanceWithExecutionId()
	{
		final QueryContextToImpl queryContext = new QueryContextToImpl();
		generateExecutionId(queryContext);
		queryContext.setStaleDateTime(FqeDsApiResourceLocator
				.getInstance()
				.getStaleDateTimeFactory()
				.getStaleDateTime());
		return queryContext;
	}

	/**
	 * A copy-constructor factory method. Copies all fields except the child ID field that
	 * is generated as in {@link #newInstanceWithExecutionId()}.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static QueryContextToImpl newCopyWithGeneratedExecutionId(
			final QueryContext other)
	{
		final QueryContextToImpl instance = new QueryContextToImpl();
		instance.copyFrom(other);
		generateExecutionId(instance);
		return instance;
	}

	/**
	 * A copy-constructor factory method for creating children. Generates an execution ID
	 * for this object or sets a manual execution ID if passed as a non-<code>null</code>
	 * argument. Copies only query fields from the parent.
	 * 
	 * @param parent
	 *            other object to deep-copy fields from
	 * @param executionId
	 *            if non-<code>null</code>, will be set as the execution ID. Otherwise, a
	 *            system-generated execution ID will be set
	 * @return a deep copy of <code>other</code>
	 */
	public static QueryContextTo newChildInstance(final QueryContext parent)
	{
		final QueryContextToImpl instance = QueryContextToImpl
				.newInstanceWithExecutionId();
		instance.copyQueryFieldsFrom(parent);
		instance.setParent(parent);
		return instance;
	}

	/**
	 * A factory method that only sets the ID field. Mostly for testing purposes.
	 * 
	 * @param id
	 *            query context ID
	 * @return {@link QueryContextTo} instance with that ID
	 */
	public static QueryContextTo newInstance(final long id)
	{
		final QueryContextTo qc = newInstance();
		qc.setId(new Long(id));
		return qc;
	}

	/**
	 * A copy-constructor factory method.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static QueryContextToImpl newCopy(final QueryContext other)
	{
		return new QueryContextToImpl().copyFrom(other);
	}

	/**
	 * A factory method that constructs an instance with a blank state. Useful as a parent
	 * {@link QueryContextToImpl} factory method because parent contexts don't have
	 * states, they just hold children contexts.
	 * 
	 * @return blank {@link QueryContextToImpl} instance
	 */
	public static QueryContextToImpl newInstance()
	{
		final QueryContextToImpl instance = new QueryContextToImpl();
		instance.state = null;
		instance.setStaleDateTime(computeStaleDateTime());
		return instance;
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Create a copy of this instance (not a full deep-copy; behaves like
	 * {@link #copyFrom(QueryContext)}).
	 * 
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public QueryContext copy()
	{
		return newCopy(this);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Note: the <code>resultEntity</code> reference field is not copied.
	 * 
	 * @param other
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public QueryContextToImpl copyFrom(final QueryContext other)
	{
		if (other == null)
		{
			return this;
		}

		// Copy super-class fields
		super.copyFrom(other);

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		setId(other.getId());
		setUserId(other.getUserId());
		setState(other.getState());

		setNumRecords(other.getNumRecords());
		setStartDate(other.getStartDate());
		setEndDate(other.getEndDate());
		setTargetNamespaceId(other.getTargetNamespaceId());
		this.isStale = other.isStale();

		// Copy associations (usually not deep-copying; just copy references)
		// Status list is NOT copied because it can be large.
		setResultContext(other.getResultContext());
		setAssociatedResult(other.getAssociatedResult());
		final StatusMetaData otherCurrentStatus = other.getCurrentStatus();
		if (otherCurrentStatus != null)
		{
			setCurrentStatus(otherCurrentStatus);
		}

		if (instanceOf(other, QueryContextTo.class))
		{
			// Copy QueryContextTo fields
			final QueryContextTo otherTo = (QueryContextTo) other;

			// Copy QueryContextTo associations
			this.children.clear();
			for (final QueryContext childTo : otherTo.getChildren())
			{
				addChild(childTo);
			}

			setParentId(otherTo.getParentId());
		}
		else
		{
			if (other.getParent() != null)
			{
				setParentId(other.getParent().getId());
			}
		}

		return this;
	}

	// ========================= IMPLEMENTATION: HasIdentifier =============

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: HasSettableIdentifier =====

	/**
	 * @param id
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#setId(java.lang.Long)
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	// ========================= IMPLEMENTATION: HasSettableUserIdentifif. =

	/**
	 * Return the userId property.
	 * 
	 * @return the userId
	 */
	@Override
	public String getUserId()
	{
		return userId;
	}

	/**
	 * Set a new value for the userId property.
	 * 
	 * @param userId
	 *            the userId to set
	 */
	@Override
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	// ========================= IMPLEMENTATION: QueryContext ==============

	/**
	 * Is this entity persistent or not.
	 * 
	 * @return <code>true</code> if and only if this is a persistent {@link QueryContext}
	 *         implementation
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isPersistent()
	 */
	@Override
	public boolean isPersistent()
	{
		return false;
	}

	/**
	 * Return the originId property.
	 * 
	 * @return the originId
	 */
	@Override
	public Long getOriginId()
	{
		return originId;
	}

	/**
	 * Set a new value for the originId property.
	 * 
	 * @param originId
	 *            the originId to set
	 */
	@Override
	public void setOriginId(final Long originId)
	{
		this.originId = originId;
	}

	/**
	 * Return the executionId property.
	 * 
	 * @return the executionId
	 */
	@Override
	public String getExecutionId()
	{
		return executionId;
	}

	/**
	 * Set a new value for the executionId property.
	 * 
	 * @param executionId
	 *            the executionId to set
	 */
	@Override
	public void setExecutionId(final String executionId)
	{
		this.executionId = executionId;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#getState()
	 */
	@Override
	public QueryState getState()
	{
		return state;
	}

	/**
	 * Return the parent {@link QueryContext} of this object, if this is a child
	 * {@link QueryContext}. Otherwise returns <code>null</code>.
	 * 
	 * @return the parent
	 */
	@Override
	public QueryContextTo getParent()
	{
		return parent;
	}

	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parentId
	 *            the parent to set
	 */
	@Override
	public void setParent(final QueryContext parent)
	{
		this.parent = (QueryContextTo) parent;
		if (parent != null)
		{
			this.parentId = parent.getId();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getAssociatedResult()
	 */
	@Override
	public QueryContext getAssociatedResult()
	{
		if (associatedResultId != null)
		{
			setAssociatedResultId(associatedResultId);
		}
		return associatedResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.domain.QueryContext#setAssociatedResult(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void setAssociatedResult(final QueryContext queryContext)
	{
		if (queryContext != null)
		{
			this.associatedResult = QueryContextToImpl.newCopy(queryContext);
		}
		if (associatedResult != null)
		{
			setAssociatedResultId(associatedResult.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	/**
	 * Return the dataSourceId property.
	 * 
	 * @return the dataSourceId
	 */
	@Override
	public String getDataSourceId()
	{
		return dataSourceId;
	}

	/**
	 * Set a new value for the dataSourceId property.
	 * 
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	@Override
	public void setDataSourceId(final String dataSourceId)
	{
		this.dataSourceId = dataSourceId;
	}

	/**
	 * Return the queryType property
	 * 
	 * @return the queryType
	 */
	@Override
	public QueryType getQueryType()
	{
		return queryType;
	}

	/**
	 * Set the queryType property
	 * 
	 * @param queryType
	 */
	@Override
	public void setQueryType(final QueryType queryType)
	{
		this.queryType = queryType;
	}

	/**
	 * Return the parent context's ID property. Doesn't have a public setter because it is
	 * managed by {@link #setParent(QueryContext)}.
	 * 
	 * @return the parentId
	 */
	@Override
	public Long getParentId()
	{
		return parentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#getAssociatedResultId()
	 */
	@Override
	public Long getAssociatedResultId()
	{
		return associatedResultId;
	}

	/**
	 * Return the queries property.
	 * 
	 * @return the queries
	 */
	@Override
	public List<SearchQuery> getQueries()
	{
		return queries;
	}

	/**
	 * Return the queueDate property.
	 * 
	 * @return the queueDate
	 */
	@Override
	public Date getQueueDate()
	{
		return queueDate;
	}

	/**
	 * Set a new value for the queueDate property.
	 * 
	 * @param queueDate
	 *            the queueDate to set
	 */
	@Override
	public void setQueueDate(final Date queueDate)
	{
		this.queueDate = queueDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStaleDateTime()
	 */
	@Override
	public Date getStaleDateTime()
	{
		return staleDateTime;
	}

	/**
	 * @param staleDateTime
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setStaleDateTime(java.util.Date)
	 */
	@Override
	public void setStaleDateTime(final Date staleDateTime)
	{
		this.staleDateTime = staleDateTime;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isStale()
	 */
	@Override
	public boolean isStale()
	{
		return isStale;
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setStale()
	 */
	@Override
	public void setStale()
	{
		this.isStale = true;
	}

	/**
	 * @param queueTime
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#setQueueTime(java.lang.Long)
	 */
	@Override
	public void setQueueTime(final Long queueTime)
	{
		setQueueDate(new Date(queueTime.longValue()));
	}

	/**
	 * Return the startDate property.
	 * 
	 * @return the startDate
	 */
	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Set a new value for the startDate property.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	@Override
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @param startTime
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#setStartTime(java.lang.Long)
	 */
	@Override
	public void setStartTime(final Long startTime)
	{
		if (startTime != null)
		{
			setStartDate(new Date(startTime.longValue()));
		}
		else
		{
			setStartDate(null);
		}

	}

	/**
	 * Return the endDate property.
	 * 
	 * @return the endDate
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#getEndDate()
	 */
	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * Set a new value for the endDate property.
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	@Override
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @param endTime
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#setEndTime(java.lang.Long)
	 */
	@Override
	public void setEndTime(final Long endTime)
	{
		if (endTime != null)
			setEndDate(new Date(endTime.longValue()));
		else
			setEndDate(null);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getTimeInterval()
	 */
	@Override
	public ImmutableTimeInterval getTimeInterval()
	{
		return new TimeIntervalToImpl(this.startDate, this.endDate);
	}

	/**
	 * Return the resultContext property.
	 * 
	 * @return the resultContext
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getResultContext()
	 */
	@Override
	public ResultContext getResultContext()
	{
		return resultContext;
	}

	/**
	 * Set a new value for the resultContext property.
	 * 
	 * @param resultContext
	 *            the resultContext to set.
	 */
	@Override
	public void setResultContext(final ResultContext resultContext)
	{
		this.resultContext = ResultContextToImpl.newCopy(resultContext);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getCurrentStatus()
	 */
	@Override
	public StatusMetaData getCurrentStatus()
	{
		return currentStatus;
	}

	/**
	 * @param statusMetaData
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setCurrentStatus(edu.utah.further.fqe.ds.api.domain.StatusMetaData)
	 */
	@Override
	public void setCurrentStatus(final StatusMetaData statusMetaData)
	{
		this.currentStatus = StatusMetaDataToImpl.newCopy(statusMetaData);
	}

	/**
	 * Return the minRespondingDataSources property.
	 * 
	 * @return the minRespondingDataSources
	 */
	@Override
	public int getMinRespondingDataSources()
	{
		return minRespondingDataSources;
	}

	/**
	 * Set a new value for the minRespondingDataSources property.
	 * 
	 * @param minRespondingDataSources
	 *            the minRespondingDataSources to set
	 */
	@Override
	public void setMinRespondingDataSources(final int minRespondingDataSources)
	{
		this.minRespondingDataSources = minRespondingDataSources;
	}

	/**
	 * Return the maxRespondingDataSources property. See FUR-575.
	 * 
	 * @return the maxRespondingDataSources
	 */
	@Override
	public int getMaxRespondingDataSources()
	{
		return maxRespondingDataSources;
	}

	/**
	 * Set a new value for the maxRespondingDataSources property.
	 * 
	 * @param maxRespondingDataSources
	 *            the maxRespondingDataSources to set
	 */
	@Override
	public void setMaxRespondingDataSources(final int maxRespondingDataSources)
	{
		this.maxRespondingDataSources = maxRespondingDataSources;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStatuses()
	 */
	@Override
	public List<StatusMetaData> getStatuses()
	{
		return CollectionUtil.<StatusMetaData> newList(statuses);
	}

	/**
	 * @param statuses
	 *            the statuses to set
	 */
	public void setStatuses(final List<StatusMetaDataToImpl> statuses)
	{
		this.statuses = statuses;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getTargetNamespaceId()
	 */
	@Override
	public Long getTargetNamespaceId()
	{
		return targetNamespaceId;
	}

	/**
	 * @param targetNamespaceId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setTargetNamespaceId(java.lang.Long)
	 */
	@Override
	public void setTargetNamespaceId(final Long targetNamespaceId)
	{
		this.targetNamespaceId = targetNamespaceId;
	}

	/**
	 * Return the plan property.
	 * 
	 * @return the plan
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#getPlan()
	 */
	@Override
	public PlanToImpl getPlan()
	{
		return plan;
	}

	/**
	 * Set a new value for the plan property. No defensive copy is made at the moment.
	 * 
	 * @param plan
	 *            the plan to set
	 * @param plan
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#setPlan(edu.utah.further.fqe.ds.api.domain.plan.Plan)
	 */
	@Override
	public void setPlan(final Plan plan)
	{
		if ((plan != null) && !instanceOf(plan, PlanToImpl.class))
		{
			throw new UnsupportedOperationException(
					"Can only set a plan of type PlanToImpl on a QC transfer object");
		}
		this.plan = (PlanToImpl) plan;
	}

	// ========================= IMPLEMENTATION: AbstractQueryContext ======

	/**
	 * @param state
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#setState(edu.utah.further.fqe.ds.api.domain.QueryState)
	 */
	@Override
	public void setState(final QueryState state)
	{
		this.state = state;
	}

	// ========================= IMPLEMENTATION: Composite<QueryContext> ===

	/**
	 * @return
	 * @see edu.utah.further.core.api.tree.Composite#getChildren()
	 */
	@Override
	public List<QueryContextTo> getChildren()
	{
		return CollectionUtil.<QueryContextTo> newList(children);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElement#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return !children.isEmpty();
	}

	/**
	 * Return the resultViews property.
	 * 
	 * @return the resultViews
	 */
	@Override
	public Map<ResultType, ResultContext> getResultViews()
	{
		return CollectionUtil.<ResultType, ResultContext> newMap(resultViews);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public ResultContext getResultView(final ResultType key)
	{
		return resultViews.get(key);
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ResultContext addResultView(final ResultType type, final ResultContext value)
	{
		return resultViews.put(type,
				ResultContextToImpl.newCopy(value));
	}

	/**
	 * @param other
	 * @see edu.utah.further.fqe.ds.api.domain.HasResultViews#setResultViews(java.util.Map)
	 */
	@Override
	public void setResultViews(
			final Map<ResultType, ? extends ResultContext> other)
	{
		CollectionUtil.setMapElements(resultViews, other);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public ResultContext removeResultView(final ResultType key)
	{
		return resultViews.remove(key);
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clearResultViews()
	{
		resultViews.clear();
	}

	// ========================= METHODS ===================================

	/**
	 * Add a child context to the children context list.
	 * 
	 * @param child
	 *            child context to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public void addChild(final QueryContext child)
	{
		children
				.add(instanceOf(child, QueryContextToImpl.class) ? (QueryContextToImpl) child
						: newCopy(child));
	}

	/**
	 * Add children contexts to the children context list.
	 * 
	 * @param childrenToAdd
	 *            data to add
	 * @see java.util.List#add(java.lang.Object)
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#addChildren(java.util.Collection)
	 */
	@Override
	public void addChildren(final Collection<? extends QueryContext> childrenToAdd)
	{
		for (final QueryContext child : childrenToAdd)
		{
			addChild(child);
		}
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	@Override
	public int getNumChildren()
	{
		return children.size();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return a deep copy of a search query suitable for insertion into the
	 * {@link #getQueries()} collection.
	 * 
	 * @param query
	 *            original search query
	 * @return search query copy
	 */
	@Override
	protected SearchQuery newSearchQuery(final SearchQuery query)
	{
		return SearchQueryTo.newCopy(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#setQueries(java.util.List)
	 */
	@Override
	protected void setQueries(final List<? extends SearchQuery> queries)
	{
		getQueries().clear();
		addQueries(queries);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#newResultContext()
	 */
	@Override
	protected ResultContextToImpl newResultContext()
	{
		return new ResultContextToImpl();
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	@Override
	protected ResultContextKeyToImpl newKey(final ResultType type)
	{
		return new ResultContextKeyToImpl(type);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#getPrivateSectionExecutor()
	 */
	@Override
	protected PrivateSection getPrivateSectionExecutor()
	{
		return privateSectionExecutor;
	}

	private static PrivateSection privateSectionExecutor = new AbstractQueryContext.PrivateSection()
	{
		/**
		 * @param target
		 * @param newState
		 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.PrivateSection#setState(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext,
		 *      edu.utah.further.fqe.ds.api.domain.QueryState)
		 */
		@Override
		public void setState(final AbstractQueryContext target, final QueryState newState)
		{
			// Cast always succeeds because this call-back method is only called by this
			// class or its sub-classes. Also, the state field is visible because we are
			// within the scope of this class!
			((QueryContextToImpl) target).state = newState;
		}

		/**
		 * @param target
		 * @param endDate
		 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.PrivateSection#setEndDate(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext,
		 *      java.sql.Timestamp)
		 */
		@Override
		public void setEndDate(final AbstractQueryContext target, final Timestamp endDate)
		{
			((QueryContextToImpl) target).setEndDate(endDate);
		}

		/**
		 * @param target
		 * @param queueDate
		 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.PrivateSection#setQueueDate(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext,
		 *      java.sql.Timestamp)
		 */
		@Override
		public void setQueueDate(final AbstractQueryContext target,
				final Timestamp queueDate)
		{
			((QueryContextToImpl) target).setQueueDate(queueDate);
		}

		/**
		 * @param target
		 * @param startDate
		 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.PrivateSection#setStartDate(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext,
		 *      java.sql.Timestamp)
		 */
		@Override
		public void setStartDate(final AbstractQueryContext target,
				final Timestamp startDate)
		{
			((QueryContextToImpl) target).setStartDate(startDate);
		}
	};

	/**
	 * Set a new value for the parentId property.
	 * 
	 * @param parentId
	 *            the parentId to set
	 */
	private void setParentId(final Long parentId)
	{
		this.parentId = parentId;
	}

	private void setAssociatedResultId(final Long associatedResultId)
	{
		this.associatedResultId = associatedResultId;
		if (associatedResultId != null && associatedResult == null)
		{
			final QueryContextToImpl assoc = QueryContextToImpl.newInstance();
			assoc.setId(associatedResultId);
			setAssociatedResult(assoc);
		}
	}

	/**
	 * Copy on query fields (e.g. from federated parent {@link QueryContext} to a child
	 * {@link QueryContext}).
	 * 
	 * @param other
	 *            federated parent {@link QueryContext}
	 * @see edu.utah.further.QueryContext.api.domain.asset.QueryResult#copyFrom(edu.utah.further.QueryContext.api.domain.asset.QueryResult)
	 */
	private void copyQueryFieldsFrom(final QueryContext other)
	{
		if ((other == null) || (other == this))
		{
			return;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		setStaleDateTime(other.getStaleDateTime());
		setStartDate(other.getStartDate());
		setUserId(other.getUserId());
		setEndDate(other.getEndDate());
		setQuery(other.getQuery());
		setQueryType(other.getQueryType());
		setAssociatedResult(other.getAssociatedResult());
		// setQueryXml(other.getQueryXml());
	}
}