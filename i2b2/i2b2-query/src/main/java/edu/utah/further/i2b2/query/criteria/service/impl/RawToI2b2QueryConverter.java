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
package edu.utah.further.i2b2.query.criteria.service.impl;

import java.io.IOException;

import edu.utah.further.i2b2.query.model.I2b2Query;

/**
 * Converts raw i2b2 message XMLs to {@link I2b2Query} objects.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 12, 2010
 */
public interface RawToI2b2QueryConverter
{
	// ========================= FIELDS ====================================

	/**
	 * Convert a raw i2b2 request XML to an XML format acceptable to FURTHeR.
	 *
	 * @param rawI2b2Xml
	 *            i2b2 request XML document
	 * @return the converted XML format acceptable to FURTHeR as a string. If this is not
	 *         a recognized response, returns <code>null</code>
	 * @throws IOException
	 */
	String toI2b2Query(String i2b2MessageXml);
}