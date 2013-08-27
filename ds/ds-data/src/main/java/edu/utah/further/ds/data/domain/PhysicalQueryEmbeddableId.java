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
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;

import edu.utah.further.ds.api.domain.PhysicalQueryId;

/**
 * Composite key for {@link PhysicalQueryEntity}
 * 
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
@Embeddable
public class PhysicalQueryEmbeddableId implements PhysicalQueryId
{
	// ========================= CONSTANTS =================================

	/**
	 * Generated Serial Id
	 */
	private static final long serialVersionUID = 2927191536646857303L;

	// ========================= FIELDS =================================

	/**
	 * Query identifier
	 */
	@Column(name = "query_id")
	private Long id;

	/**
	 * Namespace identifier
	 */
	@Column(name = "namespace_id")
	private Long namespaceId;

	// ========================= CONSTRUCTORS ====================

	/**
	 * Mandated by Hibernate.
	 */
	protected PhysicalQueryEmbeddableId()
	{
		super();
	}

	/**
	 * @param id
	 * @param namespaceId
	 */
	public PhysicalQueryEmbeddableId(final Long id, final Long namespaceId)
	{
		super();
		this.id = id;
		this.namespaceId = namespaceId;
	}

	// ========================= IMPL: Compareable =================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final PhysicalQueryId other)
	{
		return new CompareToBuilder().append(other.getId(), this.getId()).append(
				other.getNamespaceId(), this.getNamespaceId()).toComparison();
	}

	// ========================= IMPL: PersistentEntity =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= GET/SET =================================

	/**
	 * @param id
	 * @see edu.utah.further.ds.api.domain.PhysicalQueryId#setId(java.lang.Long)
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.PhysicalQueryId#getNamespaceId()
	 */
	@Override
	public Long getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * @param namespaceId
	 * @see edu.utah.further.ds.api.domain.PhysicalQueryId#setNamespaceId(java.lang.Long)
	 */
	@Override
	public void setNamespaceId(final Long namespaceId)
	{
		this.namespaceId = namespaceId;
	}

}
