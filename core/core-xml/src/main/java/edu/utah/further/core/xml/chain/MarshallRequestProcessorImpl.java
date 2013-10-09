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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.chain.AbstractUtilityTransformer;
import edu.utah.further.core.chain.MarshallRequestProcessor;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * A generic Marshalling Processor for processing steps which require marshalling.<br/>
 * <p>
 * Expects <tt>sourceAttr</tt> of type {@link Object}.<br/>
 * Expects <tt>schemaAttr</tt> of type {@link Source} </br> Marshalled <tt>resultAttr</tt>
 * is of type {@link String}.
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
@Qualifier("marshallRequestProcessor")
public class MarshallRequestProcessorImpl extends AbstractUtilityTransformer<Object>
		implements MarshallRequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MarshallRequestProcessorImpl.class);

	// ========================= FIELDS =================================

	/**
	 * The name of the attribute of a custom set of root namespace URIs. If
	 * <code>null</code>, the default set is used during marshalling. Not an AttributeName
	 * in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled rootNamespaceUrisAttr;

	/**
	 * Marshalling service. TODO: replace with DI.
	 */
	private final XmlService xmlService = new XmlServiceImpl();

	/**
	 * The name of the attribute where to put the reference to the Schema File. Not an
	 * AttributeName in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled schemaAttr;

	/**
	 * The JAXB configuration to use during unmarshalling, default is empty. Must support
	 * exception marshalling in case of chain flow failure.
	 */
	// private Map<String, Object> jaxbConfig =
	// JaxbConfigurationFactoryBean.TRANSIENT_ANNOTATION_READER_JAXB_CONFIG;
	private Map<String, Object> jaxbConfig = CollectionUtil.<String, Object> newMap();
	
	/**
	 * Additional packages that should be searched while unmarshalling. 
	 */
	private Collection<String> extraPackages;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	// Note: we might run into https://jira.springsource.org/browse/SPR-6094, so do not
	// depend on the parent's afterPropertiesSet() here
	private void afterPropertiesSet()
	{
		Validate.notNull(schemaAttr, "The schema attribute must be set");
	}

	// ========================= IMPL: RequestProcessor =====================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read the input XML from the request
		final Object object = request.getAttribute(getSourceAttr());

		// Marshal the object into XML
		if (log.isDebugEnabled())
		{
			log.debug("Marshalling " + object);
		}
		final String result = marshal(request, object);

		// Save the result in the request
		request.setAttribute(getResultAttr(), result);
		// Marshal the object into XML
		if (log.isTraceEnabled())
		{
			log.trace("Result: " + result);
		}

		// Clean up
		request.setAttribute(getSchemaAttr(), null);
		request.setAttribute(getSourceAttr(), null);

		// OK to continue processing chain
		return false;
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
		return schemaAttr;
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the rootNamespaceUrisAttr property.
	 * 
	 * @param rootNamespaceUrisAttr
	 *            the rootNamespaceUrisAttr to set
	 */
	public void setRootNamespaceUrisAttr(final Labeled rootNamespaceUrisAttr)
	{
		this.rootNamespaceUrisAttr = rootNamespaceUrisAttr;
	}

	/**
	 * Set a new value for the schemaAttr property.
	 * 
	 * @param schemaAttr
	 *            the schemaAttr to set
	 */
	public void setSchemaAttr(final Labeled schemaAttr)
	{
		this.schemaAttr = schemaAttr;
	}

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

	
	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.chain.MarshallRequestProcessor#setExtraPackages(java.util.Collection)
	 */
	@Override
	public void setExtraPackages(final Collection<String> packages)
	{
		this.extraPackages = packages;
	}

	// ========================= PRIVATE METHODS =========================

	/**
	 * @param request
	 * @param object
	 * @return
	 */
	private String marshal(final ChainRequest request, final Object object)
	{
		try
		{
			// Build marshaling context
			final MarshallerOptions options = xmlService
					.options()
					.addClass(object.getClass())
					.addPackages(extraPackages)
					.setJaxbConfig(jaxbConfig);
			// FUR-1108: set custom root namespace URI set if it exists in the request
			final Set<String> rootNamespaceUris = request
					.getAttribute(rootNamespaceUrisAttr);
			if (rootNamespaceUris != null)
			{
				options.setRootNamespaceUris(rootNamespaceUris);
			}

			// Marshal
			return xmlService.marshal(
					object,
					options.buildContext().setValidationSchema(
							(Source) request.getAttribute(schemaAttr)));
		}
		catch (final Exception e)
		{
			if (object == null)
			{
				log.error("Null object passed to marshalling");
			}
			throw new ApplicationException("Marshalling with validation failed", e);
		}
	}
}
