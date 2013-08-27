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
package edu.utah.further.core.xml.stax;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * A visitor of an XML stream. Each method is typically invoked upon receiving a StAX
 * event with corresponding {@link XMLStreamConstants} type.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version May 7, 2010
 */
public interface XmlStreamVisitor
{
	// ========================= METHODS ===================================

	/**
	 * Visit an entire XML document.
	 * 
	 * @param xmlr
	 *            XML document stream reader
	 */
	void visit(XMLStreamReader xmlr);

	/**
	 * Visit an XML element start.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitStartElement(XMLStreamReader xmlr);

	/**
	 * Visit an XML element end.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitEndElement(XMLStreamReader xmlr);

	/**
	 * Visit a processing instruction.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitProcessingInstruction(XMLStreamReader xmlr);

	/**
	 * Visit an XML element body.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitCharacters(XMLStreamReader xmlr);

	/**
	 * Visit an XML comment.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitComment(XMLStreamReader xmlr);

	/**
	 * Visit an XML white space section.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitSpace(XMLStreamReader xmlr);

	/**
	 * Visit an XML document start.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitStartDocument(XMLStreamReader xmlr);

	/**
	 * Visit an XML document end.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitEndDocument(XMLStreamReader xmlr);

	/**
	 * Visit an entity reference (e.g. <code>&#170;</code>).
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitEntityReference(XMLStreamReader xmlr);

	/**
	 * Visit an XML element attribute.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitAttribute(XMLStreamReader xmlr);

	/**
	 * Visit an XML DTD element.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitDtd(XMLStreamReader xmlr);

	/**
	 * Visit an XML element literal CDATA section.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitCdata(XMLStreamReader xmlr);

	/**
	 * Visit an XML notation declaration.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitNotationDeclaration(XMLStreamReader xmlr);

	/**
	 * Visit an XML namespace declaration.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitNamespace(XMLStreamReader xmlr);

	/**
	 * Visit an XML entity declaration.
	 * 
	 * @param xmlr
	 *            XML stream to visit
	 */
	void visitEntityDeclaration(XMLStreamReader xmlr);
}