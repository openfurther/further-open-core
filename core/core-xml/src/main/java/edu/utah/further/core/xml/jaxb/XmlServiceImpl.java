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

import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStream;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;

/**
 * Centralizes JAXB XML utilities. Uses an empty default JAXB configuration map if used as
 * a stand-alone object outside spring, or the FURTHeR configuration by default, if
 * deployed as a Spring service bean.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Service("xmlService")
public class XmlServiceImpl implements XmlService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmlServiceImpl.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * A custom JAXB context used by default if a configuration not specified in the
	 * marshalling and unmarshalling methods of this service.
	 */
	private Map<String, Object> defaultJaxbConfig = JaxbConfig.EMPTY.getJaxbConfig();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Set FURTHeR as the default configuration if this class is deployed as a Spring
	 * service. If you create the instance outside spring, you will have to set the
	 * default configuration manually if desired.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		setDefaultJaxbConfig(JaxbConfig.FURTHER.getJaxbConfig());
	}

	// ========================= IMPLEMENTATION: XmlService ================

	/**
	 * A factory method of marshalling options.
	 *
	 * @return empty marshalling options options instance. Use chaining calls to set
	 *         options
	 */
	@Override
	public MarshallerOptions options()
	{
		return MarshallerOptionsImpl.newInstance().setJaxbConfig(defaultJaxbConfig);
	}

	/**
	 * @param entity
	 * @param options
	 * @return
	 */
	@Override
	public String marshal(final Object entity)
	{
		return marshal(entity, null);
	}

	/**
	 * @param entity
	 * @param options
	 * @return
	 */
	@Override
	public String marshalAndPrint(final Object entity)
	{
		return marshal(entity, options().setDebugPrintResult(true));
	}

	/**
	 * Marshal an entity into XML. <code>result</code> is updated with the marshalling
	 * result at the end of this call.
	 *
	 * @param entity
	 *            entity to marshal
	 * @param result
	 *            XML output handler
	 * @param options
	 *            contains marshalling options. If <code>null</code>, using default
	 *            options
	 */
	@Override
	public void marshal(final Object entity, final Object result,
			final MarshallerOptions options)
	{
		try
		{
			final MarshallerOptions marshallingOptions = createBuilderFromParameter(
					entity, options);
			buildContextIfNecessary(marshallingOptions);
			MarshallerOutput.marshal(marshallingOptions.getMarshaller(), entity, result);
		}
		catch (final Exception e)
		{
			log.error("Marshalling failed", e);
		}
	}

	/**
	 * Marshal an entity into XML.
	 *
	 * @param entity
	 *            entity to marshal
	 * @param args
	 *            Optional arguments:
	 *            <ol>
	 *            <li>options - MarshallerOptions</li>
	 *            <li>result - Output handler (Result, etc.)</li>
	 *            </ol>
	 * @return If <code>result = null</code>, assumes that the marshalling is into a
	 *         string and returns it. Otherwise, returns <code>null</code> and
	 *         <code>result</code> is updated with the marshalling result
	 */
	@Override
	public String marshal(final Object entity, final MarshallerOptions options)
	{
		String s;
		try(final OutputStream result = new ByteArrayOutputStream())
		{
			final MarshallerOptions marshallingOptions = createBuilderFromParameter(
					entity, options);
			buildContextIfNecessary(marshallingOptions);
			MarshallerOutput.marshal(marshallingOptions.getMarshaller(), entity, result);
			s = result.toString();
			if (log.isDebugEnabled() && marshallingOptions.isDebugPrintResult())
			{
				log.debug(s);
			}
			return s;
		}
		catch (final Exception e)
		{
			log.error("Marshalling failed", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.xml.XmlService#unmarshal(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public <T> T unmarshalResource(final String resourceName, final Class<T> rootClass)
			throws JAXBException, IOException
	{
		return this.<T> unmarshalResource(resourceName, options().addClass(rootClass));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.xml.XmlService#unmarshal(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public <T> T unmarshal(final Object input, final Class<?> rootClass)
	{
		return this.<T> unmarshal(input, options().addClass(rootClass));
	}

	/**
	 * Unmarshal an XML input source into an object.
	 *
	 * @param <T>
	 * @param inputSource
	 * @param context
	 * @return
	 * @throws JAXBException
	 * @see edu.utah.further.core.xml.jaxb.XmlService#unmarshalResource(java.io.InputStream,
	 *      javax.xml.bind.JAXBContext)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(final Object input, final MarshallerOptions options)
	{
		T value = null;
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("~~~Input is " + input);
			}
			buildContextIfNecessary(options);
			final Object unmarshalled = UnmarshallerInput.unmarshal(
					options.getUnmarshaller(), input);
			if (ReflectionUtil.instanceOf(unmarshalled, JAXBElement.class))
			{
				final JAXBElement<T> raw = (JAXBElement<T>) unmarshalled;
				value = raw.getValue();
			}
			else
			{
				value = (T) unmarshalled;
			}
			if (log.isDebugEnabled())
			{
				log.debug("~~~Unmarshaled input to " + value);
			}
			return value;
		}
		catch (final Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("~~~Unmarshal failed on " + value);
			}
			log.error("~~~Unmarshalling failed", e);
			return null;
		}
	}

	/**
	 * @param <T>
	 * @param name
	 * @param jaxbPackage
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 * @see edu.utah.further.core.xml.jaxb.XmlService#unmarshalResource(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public <T> T unmarshalResource(final String resourceName, final MarshallerOptions options)
			throws JAXBException, IOException
	{
		T entity;

		try (final InputStream resource = getResourceAsStream(resourceName))
		{
			if (resource == null)
			{
				throw new ApplicationException("Resource "
						+ StringUtil.quote(resourceName) + " not found");
			}
			entity = this.<T> unmarshal(StringUtil.toUtf8(resource), options);
		}
		
		return entity;
	}

	/**
	 * Return the defaultJaxbConfig property.
	 *
	 * @return the defaultJaxbConfig
	 * @see edu.utah.further.core.api.xml.XmlService#getDefaultJaxbConfig()
	 */
	@Override
	public Map<String, Object> getDefaultJaxbConfig()
	{
		// Defensive copy
		return CollectionUtil.newMap(defaultJaxbConfig);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.xml.XmlService#setDefaultJaxbConfig(java.util.Map)
	 */
	@Override
	public void setDefaultJaxbConfig(final Map<String, Object> defaultJaxbConfig)
	{
		this.defaultJaxbConfig = defaultJaxbConfig;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param entity
	 * @param options
	 * @return
	 */
	private MarshallerOptions createBuilderFromParameter(final Object entity,
			final MarshallerOptions options)
	{
		final MarshallerOptions marshallingOptions = (options == null) ? options()
				: options;
		return marshallingOptions.addClass(entity.getClass());
	}

	/**
	 * @param options
	 * @throws JAXBException
	 */
	private void buildContextIfNecessary(final MarshallerOptions options)
			throws JAXBException
	{
		if (options.getContext() == null)
		{
			options.buildContext();
		}
	}
}
