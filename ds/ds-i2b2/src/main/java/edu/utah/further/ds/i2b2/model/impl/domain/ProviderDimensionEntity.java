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
package edu.utah.further.ds.i2b2.model.impl.domain;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension;
import edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId;

/**
 * Provider Dimension Entity
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
@Entity
@Table(name = "PROVIDER_DIMENSION")
public class ProviderDimensionEntity implements PersistentEntity<ProviderDimensionId>,
		ProviderDimension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2141095815769601187L;

	@EmbeddedId
	protected ProviderDimensionPK id;

	@Column(name = "NAME_CHAR")
	private String nameChar;
	@Lob
	@Column(name = "PROVIDER_BLOB")
	private String providerBlob;
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@Column(name = "DOWNLOAD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date downloadDate;
	@Column(name = "IMPORT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date importDate;
	@Column(name = "SOURCESYSTEM_CD")
	private String sourcesystemCd;
	@Column(name = "UPLOAD_ID")
	private BigInteger uploadId;

	public ProviderDimensionEntity()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getId()
	 */
	@Override
	public ProviderDimensionId getId()
	{
		return id;
	}

	/**
	 * @param providerDimensionPK
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setId(edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId)
	 */
	@Override
	public void setId(ProviderDimensionId providerDimensionPK)
	{
		this.id = (ProviderDimensionPK) providerDimensionPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getNameChar()
	 */
	@Override
	public String getNameChar()
	{
		return nameChar;
	}

	/**
	 * @param nameChar
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setNameChar(java.lang.String)
	 */
	@Override
	public void setNameChar(String nameChar)
	{
		this.nameChar = nameChar;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getProviderBlob()
	 */
	@Override
	public String getProviderBlob()
	{
		return providerBlob;
	}

	/**
	 * @param providerBlob
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setProviderBlob(java.lang.String)
	 */
	@Override
	public void setProviderBlob(String providerBlob)
	{
		this.providerBlob = providerBlob;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension#setUploadId(java.math.BigInteger)
	 */
	@Override
	public void setUploadId(BigInteger uploadId)
	{
		this.uploadId = uploadId;
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final ProviderDimensionEntity that = (ProviderDimensionEntity) object;
		return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ProviderDimension copy()
	{
		final ProviderDimensionEntity entity = new ProviderDimensionEntity();
		entity.setDownloadDate(this.getDownloadDate());
		entity.setId(this.getId().copy());
		entity.setImportDate(this.getImportDate());
		entity.setNameChar(this.getNameChar());
		entity.setProviderBlob(this.getProviderBlob());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadId(this.getUploadId());
		return entity;
	}

}
