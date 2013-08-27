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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Utility methods for common XQuery operations. All methods wrap standard XQuery
 * exceptions with unchecked exceptions.
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
 * @version Sep 10, 2009
 */
@Utility
// Only sparsely used. Need to close connection at the end of XQuery processing, so hard
// to rely on a stateless utility class
public final class XQueryUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XQueryUtil.class);

	/**
	 * Standard parameter name to bind an XQuery program to.
	 */
	public static final String DOCUMENT_NAME = "docName";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor in utility class.
	 */
	private XQueryUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Helper method to retrieve an {@link XQConnection} from a {@link XQDataSource}
	 * 
	 * @param xqDataSource
	 *            the data source to retrieve the connection from
	 * @return an XQConnection
	 * @throws ApplicationException
	 *             if the connection could not be retrieved
	 */
	public static XQConnection getConnection(final XQDataSource xqDataSource)
	{
		try
		{
			return xqDataSource.getConnection();
		}
		catch (final XQException e)
		{
			throw new ApplicationException("Unable to get XQConnection", e);
		}
	}

	/**
	 * Helper method to close an {@link XQConnection}.
	 * 
	 * @param connection
	 *            the connection to close
	 * @throws ApplicationException
	 *             if the connection could not be close
	 */
	public static void closeConnection(final XQConnection connection)
	{
		try
		{
			connection.close();
		}
		catch (final XQException e)
		{
			throw new ApplicationException("Unable to close XQConnection", e);
		}
	}

	/**
	 * Helper method to retrieve {@link XQExpression} from {@link XQConnection}
	 * 
	 * @param xqConnection
	 *            the {@link XQConnection} to retrieve the expression from
	 * @return a XQExpression
	 * @throws ApplicationException
	 *             if an exception occurs while trying to create the expression
	 */
	public static XQExpression createExpression(final XQConnection xqConnection)
	{
		try
		{
			return xqConnection.createExpression();
		}
		catch (final XQException e)
		{
			throw new ApplicationException("Unable to get XQExpression", e);
		}
	}

	/**
	 * Helper method to retrieve a {@link XQExpression} from a {@link XQDataSource}
	 * 
	 * @param xqDataSource
	 *            the {@link XQDataSource} TO USE
	 * @return a XQExpression
	 * @throws ApplicationException
	 *             if an exception occurs while trying to create the expression
	 */
	public static XQExpression createExpression(final XQDataSource xqDataSource)
	{
		return createExpression(getConnection(xqDataSource));
	}

	/**
	 * Helper method to retrieve a {@link XQPreparedExpression} from a
	 * {@link XQDataSource} and a String of XQuery.
	 * 
	 * @param xqDataSource
	 *            the XQuery data source to use
	 * @param xquery
	 *            the XQuery program to create the {@link XQPreparedExpression} from
	 * @return a XQPreparedExpression
	 * @throws ApplicationException
	 *             if there is an error creating the prepared expression
	 */
	public static XQPreparedExpression createPreparedExpression(
			final XQDataSource xqDataSource, final String xquery)
	{
		return createPreparedExpression(xqDataSource,
				new ByteArrayInputStream(xquery.getBytes()));
	}

	/**
	 * Helper method to retrieve a {@link XQPreparedExpression} from a
	 * {@link XQDataSource} and a {@link InputStream} of XQuery.
	 * 
	 * @param xqDataSource
	 *            the XQuery data source to use
	 * @param inputStream
	 *            the XQuery program input stream to create the
	 *            {@link XQPreparedExpression} from
	 * @return a XQPreparedExpression
	 * @throws ApplicationException
	 *             if there is an error creating the prepared expression
	 */
	public static XQPreparedExpression createPreparedExpression(
			final XQDataSource xqDataSource, final InputStream inputStream)
	{
		try
		{
			return getConnection(xqDataSource).prepareExpression(inputStream);
		}
		catch (final XQException e)
		{
			throw new ApplicationException("Unable to get XQPreparedExpression", e);
		}
	}

	/**
	 * Helper method to execute a {@link XQPreparedExpression} and return an
	 * {@link OutputStream} of results. This method does not support setting write
	 * properties.
	 * 
	 * @param xqPreparedExpression
	 *            the expression to execute
	 * @return an output stream with the results
	 */
	public static ByteArrayOutputStream executePreparedExpression(
			final XQPreparedExpression xqPreparedExpression)
	{
		try
		{
			final XQResultSequence xqResultSequence = xqPreparedExpression.executeQuery();
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			xqResultSequence.writeSequence(outputStream, null);
			return outputStream;
		}
		catch (final XQException e)
		{
			throw new ApplicationException("Unable to execute prepared expression", e);
		}
	}

	/**
	 * Bind an XQuery program to the standardly-named {@link XQueryUtil#DOCUMENT_NAME}
	 * expression parameter.
	 * 
	 * @param expression
	 *            XQuery expression
	 * @param inputStream
	 *            XQuery program input stream
	 * @throws XQException
	 */
	public static void bindDocument(final XQExpression expression,
			final InputStream inputStream) throws XQException
	{
		bindDocument(expression, XQueryUtil.DOCUMENT_NAME, inputStream);
	}

	/**
	 * Bind an XQuery program to an expression parameter.
	 * 
	 * @param expression
	 *            XQuery expression
	 * @param parameterName
	 *            name of parameter to bind program to
	 * @param inputStream
	 *            XQuery program input stream
	 * @throws XQException
	 */
	public static void bindDocument(final XQExpression expression,
			final String parameterName, final InputStream inputStream) throws XQException
	{
		final XMLStreamReader xmlStreamReader = getXmlStreamReader(inputStream);
		expression.bindDocument(new QName(XQueryUtil.DOCUMENT_NAME), xmlStreamReader,
				null);
	}

	/**
	 * Bind parameter values to an XQuery expression.
	 * 
	 * @param expression
	 *            XQuery expression
	 * @param parameterMap
	 *            parameter key-value pairs
	 */
	public static void bindParameters(final XQExpression expression,
			final Map<String, String> parameterMap)
	{
		for (final String parameterName : parameterMap.keySet())
		{
			final String parameterValue = parameterMap.get(parameterName);
			try
			{
				expression.bindString(new QName(parameterName), parameterValue, null);
			}
			catch (final XQException ex)
			{
				throw new ApplicationException("Failed to bind XQuery parameters", ex);
			}
		}
	}

	/**
	 * Executes an XQuery with no binding parameters and an input XML into an output
	 * String.
	 * 
	 * @param xQuery
	 * @param inputXml
	 * @param xQueryService
	 * @return
	 */
	public static String executionIntoStringNoParameters(final InputStream xQuery,
			final InputStream inputXml, final XQueryService xQueryService)
	{
		return xQueryService.executeIntoString(xQuery, inputXml,
				Collections.<String, String> emptyMap());
	}

	/**
	 * Executes an XQuery with no binding parameters and an input XML into an output
	 * {@link XMLStreamReader}.
	 * 
	 * @param xQuery
	 * @param inputXml
	 * @param xQueryService
	 * @return
	 */
	public static XMLStreamReader executeIntoStreamNoParameters(final InputStream xQuery,
			final InputStream inputXml, final XQueryService xQueryService)
	{
		return xQueryService.executeIntoStream(xQuery, inputXml,
				Collections.<String, String> emptyMap());
	}

	/**
	 * Utility method to create an XMLStreamReader from an inputstream.
	 * 
	 * @param inputStream
	 * @return
	 */
	private static XMLStreamReader getXmlStreamReader(final InputStream inputStream)
	{
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = null;
		try
		{
			xmlStreamReader = factory.createXMLStreamReader(inputStream);
		}
		catch (final XMLStreamException e)
		{
			throw new ApplicationException("Unable to read XML", e);
		}
		return xmlStreamReader;
	}
}
