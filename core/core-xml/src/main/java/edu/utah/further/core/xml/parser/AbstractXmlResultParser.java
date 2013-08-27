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

import static edu.utah.further.core.api.constant.Strings.DEFAULT_ENCODING;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlUtil;

/**
 * A convenient base class for XML result parsers.
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
 *      /java/org/nuxeo/portlet/search/parser/AbstractXmlResultParser.java?rev=24272
 */
@Api
public abstract class AbstractXmlResultParser implements XmlResultParser
{
	// ========================= IMPLEMENTATION: XmlResultParser ===========

	/**
	 * @param <T>
	 * @param xmlSource
	 * @return
	 * @throws Exception
	 * @see edu.utah.further.core.xml.parser.XmlResultParser#parseXmlResults(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public <T> T parseXmlResults(final String xmlSource) throws IOException,
			JAXBException
	{
		return isBlank(xmlSource) ? null : (T) parseXmlResults(new ByteArrayInputStream(
				xmlSource.getBytes(DEFAULT_ENCODING)));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parse an XML input source using JAXP.
	 *
	 * @param xmlSource
	 *            XML input source
	 * @return DOM document node
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	protected Document parseDocument(final InputStream xmlSource)
			throws ParserConfigurationException, IOException, SAXException
	{
		final DocumentBuilderFactory factory = XmlUtil.getDocumentBuilderFactory();
		factory.setNamespaceAware(true); // never forget this!
		final DocumentBuilder builder = factory.newDocumentBuilder();

		// Force re-encoding of the input source and parse
		return builder.parse(StringUtil.toUtf8(xmlSource));
	}
}
