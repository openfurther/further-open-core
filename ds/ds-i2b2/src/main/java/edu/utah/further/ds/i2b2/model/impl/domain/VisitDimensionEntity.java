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
import edu.utah.further.ds.i2b2.model.api.domain.VisitDimension;
import edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId;

/**
 * Visit Dimension Entity
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
@Table(name = "VISIT_DIMENSION")
public class VisitDimensionEntity implements PersistentEntity<VisitDimensionId>,
		VisitDimension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5586613974287912320L;

	@EmbeddedId
	protected VisitDimensionPK id;

	@Column(name = "ACTIVE_STATUS_CD")
	private String activeStatusCd;
	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@Column(name = "INOUT_CD")
	private String inoutCd;
	@Column(name = "LOCATION_CD")
	private String locationCd;
	@Column(name = "LOCATION_PATH")
	private String locationPath;
	@Lob
	@Column(name = "VISIT_BLOB")
	private String visitBlob;
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

	public VisitDimensionEntity()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getId()
	 */
	@Override
	public VisitDimensionId getId()
	{
		return id;
	}

	/**
	 * @param visitDimensionPK
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setId(edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId)
	 */
	@Override
	public void setId(VisitDimensionId visitDimensionPK)
	{
		this.id = (VisitDimensionPK) visitDimensionPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getActiveStatusCd()
	 */
	@Override
	public String getActiveStatusCd()
	{
		return activeStatusCd;
	}

	/**
	 * @param activeStatusCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setActiveStatusCd(java.lang.String)
	 */
	@Override
	public void setActiveStatusCd(String activeStatusCd)
	{
		this.activeStatusCd = activeStatusCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getStartDate()
	 */
	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setStartDate(java.util.Date)
	 */
	@Override
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getEndDate()
	 */
	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setEndDate(java.util.Date)
	 */
	@Override
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getInoutCd()
	 */
	@Override
	public String getInoutCd()
	{
		return inoutCd;
	}

	/**
	 * @param inoutCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setInoutCd(java.lang.String)
	 */
	@Override
	public void setInoutCd(String inoutCd)
	{
		this.inoutCd = inoutCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getLocationCd()
	 */
	@Override
	public String getLocationCd()
	{
		return locationCd;
	}

	/**
	 * @param locationCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setLocationCd(java.lang.String)
	 */
	@Override
	public void setLocationCd(String locationCd)
	{
		this.locationCd = locationCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getLocationPath()
	 */
	@Override
	public String getLocationPath()
	{
		return locationPath;
	}

	/**
	 * @param locationPath
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setLocationPath(java.lang.String)
	 */
	@Override
	public void setLocationPath(String locationPath)
	{
		this.locationPath = locationPath;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getVisitBlob()
	 */
	@Override
	public String getVisitBlob()
	{
		return visitBlob;
	}

	/**
	 * @param visitBlob
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setVisitBlob(java.lang.String)
	 */
	@Override
	public void setVisitBlob(String visitBlob)
	{
		this.visitBlob = visitBlob;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimension#setUploadId(java.math.BigInteger)
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

		final VisitDimensionEntity that = (VisitDimensionEntity) object;
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
	public VisitDimension copy()
	{
		final VisitDimensionEntity entity = new VisitDimensionEntity();
		entity.setActiveStatusCd(this.getActiveStatusCd());
		entity.setDownloadDate(this.getDownloadDate());
		entity.setEndDate(this.getEndDate());
		entity.setId(this.getId());
		entity.setImportDate(this.getImportDate());
		entity.setInoutCd(this.getInoutCd());
		entity.setLocationCd(this.getLocationCd());
		entity.setLocationPath(this.getLocationPath());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setStartDate(this.getStartDate());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadId(this.getUploadId());
		entity.setVisitBlob(this.getVisitBlob());
		return entity;
	}
}
