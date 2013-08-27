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
import edu.utah.further.ds.i2b2.model.api.domain.VisitDimension;

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
public interface VisitDimensionTo extends PubliclyCloneable<VisitDimension>
{

	/**
	 * Gets the value of the visitDimensionPK property.
	 * 
	 * @return possible object is {@link VisitDimensionPKToImpl }
	 * 
	 */
	VisitDimensionIdTo getVisitDimensionPK();

	/**
	 * Sets the value of the visitDimensionPK property.
	 * 
	 * @param value
	 *            allowed object is {@link VisitDimensionPKToImpl }
	 * 
	 */
	void setVisitDimensionPK(VisitDimensionIdTo value);

	/**
	 * Gets the value of the activeStatusCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getActiveStatusCd();

	/**
	 * Sets the value of the activeStatusCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setActiveStatusCd(String value);

	/**
	 * Gets the value of the startDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	XMLGregorianCalendar getStartDate();

	/**
	 * Sets the value of the startDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	void setStartDate(XMLGregorianCalendar value);

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
	 * Gets the value of the inoutCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getInoutCd();

	/**
	 * Sets the value of the inoutCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setInoutCd(String value);

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
	 * Gets the value of the locationPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getLocationPath();

	/**
	 * Sets the value of the locationPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setLocationPath(String value);

	/**
	 * Gets the value of the visitBlob property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getVisitBlob();

	/**
	 * Sets the value of the visitBlob property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setVisitBlob(String value);

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