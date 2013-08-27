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

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Uses the visitor pattern to coalese different argument types of {@link Marshaller}'s
 * <code>marshal()</code> methods.
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
enum MarshallerOutput
{
	// ========================= ENUMERATED CONSTANTS ======================

	RESULT(Result.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (Result) result);
		}
	},

	OUTPUT_STREAM(OutputStream.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (OutputStream) result);
		}
	},

	FILE(File.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (File) result);
		}
	},

	WRITER(Writer.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (Writer) result);
		}
	},

	CONTENT_HANDLER(ContentHandler.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (ContentHandler) result);
		}
	},

	NODE(Node.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (Node) result);
		}
	},

	XML_STREAM_WRITER(XMLStreamWriter.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (XMLStreamWriter) result);
		}
	},

	XML_EVENT_WRITER(XMLEventWriter.class)
	{
		@Override
		public void marshalInternal(final Marshaller marshaller,
				final Object jaxbElement, final Object result) throws JAXBException
		{
			marshaller.marshal(jaxbElement, (XMLEventWriter) result);
		}
	};

	/**
	 * Caches the class-value-to-our-enum-type mapping.
	 */
	private static Map<Class<?>, MarshallerOutput> instances = CollectionUtil.newMap();

	static
	{
		for (final MarshallerOutput instance : MarshallerOutput.values())
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
	private MarshallerOutput(final Class<?> clazz)
	{
		this.clazz = clazz;
	}

	// ========================= METHODS ===================================

	/**
	 * @param marshaller
	 * @param jaxbElement
	 * @param result
	 * @return
	 * @throws JAXBException
	 */
	public abstract void marshalInternal(Marshaller marshaller, Object jaxbElement,
			Object result) throws JAXBException;

	/**
	 * @param marshaller
	 * @param jaxbElement
	 * @param result
	 * @return
	 * @throws JAXBException
	 */
	public static void marshal(final Marshaller marshaller, final Object jaxbElement,
			final Object result) throws JAXBException
	{
		MarshallerOutput.valueOfClazz(result).marshalInternal(marshaller, jaxbElement,
				result);
	}

	/**
	 * A factory method.
	 *
	 * @param result
	 *            marshalling output object
	 * @return corresponding enum constant
	 */
	public static MarshallerOutput valueOfClazz(final Object result)
	{
		// object could be a sub-class/implementor of one of the instances' keys
		for (final Class<?> supportedClass : instances.keySet())
		{
			if (ReflectionUtil.instanceOf(result, supportedClass))
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
