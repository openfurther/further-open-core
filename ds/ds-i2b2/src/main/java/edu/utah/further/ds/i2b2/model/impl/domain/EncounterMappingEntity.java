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
import edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping;
import edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId;

/**
 * Encounter Mapping Entity
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
@Table(name = "ENCOUNTER_MAPPING")
public class EncounterMappingEntity implements PersistentEntity<EncounterMappingId>,
		EncounterMapping
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7730972373807676681L;

	@EmbeddedId
	protected EncounterMappingPK id;
	@Basic(optional = false)
	@Column(name = "ENCOUNTER_NUM")
	private BigInteger encounterNum;
	@Column(name = "PATIENT_IDE")
	private String patientIde;
	@Column(name = "PATIENT_IDE_SOURCE")
	private String patientIdeSource;
	@Column(name = "ENCOUNTER_IDE_STATUS")
	private String encounterIdeStatus;
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@Column(name = "UPLOAD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;
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

	public EncounterMappingEntity()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getId()
	 */
	@Override
	public EncounterMappingId getId()
	{
		return id;
	}

	public void setId(EncounterMappingId encounterMappingPK)
	{
		this.id = (EncounterMappingPK) encounterMappingPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getEncounterNum()
	 */
	@Override
	public BigInteger getEncounterNum()
	{
		return encounterNum;
	}

	/**
	 * @param encounterNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setEncounterNum(java.math.BigInteger)
	 */
	@Override
	public void setEncounterNum(BigInteger encounterNum)
	{
		this.encounterNum = encounterNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getPatientIde()
	 */
	@Override
	public String getPatientIde()
	{
		return patientIde;
	}

	/**
	 * @param patientIde
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setPatientIde(java.lang.String)
	 */
	@Override
	public void setPatientIde(String patientIde)
	{
		this.patientIde = patientIde;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getPatientIdeSource()
	 */
	@Override
	public String getPatientIdeSource()
	{
		return patientIdeSource;
	}

	/**
	 * @param patientIdeSource
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setPatientIdeSource(java.lang.String)
	 */
	@Override
	public void setPatientIdeSource(String patientIdeSource)
	{
		this.patientIdeSource = patientIdeSource;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getEncounterIdeStatus()
	 */
	@Override
	public String getEncounterIdeStatus()
	{
		return encounterIdeStatus;
	}

	/**
	 * @param encounterIdeStatus
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setEncounterIdeStatus(java.lang.String)
	 */
	@Override
	public void setEncounterIdeStatus(String encounterIdeStatus)
	{
		this.encounterIdeStatus = encounterIdeStatus;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getUploadDate()
	 */
	@Override
	public Date getUploadDate()
	{
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setUploadDate(java.util.Date)
	 */
	@Override
	public void setUploadDate(Date uploadDate)
	{
		this.uploadDate = uploadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMapping#setUploadId(java.math.BigInteger)
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

		final EncounterMappingEntity that = (EncounterMappingEntity) object;
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
	public EncounterMapping copy()
	{
		final EncounterMappingEntity entity = new EncounterMappingEntity();
		entity.setDownloadDate(this.getDownloadDate());
		entity.setEncounterIdeStatus(this.getEncounterIdeStatus());
		entity.setEncounterNum(this.getEncounterNum());
		entity.setId(this.getId().copy());
		entity.setImportDate(this.getImportDate());
		entity.setPatientIde(this.getPatientIde());
		entity.setPatientIdeSource(this.getPatientIdeSource());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadDate(this.getUploadDate());
		entity.setUploadId(this.getUploadId());
		return entity;
	}

}
