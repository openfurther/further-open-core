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

import static edu.utah.further.core.api.xml.XmlUtil.newXmlStreamReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * A StAX stream printer. Adapted from
 * http://stackoverflow.com/questions/920904/reading-multiple
 * -xml-documents-from-a-socket-in-java
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
 * @version May 6, 2010
 */
public final class XmlStreamPrinter
{
	// ========================= FIELDS ====================================

	private final XMLStreamReader xmlReader;
	private PrintStream output = System.out;
	private boolean printComments = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param inputStream
	 * @throws XMLStreamException
	 */
	private XmlStreamPrinter(final InputStream inputStream)
	{
		this.xmlReader = newXmlStreamReader(inputStream);
	}

	/**
	 * @param inputStream
	 * @throws XMLStreamException
	 */
	private XmlStreamPrinter(final XMLStreamReader xmlReader)
	{
		this.xmlReader = xmlReader;
	}

	/**
	 * Print & format an XML string back to a string. A useful facade.
	 * 
	 * @param xmlString
	 *            input stream
	 * @return input stream as XML string
	 */
	public static String printToString(final String xmlString)
	{
		return printToString(new ByteArrayInputStream(xmlString.getBytes()));
	}

	/**
	 * Print an XML input stream to a string. A useful facade.
	 * 
	 * @param inputStream
	 *            input stream
	 * @return input stream as XML string
	 */
	public static String printToString(final InputStream inputStream)
	{
		return new XmlStreamPrinter.Builder(inputStream).printToString();
	}

	// ========================= BUILDER ===================================

	/**
	 * Assertion class builder.
	 */
	public static class Builder implements
			edu.utah.further.core.api.lang.Builder<XmlStreamPrinter>
	{
		private final XmlStreamPrinter target;

		/**
		 * Construct a builder. Use setters to add field values and then invoke
		 * {@link #build()} to get a fully constructed {@link XQueryAssertion} instance.
		 * 
		 * @param inputStream
		 *            XML input stream to print
		 */
		public Builder(final InputStream inputStream)
		{
			this.target = new XmlStreamPrinter(inputStream);
		}

		/**
		 * Construct a builder. Use setters to add field values and then invoke
		 * {@link #build()} to get a fully constructed {@link XQueryAssertion} instance.
		 * 
		 * @param reader
		 *            XML stream to print
		 */
		public Builder(final XMLStreamReader reader)
		{
			this.target = new XmlStreamPrinter(reader);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public XmlStreamPrinter build()
		{
			return target;
		}

		/**
		 * Print the XML stream to standard output.
		 */
		public void print()
		{
			target.print();
		}

		/**
		 * Print an XML document to a formatted string.
		 * 
		 * @return formatted string
		 */
		public String printToString()
		{
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			setOutput(new PrintStream(baos));
			target.print();
			return baos.toString();
		}

		/**
		 * Set a new value for the output property.
		 * 
		 * @param output
		 *            the output to set
		 * @return this, for method chaining
		 */
		public Builder setOutput(final PrintStream output)
		{
			target.output = output;
			return this;
		}

		/**
		 * Set a new value for the printComments property.
		 * 
		 * @param printComments
		 *            the printComments to set
		 * @return this, for method chaining
		 */
		public Builder printComments(final boolean printComments)
		{
			target.printComments = printComments;
			return this;
		}
	}

	// ========================= METHODS ===================================

	/**
	 * Print the XML stream to standard output.
	 */
	private void print()
	{
		final XmlStreamElementPrinter visitor = new XmlStreamElementPrinter(output);
		visitor.setPrintComments(printComments);
		visitor.visit(xmlReader);
	}

	// ========================= PRIVATE METHODS ===========================

	// private static class XMLStream extends InputStream
	// {
	// private final InputStream delegate;
	// private final StringReader startroot = new StringReader("<root>");
	// private final StringReader endroot = new StringReader("</root>");
	//
	// public XMLStream(final InputStream delegate)
	// {
	// this.delegate = delegate;
	// }
	//
	// @Override
	// public int read() throws IOException
	// {
	// int c = startroot.read();
	// if (c == -1)
	// {
	// c = delegate.read();
	// }
	// if (c == -1)
	// {
	// c = endroot.read();
	// }
	// return c;
	// }
	// }
}
