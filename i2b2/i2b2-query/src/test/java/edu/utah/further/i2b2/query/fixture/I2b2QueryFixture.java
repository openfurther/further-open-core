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
package edu.utah.further.i2b2.query.fixture;

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.xml.XmlService;

/**
 * Test fixture for common data, objects, etc used in I2b2 Query tests.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 19, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/i2b2-query-test-annotation-context.xml", "/i2b2-query-test-context.xml" })
public abstract class I2b2QueryFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(I2b2QueryFixture.class);

	/**
	 * The directory where the request files are located.
	 */
	protected static final String REQ_DIR = "requests/";

	/**
	 * I2b2 query input XML.
	 */
	protected static final String RAW_I2b2_XML_SIMPLE = "requests/i2b2-raw-simple.xml";

	/**
	 * FURTHeR query output XML.
	 */
	protected static final String I2B2_QUERY_XML_SIMPLE = "requests/i2b2-request-query-simple.xml";

	/**
	 * I2b2 query input XML.
	 */
	protected static final String RAW_I2b2_XML_MCLAIN = "requests/i2b2-raw-mclain.xml";

	/**
	 * FURTHeR query output XML.
	 */
	protected static final String I2B2_QUERY_XML_MCLAIN = "requests/i2b2-request-query-mclain.xml";
	// ========================= DEPENDENCIES ==============================

	/**
	 * XmlService
	 */
	@Autowired
	protected XmlService xmlService;
}
