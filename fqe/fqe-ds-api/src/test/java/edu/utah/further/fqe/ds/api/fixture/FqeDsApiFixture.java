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
package edu.utah.further.fqe.ds.api.fixture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.util.io.IoUtil;

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
{ "/fqe-ds-api-test-context.xml" })
public abstract class FqeDsApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(FqeDsApiFixture.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles XML marshalling and unmarshalling.
	 */
	@Autowired
	protected XmlService xmlService;

	// ========================= SETUP METHODS =============================

	@Before
	public void fixtureSetup()
	{
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param fileName
	 * @param entity
	 * @throws IOException
	 * @throws JAXBException
	 * @throws SAXException
	 */
	protected final void marshallingTest(final String fileName, final Object entity)
			throws JAXBException, IOException, SAXException
	{
		final String marshalled = xmlService.marshal(entity, xmlService
				.options()
				.setFormat(true));
		if (log.isDebugEnabled())
		{
			log.debug("Marshalled:" + Strings.NEW_LINE_STRING + marshalled);
		}
		assertNotNull("Marshalling failed", marshalled);
		final String expected = IoUtil.getResourceAsString(fileName);
		final DetailedDiff diff = new DetailedDiff(new Diff(expected, marshalled));

		assertTrue("XML is different" + diff.getAllDifferences(), diff.similar());

	}
}
