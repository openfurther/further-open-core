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

import static edu.utah.further.core.api.xml.XmlUtil.closeTag;
import static edu.utah.further.core.api.xml.XmlUtil.openTag;
import static edu.utah.further.core.xml.xpath.XPathUtil.getNodeSubTreeAsString;
import static edu.utah.further.i2b2.query.criteria.service.impl.RequestElementNames.NS1;
import static edu.utah.further.i2b2.query.criteria.service.impl.RequestElementNames.QUERY_DEFINITION;
import static edu.utah.further.i2b2.query.criteria.service.impl.RequestElementNames.REQUEST_XML_NAMESPACE;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.xml.xpath.XPathNamespaceContext;

/**
 * Pre-processes the i2b2 XML request to a friendlier format to be sent to FURTHeR.
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
 * @version Dec 27, 2008
 */
@Service("rawToI2b2QueryConverter")
public class RawToI2b2QueryConverterRegexImpl implements RawToI2b2QueryConverter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RawToI2b2QueryConverterRegexImpl.class);

	/**
	 * XPath context to use in all XML manipulations.
	 */
	private static final NamespaceContext NS_CONTEXT = createNamespaceContext();

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	// ========================= HOOKS =====================================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.i2b2.query.criteria.service.impl.RawToI2b2QueryConverter#toI2b2Query
	 * (java.lang.String)
	 */
	@Override
	public String toI2b2Query(final String rawI2b2Xml)
	{
		final ByteArrayInputStream inputXmlBytes = new ByteArrayInputStream(
				rawI2b2Xml.getBytes());
		String queryXml = getNodeSubTreeAsString(inputXmlBytes, "//" + QUERY_DEFINITION,
				NS_CONTEXT);
		if (queryXml == null)
		{
			return null;
		}

		// Convert root element to be namespace-qualified
		final String qualifiedRootElement = openTag(NS1, REQUEST_XML_NAMESPACE,
				QUERY_DEFINITION).toString();
		queryXml = queryXml.replaceFirst(openTag(QUERY_DEFINITION).toString(),
				qualifiedRootElement);
		queryXml = queryXml.replaceFirst(closeTag(QUERY_DEFINITION).toString(),
				closeTag(NS1, QUERY_DEFINITION).toString());

		// Replace single quotes by double quotes, which is the standard used for the FQE
		// and works with Javascript escaping in portal JSF pages
		final String userXml = getNodeSubTreeAsString(
				new ByteArrayInputStream(rawI2b2Xml.getBytes()),
				"//" + RequestElementNames.USER, NS_CONTEXT).replace(
				Strings.SINGLE_QUOTE, Strings.DOUBLE_QUOTE);
		queryXml = queryXml.replaceFirst(qualifiedRootElement, qualifiedRootElement
				+ "\n" + userXml);
		if (log.isDebugEnabled())
		{
			log.debug("FURTHeR request:\n" + queryXml);
		}
		return queryXml;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param inputStream
	 * @param xpathExpression
	 * @return
	 */
	private static XPathNamespaceContext createNamespaceContext()
	{
		final XPathNamespaceContext nsContext = new XPathNamespaceContext(
				RequestElementNames.I2b2_HIVE_NAMESPACE);
		nsContext.addPrefix("ns4", RequestElementNames.REQUEST_XML_NAMESPACE);
		return nsContext;
	}
}
