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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Version;
import edu.utah.further.mdr.data.common.domain.asset.AbstractVersionEntity;

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
@Entity
@Table(name = "ASSET_VERSION")
public class VersionEntity extends AbstractVersionEntity
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Reverse link to this version's asset entity.
	 */
	@ManyToOne
	@JoinColumn(name = "ASSET_ID")
	private AssetEntity asset;

	/**
	 * Asset properties in XML format.
	 */
	@Column(name = "ASSET_PROPERTIES_XML", nullable = true)
	@Type(type = "xml-type")
	private String propertiesXml;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for Hibernate.
	 */
	public VersionEntity()
	{
		super();
	}

	/**
	 * A copy constructor.
	 * 
	 * @param other
	 *            other entity to copy from
	 */
	public VersionEntity(final Version other)
	{
		super(other);
	}

	// ========================= IMPL: Version =============================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getAsset()
	 */
	@Override
	public Asset getAsset()
	{
		return asset;
	}

	/**
	 * @param asset
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setAsset(edu.utah.further.mdr.data.impl.domain.asset.AbstractAssetEntity)
	 */
	@Override
	public void setAsset(final Asset asset)
	{
		this.asset = (AssetEntity) asset;
	}

	// ========================= IMPL: AbstractVersionEntity ===============

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Version#getPropertiesXml()
	 */
	@Override
	public String getPropertiesXml()
	{
		return propertiesXml;
	}

	/**
	 * @param propertiesXml
	 * @see edu.utah.further.mdr.api.domain.asset.Version#setPropertiesXml(java.lang.String)
	 */
	@Override
	public void setPropertiesXml(final String propertiesXml)
	{
		this.propertiesXml = propertiesXml;
	}
}
