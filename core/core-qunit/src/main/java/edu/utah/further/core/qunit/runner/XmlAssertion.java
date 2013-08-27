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

import java.io.InputStream;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * XML document assertion (builder pattern: builds criteria and runs an assertion).
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
public abstract class XmlAssertion
{
	// ========================= CONSTANTS =================================

	/**
	 * A taxonomy of supported XQuery assertion types.
	 */
	public static enum Type
	{
		/**
		 * Exact match of XML documents as strings.
		 */
		EXACT_MATCH,

		/**
		 * XML stream comparison.
		 */
		STREAM_MATCH;
	}

	// ========================= FIELDS ====================================

	// Input arguments

	/**
	 * If <code>true</code> and exact XML string matching is turned on, strips new lines
	 * and tabs from XML strings before comparing them.
	 */
	private boolean stripNewLinesAndTabs = false;

	/**
	 * Actual XML document information.
	 */
	private Resource actual;

	/**
	 * Expected XML document information.
	 */
	private Resource expected;

	/**
	 * If stream matching is turned on, these elements are ignored from the XML stream
	 * comparison.
	 */
	private final Set<String> ignoredElements = CollectionUtil.newSet();

	// Options

	/**
	 * Fail silently or die upon assertion failure.
	 */
	private boolean die = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor - call {@link #xmlAssertion(Type)} instead.
	 * </p>
	 */
	protected XmlAssertion()
	{
		// Can only be invoked by the builder.
	}

	/**
	 * Start building an XQuery assertion.
	 *
	 * @param type
	 *            assertion type
	 * @return initial {@link XmlAssertion} builder
	 */
	public static XmlAssertion.Builder xmlAssertion(final Type type)
	{
		return new XmlAssertion.Builder(type);
	}

	// ========================= METHODS ===================================

	/**
	 * Run the assertion logic.
	 *
	 * @return assertion result
	 * @throws AssertionError
	 *             if the assertion logic implemented by the {@link XmlAssertion}
	 *             sub-class is not satisfied and <code>die</code> is true.
	 */
	public abstract boolean doAssert();

	/**
	 * Print actual vs. expected. Useful for testing.
	 *
	 * @return assertion result
	 */
	public abstract void printToCompare();

	// ========================= BUILDER ===================================

	/**
	 * Assertion class builder.
	 */
	public static class Builder implements
			edu.utah.further.core.api.lang.Builder<XmlAssertion>
	{
		private final XmlAssertion target;
		private final Resource.Builder actualBuilder;
		private final Resource.Builder expectedBuilder;

		/**
		 * Construct a builder. Use setters to add field values and then invoke
		 * {@link #build()} to get a fully constructed {@link XmlAssertion} instance.
		 *
		 * @param type
		 *            assertion type
		 */
		private Builder(final Type type)
		{
			this.target = newBlankInstance(type);
			this.actualBuilder = new Resource.Builder(type);
			this.expectedBuilder = new Resource.Builder(type);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public XmlAssertion build()
		{
			target.actual = actualBuilder.build();
			target.expected = expectedBuilder.build();

			// Add validation rules here if needed

			return target;
		}

		/**
		 * Run the assertion. A convenient shortcut.
		 *
		 * @return assertion result
		 */
		public boolean doAssert()
		{
			return build().doAssert();
		}

		/**
		 * Print actual vs. expected. Useful for testing.
		 *
		 * @return assertion result
		 */
		public void printToCompare()
		{
			build().printToCompare();
		}

		/**
		 * Set a new value for the stripNewLinesAndTabs property.
		 *
		 * @param stripNewLinesAndTabs
		 *            the stripNewLinesAndTabs to set
		 * @return this, for method chaining
		 */
		public Builder stripNewLinesAndTabs(final boolean stripNewLinesAndTabs)
		{
			target.stripNewLinesAndTabs = stripNewLinesAndTabs;
			return this;
		}

		/**
		 * Set a new value for the die property.
		 *
		 * @param die
		 *            the die to set
		 * @return this, for method chaining
		 */
		public Builder die(final boolean die)
		{
			target.die = die;
			return this;
		}

		/**
		 * Set a new value for the ignoredElements property.
		 *
		 * @param ignoredElements
		 *            the ignoredElements to set
		 * @return this, for method chaining
		 */
		public Builder ignoredElements(final Set<String> ignoredElements)
		{
			target.ignoredElements.addAll(ignoredElements);
			return this;
		}

		/**
		 * Set a new value for the ignoredElements property.
		 *
		 * @param ignoredElements
		 *            the ignoredElements to set
		 * @return this, for method chaining
		 */
		public Builder ignoredElements(final String... ignoredElements)
		{
			for (final String element : ignoredElements)
			{
				target.ignoredElements.add(element);
			}
			return this;
		}

		/**
		 * Add a single ignored element.
		 *
		 * @param element
		 *            the ignored element to add
		 * @return this, for method chaining
		 */
		public Builder ignoredElement(final String element)
		{
			target.ignoredElements.add(element);
			return this;
		}

		/**
		 * Set the actual resource's name on the class path.
		 *
		 * @param actualResourceName
		 *            the actualResourceName to set
		 * @return this, for method chaining
		 */
		public Builder actualResourceName(final String actualResourceName)
		{
			actualBuilder.name(actualResourceName);
			return this;
		}

		/**
		 * Set the actual resource's input stream.
		 *
		 * @param actualResourceStream
		 *            the actualResourceStream to set
		 * @return this, for method chaining
		 */
		public Builder actualResourceStream(final InputStream actualResourceStream)
		{
			actualBuilder.stream(actualResourceStream);
			return this;
		}

		/**
		 * Set the actual resource's string value.
		 *
		 * @param actualResourceString
		 *            the actualResourceName to set
		 * @return this, for method chaining
		 */
		public Builder actualResourceString(final String actualResourceString)
		{
			actualBuilder.string(actualResourceString);
			return this;
		}

		/**
		 * Set the actual resource's xmlStreamReader value.
		 *
		 * @param actualResourceXmlStreamReader
		 *            the actualResourceName to set
		 * @return this, for method chaining
		 */
		public Builder actualResourceXmlStreamReader(
				final XMLStreamReader actualResourceXmlStreamReader)
		{
			actualBuilder.xmlStreamReader(actualResourceXmlStreamReader);
			return this;
		}

		/**
		 * Set the expected resource's name on the class path.
		 *
		 * @param expectedResourceName
		 *            the expectedResourceName to set
		 * @return this, for method chaining
		 */
		public Builder expectedResourceName(final String expectedResourceName)
		{
			expectedBuilder.name(expectedResourceName);
			return this;
		}

		/**
		 * Set the expected resource's input stream.
		 *
		 * @param expectedResourceName
		 *            the expectedResourceName to set
		 * @return this, for method chaining
		 */
		public Builder expectedResourceStream(final InputStream expectedResourceStream)
		{
			expectedBuilder.stream(expectedResourceStream);
			return this;
		}

		/**
		 * Set the expected resource's string value.
		 *
		 * @param expectedResourceString
		 *            the expectedResourceName to set
		 * @return this, for method chaining
		 */
		public Builder expectedResourceString(final String expectedResourceString)
		{
			expectedBuilder.string(expectedResourceString);
			return this;
		}

		/**
		 * Set the expected resource's xmlStreamReader value.
		 *
		 * @param expectedResourceXmlStreamReader
		 *            the expectedResourceName to set
		 * @return this, for method chaining
		 */
		public Builder expectedResourceString(
				final XMLStreamReader expectedResourceXmlStreamReader)
		{
			expectedBuilder.xmlStreamReader(expectedResourceXmlStreamReader);
			return this;
		}

		/**
		 * A factory method of {@link XmlAssertion} instances.
		 *
		 * @param type
		 * @return
		 */
		private static XmlAssertion newBlankInstance(final Type type)
		{
			switch (type)
			{
				case EXACT_MATCH:
				{
					return new XmlAssertionExactMatch();
				}

				case STREAM_MATCH:
				{
					return new XmlAssertionStreamMatch();
				}

				default:
				{
					throw new UnsupportedOperationException(
							"Unrecognized assertion type " + type);
				}
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the stripNewLinesAndTabs property.
	 *
	 * @return the stripNewLinesAndTabs
	 */
	protected final boolean isStripNewLinesAndTabs()
	{
		return stripNewLinesAndTabs;
	}

	/**
	 * Return the die property.
	 *
	 * @return the die
	 */
	protected final boolean isDie()
	{
		return die;
	}

	/**
	 * Return the actual property.
	 *
	 * @return the actual
	 */
	protected final Resource getActual()
	{
		return actual;
	}

	/**
	 * Return the expected property.
	 *
	 * @return the expected
	 */
	protected final Resource getExpected()
	{
		return expected;
	}

	/**
	 * Return the ignoredElements property.
	 *
	 * @return the ignoredElements
	 */
	protected final Set<String> getIgnoredElements()
	{
		// Not making defensive copies for efficiency
		return ignoredElements;
	}

}
