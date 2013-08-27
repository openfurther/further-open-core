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
package edu.utah.further.fqe.ds.api.to.plan;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.DependencyRule;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;

/**
 * A query plan send rule JAXB transfer object. Specifies the target data source to send a
 * {@link SearchQuery} to. If no data source ID is specified, the query is sent to all
 * data sources. The plan is represented as a graph whose nodes are {@link ExecutionRule}s
 * and its edges are {@link DependencyRule}s.
 * <p>
 * Only the search query ID is stored here; the parent {@link QueryContext} of this object
 * can be used to look up the {@link SearchQuery} objects if necessary.
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
 * @version Nov 18, 2010
 */
@Implementation
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "id", "searchQueryId", "dataSourceId" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = ExecutionRuleToImpl.ENTITY_NAME)
public final class ExecutionRuleToImpl implements ExecutionRule
{
	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "executionRule";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this query execution.
	 */
	@XmlAttribute(name = "executionId", required = true)
	@Final
	private String id;

	/**
	 * The unique identifier of the {@link SearchQuery} <i>within the current
	 * {@link QueryContext}</i>.
	 */
	@XmlElement(name = "qid", required = true, namespace = XmlNamespace.FQE)
	@Final
	private Long searchQueryId;

	/**
	 * The unique identifier of the data source generating this status message.
	 */
	@XmlElement(name = "dataSourceId", required = false, namespace = XmlNamespace.FQE)
	@Final
	private String dataSourceId;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * No-argument constructor, required for a JAXB entity.
	 */
	public ExecutionRuleToImpl()
	{
		super();
	}

	/**
	 * @param id
	 * @param searchQueryId
	 * @param dataSourceId
	 */
	public ExecutionRuleToImpl(final String id, final Long searchQueryId,
			final String dataSourceId)
	{
		super();
		this.id = id;
		this.searchQueryId = searchQueryId;
		this.dataSourceId = dataSourceId;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other
	 *            send rule to fields copy from
	 */
	ExecutionRuleToImpl(final ExecutionRule other)
	{
		super();
		this.id = other.getId();
		this.searchQueryId = other.getSearchQueryId();
		this.dataSourceId = other.getDataSourceId();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.appendSuper(super.toString())
				.append("executionId", getId())
				.append("searchQueryId", getSearchQueryId())
				.append("dataSourceId", getDataSourceId())
				.toString();
	}

	/**
	 * Return the hash code of this object. includes the identifier field only.
	 *
	 * @return hash code of this object
	 * @see edu.utah.further.audit.api.domain.AbstractMessage#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		// ------------------------------------------------
		// Works only because this method is final!!!
		// ------------------------------------------------
		if (!ReflectionUtil.instanceOf(o, ExecutionRule.class))
			return false;

		final ExecutionRule that = (ExecutionRule) o;
		return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two keys. keys are ordered by search query ID, then by DS ID.
	 *
	 * @param that
	 *            the other {@link ExecutionRule} object
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final ExecutionRule that)
	{
		return new CompareToBuilder()
				.append(this.getSearchQueryId(), that.getSearchQueryId())
				.append(this.getDataSourceId(), that.getDataSourceId())
				.toComparison();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the id property.
	 *
	 * @return the id
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#setId(java.lang.Long)
	 */
	@Override
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#getSearchQueryId()
	 */
	@Override
	public Long getSearchQueryId()
	{
		return searchQueryId;
	}

	/**
	 * @param searchQueryId
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#setSearchQueryId(java.lang.Long)
	 */
	@Override
	public void setSearchQueryId(final Long searchQueryId)
	{
		this.searchQueryId = searchQueryId;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#getDataSourceId()
	 */
	@Override
	public String getDataSourceId()
	{
		return dataSourceId;
	}

	/**
	 * @param dataSourceId
	 * @see edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule#setDataSourceId(java.lang.String)
	 */
	@Override
	public void setDataSourceId(final String dataSourceId)
	{
		this.dataSourceId = dataSourceId;
	}
}
