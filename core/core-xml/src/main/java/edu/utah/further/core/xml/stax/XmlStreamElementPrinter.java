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

import static edu.utah.further.core.api.text.StringUtil.quote;

import java.io.PrintStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.Validate;

import edu.utah.further.core.api.xml.XmlUtil;

/**
 * Encapsulates the business logic of printing different types of XML elements.
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
public final class XmlStreamElementPrinter extends XmlStreamVisitorStub
{
	// ========================= FIELDS ====================================

	// Options
	private PrintStream os;
	private boolean printComments = true;

	// Internal variables
	private int depth;
	private QName rootElement;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param outputStream
	 */
	public XmlStreamElementPrinter(final PrintStream outputStream)
	{
		super();
		Validate.notNull(outputStream, "An output stream must be set");
		this.os = outputStream;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the printComments property.
	 * 
	 * @return the printComments
	 */
	public boolean isPrintComments()
	{
		return printComments;
	}

	/**
	 * Set a new value for the printComments property.
	 * 
	 * @param printComments
	 *            the printComments to set
	 */
	public void setPrintComments(final boolean printComments)
	{
		this.printComments = printComments;
	}

	// ========================= Impl: XmlStreamVisitor ====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitStartDocument(javax
	 * .xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitStartDocument(final XMLStreamReader xmlReader)
	{
		os.println("<?xml version=" + quote(xmlReader.getVersion()) + " encoding="
				+ quote(xmlReader.getCharacterEncodingScheme()) + " standalone="
				+ quote(xmlReader.isStandalone() ? "yes" : "no") + "?>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitEntityReference(javax
	 * .xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitEntityReference(final XMLStreamReader xmlReader)
	{
		final String str = xmlReader.getLocalName() + "=";
		os.print(str);
		if (xmlReader.hasText())
		{
			os.print("[" + xmlReader.getText() + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitComment(javax.xml.
	 * stream.XMLStreamReader)
	 */
	@Override
	public void visitComment(final XMLStreamReader xmlReader)
	{
		if (isPrintComments())
		{
			os.print("<!--");
			if (xmlReader.hasText())
				os.print(xmlReader.getText());
			os.print("-->");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitCdata(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitCdata(final XMLStreamReader xmlReader)
	{
		os.print("<![CDATA[");
		final int start = xmlReader.getTextStart();
		final int length = xmlReader.getTextLength();
		os.print(new String(xmlReader.getTextCharacters(), start, length));
		os.print("]]>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitProcessingInstruction
	 * (javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitProcessingInstruction(final XMLStreamReader xmlReader)
	{
		os.print("<?");
		if (xmlReader.hasText())
			os.print(xmlReader.getText());
		os.print("?>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitSpace(javax.xml.stream
	 * .XMLStreamReader)
	 */
	@Override
	public void visitSpace(final XMLStreamReader xmlReader)
	{
		XmlUtil.printLiterally(os, xmlReader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitCharacters(javax.xml
	 * .stream.XMLStreamReader)
	 */
	@Override
	public void visitCharacters(final XMLStreamReader xmlReader)
	{
		XmlUtil.printLiterally(os, xmlReader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitStartElement(javax
	 * .xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitStartElement(final XMLStreamReader xmlReader)
	{
		XmlUtil.printElement(os, xmlReader, true);
		if (rootElement == null && depth == 1)
		{
			rootElement = xmlReader.getName();
		}
		depth++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitEndElement(javax.xml
	 * .stream.XMLStreamReader)
	 */
	@Override
	public void visitEndElement(final XMLStreamReader xmlReader)
	{
		depth--;
		if (depth == 1 && rootElement.equals(xmlReader.getName()))
		{
			rootElement = null;
		}
		XmlUtil.printEndElement(os, xmlReader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitEndDocument(javax.
	 * xml.stream.XMLStreamReader)
	 */
	@Override
	public void visitEndDocument(final XMLStreamReader xmlReader)
	{
		// println("finished document");
	}

	// ========================= PRIVATE METHODS ===========================
}
