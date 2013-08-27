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

import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlUtil;
import edu.utah.further.core.util.collections.page.AbstractPager;
import edu.utah.further.core.util.collections.page.DefaultPagingStrategy;

/**
 * Adapts an {@link XMLStreamReader} to an {@link Iterator}. Assumes that the XML document
 * is of the form
 *
 * <pre>
 * <rootTag>
 *   <entityTag>...</entityTag>
 *   <entityTag>...</entityTag>
 *   ...
 *   <entityTag>...</entityTag>
 * </rootTag>
 * That is, all depth-2 element names are the same (entityTag in this case).
 * An iterant is the string of the sub-tree of a <code>entityTag</code> element,
 * e.g.
 *
 * <pre>
 * &quot;&lt;entityTag&gt;...&lt;/entityTag&gt;&quot;
 * </pre>
 * <p>
 * If not all depth-2 elements tags are the same, the behavior is the same: each depth-2
 * element is output as an iterant.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 26, 2010
 */
final class XmlStreamPager extends AbstractPager<String>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmlStreamPager.class);

	/**
	 * Maximum XML tree depth of keeping track of element names.
	 */
	private static final int MAX_DEPTH = 2;

	// ========================= FIELDS ====================================

	/**
	 * XML document to iterate on.
	 */
	private final XMLStreamReader xmlReader;

	/**
	 * Returns entity tag XML tree strings as iterants.
	 */
	private final XmlStreamIterantPrinter iterantPrinter;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param xmlReader
	 * @param pagingStrategy
	 */
	public XmlStreamPager(final XMLStreamReader xmlReader,
			final PagingStrategy pagingStrategy)
	{
		super(pagingStrategy);
		this.xmlReader = xmlReader;
		if (!ReflectionUtil.instanceOf(pagingStrategy, DefaultPagingStrategy.class))
		{
			throw new UnsupportedOperationException(
					"Only DefaultPagingStrategy is currently supported");
		}
		// Default paging strategy returns a fixed page size ==> can use dummy parameters
		// in the following call
		final int pageSize = pagingStrategy.getNextPageSize(0, 0, 0);
		this.iterantPrinter = new XmlStreamIterantPrinter(this, pageSize);
	}

	// ========================= Impl: Iterator<String> ====================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public String next()
	{
		iterantPrinter.visit(xmlReader);
		return iterantPrinter.getIterantString();
	}

	// ========================= IMPL: AbstractPager =======================

	/**
	 * @return
	 * @see edu.utah.further.core.util.collections.page.AbstractPager#hasNextInternal()
	 */
	@Override
	protected boolean hasNextInternal()
	{
		return iterantPrinter.isNextIterantExists();
	}

	// ========================= PRIVATE METHODS: Visitation ===============

	/**
	 * Visits the XML input stream and prints elements under a single entityTag to an
	 * output stream.
	 * <p>
	 * Does not print comments. Delegates to an XML element printer for all other StAX
	 * events.
	 */
	private static class XmlStreamIterantPrinter extends XmlStreamVisitorStub
	{
		// ========================= FIELDS ====================================

		/**
		 * Owning object, for call-backs.
		 */
		private final XmlStreamPager pager;

		/**
		 * Keeps track of the element depth in the XML tree. 1-based, i.e. the root tag's
		 * depth is 1.
		 */
		private int depth = 1;

		/**
		 * Name of encountered element at each depth of the XML tree, up to depth
		 * {@link XmlStreamPager#MAX_DEPTH}.
		 */
		private final Map<Integer, QName> elementNames = CollectionUtil.newMap();

		/**
		 * A delegate that prints out XML elements to the output stream.
		 */
		private XmlStreamVisitor elementPrinter;

		/**
		 * Output stream of the body of the single iterant (excluding the root tag
		 * enclosing).
		 */
		private PrintStream outputStream;

		/**
		 * Signals that there are no more iterants to read from the stream.
		 */
		private boolean nextIterantExists = true;

		/**
		 * Signals that we have arrived at the end of the current XML entity element
		 * sub-tree (iterant).
		 */
		private boolean endOfCurrentIterant;

		/**
		 * Caches a root tag opening string to print at the beginning of each iterant.
		 */
		private String rootElementStart;

		/**
		 * Caches a root tag closing string to print at the end of each iterant.
		 */
		private String rootElementEnd;

		/**
		 * The entire current iterant XML tree, as a string.
		 */
		private String iterantString;

		/**
		 * Counts how many entity elemnts have been encountered since the last iterant
		 * beginning.
		 */
		private int entityElementCounter;

		/**
		 * Maximum size of iterant.
		 */
		private final int pageSize;

		// ========================= CONSTRUCTORS ==============================

		/**
		 * @param pager
		 * @param pageSize
		 */
		public XmlStreamIterantPrinter(final XmlStreamPager pager, final int pageSize)
		{
			super();
			this.pager = pager;
			this.pageSize = pageSize;
		}

		// ========================= METHODS ===================================

		/**
		 * Return the iterantString property. Populated with the current iterant after
		 * after {@link #visit(XMLStreamReader)}.
		 *
		 * @return the iterantString
		 */
		public String getIterantString()
		{
			return iterantString;
		}

		// ========================= IMPL: XmlStreamVisitor ====================

		/**
		 *
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visit(javax.xml.stream.
		 *      XMLStreamReader )
		 */
		@Override
		public void visit(final XMLStreamReader xmlReader)
		{
			// Reset internal state
			final ByteArrayOutputStream byteArrayOutputstream = new ByteArrayOutputStream();
			outputStream = new PrintStream(byteArrayOutputstream);
			elementPrinter = new XmlStreamElementPrinter(outputStream);
			endOfCurrentIterant = pager.isEndOfIteration();
			entityElementCounter = 0;

			// Loop until end of iterant or end of file
			try
			{
				printState(xmlReader);
				while (!endOfCurrentIterant && nextIterantExists && xmlReader.hasNext())
				{
					if (log.isTraceEnabled())
					{
						log.trace(XmlUtil.getEventSimpleString(xmlReader));
					}
					visitEvent(xmlReader);
					xmlReader.next();
					printState(xmlReader);
				}

				if (log.isTraceEnabled())
				{
					log.trace("Terminating loop over XML elements");
				}
				// Keep stream open at iterant ends; close only when the stream ends
				// (or right before it ends, when we detect no more entity tags)
				if (!nextIterantExists || !xmlReader.hasNext())
				{
					if (log.isTraceEnabled())
					{
						log.trace("End of stream, closing");
					}
					xmlReader.close();
					if (entityElementCounter >= 0)
					{
						// Some left-over entities in our current output stream buffer
						// exist, so this is the last iterant. This happens for instance
						// when the page size is greater than the number of entities in
						// the XML. So there's a single iterant in the file.
						// We also need to construct the string when this is zero for when
						// there are no results.
						constructIterantString(byteArrayOutputstream);
					}
				}
				else
				{
					// Construct the current iterant string for output
					constructIterantString(byteArrayOutputstream);
					if (log.isTraceEnabled())
					{
						log.trace("End of iterant");
						log.trace("iterantString " + iterantString);
					}
				}
			}
			catch (final XMLStreamException e)
			{
				e.printStackTrace();
				throw new ApplicationException("Error print StAX stream", e);
			}
		}

		/**
		 * @param byteArrayOutputstream
		 */
		private void constructIterantString(
				final ByteArrayOutputStream byteArrayOutputstream)
		{
			iterantString = rootElementStart + byteArrayOutputstream.toString()
					+ rootElementEnd;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitStartElement(javax
		 * .xml.stream.XMLStreamReader)
		 */
		@SuppressWarnings("boxing")
		@Override
		public void visitStartElement(final XMLStreamReader xmlReader)
		{
			if (log.isTraceEnabled())
			{
				log.trace("visitStartElement() depth " + depth);
			}
			// Save element name in map
			if ((depth <= MAX_DEPTH) && !elementNames.containsKey(depth))
			{
				elementNames.put(depth, xmlReader.getName());

				// Cache root tag opening and closing strings
				if (depth == 1)
				{
					if (log.isTraceEnabled())
					{
						log.trace("Caching root tag");
					}
					final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					try (final PrintStream rootOs = new PrintStream(byteArrayOutputStream)) {
						XmlUtil.printElement(rootOs, xmlReader, true);
						rootElementStart = byteArrayOutputStream.toString();
					}
					rootElementEnd = "</" + XmlUtil.getName(xmlReader) + ">";
				}
			}
			// Do not print root tag
			if (depth >= 2)
			{
				elementPrinter.visitStartElement(xmlReader);
			}
			depth++;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitEndElement(javax.xml
		 * .stream.XMLStreamReader)
		 */
		@Override
		public void visitEndElement(final XMLStreamReader xmlReader)
		{
			depth--;
			if (log.isTraceEnabled())
			{
				log.trace("visitEndElement() depth " + depth);
			}

			if (depth == 1)
			{
				// Do not print root tag here; it is printed in visit()
				// Arrived at end of root tag ==> no more entity elements expected
				nextIterantExists = false;
			}
			else if (depth == 2) // entityElement.equals(xmlReader.getName()))
			{
				// End of last entity tag ==> end of chunk
				elementPrinter.visitEndElement(xmlReader);

				// Update state
				entityElementCounter++;
				pager.incrementTotalIterantCounter();
				if (pager.getTotalIterantCounter() > pager.getNumHeaderRows())
				{
					pager.addSingleIterant();
				}
				if (pager.isEndOfIteration() || (entityElementCounter == pageSize))
				{
					endOfCurrentIterant = true;
				}
			}
			else
			{
				// Any other element of depth 3 or more
				elementPrinter.visitEndElement(xmlReader);
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * edu.utah.further.core.xml.stax.XmlStreamVisitorStub#visitEndDocument(javax.
		 * xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitEndDocument(final XMLStreamReader xmlReader)
		{
			// In case we haven't yet set this to false in visitEndElement() with depth=1
			nextIterantExists = false;
		}

		// Suppress processing instruction because we append to root tag string BEFORE the
		// processing instruction string, which is illegal.
		// TODO: improve root tag snippet appending logic to allow processing
		// instructions.

		// /**
		// * @param xmlr
		// * @see
		// edu.utah.further.core.xml.stax.XmlStreamVisitor#visitProcessingInstruction(javax.xml.stream.XMLStreamReader)
		// */
		// @Override
		// public void visitProcessingInstruction(final XMLStreamReader xmlr)
		// {
		// elementPrinter.visitProcessingInstruction(xmlr);
		// }
		// /**
		// * @param xmlr
		// * @see
		// edu.utah.further.core.xml.stax.XmlStreamVisitor#visitStartDocument(javax.xml.stream.XMLStreamReader)
		// */
		// @Override
		// public void visitStartDocument(final XMLStreamReader xmlr)
		// {
		// elementPrinter.visitStartDocument(xmlr);
		// }

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitCharacters(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitCharacters(final XMLStreamReader xmlr)
		{
			elementPrinter.visitCharacters(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitSpace(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitSpace(final XMLStreamReader xmlr)
		{
			elementPrinter.visitSpace(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEntityReference(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitEntityReference(final XMLStreamReader xmlr)
		{
			elementPrinter.visitEntityReference(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitAttribute(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitAttribute(final XMLStreamReader xmlr)
		{
			elementPrinter.visitAttribute(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitDtd(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitDtd(final XMLStreamReader xmlr)
		{
			elementPrinter.visitDtd(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitCdata(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitCdata(final XMLStreamReader xmlr)
		{
			elementPrinter.visitCdata(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitNotationDeclaration(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitNotationDeclaration(final XMLStreamReader xmlr)
		{
			elementPrinter.visitNotationDeclaration(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitNamespace(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitNamespace(final XMLStreamReader xmlr)
		{
			elementPrinter.visitNamespace(xmlr);
		}

		/**
		 * @param xmlr
		 * @see edu.utah.further.core.xml.stax.XmlStreamVisitor#visitEntityDeclaration(javax.xml.stream.XMLStreamReader)
		 */
		@Override
		public void visitEntityDeclaration(final XMLStreamReader xmlr)
		{
			elementPrinter.visitEntityDeclaration(xmlr);
		}

		/**
		 * Return the nextIterantExists property.
		 *
		 * @return the nextIterantExists
		 */
		public boolean isNextIterantExists()
		{
			return nextIterantExists;
		}

		/**
		 * @param xmlReader
		 * @throws XMLStreamException
		 */
		private void printState(final XMLStreamReader xmlReader)
				throws XMLStreamException
		{
			if (log.isTraceEnabled())
			{
				log.trace("endOfCurrentIterant " + endOfCurrentIterant
						+ " nextIterantExists " + nextIterantExists + " hasNext "
						+ xmlReader.hasNext());
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================
}