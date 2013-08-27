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
package edu.utah.further.core.qunit.runner;

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.xml.XmlUtil.getEventSimpleString;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.xml.stax.XmlElementType;
import edu.utah.further.core.xml.stax.XmlStreamPrinter;

/**
 * An XML stream match assertion based on StAX (streaming XML API). Both the XQuery output
 * and the expected output streams are iterated over and compared, therefore XML white
 * space is a non-issue (setting the option
 * {@link XmlAssertion.Builder#stripNewLinesAndTabs} has no effect here).
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
final class XmlAssertionStreamMatch extends XmlAssertion
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmlAssertionStreamMatch.class);

	// ========================= FIELDS ====================================

	/**
	 * Keeps track of DOM tree depth into an ignored element. Only elements with zero
	 * depth are considered; positive values indicate that we are within the scope of an
	 * ignored element.
	 */
	private int ignoredElementDepth = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor - call {@link Builder#build()} instead.
	 * </p>
	 */
	protected XmlAssertionStreamMatch()
	{
		// Can only be invoked by the builder.
		super();
	}

	// ========================= METHODS ===================================

	/**
	 * Print actual vs. expected. Useful for testing.
	 * 
	 * @return assertion result
	 */
	@Override
	public void printToCompare()
	{
		if (log.isDebugEnabled())
		{
			final XMLStreamReader actual = getActual().getXmlStreamReader();
			final XMLStreamReader expected = getExpected().getXmlStreamReader();
			final String expectedOutput = new XmlStreamPrinter.Builder(expected)
					.printComments(false)
					.printToString();
			final String actualOutput = new XmlStreamPrinter.Builder(actual)
					.printComments(false)
					.printToString();
			final String s = "Printing documents to be compared" + NEW_LINE_STRING
					+ NEW_LINE_STRING + "----- EXPECTED -----" + NEW_LINE_STRING
					+ expectedOutput + NEW_LINE_STRING + NEW_LINE_STRING
					+ "----- ACTUAL -----" + NEW_LINE_STRING + actualOutput;
			log.debug(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.qunit.runner.XmlAssertion#doAssert()
	 */
	@Override
	public boolean doAssert()
	{
		final XMLStreamReader actual = getActual().getXmlStreamReader();
		final XMLStreamReader expected = getExpected().getXmlStreamReader();
		try
		{
			int count = 0;
			for (; actual.hasNext() && expected.hasNext(); nextConsideredEvent(actual), nextConsideredEvent(expected))
			{
				count++;
				if (log.isDebugEnabled())
				{
					log.debug("Actual  : " + getEventSimpleString(actual));
					log.debug("Expected: " + getEventSimpleString(expected));
				}
				if (!assertEventTypeEquals(expected, actual, count))
				{
					return fail(actual, expected);
				}
				if (!assertEventContentEquals(expected, actual, count))
				{
					return fail(actual, expected);
				}
			}
			return true;
		}
		catch (final XMLStreamException e)
		{
			throw new ApplicationException("Problem reading XML stream", e);
		}
	}

	/**
	 * @param actual
	 * @param expected
	 * @return
	 * @throws XMLStreamException
	 */
	private boolean fail(final XMLStreamReader actual, final XMLStreamReader expected)
			throws XMLStreamException
	{
		actual.close();
		expected.close();
		return false;
	}

	/**
	 * @param expected
	 * @param actual
	 * @param count
	 */
	private boolean assertEventTypeEquals(final XMLStreamReader expected,
			final XMLStreamReader actual, final int count)
	{
		final XmlElementType actualType = XmlElementType.valueOf(actual.getEventType());
		final XmlElementType expectedType = XmlElementType.valueOf(expected
				.getEventType());
		if (isDie())
		{
			assertEquals("XML output stream does not match expectation at element #"
					+ count + ". " + "Expected element type is " + expectedType
					+ ", actual is " + actualType, expectedType, actualType);
			return true;
		}
		return expectedType.equals(actualType);

	}

	/**
	 * @param expected
	 * @param actual
	 * @param count
	 */
	private boolean assertEventContentEquals(final XMLStreamReader expected,
			final XMLStreamReader actual, final int count)
	{
		final String actualContent = getEventContent(actual);
		final String expectedContent = getEventContent(expected);
		if (isDie())
		{
			assertEquals("XML output stream does not match expectation at element #"
					+ count + ". " + "Expected element content is " + expectedContent
					+ ", actual is " + actualContent + ".", expectedContent,
					actualContent);
			return true;
		}
		return (expectedContent == null) ? (actualContent == null) : expectedContent
				.equals(actualContent);
	}

	/**
	 * @param reader
	 * @throws XMLStreamException
	 */
	public void nextConsideredEvent(final XMLStreamReader reader)
			throws XMLStreamException
	{
		boolean foundConsideredElement = false;
		while (!foundConsideredElement && reader.hasNext())
		{
			reader.next();
			if (log.isTraceEnabled())
			{
				log.trace("  Considering " + getEventSimpleString(reader));
			}
			if (!isConsideredEvent())
			{
				if (log.isTraceEnabled())
				{
					log.trace("  Within ignored element, ignoring");
				}

				if (reader.getEventType() != XMLStreamConstants.END_ELEMENT)
				{
					continue;
				}
			}

			switch (reader.getEventType())
			{
			// ========================================
			// Ignore all white space and comments
			// ========================================
				case XMLStreamConstants.COMMENT:
				case XMLStreamConstants.SPACE:
				{
					continue;
				}

				case XMLStreamConstants.CHARACTERS:
				{
					if (StringUtils.isWhitespace(reader.getText()))
					{
						continue;
					}
					foundConsideredElement = true;
					break;
				}

				// ========================================
				// All other elements are considered
				// ========================================

				case XMLStreamConstants.START_ELEMENT:
				{
					if (isIgnoredElement(reader))
					{
						ignoredElementDepth++;
						foundConsideredElement = false;
					}
					else
					{
						foundConsideredElement = true;
					}
					break;
				}

				case XMLStreamConstants.END_ELEMENT:
				{
					if (isIgnoredElement(reader))
					{
						ignoredElementDepth--;
						foundConsideredElement = false;
					}
					else
					{
						foundConsideredElement = true;
					}
					break;
				}

				default:
				{
					foundConsideredElement = true;
					break;
				}
			}
		}
		if (log.isTraceEnabled())
		{
			log.trace("  Found event " + getEventSimpleString(reader));
			log.trace(StringUtil.repeat("=", 50));
		}
	}

	/**
	 * Further narrow down relevant events. Ignoring all elements in the ignored element
	 * list.
	 * 
	 * @return <code>true</code> if and only if the event is to be considered for
	 *         comparison
	 */
	private boolean isConsideredEvent()
	{
		return ignoredElementDepth == 0;
	}

	/**
	 * @param reader
	 * @return
	 */
	private boolean isIgnoredElement(final XMLStreamReader reader)
	{
		return getIgnoredElements().contains(reader.getLocalName());
	}

	/**
	 * Return a StAX element's content, for comparison. Applies only to element names and
	 * bodies.
	 * 
	 * @param reader
	 *            reader
	 * @return reader's next element textual representation. If not supported here,
	 *         returns <code>null</code>
	 */
	private static String getEventContent(final XMLStreamReader reader)
	{
		switch (reader.getEventType())
		{
			case XMLStreamConstants.START_ELEMENT:
			case XMLStreamConstants.END_ELEMENT:
			{
				return reader.getLocalName();
			}

			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			{
				return reader.getText();
			}

			default:
			{
				return null;
			}
		}
	}
}
