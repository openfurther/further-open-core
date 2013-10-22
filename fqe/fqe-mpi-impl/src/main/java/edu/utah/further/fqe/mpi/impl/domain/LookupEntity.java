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
package edu.utah.further.fqe.mpi.impl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * An entity for a lookup table
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 22, 2013
 */
@Table(name = "demo_mpi")
@Entity
public class LookupEntity implements PersistentEntity<String>
{
	// ========================= CONSTANTS ================================

	/**
	 * Default serial id
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ===================================

	@Id
	@Column(name = "mpi_uuid")
	private String mpiId;

	@Column(name = "common_id")
	private Long commonId;

	@Column(name = "namespace_id")
	private Long namespaceId;

	@Column(name = "id")
	private Long sourceId;

	// ========================= IMPL: HasId =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public String getId()
	{
		return mpiId;
	}

	// ========================= GET/SET =================

	/**
	 * Set a new value for the mpiId property.
	 * 
	 * @param mpiId
	 *            the mpiId to set
	 */
	public void setId(final String mpiId)
	{
		this.mpiId = mpiId;
	}

	/**
	 * Return the mpiId property.
	 * 
	 * @return the mpiId
	 */
	public String getMpiId()
	{
		return mpiId;
	}

	/**
	 * Return the commonId property.
	 * 
	 * @return the commonId
	 */
	public Long getCommonId()
	{
		return commonId;
	}

	/**
	 * Set a new value for the commonId property.
	 * 
	 * @param commonId
	 *            the commonId to set
	 */
	public void setCommonId(final Long commonId)
	{
		this.commonId = commonId;
	}

	/**
	 * Return the namespaceId property.
	 * 
	 * @return the namespaceId
	 */
	public Long getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * Set a new value for the namespaceId property.
	 * 
	 * @param namespaceId
	 *            the namespaceId to set
	 */
	public void setNamespaceId(final Long namespaceId)
	{
		this.namespaceId = namespaceId;
	}

	/**
	 * Return the sourceId property.
	 * 
	 * @return the sourceId
	 */
	public Long getSourceId()
	{
		return sourceId;
	}

	/**
	 * Set a new value for the sourceId property.
	 * 
	 * @param sourceId
	 *            the sourceId to set
	 */
	public void setSourceId(final Long sourceId)
	{
		this.sourceId = sourceId;
	}
}
