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
package edu.utah.further.dts.ws.impl.service.soap;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.ErrorCode.MISSING_INPUT_ARGUMENT;
import static edu.utah.further.core.api.constant.ErrorCode.PROPERTY_NOT_FOUND;
import static edu.utah.further.dts.ws.api.domain.ViewType.HUMAN;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_TRANSLATION_SERVICE_INTERFACE;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_TRANSLATION_SERVICE_SPRING_NAME;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.TARGET_PROPERTY_NAME;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.TextTemplate;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptsTo;
import edu.utah.further.dts.ws.api.domain.Options;
import edu.utah.further.dts.ws.api.domain.OptionsImpl;
import edu.utah.further.dts.ws.api.service.soap.TranslateServiceSoap;
import edu.utah.further.dts.ws.impl.util.DtsWsUtil;

/**
 * Terminology translation web services implementation.
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
@Service(SOAP_TRANSLATION_SERVICE_SPRING_NAME)
@WebService(endpointInterface = SOAP_TRANSLATION_SERVICE_INTERFACE)
public class TranslateServiceSoapImpl implements TranslateServiceSoap
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(TranslateServiceSoapImpl.class);

	/**
	 * Default value of DTS services exception throwing flag.
	 */
	private static final boolean THROW_EXCEPTION_ON_FAILURE = false;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	@Autowired
	private DtsOperationService dos;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: TranslateService ========

	/**
	 * Translate a concept in one namespace to an (inversely) associated concept in
	 * another namespace. Automatically detects whether the source namespace is in a
	 * standard FURTHeR namespace or in a non-standard FURTHeR namespace.
	 *
	 * @param source
	 *            source concept information at source namespace: includes namespace,
	 *            property name, property value
	 * @param targetNamespace
	 *            name of target concept's namespace
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
	public DtsConceptsTo translateConcept(final DtsConceptId source,
			final String targetNamespace, final String targetPropertyName,
			final OptionsImpl options) throws WsException
	{
		// Validate input arguments
		DtsWsUtil.validateConceptIdArgument(source);
		validateInputArgumentsForTranslateConcept(targetPropertyName, options);

		if (log.isDebugEnabled())
		{
			log.debug("Translating concept from " + source + " to FURTHeR namespace");
		}
		final DtsOptions translateOptions = DtsOptions
				.newInstanceFromThrowExceptionOnFailure(THROW_EXCEPTION_ON_FAILURE)
				.setTargetNamespace(targetNamespace);
		final List<DtsConcept> targetConcepts = dos.translateConcept(source,
				translateOptions).getConcepts();
		return populateResultObject(targetPropertyName, options, targetConcepts);
	}

	/**
	 * Return the preferred name synonym of a concept.
	 *
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @return concept's preferred name synonym
	 * @see edu.utah.further.dts.ws.api.service.soap.TranslateServiceSoap#getConceptPreferredName(edu.utah.further.dts.api.to.DtsConceptId)
	 */
	@Override
	public String getConceptPreferredName(final DtsConceptId conceptId)
			throws WsException
	{
		// Validate input arguments
		DtsWsUtil.validateConceptIdArgument(conceptId);

		if (log.isDebugEnabled())
		{
			log.debug("Getting preffered name of concept " + conceptId);
		}
		return dos.getConceptPreferredName(conceptId, DtsOptions
				.newInstanceFromThrowExceptionOnFailure(THROW_EXCEPTION_ON_FAILURE));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Validate input arguments to {@link #translateConcept(DtsConceptPair)}.
	 *
	 * @param source
	 * @param targetPropertyName
	 * @param options
	 */
	private void validateInputArgumentsForTranslateConcept(
			final String targetPropertyName, final Options options) throws WsException
	{
		final TextTemplate missingArgumentMessage = new TextTemplate(
				"Missing argument %ARGUMENT_NAME%", asList("ARGUMENT_NAME"));
		if ((options.getView() != HUMAN) && isBlank(targetPropertyName))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT,
					missingArgumentMessage.evaluate(asList(TARGET_PROPERTY_NAME)));
		}
	}

	/**
	 * @param targetPropertyName
	 * @param options
	 * @param targetConcept
	 * @return
	 */
	private DtsConceptsTo populateResultObject(final String targetPropertyName,
			final Options options, final List<DtsConcept> targetConcepts)
			throws WsException
	{
		final List<DtsConceptId> results = newList();
		for (final DtsConcept targetConcept : targetConcepts)
		{
			final DtsConceptId result = new DtsConceptId();

			// If human view selected, turned on debugging fields in the returned
			// representation
			if (options.getView() == HUMAN)
			{
				result.setPropertyName(targetPropertyName);
				final DtsNamespace targetNamespace = dos.findNamespaceById(targetConcept
						.getNamespaceId());
				result.setNamespace(targetNamespace.getName());
			}
			final List<String> propertyValue = targetConcept
					.getPropertyValue(targetPropertyName);
			if (propertyValue == null || propertyValue.size() == 0 || isBlank(propertyValue.get(0)))
			{
				throw new WsException(PROPERTY_NOT_FOUND,
						"Did not find target concept property " + targetPropertyName);
			}

			result.setPropertyValue(propertyValue.get(0));

			results.add(result);
		}

		return new DtsConceptsTo(results);
	}
}
