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
package edu.utah.further.fqe.impl.service.plan;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.AbstractQueryContext;
import edu.utah.further.fqe.ds.api.domain.IdentityResolutionType;
import edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval;
import edu.utah.further.fqe.ds.api.domain.QueryAction;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.QueryType;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * A decorator of {@link QueryContext}. The only difference between this class and
 * {@link AbstractQueryContext} and sub-classes is the {@link #equals(Object)} and
 * {@link #hashCode()} methods, which determine equality based on the
 * <code>executionId</code> field.
 * <p>
 * {@link QueryPlanPhasedImpl} uses {@link QueryJob} as its dependency graph vertex type.
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
 * @version Dec 21, 2010
 */
public final class QueryJob implements QueryContext
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Delegate.
	 */
	private final QueryContext delegate;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param delegate
	 */
	public QueryJob(final QueryContext delegate)
	{
		super();
		this.delegate = delegate;
	}

	// ========================= IMPL: Object ==============================

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final QueryContext that = (QueryContext) obj;
		return new EqualsBuilder()
				.append(this.getExecutionId(), that.getExecutionId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getExecutionId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		final String executionId = getExecutionId();
		return (executionId == null) ? "broadcast query" : executionId;
	}

	// ========================= IMPL: QueryContext ========================

	/**
	 * 
	 * @see edu.utah.further.core.api.state.Switch#start()
	 */
	@Override
	public void start()
	{
		delegate.start();
	}

	/**
	 * @return
	 * @see edu.utah.further.security.api.HasUserIdentifier#getUserId()
	 */
	@Override
	public String getUserId()
	{
		return delegate.getUserId();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getOriginId()
	 */
	@Override
	public Long getOriginId()
	{
		return delegate.getOriginId();
	}

	/**
	 * @param originId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setOriginId(java.lang.Long)
	 */
	@Override
	public void setOriginId(final Long originId)
	{
		delegate.setOriginId(originId);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.HasNumRecords#getNumRecords()
	 */
	@Override
	public long getNumRecords()
	{
		return delegate.getNumRecords();
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#queue()
	 */
	@Override
	public void queue()
	{
		delegate.queue();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return delegate.getId();
	}

	/**
	 * 
	 * @see edu.utah.further.core.api.state.Switch#stop()
	 */
	@Override
	public void stop()
	{
		delegate.stop();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.Switch#isStarted()
	 */
	@Override
	public boolean isStarted()
	{
		return delegate.isStarted();
	}

	/**
	 * @param numRecords
	 * @see edu.utah.further.fqe.ds.api.domain.HasNumRecords#setNumRecords(long)
	 */
	@Override
	public void setNumRecords(final long numRecords)
	{
		delegate.setNumRecords(numRecords);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public QueryContext copy()
	{
		return delegate.copy();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.Switch#isStopped()
	 */
	@Override
	public boolean isStopped()
	{
		return delegate.isStopped();
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#fail()
	 */
	@Override
	public void fail()
	{
		delegate.fail();
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#finish()
	 */
	@Override
	public void finish()
	{
		delegate.finish();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.StateMachine#getState()
	 */
	@Override
	public QueryState getState()
	{
		return delegate.getState();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.StateMachine#getActions()
	 */
	@Override
	public SortedSet<QueryAction> getActions()
	{
		return delegate.getActions();
	}

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public QueryContext copyFrom(final QueryContext other)
	{
		return delegate.copyFrom(other);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getExecutionId()
	 */
	@Override
	public String getExecutionId()
	{
		return delegate.getExecutionId();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getDataSourceId()
	 */
	@Override
	public String getDataSourceId()
	{
		return delegate.getDataSourceId();
	}

	/**
	 * @param dataSourceId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setDataSourceId(java.lang.String)
	 */
	@Override
	public void setDataSourceId(final String dataSourceId)
	{
		delegate.setDataSourceId(dataSourceId);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueryType()
	 */
	@Override
	public QueryType getQueryType()
	{
		return delegate.getQueryType();
	}

	/**
	 * @param dataSourceId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setQueryType(java.lang.String)
	 */
	@Override
	public void setQueryType(final QueryType queryType)
	{
		delegate.setQueryType(queryType);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getIdentityResolutionType()
	 */
	@Override
	public IdentityResolutionType getIdentityResolutionType()
	{
		return delegate.getIdentityResolutionType();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setIdentityResolutionType(edu.utah.further.fqe.ds.api.domain.IdentityResolutionType)
	 */
	@Override
	public void setIdentityResolutionType(final IdentityResolutionType identityResolutionType)
	{
		delegate.setIdentityResolutionType(identityResolutionType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getParent()
	 */
	@Override
	public QueryContext getParent()
	{
		return delegate.getParent();
	}

	/**
	 * @param parent
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setParent(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void setParent(final QueryContext parent)
	{
		delegate.setParent(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getAssociatedResult()
	 */
	@Override
	public QueryContext getAssociatedResult()
	{
		return delegate.getAssociatedResult();
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
		delegate.setAssociatedResult(queryContext);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQuery()
	 */
	@Override
	public SearchQuery getQuery()
	{
		return delegate.getQuery();
	}

	/**
	 * @param qid
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueryByQid(java.lang.Long)
	 */
	@Override
	public SearchQuery getQueryByQid(final Long qid)
	{
		return delegate.getQueryByQid(qid);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueries()
	 */
	@Override
	public List<SearchQuery> getQueries()
	{
		return delegate.getQueries();
	}

	/**
	 * @param searchQuery
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setQuery(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public void setQuery(final SearchQuery searchQuery)
	{
		delegate.setQuery(searchQuery);
	}

	/**
	 * @param query
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#addQuery(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public void addQuery(final SearchQuery query)
	{
		delegate.addQuery(query);
	}

	/**
	 * @param searchQueries
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setQueries(java.util.Collection)
	 */
	@Override
	public void setQueries(final Collection<? extends SearchQuery> searchQueries)
	{
		delegate.setQueries(searchQueries);
	}

	/**
	 * @param searchQueryToAdd
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#addQueries(java.util.Collection)
	 */
	@Override
	public void addQueries(final Collection<? extends SearchQuery> searchQueryToAdd)
	{
		delegate.addQueries(searchQueryToAdd);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getNumQueries()
	 */
	@Override
	public int getNumQueries()
	{
		return delegate.getNumQueries();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueueDate()
	 */
	@Override
	public Date getQueueDate()
	{
		return delegate.getQueueDate();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueueTime()
	 */
	@Override
	public Long getQueueTime()
	{
		return delegate.getQueueTime();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStartDate()
	 */
	@Override
	public Date getStartDate()
	{
		return delegate.getStartDate();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStartTime()
	 */
	@Override
	public Long getStartTime()
	{
		return delegate.getStartTime();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStaleDateTime()
	 */
	@Override
	public Date getStaleDateTime()
	{
		return delegate.getStaleDateTime();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isStale()
	 */
	@Override
	public boolean isStale()
	{
		return delegate.isStale();
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setStale()
	 */
	@Override
	public void setStale()
	{
		delegate.setStale();
	}

	/**
	 * @param staleDateTime
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setStaleDateTime(java.util.Date)
	 */
	@Override
	public void setStaleDateTime(final Date staleDateTime)
	{
		delegate.setStaleDateTime(staleDateTime);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getEndDate()
	 */
	@Override
	public Date getEndDate()
	{
		return delegate.getEndDate();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getEndTime()
	 */
	@Override
	public Long getEndTime()
	{
		return delegate.getEndTime();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getTimeInterval()
	 */
	@Override
	public ImmutableTimeInterval getTimeInterval()
	{
		return delegate.getTimeInterval();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getResultContext()
	 */
	@Override
	public ResultContext getResultContext()
	{
		return delegate.getResultContext();
	}

	/**
	 * @param resultContext
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setResultContext(edu.utah.further.fqe.ds.api.domain.ResultContext)
	 */
	@Override
	public void setResultContext(final ResultContext resultContext)
	{
		delegate.setResultContext(resultContext);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getCurrentStatus()
	 */
	@Override
	public StatusMetaData getCurrentStatus()
	{
		return delegate.getCurrentStatus();
	}

	/**
	 * @param currentStatus
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setCurrentStatus(edu.utah.further.fqe.ds.api.domain.StatusMetaData)
	 */
	@Override
	public void setCurrentStatus(final StatusMetaData currentStatus)
	{
		delegate.setCurrentStatus(currentStatus);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getMinRespondingDataSources()
	 */
	@Override
	public int getMinRespondingDataSources()
	{
		return delegate.getMinRespondingDataSources();
	}

	/**
	 * @param minRespondingDataSources
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setMinRespondingDataSources(int)
	 */
	@Override
	public void setMinRespondingDataSources(final int minRespondingDataSources)
	{
		delegate.setMinRespondingDataSources(minRespondingDataSources);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getMaxRespondingDataSources()
	 */
	@Override
	public int getMaxRespondingDataSources()
	{
		return delegate.getMaxRespondingDataSources();
	}

	/**
	 * @param maxRespondingDataSources
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setMaxRespondingDataSources(int)
	 */
	@Override
	public void setMaxRespondingDataSources(final int maxRespondingDataSources)
	{
		delegate.setMaxRespondingDataSources(maxRespondingDataSources);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getStatuses()
	 */
	@Override
	public List<StatusMetaData> getStatuses()
	{
		return delegate.getStatuses();
	}

	/**
	 * @param namespaceId
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setTargetNamespaceId(java.lang.Long)
	 */
	@Override
	public void setTargetNamespaceId(final Long namespaceId)
	{
		delegate.setTargetNamespaceId(namespaceId);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getTargetNamespaceId()
	 */
	@Override
	public Long getTargetNamespaceId()
	{
		return delegate.getTargetNamespaceId();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isPersistent()
	 */
	@Override
	public boolean isPersistent()
	{
		return delegate.isPersistent();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isFailed()
	 */
	@Override
	public boolean isFailed()
	{
		return delegate.isFailed();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isInFinalState()
	 */
	@Override
	public boolean isInFinalState()
	{
		return delegate.isInFinalState();
	}

	/**
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#clearResultViews()
	 */
	@Override
	public void clearResultViews()
	{
		delegate.clearResultViews();
	}

	/**
	 * @param key
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#removeResultView(edu.utah.further.fqe.ds.api.domain.ResultContextKey)
	 */
	@Override
	public ResultContext removeResultView(final ResultType key)
	{
		return delegate.removeResultView(key);
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param resultContext
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#addResultView(edu.utah.further.fqe.ds.api.service.results.ResultType,
	 *      edu.utah.further.fqe.ds.api.domain.ResultContext)
	 */
	@Override
	public ResultContext addResultView(final ResultType type,
			final ResultContext resultContext)
	{
		return delegate.addResultView(type, resultContext);
	}

	/**
	 * @param other
	 * @see edu.utah.further.fqe.ds.api.domain.HasResultViews#setResultViews(java.util.Map)
	 */
	@Override
	public void setResultViews(
			final Map<ResultType, ? extends ResultContext> other)
	{
		delegate.setResultViews(other);
	}

	/**
	 * @param key
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getResultView(edu.utah.further.fqe.ds.api.domain.ResultContextKey)
	 */
	@Override
	public ResultContext getResultView(final ResultType key)
	{
		return delegate.getResultView(key);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getResultViews()
	 */
	@Override
	public Map<ResultType, ResultContext> getResultViews()
	{
		return delegate.getResultViews();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getPlan()
	 */
	@Override
	public Plan getPlan()
	{
		return delegate.getPlan();
	}

	/**
	 * @param plan
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setPlan(edu.utah.further.fqe.ds.api.domain.plan.Plan)
	 */
	@Override
	public void setPlan(final Plan plan)
	{
		delegate.setPlan(plan);
	}

	// ========================= METHODS ===================================

	/**
	 * Return the delegate property.
	 * 
	 * @return the delegate
	 */
	public QueryContext getDelegate()
	{
		return delegate;
	}

	/**
	 * Evaluate dependency expressions in all {@link SearchQuery}s of this job.
	 */
	public void evaluateDependencyExpressions()
	{
		// Substitute query dependency expression (results from previous queries)
		final List<SearchQuery> queries = CollectionUtil.newList(getQueries());
		for (final SearchQuery query : queries)
		{
			final SearchQueryDeEvaluator evaluator = new SearchQueryDeEvaluator(query);
			evaluator.evaluate();
		}
	}
}
