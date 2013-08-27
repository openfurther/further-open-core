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
package edu.utah.further.core.api.xml;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.xml.XmlNamespace.XMLNS_ATTRIBUTE;
import static edu.utah.further.core.api.xml.XmlNamespace.XMLNS_SCOPE;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * XML-related utils. Includes various XML tag generation methods. Used in server actions
 * processing AJAX requests.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 9, 2008
 */
@Utility
public final class XmlUtil
{
	// ========================= CONSTANTS =================================

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory
			.newInstance();

	/**
	 * Instantiates StAX readers.
	 */
	private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory
			.newInstance();

	static
	{
		DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true); // never forget this!!!
	}

	// ------------------------------------------------
	// XML Element syntax symbol conventions
	// ------------------------------------------------

	// Start of an element syntax:
	// ELEMENT_START_OPEN + element_name + ELEMENT_START_CLOSE
	public static final String ELEMENT_START_OPEN = "<";

	public static final String ELEMENT_START_CLOSE = ">";

	// End of an element syntax:
	// ELEMENT_END_OPEN + element_name + ELEMENT_END_CLOSE

	public static final String ELEMENT_END_OPEN = "</";

	public static final String ELEMENT_END_CLOSE = ">";

	// Empty element syntax:
	// ELEMENT_EMPTY_OPEN + element_name + ELEMENT_EMPTY_CLOSE

	public static final String ELEMENT_EMPTY_OPEN = "<";

	public static final String ELEMENT_EMPTY_CLOSE = "/>";

	/**
	 * Attribute syntax within an element start: <code>(ATTRIBUTE_DELIMITER +
	 * attribute_name + ATTRIBUTE_EQUALS + ATTRIBUTE_VALUE_ESCAPE_START +
	 * attribute_value + ATTRIBUTE_VALUE_ESCAPE_END)</code>.
	 */
	public static final String ATTRIBUTE_DELIMITER = " ";

	public static final String ATTRIBUTE_EQUALS = "=";

	public static final String ATTRIBUTE_VALUE_ESCAPE_START = "\"";

	public static final String ATTRIBUTE_VALUE_ESCAPE_END = "\"";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private XmlUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the document builder factory property.
	 * 
	 * @return the document builder factory
	 */
	public static DocumentBuilderFactory getDocumentBuilderFactory()
	{
		return DOCUMENT_BUILDER_FACTORY;
	}

	/**
	 * Print an XML opening tag with no attributes.
	 * 
	 * @param tag
	 *            tag name
	 * @return opening tag
	 */
	public static StringBuilder openTag(final String tag)
	{
		return new StringBuilder(ELEMENT_START_OPEN + tag + ELEMENT_START_CLOSE);
	}

	/**
	 * Print an XML opening tag with attributes.
	 * 
	 * @param tag
	 *            tag name
	 * @param attributes
	 *            list of attributes
	 * @return opening tag
	 */
	public static StringBuilder openTag(final String tag,
			final Map<String, String> attributes)
	{
		final StringBuilder s = new StringBuilder(ELEMENT_START_OPEN).append(tag);
		for (final Map.Entry<String, String> attribute : attributes.entrySet())
		{
			s.append(ATTRIBUTE_DELIMITER)
					.append(attribute.getKey())
					.append(ATTRIBUTE_EQUALS)
					.append(ATTRIBUTE_VALUE_ESCAPE_START)
					.append(attribute.getValue())
					.append(ATTRIBUTE_VALUE_ESCAPE_END);
		}
		s.append(ELEMENT_START_CLOSE);
		return s;
	}

	/**
	 * @param element
	 * @return
	 */
	public static StringBuilder openTag(final String namespace, final String schema,
			final String element)
	{
		final Map<String, String> attributes = CollectionUtil.newMap();
		attributes.put(getQualifiedElement(XMLNS_ATTRIBUTE, namespace), schema);
		return openTag(getQualifiedElement(namespace, element), attributes);
	}

	/**
	 * @param element
	 * @return
	 */
	public static StringBuilder closeTag(final String namespace, final String element)
	{
		return closeTag(getQualifiedElement(namespace, element));
	}

	/**
	 * Print an XML closing tag.
	 * 
	 * @param tag
	 *            tag name
	 * @return closing tag
	 */
	public static StringBuilder closeTag(final String tag)
	{
		return new StringBuilder(ELEMENT_END_OPEN).append(tag).append(ELEMENT_END_CLOSE);
	}

	/**
	 * Print an empty XML tag with attributes.
	 * 
	 * @param tag
	 *            tag name
	 * @return opening tag
	 */
	public static StringBuilder emptyTag(final String tag)
	{
		final StringBuilder s = new StringBuilder(ELEMENT_EMPTY_OPEN);
		s.append(tag);
		s.append(ELEMENT_EMPTY_CLOSE);
		return s;
	}

	/**
	 * Print an empty XML tag.
	 * 
	 * @param tag
	 *            tag name
	 * @param attributes
	 *            list of attributes
	 * @return opening tag
	 */
	public static StringBuilder emptyTag(final String tag,
			final Map<String, String> attributes)
	{
		final StringBuilder s = new StringBuilder(ELEMENT_EMPTY_OPEN).append(tag);
		for (final Map.Entry<String, String> attribute : attributes.entrySet())
		{
			s.append(ATTRIBUTE_DELIMITER);
			s.append(attribute.getKey());
			s.append(ATTRIBUTE_EQUALS);
			s.append(ATTRIBUTE_VALUE_ESCAPE_START);
			s.append(attribute.getValue());
			s.append(ATTRIBUTE_VALUE_ESCAPE_END);
		}
		s.append(ELEMENT_EMPTY_CLOSE);
		return s;
	}

	/**
	 * Print a full XML tag. If the body is empty, uses <code>emptyTag</code>.
	 * 
	 * @param tag
	 *            tag name
	 * @param body
	 *            tag body to place within the opening and closing tag.
	 * @return full tag
	 */
	public static StringBuilder fullTag(final String tag, final StringBuilder body)
	{
		if ((body == null) || body.length() == 0)
		{
			return emptyTag(tag);
		}
		final StringBuilder s = openTag(tag);
		s.append(body);
		s.append(closeTag(tag));
		return s;
	}

	/**
	 * Print an XML opening tag with attributes.
	 * 
	 * @param tag
	 *            tag name
	 * @param attributes
	 *            list of attributes
	 * @param body
	 *            tag body to place within the opening and closing tag.
	 * @return full tag
	 */
	public static StringBuilder fullTag(final String tag,
			final Map<String, String> attributes, final StringBuilder body)
	{
		if ((body == null) || body.length() == 0)
		{
			return emptyTag(tag, attributes);
		}
		final StringBuilder s = openTag(tag, attributes);
		s.append(body);
		s.append(closeTag(tag));
		return s;
	}

	/**
	 * Print a full XML tag. If the body is empty, uses <code>emptyTag</code>.
	 * 
	 * @param tag
	 *            tag name
	 * @param body
	 *            tag body to place within the opening and closing tag.
	 * @return full tag
	 */
	public static StringBuilder fullTag(final String tag, final String body)
	{
		return fullTag(tag, new StringBuilder(body));
	}

	/**
	 * Print an XML opening tag with attributes.
	 * 
	 * @param tag
	 *            tag name
	 * @param attributes
	 *            list of attributes
	 * @param body
	 *            tag body to place within the opening and closing tag.
	 * @return full tag
	 */
	public static StringBuilder fullTag(final String tag,
			final Map<String, String> attributes, final String body)
	{
		return fullTag(tag, attributes, new StringBuilder(body));
	}

	/**
	 * Create a new DOM document.
	 * 
	 * @return an empty DOM Document
	 */
	public static Document createDomDocument()
	{
		try
		{
			final DocumentBuilder builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
			final Document doc = builder.newDocument();
			return doc;
		}
		catch (final ParserConfigurationException e)
		{
		}
		return null;
	}

	/**
	 * Create a new DOM document from an XML string.
	 * 
	 * @param xmlString
	 *            XML string
	 * @return an empty DOM Document
	 */
	public static Document createDomDocument(final String xmlString)
	{
		try
		{
			final DocumentBuilder builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
			// Closing a ByteArrayInputStream has no effect.
			return builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Unable to create DOM document", e);
		}

	}

	/**
	 * Return a qualified namespace attribute String.
	 * 
	 * @param ns
	 *            unqualified namespace, e.g. &quot;ns&quot;
	 * @return qualified namespace attribute string, e.g. &quot;xlmns:ns&quot;
	 */
	public static String getQualifiedNamespace(final String ns)
	{
		return XMLNS_ATTRIBUTE + XMLNS_SCOPE + ns;
	}

	/**
	 * Return a qualified XML element name.
	 * 
	 * @param ns
	 *            XML namespace, e.g. &quot;ns&quot;
	 * @param element
	 *            unqualified element name, e.g. &quot;element&quot;
	 * @return qualified element name e.g. &quot;ns:element&quot;
	 */
	public static String getQualifiedElement(final String ns, final String element)
	{
		return ns + XMLNS_SCOPE + element;
	}

	/**
	 * @param stream
	 * @return
	 */
	public static XMLStreamReader newXmlStreamReader(final InputStream stream)
	{
		try
		{
			return XML_INPUT_FACTORY.createXMLStreamReader(stream);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new ApplicationException("Failed to convert stream to XML stream", e);
		}
	}

	/**
	 * Basic StAX element printout. For more sophisticated functionality, use
	 * {@link XmlStreamPrinter}.
	 * 
	 * @param reader
	 *            reader
	 * @return reader's next element textual representation
	 */
	public static String getEventSimpleString(final XMLStreamReader reader)
	{
		switch (reader.getEventType())
		{
			case XMLStreamConstants.START_ELEMENT:
				return "START_ELEMENT:\t\"" + reader.getLocalName() + "\"";
			case XMLStreamConstants.END_ELEMENT:
				return "END_ELEMENT:\t\"" + reader.getLocalName() + "\"";
			case XMLStreamConstants.START_DOCUMENT:
				return "START_DOCUMENT";
			case XMLStreamConstants.END_DOCUMENT:
				return "END_DOCUMENT";
			case XMLStreamConstants.CHARACTERS:
				return "CHARACTERS:\t\"" + reader.getText() + "\"" + " blank? "
						+ StringUtils.isWhitespace(reader.getText());
			case XMLStreamConstants.SPACE:
				return "SPACE:\t\"" + reader.getText() + "\"";
			default:
				return "EVENT:\t" + reader.getEventType();
		}
	}

	/**
	 * @param os
	 * @param xmlReader
	 * @param printAttributes
	 */
	public static void printElement(final PrintStream os,
			final XMLStreamReader xmlReader, final boolean printAttributes)
	{
		os.print("<");
		printName(os, xmlReader);
		printNamespaces(os, xmlReader);
		if (printAttributes)
		{
			printAttributes(os, xmlReader);
		}
		os.print(">");
	}

	/**
	 * @param xmlReader
	 */
	public static void printLiterally(final PrintStream os,
			final XMLStreamReader xmlReader)
	{
		final int start = xmlReader.getTextStart();
		final int length = xmlReader.getTextLength();
		os.print(new String(xmlReader.getTextCharacters(), start, length));
	}

	/**
	 * @param xmlReader
	 * @return
	 */
	public static String getName(final XMLStreamReader xmlReader)
	{
		if (xmlReader.hasName())
		{
			final String prefix = xmlReader.getPrefix();
			final String uri = xmlReader.getNamespaceURI();
			final String localName = xmlReader.getLocalName();
			return getName(prefix, uri, localName);
		}
		return null;
	}

	/**
	 * @param xmlReader
	 */
	public static void printEndElement(final PrintStream os,
			final XMLStreamReader xmlReader)
	{
		os.print("</");
		printName(os, xmlReader);
		os.print(">");
	}

	/**
	 * Return XQuery XML serialization properties for pretty XML output formatting.
	 * 
	 * @return XML serialization properties for printouts
	 */
	public static Properties newSerializationPropertiesForPrintout()
	{
		final Properties serializationProps = new Properties();
		// Make sure we output in XML format
		serializationProps.setProperty("method", "xml");
		// Pretty printing
		serializationProps.setProperty("indent", "yes");
		return serializationProps;
	}

	/**
	 * @param xmlReader
	 */
	private static void printName(final PrintStream os, final XMLStreamReader xmlReader)
	{
		if (xmlReader.hasName())
		{
			os.print(getName(xmlReader));
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param prefix
	 * @param uri
	 * @param localName
	 * @return
	 */
	private static String getName(final String prefix, final String uri,
			final String localName)
	{
		String name = "";
		// if (StringUtils.isNotBlank(uri))
		// name += "[" + quote(uri) + "]:";
		if (StringUtils.isNotBlank(prefix))
			name += prefix + ":";
		if (localName != null)
			name += localName;
		return name;
	}

	/**
	 * @param xmlReader
	 */
	private static void printAttributes(final PrintStream os,
			final XMLStreamReader xmlReader)
	{
		for (int i = 0; i < xmlReader.getAttributeCount(); i++)
		{
			printAttribute(os, xmlReader, i);
		}
	}

	/**
	 * @param xmlReader
	 * @param index
	 */
	private static void printAttribute(final PrintStream os,
			final XMLStreamReader xmlReader, final int index)
	{
		final String prefix = xmlReader.getAttributePrefix(index);
		final String namespace = xmlReader.getAttributeNamespace(index);
		final String localName = xmlReader.getAttributeLocalName(index);
		final String value = xmlReader.getAttributeValue(index);
		os.print(Strings.SPACE_STRING);
		os.print(getName(prefix, namespace, localName));
		os.print("=" + quote(value));
	}

	/**
	 * @param xmlReader
	 */
	private static void printNamespaces(final PrintStream os,
			final XMLStreamReader xmlReader)
	{
		for (int i = 0; i < xmlReader.getNamespaceCount(); i++)
		{
			printNamespace(os, xmlReader, i);
		}
	}

	/**
	 * @param xmlReader
	 * @param index
	 */
	private static void printNamespace(final PrintStream os,
			final XMLStreamReader xmlReader, final int index)
	{
		final String prefix = xmlReader.getNamespacePrefix(index);
		final String uri = xmlReader.getNamespaceURI(index);
		os.print(Strings.SPACE_STRING);
		if (StringUtils.isBlank(prefix))
		{
			os.print("xmlns=" + quote(uri));
		}
		else
		{
			os.print("xmlns:" + prefix + "=" + quote(uri));
		}
	}
}
