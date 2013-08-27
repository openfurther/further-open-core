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
package edu.utah.further.core.xml.xquery;

import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Centralizes XQuery XML translation utilities.
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
 * @version May 4, 2010
 */
@Api
public interface XQueryService extends XQueryPropertiesContainer
{
	// ========================= METHODS ===================================

	/**
	 * Execute an XQuery program and return the transformed XML document as a string. Use
	 * for small output XML.
	 * 
	 * @param xQuery
	 *            XQuery program input stream
	 * @param inputXml
	 *            input XML document input stream. Closed upon returning from this method.
	 * @param parameters
	 *            contains externally-binded parameter names, values and types
	 * @throws ApplicationException
	 *             upon XQuery execution failure, or if an I/O exception is encountered
	 *             opening or closing the input streams or output stream
	 */
	String executeIntoString(InputStream xQuery, InputStream inputXml,
			Map<String, String> parameters);
	
	/**
	 * Execute an XQuery program and return the transformed XML document as a string. Use
	 * for small output XML.
	 * 
	 * @param xQuery
	 *            XQuery program input stream
	 * @param inputXml
	 *            input XML document input stream. Closed upon returning from this method.
	 * @throws ApplicationException
	 *             upon XQuery execution failure, or if an I/O exception is encountered
	 *             opening or closing the input streams or output stream
	 */
	String executeIntoString(InputStream xQuery, InputStream inputXml);

	/**
	 * Execute an XQuery program and process with a general processor.
	 * 
	 * @param xQuery
	 *            XQuery program input stream
	 * @param inputXml
	 *            input XML document input stream. Closed upon returning from this method.
	 * @param parameters
	 *            contains externally-binded parameter names, values and types
	 * @throws ApplicationException
	 *             upon XQuery execution failure, or if an I/O exception is encountered
	 *             opening or closing the input streams or output stream
	 */
	<T> T executeAndProcess(InputStream xQuery, InputStream inputXml,
			XQueryResultProcessor<T> resultProcessor, Map<String, String> parameters);

	/**
	 * Execute an XQuery program and stream the transformed XML document. Use for large
	 * output XML.
	 * 
	 * @param xQuery
	 *            XQuery program input stream
	 * @param inputXml
	 *            input XML document input stream. Closed upon returning from this method.
	 * @param parameters
	 *            contains externally-binded parameter names, values and types
	 * @throws ApplicationException
	 *             upon XQuery execution failure, or if an I/O exception is encountered
	 *             opening or closing the input streams or output stream
	 */
	XMLStreamReader executeIntoStream(InputStream xQuery, InputStream inputXml,
			Map<String, String> parameters);
	
	/**
	 * Execute an XQuery program and stream the transformed XML document. Use for large
	 * output XML.
	 * 
	 * @param xQuery
	 *            XQuery program input stream
	 * @param inputXml
	 *            input XML document input stream. Closed upon returning from this method.
	 * @throws ApplicationException
	 *             upon XQuery execution failure, or if an I/O exception is encountered
	 *             opening or closing the input streams or output stream
	 */
	XMLStreamReader executeIntoStream(InputStream xQuery, InputStream inputXml);
}