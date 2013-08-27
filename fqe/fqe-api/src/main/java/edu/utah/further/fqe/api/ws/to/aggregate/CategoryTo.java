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

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.CompareToBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.IdentifiableMapAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.api.util.NaturalStringComparator;
import edu.utah.further.fqe.ds.api.domain.QueryContext;


// ============================
// JAXB annotations
// ============================
/**
 *import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

import edu.utah.further.core.api.xml.XmlNamespace;
------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see http
 *      ://download.oracle.com/javase/6/docs/api/javax/xml/bind/annotation/adapters/XmlAdapter
 *      .html
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 2, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "name", "histogram" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = CategoryTo.ENTITY_NAME)
public final class CategoryTo implements Category
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "category";

	// ========================= FIELDS ====================================

	/**
	 * Name of this category. Serves as the unique identifier of this category within an
	 * {@link AggregatedResult}.
	 */
	@XmlAttribute(name = "name", required = true)
	private String name;

	/**
	 * A JAXB adapter of the histogram (map) into a list. This adapter is responsible for
	 * sorting the map for the view.
	 */
	private static class HistogramMapAdapter
			extends
			IdentifiableMapAdapter<String, Long, HistogramEntryTo, HistogramMapAdapter.ValueType>
	{
		
		
		@SuppressWarnings("unused")
		public HistogramMapAdapter() {
			
			setComparator(new NaturalStringComparator());
		}

		@XmlRootElement(namespace = XmlNamespace.FQE, name = "histogramValueType")
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "histogramValueType")
		public static class ValueType
		{
			@XmlElement(name = "entry", required = false, namespace = XmlNamespace.FQE)
			public List<HistogramEntryTo> list;
		}
	}

	/**
	 * A set of result views, parameterized by {@link String}s. Concept's property map.
	 * Keyed and sorted by the {@link String} natural ordering.
	 */
	@XmlJavaTypeAdapter(HistogramMapAdapter.class)
	@XmlElement(name = "values", required = false, namespace = XmlNamespace.FQE)
	private final Map<String, Long> histogram = CollectionUtil.newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public CategoryTo()
	{
		super();
	}

	/**
	 * Construct a named category.
	 *
	 * @param name
	 *            category's name
	 */
	public CategoryTo(final String name)
	{
		this();
		this.name = name;
	}

	/**
	 * Populated a category.
	 *
	 * @param name
	 *            category's name
	 * @param entries
	 *            entries to add to the category
	 */
	public CategoryTo(final String name, final Map<String, Long> entries)
	{
		this(name);
		histogram.putAll(entries);
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public CategoryTo(final Category other)
	{
		this();
		copyFrom(other);
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
	public int compareTo(final Category other)
	{
		return new CompareToBuilder()
				.append(this.getName(), other.getName())
				.toComparison();
	}

	// ========================= IMPL: Category ============================

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#getKey()
	 * @return
	 */
	@Override
	public String getName()
	{
		return name;
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
	public CategoryTo copy()
	{
		return new CategoryTo().copyFrom(this);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Note: the <code>resultEntity</code> reference field is not copied.
	 *
	 * @param other
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public CategoryTo copyFrom(final Category other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy values, collections
		this.name = other.getName();
		CollectionUtil.setMapElements(histogram, other.getEntries());

		return this;
	}

	// ========================= GET & SET =================================

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#getEntries()
	 * @return
	 */
	@Override
	public Map<String, Long> getEntries()
	{
		return CollectionUtil.<String, Long> newMap(histogram);
	}

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#getEntry(java.lang.String)
	 * @param aKey
	 * @return
	 */
	@Override
	public Long getEntry(final String aKey)
	{
		return histogram.get(aKey);
	}

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#addEntry(java.lang.String,
	 *      java.lang.Long)
	 * @param aKey
	 * @param value
	 * @return
	 */
	@Override
	public Long addEntry(final String aKey, final Long value)
	{
		return histogram.put(aKey, value);
	}

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#removeEntry(java.lang.String)
	 * @param aKey
	 * @return
	 */
	@Override
	public Long removeEntry(final String aKey)
	{
		return histogram.remove(aKey);
	}

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#clear()
	 */
	@Override
	public void clear()
	{
		histogram.clear();
	}

	/**
	 * @see edu.utah.further.fqe.api.ws.to.aggregate.Category#getSize()
	 * @return
	 */
	@Override
	public int getSize()
	{
		return histogram.size();
	}
}
