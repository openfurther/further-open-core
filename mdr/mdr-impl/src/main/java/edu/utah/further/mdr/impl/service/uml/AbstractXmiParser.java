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
package edu.utah.further.mdr.impl.service.uml;

import static edu.utah.further.core.api.constant.Strings.DEFAULT_ENCODING;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStream;
import static edu.utah.further.core.api.message.Severity.ERROR;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.xml.XmlUtil.newSerializationPropertiesForPrintout;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import org.codehaus.staxmate.dom.DOMConverter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.util.message.SeverityMessageContainerImpl;
import edu.utah.further.core.xml.xquery.XQueryDataSource;
import edu.utah.further.core.xml.xquery.XQueryService;
import edu.utah.further.dts.api.annotation.DtsTransactional;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.service.uml.XmiParser;
import edu.utah.further.mdr.api.service.uml.XmiParserOptions;

/**
 * A base class for XMI parser implementations.
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
@Implementation
abstract class AbstractXmiParser implements XmiParser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractXmiParser.class);

	/**
	 * A function pointer for transforming lines in the xquery file.
	 */
	protected interface LineTransformer
	{
		String transform(String line);
	}

	// ========================= FIELDS ====================================

	/**
	 * Points to an XQuery document to execute.
	 */
	private InputStream queryIs;

	/**
	 * Points to an XMI document to parser.
	 */
	private InputStream xmiIs;

	/**
	 * A list of error messages.
	 */
	private final SeverityMessageContainer messages = new SeverityMessageContainerImpl();

	/**
	 * XMI Parser options.
	 */
	protected XmiParserOptions options = new XmiParserOptionsImpl();

	/**
	 * List of line transformers to apply in {@link #fixXmiFile(InputStream)}.
	 */
	private final List<LineTransformer> lineTransformers = CollectionUtil.newList();

	// ========================= DEPENDENCIES ==============================

	/**
	 * Instantiates prototype XQuery data sources.
	 */
	@Autowired
	private XQueryService xQueryService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 *
	 */
	public AbstractXmiParser()
	{
		super();
		// Line transformers common to all XMI parser implementations
		addLineTransformer(DECLARE_VARIABLE_LINE_TRANSFORMER);
	}

	// ========================= IMPLEMENTATION: XmiParser =================

	/**
	 * Parse UML model from XMI file.
	 * 
	 * @param queryResourceName
	 *            XQuery file resource name
	 * @param xmiResourceName
	 *            XMI file resource name
	 * @return the output UML model
	 * @see edu.utah.further.dts.api.service.uml.XmiParser#parse(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@DtsTransactional
	public UmlModel parse(final String queryResourceName, final String xmiResourceName)
	{
		try
		{
			messages.clearMessages();
			xmiIs = loadResource(xmiResourceName);
			final UmlModel model = setupXQueryAndLoadModelAndTearDown(queryResourceName);
			xmiIs.close();
			return model;
		}
		catch (final Exception e)
		{
			addMessage(ERROR, "Failed to parse XMI file " + quote(xmiResourceName) + ": "
					+ e.getMessage());
			return null;
		}
	}

	/**
	 * Parse UML model from an XMI input stream.
	 * 
	 * @param queryResourceName
	 *            XQuery file resource name
	 * @param xmiInputStream
	 *            XMI file resource input stream
	 * @return the output UML model
	 * @see edu.utah.further.dts.api.service.uml.XmiParser#parse(java.lang.String,
	 *      java.io.InputStream)
	 */
	@Override
	@DtsTransactional
	public UmlModel parse(final String queryResourceName, final InputStream xmiInputStream)
	{
		try
		{
			messages.clearMessages();
			this.xmiIs = xmiInputStream;
			final UmlModel model = setupXQueryAndLoadModelAndTearDown(queryResourceName);
			// Do NOT close the input stream; it is the client's responsibility and might
			// be reused after returning from this method.
			return model;
		}
		catch (final Exception e)
		{
			addMessage(ERROR, "Failed to parse XMI input stream: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Return the message container.
	 * 
	 * @return the message container
	 */
	@Override
	public SeverityMessageContainer getMessages()
	{
		return messages;
	}

	/**
	 * Return the options property.
	 * 
	 * @return the options
	 */
	@Override
	public XmiParserOptions getOptions()
	{
		return options;
	}

	/**
	 * Set a new value for the options property.
	 * 
	 * @param options
	 *            the options to set
	 */
	@Override
	public void setOptions(final XmiParserOptions options)
	{
		this.options = options;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns an {@link Element} after parsing the results of the {@link XMLStreamReader}
	 * 
	 * @return
	 */
	protected Element getRootElement(final XMLStreamReader xmlStreamReader)
	{
		try
		{
			final Document doc = new DOMConverter().buildDocument(xmlStreamReader);
			return doc.getDocumentElement();
		}
		catch (final XMLStreamException e)
		{
			throw new ApplicationException("Unable to convert XML Stream to DOM");
		}
	}

	/**
	 * Parse the XML results.
	 * 
	 * @param results
	 * @return UML model
	 * @throws XMLStreamException
	 */
	abstract protected UmlModel parseResults(final XMLStreamReader results)
			throws XMLStreamException;

	// /**
	// * Return the XMI version supported by this parser.
	// *
	// * @return the XMI version supported by this parser
	// */
	// abstract protected XmiVersion getXmiVersion();

	/**
	 * Fix XMI file. Also, re-encode an input stream in UTF-8 encoding. A hook.
	 * 
	 * @param originalXmi
	 *            original XMI input source
	 * @return fixed XMI input source
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	// Calling code closes resource stream
	protected final InputStream fixXmiFile(final InputStream originalXmi)
			throws IOException
	{
		if (lineTransformers.isEmpty())
		{
			// No transformers, return original file
			return originalXmi;
		}
		IOException caught;
		BufferedReader inputStream = null;
		InputStream fixedXmi = null;
		// final int headLines = 5;
		try
		{
			inputStream = new BufferedReader(new InputStreamReader(originalXmi));
			final StringBuilder outputBuilder = StringUtil.newStringBuilder();
			// int lineNumber = 0;
			String line;
			while ((line = inputStream.readLine()) != null)
			{
				// lineNumber++;
				// if (lineNumber < headLines)
				// {
				for (final LineTransformer lineTransformer : lineTransformers)
				{
					line = lineTransformer.transform(line);
				}
				// }
				outputBuilder.append(line).append(NEW_LINE_STRING);
			}
			final byte[] bytes = outputBuilder.toString().getBytes(DEFAULT_ENCODING);
			// if (log.isDebugEnabled())
			// {
			// log.debug("Fixed XMI file stream:\n" + new String(bytes));
			// }
			fixedXmi = new ByteArrayInputStream(bytes);
			return fixedXmi;
		}
		catch (final IOException e)
		{
			caught = e;
		}
		finally
		{
			// Closing the original input stream is the calling code's responsibility
			// if (inputStream != null)
			// {
			// inputStream.close();
			// }
		}
		throw new IOException(caught);
	}

	/**
	 * @throws XQException
	 */
	protected final void setupXQuery(final String queryResourceName) throws XQException
	{
		queryIs = loadResource(queryResourceName);
	}

	/**
	 * @throws XQException
	 * @throws IOException
	 */
	protected final void tearDown() throws XQException, IOException
	{
		queryIs.close();
	}

	/**
	 * Add a line transformer at the end of the transformer list.
	 * 
	 * @param lineTransformer
	 *            transformer to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	protected final boolean addLineTransformer(final LineTransformer lineTransformer)
	{
		return lineTransformers.add(lineTransformer);
	}

	/**
	 * Support both BaseX and SAXON-B/DataDirect XQJ implementations. More generally, if
	 * the {@link XQueryDataSource} supports inline variable declarations, allow them in
	 * the query file. Otherwise, this transformer comments out these lines.
	 */
	private final LineTransformer DECLARE_VARIABLE_LINE_TRANSFORMER = new LineTransformer()
	{
		@Override
		public String transform(final String line)
		{
			return (!xQueryService.isSupportsInlineVariables() && line.trim().startsWith(
					"declare variable ")) ? "(: " + line + " :)" : line;
		}
	};

	/**
	 * @param resourceName
	 * @return
	 */
	private static InputStream loadResource(final String resourceName)
	{
		final InputStream is = getResourceAsStream(resourceName);
		if (is == null)
		{
			throw new ApplicationException("Could not find resource "
					+ quote(resourceName));
		}
		return is;
	}

	/**
	 * Extract DTS information from the XMI resource {@link #resourceName}. A template
	 * method.
	 * 
	 * @return the output UML model
	 * @throws XQException
	 */
	protected final UmlModel loadModel() throws XQException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Loading UmlModel");
		}

		try
		{
			return parseResults(xQueryService.executeIntoStream(queryIs, xmiIs));
		}
		catch (final XMLStreamException e)
		{
			throw new ApplicationException("Unable to load model", e);
		}
	}

	/**
	 * @param severity
	 * @param text
	 * @see java.util.List#add(java.lang.Object)
	 * @return
	 */
	protected final void addMessage(final Severity severity, final String text)
	{
		messages.addMessage(severity, text);
	}

	/**
	 * @param container
	 * @return
	 */
	protected final void addMessages(final SeverityMessageContainer container)
	{
		messages.addMessages(container);
	}

	/**
	 * @return
	 */
	protected final InputStream getQueryFileResource(final String fileName)
	{
		final InputStream is = getResourceAsStream(fileName);
		if (is == null)
		{
			throw new ApplicationException("Could not find xquery file "
					+ quote(fileName));
		}
		return is;
	}

	/**
	 * @param result
	 * @throws XQException
	 */
	@SuppressWarnings("unused")
	private String printResultToString(final XQResultSequence result) throws XQException
	{
		final Properties serializationProps = newSerializationPropertiesForPrintout();
		final String xmlString = result.getSequenceAsString(serializationProps);
		// Note: BaseX's getSequenceAsString() implementation ignores the serialization
		// properties, so don't expect much.
		return xmlString;
	}

	/**
	 * @param queryResourceName
	 * @return
	 * @throws XQException
	 * @throws IOException
	 */
	private UmlModel setupXQueryAndLoadModelAndTearDown(final String queryResourceName)
			throws XQException, IOException
	{
		xmiIs = fixXmiFile(xmiIs);
		setupXQuery(queryResourceName);
		final UmlModel model = loadModel();
		tearDown();
		return model;
	}
}