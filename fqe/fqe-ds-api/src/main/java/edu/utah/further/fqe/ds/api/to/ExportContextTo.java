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
package edu.utah.further.fqe.ds.api.to;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.fqe.ds.api.domain.ExportContext;

/**
 * Default implementation of the export context which contains a query identifier and a
 * number of query "filters" to further refine the export that is returned.
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
 * @version Sep 26, 2012
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = ExportContextTo.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "queryId", "userId", "filters" })
public final class ExportContextTo implements ExportContext
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ExportContextTo.class);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "exportContext";

	// ========================= FIELDS ====================================

	/**
	 * The query identifier that represents the query that should be exported.
	 */
	@XmlElement(name = "query_id", required = false, namespace = XmlNamespace.FQE)
	private Long queryId;

	/**
	 * The user who requested the export
	 */
	@XmlElement(name = "user_id", required = false, namespace = XmlNamespace.FQE)
	private String userId;

	/**
	 * Zero or more filters to refine the export data that is returned.
	 */
	@XmlElementWrapper(name = "filters", namespace = XmlNamespace.FQE, required = false)
	@XmlElementRef(name = "query", namespace = XmlNamespace.CORE_QUERY, type = SearchQueryTo.class)
	private final List<SearchQuery> filters = CollectionUtil.newList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.export.ExportContext#getUserId()
	 */
	@Override
	public String getUserId()
	{
		return this.userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.export.ExportContext#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.service.export.ExportContext#getQueryId()
	 */
	@Override
	public Long getQueryId()
	{
		return queryId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.export.ExportContext#setQueryId(java.lang.Long)
	 */
	@Override
	public void setQueryId(final Long queryId)
	{
		this.queryId = queryId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.service.export.ExportContext#getFilters()
	 */
	@Override
	public List<SearchQuery> getFilters()
	{
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.service.export.ExportContext#addFilter(edu.utah.further
	 * .core.query.domain.SearchQuery)
	 */
	@Override
	public void addFilter(final SearchQuery filter)
	{
		filters.add(filter);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(getQueryId())
				.append(getUserId())
				.append(getFilters())
				.toHashCode();
	}

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
		final ExportContext that = (ExportContext) obj;
		return new EqualsBuilder()
				.append(this.getQueryId(), that.getUserId())
				.append(this.getUserId(), that.getUserId())
				.append(this.getFilters(), that.getFilters())
				.isEquals();
	}

}
