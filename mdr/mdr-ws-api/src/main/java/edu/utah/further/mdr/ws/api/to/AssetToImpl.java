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
package edu.utah.further.mdr.ws.api.to;

import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.SortedSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.discrete.HasSettableIdentifier;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Version;
import edu.utah.further.mdr.api.to.asset.AssetTo;

/**
 * An MDR asset transfer object. JAXB-serializable to and from XML.
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
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = AssetToImpl.ENTITY_NAME, propOrder =
{ "id", "namespaceId", "typeId", "label", "description", "activationInfo", "versionIdSet" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AssetToImpl.ENTITY_NAME)
public class AssetToImpl implements AssetTo, HasSettableIdentifier<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "asset";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@XmlElement(name = "id", required = false)
	private Long id;

	/**
	 * Link to this asset's namespace entity.
	 */
	@XmlTransient
	private AssetTo namespace;

	/**
	 * Link to this asset's namespace entity's ID. TOs do not support deep copy of an
	 * Asset entity because it is expensive, and the entity tree may have cycles.
	 */
	@XmlElement(name = "namespaceId", required = false)
	private Long namespaceId;

	/**
	 * Link to this asset's asset type entity.
	 */
	@XmlTransient
	private AssetTo type;

	/**
	 * Link to this asset's asset type entity ID.
	 */
	@XmlElement(name = "typeId", required = false)
	private Long typeId;

	/**
	 * Label (short description of the asset).
	 */
	@XmlElement(name = "label", required = false)
	private String label;

	/**
	 * Long description of the asset.
	 */
	@XmlElement(name = "description", required = false)
	private String description;

	/**
	 * Holds resource activation dates.
	 */
	@Final
	@XmlElement(name = "activationInfo", required = false)
	private ActivationInfoToImpl activationInfo = new ActivationInfoToImpl();

	/**
	 * Set of versions of this asset. Only one can be active at any given time.
	 */
	@XmlTransient
	private SortedSet<Version> versionSet = newSortedSet();

	/**
	 * Set of version IDs of this asset.
	 */
	@XmlElement(name = "versionIdSet", required = false)
	private SortedSet<Long> versionIdSet = newSortedSet();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public AssetToImpl()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public AssetToImpl(final Asset other)
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
		if (namespace != null)
		{
			builder.append("namespace", namespace);
		}
		if (type != null)
		{
			builder.append("type", type);
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
	public AssetToImpl copyFrom(final Asset other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		this.id = other.getId();
		final Asset otherNamespace = other.getNamespace();
		if (otherNamespace != null)
		{
			this.namespaceId = otherNamespace.getId();
		}
		final Asset otherType = other.getType();
		if (otherType != null)
		{
			this.typeId = otherType.getId();
		}
		this.label = other.getLabel();
		this.description = other.getDescription();
		this.activationInfo = new ActivationInfoToImpl().copyFrom(other
				.getActivationInfo());

		// Deep-copy collection references (IDs) but do not deep-copy their elements
		// unless it's easy to deep-copy them too. Note: the hibernate entity (other) 's
		// version set must be eagerly fetched so that copying it here, outside the
		// Hibernate session, does not throw a lazy init exception.
		this.versionSet = newSortedSet();
		this.versionIdSet = newSortedSet();
		for (final Version version : other.getVersionSet())
		{
			// TODO: add version TOs here in the future
			// addVersion(new VersionEntity().copyFrom(version));
			versionIdSet.add(version.getId());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.discrete.HasSettableIdentifier#setId(java.lang.Comparable
	 * )
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	// ========================= IMPLEMENTATION: Asset =====================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getNamespace()
	 */
	@Override
	public Asset getNamespace()
	{
		return namespace;
	}

	/**
	 * @param namespace
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setNamespace(edu.utah.further.mdr.api.domain.asset.Asset)
	 */
	@Override
	public void setNamespace(final Asset namespace)
	{
		this.namespace = (AssetTo) namespace;
		if (namespace != null)
		{
			this.namespaceId = namespace.getId();
		}
	}

	/**
	 * Return the namespaceId property.
	 * 
	 * @return the namespaceId
	 */
	@Override
	public Long getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getType()
	 */
	@Override
	public Asset getType()
	{
		return type;
	}

	/**
	 * Return the typeId property.
	 * 
	 * @return the typeId
	 */
	@Override
	public Long getTypeId()
	{
		return typeId;
	}

	/**
	 * @param type
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setType(edu.utah.further.mdr.api.domain.asset.Asset)
	 */
	@Override
	public void setType(final Asset type)
	{
		this.type = (AssetTo) type;
		if (type != null)
		{
			this.typeId = type.getId();
		}
	}

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
	public ActivationInfoToImpl getActivationInfo()
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
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getActivationTime()
	 */
	@Override
	public Long getActivationTime()
	{
		final Timestamp ts = getActivationDate();
		return (ts == null) ? null : new Long(ts.getTime());
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
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getDeactivationTime()
	 */
	@Override
	public Long getDeactivationTime()
	{
		final Timestamp ts = getDeactivationDate();
		return (ts == null) ? null : new Long(ts.getTime());
	}

	/**
	 * @param activationTime
	 * @see edu.utah.further.mdr.api.to.asset.AssetTo#setActivationTime(java.lang.Long)
	 */
	public void setActivationTime(final Long activationTime)
	{
		activationInfo.setActivationDate((activationTime == null) ? null : new Timestamp(
				activationTime.longValue()));
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
	 * @param deactivationTime
	 * @see edu.utah.further.mdr.api.to.asset.AssetTo#setDeactivationTime(java.lang.Long)
	 */
	public void setDeactivationTime(final Long deactivationTime)
	{
		activationInfo.setDeactivationDate((deactivationTime == null) ? null
				: new Timestamp(deactivationTime.longValue()));
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getVersionSet()
	 */
	@Override
	public SortedSet<Version> getVersionSet()
	{
		return CollectionUtil.<Version> newSortedSet(versionSet);
	}

	/**
	 * Return the versionIdSet property.
	 * 
	 * @return the versionIdSet
	 */
	public SortedSet<Long> getVersionIdSet()
	{
		return newSortedSet(versionIdSet);
	}

	/**
	 * @param versionSet
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setVersionSet(java.util.SortedSet)
	 */
	@Override
	public void setVersionSet(final SortedSet<? extends Version> versionSet)
	{
		this.versionSet.clear();
		addVersions(versionSet);
	}

	/**
	 * @param version
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#addVersion(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public void addVersion(final Version version)
	{
		final Long versionId = (version == null) ? null : version.getId();
		if (versionId != null)
		{
			versionIdSet.add(versionId);
		}
		versionSet.add(version);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#addVersions(java.util.Set)
	 */
	@Override
	public void addVersions(final Collection<? extends Version> c)
	{
		for (final Version version : c)
		{
			addVersion(version);
		}
	}

	/**
	 * @param version
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#removeVersion(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public void removeVersion(final Version version)
	{
		final Long versionId = (version == null) ? null : version.getId();
		versionSet.remove(versionId);
		versionSet.remove(version);
	}

	/**
	 * @param c
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#removeVersions(java.util.Collection)
	 */
	@Override
	public void removeVersions(final Collection<? extends Version> c)
	{
		for (final Version version : c)
		{
			removeVersion(version);
		}
	}

}
