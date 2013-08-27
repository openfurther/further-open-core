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
package edu.utah.further.ds.data.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import edu.utah.further.ds.api.domain.PhysicalQuery;

/**
 * Entity which contains the physical SearchQuery for a given namespace and query. This
 * entity represents the "response" of a destination translation request.
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
 * @version Nov 24, 2009
 */
@Entity
@Table(name = "QUERY_NMSPC")
public class PhysicalQueryEntity implements PhysicalQuery
{
	// ========================= CONSTANTS ==============================

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -6584416854855822679L;

	// ========================= FIELDS =================================

	/**
	 * Physical Query identifier (composite key)
	 */
	@EmbeddedId
	private PhysicalQueryEmbeddableId id;

	/**
	 * Term/attribute translated query xml
	 */
	@Column(name = "query_xml")
	@Type(type = "xml-type-as-string")
	private String queryXml;

	// ========================= IMPLEMENTATION: PersistentEntity =======

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public PhysicalQueryEmbeddableId getId()
	{
		return id;
	}

	// ========================= GETTERS & SETTERS ======================

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final PhysicalQueryEmbeddableId id)
	{
		this.id = id;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.PhysicalQuery#getQueryXml()
	 */
	@Override
	public String getQueryXml()
	{
		return queryXml;
	}

	/**
	 * @param queryXml
	 * @see edu.utah.further.ds.api.domain.PhysicalQuery#setQueryXml(java.lang.String)
	 */
	@Override
	public void setQueryXml(final String queryXml)
	{
		this.queryXml = queryXml;
	}

}
