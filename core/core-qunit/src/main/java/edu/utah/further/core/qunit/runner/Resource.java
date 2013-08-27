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

import static edu.utah.further.core.api.text.StringUtil.stripNewLinesAndTabs;
import static edu.utah.further.core.api.xml.XmlUtil.newXmlStreamReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.XMLStreamReader;

import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.qunit.runner.XmlAssertion.Type;
import edu.utah.further.core.util.io.IoUtil;

/**
 * A value object for a resource used by {@link XmlAssertion}. This is basically an
 * equivalent of a union-type in C that allows setting either the class path resource name
 * or an input stream. The resource is converted to {@link String} if
 * {@link #stripNewLinesAndTabs} is true.
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
final class Resource
{
	// ========================= FIELDS ====================================

	/**
	 * Resource's name on the classpath.
	 */
	private String name;

	/**
	 * Resource input stream.
	 */
	private InputStream stream;

	/**
	 * Resource string value.
	 */
	private String string;

	/**
	 * Resource StAX stream.
	 */
	private XMLStreamReader xmlStreamReader;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor - call {@link Builder#build()} instead.
	 * </p>
	 */
	private Resource()
	{
		super();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the stringValue property.
	 *
	 * @return the stringValue
	 */
	public String getFilteredString(final boolean stripNewLinesAndTabs)
	{
		return stripNewLinesAndTabs ? stripNewLinesAndTabs(string) : string;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the name property.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Return the stream property.
	 *
	 * @return the stream
	 */
	public InputStream getStream()
	{
		return stream;
	}

	/**
	 * Return the string property.
	 *
	 * @return the string
	 */
	public String getString()
	{
		return string;
	}

	/**
	 * Return the xmlStreamReader property.
	 *
	 * @return the xmlStreamReader
	 */
	public XMLStreamReader getXmlStreamReader()
	{
		return xmlStreamReader;
	}

	// ========================= BUILDER ===================================

	/**
	 * Assertion class builder.
	 */
	public static class Builder implements
			edu.utah.further.core.api.lang.Builder<Resource>
	{
		private final Resource target;
		private final Type type;

		/**
		 * Construct a builder. Use setters to add field values and then invoke
		 * {@link #build()} to get a fully constructed {@link XmlAssertion} instance.
		 */
		@SuppressWarnings("synthetic-access")
		public Builder(final Type type)
		{
			this.target = new Resource();
			this.type = type;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public Resource build()
		{
			validateInputArguments();
			return target;
		}

		/**
		 * Set a new value for the name property.
		 *
		 * @param name
		 *            the name to set
		 * @return this, for method chaining
		 */
		public Builder name(final String name)
		{
			target.name = name;
			return this;
		}

		/**
		 * Set a new value for the stream property.
		 *
		 * @param stream
		 *            the stream to set
		 * @return this, for method chaining
		 */
		public Builder stream(final InputStream stream)
		{
			target.stream = stream;
			return this;
		}

		/**
		 * Set a new value for the string property.
		 *
		 * @param string
		 *            the string to set
		 * @return this, for method chaining
		 */
		public Builder string(final String string)
		{
			target.string = string;
			return this;
		}

		/**
		 * Set a new value for the xmlStreamReader property.
		 *
		 * @param xmlStreamReader
		 *            the xmlStreamReader to set
		 * @return this, for method chaining
		 */
		public Builder xmlStreamReader(final XMLStreamReader xmlStreamReader)
		{
			target.xmlStreamReader = xmlStreamReader;
			return this;
		}

		/**
		 * Validate input arguments and synchronize {@link Resource} properties.
		 */
		private void validateInputArguments()
		{
			// Verify validation rules
			if (!CoreUtil.oneTrue(target.name != null, target.stream != null,
					target.string != null, target.xmlStreamReader != null))
			{
				throw new IllegalArgumentException(
						"Must set exactly one of the following: resource name, input stream, string value, XML StAX reader");
			}

			// Synchronize arguments
			if (target.name != null)
			{
				target.stream = CoreUtil.getResourceAsStreamValidate(target.name);
			}

			switch (type)
			{
				case EXACT_MATCH:
				{
					if (target.string == null)
					{
						target.string = IoUtil.getInputStreamAsString(target.stream);
					}
					break;
				}

				case STREAM_MATCH:
				{
					if (target.string != null)
					{
						target.xmlStreamReader = newXmlStreamReader(new ByteArrayInputStream(
								target.string.getBytes()));
					}
					else if (target.stream != null)
					{
						target.xmlStreamReader = newXmlStreamReader(target.stream);
					}
					break;
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
}
