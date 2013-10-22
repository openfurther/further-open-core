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
package edu.utah.further.fqe.impl.domain;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.api.time.TimeUtil.getTimestampNullSafe;
import static org.hibernate.annotations.CascadeType.ALL;
import static org.slf4j.LoggerFactory.getLogger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.PublicMapEntry;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.AbstractQueryContext;
import edu.utah.further.fqe.ds.api.domain.IdentityResolutionType;
import edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.QueryType;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;

/**
 * A {@link QueryContext} persistent entity implementation.
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
 * @version Mar 19, 2009
 */
// ============================
// Hibernate annotations
// ============================
@Entity
@Table(name = "query_context")
public class QueryContextEntity extends AbstractQueryContext
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryContextEntity.class);

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "query_id")
	@Final
	private Long id;

	/**
	 * The unique identifier of the originating query that was used to form this query
	 * (e.g. i2b2 query ID), if applicable.
	 */
	@Column(name = "origin_id", updatable = false)
	private Long originId;

	/**
	 * Our own identifier within the federated context. Matches the corresponding plan
	 * execution rules and dependency rules execution ID. See also
	 * https://jira.chpc.utah.edu/browse/FUR-609
	 * https://jira.chpc.utah.edu/browse/FUR-1420
	 */
	@Column(name = "execution_id", nullable = false, updatable = false)
	@Final
	private String executionId = UUID.randomUUID().toString();

	/**
	 * The unique identifier of the user that created this query.
	 */
	@Column(name = "user_id", updatable = false)
	private String userId;

	/**
	 * The unique identifier of the data source that generated this context.
	 */
	@Column(name = "datasourceid")
	private String dataSourceId;

	/**
	 * An enumerated value specifying what type query this was.
	 * 
	 */
	@Column(name = "query_type")
	@Enumerated(EnumType.STRING)
	private QueryType queryType = QueryType.DATA_QUERY;
	
	/**
	 * An enumerated value specifying what type of identity resolution should be perform.
	 */
	@Column(name = "identity_res_type")
	@Enumerated(EnumType.STRING)
	private IdentityResolutionType identityResolutionType;

	/**
	 * Link to this asset's namespace entity's ID. TOs do not support deep copy of an
	 * Asset entity because it is expensive, and the entity tree may have cycles.
	 */
	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private QueryState state = QueryState.SUBMITTED;

	/**
	 * Starting point of this time interval.
	 */
	@Column(name = "queue_date", nullable = true)
	private Date queueDate;

	/**
	 * Stale date & time for this context.
	 */
	@Column(name = "stale_date", nullable = false)
	private Date staleDateTime = computeStaleDateTime();

	/**
	 * Whether or not this Query Context entity is stale
	 */
	@Column(name = "is_stale", nullable = false)
	private boolean isStale = false;

	/**
	 * Minimum number of data sources sought. Sufficient to complete the federated query.
	 */
	@Column(name = "minrespondingdatasources", nullable = false)
	private int minRespondingDataSources = 2;

	/**
	 * Maximum number of data sources sought. Sufficient to complete the federated query.
	 */
	@Column(name = "maxrespondingdatasources", nullable = false)
	private int maxRespondingDataSources = 100;

	/**
	 * Holds start and end times of query execution. Used for illustration purposes of a
	 * Hibernate embedded entity. We could simply use individual fields like
	 * {@link #queueDate} as an alternative.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "start", column = @Column(name = "start_date", nullable = true)),
			@AttributeOverride(name = "end", column = @Column(name = "end_date", nullable = true)) })
	@Final
	private TimeIntervalEntity timeInterval = new TimeIntervalEntity();

	// ========================= FIELDS - ASSOCIATIONS =====================

	/**
	 * The history of status change events of this context.
	 */
	@OneToMany(fetch = FetchType.EAGER, targetEntity = SearchQueryEntity.class, mappedBy = "queryContext")
	@Cascade(
	{ ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private final List<SearchQuery> queries = CollectionUtil.newList();

	/**
	 * A link to the federated parent {@link QueryContext}, if this is a child
	 * {@link QueryContext} produced by a data source.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	// No cascading on purpose. See QCServiceImpl#update()
	@OnDelete(action = OnDeleteAction.CASCADE)
	private QueryContextEntity parent;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "associatedresult")
	@Cascade(
	{ ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private QueryContextEntity associatedResult;

	/**
	 * Holds information on where to find the result set and what type it is.
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "resultcontext")
	@Cascade(
	{ ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ResultContextEntity resultContext = new ResultContextEntity();

	/**
	 * The target namespace, if applicable to this query.
	 */
	@Column(name = "targetnamespace")
	private Long targetNamespaceId;

	/**
	 * Current status
	 */
	@OneToOne
	@JoinColumn(name = "currentstatus")
	private StatusMetaDataEntity currentStatus;

	/**
	 * The history of status change events of this context.
	 */
	@OneToMany(fetch = FetchType.EAGER, targetEntity = StatusMetaDataEntity.class, mappedBy = "queryContext")
	@Cascade(
	{ ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private final List<StatusMetaData> statuses = CollectionUtil.newList();

	/**
	 * A Hibernate adapter of result views, because using Hibernate JPA 1.99.0 {@link Map}
	 * mapping annotations is horribly hard and is not MySQL-compatible as per
	 * https://jira.chpc.utah.edu/browse/FUR-1348 .
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = ResultContextKeyEntity.class)
	@JoinColumn(name = "query_context_id")
	private final Set<ResultContextEntry> resultViews = CollectionUtil.newSet();

	/**
	 * The exposed set of result views, parameterized by {@link ResultContextKey}s.
	 * Synchronized with {@link #resultViews}.
	 */
	@Transient
	private Map<ResultType, ResultContext> resultViewsMap; // Must be lazy-init'ed

	/**
	 * This instance might be a copy of an original QC TO during the query plan
	 * initialization phase. This is a soft-copied reference from the TO.
	 */
	@Transient
	private Plan plan;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public QueryContextEntity()
	{
		super();
	}

	/**
	 * Initialize only the execution ID field.
	 * 
	 * @param executionId
	 *            execution ID
	 * @return {@link QueryContextEntity} instance
	 */
	public static QueryContextEntity newInstanceWithExecutionId()
	{
		final QueryContextEntity instance = new QueryContextEntity();
		generateExecutionId(instance);
		return instance;
	}

	/**
	 * A copy-constructor factory method.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static QueryContextEntity newCopy(final QueryContext other)
	{
		return new QueryContextEntity().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Create a copy of this instance (not a full deep-copy; behaves like
	 * {@link #copyFrom(QueryContext)}).
	 * 
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public QueryContextEntity copy()
	{
		return new QueryContextEntity().copyFrom(this);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public QueryContextEntity copyFrom(final QueryContext other)
	{
		if (other == null)
		{
			return this;
		}

		// Copy super-class fields
		super.copyFrom(other);

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references

		// Do not alter the ID of this entity if it's already set
		if (getId() == null)
		{
			setId(other.getId());
		}
		// Do not alter the user ID of this entity if it's already set
		if (getUserId() == null)
		{
			setUserId(other.getUserId());
		}
		this.state = other.getState();
		this.isStale = other.isStale();

		if (instanceOf(other, QueryContextTo.class))
		{
			// Copy QueryContextTo fields
			final QueryContextTo otherTo = (QueryContextTo) other;

			if (otherTo.getParentId() != null)
			{
				final QueryContextEntity contextEntity = new QueryContextEntity();
				contextEntity.setId(otherTo.getParentId());
				setParent(contextEntity);
			}

			// Do not alter associatedResult if it's already set
			if (this.associatedResult == null && otherTo.getAssociatedResultId() != null)
			{
				final QueryContextEntity contextEntity = new QueryContextEntity();
				contextEntity.setId(otherTo.getAssociatedResultId());
				setAssociatedResult(contextEntity);
			}
		}

		// Time interval might not be statically-initialized yet if this method is called
		// from a superclass.
		if (timeInterval == null)
		{
			timeInterval = new TimeIntervalEntity();
		}
		// Copy fields that are managed by state handlers because we want to preserve
		// their values in this copy instance, and not allow handlers to potentially
		// update them (e.g. timestamp fields)
		this.timeInterval.copyFrom(other.getTimeInterval());

		// Copy associations - deep copies
		this.resultContext.copyFrom(other.getResultContext());

		// Copy new status only if it exists with a non-trivial message in the other
		// object. We don't want to add null to the status list and then attempt to
		// persist it.
		final StatusMetaData otherCurrentStatus = other.getCurrentStatus();
		final StatusMetaData thisCurrentStatus = getCurrentStatus();
		if ((otherCurrentStatus != null) && !otherCurrentStatus.equals(thisCurrentStatus))
		{
			setCurrentStatus(otherCurrentStatus);
		}

		return this;
	}

	// ========================= IMPLEMENTATION: HasIdentifier =============

	/**
	 * @return
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: HasUserIdentifier =========

	/**
	 * @return
	 * @see edu.utah.further.security.api.HasUserIdentifier#getUserId()
	 */
	@Override
	public String getUserId()
	{
		return userId;
	}

	// ========================= IMPLEMENTATION: QueryContext ==============

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
	 * Is this entity persistent or not.
	 * 
	 * @return <code>true</code> if and only if this is a persistent {@link QueryContext}
	 *         implementation
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isPersistent()
	 */
	@Override
	public boolean isPersistent()
	{
		return true;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContext#getState()
	 */
	@Override
	public QueryState getState()
	{
		return state;
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
	 * Return the identityResolutionType property.
	 *
	 * @return the identityResolutionType
	 */
	@Override
	public IdentityResolutionType getIdentityResolutionType()
	{
		return identityResolutionType;
	}

	/**
	 * Set a new value for the identityResolutionType property.
	 *
	 * @param identityResolutionType the identityResolutionType to set
	 */
	@Override
	public void setIdentityResolutionType(final IdentityResolutionType identityResolutionType)
	{
		this.identityResolutionType = identityResolutionType;
	}

	/**
	 * Return the parent {@link QueryContext} of this object, if this is a child
	 * {@link QueryContext}. Otherwise returns <code>null</code>.
	 * 
	 * @return the parent
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getParent()
	 */
	@Override
	public QueryContextEntity getParent()
	{
		return parent;
	}

	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parentId
	 *            the parent to set. Must be of type {@link QueryContextEntity}!
	 */
	@Override
	public void setParent(final QueryContext parent)
	{
		this.parent = (QueryContextEntity) parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getAssociatedResult()
	 */
	@Override
	public QueryContext getAssociatedResult()
	{
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
		this.associatedResult = QueryContextEntity.newCopy(queryContext);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContext#getTimeInterval()
	 */
	@Override
	public ImmutableTimeInterval getTimeInterval()
	{
		if (timeInterval == null)
		{
			timeInterval = new TimeIntervalEntity();
		}
		return timeInterval;
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
	 * @param isStale
	 *            the isStale to set
	 */
	@Override
	public void setStale()
	{
		this.isStale = true;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContext#getStart()
	 */
	@Override
	public Timestamp getStartDate()
	{
		return getTimeInterval().getStart();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getEndDate()
	 */
	@Override
	public Timestamp getEndDate()
	{
		return getTimeInterval().getEnd();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueries()
	 */
	@Override
	public List<SearchQuery> getQueries()
	{
		return queries;
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
	 *            the resultContext to set. Must be of type {@link ResultContextEntity}
	 *            here.
	 */
	@Override
	public void setResultContext(final ResultContext resultContext)
	{
		this.resultContext = (ResultContextEntity) resultContext;
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
	 * Set the current status event of this context. Adds the event to the status event
	 * history list.
	 * 
	 * @param statusMetaData
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setCurrentStatus(edu.utah.further.fqe.ds.api.domain.StatusMetaData)
	 */
	@Override
	public void setCurrentStatus(final StatusMetaData statusMetaData)
	{
		this.currentStatus = StatusMetaDataEntity.newCopy(statusMetaData);
		currentStatus.setQueryContext(this);
		statuses.add(currentStatus);
		if (log.isTraceEnabled())
		{
			log.trace("setCurrentStatus() " + statusMetaData);
		}
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
		return statuses;
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
	 * @param namespaceId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setTargetNamespaceId(java.lang.Long)
	 */
	@Override
	public void setTargetNamespaceId(final Long namespaceId)
	{
		this.targetNamespaceId = namespaceId;

	}

	/**
	 * Return the resultViews property.
	 * 
	 * @return the resultViews
	 */
	@Override
	public Map<ResultType, ResultContext> getResultViews()
	{
		synchronizeResultViewsMapWithSet();
		return CollectionUtil.<ResultType, ResultContext> newMap(resultViewsMap);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public ResultContext getResultView(final ResultType key)
	{
		synchronizeResultViewsMapWithSet();
		return resultViewsMap.get(key);
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
		// Create a new entry of the view "value"
		final ResultContextKeyEntity key = newKey(type);
		final ResultContextEntity valueCopy = ResultContextEntity.newCopy(value);
		key.setValue(valueCopy);

		// Update both the set and map
		resultViews.add(key);
		synchronizeResultViewsMapWithSet();
		return resultViewsMap.put(type, valueCopy);
	}

	/**
	 * @param other
	 * @see edu.utah.further.fqe.ds.api.domain.HasResultViews#setResultViews(java.util.Map)
	 */
	@Override
	public void setResultViews(
			final Map<ResultType, ? extends ResultContext> other)
	{
		throw new UnsupportedOperationException(
				"use addResult() instead for the time being");
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public ResultContext removeResultView(final ResultType key)
	{
		for (final ResultContextEntry entry : resultViews) {
			if (entry.getKey().getType().equals(key)) {
				resultViews.remove(entry);
			}
		}
		synchronizeResultViewsMapWithSet();
		return resultViewsMap.remove(key);
	}

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clearResultViews()
	{
		resultViews.clear();
		synchronizeResultViewsMapWithSet();
		resultViewsMap.clear();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#getPlan()
	 */
	@Override
	public Plan getPlan()
	{
		return plan;
	}

	/**
	 * Soft-copy plan.
	 * 
	 * @param otherPlan
	 *            another QC object's plan
	 * @param plan
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#setPlan(edu.utah.further.fqe.ds.api.domain.plan.Plan)
	 */
	@Override
	public void setPlan(final Plan otherPlan)
	{
		this.plan = otherPlan;
	}

	/**
	 * @return
	 * @throws CloneNotSupportedException
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

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
		return SearchQueryEntity.newCopy(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#setQueries(java.util.Collection
	 * )
	 */
	@Override
	protected final void setQueries(final List<? extends SearchQuery> queries)
	{
		if (queries == null)
		{
			return;
		}

		if (getQueries().size() == 0)
		{
			addQueries(queries);
		}
		else
		{
			Validate.isTrue(getQueries().size() == queries.size(),
					"Local queries and copy queries must be the same size.");

			for (int i = 0; i < getQueries().size(); i++)
			{
				// Keep all of the persistent information and copy anything new
				((SearchQueryEntity) getQueries().get(i)).copyFrom(queries.get(i));
			}
		}

	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext#newResultContext()
	 */
	@Override
	protected ResultContext newResultContext()
	{
		return new ResultContextEntity();
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	@Override
	protected ResultContextKeyEntity newKey(final ResultType type)
	{
		return new ResultContextKeyEntity(type);
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

	/**
	 * Only to be used in constructors/copy instance methods. Set a new value for the
	 * executionId property.
	 * 
	 * @param executionId
	 *            the executionId to set
	 */
	@Override
	protected void setExecutionId(final String executionId)
	{
		this.executionId = executionId;
	}

	/**
	 * Set a new value for the queueDate property. Should be invoked by state classes
	 * only.
	 * 
	 * @param queueDate
	 *            the queueDate to set
	 */
	@Override
	protected void setQueueDate(final Date queueDate)
	{
		this.queueDate = getTimestampNullSafe(queueDate);
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
			((QueryContextEntity) target).state = newState;
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
			((QueryContextEntity) target).timeInterval.setEnd(endDate);
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
			((QueryContextEntity) target).setQueueDate(queueDate);
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
			((QueryContextEntity) target).timeInterval.setStart(startDate);
		}
	};

	/**
	 * Only to be used in constructors/copy instance methods. Set a new value for the id
	 * property.
	 * 
	 * @param id
	 */
	private void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Set a new value for the userId property.
	 * 
	 * @param userId
	 *            the userId to set
	 */
	private void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/**
	 * Populates the result views map upon the first time it is accessed through our API,
	 * which always is after Hibernate populates the {@link #resultViews} set when loading
	 * this entity.
	 */
	private void synchronizeResultViewsMapWithSet()
	{
		if (resultViewsMap == null)
		{
			resultViewsMap = CollectionUtil.newMap();
			for (final PublicMapEntry<? extends ResultContextKey, ? extends ResultContext> key : resultViews)
			{
				resultViewsMap.put(key.getKey().getType(), key.getValue());
			}
		}
	}
}
