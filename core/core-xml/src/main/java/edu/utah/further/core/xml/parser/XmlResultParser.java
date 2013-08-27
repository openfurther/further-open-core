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
package edu.utah.further.core.xml.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.utah.further.core.api.context.Api;

/**
 * A parser of an XML input source. Implemented by web service clients.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 19, 2008
 * @see http
 *      ://svn.nuxeo.org/trac/nuxeo/browser/sandbox/nuxeo-portlet-search/trunk/src/main
 *      /java/org/nuxeo/portlet/search/parser/XmlResultParser.java?rev=24272
 */
@Api
public interface XmlResultParser
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Parse an XML source.
	 *
	 * @param xmlSource
	 *            XML input source
	 * @return parser target object
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	<T> T parseXmlResults(InputStream xmlSource) throws IOException, JAXBException;

	/**
	 * Parse an XML source.
	 *
	 * @param xmlSource
	 *            path (URI) to XML input resource
	 * @return parser target object
	 */
	<T> T parseXmlResults(String xmlSource) throws IOException, JAXBException;
}