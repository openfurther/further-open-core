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

import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStreamValidate;
import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.xml.xquery.XQueryService;

/**
 * XQuery test assertions and utilities.
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
 * @version May 6, 2010
 */
@Service("xqueryTestUtil")
public final class XQueryAssertUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XQueryAssertUtil.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes XQuery programs.
	 */
	@Autowired
	private XQueryService xqueryService;

	// ========================= METHODS ===================================

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to strings and compared.
	 * 
	 * @param xqueryResourceName
	 * @param inputResourceName
	 * @param resourceName
	 * @param parameters
	 *            contains externally-binded parameter names, values and types
	 * @param ignoredElements
	 *            local names of XML elements to be excluded from the comparison
	 */
	public void assertXQueryOutputEquals(final String xqueryResourceName,
			final String inputResourceName, final String resourceName,
			final String... ignoredElements)
	{
		assertXQueryOutputEquals(xqueryResourceName, inputResourceName, resourceName,
				CollectionUtil.<String, String> newMap(), ignoredElements);
	}

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to strings and compared.
	 * 
	 * @param xqueryResourceName
	 * @param inputResourceName
	 * @param resourceName
	 * @param parameters
	 *            contains externally-binded parameter names, values and types
	 * @param ignoredElements
	 *            local names of XML elements to be excluded from the comparison
	 */
	@SuppressWarnings("resource")
	public void assertXQueryOutputEquals(final String xqueryResourceName,
			final String inputResourceName, final String resourceName,
			final Map<String, String> parameters, final String... ignoredElements)
	{
		InputStream xqueryInputStream = null;
		InputStream inputStream = null;
		try
		{
			xqueryInputStream = getResourceAsStreamValidate(xqueryResourceName);
			inputStream = getResourceAsStreamValidate(inputResourceName);
			final String actualResult = xqueryService.executeIntoString(
					xqueryInputStream, inputStream, parameters);
			// Validate XQuery output
			xmlAssertion(XmlAssertion.Type.EXACT_MATCH)
					.stripNewLinesAndTabs(true)
					.actualResourceString(actualResult)
					.expectedResourceName(resourceName)
					.ignoredElements(ignoredElements)
					.doAssert();
		}
		finally
		{
			if (xqueryInputStream != null)
			{
				try
				{
					xqueryInputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				}
			}
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				}
			}
		}

	}

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to StAX streams and converted.
	 * 
	 * @param xqueryResourceName
	 * @param inputResourceName
	 * @param resourceName
	 */
	public void assertXQueryStreamEquals(final String xqueryResourceName,
			final String inputResourceName, final String resourceName)
	{
		assertXQueryStreamEquals(xqueryResourceName, inputResourceName, resourceName,
				CollectionUtil.<String, String> newMap());
	}

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to StAX streams and converted.
	 * 
	 * @param xqueryResourceName
	 * @param inputResourceName
	 * @param expectedResourceName
	 */
	@SuppressWarnings("resource")
	// Resources closed in the finally block
	public void assertXQueryStreamEquals(final String xqueryResourceName,
			final String inputResourceName, final String expectedResourceName,
			final Map<String, String> parameters)
	{
		InputStream xqueryInputStream = null;
		InputStream inputStream = null;
		try
		{
			// Run XQuery program
			xqueryInputStream = getResourceAsStreamValidate(xqueryResourceName);
			inputStream = getResourceAsStreamValidate(inputResourceName);
			final XMLStreamReader actualResult = xqueryService.executeIntoStream(
					xqueryInputStream, inputStream, parameters);

			// Validate XQuery output
			xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
					.actualResourceXmlStreamReader(actualResult)
					.expectedResourceName(expectedResourceName)
					.doAssert();
		}
		finally
		{
			if (xqueryInputStream != null)
			{
				try
				{
					xqueryInputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				}
			}
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				}
			}
		}
	}

	// ========================= PRIVATE METHODS ===================================

}
