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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.w3c.dom.Node;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * XML transofmration utilities.
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
 * @version Jul 9, 2010
 */
@Utility
public final class XmlTransformUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Pretty-prints DOM documents.
	 */
	private static final DocumentSerializer DEFAULT_XML_WRITER = new TransformerDocumentSerializer();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private XmlTransformUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Pretty-print an XML document to string using a default writer.
	 *
	 * @param document
	 *            root DOM node of a document
	 * @return string representation of the document
	 */
	public static String printToString(final Node document)
	{
		return printToString(document, DEFAULT_XML_WRITER);
	}

	/**
	 * Pretty-print an XML document to string using a particular writer.
	 *
	 * @param document
	 *            root DOM node of a document
	 * @param xmlWriter
	 *            writer
	 * @return string representation of the document
	 */
	public static String printToString(final Node document,
			final DocumentSerializer xmlWriter)
	{
		try (final OutputStream out = new ByteArrayOutputStream())
		{
			xmlWriter.setIndent(true);
			xmlWriter.setIndentAmount(2);
			xmlWriter.serialize(document, out);
			final String xmlString = out.toString();
			return xmlString;
		}
		catch (final IOException e)
		{
			throw new ApplicationException(
					"Problem opening byte array output stream - we shouldn't be here", e);
		}
	}
}
