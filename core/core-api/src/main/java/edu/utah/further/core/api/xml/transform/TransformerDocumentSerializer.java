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

import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import edu.utah.further.core.api.constant.Strings;

/**
 * Uses the Transformer API to serialize a DOM.
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
public final class TransformerDocumentSerializer extends AbstractDocumentSerializer
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: DocumentSerializer ========

	/**
	 * Pretty print an XML document using the Java transform API.
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
		try
		{
			final TransformerFactory tfactory = TransformerFactory.newInstance();
			final Transformer serializer = tfactory.newTransformer();

			// Setup indenting to "pretty print"
			// serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.INDENT, isIndent() ? "yes" : "no");
			if (isIndent())
			{
				try
				{
					serializer.setOutputProperty(
							"{http://xml.apache.org/xslt}indent-amount",
							Strings.EMPTY_STRING + getIndentAmount());
				}
				catch (final IllegalArgumentException e)
				{
					// if (log.isInfoEnabled())
					// {
					// log.info("indent-amount property not supported by"
					// + " the transformer implementation: "
					// + serializer.getClass() + ", ignoring");
					// }
				}
			}

			// Transform the document
			final DOMSource xmlSource = new DOMSource(doc);
			serializer.transform(xmlSource, new StreamResult(out));
		}
		catch (final TransformerException e)
		{
			// this is fatal, just dump the stack and throw a runtime exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
