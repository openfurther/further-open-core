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
package edu.utah.further.dts.ws.impl.service.rest;

import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_TRANSLATION_SERVICE_SPRING_NAME;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.Path;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptsTo;
import edu.utah.further.dts.ws.api.domain.OptionsImpl;
import edu.utah.further.dts.ws.api.domain.ViewType;
import edu.utah.further.dts.ws.api.service.rest.TranslateServiceRest;
import edu.utah.further.dts.ws.api.service.soap.TranslateServiceSoap;

/**
 * Terminology translation RESTful web service implementation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Implementation
@Service("dtsTranslateServiceRest")
// Redundant -- @Path already appears in the interface, but required for FUR-1332.
@Path(TranslateServiceRest.PATH)
public class TranslateServiceRestImpl implements TranslateServiceRest
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(TranslateServiceRestImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * SOAP version of this service that allows complex input data types. We delegate most
	 * of the implementation to this object.
	 */
	@Autowired
	@Qualifier(SOAP_TRANSLATION_SERVICE_SPRING_NAME)
	private TranslateServiceSoap translateServiceSoap;

	/**
	 * Executes DTS business operations.
	 */
	@Autowired
	private DtsOperationService dos;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: TranslateServiceRest ====

	/**
	 * Translate a concept in non-standard namespace to an (inversely) associated concept
	 * in FURTHeR namespace.
	 * 
	 * @param source
	 *            source concept information at source namespace: includes namespace,
	 *            property name, property value
	 * @param targetNamespace
	 *            target concept's namespace name
	 * @param targetPropertyName
	 *            name of target concept's property to return
	 * @param options
	 *            options that customize the returned type's view
	 * @return FURTHeR-ExactMatch-associated concept information at target namespace.
	 *         Assumed to be unique in the the entire DTS. Includes at the most namespace,
	 *         property name, property value for human view, or just the property value
	 *         for minimal machine view
	 * @see edu.utah.further.dts.ws.api.service.soap.TranslateServiceSoap#translateConcept(edu.utah.further.dts.api.to.DtsConceptId,
	 *      java.lang.String, edu.utah.further.dts.ws.api.domain.Options)
	 */
	@Override
	public DtsConceptsTo translateConcept(final String namespace,
			final String propertyName, final String propertyValue,
			final String targetNamespace, final String targetPropertyName,
			final ViewType view) throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Input arguments:");
			log.debug("namespace\t= " + namespace);
			log.debug("propertyName\t= " + propertyName);
			log.debug("propertyValue\t= " + propertyValue);
			log.debug("targetPropertyName\t= " + targetPropertyName);
			log.debug("view\t= " + view);
		}

		String localNamespace;
		String localTargetNamespace;

		// FUR-2563 - support id based and string based namespace names
		if (NumberUtils.isNumber(namespace) && NumberUtils.isNumber(targetNamespace))
		{
			final DtsNamespace dtsNamespace = dos.findNamespaceById(Integer.valueOf(
					namespace).intValue());
			final DtsNamespace dtsTargetNamespace = dos.findNamespaceById(Integer
					.valueOf(targetNamespace)
					.intValue());

			if (dtsNamespace == null)
			{
				throw new WsException("Unknown namespace " + namespace);
			}

			if (dtsTargetNamespace == null)
			{
				throw new WsException("Unknown target namespace " + targetNamespace);
			}
			localNamespace = dtsNamespace.getName();
			localTargetNamespace = dtsTargetNamespace.getName();
		}
		else
		{
			localNamespace = namespace;
			localTargetNamespace = targetNamespace;
		}

		// Prepare input beans from raw JAX-RS parameters
		final DtsConceptId source = new DtsConceptId(localNamespace, propertyName,
				propertyValue);
		final OptionsImpl options = new OptionsImpl();
		if (view != null)
		{
			options.setView(view);
		}

		return translateServiceSoap.translateConcept(source, localTargetNamespace,
				targetPropertyName, options);
	}

	/**
	 * Return the preferred name synonym of a concept.
	 * 
	 * @param namespace
	 *            name of the namespace of the source concept
	 * @param propertyName
	 *            source concept property name
	 * @param propertyValue
	 *            source concept property value
	 * @param targetPropertyName
	 *            target concept's property name whose value is output
	 * @return concept's preferred name synonym
	 * @see edu.utah.further.dts.ws.api.service.rest.TranslateServiceRest#getConceptPreferredName(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public String getConceptPreferredName(final String namespace,
			final String propertyName, final String propertyValue) throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Input arguments:");
			log.debug("namespace\t= " + namespace);
			log.debug("propertyName\t= " + propertyName);
			log.debug("propertyValue\t= " + propertyValue);
		}

		// Prepare input beans from raw JAX-RS parameters
		final DtsConceptId conceptId = new DtsConceptId(namespace, propertyName,
				propertyValue);

		return translateServiceSoap.getConceptPreferredName(conceptId);
	}

	// ========================= PRIVATE METHODS ===========================
}
