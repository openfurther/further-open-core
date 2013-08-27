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
package edu.utah.further.mdr.api.domain.asset;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * MDR resource abstraction.
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
 * @version Mar 20, 2009
 */
public interface Resource extends PersistentEntity<Long>, MutableActivationInfo,
		CopyableFrom<Resource, Resource>
{
	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getStorageCode()
	 */
	StorageCode getStorageCode();

	/**
	 * @param storageCode
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setStorageCode(edu.utah.further.mdr.api.domain.asset.StorageCode)
	 */
	void setStorageCode(StorageCode storageCode);

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getMimeType()
	 */
	String getMimeType();

	/**
	 * @param mimeType
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setMimeType(java.lang.String)
	 */
	void setMimeType(String mimeType);

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getText()
	 */
	String getText();

	/**
	 * @param text
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setText(java.lang.String)
	 */
	void setText(String text);

	/**
	 * Return the activationInfo property.
	 * 
	 * @return the activationInfo
	 */
	ActivationInfo getActivationInfo();

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getActivationDate()
	 */
	@Override
	Timestamp getActivationDate();

	/**
	 * @param activationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setActivationDate(java.sql.Timestamp)
	 */
	@Override
	void setActivationDate(Timestamp activationDate);

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDeactivationDate()
	 */
	@Override
	Timestamp getDeactivationDate();

	/**
	 * @param deactivationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setDeactivationDate(java.sql.Timestamp)
	 */
	@Override
	void setDeactivationDate(Timestamp deactivationDate);

	/**
	 * Return the url property.
	 * 
	 * @return the url
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getUrl()
	 */
	String getUrl();

	/**
	 * Set a new value for the url property.
	 * 
	 * @param url
	 *            the url to set
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setUrl(java.lang.String)
	 */
	void setUrl(String url);

	/**
	 * Return the type property.
	 * 
	 * @return the type
	 */
	ResourceType getType();

	/**
	 * Set a new value for the type property.
	 * 
	 * @param type
	 *            the type to set
	 */
	void setType(ResourceType type);

	/**
	 * Return the description property.
	 * 
	 * @return the description
	 */
	String getDescription();

	/**
	 * Set a new value for the description property.
	 * 
	 * @param description
	 *            the description to set
	 */
	void setDescription(String description);

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name);

	/**
	 * Return the fileName property.
	 * 
	 * @return the fileName
	 */
	String getFileName();

	/**
	 * Set a new value for the fileName property.
	 * 
	 * @param fileName
	 *            the fileName to set
	 */
	void setFileName(String fileName);

	/**
	 * Return the xml property.
	 * 
	 * @return the xml
	 */
	String getXml();

	/**
	 * Set a new value for the xml property.
	 * 
	 * @param xml
	 *            the xml to set
	 */
	void setXml(String xml);

	/**
	 * Return the clob property.
	 * 
	 * @return the clob
	 */
	String getClob();

	/**
	 * Set a new value for the clob property.
	 * 
	 * @param clob
	 *            the clob to set
	 */
	void setClob(String clob);

	/**
	 * Return the blob property.
	 * 
	 * @return the blob
	 */
	Blob getBlob();

	/**
	 * Set a new value for the blob property.
	 * 
	 * @param blob
	 *            the blob to set
	 */
	void setBlob(Blob blob);

	/**
	 * Return the asset property.
	 * 
	 * @return the asset
	 */
	Asset getAsset();

	/**
	 * Set a new value for the asset property.
	 * 
	 * @param asset
	 *            the asset to set
	 */
	void setAsset(Asset asset);

	/**
	 * Return the version property.
	 * 
	 * @return the version
	 */
	Version getVersion();

	/**
	 * Set a new value for the version property.
	 * 
	 * @param version
	 *            the version to set
	 */
	void setVersion(Version version);

	/**
	 * Return the versionNumber property.
	 * 
	 * @return the versionNumber
	 */
	Long getVersionNumber();

	/**
	 * Set a new value for the versionNumber property.
	 * 
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	void setVersionNumber(Long versionNumber);

	/**
	 * Return the path property.
	 * 
	 * @return the path
	 */
	String getPath();

	/**
	 * Set a new value for the path property.
	 * 
	 * @param path
	 *            the path to set
	 */
	void setPath(final String path);

	/**
	 * Return the linkedResourcePaths property.
	 * 
	 * @return the linkedResourcePaths
	 */
	List<String> getLinkedResourcePaths();

	/**
	 * Set a new value for the linkedResourcePaths property.
	 * 
	 * @param linkedResourcePaths
	 *            the linkedResourcePaths to set
	 */
	void setLinkedResourcePaths(final List<String> linkedResourcePaths);
}