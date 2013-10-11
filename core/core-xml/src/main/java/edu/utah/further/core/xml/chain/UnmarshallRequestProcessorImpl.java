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
package edu.utah.further.core.xml.chain;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.chain.AbstractUtilityTransformer;
import edu.utah.further.core.chain.MarshallRequestProcessor;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * A generic Unmarshalling Processor for processing steps which require
 * unmarshalling.</br>
 * 
 * Expects a <tt>sourceAttr</tt> of type {@link InputStream}</br> Expects a
 * <tt>jaxbPackageAttr</tt> of type {@link String}</br> <tt>resultAttr</tt> will be of
 * type {@link Object}</br>
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 23, 2010
 */
@Qualifier("unmarshallRequestProcessor")
public class UnmarshallRequestProcessorImpl extends AbstractUtilityTransformer<Object>
		implements MarshallRequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UnmarshallRequestProcessorImpl.class);

	// ========================= FIELDS =================================

	/**
	 * The name of the attribute where to retrieve the package names that should be
	 * considered when marshalling. A {@link Collection} of Strings is expected.
	 */
	private Labeled marshalPkgsAttr;

	/**
	 * Marshalling service. TODO: replace with DI.
	 */
	private final XmlService xmlService = new XmlServiceImpl();

	/**
	 * The JAXB configuration to use during unmarshalling, default is empty
	 */
	private Map<String, Object> jaxbConfig = Collections.<String, Object> emptyMap();

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPL: RequestProcessor =====================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@SuppressWarnings("resource")
	// Resource is closed in the finally block
	@Override
	public boolean process(final ChainRequest request)
	{
		InputStream inputStream = null;
		try
		{
			// Set up unmarshaller: configure the JAXB context
			Validate.notNull(request.getAttribute(getMarshalPkgsAttr()),
					"JAXB unmarshalling requires jab packages attribute");

			// Read the input XML from the request
			final Object source = request.getAttribute(getSourceAttr());
			inputStream = getSourceAsInputStream(source);
			printInputXmlForDebugging(inputStream);

			// Unmarshal the XML into an object
			if (log.isDebugEnabled())
			{
				log.debug("Unmarshalling");
			}
			final Object result = unmarshal(request, inputStream);
			if (log.isDebugEnabled())
			{
				log.debug("result type " + StringUtil.getClassAsStringNullSafe(result));
			}

			// Save the result in the request
			request.setAttribute(getResultAttr(), result);

			// Clean up
			request.setAttribute(getSourceAttr(), null);

			// OK to continue processing chain
			return false;
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (final IOException e)
				{
					// ignore
				}
			}
		}
	}

	// ========================= IMPL: MarshallRequestProcessor ==========

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.chain.MarshallRequestProcessor#getSchemaAttr()
	 */
	@Override
	public Labeled getSchemaAttr()
	{
		throw new UnsupportedOperationException(
				"Unmarshal request processor does not support the schema attribute at this time.");
	}

	/**
	 * Return the marshalPkgsAttr property.
	 * 
	 * @return the marshalPkgsAttr
	 */
	@Override
	public Labeled getMarshalPkgsAttr()
	{
		return marshalPkgsAttr;
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the jaxbConfig property.
	 * 
	 * @param jaxbConfig
	 *            the jaxbConfig to set
	 */
	public void setJaxbConfig(final Map<String, Object> jaxbConfig)
	{
		this.jaxbConfig = jaxbConfig;
	}

	/**
	 * Set a new value for the marshalPkgsAttr property.
	 * 
	 * @param marshalPkgsAttr
	 *            the marshalPkgsAttr to set
	 */
	public void setMarshalPkgsAttr(final Labeled marshalPkgsAttr)
	{
		this.marshalPkgsAttr = marshalPkgsAttr;
	}

	// ========================= PRIVATE METHODS =========================

	/**
	 * @param request
	 * @param inputStream
	 * @return
	 */
	private Object unmarshal(final ChainRequest request, final InputStream inputStream)
	{
		final Object extraPackages = request.getAttribute(getMarshalPkgsAttr());

		if (!Collection.class.isAssignableFrom(extraPackages.getClass()))
		{
			throw new RuntimeException("Expected a Collection for jaxb packages but got "
					+ extraPackages.getClass());
		}

		try
		{
			return xmlService.unmarshal(
					inputStream,
					xmlService
							.options()
							.addPackages(
									(Collection<String>) request
											.getAttribute(getMarshalPkgsAttr()))
							.setJaxbConfig(jaxbConfig));
		}
		catch (final JAXBException e)
		{
			throw new RuntimeException(
					"Unable to continue chain processing, unmarshalling failed", e);
		}
	}

	/**
	 * @param inputStream
	 */
	private void printInputXmlForDebugging(final InputStream inputStream)
	{
		if (log.isTraceEnabled() && inputStream.markSupported())
		{
			inputStream.mark(0);
			log.trace("Unmarshalling input is "
					+ IoUtil.getInputStreamAsString(inputStream));
			try
			{
				inputStream.reset();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param source
	 * @return
	 */
	private InputStream getSourceAsInputStream(final Object source)
	{
		if (!instanceOf(source, InputStream.class))
		{
			// Explicitly throw an error as we can't continue
			throw new RuntimeException(
					"Source attribute for unmarshalling must be of type InputStream but got type "
							+ source.getClass());
		}
		final InputStream inputStream = (InputStream) source;
		return inputStream;
	}
}
