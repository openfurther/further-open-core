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
package edu.utah.further.fqe.api.fixture;

import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.jaxb.JaxbConfigurationFactoryBean;

/**
 * A common test fixture for the fqe-ds-api module.
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
@ContextConfiguration(locations =
{ "/fqe-api-test-context.xml" })
public abstract class FqeApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(FqeApiFixture.class);

	/**
	 * Our custom JAXB configuration.
	 */
	protected static final Map<String, Object> FQE_JAXB_CONFIG = JaxbConfigurationFactoryBean.DEFAULT_JAXB_CONFIG;

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles XML marshalling and unmarshalling.
	 */
	@Autowired
	protected XmlService xmlService;

	// ========================= SETUP METHODS =============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param fileName
	 * @param entity
	 * @throws IOException
	 * @throws JAXBException
	 */
	protected final void marshallingTest(final String fileName, final Object entity)
			throws JAXBException, IOException
	{
		final String marshalled = xmlService.marshal(entity, xmlService
				.options()
				.setFormat(true));
		if (log.isDebugEnabled())
		{
			log.debug("Marshalled:" + Strings.NEW_LINE_STRING + marshalled);
		}
		final String expected = IoUtil.getResourceAsString(fileName);
		assertNotNull("Marshalling failed", marshalled);
		xmlAssertion(XmlAssertion.Type.EXACT_MATCH)
				.actualResourceString(marshalled)
				.expectedResourceString(expected)
				.stripNewLinesAndTabs(true)
				.doAssert();
	}
}
