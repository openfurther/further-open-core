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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A XML document visitor stub implementation.
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
public class XmlStreamVisitorStub implements XmlStreamVisitor
{
	// ========================= Impl: XmlStreamVisitor ====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visit(javax.xml.stream.XMLStreamReader
	 * )
	 */
	@Override
	public void visit(final XMLStreamReader xmlReader)
	{
		try
		{
			while (xmlReader.hasNext())
			{
				visitEvent(xmlReader);
				xmlReader.next();
			}
			xmlReader.close();
		}
		catch (final XMLStreamException e)
		{
			e.printStackTrace();
			throw new ApplicationException("Error print StAX stream", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitAttribute(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitAttribute(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.utah.further.core.xml.stax.XmlStreamVisitor#visitCdata(javax.xml.stream.
	 * XMLStreamReader)
	 */
	@Override
	public void visitCdata(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitCharacters(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitCharacters(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.utah.further.core.xml.stax.XmlStreamVisitor#visitComment(javax.xml.stream.
	 * XMLStreamReader)
	 */
	@Override
	public void visitComment(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.utah.further.core.xml.stax.XmlStreamVisitor#visitDtd(javax.xml.stream.
	 * XMLStreamReader)
	 */
	@Override
	public void visitDtd(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEndDocument(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitEndDocument(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEndElement(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitEndElement(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEntityDeclaration(javax.xml
	 * .stream.XMLStreamReader)
	 */
	@Override
	public void visitEntityDeclaration(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEntityReference(javax.xml.
	 * stream.XMLStreamReader)
	 */
	@Override
	public void visitEntityReference(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitNamespace(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitNamespace(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitNotationDeclaration(javax.
	 * xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitNotationDeclaration(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitProcessingInstruction(javax
	 * .xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitProcessingInstruction(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.utah.further.core.xml.stax.XmlStreamVisitor#visitSpace(javax.xml.stream.
	 * XMLStreamReader)
	 */
	@Override
	public void visitSpace(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitStartDocument(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitStartDocument(final XMLStreamReader xmlReader)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitor#visitStartElement(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitStartElement(final XMLStreamReader xmlReader)
	{

	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Process a single XML document event -- a standard switch template.
	 * <p>
	 * Using the integer constants directly here, as in StAX's API design, should be
	 * faster than encapsulating the different XMLStreamConstants with type-safe enum and
	 * using the virtual function table to override a single <code>visit()</code> method.
	 * <p>
	 * On the other hand, the API is slightly more cumbersome, having different method
	 * names for different {@link XMLStreamConstants} types.
	 * 
	 * @param xmlReader
	 *            XML document stream reader
	 */
	protected void visitEvent(final XMLStreamReader xmlReader)
	{
		switch (xmlReader.getEventType())
		{
			case XMLStreamConstants.START_ELEMENT:
			{
				visitStartElement(xmlReader);
				break;
			}

			case XMLStreamConstants.END_ELEMENT:
			{
				visitEndElement(xmlReader);
				break;
			}

			case XMLStreamConstants.PROCESSING_INSTRUCTION:
			{
				visitProcessingInstruction(xmlReader);
				break;
			}

			case XMLStreamConstants.CHARACTERS:
			{
				visitCharacters(xmlReader);
				break;
			}

			case XMLStreamConstants.COMMENT:
			{
				visitComment(xmlReader);
				break;
			}

			case XMLStreamConstants.SPACE:
			{
				visitSpace(xmlReader);
				break;
			}

			case XMLStreamConstants.START_DOCUMENT:
			{
				visitStartDocument(xmlReader);
				break;
			}

			case XMLStreamConstants.END_DOCUMENT:
			{
				visitEndDocument(xmlReader);
				break;
			}

			case XMLStreamConstants.ENTITY_REFERENCE:
			{
				visitEntityReference(xmlReader);
				break;
			}

			case XMLStreamConstants.CDATA:
			{
				visitCdata(xmlReader);
				break;
			}

			case XMLStreamConstants.ATTRIBUTE:
			{
				visitAttribute(xmlReader);
				break;
			}

			case XMLStreamConstants.DTD:
			{
				visitDtd(xmlReader);
				break;
			}

			case XMLStreamConstants.NAMESPACE:
			{
				visitNamespace(xmlReader);
				break;
			}

			case XMLStreamConstants.NOTATION_DECLARATION:
			{
				visitNotationDeclaration(xmlReader);
				break;
			}

			case XMLStreamConstants.ENTITY_DECLARATION:
			{
				visitEntityDeclaration(xmlReader);
				break;
			}
		}
	}

}
