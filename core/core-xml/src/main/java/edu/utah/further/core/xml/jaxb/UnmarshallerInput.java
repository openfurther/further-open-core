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
package edu.utah.further.core.xml.jaxb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.util.io.IoUtil;

/**
 * Uses the visitor pattern to coalese different argument types of {@link Unmarshaller}'s
 * <code>unmarshal()</code> methods.
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
 * @version Jul 19, 2010
 */
enum UnmarshallerInput
{
	// ========================= ENUMERATED CONSTANTS ======================

	FILE(File.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((File) input);
		}
	},

	INPUT_SOURCE(InputSource.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((InputSource) input);
		}
	},

	INPUT_STREAM(InputStream.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((InputStream) input);
		}
	},

	NODE(Node.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((Node) input);
		}
	},

	READER(Reader.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((Reader) input);
		}
	},

	SOURCE(Source.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((Source) input);
		}
	},

	STRING(String.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(
					((String) input).getBytes());
			final Object unmarshal = unmarshaller.unmarshal(inputStream);
			IoUtil.closeSilently(inputStream);
			return unmarshal;
		}
	},

	URL(URL.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((URL) input);
		}
	},

	XML_STREAM_READER(XMLStreamReader.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((XMLStreamReader) input);
		}
	},

	XML_EVENT_READER(XMLEventReader.class)
	{
		@Override
		public Object unmarshalInternal(final Unmarshaller unmarshaller,
				final Object input) throws JAXBException
		{
			return unmarshaller.unmarshal((XMLEventReader) input);
		}
	};

	/**
	 * Caches the class-value-to-our-enum-type mapping.
	 */
	private static Map<Class<?>, UnmarshallerInput> instances = CollectionUtil.newMap();

	static
	{
		for (final UnmarshallerInput instance : UnmarshallerInput.values())
		{
			instances.put(instance.getClazz(), instance);
		}
	}

	// ========================= FIELDS ====================================

	/**
	 * Result type corresponding to this enumerated type.
	 */
	private final Class<?> clazz;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param clazz
	 */
	private UnmarshallerInput(final Class<?> clazz)
	{
		this.clazz = clazz;
	}

	// ========================= METHODS ===================================

	/**
	 * @param unmarshaller
	 * @param jaxbElement
	 * @param input
	 * @return
	 * @throws JAXBException
	 */
	public abstract Object unmarshalInternal(Unmarshaller unmarshaller, Object input)
			throws JAXBException;

	/**
	 * @param unmarshaller
	 * @param jaxbElement
	 * @param input
	 * @return
	 * @throws JAXBException
	 */
	public static Object unmarshal(final Unmarshaller unmarshaller, final Object input)
			throws JAXBException
	{
		return UnmarshallerInput.valueOfClazz(input).unmarshalInternal(unmarshaller,
				input);
	}

	/**
	 * A factory method.
	 *
	 * @param input
	 *            unmarshalling input object
	 * @return corresponding enum constant
	 */
	public static UnmarshallerInput valueOfClazz(final Object input)
	{
		// object could be a sub-class/implementor of one of the instances' keys
		for (final Class<?> supportedClass : instances.keySet())
		{
			if (ReflectionUtil.instanceOf(input, supportedClass))
			{
				return instances.get(supportedClass);
			}
		}
		return null;
	}

	// ========================= GET / SET =================================

	/**
	 * Return the clazz property.
	 *
	 * @return the clazz
	 */
	public Class<?> getClazz()
	{
		return clazz;
	}
}
