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
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId;

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
public interface ObservationFactIdTo extends PubliclyCloneable<ObservationFactId>
{

	/**
	 * Gets the value of the encounterNum property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getEncounterNum();

	/**
	 * Sets the value of the encounterNum property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setEncounterNum(String value);

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
	 * Gets the value of the providerId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getProviderId();

	/**
	 * Sets the value of the providerId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setProviderId(String value);

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
	 * Gets the value of the modifierCd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	String getModifierCd();

	/**
	 * Sets the value of the modifierCd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	void setModifierCd(String value);

}