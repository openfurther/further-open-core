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
package edu.utah.further.core.camel;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.test.annotation.UnitTest;

/**
 * Test JAXB message post processing.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@UnitTest
// @RunWith(SpringJUnit4ClassRunner.class)
// @RunWith(JUnit4ClassRunner.class)
public final class UTestJaxbPostProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestJaxbPostProcessor.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 *
	 */
	@Test
	public void replaceFirstMethodCalledOnTheFly()
	{
		assertEquals("|abc>", "<abc>".replaceFirst("<", "|"));
	}

	/**
	 *
	 */
	@Test
	public void replaceFirstSetMethodReturnType()
	{
		String body = "<abc>";
		body = body.replaceFirst("<", "|");
		assertEquals("|abc>", body);
	}

	/**
	 * Test stripping the XML document declaration.
	 */
	@Test
	public void stripXmlDeclaration() throws Exception
	{
		final String rawBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>aaa";
		final String expectedBody = "aaa";
		testStripBody(rawBody, expectedBody);
	}

	/**
	 * Test stripping a body.
	 */
	@Test
	public void stripBodySameClass() throws Exception
	{
		final String rawBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:data xmlns:ns2=\""
				+ XmlNamespace.FQE
				+ "\"><name>Default name</name><description>Default description</description><status>INACTIVE</status></ns2:data>";
		final String expectedBody = "<ns1:data><name>Default name</name><description>Default description</description><status>INACTIVE</status></ns1:data>";
		testStripBody(rawBody, expectedBody);
	}

	/**
	 * Test stripping a body.
	 */
	@Test
	public void stripBodySubClass() throws Exception
	{
		final String rawBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:dsMetaData xmlns:ns2=\""
				+ XmlNamespace.FQE
				+ "\"><name>Default name</name><description>Default description</description><status>INACTIVE</status></ns2:dsMetaData>";
		final String expectedBody = "<ns1:dsMetaData><name>Default name</name><description>Default description</description><status>INACTIVE</status></ns1:dsMetaData>";
		testStripBody(rawBody, expectedBody);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param rawBody
	 * @param expectedBody
	 */
	private void testStripBody(final String rawBody, final String expectedBody)
	{
		final String transformedBody = JaxbNamespaceStripper.newPolymorphicInstance(
				XmlNamespace.XML_NS1, "data", false).stripNamespace(rawBody);
		if (log.isDebugEnabled())
		{
			log.debug("rawBody         = " + rawBody);
			log.debug("expectedBody    = " + expectedBody);
			log.debug("transformedBody = " + transformedBody);
		}
		assertEquals("Incorrect namespace stripping", expectedBody, transformedBody);
	}

}
