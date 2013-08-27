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
package edu.utah.further.mdr.data.impl.domain.asset;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.utah.further.core.data.oracle.type.OracleXmlType;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Version;
import edu.utah.further.mdr.data.common.domain.asset.AbstractResourceEntity;

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
@Entity
@Table(name = "ASSET_RESOURCE_V")
public class ResourceEntity extends AbstractResourceEntity
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Link to this resource's owning asset.
	 */
	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private AssetEntity asset;

	/**
	 * Link to this resource's owning asset version.
	 */
	@ManyToOne
	@JoinColumn(name = "ASSET_VERSION_SEQ_ID")
	private VersionEntity version;
	
	/**
	 * XML storage. NOTE: THIS FIELD CAN ONLY BE READ FROM, NOT WRITTEN TO, THE DATABASE.
	 * <p>
	 * We only read it here using a fast converter to a String. For read/write, use the
	 * {@link OracleXmlType} converter instead.
	 */
	@Column(name = "RESOURCE_XML", nullable = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String xml;

	// ========================= IMPL: Resource ============================

	/**
	 * Return the asset property.
	 * 
	 * @return the asset
	 */
	@Override
	public Asset getAsset()
	{
		return asset;
	}

	/**
	 * Set a new value for the asset property.
	 * 
	 * @param asset
	 *            the asset to set
	 */
	@Override
	public void setAsset(final Asset asset)
	{
		this.asset = (AssetEntity) asset;
	}

	/**
	 * Return the version property.
	 * 
	 * @return the version
	 */
	@Override
	public Version getVersion()
	{
		return version;
	}

	/**
	 * Set a new value for the version property.
	 * 
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(final Version version)
	{
		this.version = (VersionEntity) version;
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
	 * @param xml
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setXml(java.lang.String)
	 */
	@Override
	public void setXml(final String xml)
	{
		this.xml = xml;
	}
}
