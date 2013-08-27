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
package edu.utah.further.ds.i2b2.model.impl.to;

import static edu.utah.further.core.api.math.ArithmeticUtil.newBigDecimalNullSafe;
import static edu.utah.further.core.api.math.ArithmeticUtil.newBigIntegerNullSafe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;
import edu.utah.further.ds.i2b2.model.api.to.ObservationFactIdTo;
import edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo;
import edu.utah.further.ds.i2b2.model.impl.domain.ObservationFactEntity;

/**
 * Observation Fact To
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author {@code Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
@XmlRootElement(name = "ObservationFact")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObservationFact", propOrder =
{ "observationFactPK", "patientNum", "valtypeCd", "tvalChar", "nvalNum", "valueflagCd",
		"quantityNum", "unitsCd", "endDate", "locationCd", "confidenceNum",
		"observationBlob", "updateDate", "downloadDate", "importDate", "sourcesystemCd",
		"uploadId" })
public class ObservationFactToImpl implements ObservationFactTo
{

	@XmlElement(required = true)
	private ObservationFactPKToImpl observationFactPK;
	@XmlElement(required = true)
	private String patientNum;
	@XmlElement(required = true)
	private String valtypeCd;
	@XmlElement(required = true)
	private String tvalChar;
	@XmlElement(required = true)
	private String nvalNum;
	@XmlElement(required = true)
	private String valueflagCd;
	@XmlElement(required = true)
	private String quantityNum;
	@XmlElement(required = true)
	private String unitsCd;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar endDate;
	@XmlElement(required = true)
	private String locationCd;
	@XmlElement(required = true)
	private String confidenceNum;
	@XmlElement(required = true)
	private String observationBlob;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar updateDate;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar downloadDate;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar importDate;
	@XmlElement(required = true)
	private String sourcesystemCd;
	@XmlElement(required = true)
	private String uploadId;

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getObservationFactPK()
	 */
	@Override
	public ObservationFactIdTo getObservationFactPK()
	{
		return observationFactPK;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setObservationFactPK(edu.utah.further.ds.i2b2.model.api.to.ObservationFactIdTo)
	 */
	@Override
	public void setObservationFactPK(final ObservationFactIdTo value)
	{
		this.observationFactPK = (ObservationFactPKToImpl) value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getPatientNum()
	 */
	@Override
	public String getPatientNum()
	{
		return patientNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setPatientNum(java.lang.String)
	 */
	@Override
	public void setPatientNum(final String value)
	{
		this.patientNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getValtypeCd()
	 */
	@Override
	public String getValtypeCd()
	{
		return valtypeCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setValtypeCd(java.lang.String)
	 */
	@Override
	public void setValtypeCd(final String value)
	{
		this.valtypeCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getTvalChar()
	 */
	@Override
	public String getTvalChar()
	{
		return tvalChar;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setTvalChar(java.lang.String)
	 */
	@Override
	public void setTvalChar(final String value)
	{
		this.tvalChar = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getNvalNum()
	 */
	@Override
	public String getNvalNum()
	{
		return nvalNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setNvalNum(java.lang.String)
	 */
	@Override
	public void setNvalNum(final String value)
	{
		this.nvalNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getValueflagCd()
	 */
	@Override
	public String getValueflagCd()
	{
		return valueflagCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setValueflagCd(java.lang.String)
	 */
	@Override
	public void setValueflagCd(final String value)
	{
		this.valueflagCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getQuantityNum()
	 */
	@Override
	public String getQuantityNum()
	{
		return quantityNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setQuantityNum(java.lang.String)
	 */
	@Override
	public void setQuantityNum(final String value)
	{
		this.quantityNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getUnitsCd()
	 */
	@Override
	public String getUnitsCd()
	{
		return unitsCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setUnitsCd(java.lang.String)
	 */
	@Override
	public void setUnitsCd(final String value)
	{
		this.unitsCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getEndDate()
	 */
	@Override
	public XMLGregorianCalendar getEndDate()
	{
		return endDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setEndDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setEndDate(final XMLGregorianCalendar value)
	{
		this.endDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getLocationCd()
	 */
	@Override
	public String getLocationCd()
	{
		return locationCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setLocationCd(java.lang.String)
	 */
	@Override
	public void setLocationCd(final String value)
	{
		this.locationCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getConfidenceNum()
	 */
	@Override
	public String getConfidenceNum()
	{
		return confidenceNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setConfidenceNum(java.lang.String)
	 */
	@Override
	public void setConfidenceNum(final String value)
	{
		this.confidenceNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getObservationBlob()
	 */
	@Override
	public String getObservationBlob()
	{
		return observationBlob;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setObservationBlob(java.lang.String)
	 */
	@Override
	public void setObservationBlob(final String value)
	{
		this.observationBlob = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getUpdateDate()
	 */
	@Override
	public XMLGregorianCalendar getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setUpdateDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setUpdateDate(final XMLGregorianCalendar value)
	{
		this.updateDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getDownloadDate()
	 */
	@Override
	public XMLGregorianCalendar getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setDownloadDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDownloadDate(final XMLGregorianCalendar value)
	{
		this.downloadDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getImportDate()
	 */
	@Override
	public XMLGregorianCalendar getImportDate()
	{
		return importDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setImportDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setImportDate(final XMLGregorianCalendar value)
	{
		this.importDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String value)
	{
		this.sourcesystemCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#getUploadId()
	 */
	@Override
	public String getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo#setUploadId(java.lang.String)
	 */
	@Override
	public void setUploadId(final String value)
	{
		this.uploadId = value;
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final ObservationFactTo that = (ObservationFactTo) object;
		return new EqualsBuilder().append(this.getObservationFactPK(),
				that.getObservationFactPK()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getObservationFactPK()).toHashCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ObservationFact copy()
	{
		final ObservationFactEntity entity = new ObservationFactEntity();
		entity.setConfidenceNum(newBigDecimalNullSafe(this.getConfidenceNum()));
		entity.setDownloadDate(this.getDownloadDate() != null ? this
				.getDownloadDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setEndDate(this.getEndDate() != null ? this
				.getEndDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setId(this.getObservationFactPK() != null ? this
				.getObservationFactPK()
				.copy() : null);
		entity.setImportDate(this.getImportDate() != null ? this
				.getImportDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setLocationCd(this.getLocationCd());
		entity.setNvalNum(newBigDecimalNullSafe(this.getNvalNum()));
		entity.setObservationBlob(this.getObservationBlob());
		entity.setPatientNum(newBigIntegerNullSafe(this.getPatientNum()));
		entity.setQuantityNum(newBigDecimalNullSafe(this.getQuantityNum()));
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setTvalChar(this.getTvalChar());
		entity.setUnitsCd(this.getUnitsCd());
		entity.setUpdateDate(this.getUpdateDate().toGregorianCalendar().getTime());
		entity.setUploadId(newBigIntegerNullSafe(this.getUploadId()));
		entity.setValtypeCd(this.getValtypeCd());
		entity.setValueflagCd(this.getValueflagCd());
		return entity;
	}
}
