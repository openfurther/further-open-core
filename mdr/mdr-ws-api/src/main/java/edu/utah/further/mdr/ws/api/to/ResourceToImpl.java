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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

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
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.domain.asset.StorageCode;
import edu.utah.further.mdr.api.domain.asset.Version;
import edu.utah.further.mdr.api.to.asset.AssetTo;
import edu.utah.further.mdr.api.to.asset.ResourceTo;
import edu.utah.further.mdr.api.to.asset.VersionTo;

/**
 * An MDR resource persistent entity.
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
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = ResourceToImpl.ENTITY_NAME, propOrder =
{ "id", "path", "typeId", "assetId", "versionId", "versionNumber", "name", "description",
		"fileName", "storageCode", "mimeType", "activationInfo", "url" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = ResourceToImpl.ENTITY_NAME)
public class ResourceToImpl implements ResourceTo, HasSettableIdentifier<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "resource";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@XmlElement(name = "id", required = false)
	private Long id;

	/**
	 * The MDR virtual path to this resource. Uniquely identifies the resource.
	 */
	@XmlElement(name = "path", required = false)
	private String path;

	/**
	 * Link to this resource's asset type entity.
	 */
	@XmlTransient
	private ResourceType type;

	/**
	 * Link to this asset's asset type entity ID.
	 */
	@XmlElement(name = "typeId", required = false)
	private Long typeId;

	/**
	 * Link to this resource's asset entity.
	 */
	@XmlTransient
	private AssetTo asset;

	/**
	 * Link to the asset entity's ID. TOs do not support deep copy of an Asset entity
	 * because it is expensive, and the entity tree may have cycles.
	 */
	@XmlElement(name = "assetId", required = false)
	private Long assetId;

	/**
	 * Link to this resource's version entity.
	 */
	@XmlTransient
	private VersionTo version;

	/**
	 * Link to the version entity's ID. TOs do not support deep copy of an Asset entity
	 * because it is expensive, and the entity tree may have cycles.
	 */
	@XmlElement(name = "versionId", required = false)
	private Long versionId;

	/**
	 * Version number of this resource's owning version entity. For optimization of
	 * resource searches. Must match <code>version.getId()</code>.
	 */
	@XmlElement(name = "versionNumber", required = false)
	private Long versionNumber;

	/**
	 * Short description of the resource.
	 */
	@XmlElement(name = "name", required = false)
	private String name;

	/**
	 * Long description of the resource.
	 */
	@XmlElement(name = "description", required = false)
	private String description;

	/**
	 * Name of the file associated with this resource.
	 */
	@XmlElement(name = "fileName", required = false)
	private String fileName;

	/**
	 * Resource storage type code.
	 */
	@XmlElement(name = "storageCode", required = false)
	private StorageCode storageCode;

	/**
	 * Mime type of the resource's stored representation.
	 */
	@XmlElement(name = "mimeType", required = false)
	private String mimeType;

	/**
	 * Text storage.
	 */
	@XmlTransient
	private String text;

	/**
	 * XML storage. NOTE: THIS FIELD CAN ONLY BE READ FROM, NOT WRITTEN TO, THE DATABASE.
	 * <p>
	 * We only read it here using a fast converter to a String. For read/write, use the
	 * <code>OracleXmlType</code> converter instead.
	 */
	@XmlTransient
	private String xml;
	
	/**
	 * Character Large Object (CLOB) storage.
	 */
	@XmlTransient
	private String clob;

	/**
	 * Binary Large Object (CLOB) storage.
	 */
	@XmlTransient
	private Blob blob;

	/**
	 * URL storage.
	 */
	@XmlElement(name = "url", required = false)
	private String url;

	/**
	 * Holds resource activation dates.
	 */
	@Final
	@XmlElement(name = "activationInfo", required = false)
	private ActivationInfoToImpl activationInfo = new ActivationInfoToImpl();

	/**
	 * List of MDR paths to resources linked to this resource. The resource therefore
	 * implements the Composite Pattern. Each path references the currently-active
	 * resource with that path.
	 */
	// @XmlElementWrapper(name = "linkedResourcePaths")
	// @XmlElement(name = "path", required = false)
	@XmlTransient
	// to be implemented
	private List<String> linkedResourcePaths = CollectionUtil.newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public ResourceToImpl()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public ResourceToImpl(final Resource other)
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
				.append("id", id)
				.append("storageCode", storageCode);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#copyFrom(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	@Override
	public ResourceToImpl copyFrom(final Resource other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		this.id = other.getId();
		this.path = other.getPath();

		final ResourceType otherType = other.getType();
		if (otherType != null)
		{
			this.typeId = otherType.getId();
		}

		final Asset otherAsset = other.getAsset();
		if (otherAsset != null)
		{
			this.assetId = otherAsset.getId();
		}

		final Version otherVersion = other.getVersion();
		if (otherVersion != null)
		{
			this.versionId = otherVersion.getId();
		}

		final String otherPath = other.getPath();
		if (path != null)
		{
			setPath(otherPath);
		}

		setVersionNumber(other.getVersionNumber());
		setName(other.getName());

		this.description = other.getDescription();
		this.fileName = other.getFileName();
		this.storageCode = other.getStorageCode();
		this.mimeType = other.getMimeType();

		this.text = other.getText();
		this.xml = other.getXml();
		this.clob = other.getClob();
		this.blob = other.getBlob();
		this.url = other.getUrl();

		this.activationInfo = new ActivationInfoToImpl().copyFrom(other
				.getActivationInfo());

		// Deep-copy collection references but soft-copy their elements

		return this;
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getId()
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

	// ========================= IMPLEMENTATION: Resource ==================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getStorageCode()
	 */
	@Override
	public StorageCode getStorageCode()
	{
		return storageCode;
	}

	/**
	 * @param storageCode
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setStorageCode(edu.utah.further.mdr.api.domain.asset.StorageCode)
	 */
	@Override
	public void setStorageCode(final StorageCode storageCode)
	{
		this.storageCode = storageCode;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getMimeType()
	 */
	@Override
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * @param mimeType
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setMimeType(java.lang.String)
	 */
	@Override
	public void setMimeType(final String mimeType)
	{
		this.mimeType = mimeType;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getText()
	 */
	@Override
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setText(java.lang.String)
	 */
	@Override
	public void setText(final String text)
	{
		this.text = text;
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
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getUrl()
	 */
	@Override
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getType()
	 */
	@Override
	public ResourceType getType()
	{
		return type;
	}

	/**
	 * Return the typeId property.
	 * 
	 * @return the typeId
	 * @see edu.utah.further.mdr.api.to.asset.ResourceTo#getTypeId()
	 */
	@Override
	public Long getTypeId()
	{
		return typeId;
	}

	/**
	 * @param type
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setClazz(edu.utah.further.mdr.data.impl.domain.asset.AssetEntity)
	 */
	@Override
	public void setType(final ResourceType type)
	{
		this.type = type;
		if (type != null)
		{
			this.typeId = type.getId();
		}
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getAsset()
	 */
	@Override
	public Asset getAsset()
	{
		return asset;
	}

	/**
	 * Return the assetId property.
	 * 
	 * @return the assetId
	 * @see edu.utah.further.mdr.api.to.asset.ResourceTo#getAssetId()
	 */
	@Override
	public Long getAssetId()
	{
		return assetId;
	}

	/**
	 * @param asset
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setAsset(edu.utah.further.mdr.data.impl.domain.asset.AssetEntity)
	 */
	@Override
	public void setAsset(final Asset asset)
	{
		this.asset = (AssetTo) asset;
		if (asset != null)
		{
			this.assetId = asset.getId();
		}
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.version.Resource#getVersion()
	 */
	@Override
	public Version getVersion()
	{
		return version;
	}

	/**
	 * Return the versionId property.
	 * 
	 * @return the versionId
	 * @see edu.utah.further.mdr.api.to.version.ResourceTo#getVersionId()
	 */
	@Override
	public Long getVersionId()
	{
		return versionId;
	}

	/**
	 * @param version
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setVersion(edu.utah.further.mdr.api.domain.asset.Version)
	 */
	@Override
	public void setVersion(final Version version)
	{
		this.version = (VersionTo) version;
		if (version != null)
		{
			this.versionId = version.getId();
		}
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getFileName()
	 */
	@Override
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setFileName(java.lang.String)
	 */
	@Override
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getXml()
	 */
	@Override
	public String getXml()
	{
		return xml;
	}

	/**
	 * Sets both the xml and xmlLength properties.
	 * 
	 * @param xml
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setXml(java.lang.String)
	 */
	@Override
	public void setXml(final String xml)
	{
		this.xml = xml;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getClob()
	 */
	@Override
	public String getClob()
	{
		return clob;
	}

	/**
	 * @param clob
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setClob(java.lang.String)
	 */
	@Override
	public void setClob(final String clob)
	{
		this.clob = clob;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getBlob()
	 */
	@Override
	public Blob getBlob()
	{
		return blob;
	}

	/**
	 * @param blob
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setBlob(java.sql.Blob)
	 */
	@Override
	public void setBlob(final Blob blob)
	{
		this.blob = blob;
	}

	/**
	 * Return the versionNumber property.
	 * 
	 * @return the versionNumber
	 */
	@Override
	public Long getVersionNumber()
	{
		return versionNumber;
	}

	/**
	 * Set a new value for the versionNumber property.
	 * 
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	@Override
	public void setVersionNumber(final Long versionNumber)
	{
		this.versionNumber = versionNumber;
	}

	/**
	 * Return the path property.
	 * 
	 * @return the path
	 */
	@Override
	public String getPath()
	{
		return path;
	}

	/**
	 * Set a new value for the path property.
	 * 
	 * @param path
	 *            the path to set
	 */
	@Override
	public void setPath(final String path)
	{
		this.path = path;
	}

	/**
	 * Return the linkedResourcePaths property.
	 * 
	 * @return the linkedResourcePaths
	 */
	@Override
	public List<String> getLinkedResourcePaths()
	{
		return linkedResourcePaths;
	}

	/**
	 * Set a new value for the linkedResourcePaths property.
	 * 
	 * @param linkedResourcePaths
	 *            the linkedResourcePaths to set
	 */
	@Override
	public void setLinkedResourcePaths(final List<String> linkedResourcePaths)
	{
		this.linkedResourcePaths = linkedResourcePaths;
	}
}
