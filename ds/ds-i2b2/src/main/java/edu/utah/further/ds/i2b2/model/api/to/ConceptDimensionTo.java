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
import edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension;

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
public interface ConceptDimensionTo extends PubliclyCloneable<ConceptDimension>
{

	/**
	 * Gets the value of the conceptCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getConceptCd();

	/**
	 * Sets the value of the conceptCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setConceptCd(String value);

	/**
	 * Gets the value of the conceptPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getConceptPath();

	/**
	 * Sets the value of the conceptPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setConceptPath(String value);

	/**
	 * Gets the value of the nameChar property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getNameChar();

	/**
	 * Sets the value of the nameChar property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setNameChar(String value);

	/**
	 * Gets the value of the conceptBlob property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getConceptBlob();

	/**
	 * Sets the value of the conceptBlob property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setConceptBlob(String value);

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