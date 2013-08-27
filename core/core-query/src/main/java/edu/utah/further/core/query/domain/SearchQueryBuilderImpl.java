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
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

/**
 * Builds a {@link SearchQuery} object.
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
 * @version Apr 2, 2009
 */
public final class SearchQueryBuilderImpl implements SearchQueryBuilder
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SearchQueryBuilderImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * A criterion to be decorated by this object.
	 */
	final SearchCriterion rootCriterion;

	/**
	 * List of sort criteria.
	 */
	List<SortCriterion> sortCriteria = newList();

	/**
	 * A list of alias definitions.
	 */
	List<SearchQueryAlias> aliases = newList();

	/**
	 * An optional query identifier.
	 */
	Long id;

	/**
	 * The root object which this query is based upon.
	 */
	String rootObject;

	/**
	 * The first result to be retrieved.
	 */
	Integer firstResult;

	/**
	 * A limit upon the number of objects to be retrieved.
	 */
	Integer maxResults;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a search criteria object.
	 * 
	 * @param rootCriterion
	 *            root node of the criteria hierarchy
	 */
	public SearchQueryBuilderImpl(final SearchCriterion rootCriterion)
	{
		// This cast is OK as long as all SearchCriterion implementations
		// implement SearchCriterion.
		this.rootCriterion = rootCriterion;
	}

	/**
	 * Copy an existing searchQuery but allow things to be overridden by calling set/add
	 * methods.
	 * 
	 * @param searchQuery
	 */
	public SearchQueryBuilderImpl(final SearchQuery searchQuery)
	{
		this.aliases = searchQuery.getAliases();
		this.firstResult = searchQuery.getFirstResult();
		this.id = searchQuery.getId();
		this.maxResults = searchQuery.getMaxResults();
		this.rootCriterion = searchQuery.getRootCriterion();
		this.rootObject = searchQuery.getRootObjectName();
		this.sortCriteria = searchQuery.getSortCriteria();
	}

	// ========================= IMPLEMENTATION: Builder<SearchQuery> ======

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public SearchQuery build()
	{
		return SearchQueryTo.newInstance(this);
	}

	// ========================= IMPLEMENTATION: SearchQueryBuilder ========

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryInterface#addSortCriterion(edu.utah
	 * .further.core.query.domain.SortCriterion)
	 */
	@Override
	public SearchQueryBuilder addSortCriterion(final SortCriterion e)
	{
		sortCriteria.add(e);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryBuilder#addSortCriteriones(java.util
	 * .List)
	 */
	@Override
	public SearchQueryBuilder addSortCriteriones(final List<SortCriterion> sortOrders)
	{
		sortCriteria.addAll(sortOrders);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryInterface#addAlias(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public SearchQueryBuilder addAlias(final String objecType, final String alias,
			final String associationPath)
	{
		aliases.add(new SearchQueryAliasTo(objecType, alias, associationPath));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryBuilder#addAliases(java.util.Map)
	 */
	@Override
	public SearchQueryBuilder addAliases(final List<SearchQueryAlias> newAliases)
	{
		if (newAliases == null)
		{
			aliases = newList();
		}
		aliases.addAll(newAliases);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryInterface#setFirstResult(int)
	 */
	@Override
	public SearchQueryBuilder setFirstResult(final int firstResult)
	{
		this.firstResult = new Integer(firstResult);
		return this;
	}

	/**
	 * @param maxResults
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQueryBuilder#setMaxResults(int)
	 */
	@Override
	public SearchQueryBuilder setMaxResults(final int maxResults)
	{
		this.maxResults = new Integer(maxResults);
		return this;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 * @see edu.utah.further.core.query.domain.SearchQueryBuilder#setId(java.lang.Long)
	 */
	@Override
	public SearchQueryBuilder setId(final Long id)
	{
		this.id = id;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryBuilder#setRootObject(java.lang.String
	 * )
	 */
	@Override
	public SearchQueryBuilder setRootObject(final String rootObject)
	{
		this.rootObject = rootObject;
		return this;
	}

}
