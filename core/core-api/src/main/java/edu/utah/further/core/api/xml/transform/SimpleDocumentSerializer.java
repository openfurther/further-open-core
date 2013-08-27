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
package edu.utah.further.core.api.xml.transform;

import static edu.utah.further.core.api.text.StringUtil.quote;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Uses {@link XmlDocumentWriter} to serialize a DOM.
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
 * @version Aug 14, 2009
 */
public final class SimpleDocumentSerializer extends AbstractDocumentSerializer
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: DocumentSerializer ========

	/**
	 * Pretty print an XML document using {@link XmlDocumentWriter}.
	 *
	 * @param doc
	 *            DOM document root node
	 * @param out
	 *            output stream to output to
	 * @throws Exception
	 * @see http://faq.javaranch.com/java/HowToPrettyPrintXmlWithJava
	 * @see edu.utah.further.core.api.xml.transform.DocumentSerializer#serialize(org.w3c.dom.Node,
	 *      java.io.OutputStream)
	 */
	@Override
	public void serialize(final Node doc, final OutputStream out)
	{
		try (final PrintWriter writer = new PrintWriter(out)) {
			write(writer, doc, getIndentString());
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Output the specified DOM Node object, printing it using the specified indentation
	 * string
	 *
	 * @param writer
	 * @param node
	 * @param indent
	 */
	public void write(final PrintWriter writer, final Node node, final String indent)
	{
		// The output depends on the type of the node
		switch (node.getNodeType())
		{
			case Node.DOCUMENT_NODE:
			{ // If its a Document node
				final Document doc = (Document) node;
				writer.println(indent + "<?xml version='1.0'?>"); // Output header
				Node child = doc.getFirstChild(); // Get the first node
				while (child != null)
				{ // Loop 'till no more nodes
					write(writer, child, indent); // Output node
					child = child.getNextSibling(); // Get next node
				}
				break;
			}
			case Node.DOCUMENT_TYPE_NODE:
			{ // It is a <!DOCTYPE> tag
				final DocumentType doctype = (DocumentType) node;
				// Note that the DOM Level 1 does not give us information about
				// the the public or system ids of the doctype, so we can't output
				// a complete <!DOCTYPE> tag here. We can do better with Level 2.
				writer.println("<!DOCTYPE " + doctype.getName() + ">");
				break;
			}
			case Node.ELEMENT_NODE:
			{ // Most nodes are Elements
				final Element elt = (Element) node;
				writer.print(indent + "<" + elt.getTagName()); // Begin start tag
				final NamedNodeMap attrs = elt.getAttributes(); // Get attributes
				for (int i = 0; i < attrs.getLength(); i++)
				{ // Loop through them
					final Node a = attrs.item(i);
					final String attributeName = a.getNodeName();
					if (isPrintElementXmlXlmns() || !XMLNS_XML.equals(attributeName))
					{
						writer.print(" " + attributeName + "=" + // Print attr. name
								quote(fixup(a.getNodeValue()))); // Print attr. value
					}
				}
				writer.println(">"); // Finish start tag

				final String newindent = indent + "    "; // Increase indent
				Node child = elt.getFirstChild(); // Get child
				while (child != null)
				{ // Loop
					write(writer, child, newindent); // Output child
					child = child.getNextSibling(); // Get next child
				}

				writer.println(indent + "</" + // Output end tag
						elt.getTagName() + ">");
				break;
			}
			case Node.TEXT_NODE:
			{ // Plain text node
				final Text textNode = (Text) node;
				final String text = textNode.getData().trim(); // Strip off space
				if ((text != null) && text.length() > 0) // If non-empty
					writer.println(indent + fixup(text)); // print text
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE:
			{ // Handle PI nodes
				final ProcessingInstruction pi = (ProcessingInstruction) node;
				writer
						.println(indent + "<?" + pi.getTarget() + " " + pi.getData()
								+ "?>");
				break;
			}
			case Node.ENTITY_REFERENCE_NODE:
			{ // Handle entities
				writer.println(indent + "&" + node.getNodeName() + ";");
				break;
			}
			case Node.CDATA_SECTION_NODE:
			{ // Output CDATA sections
				final CDATASection cdata = (CDATASection) node;
				// Careful! Don't put a CDATA section in the program itself!
				writer.println(indent + "<" + "![CDATA[" + cdata.getData() + "]]" + ">");
				break;
			}
			case Node.COMMENT_NODE:
			{ // Comments
				final Comment c = (Comment) node;
				writer.println(indent + "<!--" + c.getData() + "-->");
				break;
			}
			default: // Hopefully, this won't happen too much!
			{
				// log.error("Ignoring node: " + node.getClass().getName());
				break;
			}
		}
	}

	// This method replaces reserved characters with entities.
	private String fixup(final String s)
	{
		final StringBuilder sb = StringUtil.newStringBuilder();
		final int len = s.length();
		for (int i = 0; i < len; i++)
		{
			final char c = s.charAt(i);
			switch (c)
			{
				default:
					sb.append(c);
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&apos;");
					break;
			}
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	private String getIndentString()
	{
		if (isIndent())
		{
			return Strings.EMPTY_STRING;
		}
		final StringBuilder indent = StringUtil.newStringBuilder();
		for (int i = 0; i < getIndentAmount(); i++)
		{
			indent.append(Strings.SPACE_STRING);
		}
		return indent.toString();
	}
}
