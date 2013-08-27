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
package edu.utah.further.core.qunit.fixture;

import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.text.StringUtil.stripNewLinesAndTabs;
import static edu.utah.further.core.api.xml.XmlUtil.newSerializationPropertiesForPrintout;
import static edu.utah.further.core.util.io.IoUtil.copyToStringAndStripNewLinesAndTabs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQSequence;
import javax.xml.xquery.XQStaticContext;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.xml.xquery.XQueryDataSource;
import edu.utah.further.core.xml.xquery.XQueryUtil;

/**
 * Learning how to parse an XML document using XQuery's DataDirect implementation.
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-xml-test-context.xml" })
public abstract class XQueryLearningFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XQueryLearningFixture.class);

	// ========================= FIELDS ====================================

	/**
	 * Instantiates prototype XQuery data sources.
	 */
	@Autowired
	protected XQueryDataSource xqueryDataSource;

	/**
	 * Data Source for querying.
	 */
	protected XQDataSource dataSource;

	/**
	 * Connection for querying.
	 */
	protected XQConnection conn;

	/**
	 * Points to a sample XML document to use.
	 */
	protected InputStream is;

	// ========================= SETUP METHODS =============================

	/**
	 * @throws XQException
	 */
	@Before
	public void setUp() throws XQException
	{
		if (dataSource == null)
		{
			dataSource = xqueryDataSource.newXQDataSource();
		}
		conn = dataSource.getConnection();
	}

	/**
	 * @throws XQException
	 */
	@After
	public void tearDown() throws XQException
	{
		conn.close();
		conn = null;
		is = null;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * An XQuery program test template.
	 *
	 * @param xqueryClassPath
	 * @param inputClassPath
	 * @param expectedOutputClassPath
	 * @throws XQException
	 * @throws IOException
	 */
	protected final void testXquery(final String xqueryClassPath,
			final String inputClassPath, final String expectedOutputClassPath)
			throws XQException, IOException
	{
		String actualResult;
		String expectedResult;
		
		try (final InputStream inputStream = CoreUtil.getResourceAsStream(inputClassPath);final InputStream xqueryInputStream = CoreUtil
				.getResourceAsStream(xqueryClassPath)) {
			// new ByteArrayInputStream(client
			// .getActiveResourceContentByPath(xqueryClassPath)
			// .getBytes());
			assertNotNull("Input file classpath resource " + quote(inputClassPath)
					+ " not found", inputStream);
			actualResult = stripNewLinesAndTabs(runXquery(xqueryInputStream,
					inputStream));
			expectedResult = copyToStringAndStripNewLinesAndTabs(expectedOutputClassPath);

			if (log.isDebugEnabled())
			{
				log.debug("Actual   : " + actualResult);
				log.debug("Expected : " + expectedResult);
			}
		}
		assertEquals("Unexpected XQuery output", expectedResult, actualResult);
	}

	/**
	 * @param results
	 * @throws XQException
	 */
	protected static final String getXqueryResults(final XQSequence results)
			throws XQException
	{
		final Properties serializationProps = newSerializationPropertiesForPrintout();
		final String result = results.getSequenceAsString(serializationProps);
		// if (log.isDebugEnabled())
		// {
		// log.debug("XQuery result:" + NEW_LINE_STRING + result);
		// }
		return result;
	}

	/**
	 * @param xqueryInputStream
	 * @return
	 * @throws XQException
	 * @throws IOException
	 */
	protected final String runXquery(final InputStream xqueryInputStream,
			final InputStream inputStream) throws XQException, IOException
	{
		// Get the default values from the implementation
		final XQStaticContext context = createContext();
		final XQExpression expression = conn.createExpression(context);
		XQueryUtil.bindDocument(expression, inputStream);
		final Map<String, String> parameterMap = CollectionUtil.newMap();
		XQueryUtil.bindParameters(expression, parameterMap);

		final XQSequence results = expression.executeQuery(xqueryInputStream);
		final String result = getXqueryResults(results);
		return result;
	}

	/**
	 * @return
	 * @throws XQException
	 */
	private XQStaticContext createContext() throws XQException
	{
		final XQStaticContext context = conn.getStaticContext();
		// context.declareNamespace("uml", "http://schema.omg.org/spec/UML/2.1");
		// context.declareNamespace("xmi", "http://schema.omg.org/spec/XMI/2.1");
		return context;
	}

}