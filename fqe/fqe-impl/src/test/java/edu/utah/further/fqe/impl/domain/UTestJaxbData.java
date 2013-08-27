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
package edu.utah.further.fqe.impl.domain;

import static junit.framework.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.JaxbConfig;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;

/**
 * Test [un-]marshalling composite {@link Data} entities and their sub-classes with JAXB
 * polymorphism syntax.
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
 * @version May 29, 2009
 */
@UnitTest
public final class UTestJaxbData extends FqeImplUtestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestJaxbData.class);

	/**
	 * Test input for marshalling tests.
	 */
	private static final String RAW_CHILDREN_FILE = "dataset-raw.xml";

	/**
	 * Test input for marshalling tests.
	 */
	private static final String RAW_XSI_FILE = "dataset-ds-xsi-type.xml";

	/**
	 * Test input for marshalling tests.
	 */
	private static final String RAW_SHORT_FILE = "dataset-ds-short.xml";

	/**
	 * Test input for marshalling tests.
	 */
	private static final String FQE_FILE = "dataset-ds-fqe.xml";

	// ========================= DEPENDENCIES ==============================

	/**
	 * JAXB utilities.
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * For setup/tear-down.
	 */
	private Map<String, Object> jaxbConfig;

	// ========================= FIELDS ====================================

	/**
	 * Set up default JAXB configuration.
	 */
	@Before
	public void setUpXmlService()
	{
		jaxbConfig = xmlService.getDefaultJaxbConfig();
		xmlService.setDefaultJaxbConfig(JaxbConfig.FURTHER.getJaxbConfig());
	}

	/**
	 * Restore original default JAXB configuration.
	 */
	@After
	public void restoreXmlService()
	{
		xmlService.setDefaultJaxbConfig(jaxbConfig);
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a Data set with raw {@link Data} children.
	 */
	@Test
	public void marshalDataSetRawChildren() throws Exception
	{
		// Raw parent
		final Data set = new Data();

		// Raw child
		final Data metaData = new Data("name", "description");
		set.addChild(metaData);

		assertEquals(1, set.getNumChildren());

		final String s = xmlService.marshal(set);
		if (log.isDebugEnabled())
		{
			log.debug("Entity    = " + set);
			log.debug("Marshaled = " + s);
		}
	}

	/**
	 * Marshal a Data set with {@link DsMetaData} children.
	 */
	@Test
	public void marshalDataSetDsChildren() throws Exception
	{
		// Raw parent
		final Data set = new Data();

		// Specific child
		final DsMetaData metaData = new DsMetaData("name", "description");
		metaData.setState(DsState.ACTIVE);
		set.addChild(metaData);

		assertEquals(1, set.getNumChildren());

		final String s = xmlService.marshal(set);
		if (log.isDebugEnabled())
		{
			log.debug("Entity    = " + set);
			log.debug("Marshaled = " + s);
		}
	}

	/**
	 * Unmarshal a Data set with raw {@link Data} children.
	 */
	@Test
	public void unmarshalDataSetRawChildren() throws Exception
	{
		final Data result = xmlService.unmarshalResource(RAW_CHILDREN_FILE, Data.class);
		assertEquals(2, result.getNumChildren());
	}

	/**
	 * Unmarshal a Data set with {@link DsMetaData} children.
	 */
	@Test
	public void unmarshalDataSetDsChildrenXsiType() throws Exception
	{
		// Doesn't work because we are using @XmlElementRef, not @XmlElement
		testDataSet(RAW_XSI_FILE, 0);
	}

	/**
	 * Unmarshal a Data set with {@link DsMetaData} children. Shortened XML namespace
	 * format.
	 */
	@Test
	public void unmarshalDataSetDsChildrenShortFormat() throws Exception
	{
		testDataSet(RAW_SHORT_FILE, 2);
	}

	/**
	 * Unmarshal a Data set with {@link DsMetaData} children from FQE-processed XML before
	 * it is massaged by some translators.
	 */
	@Test
	public void unmarshalDataSetDsChildrenRawFqeProcessingFormat() throws Exception
	{
		testDataSet(FQE_FILE, 2);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param resourceName
	 * @param expectedNumChildren
	 * @throws JAXBException
	 * @throws IOException
	 */
	private void testDataSet(final String resourceName, final int expectedNumChildren)
			throws JAXBException, IOException
	{
		final Data result = xmlService.unmarshalResource(resourceName, xmlService
				.options()
				.addClass(DsMetaData.class));

		assertEquals(expectedNumChildren, result.getNumChildren());
		if (expectedNumChildren > 0)
		{
			final DsMetaData md1 = (DsMetaData) result.getChildren().get(0);
			assertEquals(DsState.ACTIVE, md1.getState());
		}
		if (expectedNumChildren > 1)
		{
			final DsMetaData md2 = (DsMetaData) result.getChildren().get(1);
			assertEquals(DsState.INACTIVE, md2.getState());
		}
	}
}
