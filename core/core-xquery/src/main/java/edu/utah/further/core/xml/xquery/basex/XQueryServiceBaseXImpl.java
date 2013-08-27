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
package edu.utah.further.core.xml.xquery.basex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.basex.core.Context;
import org.basex.data.Result;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.xquery.XQueryResultProcessor;
import edu.utah.further.core.xml.xquery.XQueryService;
import edu.utah.further.core.xml.xquery.XQueryUtil;

/**
 * A BaseX Implementation of the {@link XQueryService}
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
 * @version Jun 14, 2013
 */
public final class XQueryServiceBaseXImpl implements XQueryService
{

	/**
	 * An {@link XMLInputFactory} for creating {@link XMLStreamReader}s
	 */
	private XMLInputFactory factory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryPropertiesContainer#isSupportsInlineVariables
	 * ()
	 */
	@Override
	public boolean isSupportsInlineVariables()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoString(java.io.InputStream
	 * , java.io.InputStream, java.util.Map)
	 */
	@Override
	public String executeIntoString(final InputStream xQuery, final InputStream inputXml,
			final Map<String, String> parameters)
	{
		final Context context = new Context();
		QueryProcessor queryProcessor = null;
		String resultString = null;
		try
		{
			final String xQueryString = IoUtil
					.copyToStringAndStripNewLinesAndTabs(xQuery);
			final String inputXmlString = IoUtil
					.copyToStringAndStripNewLinesAndTabs(inputXml);

			queryProcessor = new QueryProcessor(xQueryString, context);

			queryProcessor.bind(XQueryUtil.DOCUMENT_NAME, inputXmlString,
					"document-node()");

			for (final Entry<String, String> parameter : parameters.entrySet())
			{
				try
				{
					queryProcessor.bind(parameter.getKey(), parameter.getValue());
				}
				catch (final QueryException e)
				{
					throw new ApplicationException("Unable to bind variable name "
							+ parameter.getKey() + " with value " + parameter.getValue(),
							e);
				}
			}

			final Result result = queryProcessor.execute();
			resultString = result.toString();

		}
		catch (final QueryException e)
		{
			throw new ApplicationException("Unable to execute XQuery", e);
		}
		finally
		{
			if (queryProcessor != null)
			{
				queryProcessor.close();
			}

			context.close();
		}

		return resultString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoString(java.io.InputStream
	 * , java.io.InputStream)
	 */
	@Override
	public String executeIntoString(final InputStream xQuery, final InputStream inputXml)
	{
		return XQueryUtil.executionIntoStringNoParameters(xQuery, inputXml, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeAndProcess(java.io.InputStream
	 * , java.io.InputStream, edu.utah.further.core.xml.xquery.XQueryResultProcessor,
	 * java.util.Map)
	 */
	@Override
	public <T> T executeAndProcess(final InputStream xQuery, final InputStream inputXml,
			final XQueryResultProcessor<T> resultProcessor,
			final Map<String, String> parameters)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoStream(java.io.InputStream
	 * , java.io.InputStream, java.util.Map)
	 */
	@Override
	public XMLStreamReader executeIntoStream(final InputStream xQuery,
			final InputStream inputXml, final Map<String, String> parameters)
	{
		final String results = executeIntoString(xQuery, inputXml, parameters);

		try
		{
			return getXMLInputFactory().createXMLStreamReader(
					new ByteArrayInputStream(results.getBytes("UTF-8")));
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new ApplicationException("Unsupported encoding", e);
		}
		catch (final XMLStreamException e)
		{
			throw new ApplicationException("Unable to create XMLStreamReader", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoStream(java.io.InputStream
	 * , java.io.InputStream)
	 */
	@Override
	public XMLStreamReader executeIntoStream(final InputStream xQuery,
			final InputStream inputXml)
	{
		return XQueryUtil.executeIntoStreamNoParameters(xQuery, inputXml, this);
	}

	/**
	 * Returns an {@link XMLInputFactory} for creating {@link XMLStreamReader}s.
	 * 
	 * @return
	 */
	private XMLInputFactory getXMLInputFactory()
	{
		if (factory == null)
		{
			factory = XMLInputFactory.newInstance();
			factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		}

		return factory;
	}

}
