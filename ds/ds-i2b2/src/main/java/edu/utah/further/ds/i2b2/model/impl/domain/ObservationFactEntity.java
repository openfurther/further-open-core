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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
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
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId;

/**
 * Observation Fact Entity
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
@Table(name = "OBSERVATION_FACT")
public class ObservationFactEntity implements PersistentEntity<ObservationFactId>,
		ObservationFact
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4218985975777184819L;

	@EmbeddedId
	protected ObservationFactPK id;

	@Basic(optional = true)
	@Column(name = "PATIENT_NUM")
	private BigInteger patientNum;
	@Column(name = "VALTYPE_CD")
	private String valtypeCd;
	@Column(name = "TVAL_CHAR")
	private String tvalChar;
	@Column(name = "NVAL_NUM")
	private BigDecimal nvalNum;
	@Column(name = "VALUEFLAG_CD")
	private String valueflagCd;
	@Column(name = "QUANTITY_NUM")
	private BigDecimal quantityNum;
	@Column(name = "UNITS_CD")
	private String unitsCd;
	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@Column(name = "LOCATION_CD")
	private String locationCd;
	@Column(name = "CONFIDENCE_NUM")
	private BigDecimal confidenceNum;
	@Lob
	@Column(name = "OBSERVATION_BLOB")
	private String observationBlob;
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

	public ObservationFactEntity()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getId()
	 */
	@Override
	public ObservationFactId getId()
	{
		return id;
	}

	/**
	 * @param observationFactPK
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setId(edu.utah.further.i2b2.model.impl.domain.ObservationFactPK)
	 */
	@Override
	public void setId(ObservationFactId observationFactPK)
	{
		this.id = (ObservationFactPK) observationFactPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getPatientNum()
	 */
	@Override
	public BigInteger getPatientNum()
	{
		return patientNum;
	}

	/**
	 * @param patientNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setPatientNum(java.math.BigInteger)
	 */
	@Override
	public void setPatientNum(BigInteger patientNum)
	{
		this.patientNum = patientNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getValtypeCd()
	 */
	@Override
	public String getValtypeCd()
	{
		return valtypeCd;
	}

	/**
	 * @param valtypeCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setValtypeCd(java.lang.String)
	 */
	@Override
	public void setValtypeCd(String valtypeCd)
	{
		this.valtypeCd = valtypeCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getTvalChar()
	 */
	@Override
	public String getTvalChar()
	{
		return tvalChar;
	}

	/**
	 * @param tvalChar
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setTvalChar(java.lang.String)
	 */
	@Override
	public void setTvalChar(String tvalChar)
	{
		this.tvalChar = tvalChar;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getNvalNum()
	 */
	@Override
	public BigDecimal getNvalNum()
	{
		return nvalNum;
	}

	/**
	 * @param nvalNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setNvalNum(java.math.BigDecimal)
	 */
	@Override
	public void setNvalNum(BigDecimal nvalNum)
	{
		this.nvalNum = nvalNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getValueflagCd()
	 */
	@Override
	public String getValueflagCd()
	{
		return valueflagCd;
	}

	/**
	 * @param valueflagCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setValueflagCd(java.lang.String)
	 */
	@Override
	public void setValueflagCd(String valueflagCd)
	{
		this.valueflagCd = valueflagCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getQuantityNum()
	 */
	@Override
	public BigDecimal getQuantityNum()
	{
		return quantityNum;
	}

	/**
	 * @param quantityNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setQuantityNum(java.math.BigDecimal)
	 */
	@Override
	public void setQuantityNum(BigDecimal quantityNum)
	{
		this.quantityNum = quantityNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getUnitsCd()
	 */
	@Override
	public String getUnitsCd()
	{
		return unitsCd;
	}

	/**
	 * @param unitsCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setUnitsCd(java.lang.String)
	 */
	@Override
	public void setUnitsCd(String unitsCd)
	{
		this.unitsCd = unitsCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getEndDate()
	 */
	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setEndDate(java.util.Date)
	 */
	@Override
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getLocationCd()
	 */
	@Override
	public String getLocationCd()
	{
		return locationCd;
	}

	/**
	 * @param locationCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setLocationCd(java.lang.String)
	 */
	@Override
	public void setLocationCd(String locationCd)
	{
		this.locationCd = locationCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getConfidenceNum()
	 */
	@Override
	public BigDecimal getConfidenceNum()
	{
		return confidenceNum;
	}

	/**
	 * @param confidenceNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setConfidenceNum(java.math.BigDecimal)
	 */
	@Override
	public void setConfidenceNum(BigDecimal confidenceNum)
	{
		this.confidenceNum = confidenceNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getObservationBlob()
	 */
	@Override
	public String getObservationBlob()
	{
		return observationBlob;
	}

	/**
	 * @param observationBlob
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setObservationBlob(java.lang.String)
	 */
	@Override
	public void setObservationBlob(String observationBlob)
	{
		this.observationBlob = observationBlob;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFact#setUploadId(java.math.BigInteger)
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

		final ObservationFactEntity that = (ObservationFactEntity) object;
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
	public ObservationFact copy()
	{
		final ObservationFactEntity entity = new ObservationFactEntity();
		entity.setConfidenceNum(this.getConfidenceNum());
		entity.setDownloadDate(this.getDownloadDate());
		entity.setEndDate(this.getEndDate());
		entity.setId(this.getId().copy());
		entity.setImportDate(this.getImportDate());
		entity.setLocationCd(this.getLocationCd());
		entity.setNvalNum(this.getNvalNum());
		entity.setObservationBlob(this.getObservationBlob());
		entity.setPatientNum(this.getPatientNum());
		entity.setQuantityNum(this.quantityNum);
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setTvalChar(this.getTvalChar());
		entity.setUnitsCd(this.getUnitsCd());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadId(this.getUploadId());
		entity.setValtypeCd(this.getValtypeCd());
		entity.setValueflagCd(this.getValueflagCd());
		return entity;
	}
}
