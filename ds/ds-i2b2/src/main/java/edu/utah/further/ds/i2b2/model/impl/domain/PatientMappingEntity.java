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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.i2b2.model.api.domain.PatientMapping;
import edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId;

/**
 * Patient Mapping Entity
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
@Table(name = "PATIENT_MAPPING")
public class PatientMappingEntity implements PersistentEntity<PatientMappingId>,
		PatientMapping
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782043493851252879L;

	@EmbeddedId
	protected PatientMappingPK id;

	@Basic(optional = false)
	@Column(name = "PATIENT_NUM")
	private BigInteger patientNum;
	@Column(name = "PATIENT_IDE_STATUS")
	private String patientIdeStatus;
	@Column(name = "UPLOAD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;
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

	public PatientMappingEntity()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getId()
	 */
	@Override
	public PatientMappingId getId()
	{
		return id;
	}

	/**
	 * @param patientMappingPK
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setId(edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId)
	 */
	@Override
	public void setId(PatientMappingId patientMappingPK)
	{
		this.id = (PatientMappingPK) patientMappingPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getPatientNum()
	 */
	@Override
	public BigInteger getPatientNum()
	{
		return patientNum;
	}

	/**
	 * @param patientNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setPatientNum(java.math.BigInteger)
	 */
	@Override
	public void setPatientNum(BigInteger patientNum)
	{
		this.patientNum = patientNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getPatientIdeStatus()
	 */
	@Override
	public String getPatientIdeStatus()
	{
		return patientIdeStatus;
	}

	/**
	 * @param patientIdeStatus
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setPatientIdeStatus(java.lang.String)
	 */
	@Override
	public void setPatientIdeStatus(String patientIdeStatus)
	{
		this.patientIdeStatus = patientIdeStatus;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getUploadDate()
	 */
	@Override
	public Date getUploadDate()
	{
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setUploadDate(java.util.Date)
	 */
	@Override
	public void setUploadDate(Date uploadDate)
	{
		this.uploadDate = uploadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMapping#setUploadId(java.math.BigInteger)
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

		final PatientMappingEntity that = (PatientMappingEntity) object;
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
	public PatientMapping copy()
	{
		final PatientMappingEntity entity = new PatientMappingEntity();
		entity.setDownloadDate(this.getDownloadDate());
		entity.setId(this.getId().copy());
		entity.setImportDate(this.getImportDate());
		entity.setPatientIdeStatus(this.getPatientIdeStatus());
		entity.setPatientNum(this.getPatientNum());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadDate(this.getUpdateDate());
		entity.setUploadId(this.getUploadId());
		return entity;
	}

}
