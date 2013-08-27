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

/**
 * An abstract adapter class which throws an {@link UnsupportedOperationException} for
 * every operation. It's purpose is primarily for convenience when implementing a
 * new/anonymous {@link Resource}.
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
 * @version Jul 8, 2013
 */
public abstract class ResourceAdapter implements Resource
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public Resource copyFrom(final Resource other)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getStorageCode()
	 */
	@Override
	public StorageCode getStorageCode()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setStorageCode(edu.utah.further.
	 * mdr.api.domain.asset.StorageCode)
	 */
	@Override
	public void setStorageCode(final StorageCode storageCode)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getMimeType()
	 */
	@Override
	public String getMimeType()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setMimeType(java.lang.String)
	 */
	@Override
	public void setMimeType(final String mimeType)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getText()
	 */
	@Override
	public String getText()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setText(java.lang.String)
	 */
	@Override
	public void setText(final String text)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getActivationInfo()
	 */
	@Override
	public ActivationInfo getActivationInfo()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getActivationDate()
	 */
	@Override
	public Timestamp getActivationDate()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setActivationDate(java.sql.Timestamp
	 * )
	 */
	@Override
	public void setActivationDate(final Timestamp activationDate)
	{
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDeactivationDate()
	 */
	@Override
	public Timestamp getDeactivationDate()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setDeactivationDate(java.sql.Timestamp
	 * )
	 */
	@Override
	public void setDeactivationDate(final Timestamp deactivationDate)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getUrl()
	 */
	@Override
	public String getUrl()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(final String url)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getType()
	 */
	@Override
	public ResourceType getType()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setType(edu.utah.further.mdr.api
	 * .domain.asset.ResourceType)
	 */
	@Override
	public void setType(final ResourceType type)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDescription()
	 */
	@Override
	public String getDescription()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getName()
	 */
	@Override
	public String getName()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getFileName()
	 */
	@Override
	public String getFileName()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setFileName(java.lang.String)
	 */
	@Override
	public void setFileName(final String fileName)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getXml()
	 */
	@Override
	public String getXml()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setXml(java.lang.String)
	 */
	@Override
	public void setXml(final String xml)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getClob()
	 */
	@Override
	public String getClob()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setClob(java.lang.String)
	 */
	@Override
	public void setClob(final String clob)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getBlob()
	 */
	@Override
	public Blob getBlob()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setBlob(java.sql.Blob)
	 */
	@Override
	public void setBlob(final Blob blob)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getAsset()
	 */
	@Override
	public Asset getAsset()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setAsset(edu.utah.further.mdr.api
	 * .domain.asset.Asset)
	 */
	@Override
	public void setAsset(final Asset asset)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getVersion()
	 */
	@Override
	public Version getVersion()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setVersion(edu.utah.further.mdr.
	 * api.domain.asset.Version)
	 */
	@Override
	public void setVersion(final Version version)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getVersionNumber()
	 */
	@Override
	public Long getVersionNumber()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setVersionNumber(java.lang.Long)
	 */
	@Override
	public void setVersionNumber(final Long versionNumber)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getPath()
	 */
	@Override
	public String getPath()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setPath(java.lang.String)
	 */
	@Override
	public void setPath(final String path)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getLinkedResourcePaths()
	 */
	@Override
	public List<String> getLinkedResourcePaths()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.Resource#setLinkedResourcePaths(java.util
	 * .List)
	 */
	@Override
	public void setLinkedResourcePaths(final List<String> linkedResourcePaths)
	{
		throw new UnsupportedOperationException();
	}

}
