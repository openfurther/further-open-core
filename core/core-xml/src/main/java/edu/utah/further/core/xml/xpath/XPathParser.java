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
package edu.utah.further.core.xml.xpath;

import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlUtil;
import edu.utah.further.core.util.io.IoUtil;

/**
 * XPath parser of XML input sources.
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
 */
@Implementation
public final class XPathParser
{
	// ========================= CONSTANTS =================================

	/**
	 * XPath factory.
	 */
	private static final XPathFactory xpathFactory = XPathFactory.newInstance();

	// ========================= FIELDS ====================================

	/**
	 * XPath engine used to compile and evaluate expressions.
	 */
	private final XPath xpath = xpathFactory.newXPath();

	/**
	 * The XPath expression used in this parser.
	 * <p>
	 * TODO: For now, each parser instance handles one expression. Support and cache
	 * multiple expressions.
	 */
	private XPathExpression expr;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param xpathExpression
	 */
	public XPathParser(final String xpathExpression)
	{
		super();
		compileExpression(xpathExpression);
	}

	/**
	 * @param xpathExpression
	 * @param nsContext
	 */
	public XPathParser(final String xpathExpression, final NamespaceContext nsContext)
	{
		super();
		xpath.setNamespaceContext(nsContext);
		compileExpression(xpathExpression);
	}

	// ========================= IMPLEMENTATION: XmlResultParser ===========

	/**
	 * @param <T>
	 * @param xmlSource
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 * @throws XPathExpressionException
	 * @see edu.utah.further.core.xml.parser.XmlResultParser#parseXmlResults(java.io.InputStream)
	 */
	public <T> T parseXmlResults(final InputStream xmlSource) throws IOException,
			JAXBException
	{
		throw new ApplicationException(unsupportedOperationMessage("parseXmlResults()"));
	}

	// ========================= METHODS ===================================

	/**
	 * Evaluate an xpath expression.
	 *
	 * @param xmlSource
	 *            XML input stream
	 * @param returnType
	 *            XPath return type (node list/boolean/number/...)
	 * @return XPath result
	 * @throws ApplicatioException
	 *             upon document I/O error or XPath parse error
	 */
	public Object evaluateXPath(final InputStream xmlSource, final QName returnType)
	{
		try
		{
			final Document document = parseDocument(xmlSource);
			return expr.evaluate(document, returnType);
		}
		catch (final Throwable e)
		{
			throw new ApplicationException("Failed to evaluate XPath expression: ", e);
		}
	}
	
	/**
	 * Evaluate an xpath expression.
	 *
	 * @param xmlSource
	 *            XML input stream
	 * @param returnType
	 *            XPath return type (node list/boolean/number/...)
	 * @return XPath result
	 * @throws ApplicatioException
	 *             upon document I/O error or XPath parse error
	 */
	public Object evaluateXPath(final Node node, final QName returnType)
	{
		try
		{
			return expr.evaluate(node, returnType);
		}
		catch (final Throwable e)
		{
			throw new ApplicationException("Failed to evaluate XPath expression: ", e);
		}
	}

	/**
	 * Evaluate an xpath expression.
	 *
	 * @param xmlString
	 *            an XML document as a string; must be non-<code>null</code>
	 * @param returnType
	 *            XPath return type (node list/boolean/number/...)
	 * @return XPath result
	 * @throws ApplicatioException
	 *             upon document I/O error or XPath parse error
	 * @throws NullPointerException
	 *             if <code>xmlString = null</code>
	 */
	public Object evaluateXPath(final String xmlString, final QName returnType)
	{
		final InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
		final Object result = evaluateXPath(inputStream, returnType);
		IoUtil.closeSilently(inputStream);
		return result;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parse an XML input source using JAXP.
	 *
	 * @param xmlSource
	 *            XML input source
	 * @return DOM document node
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	protected Document parseDocument(final InputStream xmlSource)
			throws ParserConfigurationException, IOException, SAXException
	{
		final DocumentBuilder builder = XmlUtil
				.getDocumentBuilderFactory()
				.newDocumentBuilder();
		// Force re-encoding of the input source and parse
		return builder.parse(StringUtil.toUtf8(xmlSource));
	}

	/**
	 * @param xpathExpression
	 */
	private void compileExpression(final String xpathExpression)
	{
		try
		{
			expr = xpath.compile(xpathExpression);
		}
		catch (final XPathExpressionException e)
		{
			throw new ApplicationException("Invalid XPath expression: " + xpathExpression);
		}
	}
}
