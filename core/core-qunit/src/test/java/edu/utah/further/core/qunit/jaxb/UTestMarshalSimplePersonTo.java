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
package edu.utah.further.core.qunit.jaxb;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.qunit.fixture.CoreXmlTestFixture;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.xml.jaxb.JaxbConfig;

/**
 * Unit test to marshal and unmarshal {@link SimplePersonTo} required and optional
 * attributes.
 * <p>
 * Note: JAXB does not guarantee any attribute ordering.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jun 21, 2010
 */
@DirtiesContext
public final class UTestMarshalSimplePersonTo extends CoreXmlTestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * The directory which contains pertinent test XML files.
	 */
	private static final String JAXB_DIR = "jaxb" + Strings.VIRTUAL_DIRECTORY;

	/**
	 * Expected marshalling result.
	 */
	private static final String SIMPLE_PERSON_XML_POPULATED = JAXB_DIR
			+ "simple-person-to-populated.xml";

	/**
	 * Expected marshalling result.
	 */
	private static final String SIMPLE_PERSON_XML_EMPTY = JAXB_DIR
			+ "simple-person-to-empty.xml";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes XML JAXB operations.
	 */
	@Autowired
	private XmlService xmlService;

	// ========================= SETUP METHODS =============================
	/**
	 * Setup for all test methods
	 */
	@Before
	public void globalSetup()
	{
		// Uses default empty namespace
		xmlService.setDefaultJaxbConfig(JaxbConfig.EMPTY.getJaxbConfig());
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Unmarshal a person TO whose XML-attribute-type properties are set.
	 *
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void unmarshalPopulatedObject() throws JAXBException, IOException
	{
		assertMarshalingResultEquals(SIMPLE_PERSON_XML_POPULATED,
				newSimplePersonToPopulated());
	}

	/**
	 * Marshal a person TO whose XML-attribute-type properties are set into XML.
	 *
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void marshalPopulatedObject() throws JAXBException
	{
		assertUnmarshalingResultEquals(newSimplePersonToPopulated(),
				SIMPLE_PERSON_XML_POPULATED);
	}

	/**
	 * Unmarshal a person TO whose XML-attribute-type properties are NOT set.
	 *
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void unmarshalEmptyObject() throws JAXBException, IOException
	{
		assertMarshalingResultEquals(SIMPLE_PERSON_XML_EMPTY, newSimplePersonToEmpty());
	}

	/**
	 * Marshal a person TO whose XML-attribute-type properties are NOT set into XML.
	 *
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void marshalEmptyObject() throws JAXBException
	{
		assertUnmarshalingResultEquals(newSimplePersonToEmpty(), SIMPLE_PERSON_XML_EMPTY);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param inputXmlResourceName
	 * @param expectedPerson
	 * @throws JAXBException
	 * @throws IOException
	 */
	private void assertMarshalingResultEquals(final String inputXmlResourceName,
			final SimplePersonTo expectedPerson) throws JAXBException, IOException
	{
		final SimplePersonTo person = xmlService.unmarshalResource(inputXmlResourceName,
				SimplePersonTo.class);
		assertEquals("Unexpected unmarshalling result", expectedPerson, person);
	}

	/**
	 * @param person
	 * @param expectedXmlResourceName
	 * @throws JAXBException
	 */
	private void assertUnmarshalingResultEquals(final SimplePersonTo person,
			final String expectedXmlResourceName) throws JAXBException
	{
		XmlAssertion
				.xmlAssertion(XmlAssertion.Type.EXACT_MATCH)
				.actualResourceString(
						xmlService.marshal(
								person,
								xmlService
										.options()
										.addClass(SimplePersonTo.class)
										.buildContext()
										.setRootNamespaceUris(
												CollectionUtil.<String> newSet())))
				.expectedResourceName(expectedXmlResourceName)
				.stripNewLinesAndTabs(true)
				.doAssert();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("boxing")
	private SimplePersonTo newSimplePersonToPopulated()
	{
		final SimplePersonTo person = new SimplePersonTo();
		person.setRequiredInt(123);
		person.setOptionalInt(456);
		person.setRequiredString("abc");
		person.setOptionalString("def");
		return person;
	}

	/**
	 * @return
	 */
	private SimplePersonTo newSimplePersonToEmpty()
	{
		return new SimplePersonTo();
	}
}
