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
package edu.utah.further.i2b2.query.criteria.service.impl;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryAlias;
import edu.utah.further.core.query.domain.SortCriterion;
import edu.utah.further.i2b2.query.model.I2b2Query;

/**
 * Adapts an {@link I2b2Query} to a FURTHeR logical {@link SearchQuery}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 20, 2009
 */
final class I2b2QueryAdapter implements SearchQuery
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The logical query built by this object.
	 */
	private final SearchQuery searchQuery;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param i2b2Query
	 */
	public I2b2QueryAdapter(final I2b2SearchQueryBuilder queryBuilder)
	{
		super();
		this.searchQuery = queryBuilder.build();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
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
		final I2b2QueryAdapter that = (I2b2QueryAdapter) obj;
		return new EqualsBuilder().append(this.searchQuery, that.searchQuery).isEquals();
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
		return new HashCodeBuilder().append(this.searchQuery).toHashCode();
	}

	// ========================= IMPLEMENTATION: SearchQuery ===============

	@Override
	public Class<? extends PersistentEntity<?>> getRootObject(
			final String domainClassPackage)
	{
		return searchQuery.getRootObject(domainClassPackage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQuery#getRootObject()
	 */
	@Override
	public String getRootObjectName()
	{
		return searchQuery.getRootObjectName();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return searchQuery.getId();
	}

	/**
	 * @param id
	 * @see edu.utah.further.core.api.discrete.HasSettableIdentifier#setId(java.lang.Comparable)
	 */
	@Override
	public void setId(final Long id)
	{
		searchQuery.setId(id);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getAliases()
	 */
	@Override
	public List<SearchQueryAlias> getAliases()
	{
		return searchQuery.getAliases();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getFirstResult()
	 */
	@Override
	public Integer getFirstResult()
	{
		return searchQuery.getFirstResult();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getMaxResults()
	 */
	@Override
	public Integer getMaxResults()
	{
		return searchQuery.getMaxResults();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getRootCriterion()
	 */
	@Override
	public SearchCriterion getRootCriterion()
	{
		return searchQuery.getRootCriterion();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getSortCriteria()
	 */
	@Override
	public List<SortCriterion> getSortCriteria()
	{
		return searchQuery.getSortCriteria();
	}

	/**
	 * @param maxResults
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#setMaxResults(java.lang.Integer)
	 */
	@Override
	public void setMaxResults(final Integer maxResults)
	{
		searchQuery.setMaxResults(maxResults);
	}

}
