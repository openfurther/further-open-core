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
package edu.utah.further.core.qunit.xquery;

import static edu.utah.further.core.qunit.fixture.CoreXmlNames.INPUT_FILE_DATE1;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.INPUT_FILE_DATE2;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.INPUT_FILE_NAMESPACE_AWARE;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.INPUT_FILE_NO_DATE;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.INPUT_FILE_UNQUALIFIED;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.OUTPUT_FILE_NAMESPACE_AWARE;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.OUTPUT_FILE_UNQUALIFIED;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.XQUERY_FILE_NAMESPACE_AWARE;
import static edu.utah.further.core.qunit.fixture.CoreXmlNames.XQUERY_FILE_UNQUALIFIED;
import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.xquery.XQException;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;

import edu.utah.further.core.qunit.fixture.CoreXmlNames;
import edu.utah.further.core.qunit.fixture.CoreXmlTestFixture;
import edu.utah.further.core.qunit.runner.XmlAssertUtil;
import edu.utah.further.core.test.annotation.UnitTest;

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
@UnitTest
@ContextConfiguration(locations =
{ "/core-xml-test-context.xml" })
public final class UTestXQueryService extends CoreXmlTestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestXQueryService.class);

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * @throws XQException
	 */
	@Test
	public void extractTitlesCdCatalogUnqualified() throws XQException
	{
		xqueryTestUtil.assertXQueryOutputEquals(XQUERY_FILE_UNQUALIFIED,
				INPUT_FILE_UNQUALIFIED, OUTPUT_FILE_UNQUALIFIED);
	}

	/**
	 * @throws XQException
	 */
	@Test
	public void extractTitlesCdCatalogNamespaceAware() throws XQException
	{
		xqueryTestUtil.assertXQueryOutputEquals(XQUERY_FILE_NAMESPACE_AWARE,
				INPUT_FILE_NAMESPACE_AWARE, OUTPUT_FILE_NAMESPACE_AWARE);
	}

	/**
	 * @throws XQException
	 */
	@Test
	public void extractTitlesCdCatalogNamespaceAwareStreamComparison() throws XQException
	{
		xqueryTestUtil.assertXQueryStreamEquals(XQUERY_FILE_NAMESPACE_AWARE,
				INPUT_FILE_NAMESPACE_AWARE, OUTPUT_FILE_NAMESPACE_AWARE);
	}

	/**
	 * @throws XQException
	 */
	@Test
	public void ignoredElement() throws XQException
	{
		XmlAssertUtil.assertStreamEquals(INPUT_FILE_DATE1, INPUT_FILE_DATE2, "startDate",
				"endDate");
	}

	/**
	 * @throws XQException
	 * @see FUR-1086
	 */
	@Test
	public void ignoredElementEmptyVersusNoEmpty() throws XQException
	{
		XmlAssertUtil.assertStreamEquals(INPUT_FILE_NO_DATE, INPUT_FILE_DATE1,
				"startDate", "endDate");
	}

	/**
	 * @throws XQException
	 * @see FUR-1086
	 */
	@Test
	public void ignoredElementNonEmptyVersusEmpty() throws XQException
	{
		XmlAssertUtil.assertStreamEquals(INPUT_FILE_DATE1, INPUT_FILE_NO_DATE,
				"startDate", "endDate");
	}

	/**
	 * @throws XQException
	 * @see FUR-1086
	 */
	@Test
	public void ignoredElementInPatientDocument() throws XQException
	{
		XmlAssertUtil.assertStreamEquals(CoreXmlNames.PATIENT_NO_VALUE,
				CoreXmlNames.PATIENT_WITH_VALUE, "ageInYearsNum", "encounterNum");
	}

	/**
	 * @throws XQException
	 * @see FUR-1086
	 */
	@Test
	public void ignoredEmptyElementInPatientDocument() throws XQException
	{
		XmlAssertUtil.assertStreamEquals(CoreXmlNames.PATIENT_NO_VALUE_EMPTY_ELEMENT,
				CoreXmlNames.PATIENT_WITH_VALUE, "ageInYearsNum", "encounterNum");
	}

	// ========================= PRIVATE METHODS ===========================

}
