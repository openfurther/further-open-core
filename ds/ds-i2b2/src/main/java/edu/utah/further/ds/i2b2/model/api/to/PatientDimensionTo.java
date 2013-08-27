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
package edu.utah.further.ds.i2b2.model.api.to;

import javax.xml.datatype.XMLGregorianCalendar;

import edu.utah.further.core.api.lang.PubliclyCloneable;
import edu.utah.further.ds.i2b2.model.api.domain.PatientDimension;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 19, 2010
 */
public interface PatientDimensionTo extends PubliclyCloneable<PatientDimension>
{
	// ========================= METHODS ===================================

	/**
	 * Return the patientDimensionPK property.
	 *
	 * @return the patientDimensionPK
	 */
	PatientDimensionIdTo getPatientDimensionPK();

	/**
	 * Set a new value for the patientDimensionPK property.
	 *
	 * @param patientDimensionPK
	 *            the patientDimensionPK to set
	 */
	void setPatientDimensionPK(PatientDimensionIdTo value);

	/**
	 * Gets the value of the vitalStatusCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getVitalStatusCd();

	/**
	 * Sets the value of the vitalStatusCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setVitalStatusCd(String value);

	/**
	 * Gets the value of the birthDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	XMLGregorianCalendar getBirthDate();

	/**
	 * Sets the value of the birthDate property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	void setBirthDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the deathDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	XMLGregorianCalendar getDeathDate();

	/**
	 * Sets the value of the deathDate property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	void setDeathDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the sexCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getSexCd();

	/**
	 * Sets the value of the sexCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setSexCd(String value);

	/**
	 * Gets the value of the ageInYearsNum property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getAgeInYearsNum();

	/**
	 * Sets the value of the ageInYearsNum property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setAgeInYearsNum(String value);

	/**
	 * Gets the value of the languageCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getLanguageCd();

	/**
	 * Sets the value of the languageCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setLanguageCd(String value);

	/**
	 * Gets the value of the raceCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getRaceCd();

	/**
	 * Sets the value of the raceCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setRaceCd(String value);

	/**
	 * Gets the value of the maritalStatusCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getMaritalStatusCd();

	/**
	 * Sets the value of the maritalStatusCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setMaritalStatusCd(String value);

	/**
	 * Gets the value of the religionCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getReligionCd();

	/**
	 * Sets the value of the religionCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setReligionCd(String value);

	/**
	 * Gets the value of the zipCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getZipCd();

	/**
	 * Sets the value of the zipCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setZipCd(String value);

	/**
	 * Gets the value of the statecityzipPath property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getStatecityzipPath();

	/**
	 * Sets the value of the statecityzipPath property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setStatecityzipPath(String value);

	/**
	 * Gets the value of the patientBlob property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getPatientBlob();

	/**
	 * Sets the value of the patientBlob property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setPatientBlob(String value);

	/**
	 * Gets the value of the updateDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	XMLGregorianCalendar getUpdateDate();

	/**
	 * Sets the value of the updateDate property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	void setUpdateDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the downloadDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	XMLGregorianCalendar getDownloadDate();

	/**
	 * Sets the value of the downloadDate property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	void setDownloadDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the importDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	XMLGregorianCalendar getImportDate();

	/**
	 * Sets the value of the importDate property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	void setImportDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the sourcesystemCd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getSourcesystemCd();

	/**
	 * Sets the value of the sourcesystemCd property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setSourcesystemCd(String value);

	/**
	 * Gets the value of the uploadId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	String getUploadId();

	/**
	 * Sets the value of the uploadId property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	void setUploadId(String value);

	/**
	 * Gets the value of the visitDimensions property.
	 *
	 * @return possible object is {@link VisitDimensionsToImpl }
	 *
	 */
	VisitDimensionsTo getVisitDimensions();

	/**
	 * Sets the value of the visitDimensions property.
	 *
	 * @param value
	 *            allowed object is {@link VisitDimensionsToImpl }
	 *
	 */
	void setVisitDimensions(VisitDimensionsTo value);

	/**
	 * Gets the value of the providerDimensions property.
	 *
	 * @return possible object is {@link ProviderDimensionsToImpl }
	 *
	 */
	ProviderDimensionsTo getProviderDimensions();

	/**
	 * Sets the value of the providerDimensions property.
	 *
	 * @param value
	 *            allowed object is {@link ProviderDimensionsToImpl }
	 *
	 */
	void setProviderDimensions(ProviderDimensionsTo value);

	/**
	 * Gets the value of the observationFacts property.
	 *
	 * @return possible object is {@link ObservationFactsToImpl }
	 *
	 */
	ObservationFactsTo getObservationFacts();

	/**
	 * Sets the value of the observationFacts property.
	 *
	 * @param value
	 *            allowed object is {@link ObservationFactsToImpl }
	 *
	 */
	void setObservationFacts(ObservationFactsTo value);
}