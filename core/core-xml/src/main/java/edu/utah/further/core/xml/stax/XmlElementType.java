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

import java.util.Map;

import javax.xml.stream.XMLStreamConstants;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Mirrors {@link XMLStreamConstants} with type-safe enumerated constants.
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
 * @version May 10, 2010
 */
public enum XmlElementType
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Indicates an event is a start element
	 * 
	 * @see javax.xml.stream.events.StartElement
	 */
	START_ELEMENT(XMLStreamConstants.START_ELEMENT),

	/**
	 * Indicates an event is an end element
	 * 
	 * @see javax.xml.stream.events.EndElement
	 */
	END_ELEMENT(XMLStreamConstants.END_ELEMENT),

	/**
	 * Indicates an event is a processing instruction
	 * 
	 * @see javax.xml.stream.events.ProcessingInstruction
	 */
	PROCESSING_INSTRUCTION(XMLStreamConstants.PROCESSING_INSTRUCTION),

	/**
	 * Indicates an event is characters
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	CHARACTERS(XMLStreamConstants.CHARACTERS),

	/**
	 * Indicates an event is a comment
	 * 
	 * @see javax.xml.stream.events.Comment
	 */
	COMMENT(XMLStreamConstants.COMMENT),

	/**
	 * The characters are white space (see [XML], 2.10 "White Space Handling"). Events are
	 * only reported as SPACE if they are ignorable white space. Otherwise they are
	 * reported as CHARACTERS.
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	SPACE(XMLStreamConstants.SPACE),

	/**
	 * Indicates an event is a start document
	 * 
	 * @see javax.xml.stream.events.StartDocument
	 */
	START_DOCUMENT(XMLStreamConstants.START_DOCUMENT),

	/**
	 * Indicates an event is an end document
	 * 
	 * @see javax.xml.stream.events.EndDocument
	 */
	END_DOCUMENT(XMLStreamConstants.END_DOCUMENT),

	/**
	 * Indicates an event is an entity reference
	 * 
	 * @see javax.xml.stream.events.EntityReference
	 */
	ENTITY_REFERENCE(XMLStreamConstants.ENTITY_REFERENCE),

	/**
	 * Indicates an event is an attribute
	 * 
	 * @see javax.xml.stream.events.Attribute
	 */
	ATTRIBUTE(XMLStreamConstants.ATTRIBUTE),

	/**
	 * Indicates an event is a DTD
	 * 
	 * @see javax.xml.stream.events.DTD
	 */
	DTD(XMLStreamConstants.DTD),

	/**
	 * Indicates an event is a CDATA section
	 * 
	 * @see javax.xml.stream.events.Characters
	 */
	CDATA(XMLStreamConstants.CDATA),

	/**
	 * Indicates the event is a namespace declaration
	 * 
	 * @see javax.xml.stream.events.Namespace
	 */
	NAMESPACE(XMLStreamConstants.NAMESPACE),

	/**
	 * Indicates a Notation
	 * 
	 * @see javax.xml.stream.events.NotationDeclaration
	 */
	NOTATION_DECLARATION(XMLStreamConstants.NOTATION_DECLARATION),

	/**
	 * Indicates a Entity Declaration
	 * 
	 * @see javax.xml.stream.events.NotationDeclaration
	 */
	ENTITY_DECLARATION(XMLStreamConstants.ENTITY_DECLARATION);

	// ========================= CONSTANTS ====================================

	/**
	 * Caches the {@link XMLStreamConstants}-value-to-our-enum-type mapping.
	 */
	private static final Map<Integer, XmlElementType> instances = CollectionUtil.newMap();

	static
	{
		for (final XmlElementType instance : XmlElementType.values())
		{
			instances.put(new Integer(instance.getXmlStreamConstant()), instance);
		}
	}

	// ========================= FIELDS =======================================

	/**
	 * Namespace's name.
	 */
	private final int xmlStreamConstant;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Construct an XML element type.
	 * 
	 * @param name
	 *            header name
	 */
	private XmlElementType(final int name)
	{
		this.xmlStreamConstant = name;
	}

	// ========================= METHODS ======================================

	/**
	 * A factory method.
	 * 
	 * @param xmlStreamConstant
	 *            {@link XMLStreamConstants} constant value
	 * @return corresponding enum constant
	 */
	public static XmlElementType valueOf(final int xmlStreamConstant)
	{
		return instances.get(new Integer(xmlStreamConstant));
	}

	/**
	 * Return the xmlStreamConstant property.
	 * 
	 * @return the xmlStreamConstant
	 */
	public int getXmlStreamConstant()
	{
		return xmlStreamConstant;
	}
}
