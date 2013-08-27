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
package edu.utah.further.core.xml.xquery.test;

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.xml.xquery.XQueryService;

/**
 * XQuery stand-alone tester.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2009 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jan 28, 2009
 */
// @Service("xqueryTester")
@Qualifier("xqueryTester")
public class CoreXmlXQueryTester
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(CoreXmlXQueryTester.class);

	/**
	 * Input XML document.
	 */
	private static final String INPUT_XML = "<?xml version='1.0' encoding='UTF-8'?>\n"
			+ "<CATALOG>\n" + "<CD>\n" + "<TITLE>Empire Burlesque</TITLE>\n"
			+ "<ARTIST>Bob Dylan</ARTIST>\n" + "<COUNTRY>USA</COUNTRY>\n"
			+ "<COMPANY>Columbia</COMPANY>\n" + "<PRICE>10.90</PRICE>\n"
			+ "<YEAR>1985</YEAR>\n" + "</CD>\n" + "<CD>\n"
			+ "<TITLE>Hide your heart</TITLE>\n" + "<ARTIST>Bonnie Tyler</ARTIST>\n"
			+ "<COUNTRY>UK</COUNTRY>\n" + "<COMPANY>CBS Records</COMPANY>\n"
			+ "<PRICE>9.90</PRICE>\n" + "<YEAR>1988</YEAR>\n" + "</CD>\n"
			+ "</CATALOG>\n";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates prototype XQuery data sources.
	 */
	@Autowired
	private XQueryService xQueryService;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * An XQuery unit test.
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void extractTitlesCdCatalog() throws UnsupportedEncodingException, IOException
	{
		if (log.isDebugEnabled())
		{
			log.debug("============== extractTitlesCdCatalog() begin ==============");
		}

		final String prolog = (xQueryService.isSupportsInlineVariables() ? "declare variable $docName as document-node() external;"
				+ NEW_LINE_STRING
				: EMPTY_STRING);
		final String queryString = prolog + "for $cd in $docName/CATALOG/CD "
				+ " return <cd>{$cd/TITLE/text()}</cd>";

		try (final InputStream inputXml = new ByteArrayInputStream(
				INPUT_XML.getBytes("UTF-8"));
				final InputStream inputXQuery = new ByteArrayInputStream(
						queryString.getBytes("UTF-8")))
		{

			final String result = xQueryService.executeIntoString(inputXQuery, inputXml);

			if (log.isDebugEnabled())
			{
				log.debug("result = " + result);
			}
			Validate.isTrue("<cd>Empire Burlesque</cd><cd>Hide your heart</cd>"
					.equals(StringUtil.stripNewLinesAndTabs(result)));
		}
	}

	// ========================= GET & SET =================================

	/**
	 * Return the xQueryService property.
	 * 
	 * @return the xQueryService
	 */
	public XQueryService getxQueryService()
	{
		return xQueryService;
	}

	/**
	 * Set a new value for the xQueryService property.
	 * 
	 * @param xQueryService
	 *            the xQueryService to set
	 */
	public void setxQueryService(final XQueryService xQueryService)
	{
		this.xQueryService = xQueryService;
	}

}
