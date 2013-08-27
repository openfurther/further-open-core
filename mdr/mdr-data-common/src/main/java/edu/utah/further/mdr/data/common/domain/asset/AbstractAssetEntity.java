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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.SortedSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.mdr.api.domain.asset.ActivationInfo;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Version;

/**
 * An MDR asset persistent entity.
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
 * @version Mar 19, 2009
 */
// ============================
// Hibernate annotations
// ============================
@MappedSuperclass
public abstract class AbstractAssetEntity implements Asset
{

	// ========================= CONSTANTS =================================
	
	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;
	
	// ========================= FIELDS ====================================
	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "ASSET_ID")
	@Final
	private Long id;

	/**
	 * Label (short description of the asset).
	 */
	@Column(name = "ASSET_LABEL", length = 500, nullable = true)
	private String label;

	/**
	 * Long description of the asset.
	 */
	@Column(name = "ASSET_DSC", length = 4000, nullable = true)
	private String description;
	/**
	 * Holds resource activation dates.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "activationDate", column = @Column(name = "ASSET_ACTIVATE_DT", nullable = true)),
			@AttributeOverride(name = "deactivationDate", column = @Column(name = "ASSET_DEACTIVATE_DT", nullable = true)) })
	@Final
	private ActivationInfoEntity activationInfo = new ActivationInfoEntity();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for Hibernate.
	 */
	public AbstractAssetEntity()
	{
		super();
	}

	/**
	 * A copy constructor.
	 * 
	 * @param other
	 *            other entity to copy from
	 */
	public AbstractAssetEntity(final Asset other)
	{
		this();
		copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id);
		if (getNamespace() != null)
		{
			builder.append("namespace", getNamespace());
		}
		if (getType() != null)
		{
			builder.append("type", getType());
		}
		builder.append("label", label).append("description", description);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#copyFrom(edu.utah.further.mdr.api.domain.asset.Asset)
	 */
	@Override
	public AbstractAssetEntity copyFrom(final Asset other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		setNamespace(other.getNamespace());
		setType(other.getType());
		this.label = other.getLabel();
		this.description = other.getDescription();
		this.activationInfo = new ActivationInfoEntity().copyFrom(other
				.getActivationInfo());

		// Deep-copy collection references but soft-copy their elements unless it's easy
		// to deep-copy them too
		this.getVersionSetInternal().clear();
		for (final Version version : other.getVersionSet())
		{
			addVersion(newVersionEntity().copyFrom(version));
		}

		return this;
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: Asset =====================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(final String label)
	{
		this.label = label;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getActivationInfo()
	 */
	@Override
	public ActivationInfo getActivationInfo()
	{
		return activationInfo;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getActivationDate()
	 */
	@Override
	public Timestamp getActivationDate()
	{
		return activationInfo.getActivationDate();
	}

	/**
	 * @param activationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setActivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setActivationDate(final Timestamp activationDate)
	{
		activationInfo.setActivationDate(activationDate);
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getDeactivationDate()
	 */
	@Override
	public Timestamp getDeactivationDate()
	{
		return activationInfo.getDeactivationDate();
	}

	/**
	 * @param deactivationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setDeactivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setDeactivationDate(final Timestamp deactivationDate)
	{
		activationInfo.setDeactivationDate(deactivationDate);
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getVersionSet()
	 */
	@Override
	public SortedSet<Version> getVersionSet()
	{
		return CollectionUtil.<Version> newSortedSet(getVersionSetInternal());
	}

	/**
	 * @param versionSet
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setVersionSet(java.util.SortedSet)
	 */
	@Override
	public void setVersionSet(final SortedSet<? extends Version> versionSet)
	{
		getVersionSetInternal().clear();
		addVersions(versionSet);
	}

	/**
	 * @param e
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#addVersion(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public void addVersion(final Version e)
	{
		getVersionSetInternal().add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#addVersions(java.util.Set)
	 */
	@Override
	public void addVersions(final Collection<? extends Version> c)
	{
		getVersionSetInternal().addAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#removeVersion(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public void removeVersion(final Version o)
	{
		getVersionSetInternal().remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#removeVersions(java.util.Collection)
	 */
	@Override
	public void removeVersions(final Collection<? extends Version> c)
	{
		getVersionSetInternal().removeAll(c);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Must return an actual reference to the version set, not a defensive copy.
	 * 
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getVersionSet()
	 */
	abstract protected SortedSet<Version> getVersionSetInternal();

	/**
	 * A factory method of an empty version entity, of the type associated with this asset
	 * entity.
	 * 
	 * @return empty version entity instance
	 */
	abstract protected AbstractVersionEntity newVersionEntity();
}
