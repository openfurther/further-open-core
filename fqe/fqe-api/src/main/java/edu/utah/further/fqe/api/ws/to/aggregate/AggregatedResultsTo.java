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
package edu.utah.further.fqe.api.ws.to.aggregate;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.ResultContextMapAdapter;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ResultContextKeyToImpl;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;

/**
 * Holds multiple
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
 * @version Mar 23, 2011
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE_DUMMY, name = AggregatedResultsTo.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "results", "resultViews", "numDataSources", "resultStatus" })
public final class AggregatedResultsTo implements AggregatedResults
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(AggregatedResultsTo.class);

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * Useful instance to cache.
	 */
	public static final AggregatedResultsTo EMPTY_INSTANCE = new AggregatedResultsTo();

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "aggregatedResults";

	// ========================= FIELDS ====================================

	/**
	 * A set of demographic results.
	 */
	@XmlElementRef(name = "result", namespace = XmlNamespace.FQE)
	private final Set<AggregatedResultTo> results = CollectionUtil.newSortedSet();

	/**
	 * A set of result views, parameterized by {@link ResultContextKey}s. Concept's
	 * property map. Keyed and sorted by the {@link ResultContextKeyToImpl} natural
	 * ordering.
	 */
	@XmlJavaTypeAdapter(ResultContextMapAdapter.class)
	@XmlElement(name = "resultViews", required = false, namespace = XmlNamespace.FQE)
	private final Map<ResultContextKey, ResultContext> resultViews = CollectionUtil
			.newMap();

	/**
	 * Required of any FQE i2b2 message TO by the i2b2 communication standard.
	 */
	@XmlElement(name = "numDataSources", required = true, namespace = XmlNamespace.FQE)
	private int numDataSources = 0;
	/**
	 * Required of any FQE i2b2 message TO by the i2b2 communication standard.
	 */
	@XmlElement(name = "result_status", required = true, namespace = XmlNamespace.FQE)
	private I2b2ResultStatusTo resultStatus = new I2b2ResultStatusTo();

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Create a copy of this instance (not a full deep-copy; behaves like
	 * {@link #copyFrom(QueryContext)}).
	 *
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public AggregatedResults copy()
	{
		return new AggregatedResultsTo().copyFrom(this);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Note: the <code>resultEntity</code> reference field is not copied.
	 *
	 * @param other
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AggregatedResults copyFrom(final AggregatedResults other)
	{
		if (other == null)
		{
			return this;
		}

		// Copy fields
		setNumDataSources(other.getNumDataSources());

		// Deep-copy histograms
		results.clear();
		for (final AggregatedResult result : other.getResults())
		{
			addResult(result);
		}

		// Deep-copy results
		setResultViews(other.getResultViews());

		return this;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the resultViews property.
	 *
	 * @return the resultViews
	 */
	@Override
	public Map<ResultContextKey, ResultContext> getResultViews()
	{
		return CollectionUtil.<ResultContextKey, ResultContext> newMap(resultViews);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public ResultContext getResultView(final ResultContextKey key)
	{
		return resultViews.get(key);
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
	 * @param type
	 * @param intersectionIndex
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ResultContext addResultView(final ResultType type,
			final Integer intersectionIndex, final ResultContext value)

	{
		return resultViews.put(newKey(type, intersectionIndex),
				ResultContextToImpl.newCopy(value));
	}

	/**
	 * @param other
	 * @see edu.utah.further.fqe.ds.api.domain.HasResultViews#setResultViews(java.util.Map)
	 */
	@Override
	public void setResultViews(
			final Map<? extends ResultContextKey, ? extends ResultContext> other)
	{
		CollectionUtil.setMapElements(resultViews, other);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public ResultContext removeResultView(final ResultContextKey key)
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

	/**
	 * Return the results property.
	 *
	 * @return the results
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults#getResults()
	 */
	@Override
	public Set<AggregatedResult> getResults()
	{
		return CollectionUtil.<AggregatedResult> newSet(results);
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void addResult(final AggregatedResult value)
	{
		results.add(AggregatedResultTo.newCopy(value));
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int getSize()
	{
		return results.size();
	}

	/**
	 * Return the numDataSources property.
	 *
	 * @return the numDataSources
	 */
	@Override
	public int getNumDataSources()
	{
		return numDataSources;
	}

	/**
	 * Set a new value for the numDataSources property.
	 *
	 * @param numDataSources
	 *            the numDataSources to set
	 */
	@Override
	public void setNumDataSources(final int numDataSources)
	{
		this.numDataSources = numDataSources;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param type
	 * @param intersectionIndex
	 * @return
	 */
	private ResultContextKeyToImpl newKey(final ResultType type,
			final Integer intersectionIndex)
	{
		return new ResultContextKeyToImpl(type, intersectionIndex);
	}
}