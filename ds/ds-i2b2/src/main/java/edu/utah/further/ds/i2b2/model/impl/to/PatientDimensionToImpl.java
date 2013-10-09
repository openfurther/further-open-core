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

import static java.lang.Long.valueOf;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;
import edu.utah.further.ds.i2b2.model.api.to.ObservationFactTo;
import edu.utah.further.ds.i2b2.model.api.to.ObservationFactsTo;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionIdTo;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo;
import edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionsTo;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionsTo;
import edu.utah.further.ds.i2b2.model.impl.domain.PatientDimensionEntity;

/**
 * Patient Dimension transfer object.
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
@XmlRootElement(name = "PatientDimension")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientDimension", propOrder =
{ "patientDimensionPK", "vitalStatusCd", "birthDate", "deathDate", "sexCd",
		"ageInYearsNum", "languageCd", "raceCd", "maritalStatusCd", "religionCd",
		"zipCd", "statecityzipPath", "patientBlob", "updateDate", "downloadDate",
		"importDate", "sourcesystemCd", "uploadId", "visitDimensions",
		"providerDimensions", "observationFacts" })
public class PatientDimensionToImpl implements PatientDimensionTo
{
	// ========================= FIELDS ====================================

	@XmlElement(required = true)
	private PatientDimensionPKToImpl patientDimensionPK;
	private String vitalStatusCd;
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar birthDate;
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar deathDate;
	private String sexCd;
	private String ageInYearsNum;
	private String languageCd;
	private String raceCd;
	private String maritalStatusCd;
	private String religionCd;
	private String zipCd;
	private String statecityzipPath;
	private String patientBlob;
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar updateDate;
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar downloadDate;
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar importDate;
	private String sourcesystemCd;
	private String uploadId;
	@XmlElement(name = "VisitDimensions")
	private VisitDimensionsToImpl visitDimensions;
	@XmlElement(name = "ProviderDimensions")
	private ProviderDimensionsToImpl providerDimensions;
	@XmlElement(name = "ObservationFacts")
	private ObservationFactsToImpl observationFacts;

	// ========================= IMPL: PatientDimensionTo ==================

	/**
	 * Return the patientDimensionPK property.
	 * 
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getPatientDimensionPK()
	 */
	@Override
	public PatientDimensionIdTo getPatientDimensionPK()
	{
		return patientDimensionPK;
	}

	/**
	 * Set a new value for the patientDimensionPK property.
	 * 
	 * @param patientDimensionPK
	 *            the patientDimensionPK to set
	 */
	@Override
	public void setPatientDimensionPK(final PatientDimensionIdTo patientDimensionPK)
	{
		this.patientDimensionPK = (PatientDimensionPKToImpl) patientDimensionPK;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getVitalStatusCd()
	 */
	@Override
	public String getVitalStatusCd()
	{
		return vitalStatusCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setVitalStatusCd(java.lang.String)
	 */
	@Override
	public void setVitalStatusCd(final String value)
	{
		this.vitalStatusCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getBirthDate()
	 */
	@Override
	public XMLGregorianCalendar getBirthDate()
	{
		return birthDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setBirthDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setBirthDate(final XMLGregorianCalendar value)
	{
		this.birthDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getDeathDate()
	 */
	@Override
	public XMLGregorianCalendar getDeathDate()
	{
		return deathDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setDeathDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDeathDate(final XMLGregorianCalendar value)
	{
		this.deathDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getSexCd()
	 */
	@Override
	public String getSexCd()
	{
		return sexCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setSexCd(java.lang.String)
	 */
	@Override
	public void setSexCd(final String value)
	{
		this.sexCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getAgeInYearsNum()
	 */
	@Override
	public String getAgeInYearsNum()
	{
		return ageInYearsNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setAgeInYearsNum(java.lang.String)
	 */
	@Override
	public void setAgeInYearsNum(final String value)
	{
		this.ageInYearsNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getLanguageCd()
	 */
	@Override
	public String getLanguageCd()
	{
		return languageCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setLanguageCd(java.lang.String)
	 */
	@Override
	public void setLanguageCd(final String value)
	{
		this.languageCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getRaceCd()
	 */
	@Override
	public String getRaceCd()
	{
		return raceCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setRaceCd(java.lang.String)
	 */
	@Override
	public void setRaceCd(final String value)
	{
		this.raceCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getMaritalStatusCd()
	 */
	@Override
	public String getMaritalStatusCd()
	{
		return maritalStatusCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setMaritalStatusCd(java.lang.String)
	 */
	@Override
	public void setMaritalStatusCd(final String value)
	{
		this.maritalStatusCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getReligionCd()
	 */
	@Override
	public String getReligionCd()
	{
		return religionCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setReligionCd(java.lang.String)
	 */
	@Override
	public void setReligionCd(final String value)
	{
		this.religionCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getZipCd()
	 */
	@Override
	public String getZipCd()
	{
		return zipCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setZipCd(java.lang.String)
	 */
	@Override
	public void setZipCd(final String value)
	{
		this.zipCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getStatecityzipPath()
	 */
	@Override
	public String getStatecityzipPath()
	{
		return statecityzipPath;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setStatecityzipPath(java.lang.String)
	 */
	@Override
	public void setStatecityzipPath(final String value)
	{
		this.statecityzipPath = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getPatientBlob()
	 */
	@Override
	public String getPatientBlob()
	{
		return patientBlob;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setPatientBlob(java.lang.String)
	 */
	@Override
	public void setPatientBlob(final String value)
	{
		this.patientBlob = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getUpdateDate()
	 */
	@Override
	public XMLGregorianCalendar getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setUpdateDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setUpdateDate(final XMLGregorianCalendar value)
	{
		this.updateDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getDownloadDate()
	 */
	@Override
	public XMLGregorianCalendar getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setDownloadDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDownloadDate(final XMLGregorianCalendar value)
	{
		this.downloadDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getImportDate()
	 */
	@Override
	public XMLGregorianCalendar getImportDate()
	{
		return importDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setImportDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setImportDate(final XMLGregorianCalendar value)
	{
		this.importDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String value)
	{
		this.sourcesystemCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getUploadId()
	 */
	@Override
	public String getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setUploadId(java.lang.String)
	 */
	@Override
	public void setUploadId(final String value)
	{
		this.uploadId = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getVisitDimensions()
	 */
	@Override
	public VisitDimensionsTo getVisitDimensions()
	{
		return visitDimensions;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setVisitDimensions(edu.utah.further.ds.i2b2.model.impl.to.VisitDimensionsToImpl)
	 */
	@Override
	public void setVisitDimensions(final VisitDimensionsTo value)
	{
		this.visitDimensions = (VisitDimensionsToImpl) value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getProviderDimensions()
	 */
	@Override
	public ProviderDimensionsTo getProviderDimensions()
	{
		return providerDimensions;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setProviderDimensions(edu.utah.further.ds.i2b2.model.impl.to.ProviderDimensionsToImpl)
	 */
	@Override
	public void setProviderDimensions(final ProviderDimensionsTo value)
	{
		this.providerDimensions = (ProviderDimensionsToImpl) value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#getObservationFacts()
	 */
	@Override
	public ObservationFactsTo getObservationFacts()
	{
		return observationFacts;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo#setObservationFacts(edu.utah.further.ds.i2b2.model.api.to.ObservationFactsTo)
	 */
	@Override
	public void setObservationFacts(final ObservationFactsTo value)
	{
		this.observationFacts = (ObservationFactsToImpl) value;
	}

	// ========================= IMPL: Object ==============================

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

		final PatientDimensionTo that = (PatientDimensionTo) object;
		return new EqualsBuilder().append(this.getPatientDimensionPK(),
				that.getPatientDimensionPK()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getPatientDimensionPK()).toHashCode();
	}

	// ========================= IMPL: PubliclyCloneable<PatientDimension> =

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public PatientDimensionEntity copy()
	{
		final PatientDimensionEntity entity = new PatientDimensionEntity();
		entity.setAgeInYearsNum((this.getAgeInYearsNum() != null && this
				.getAgeInYearsNum()
				.length() > 0) ? valueOf(this.getAgeInYearsNum()) : null);
		entity.setBirthDate(this.getBirthDate() != null ? this
				.getBirthDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setDeathDate(this.getDeathDate() != null ? this
				.getDeathDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setDownloadDate(this.getDownloadDate() != null ? this
				.getDownloadDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setId(this.getPatientDimensionPK().getPatientNum());
		entity.setImportDate(this.getImportDate() != null ? this
				.getImportDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setLanguageCd(this.getLanguageCd());
		entity.setMaritalStatusCd(this.getMaritalStatusCd());
		entity.setPatientBlob(this.getPatientBlob());
		entity.setRaceCd(this.getRaceCd());
		entity.setReligionCd(this.getReligionCd());
		entity.setSexCd(this.getSexCd());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setStatecityzipPath(this.getStatecityzipPath());
		entity.setUpdateDate(this.getUpdateDate() != null ? this
				.getUpdateDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity
				.setUploadId((this.getUploadId() != null && this.getUploadId().length() > 0) ? valueOf(this
						.getUploadId()) : null);
		entity.setVitalStatusCd(this.getVitalStatusCd());
		entity.setZipCd(this.getZipCd());
		final Collection<ObservationFact> observationFactList = CollectionUtil.newList();
		if (this.getObservationFacts() != null)
		{
			for (final ObservationFactTo observationFact : this
					.getObservationFacts()
					.getObservationFact())
			{
				observationFactList.add(observationFact.copy());
			}
		}
		entity.setObservations(observationFactList);
		return entity;
	}
}
