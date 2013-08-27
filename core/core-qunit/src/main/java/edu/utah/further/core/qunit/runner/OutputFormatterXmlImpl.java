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
package edu.utah.further.core.qunit.runner;

import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStreamValidate;
import static edu.utah.further.core.api.xml.XmlUtil.createDomDocument;
import static edu.utah.further.core.api.xml.transform.XmlTransformUtil.printToString;
import static edu.utah.further.core.util.io.IoUtil.getInputStreamAsString;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;

/**
 * A formatter of XML results. Pretty-prints both the actual and expected output XML
 * documents.
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
 * @version Sep 2, 2010
 */
public class OutputFormatterXmlImpl implements OutputFormatter
{
	// ========================= IMPL: OutputFormatterXmlImpl ==============

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.qunit.runner.OutputFormatter#formatActualOutput(edu.utah.
	 * further.core.qunit.runner.QTestResult,
	 * edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@Override
	public String formatActualOutput(final QTestResult result, final QTestContext context)
	{
		final Document actualOutputDom = createDomDocument(result
				.getActualResourceString());
		return printToString(actualOutputDom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.qunit.runner.OutputFormatter#formatExpectedOutput(edu.utah
	 * .further.core.qunit.runner.QTestData,
	 * edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@SuppressWarnings("resource")
	//Stream is closed in finally block
	@Override
	public String formatExpectedOutput(final QTestData testData,
			final QTestContext context)
	{
		InputStream expectedStream = null;
		try
		{
			expectedStream = getResourceAsStreamValidate(context.getExpectedClassPath()
					+ testData.getExpected());
			final Document expectedOutputDom = createDomDocument(getInputStreamAsString(expectedStream));
			final String output = printToString(expectedOutputDom);
			return output;
		}
		finally
		{
			try
			{
				if (expectedStream != null)
				{
					expectedStream.close();
				}
			}
			catch (IOException e)
			{
				// Ignore
			}
		}
	}

}
