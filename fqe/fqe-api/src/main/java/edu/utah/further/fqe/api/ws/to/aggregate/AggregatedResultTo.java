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

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ResultContextKeyToImpl;

/**
 * A federated query result set immutable transfer object implementation. Serializable to-
 * and from XML. Holds histograms of aggregated counts for multiple demographic
 * categories. Used in interacting with the i2b2 front end demographics plugin.
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
@XmlRootElement(namespace = XmlNamespace.FQE, name = AggregatedResultTo.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "key", "categories" })
public final class AggregatedResultTo implements AggregatedResult
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(AggregatedResultTo.class);

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * Useful instance to cache.
	 */
	public static final AggregatedResultTo EMPTY_INSTANCE = new AggregatedResultTo(null,
			null);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "aggregatedResult";

	// ========================= FIELDS ====================================

	/**
	 * Identifies the type of federated results.
	 */
	@XmlElementRef(name = "key", namespace = XmlNamespace.FQE)
	@Final
	// Only set in c-tors and copyFrom()
	private ResultContextKeyToImpl key = new ResultContextKeyToImpl();

	/**
	 * A set of demographic categories.
	 */
	@XmlElementRef(name = "category", namespace = XmlNamespace.FQE)
	private final Set<CategoryTo> categories = CollectionUtil.newSortedSet();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * To be called only by JAXB.
	 */
	public AggregatedResultTo()
	{
		super();
	}

	/**
	 * @param key
	 */
	public AggregatedResultTo(final ResultContextKey key)
	{
		this(key.getType(), key.getIntersectionIndex());
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 */
	public AggregatedResultTo(final ResultType type, final Integer intersectionIndex)
	{
		this.key = new ResultContextKeyToImpl(type, intersectionIndex);
	}

	/**
	 * A copy-constructor factory method.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static AggregatedResultTo newCopy(final AggregatedResult other)
	{
		return new AggregatedResultTo(null, null).copyFrom(other);
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
	public AggregatedResult copy()
	{
		return new AggregatedResultTo(this.getKey()).copyFrom(this);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Note: the <code>resultEntity</code> reference field is not copied.
	 *
	 * @param other
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AggregatedResultTo copyFrom(final AggregatedResult other)
	{
		if (other == null)
		{
			return this;
		}

		this.key = new ResultContextKeyToImpl(other.getKey());

		// Deep-copy histograms
		for (final Category category : other.getCategories())
		{
			addCategory(category);
		}
		return this;
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing demographic categories. They are ordered by their names.
	 *
	 * @param other
	 *            the other {@link CategoryTo} object
	 * @return the result of comparison
	 */
	@Override
	public int compareTo(final AggregatedResult other)
	{
		return new CompareToBuilder()
				.append(this.getKey(), other.getKey())
				.toComparison();
	}

	// ========================= GET & SET =================================

	/**
	 * Return the categories property.
	 *
	 * @return the categories
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResult#getCategories()
	 */
	@Override
	public Set<Category> getCategories()
	{
		return CollectionUtil.<Category> newSet(categories);
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void addCategory(final Category value)
	{
		categories.add(new CategoryTo().copyFrom(value));
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int getSize()
	{
		return categories.size();
	}

	/**
	 * Return the key property.
	 *
	 * @return the key
	 */
	@Override
	public ResultContextKeyToImpl getKey()
	{
		return key;
	}

	// ========================= PRIVATE METHODS ===========================
}