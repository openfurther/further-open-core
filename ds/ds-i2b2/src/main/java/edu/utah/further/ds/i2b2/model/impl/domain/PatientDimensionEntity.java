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

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;
import edu.utah.further.ds.i2b2.model.api.domain.PatientDimension;

/**
 * Patient Dimension Entity
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
@Table(name = "PATIENT_DIMENSION")
public class PatientDimensionEntity implements PatientDimension, PersistentEntity<Long>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 740100948761774893L;

	@Id
	@Column(name = "PATIENT_IDE")
	private Long patientNum;

	@Column(name = "VITAL_STATUS_CD")
	private String vitalStatusCd;
	@Column(name = "BIRTH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date birthDate;
	@Column(name = "DEATH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deathDate;
	@Column(name = "SEX_CD")
	private String sexCd;
	@Column(name = "AGE_IN_YEARS_NUM")
	private Long ageInYearsNum;
	@Column(name = "LANGUAGE_CD")
	private String languageCd;
	@Column(name = "RACE_CD")
	private String raceCd;
	@Column(name = "MARITAL_STATUS_CD")
	private String maritalStatusCd;
	@Column(name = "RELIGION_CD")
	private String religionCd;
	@Column(name = "ZIP_CD")
	private String zipCd;
	@Column(name = "STATECITYZIP_PATH")
	private String statecityzipPath;
	@Lob
	@Column(name = "PATIENT_BLOB")
	private String patientBlob;
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
	private Long uploadId;
    @OneToMany(cascade = CascadeType.ALL, targetEntity=ObservationFactEntity.class, fetch = FetchType.EAGER)
 	@JoinColumn(name="PATIENT_NUM")
    private Collection<? extends ObservationFact> observations;

	/**
	 * Return the id property.
	 *
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return patientNum;
	}

	/**
	 * Set a new value for the id property.
	 *
	 * @param patientNum
	 *            the id to set
	 */
	@Override
	public void setId(final Long patientNum)
	{
		this.patientNum = patientNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getVitalStatusCd()
	 */
	@Override
	public String getVitalStatusCd()
	{
		return vitalStatusCd;
	}

	/**
	 * @param vitalStatusCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setVitalStatusCd(java.lang.String)
	 */
	@Override
	public void setVitalStatusCd(final String vitalStatusCd)
	{
		this.vitalStatusCd = vitalStatusCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getBirthDate()
	 */
	@Override
	public Date getBirthDate()
	{
		return birthDate;
	}

	/**
	 * @param birthDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setBirthDate(java.util.Date)
	 */
	@Override
	public void setBirthDate(final Date birthDate)
	{
		this.birthDate = birthDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getDeathDate()
	 */
	@Override
	public Date getDeathDate()
	{
		return deathDate;
	}

	/**
	 * @param deathDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setDeathDate(java.util.Date)
	 */
	@Override
	public void setDeathDate(final Date deathDate)
	{
		this.deathDate = deathDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getSexCd()
	 */
	@Override
	public String getSexCd()
	{
		return sexCd;
	}

	/**
	 * @param sexCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setSexCd(java.lang.String)
	 */
	@Override
	public void setSexCd(final String sexCd)
	{
		this.sexCd = sexCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getAgeInYearsNum()
	 */
	@Override
	public Long getAgeInYearsNum()
	{
		return ageInYearsNum;
	}

	/**
	 * @param ageInYearsNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setAgeInYearsNum(java.math.BigInteger)
	 */
	@Override
	public void setAgeInYearsNum(final Long ageInYearsNum)
	{
		this.ageInYearsNum = ageInYearsNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getLanguageCd()
	 */
	@Override
	public String getLanguageCd()
	{
		return languageCd;
	}

	/**
	 * @param languageCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setLanguageCd(java.lang.String)
	 */
	@Override
	public void setLanguageCd(final String languageCd)
	{
		this.languageCd = languageCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getRaceCd()
	 */
	@Override
	public String getRaceCd()
	{
		return raceCd;
	}

	/**
	 * @param raceCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setRaceCd(java.lang.String)
	 */
	@Override
	public void setRaceCd(final String raceCd)
	{
		this.raceCd = raceCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getMaritalStatusCd()
	 */
	@Override
	public String getMaritalStatusCd()
	{
		return maritalStatusCd;
	}

	/**
	 * @param maritalStatusCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setMaritalStatusCd(java.lang.String)
	 */
	@Override
	public void setMaritalStatusCd(final String maritalStatusCd)
	{
		this.maritalStatusCd = maritalStatusCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getReligionCd()
	 */
	@Override
	public String getReligionCd()
	{
		return religionCd;
	}

	/**
	 * @param religionCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setReligionCd(java.lang.String)
	 */
	@Override
	public void setReligionCd(final String religionCd)
	{
		this.religionCd = religionCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getZipCd()
	 */
	@Override
	public String getZipCd()
	{
		return zipCd;
	}

	/**
	 * @param zipCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setZipCd(java.lang.String)
	 */
	@Override
	public void setZipCd(final String zipCd)
	{
		this.zipCd = zipCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getStatecityzipPath()
	 */
	@Override
	public String getStatecityzipPath()
	{
		return statecityzipPath;
	}

	/**
	 * @param statecityzipPath
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setStatecityzipPath(java.lang.String)
	 */
	@Override
	public void setStatecityzipPath(final String statecityzipPath)
	{
		this.statecityzipPath = statecityzipPath;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getPatientBlob()
	 */
	@Override
	public String getPatientBlob()
	{
		return patientBlob;
	}

	/**
	 * @param patientBlob
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setPatientBlob(java.lang.String)
	 */
	@Override
	public void setPatientBlob(final String patientBlob)
	{
		this.patientBlob = patientBlob;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(final Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(final Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(final Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#getUploadId()
	 */
	@Override
	public Long getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientDimension#setUploadId(java.math.BigInteger)
	 */
	@Override
	public void setUploadId(final Long uploadId)
	{
		this.uploadId = uploadId;
	}
	
	@Override
	public Collection<ObservationFact> getObservations()
	{
		return CollectionUtil.<ObservationFact> newList(this.observations);
	}
	
	@Override
	public void setObservations (final Collection<? extends ObservationFact> observations)
	{
		this.observations = observations;
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

		final PatientDimensionEntity that = (PatientDimensionEntity) object;
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
	public PatientDimension copy()
	{
		final PatientDimensionEntity entity = new PatientDimensionEntity();
		entity.setAgeInYearsNum(this.getAgeInYearsNum());
		entity.setBirthDate(this.getBirthDate());
		entity.setDeathDate(this.getDeathDate());
		entity.setDownloadDate(this.getDownloadDate());
		entity.setId(this.getId());
		entity.setImportDate(this.getImportDate());
		entity.setLanguageCd(this.getLanguageCd());
		entity.setMaritalStatusCd(this.getMaritalStatusCd());
		entity.setPatientBlob(this.getPatientBlob());
		entity.setRaceCd(this.getRaceCd());
		entity.setReligionCd(this.getReligionCd());
		entity.setSexCd(this.getSexCd());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setStatecityzipPath(this.getStatecityzipPath());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setUploadId(this.getUploadId());
		entity.setVitalStatusCd(this.getVitalStatusCd());
		entity.setZipCd(this.getZipCd());
		entity.setObservations(this.getObservations());
		return entity;
	}
}
