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
package edu.utah.further.ds.further.model.impl.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.discrete.HasDatasetIdentifier;
import edu.utah.further.core.api.discrete.HasSettableIdentifier;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version May 9, 2012
 */
@Implementation
@Embeddable
public class PersonAssociationId implements Serializable,
		Comparable<PersonAssociationId>, HasSettableIdentifier<Long>,
		HasDatasetIdentifier<String>
{
	// ========================= CONSTANTS ===================================

	private static final long serialVersionUID = -2485323759371203982L;

	// ========================= FIELDS ===================================

	@Column(name = "fperson_assoc_id", nullable = false)
	private Long id;

	@Column(name = "dataset_id", nullable = false)
	private String datasetId;

	// ========================= METHODS ===================================

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

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasDatasetIdentifier#getQueryId()
	 */
	@Override
	public String getDatasetId()
	{
		return datasetId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.discrete.HasQueryIdentifier#setDatasetId(java.lang.Comparable
	 * )
	 */
	@Override
	public void setDatasetId(final String datasetId)
	{
		this.datasetId = datasetId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final PersonAssociationId other)
	{
		return new CompareToBuilder()
				.append(other.getId(), this.getId())
				.append(other.getDatasetId(), this.getDatasetId())
				.toComparison();
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final PersonAssociationId that = (PersonAssociationId) obj;
		return new EqualsBuilder()
				.append(getId(), that.getId())
				.append(getDatasetId(), that.getDatasetId())
				.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).append(getDatasetId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
}
