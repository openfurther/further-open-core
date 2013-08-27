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

import static edu.utah.further.core.api.xml.XmlUtil.newSerializationPropertiesForPrintout;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQSequence;
import javax.xml.xquery.XQStaticContext;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * XQuery utility service implementation. Delegates to an underlying {@link XQDataSource}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<brS>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 4, 2010
 */
@Implementation
// Shying away from @Service to avoid auto-scanning in situations where there is no XQuery
// data source on the classpath.
@Qualifier("xqueryService")
public final class XQueryServiceXQJImpl implements XQueryService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XQueryServiceXQJImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates prototype XQuery data sources.
	 */
	@Autowired
	private XQueryDataSource xqueryDataSource;

	// ========================= FIELDS ====================================

	/**
	 * Data Source for querying.
	 */
	private XQDataSource dataSource;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @throws XQException
	 */
	@PostConstruct
	private void setUp()
	{
		if (dataSource == null)
		{
			dataSource = xqueryDataSource.newXQDataSource();
		}
	}

	// ========================= IMPL: XQueryDataSource ====================

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
		return xqueryDataSource.isSupportsInlineVariables();
	}

	// ========================= IMPL: XQueryService =======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoString(java.io.InputStream
	 * , java.io.InputStream)
	 */
	@Override
	public String executeIntoString(final InputStream xQuery, final InputStream inputXml,
			final Map<String, String> parameters)
	{
		return executeAndProcessResults(xQuery, inputXml, STRING_RESULT_FORMATTER,
				parameters);
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
	 * edu.utah.further.core.xml.xquery.XQueryService#executeIntoStream(java.io.InputStream
	 * , java.io.InputStream)
	 */
	@Override
	public XMLStreamReader executeIntoStream(final InputStream xQuery,
			final InputStream inputXml, final Map<String, String> parameters)
	{
		return executeAndProcessResults(xQuery, inputXml,
				XML_STREAM_READER_RESULT_FORMATTER, parameters);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.xquery.XQueryService#executeAndProcess(java.io.InputStream
	 * , java.io.InputStream, edu.utah.further.core.xml.xquery.XQueryResultProcessor)
	 */
	@Override
	public <T> T executeAndProcess(final InputStream xQuery, final InputStream inputXml,
			final XQueryResultProcessor<T> resultProcessor,
			final Map<String, String> parameters)
	{
		return executeAndProcessResults(xQuery, inputXml, resultProcessor, parameters);
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the xqueryDataSource property.
	 * 
	 * @param xqueryDataSource
	 *            the xqueryDataSource to set
	 */
	public void setXqueryDataSource(final XQueryDataSource xqueryDataSource)
	{
		this.xqueryDataSource = xqueryDataSource;
		this.dataSource = xqueryDataSource.newXQDataSource();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param results
	 * @throws XQException
	 */
	private static XQueryResultProcessor<String> STRING_RESULT_FORMATTER = new XQueryResultProcessor<String>()
	{
		/**
		 * @param results
		 * @throws XQException
		 * @see edu.utah.further.core.xml.xquery.XQueryResultProcessor#process(javax.xml.xquery.XQSequence)
		 */
		@Override
		public String process(final XQSequence results) throws XQException
		{
			final Properties serializationProps = newSerializationPropertiesForPrintout();
			final String result = results.getSequenceAsString(serializationProps);
			// if (log.isDebugEnabled())
			// {
			// log.debug("XQuery result:" + NEW_LINE_STRING + result);
			// }
			return result;
		}
	};

	/**
	 * @param results
	 * @throws XQException
	 */
	private static XQueryResultProcessor<XMLStreamReader> XML_STREAM_READER_RESULT_FORMATTER = new XQueryResultProcessor<XMLStreamReader>()
	{
		/**
		 * @param results
		 * @throws XQException
		 * @see edu.utah.further.core.xml.xquery.XQueryResultProcessor#process(javax.xml.xquery.XQSequence)
		 */
		@Override
		public XMLStreamReader process(final XQSequence results) throws XQException
		{
			return results.getSequenceAsStream();
		}
	};

	/**
	 * @param <T>
	 * @param xQuery
	 * @param inputXml
	 * @param resultFormatter
	 * @return
	 */
	private <T> T executeAndProcessResults(final InputStream xQuery,
			final InputStream inputXml, final XQueryResultProcessor<T> resultFormatter,
			final Map<String, String> parameters)
	{
		try
		{
			final XQConnection connection = XQueryUtil.getConnection(dataSource);
			final XQStaticContext context = createContext(connection);
			final XQExpression expression = connection.createExpression(context);
			XQueryUtil.bindDocument(expression, inputXml);
			XQueryUtil.bindParameters(expression, parameters);
			final XQSequence results = expression.executeQuery(xQuery);
			final T result = resultFormatter.process(results);
			XQueryUtil.closeConnection(connection);
			xQuery.close();
			inputXml.close();
			return result;
		}
		catch (final XQException e)
		{
			// log.error("Failed to execute XQuery, exception "
			// + ExceptionUtils.getFullStackTrace(e));
			throw new ApplicationException("Failed to execute XQuery", e);
		}
		catch (final IOException e)
		{
			throw new ApplicationException("I/O Error", e);
		}
	}

	/**
	 * @return
	 * @throws XQException
	 */
	private XQStaticContext createContext(final XQConnection connection)
			throws XQException
	{
		final XQStaticContext context = connection.getStaticContext();
		// TODO: builder pattern for this! e.g. add namespace bindings to builder.

		// context.declareNamespace("uml", "http://schema.omg.org/spec/UML/2.1");
		// context.declareNamespace("xmi", "http://schema.omg.org/spec/XMI/2.1");
		return context;
	}
}
