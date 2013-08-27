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
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;

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
public interface ObservationFactTo extends PubliclyCloneable<ObservationFact>
{

	/**
	 * Gets the value of the observationFactPK property.
	 * 
	 * @return possible object is {@link ObservationFactPKToImpl }
	 * 
	 */
	ObservationFactIdTo getObservationFactPK();

	/**
	 * Sets the value of the observationFactPK property.
	 * 
	 * @param value
	 *            allowed object is {@link ObservationFactPKToImpl }
	 * 
	 */
	void setObservationFactPK(ObservationFactIdTo value);

	/**
	 * Gets the value of the patientNum property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getPatientNum();

	/**
	 * Sets the value of the patientNum property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setPatientNum(String value);

	/**
	 * Gets the value of the valtypeCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getValtypeCd();

	/**
	 * Sets the value of the valtypeCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setValtypeCd(String value);

	/**
	 * Gets the value of the tvalChar property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getTvalChar();

	/**
	 * Sets the value of the tvalChar property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setTvalChar(String value);

	/**
	 * Gets the value of the nvalNum property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getNvalNum();

	/**
	 * Sets the value of the nvalNum property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setNvalNum(String value);

	/**
	 * Gets the value of the valueflagCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getValueflagCd();

	/**
	 * Sets the value of the valueflagCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setValueflagCd(String value);

	/**
	 * Gets the value of the quantityNum property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getQuantityNum();

	/**
	 * Sets the value of the quantityNum property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setQuantityNum(String value);

	/**
	 * Gets the value of the unitsCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getUnitsCd();

	/**
	 * Sets the value of the unitsCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setUnitsCd(String value);

	/**
	 * Gets the value of the endDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	XMLGregorianCalendar getEndDate();

	/**
	 * Sets the value of the endDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	void setEndDate(XMLGregorianCalendar value);

	/**
	 * Gets the value of the locationCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getLocationCd();

	/**
	 * Sets the value of the locationCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setLocationCd(String value);

	/**
	 * Gets the value of the confidenceNum property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getConfidenceNum();

	/**
	 * Sets the value of the confidenceNum property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setConfidenceNum(String value);

	/**
	 * Gets the value of the observationBlob property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getObservationBlob();

	/**
	 * Sets the value of the observationBlob property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setObservationBlob(String value);

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

}