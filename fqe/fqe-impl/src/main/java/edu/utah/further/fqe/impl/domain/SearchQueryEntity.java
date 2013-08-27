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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryAlias;
import edu.utah.further.core.query.domain.SortCriterion;
import edu.utah.further.core.query.util.SearchQueryUtil;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A search query persistent entity. This is a transfer object of a {@link SearchQuery}
 * that saves it to the database in XML format.
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
 * @version Dec 3, 2010
 */
// ============================
// Hibernate annotations
// ============================
@Entity
@Table(name = "search_query")
public class SearchQueryEntity implements SearchQuery,
		CopyableFrom<SearchQuery, SearchQueryEntity>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this persistent entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "search_query_id")
	@Final
	private Long entityId;

	/**
	 * The unique identifier of this search query within a {@link QueryContext}'s query
	 * list.
	 */
	@Column(name = "qid")
	@Final
	private Long qid;

	/**
	 * The root object which this query is based on
	 */
	@Column(name = "root_object")
	private String rootObject;

	/**
	 * A link to the federated parent {@link QueryContext}, if this is a child
	 * {@link QueryContext} produced by a data source.
	 */
	@ManyToOne
	@JoinColumn(name = "querycontext")
	private QueryContextEntity queryContext;

	/**
	 * The marshalled XML representation of this context's search query (the query part of
	 * this object). Using a character Large Object (CLOB) storage.
	 * <p>
	 * Trying to set column size to support both Oracle and MySQL.
	 * 
	 * @see http://www.elver.org/hibernate/ejb3_features.html
	 */
	@Column(name = "queryxml", nullable = true, length = 10485760)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String queryXml;

	/**
	 * The unmarshalled SearchQuery object.
	 */
	@Transient
	private SearchQuery query;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public SearchQueryEntity()
	{
		super();
	}

	/**
	 * A copy-constructor factory method.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static SearchQueryEntity newCopy(final SearchQuery other)
	{
		return new SearchQueryEntity().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public SearchQueryEntity copyFrom(final SearchQuery other)
	{
		if (other == null)
		{
			return this;
		}

		this.qid = other.getId();
		this.rootObject = other.getRootObjectName();
		setQuery(other);

		if (instanceOf(other, SearchQueryEntity.class))
		{
			// Copy QueryContextEntity fields
			final SearchQueryEntity otherEntity = (SearchQueryEntity) other;

			// Do not alter persistence-related fields of this entity if they are already
			// set
			if (this.entityId == null)
			{
				this.entityId = otherEntity.entityId;
			}
			if (this.queryContext == null)
			{
				this.queryContext = otherEntity.queryContext;
			}

			setQueryXml(otherEntity.getQueryXml());
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
		return qid;
	}

	// ========================= IMPLEMENTATION: HasSettableIdentifier =====

	/**
	 * @param id
	 * @see edu.utah.further.core.api.discrete.HasSettableIdentifier#setId(java.lang.Comparable)
	 */
	@Override
	public void setId(final Long qid)
	{
		this.qid = qid;
	}

	// ========================= IMPLEMENTATION: SearchQuery ===============

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQuery#getRootObject(java.lang.String)
	 */
	@Override
	public Class<? extends PersistentEntity<?>> getRootObject(
			final String domainClassPackage)
	{
		return SearchQueryUtil.getDomainClass(domainClassPackage, rootObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQuery#getRootObjectName()
	 */
	@Override
	public String getRootObjectName()
	{
		return rootObject;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getSortCriteria()
	 */
	@Override
	public List<SortCriterion> getSortCriteria()
	{
		return (query != null ? query.getSortCriteria() : null);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getRootCriterion()
	 */
	@Override
	public SearchCriterion getRootCriterion()
	{
		return (query != null ? query.getRootCriterion() : null);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getAliases()
	 */
	@Override
	public List<SearchQueryAlias> getAliases()
	{
		return (query != null) ? query.getAliases() : null;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getFirstResult()
	 */
	@Override
	public Integer getFirstResult()
	{
		return (query != null ? query.getFirstResult() : null);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchQuery#getMaxResults()
	 */
	@Override
	public Integer getMaxResults()
	{
		return (query != null ? query.getMaxResults() : null);
	}

	/**
	 * @param maxResults
	 * @see edu.utah.further.core.query.domain.SearchQuery#setMaxResults(java.lang.Integer)
	 */
	@Override
	public void setMaxResults(final Integer maxResults)
	{
		query.setMaxResults(maxResults);
	}

	// ========================= GET & SET =================================

	/**
	 * Return the queryXml property.
	 * 
	 * @return the queryXml
	 */
	public String getQueryXml()
	{
		return queryXml;
	}

	/**
	 * Set a new value for the queryXml property.
	 * 
	 * @param queryXml
	 *            the queryXml to set
	 */
	public void setQueryXml(final String queryXml)
	{
		this.queryXml = queryXml;
	}

	/**
	 * Return the query property.
	 * 
	 * @return the query
	 */
	public SearchQuery getQuery()
	{
		return query;
	}

	/**
	 * Set a new value for the query property.
	 * 
	 * @param query
	 *            the query to set
	 */
	public void setQuery(final SearchQuery query)
	{
		this.query = query;
	}

	/**
	 * Return the qid property.
	 * 
	 * @return the qid
	 */
	public Long getQid()
	{
		return qid;
	}

	/**
	 * Set a new value for the qid property.
	 * 
	 * @param qid
	 *            the qid to set
	 */
	public void setQid(final Long qid)
	{
		this.qid = qid;
	}

	/**
	 * Return the queryContext property.
	 * 
	 * @return the queryContext
	 */
	public QueryContextEntity getQueryContext()
	{
		return queryContext;
	}

	/**
	 * Set a new value for the queryContext property.
	 * 
	 * @param queryContext
	 *            the queryContext to set
	 */
	public void setQueryContext(final QueryContextEntity queryContext)
	{
		this.queryContext = queryContext;
	}

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
		final SearchQueryEntity that = (SearchQueryEntity) obj;
		return new EqualsBuilder()
				.append(this.getId(), that.getId())
				.append(this.getRootObjectName(), that.getRootObjectName())
				.append(this.getQueryXml(), that.getQueryXml())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(getId())
				.append(getRootObjectName())
				.append(getQueryXml())
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
}
