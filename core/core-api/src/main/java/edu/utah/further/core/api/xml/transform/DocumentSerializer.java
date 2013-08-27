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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import edu.utah.further.core.api.context.Api;

/**
 * An XML {@link Document} serializer. Usually used to indent or pretty-print a DOM.
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
 * @version Oct 19, 2008
 */
@Api
public interface DocumentSerializer
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Pretty print an XML document using the Java transform API.
	 *
	 * @param doc
	 *            DOM document root node
	 * @param out
	 *            output stream to output to
	 */
	void serialize(Node doc, OutputStream out);

	/**
	 * Return the indent property.
	 *
	 * @return the indent
	 */
	boolean isIndent();

	/**
	 * Set a new value for the indent property.
	 *
	 * @param indent
	 *            the indent to set
	 */
	void setIndent(boolean indent);

	/**
	 * Return the indentAmount property.
	 *
	 * @return the indentAmount
	 */
	int getIndentAmount();

	/**
	 * Set a new value for the indentAmount property.
	 *
	 * @param indentAmount
	 *            the indentAmount to set
	 */
	void setIndentAmount(int indentAmount);

	/**
	 * Return the printElementXmlXlmns property.
	 *
	 * @return the printElementXmlXlmns
	 */
	boolean isPrintElementXmlXlmns();

	/**
	 * Set a new value for the printElementXmlXlmns property.
	 *
	 * @param printElementXmlXlmns
	 *            the printElementXmlXlmns to set
	 */
	void setPrintElementXmlXlmns(boolean printElementXmlXlmns);
}