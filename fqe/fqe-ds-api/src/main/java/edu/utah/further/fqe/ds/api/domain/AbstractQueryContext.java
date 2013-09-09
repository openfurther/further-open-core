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

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.core.api.time.TimeUtil.getDateAsTime;
import static org.slf4j.LoggerFactory.getLogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.DefaultImplementation;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.plan.PlanToImpl;
import edu.utah.further.fqe.ds.api.util.FqeDsApiResourceLocator;

/**
 * A base class of {@link QueryContext} implementations. Using an internal
 * {@link QueryHandler} enum implementation rather than a full-blown friend pattern. If in
 * the future sub-classes need to to extend specific reusable handlers, we may
 * re-implement handlers using the friend pattern.
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
@DefaultImplementation(QueryContext.class)
@XmlTransient
public abstract class AbstractQueryContext implements QueryContext
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractQueryContext.class);

	// ========================= FIELDS ====================================

	/**
	 * Executes private methods; implemented by sub-classes.
	 */
	@XmlTransient
	private final PrivateSection privateSectionExecutor;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, for JAXB-related implementations. No fields are set.
	 */
	protected AbstractQueryContext()
	{
		super();
		this.privateSectionExecutor = getPrivateSectionExecutor();
	}

	// /**
	// * A copy-constructor.
	// *
	// * @param other
	// * other object to deep-copy fields from
	// */
	// protected AbstractQueryContext(final QueryContext other)
	// {
	// this();
	// copyFrom(other);
	// }

	// ========================= IMPLEMENTATION: Object ====================

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
		return new EqualsBuilder().append(this.getId(), that.getId())
		// .append(this.getExecutionId(), that.getExecutionId()) // see QueryJob.equals()
				.append(this.getState(), that.getState())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).append(getState()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", getId())
				.append("executionId", getExecutionId())
				.append("originId", getOriginId())
				.append("dataSourceId", getDataSourceId())
				.append("state", getState())
				.append("queryType", getQueryType());
		if (getParent() != null)
		{
			builder.append("parent ID", getParent().getId()).append("DS ID",
					getDataSourceId());
		}
		builder.append("numRecords", getNumRecords());
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AbstractQueryContext copyFrom(final QueryContext other)
	{
		if (other == null)
		{
			return this;
		}

		setOriginId(other.getOriginId());
		setExecutionId(other.getExecutionId());
		setDataSourceId(other.getDataSourceId());
		setQueryType(other.getQueryType());
		setMinRespondingDataSources(other.getMinRespondingDataSources());
		setMaxRespondingDataSources(other.getMaxRespondingDataSources());
		setQueries(other.getQueries());
		setStaleDateTime(other.getStaleDateTime());
		setQueueDate(other.getQueueDate());

		// If the other result view is empty, set ours to null to eliminate empty
		// resultView XML element
		copyResultViews(other);

		// Soft-copy plan
		setPlan(other.getPlan());

		return this;
	}

	// ========================= IMPLEMENTATION: QueryContext ==============

	/**
	 * Return the first query.
	 *
	 * @return the query
	 */
	@Override
	public final SearchQuery getQuery()
	{
		return getQueries().isEmpty() ? null : getQueries().get(0);
	}

	/**
	 * Return a search query with a specified QID (unique SQ identifier within this QC).
	 *
	 * @param qid
	 *            search query ID within this QC
	 * @return the query
	 */
	@Override
	public final SearchQuery getQueryByQid(final Long qid)
	{
		if (qid == null)
		{
			return null;
		}

		// Slow implementation; may use a Map in the future if query list becomes large
		for (final SearchQuery query : getQueries())
		{
			if (qid.equals(query.getId()))
			{
				return query;
			}
		}
		return null;
	}

	/**
	 * Set a new single query on this object. This means that the list of queries will now
	 * consist of one element.
	 *
	 * @param query
	 *            the query to set
	 */
	@Override
	public final void setQuery(final SearchQuery query)
	{
		getQueries().clear();
		addQuery(query);
	}

	/**
	 * Add a query to this object.
	 *
	 * @param query
	 *            the searvh query to to add
	 */
	@Override
	public final void addQuery(final SearchQuery query)
	{
		getQueries().add(newSearchQuery(query));
	}

	/**
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setQueries(java.util.Collection)
	 * @param searchQueries
	 */
	@Override
	public void setQueries(final Collection<? extends SearchQuery> searchQueries)
	{
		getQueries().clear();
		addQueries(searchQueries);
	}

	/**
	 * @param queries
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#addQueries(Collection)
	 */
	@Override
	public final void addQueries(final Collection<? extends SearchQuery> queries)
	{
		for (final SearchQuery child : queries)
		{
			addQuery(child);
		}
	}

	/**
	 * @return
	 */
	@Override
	public final int getNumQueries()
	{
		return getQueries().size();
	}

	/**
	 * Returns a sorted set of permissible actions based upon the current state. Delegates
	 * to the current state handler.
	 * <p>
	 * Note: do not override this method! It is non-final to allow dynamic proxying of
	 * persistent entities (e.g. in Hibernate).
	 *
	 * @return sorted set of permissible actions from the current state
	 * @see edu.utah.further.core.api.state.StateMachine#getActions()
	 */
	@Override
	@Final
	public final SortedSet<QueryAction> getActions()
	{
		return getHandler().getActions(this);
	}

	/**
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#fail()
	 */
	@Override
	@Final
	public final void fail()
	{
		getHandler().fail(this);
	}

	/**
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#finish()
	 */
	@Override
	@Final
	public final void finish()
	{
		getHandler().finish(this);
	}

	/**
	 * @see edu.utah.further.fqe.ds.api.domain.QueryActor#queue()
	 */
	@Override
	@Final
	public final void queue()
	{
		getHandler().queue(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.Switch#isStarted()
	 */
	@Override
	@Final
	public final boolean isStarted()
	{
		return getHandler().isStarted(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.state.Switch#isStopped()
	 */
	@Override
	@Final
	public boolean isStopped()
	{
		return getHandler().isStopped(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isFailed()
	 */
	@Override
	@Final
	public final boolean isFailed()
	{
		return getHandler().isFailed(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#isInFinalState()
	 */
	@Override
	@Final
	public final boolean isInFinalState()
	{
		return getActions().isEmpty();
	}

	/**
	 * @see edu.utah.further.core.api.state.Switch#start()
	 */
	@Override
	@Final
	public final void start()
	{
		getHandler().start(this);
	}

	/**
	 * @see edu.utah.further.core.api.state.Switch#stop()
	 */
	@Override
	@Final
	public final void stop()
	{
		getHandler().stop(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getQueueTime()
	 */
	@Override
	public final Long getQueueTime()
	{
		return getDateAsTime(getQueueDate());
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.to.QueryContextTo#getStartTime()
	 */
	@Override
	public final Long getStartTime()
	{
		return getDateAsTime(getStartDate());
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getEndTime()
	 */
	@Override
	public final Long getEndTime()
	{
		return getDateAsTime(getEndDate());
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.HasNumRecords#getNumRecords()
	 */
	@Override
	public final long getNumRecords()
	{
		final ResultContext rsmd = getResultContext();
		return (rsmd == null) ? Constants.INVALID_VALUE_LONG : rsmd.getNumRecords();
	}

	/**
	 * @param numRecords
	 * @see edu.utah.further.fqe.ds.api.domain.HasNumRecords#setNumRecords(long)
	 */
	@Override
	public final void setNumRecords(final long numRecords)
	{
		final ResultContext rsmd = getResultContext();
		if (rsmd == null)
		{
			setResultContext(newResultContext());
		}
		getResultContext().setNumRecords(numRecords);
	}

	/**
	 * Return a resultContext by key parameters.
	 *
	 * @param type
	 * @param intersectionIndex
	 * @return a resultContext
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public final ResultContext getResultView(final ResultType type,
			final Integer intersectionIndex)

	{
		return getResultView(newKey(type, intersectionIndex));
	}

	/**
	 * Return the plan property.
	 *
	 * @return the plan
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getPlan()
	 */
	@Override
	public Plan getPlan()
	{
		throw new UnsupportedOperationException(
				"getPlan(): Must override in selected sub-classes that support this features");
	}

	/**
	 * Set a new value for the plan property. No defensive copy is made at the moment.
	 *
	 * @param plan
	 *            the plan to set
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#setPlan(PlanToImpl)
	 */
	@Override
	public void setPlan(final Plan plan)
	{
		throw new UnsupportedOperationException(
				"setPlan(): Must override in selected sub-classes that support this features");
	}

	// ========================= STATE HANDLERS ============================

	/**
	 * Set the query collection on this object.
	 *
	 * @param queries
	 *            the search queries to set
	 */
	protected abstract void setQueries(final List<? extends SearchQuery> queries);

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	protected ResultContextKey newKey(final ResultType type,
			final Integer intersectionIndex)
	{
		return new ResultContextKeyImpl(type, intersectionIndex);
	}

	/**
	 * Default query context state handlers. Each handler corresponds to a state and
	 * implements its state behavior within an enumerated constant inner class.
	 * Sub-classes can override this behaviour by supplying their implementation of
	 * {@link QueryContext#getHandler()}.
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 *
	 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
	 * @version May 28, 2009
	 */
	private enum DefaultQueryHandler implements QueryHandler
	{
		// ========================= ENUMERATED CONSTANTS ======================

		/**
		 * The query's context is created and transient.
		 */
		SUBMITTED(QueryState.SUBMITTED, false, false)
		{
			/**
			 * Persist query to query database and add it to the execution queue.
			 *
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#queue(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void queue(final AbstractQueryContext target)
			{
				target.updateState(QueryState.QUEUED);
				if (target.getStaleDateTime() == null)
				{
					target.setStaleDateTime(FqeDsApiResourceLocator
							.getInstance()
							.getStaleDateTimeFactory()
							.getStaleDateTime());
				}
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.toSortedSet(QueryAction.QUEUE);
			}

		},

		/**
		 * The query's context has been persisted to the database. The query is queued for
		 * processing by the FQE.
		 */
		QUEUED(QueryState.QUEUED, false, false)
		{
			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#enterState(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public void enterState(final QueryContext target)
			{
				final AbstractQueryContext aqc = (AbstractQueryContext) target;
				aqc.privateSectionExecutor.setQueueDate(aqc, now());
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#start(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void start(final AbstractQueryContext target)
			{
				target.updateState(QueryState.EXECUTING);
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#fail(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void fail(final AbstractQueryContext target)
			{
				target.updateState(QueryState.FAILED);
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.toSortedSet(QueryAction.START, QueryAction.FAIL);
			}
		},

		/**
		 * The query is currently running.
		 */
		EXECUTING(QueryState.EXECUTING, true, false)
		{
			/**
			 * Update parent state. TODO: replace by the observer pattern by having the
			 * parent listen to child context changes. For the time being. the following
			 * is simpler but a little less maintainable.
			 *
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#enterState(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public void enterState(final QueryContext target)
			{
				final AbstractQueryContext aqc = (AbstractQueryContext) target;
				if (aqc.getStartDate() == null)
				{
					aqc.privateSectionExecutor.setStartDate(aqc, now());
				}
				aqc.privateSectionExecutor.setEndDate(aqc, null);
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#stop(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void stop(final AbstractQueryContext target)
			{
				target.updateState(QueryState.STOPPED);
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#finish(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void finish(final AbstractQueryContext target)
			{
				target.updateState(QueryState.COMPLETED);
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#fail(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void fail(final AbstractQueryContext target)
			{
				target.updateState(QueryState.FAILED);
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.toSortedSet(QueryAction.STOP, QueryAction.FINISH,
						QueryAction.FAIL);
			}
		},

		// /**
		// * A sub-state of {@link #EXECUTING} for the execution phase of translating the
		// * logical query to physical queries. In principle, this needs to be a separate
		// class
		// * and may have different values for each data source. TODO: move to a separate
		// state
		// * class and make it data-source-dependent.
		// */
		// TRANSLATING_QUERY,
		//
		// /**
		// * A sub-state of {@link #EXECUTING} for the execution phase of translating the
		// * physical result sets to logical result set. In principle, this needs to be a
		// * separate class and may have different values for each data source. TODO: move
		// to
		// a
		// * separate state class and make it data-source-dependent.
		// */
		// TRANSLATING_RESULT,

		/**
		 * Query execution is stopped. This is a recoverable state.
		 */
		STOPPED(QueryState.STOPPED, false, false)
		{
			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#enterState(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public void enterState(final QueryContext target)
			{
				final AbstractQueryContext aqc = (AbstractQueryContext) target;
				aqc.privateSectionExecutor.setEndDate(aqc, now());
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#start(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void start(final AbstractQueryContext target)
			{
				target.updateState(QueryState.EXECUTING);
			}

			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#fail(edu.utah.further.fqe.ds.api.domain.AbstractQueryContext)
			 */
			@Override
			public void fail(final AbstractQueryContext target)
			{
				target.updateState(QueryState.FAILED);
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.toSortedSet(QueryAction.FAIL, QueryAction.START);
			}
		},

		/**
		 * Query execution is complete. This is an unrecoverable state.
		 */
		COMPLETED(QueryState.COMPLETED, false, false)
		{
			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#enterState(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public void enterState(final QueryContext target)
			{
				final AbstractQueryContext aqc = (AbstractQueryContext) target;
				aqc.privateSectionExecutor.setEndDate(aqc, now());
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.<QueryAction> emptySortedSet();
			}
		},

		/**
		 * Query execution failed. This is an unrecoverable state.
		 */
		FAILED(QueryState.FAILED, false, true)
		{
			/**
			 * @param target
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#enterState(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public void enterState(final QueryContext target)
			{
				final AbstractQueryContext aqc = (AbstractQueryContext) target;
				aqc.privateSectionExecutor.setEndDate(aqc, now());
			}

			/**
			 * Return the list of actions that can be performed from the current state.
			 * <p>
			 * Meant to be overridden by handler sub-classes determine specific state
			 * behavior.
			 *
			 * @param target
			 *            The target state machine that this handler executes actions for
			 * @see edu.utah.further.fqe.ds.api.domain.AbstractQueryContext.DefaultQueryHandler#getActions(edu.utah.further.fqe.ds.api.domain.QueryContext)
			 */
			@Override
			public SortedSet<QueryAction> getActions(final QueryContext target)
			{
				return CollectionUtil.<QueryAction> emptySortedSet();
			}
		};

		// ========================= CONSTANTS =================================

		/**
		 * A cached map of states to their handlers.
		 */
		private static final Map<QueryState, DefaultQueryHandler> handlers = newMap();

		static
		{
			for (final DefaultQueryHandler handler : values())
			{
				handlers.put(handler.getId(), handler);
			}
		}

		// ========================= FIELDS ====================================

		/**
		 * The state that this handler handles.
		 */
		private final QueryState state;

		/**
		 * Is query execution started in this state.
		 */
		private final boolean started;

		/**
		 * Is the query in a failed state.
		 */
		private final boolean failed;

		// ========================= CONSTRUCTORS ==============================

		/**
		 * @param state
		 * @param started
		 * @param failed
		 */
		private DefaultQueryHandler(final QueryState state, final boolean started,
				final boolean failed)
		{
			this.state = state;
			this.started = started;
			this.failed = failed;
		}

		// ========================= IMPLEMENTATION: HasIdentifier<QueryState> =

		/**
		 * @return
		 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
		 */
		@Override
		public final QueryState getId()
		{
			return state;
		}

		// ========================= IMPLEMENTATION: StateHandler ==============

		/**
		 * Perform operations upon entering this handler's state.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @see edu.utah.further.core.api.state.StateHandler#enterState(edu.utah.further.core.api.state.StateMachine)
		 */
		@Override
		public void enterState(final QueryContext target)
		{
			// Method stub
		}

		/**
		 * Perform operations upon exiting this handler's state.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @see edu.utah.further.core.api.state.StateHandler#exitState(edu.utah.further.core.api.state.StateMachine)
		 */
		@Override
		public void exitState(final QueryContext target)
		{
			// Method stub
		}

		/**
		 * Return the list of actions that can be performed from the current state.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @throws UnsupportedOperationException
		 *             as the default behavior
		 * @see edu.utah.further.core.api.state.Handler#getActions(edu.utah.further.core.api.state.StateMachine)
		 */
		@Override
		public SortedSet<QueryAction> getActions(final QueryContext target)
		{
			throw new UnsupportedOperationException("Action list for state " + this
					+ " is not yet supported");
		}

		// ========================= CONTEXTUAL ACTOR METHODS ==================

		/**
		 * Persist query to query database and add it to the execution queue.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 */
		public void queue(final AbstractQueryContext target)
		{
			// Method stub
			throwBusinessRuleException(getId(), "queue()");
		}

		/**
		 * Abnormally terminate query execution.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 */
		public void fail(final AbstractQueryContext target)
		{
			// Method stub
			throwBusinessRuleException(getId(), "fail()");
		}

		/**
		 * Normally Complete query execution.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 */
		public void finish(final AbstractQueryContext target)
		{
			// Method stub
			throwBusinessRuleException(getId(), "finish()");
		}

		/**
		 * Is query execution started.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @return
		 * @see edu.utah.further.fqe.ds.api.domain.QueryState#isStarted(edu.utah.further.fqe.ds.api.domain.QueryContext)
		 */
		@Override
		public final boolean isStarted(final AbstractQueryContext target)
		{
			return started;
		}

		/**
		 * Is query execution stopped.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @return
		 * @see edu.utah.further.core.api.state.ContextualSwitch#isStopped(java.lang.Object)
		 */
		@Override
		public final boolean isStopped(final AbstractQueryContext target)
		{
			return !isStarted(target);
		}

		/**
		 * Is query in a failed state.
		 *
		 * @param target
		 *            The target query state machine that this handler executes actions
		 *            for
		 * @return <code>true</code> if and only if the query is in failed state
		 */
		public final boolean isFailed(final AbstractQueryContext target)
		{
			return failed;
		}

		/**
		 * Start the query.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @see edu.utah.further.core.api.state.ContextualSwitch#start(java.lang.Object)
		 */
		@Override
		public void start(final AbstractQueryContext target)
		{
			// Method stub
			throwBusinessRuleException(getId(), "start()");
		}

		/**
		 * Stop the query.
		 * <p>
		 * Meant to be overridden by handler sub-classes determine specific state
		 * behavior.
		 *
		 * @param target
		 *            The target state machine that this handler executes actions for
		 * @see edu.utah.further.core.api.state.ContextualSwitch#stop(java.lang.Object)
		 */
		@Override
		public void stop(final AbstractQueryContext target)
		{
			// Method stub
			throwBusinessRuleException(getId(), "stop()");
		}

		// ========================= METHODS ===================================

		/**
		 * Return the handler of a state.
		 *
		 * @param state
		 *            query state
		 * @return corresponding handler
		 */
		public static DefaultQueryHandler valueOf(final QueryState state)
		{
			return handlers.get(state);
		}

		// ========================= PRIVATE METHODS ===========================

		/**
		 * Throw a {@link BusinessRuleException} on an unsupported acction.
		 *
		 * @param state
		 *            current state
		 * @param action
		 *            action method name
		 */
		private static void throwBusinessRuleException(final QueryState state,
				final String action)
		{
			throw new BusinessRuleException("Can't " + action + " from state " + state);
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Generate a random child ID.
	 * <p>
	 * TODO: could be replaced by a builder in the future.
	 *
	 * @param instance
	 *            instance to set child ID on
	 */
	protected static final void generateExecutionId(final AbstractQueryContext instance)
	{
		instance.setExecutionId(UUID.randomUUID().toString());
	}

	/**
	 * Return a deep copy of a search query suitable for insertion into the
	 * {@link #getQueries()} collection.
	 *
	 * @param query
	 *            original search query
	 * @return search query copy
	 */
	abstract protected SearchQuery newSearchQuery(final SearchQuery query);

	/**
	 * Return an empty instance (using no-argument constructor) of the implementation type
	 * of the result set field.
	 *
	 * @return
	 */
	abstract protected ResultContext newResultContext();

	/**
	 * Return the implementation of the private method section of this class.
	 *
	 * @return the implementation of the private method section of this class
	 */
	abstract protected PrivateSection getPrivateSectionExecutor();

	/**
	 * Only to be used in constructors/copy instance methods. Set a new value for the
	 * executionId property.
	 *
	 * @param executionId
	 *            the executionId to set
	 */
	abstract protected void setExecutionId(final String executionId);

	/**
	 * Set a new value for the queueDate property. Should be invoked by state classes
	 * only.
	 *
	 * @param queueDate
	 *            the queueDate to set
	 */
	abstract protected void setQueueDate(final Date queueDate);

	/**
	 * @param other
	 * @return
	 */
	protected final void copyResultViews(final QueryContext other)
	{
		final Map<ResultContextKey, ResultContext> otherResultViews = other
				.getResultViews();
		clearResultViews();
		if (otherResultViews != null)
		{
			for (final Map.Entry<ResultContextKey, ResultContext> entry : otherResultViews
					.entrySet())
			{
				final ResultContextKey key = entry.getKey();
				addResultView(key.getType(), key.getIntersectionIndex(), entry.getValue());
			}
		}
	}

	/**
	 * This section contains methods that should be called only within this class. It is
	 * implemented by sub-classes by passing in an anonymous inner class that implements
	 * {@link AbstractQueryContext#privateSectionExecutor} to all of their constructors.
	 * <p>
	 * Caution: since this interface has to have at least protected visibility, it can be
	 * called by classes in by sub-classes as well.<br/>
	 * DO NOT CALL METHODS IN THIS INTERFACE IN SUB-CLASSES! <br/>
	 * One exception is copy constructors where a class' state has to be initialized and
	 * managed fields accordingly set, which can only be done correctly via the
	 * {@link #setState(AbstractQueryContext, QueryState)} method.
	 */
	/* protected */public// a sad OSGI requirement so that implementations that lie in
	// other bundles don't encounter ClassNotFoundException: PrivateSection
	static interface PrivateSection
	{
		/**
		 * Set the state field of this object to a new value. It is used internally by
		 * <code>updateState()</code>, which should only be called as a call-back by
		 * {@link DefaultQueryHandler} instances.
		 *
		 * @param newState
		 *            new state value
		 * @param target
		 *            the encapsulating object, for call-back.
		 */
		void setState(final AbstractQueryContext target, final QueryState newState);

		/**
		 * Set a new value for the queue date.
		 *
		 * @param queueDate
		 *            the queueDate to set
		 */
		void setQueueDate(final AbstractQueryContext target, Timestamp queueDate);

		/**
		 * Set a new value for the start date.
		 *
		 * @param startDate
		 *            the startDate to set
		 */
		void setStartDate(final AbstractQueryContext target, Timestamp startDate);

		/**
		 * Set a new value for the end date.
		 *
		 * @param endDate
		 *            the endDate to set
		 */
		void setEndDate(final AbstractQueryContext target, Timestamp endDate);
	}

	/**
	 * Update the state of this object.
	 *
	 * @param newState
	 *            new state value
	 */
	private void updateState(final QueryState newState)
	{
		final QueryState oldState = getState();
		if (log.isDebugEnabled())
		{
			log.debug(this + " updating state: old " + oldState + " new " + newState);
		}

		// Exit previous state (if exists)
		if (oldState != null)
		{
			getHandler().exitState(this);
		}

		// Set and enter new state
		privateSectionExecutor.setState(this, newState);

		getHandler().enterState(this);
	}

	/**
	 * Return the handler of the current state
	 *
	 * @return curren state's handler instance
	 */
	private DefaultQueryHandler getHandler()
	{
		return DefaultQueryHandler.valueOf(getState());
	}

	/**
	 * @return
	 */
	private static Timestamp now()
	{
		return new Timestamp(TimeService.getMillis());
	}
	
	/**
	 * Compute the stale date of this query context upon its creation. Returns
	 * <code>null</code> if no resource locator is set up in the current context.
	 *
	 * @return stale date of this query context
	 */
	protected static Date computeStaleDateTime()
	{
		final FqeDsApiResourceLocator instance = FqeDsApiResourceLocator.getInstance();
		return (instance == null) ? null : instance
				.getStaleDateTimeFactory()
				.getStaleDateTime();
	}
}
