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

import static edu.utah.further.i2b2.query.criteria.service.impl.RequestElementNames.I2b2_HIVE_NAMESPACE;
import static edu.utah.further.i2b2.query.criteria.service.impl.RequestElementNames.REQUEST_XML_NAMESPACE;
import static javax.xml.xpath.XPathConstants.NODESET;
import static junit.framework.Assert.assertEquals;

import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.api.xml.transform.XmlTransformUtil;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.qunit.runner.XmlAssertion.Type;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.xpath.XPathNamespaceContext;
import edu.utah.further.core.xml.xpath.XPathParser;
import edu.utah.further.i2b2.query.fixture.I2b2QueryFixture;

/**
 * Learning how to parse an XML document using the Java XPath API.
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
 * @version Dec 5, 2008
 * @see http://www.ibm.com/developerworks/library/x-javaxpathapi.html#changed
 */
public final class UTestRawI2b2Converter extends I2b2QueryFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(UTestRawI2b2Converter.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * i2b2-raw-to-I2b2-query converter.
	 */
	@Resource(name = "rawToI2b2QueryConverter")
	private RawToI2b2QueryConverter rawToI2b2QueryConverter;

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * @throws Exception
	 */
	@Test
	public void getRequestNodeSubTreeAsNewDocumentNamespaceAware() throws Exception
	{
		try (final InputStream inputStream = CoreUtil.getResourceAsStream(RAW_I2b2_XML_SIMPLE))
		{
			final XPathNamespaceContext nsContext = new XPathNamespaceContext(
					I2b2_HIVE_NAMESPACE);
			nsContext.addPrefix("ns4", REQUEST_XML_NAMESPACE);

			final String xpathExpression = "//ns4:request";
			// final String xpathExpression = "//query_definition";
			final XPathParser parser = new XPathParser(xpathExpression, nsContext);
			final NodeList nodes = (NodeList) parser.evaluateXPath(inputStream, NODESET);
			assertEquals(1, nodes.getLength());

			// Marshal the node
			final Node node = nodes.item(0);
			XmlTransformUtil.printToString(node);
		}
	}

	/**
	 * Test {@link RequestProcessor#toI2b2QueryXml(InputStream)}.
	 */
	@Test
	public void convertRawI2b2ToI2b2QuerySimple() throws Exception
	{
		convertRawI2b2ToI2b2Query(RAW_I2b2_XML_SIMPLE, I2B2_QUERY_XML_SIMPLE);
	}

	/**
	 * Test {@link RequestProcessor#toI2b2QueryXml(InputStream)}.
	 */
	@Test
	public void convertRawI2b2ToI2b2QueryMcLain() throws Exception
	{
		convertRawI2b2ToI2b2Query(RAW_I2b2_XML_MCLAIN, I2B2_QUERY_XML_MCLAIN);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param rawRequestResource
	 * @param i2b2RequestResource
	 */
	private void convertRawI2b2ToI2b2Query(final String rawRequestResource,
			final String i2b2RequestResource)
	{
		final String rawI2b2Xml = IoUtil.getInputStreamAsString(CoreUtil
				.getResourceAsStream(rawRequestResource));
		final String i2b2QueryXml = rawToI2b2QueryConverter.toI2b2Query(rawI2b2Xml);
		XmlAssertion
				.xmlAssertion(Type.EXACT_MATCH)
				.actualResourceString(i2b2QueryXml)
				.expectedResourceName(i2b2RequestResource)
				.stripNewLinesAndTabs(true)
				.doAssert();
	}
}