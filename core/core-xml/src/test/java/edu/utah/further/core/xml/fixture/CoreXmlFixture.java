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
package edu.utah.further.core.xml.fixture;

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.context.Labeled;

/**
 * A test fixture for the <code>core-util</code> module.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-xml-test-context.xml" }, inheritLocations = false)
public abstract class CoreXmlFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CoreXmlFixture.class);

	/**
	 * Common path prefix to request processor test resources.
	 */
	private static final String CHAIN_TEST_PATH = "edu/utah/further/core/xml/chain";

	/**
	 * XML test file for marshalling and unmarshalling processor tests.
	 */
	protected static final String BOOKS_XML = CHAIN_TEST_PATH + "/books.xml";

	/**
	 * XML test schema for marshalling and unmarshalling processor tests.
	 */
	protected static final String BOOKS_XSD = CHAIN_TEST_PATH + "/books.xsd";

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	// ========================= CLASSES ===================================

	/**
	 * Private enum for request attributes
	 */
	protected static enum RequestAttributes implements Labeled
	{
		OBJECT_ATTR("object_attr"),

		SOURCE_ATTR("source_attr"),

		RESULT_ATTR("result_attr"),

		SCHEMA_ATTR("schema_attr"),

		JAXB_PKG_ATTR("jaxb_pkg_attr");

		/**
		 * The label
		 */
		private String label;

		/**
		 * @param label
		 */
		private RequestAttributes(final String label)
		{
			this.label = label;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.context.Labeled#getLabel()
		 */
		@Override
		public String getLabel()
		{
			return label;
		}

	}
}
