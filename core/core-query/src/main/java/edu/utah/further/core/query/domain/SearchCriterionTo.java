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
package edu.utah.further.core.query.domain;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static edu.utah.further.core.api.text.StringUtil.pluralForm;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A search criterion implementation and transfer object.
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
 * @version May 29, 2009
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "searchType", "parameters", "options", "criteria", "queries" })
@XmlRootElement(namespace = XmlNamespace.CORE_QUERY, name = SearchCriterionTo.ENTITY_NAME)
@XmlSeeAlso(
{ Relation.class })
final class SearchCriterionTo implements SearchCriterion,
		CopyableFrom<SearchCriterion, SearchCriterionTo>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SearchCriterionTo.class);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "criteria";

	// ========================= FIELDS ====================================

	/**
	 * Type of search (equals/less than/....
	 */
	@XmlElement(name = "searchType", required = false)
	private SearchType searchType;

	/**
	 * Criteria to operator on, if this is a composite (junction) criterion.
	 */
	// For uniformity of element names in the entire criterion tree, we match nested
	// criterion element names with their parent entity element name
	@XmlElement(name = ENTITY_NAME, required = false)
	private final List<SearchCriterionTo> criteria = CollectionUtil.newList();

	/**
	 * Optional sub-select queries to run as part of this criterion.
	 */
	// For uniformity of element names in the entire criterion tree, we match nested
	// query names with their root query entity element name
	@XmlElement(name = SearchQueryTo.ENTITY_NAME, required = false)
	private final List<SearchQueryTo> queries = CollectionUtil.newList();

	/**
	 * Search parameters, e.g. low and high for {@link SearchType#BETWEEN}. Lazily
	 * initialized to prevent creating spurious empty parameters elements during
	 * marshalling.
	 * <p>
	 * TODO: replace by a stack?
	 */
	@XmlElementWrapper(name = "parameters", required = false)
	@XmlElements(
	{ @XmlElement(name = "parameter", type = Object.class) })
	private List<Object> parameters;

	/**
	 * Additional search options (e.g. secondary search types).
	 */
	@XmlElement(name = "options", required = false)
	private SearchOptionsTo options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	private SearchCriterionTo()
	{
		super();
	}

	/**
	 * A no-argument constructor. Initializes no fields.
	 */
	public static SearchCriterionTo newInstance()
	{
		return new SearchCriterionTo();
	}

	/**
	 * A copy-constructor.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public static SearchCriterionTo newCopy(final SearchCriterion other)
	{
		return newInstance().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("searchType", searchType)
				.append("parameters", parameters)
				.append("options", options)
				.append("criteria", criteria)
				.append("queries", queries)
				.toString();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
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
		final SearchCriterionTo that = (SearchCriterionTo) obj;
		return new EqualsBuilder()
				.append(this.searchType, that.searchType)
				.append(this.parameters, that.parameters)
				.append(this.options, that.options)
				.append(this.criteria, that.criteria)
				.append(this.queries, that.queries)
				.isEquals();
	}

	/**
	 * Two {@link SearchCriterionTo} objects must be equal <i>if and only if</i> their
	 * hash codes are equal. This is important because a dependent object (e.g. a query
	 * context) may decide whether to deep-copy its search query field based on hash code
	 * equality.
	 * 
	 * @return hash code
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(this.searchType)
				.append(this.parameters)
				.append(this.options)
				.append(this.criteria)
				.append(this.queries)
				.toHashCode();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * Returns a deep-copy of the argument into this object. This object is usually
	 * constructed with a no-argument constructor first, and then this method is called to
	 * copy fields into it.
	 * 
	 * @param other
	 *            object to copy
	 * @return this object, for chaining
	 * @see edu.utah.further.core.misc.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public SearchCriterionTo copyFrom(final SearchCriterion other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy fields
		final SearchOptions otherOptions = other.getOptions();
		// Defensive copying is done within this setter:
		setOptions(otherOptions);
		setSearchType(other.getSearchType());

		// Deep-copy collection fields
		setParameters(CollectionUtil.copyListOrNull(other.getParameters()));
		setCriteria(other.getCriteria());
		setQueries(other.getQueries());

		return this;
	}

	// ========================= IMPLEMENTATION: SearchCriterion ===========

	/**
	 * Add a sub-criterion.
	 * 
	 * @param subCriterion
	 *            sub-criterion to add
	 * @return this (for method chaining)
	 * @see edu.utah.further.core.query.domain.SearchCriterion#addCriterion(edu.utah.further.core.query.domain.SearchCriterion)
	 */
	@Override
	public SearchCriterionTo addCriterion(final SearchCriterion subCriterion)
	{
		criteria.add(SearchCriterionTo.newCopy(subCriterion));
		return this;
	}

	/**
	 * Add a collection of sub-criteria.
	 * 
	 * @param subCriteria
	 *            sub-criteria collection to add
	 * @return this (for method chaining)
	 * @see edu.utah.further.core.query.domain.SearchCriterion#addCriteria(java.util.Collection)
	 */
	@Override
	public SearchCriterionTo addCriteria(
			final Collection<? extends SearchCriterion> subCriteria)
	{
		for (final SearchCriterion subCriterion : subCriteria)
		{
			addCriterion(subCriterion);
		}
		return this;
	}

	/**
	 * Add a sub-query.
	 * 
	 * @param subQuery
	 *            sub-query to add
	 * @return this (for method chaining)
	 * @see edu.utah.further.core.query.domain.SearchCriterion#addQuery(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public SearchCriterionTo addQuery(final SearchQuery subQuery)
	{
		queries.add(SearchQueryTo.newCopy(subQuery));
		return this;
	}

	/**
	 * Add a collection of sub-queries.
	 * 
	 * @param subCriteria
	 *            sub-queries collection to add
	 * @return this (for method chaining)
	 * @see edu.utah.further.core.query.domain.SearchCriterion#addQueries(java.util.Collection)
	 */
	@Override
	public SearchCriterionTo addQueries(
			final Collection<? extends SearchQuery> subCriteria)
	{
		for (final SearchQuery subQuery : subCriteria)
		{
			addQuery(subQuery);
		}
		return this;
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Returns the searchType property.
	 * 
	 * @return the searchType
	 */
	@Override
	public SearchType getSearchType()
	{

		return searchType;
	}

	/**
	 * Return the list of sub-criteria. Defensively copied.
	 * 
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchCriterion#getCriteria()
	 */
	@Override
	public List<SearchCriterion> getCriteria()
	{
		return CollectionUtil.<SearchCriterion> newList(criteria);
	}

	/**
	 * Return the list of sub-queries. Defensively copied.
	 * 
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchCriterion#getQueries()
	 */
	@Override
	public List<SearchQuery> getQueries()
	{
		return CollectionUtil.<SearchQuery> newList(queries);
	}

	/**
	 * Return the parameters property.
	 * 
	 * @return the parameters
	 */
	@Override
	public List<?> getParameters()
	{
		return (parameters == null) ? Collections.EMPTY_LIST : newList(parameters);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getNumParameters()
	 */
	@Override
	public int getNumParameters()
	{
		return (parameters == null) ? 0 : parameters.size();
	}

	/**
	 * Return a parameter by index.
	 * 
	 * @param index
	 *            parameter index in the parameters list
	 * @return corresponding parameter value
	 */
	@Override
	public Object getParameter(final int index)
	{
		return parameters.get(index);
	}

	/**
	 * Set the parameter at position <code>index</code> to a new value.
	 * 
	 * @param index
	 *            parameter index in the parameters list
	 * @param value
	 *            new parameter value
	 * @see edu.utah.further.core.query.domain.SearchCriterion#setParameter(int,
	 *      java.lang.Object)
	 */
	@Override
	public void setParameter(final int index, final Object value)
	{
		parameters.set(index, value);
	}

	/**
	 * Return the options property.
	 * 
	 * @return the options
	 */
	@Override
	public SearchOptions getOptions()
	{
		return (options == null) ? null : SearchOptionsTo.newCopy(options);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getEscapeChar()
	 */
	@Override
	public Character getEscapeChar()
	{
		return options.getEscapeChar();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchOptions#getMatchType()
	 */
	@Override
	public MatchType getMatchType()
	{
		return options.getMatchType();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchCriterion#isIgnoreCase()
	 */
	@Override
	public Boolean isIgnoreCase()
	{
		return options.isIgnoreCase();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param searchType
	 * @return
	 */
	public SearchCriterionTo setSearchType(final SearchType searchType)
	{
		this.searchType = searchType;
		return this;
	}

	/**
	 * @param options
	 * @return
	 */
	private SearchCriterion setOptions(final SearchOptions options)
	{
		this.options = (options == null) ? null : SearchOptionsTo.newCopy(options);
		return this;
	}

	/**
	 * @param parameters
	 * @return
	 */
	private SearchCriterionTo setParameters(final List<?> parameters)
	{
		if (parameters == null)
		{
			this.parameters = null;
		}
		else
		{
			//Would like to have used XMLTypeAdapter on parameters but can't
			//http://java.net/jira/browse/JAXB-359
			//http://www.java.net/node/665119
			final List<Object> params = newList(parameters);
			for (int i = 0; i < params.size(); i++)
			{
				if (ReflectionUtil.instanceOf(params.get(i), XMLGregorianCalendar.class))
				{
					final Date date = ((XMLGregorianCalendar) params.get(i))
							.toGregorianCalendar()
							.getTime();
					params.set(i, date);
				}
			}
			
			this.parameters = params;
		}
		return this;
	}

	/**
	 * @param criteria
	 */
	private SearchCriterionTo setCriteria(final List<? extends SearchCriterion> criteria)
	{
		this.criteria.clear();
		addCriteria(criteria);
		return this;
	}

	/**
	 * @param queries
	 */
	private SearchCriterionTo setQueries(final List<? extends SearchQuery> queries)
	{
		this.queries.clear();
		addQueries(queries);
		return this;
	}

	/**
	 * Builds a {@link SearchCriterion} object.
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
	 * @version Apr 2, 2009
	 */
	static final class SearchCriterionBuilder implements Builder<SearchCriterion>
	{
		// ========================= FIELDS ====================================

		/**
		 * The target object we build here.
		 */
		private final SearchCriterionTo target = SearchCriterionTo.newInstance();

		/**
		 * Number of expected parameters passed to the construction/builder of
		 * {@link SearchCriterion} object. A value of
		 * {@link Constants#INVALID_VALUE_INTEGER} signifies that an arbitrary number of
		 * parameters.
		 */
		private final int numParameters;

		/**
		 * Additional search options (e.g. secondary search types).
		 */
		private final SearchOptionsTo options = SearchOptionsTo.newInstance();

		// ========================= CONSTRUCTORS ==============================

		/**
		 * Create a search criterion builder object.
		 * 
		 * @param searchType
		 *            type of search (equals/less than/...)
		 * @param numParameters
		 *            number of expected parameters passed to the construction/builder of
		 *            {@link SearchCriterion} object.
		 */
		public SearchCriterionBuilder(final SearchType searchType, final int numParameters)
		{
			target.setSearchType(searchType);
			this.numParameters = numParameters;
		}

		// ========================= IMPLEMENTATION: Builder<SearchCriterion> ==

		/**
		 * @return
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public SearchCriterion build()
		{
			// Validate construction arguments
			final int expectedSize = numParameters;
			if (expectedSize > 0) // FUR-1662: skip check for 0 parameters!
			{
				final int actualSize = target.parameters.size();
				validateIsTrue(
						actualSize == expectedSize,
						"This criterion accepts " + expectedSize + " "
								+ pluralForm("parameter", expectedSize) + ", but "
								+ actualSize + " " + pluralForm("parameter", actualSize)
								+ " were specified");
			}

			// Create and return a criterion object
			if (!isEmpty(options))
			{
				target.options = options;
			}

			return target;
		}

		// ========================= IMPLEMENTATION: SearchQueryBuilder ========

		/**
		 * Add a sub-criterion.
		 * 
		 * @param subCriterion
		 *            sub-criterion to add
		 * @return this (for method chaining)
		 */
		public SearchCriterionBuilder addCriterion(final SearchCriterion subCriterion)
		{
			target.addCriterion(subCriterion);
			return this;
		}

		/**
		 * Add a collection of sub-criteria.
		 * 
		 * @param subCriteria
		 *            sub-criteria collection to add
		 * @return this (for method chaining)
		 */
		public SearchCriterionBuilder addCriteria(
				final Collection<? extends SearchCriterion> subCriteria)
		{
			target.addCriteria(subCriteria);
			return this;
		}

		/**
		 * Add a sub-query.
		 * 
		 * @param subQuery
		 *            sub-query to add
		 * @return this (for method chaining)
		 */
		public SearchCriterionBuilder addQuery(final SearchQuery subQuery)
		{
			target.addQuery(subQuery);
			return this;
		}

		/**
		 * Add a collection of sub-queries.
		 * 
		 * @param subQueries
		 *            sub-query collection to add
		 * @return this (for method chaining)
		 */
		public SearchCriterionBuilder addQueries(
				final Collection<? extends SearchQuery> subQueries)
		{
			target.addQueries(subQueries);
			return this;
		}

		/**
		 * Set a new value for the parameters property.
		 * 
		 * @param parameters
		 *            the parameters to set
		 * @return this (for method chaining)
		 */
		public <T> SearchCriterionBuilder setParameters(final List<T> parameters)
		{
			target.setParameters(parameters);
			return this;
		}

		/**
		 * Set a new value for the parameters property.
		 * 
		 * @param parameters
		 *            the parameters to set
		 * @return this (for method chaining)
		 */
		@SafeVarargs
		public final <T> SearchCriterionBuilder setParameters(final T... parameters)
		{
			return setParameters(CollectionUtil.<T> newList(parameters));
		}

		/**
		 * @param escapeChar
		 * @see edu.utah.further.core.query.domain.SearchOptions#setEscapeChar(java.lang.Character)
		 */
		public SearchCriterionBuilder setEscapeChar(final Character escapeChar)
		{
			options.setEscapeChar(escapeChar);
			return this;
		}

		/**
		 * @param ignoreCase
		 * @see edu.utah.further.core.query.domain.SearchOptions#setIgnoreCase(Boolean)
		 */
		public SearchCriterionBuilder setIgnoreCase(final Boolean ignoreCase)
		{
			options.setIgnoreCase(ignoreCase);
			return this;
		}

		/**
		 * @param matchType
		 * @see edu.utah.further.core.query.domain.SearchOptions#setMatchType(edu.utah.further.core.query.domain.MatchType)
		 */
		public SearchCriterionBuilder setMatchType(final MatchType matchType)
		{
			options.setMatchType(matchType);
			return this;
		}

		// ========================= PRIVATE METHODS ===========================

		/**
		 * A flag that indicates if there are set options in an options object.
		 * 
		 * @param options
		 *            search criterion options object
		 * @return <code>true</code> if and only if no fields are set in
		 *         <code>options</code>
		 */
		private static boolean isEmpty(final SearchOptions options)
		{
			// Using negative checks
			if (options.getEscapeChar() != null)
			{
				return false;
			}
			if (options.getMatchType() != null)
			{
				return false;
			}
			if (options.isIgnoreCase() != null)
			{
				return false;
			}
			return true;
		}
	}

}
