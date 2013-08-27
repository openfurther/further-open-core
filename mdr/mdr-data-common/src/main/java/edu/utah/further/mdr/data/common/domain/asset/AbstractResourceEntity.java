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

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.mdr.api.domain.asset.ActivationInfo;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.domain.asset.StorageCode;

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
@MappedSuperclass
public abstract class AbstractResourceEntity implements Resource
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
	@Column(name = "ASSET_RESOURCE_ID")
	@Final
	private Long id;

	/**
	 * The MDR virtual path to this resource. Uniquely identifies the resource.
	 */
	@Column(name = "RELATIVE_RESOURCE_URL", length = 255, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String path;

	/**
	 * Version number of this resource's owning version entity. For optimization of
	 * resource searches. Must match <code>version.getId()</code>.
	 */
	@Column(name = "ASSET_VERSION")
	@Basic(fetch = FetchType.EAGER)
	private Long versionNumber;

	/**
	 * Short description of the resource.
	 */
	@Column(name = "RESOURCE_NAME", length = 255, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String name;

	/**
	 * Long description of the resource.
	 */
	@Column(name = "RESOURCE_DSC", length = 2000, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String description;

	/**
	 * Name of the file associated with this resource.
	 */
	@Column(name = "RESOURCE_FILE_NAME", length = 255, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String fileName;

	/**
	 * Resource storage type code.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STORAGE_CD", length = 20, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private StorageCode storageCode;

	/**
	 * Mime type of the resource's stored representation.
	 */
	@Column(name = "MIME_TYPE", length = 100, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String mimeType;

	/**
	 * Text storage.
	 */
	@Column(name = "RESOURCE_TEXT", length = 4000, nullable = true)
	@Basic(fetch = FetchType.LAZY)
	private String text;

	/**
	 * Character Large Object (CLOB) storage.
	 * <p>
	 * Trying to set column size to support both Oracle and MySQL.
	 * 
	 * @see http://www.elver.org/hibernate/ejb3_features.html
	 */
	@Column(name = "RESOURCE_CLOB", nullable = true, length = 10485760)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String clob;

	/**
	 * <p>
	 * Trying to set column size to support both Oracle and MySQL.
	 * 
	 * @see http://www.elver.org/hibernate/ejb3_features.html Binary Large Object (CLOB)
	 *      storage.
	 */
	@Column(name = "RESOURCE_BLOB", nullable = true, length = 10485760)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private Blob blob;

	/**
	 * URL storage.
	 */
	@Column(name = "RESOURCE_URL", length = 1000, nullable = true)
	@Basic(fetch = FetchType.LAZY)
	private String url;

	/**
	 * Holds resource activation dates.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "activationDate", column = @Column(name = "RESOURCE_ACTIVATE_DT", nullable = true)),
			@AttributeOverride(name = "deactivationDate", column = @Column(name = "RESOURCE_DEACTIVATE_DT", nullable = true)) })
	@Final
	private ActivationInfoEntity activationInfo = new ActivationInfoEntity();

	// ========================= FIELDS: ASSOCIATIONS ======================

	/**
	 * Link to this resource's asset type entity.
	 */
	@ManyToOne
	@JoinColumn(name = "ASSET_RESOURCE_TYPE_ID")
	private ResourceTypeEntity type;

	/**
	 * List of MDR paths of resources linked to this resource. The resource therefore
	 * implements the Composite Pattern. Each path references the currently-active
	 * resource with that path.
	 */
	// @OneToMany(mappedBy = "???", fetch = FetchType.EAGER)
	@Transient
	// implementation to be added
	private List<String> linkedResourcePaths = CollectionUtil.newList();

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
	public Resource copyFrom(final Resource other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		this.path = other.getPath();
		this.type = (ResourceTypeEntity) other.getType();
		setAsset(other.getAsset());
		setVersion(other.getVersion());
		setPath(other.getPath());
		this.versionNumber = other.getVersionNumber();
		this.name = other.getName();
		this.description = other.getDescription();
		this.fileName = other.getFileName();
		this.storageCode = other.getStorageCode();
		this.mimeType = other.getMimeType();

		this.text = other.getText();
		setXml(other.getXml());
		this.clob = other.getClob();
		this.blob = other.getBlob();
		this.url = other.getUrl();

		this.activationInfo = new ActivationInfoEntity().copyFrom(other
				.getActivationInfo());

		// Deep-copy collection references but soft-copy their elements

		// TODO: copy resource links here

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
	 * Return the activationInfo property.
	 * 
	 * @return the activationInfo
	 */
	@Override
	public ActivationInfo getActivationInfo()
	{
		return activationInfo;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getActivationDate()
	 */
	@Override
	public Timestamp getActivationDate()
	{
		return activationInfo.getActivationDate();
	}

	/**
	 * @param activationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setActivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setActivationDate(final Timestamp activationDate)
	{
		activationInfo.setActivationDate(activationDate);
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDeactivationDate()
	 */
	@Override
	public Timestamp getDeactivationDate()
	{
		return activationInfo.getDeactivationDate();
	}

	/**
	 * @param deactivationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setDeactivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setDeactivationDate(final Timestamp deactivationDate)
	{
		activationInfo.setDeactivationDate(deactivationDate);
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
	 * @param type
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setType(edu.utah.further.mdr.data.impl.domain.asset.AbstractAssetEntity)
	 */
	@Override
	public void setType(final ResourceType type)
	{
		this.type = (ResourceTypeEntity) type;
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
	 * Return the name property.
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
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
