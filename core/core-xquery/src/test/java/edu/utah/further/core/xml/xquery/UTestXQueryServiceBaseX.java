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
package edu.utah.further.core.xml.xquery;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A unit test for testing the BaseX implementation of {@link XQueryService}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jun 3, 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "/core-xquery-test-basex.xml" })
public class UTestXQueryServiceBaseX
{
	/**
	 * The XQuery Service we're testing.
	 */
	@Autowired
	private XQueryService xQueryService;

	/**
	 * The XQuery to execute
	 */
	private InputStream testXQuery;

	/**
	 * Test XML document
	 */
	private InputStream testXmlDocument;

	@Before
	public void setup()
	{
		final StringBuilder sbXq = new StringBuilder();
		sbXq.append("xquery version \"1.0\";");
		sbXq.append("let $message := 'Hello World!'");
		sbXq.append("return");
		sbXq.append("<results>");
		sbXq.append("<message>{$message}</message>");
		sbXq.append("</results>");

		testXQuery = IOUtils.toInputStream(sbXq.toString());

		final StringBuilder sbXml = new StringBuilder();
		sbXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sbXml.append("<input></input>");

		testXmlDocument = IOUtils.toInputStream(sbXml.toString());
	}

	/**
	 * Execute a test XQuery document and put the results in a String.
	 */
	@Test
	public void executeXQueryIntoString()
	{
		final String result = xQueryService.executeIntoString(testXQuery,
				testXmlDocument, new HashMap<String, String>());
		assertNotNull(result);
		assertThat(result, containsString("<message>Hello World!</message>"));
	}

	/**
	 * Execute a test XQuery document and put the results in a Stream.
	 * 
	 * @throws XMLStreamException
	 */
	@Test
	public void executeXQueryIntoStream() throws XMLStreamException
	{
		final XMLStreamReader result = xQueryService.executeIntoStream(testXQuery,
				testXmlDocument, new HashMap<String, String>());
		assertNotNull(result);

		String elementResult = null;
		while (result.hasNext())
		{
			if (result.next() == XMLStreamReader.START_ELEMENT
					&& result.getName().getLocalPart() == "message")
			{
				elementResult = result.getElementText();
			}
		}

		assertNotNull(elementResult);
		assertThat(elementResult, is("Hello World!"));
	}
}
