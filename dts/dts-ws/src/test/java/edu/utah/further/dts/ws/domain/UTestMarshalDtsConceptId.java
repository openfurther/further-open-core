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
package edu.utah.further.dts.ws.domain;

import static edu.utah.further.dts.impl.util.DtsTestingNames.MARRIED_UUEDW_DWID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.ws.fixture.DtsWsFixture;

/**
 * A unit test of reading and writing XML files into and from JavaBeans using
 * {@link XmlService}.
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
 * @version Oct 17, 2008
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestMarshalDtsConceptId extends DtsWsFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestMarshalDtsConceptId.class);

	/**
	 * Marshalling test file.
	 */
	private static final String DTS_CONCEPT_ID_XML = "dts-concept-id.xml";

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * DTS service.
	 */
	@Autowired
	private DtsOperationService dos;

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Unmarshal a concept ID -- an entity used by DTS web services.
	 */
	@Test
	public void unmarshalDtsConceptId() throws Exception
	{
		final DtsConceptId result = xmlService.unmarshalResource(DTS_CONCEPT_ID_XML,
				DtsConceptId.class);
		assertNull("Namespace should have NOT been set for a machine view",
				result.getNamespace());
		assertNull("Property name should have NOT been set for a machine view",
				result.getPropertyName());
		assertEquals(MARRIED_UUEDW_DWID, result.getPropertyValue());
	}

	/**
	 * Marshal a concept ID -- an entity used by DTS web services.
	 */
	@Test
	public void marshalDtsConceptId() throws Exception
	{
		xmlService.marshalAndPrint(getFakeConceptId());
	}

	/**
	 * Marshal a concept ID -- an entity used by DTS web services.
	 */
	@Test
	public void marshalDtsConceptIdNullFields() throws Exception
	{
		xmlService.marshalAndPrint(getFakeConceptId());
	}

	/**
	 * Marshal a concept ID -- an entity used by DTS web services.
	 */
	@Test
	public void marshalDtsConceptIdAllNullFields() throws Exception
	{
		xmlService.marshalAndPrint(new DtsConceptId());
	}

	/**
	 * Marshal a {@link DtsNamespace} into XML.
	 */
	@Test
	public void marshalNamespace() throws Exception
	{
		final List<DtsNamespace> nsList = dos.getNamespaceList();
		for (final DtsNamespace namespace : nsList)
		{
			log.debug("namespace " + namespace);
			xmlService.marshalAndPrint(dtsDataToFactory.newInstance(namespace));
		}
	}

	/**
	 * @return
	 */
	private DtsConceptId getFakeConceptId()
	{
		return new DtsConceptId("namespace", "propertyName", "propertyValue");
	}
}
