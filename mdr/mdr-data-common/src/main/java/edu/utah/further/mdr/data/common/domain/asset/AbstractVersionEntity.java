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
package edu.utah.further.mdr.data.common.domain.asset;

import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.mdr.api.domain.asset.LookupValue;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.Version;

/**
 * An MDR asset version persistent entity.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @resource Mar 19, 2009
 */
@MappedSuperclass
public abstract class AbstractVersionEntity implements Version
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "ASSET_VERSION_SEQ_ID")
	@Final
	private Long id;

	/**
	 * Holds the status of this resource (active/inactive).
	 */
	@ManyToOne
	@JoinColumn(name = "ASSET_STATUS_ID")
	private LookupValueEntity status;

	/**
	 * Version number.
	 */
	@Column(name = "ASSET_VERSION", nullable = true)
	private Long version;

	/**
	 * Asset update date and time of this version (Rick emails on 19-MAR-2009: "DT MEANS
	 * DATE, DTS MEANS DATE & TIME. ORACLE DATE USES THE SAME TYPE BUT IT IS AN INDICATION
	 * OF HOW GRANULAR THE STORED VALUE IS").
	 */
	@Column(name = "ASSET_UPDATED_DTS", nullable = true)
	private Timestamp updatedDate;

	/**
	 * TODO: ask Rick what this field means. Is this a link to the APP_USER table?
	 */
	@Column(name = "ASSET_UPDATED_BY_USER_ID", length = 100, nullable = true)
	private String updatedByUserId;

	/**
	 * Long description of the asset update that this version represents.
	 */
	@Column(name = "ASSET_UPDATE_DSC", length = 1000, nullable = true)
	private String updateDescription;

	/**
	 * Set of resources of this asset. Only one can be active at any given time.
	 */
	// @CollectionOfElements
	@Transient
	private Set<Resource> resourceSet = newSet();

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id)
				.append("version", version)
				.append("asset", getAsset());
		return builder.toString();
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for Hibernate.
	 */
	public AbstractVersionEntity()
	{
		super();
	}

	/**
	 * A copy constructor.
	 * 
	 * @param other
	 *            other entity to copy from
	 */
	public AbstractVersionEntity(final Version other)
	{
		this();
		copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#compareTo(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public int compareTo(final Version other)
	{
		return new CompareToBuilder()
				.append(this.version, other.getVersion())
				.toComparison();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#copyFrom(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public Version copyFrom(final Version other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		setAsset(other.getAsset());
		this.status = (LookupValueEntity) other.getStatus();
		this.version = other.getVersion();
		setPropertiesXml(other.getPropertiesXml());
		this.updatedByUserId = other.getUpdatedByUserId();
		final Timestamp otherUpdatedDate = other.getUpdatedDate();
		if (otherUpdatedDate != null)
		{
			this.updatedDate = new Timestamp(otherUpdatedDate.getTime());
		}
		this.updateDescription = other.getUpdateDescription();

		// Deep-copy collection references but soft-copy their elements unless it's easy
		// to deep-copy them too
		// this.resourceSet = newSet();
		// for (final Resource resource : other.getResourceSet())
		// {
		// addResource(new ResourceEntity().copyFrom(resource));
		// }

		// Deep-copy collection references but soft-copy their elements

		return this;
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: Version ===================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getVersion()
	 */
	@Override
	public Long getVersion()
	{
		return version;
	}

	/**
	 * @param version
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setVersion(java.lang.Long)
	 */
	@Override
	public void setVersion(final Long version)
	{
		this.version = version;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getUpdateDescription()
	 */
	@Override
	public String getUpdateDescription()
	{
		return updateDescription;
	}

	/**
	 * @param description
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setUpdateDescription(java.lang.String)
	 */
	@Override
	public void setUpdateDescription(final String description)
	{
		this.updateDescription = description;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getResourceSet()
	 */
	@Override
	public Set<Resource> getResourceSet()
	{
		return CollectionUtil.<Resource> newSet(resourceSet);
	}

	/**
	 * @param resourceSet
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setResourceSet(java.util.Set)
	 */
	@Override
	public void setResourceSet(final Set<? extends Resource> resourceSet)
	{
		this.resourceSet.clear();
		addResources(resourceSet);
	}

	/**
	 * @param e
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#addResource(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	@Override
	public void addResource(final Resource e)
	{
		resourceSet.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#addResources(java.util.Set)
	 */
	@Override
	public void addResources(final Collection<? extends Resource> c)
	{
		resourceSet.addAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#removeResource(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	@Override
	public void removeResource(final Resource o)
	{
		resourceSet.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#removeResources(java.util.Collection)
	 */
	@Override
	public void removeResources(final Collection<? extends Resource> c)
	{
		resourceSet.removeAll(c);
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getUpdatedDate()
	 */
	@Override
	public Timestamp getUpdatedDate()
	{
		return updatedDate;
	}

	/**
	 * @param updatedDate
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setUpdatedDate(java.sql.Timestamp)
	 */
	@Override
	public void setUpdatedDate(final Timestamp updatedDate)
	{
		this.updatedDate = updatedDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getUpdatedByUserId()
	 */
	@Override
	public String getUpdatedByUserId()
	{
		return updatedByUserId;
	}

	/**
	 * @param updatedByUserId
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setUpdatedByUserId(java.lang.String)
	 */
	@Override
	public void setUpdatedByUserId(final String updatedByUserId)
	{
		this.updatedByUserId = updatedByUserId;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getStatus()
	 */
	@Override
	public LookupValue getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setStatus(edu.utah.further.mdr.api.domain.asset.LookupValue)
	 */
	@Override
	public void setStatus(final LookupValue status)
	{
		this.status = (LookupValueEntity) status;
	}

	// ========================= PRIVATE METHODS ===========================
}
