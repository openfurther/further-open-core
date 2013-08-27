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

import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_SEARCH_SERVICE_INTERFACE;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_SEARCH_SERVICE_SPRING_NAME;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSetType;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.dts.ws.api.domain.DtsConceptList;
import edu.utah.further.dts.ws.api.domain.DtsConceptToWsImpl;
import edu.utah.further.dts.ws.api.domain.DtsNamespaceList;
import edu.utah.further.dts.ws.api.service.rest.SearchServiceRest;
import edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap;
import edu.utah.further.dts.ws.impl.util.DtsWsUtil;

/**
 * Terminology search/query web services implementation.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Implementation
@Service(SOAP_SEARCH_SERVICE_SPRING_NAME)
@WebService(endpointInterface = SOAP_SEARCH_SERVICE_INTERFACE)
public class SearchServiceSoapImpl implements SearchServiceSoap
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SearchServiceSoapImpl.class);

	/**
	 * Cached instance of a commonly used options object.
	 */
	private static final DtsOptions DTS_OPTIONS_ALL_ATTRIBUTES_THROWS_EXCEPTION = new DtsOptions(
			DtsAttributeSetType.ALL_ATTRIBUTES).setThrowExceptionOnFailure(true);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	@Autowired
	private DtsOperationService dos;

	/**
	 * REST version of this service from which we retrieve URIs for RESTful links. These
	 * links are required by that REST service, which however delegates back to this
	 * object to build the XML result. Therefore, we need cyclic dependencies to
	 * accomplish the required call-back.
	 */
	@Autowired
	@Qualifier("dtsSearchServiceRest")
	private SearchServiceRest searchServiceRest;

	/**
	 * For REST links.
	 */
	private volatile UriInfo uriInfo;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: TranslateService ========

	/**
	 * @return
	 * @see edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap#getNamespaceList()
	 */
	@Override
	public DtsNamespaceList getNamespaceList() throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Returning namespace list");
		}
		return new DtsNamespaceList(dos.getNamespaceList());
	}

	/**
	 * @see edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap#getNamespace(int)
	 * @param namespaceId
	 * @return
	 * @throws WsException
	 */
	@Override
	@Documentation(name = "Namespace list", description = "Return namespace by ID")
	public DtsNamespaceToImpl getNamespace(final int namespaceId) throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Returning namespace list");
		}
		return new DtsNamespaceToImpl(dos.findNamespaceById(namespaceId));
	}

	/**
	 * Return a fully loaded DTS concept.
	 *
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @return corresponding DTS concept
	 * @see edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap#findConcept(edu.utah.further.dts.api.to.DtsConceptId)
	 */
	@Override
	public DtsConceptToWsImpl findConcept(final DtsConceptId conceptId)
			throws WsException
	{
		// Validate input arguments
		DtsWsUtil.validateConceptIdArgument(conceptId);

		if (log.isDebugEnabled())
		{
			log.debug("Getting preffered name of concept " + conceptId);
		}
		final DtsConcept concept = dos.findConceptById(conceptId,
				DTS_OPTIONS_ALL_ATTRIBUTES_THROWS_EXCEPTION);
		// Copy business layer result to a TO for XML serialization
		// TODO: add URI info c-tor arg somehow?! Only in REST impls of
		// course
		final UriBuilder urlBuilder = getFindConceptByUniqueIdRestUri();
		final DtsConceptToWsImpl conceptTo = new DtsConceptToWsImpl(urlBuilder)
				.copyFrom(concept);
		return conceptTo;
	}

	/**
	 * Return a fully loaded DTS concept.
	 *
	 * @param conceptUniqueId
	 *            concept uniquely-identifying information: namespace ID and concept ID
	 * @return corresponding DTS concept
	 */
	@Override
	public DtsConceptToWsImpl findConceptByUniqueId(
			final DtsConceptUniqueId conceptUniqueId) throws WsException
	{
		final DtsConcept concept = findConceptByUniqueIdHelper(conceptUniqueId);
		// Copy business layer result to a TO for XML serialization
		final UriBuilder urlBuilder = getFindConceptByUniqueIdRestUri();
		final DtsConceptToWsImpl conceptTo = new DtsConceptToWsImpl(urlBuilder)
				.copyFrom(concept);
		return conceptTo;
	}

	/**
	 * Return the list of children of a DTS concept by unique concept ID.
	 *
	 * @param namespaceId
	 *            ID of concept's namespace
	 * @param conceptId
	 *            ID of concept
	 * @return corresponding DTS concept
	 * @see edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap#findChildren(int,
	 *      int)
	 */
	@Override
	public DtsConceptList findChildren(final int namespaceId, final int conceptId)
			throws WsException
	{
		// Prepare input beans from raw JAX-RS parameters
		final DtsConceptUniqueId conceptUniqueId = new DtsConceptUniqueId(namespaceId,
				conceptId);
		final DtsConcept concept = findConceptByUniqueIdHelper(conceptUniqueId);
		return new DtsConceptList(dos.getChildren(concept));
	}

	/**
	 * Return a concept by namespace ID and name.
	 *
	 * @param namespace
	 *            namespace ID
	 * @param conceptName
	 *            concept name
	 * @return the concept
	 * @throws ApplicationException
	 *             if a DTS error occurs
	 * @throws WsException
	 * @see edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap#findConceptsByName(int,
	 *      java.lang.String)
	 */
	@Override
	public DtsConceptList findConceptsByName(final int namespaceId,
			final String conceptName) throws WsException
	{
		final List<DtsConcept> concepts = dos.findConceptsByName(namespaceId, conceptName);
		return new DtsConceptList(concepts);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the URI info context object. Sets up URI Info in a double-locking singleton
	 * pattern and then returns it. Must be done after both REST, SOAP service objects are
	 * fully spring-configured.
	 *
	 * @return the URI info context object
	 */
	private UriInfo getUriInfo()
	{
		// URI info will only be set in an integration environment. During unit testing,
		// it will always return null unless we mock this method in the future.
		if (uriInfo == null)
		{
			synchronized (this)
			{
				if (uriInfo == null)
				{
					uriInfo = searchServiceRest.getUriInfo();
				}
			}
		}
		return uriInfo;
	}

	/**
	 * @return
	 */
	private UriBuilder getFindConceptByUniqueIdRestUri()
	{
		try
		{
			return (getUriInfo() == null) ? null : getUriInfo().getBaseUriBuilder().path(
					searchServiceRest.getClass(), "findConceptByUniqueId");
		}
		catch (final Throwable e)
		{
			// It seems that in SOAP calls getBsaeUriBuilder() throws a NPE. Therefore,
			// we do not support it but simply return null, which will simply skip REST
			// link creation in the returned SOAP result.
			return null;
		}
	}

	/**
	 * @param conceptUniqueId
	 * @return
	 * @throws WsException
	 */
	private DtsConcept findConceptByUniqueIdHelper(
			final DtsConceptUniqueId conceptUniqueId) throws WsException
	{
		// Validate input arguments
		DtsWsUtil.validateConceptUniqueIdArgument(conceptUniqueId);

		if (log.isDebugEnabled())
		{
			log.debug("Getting preffered name of concept " + conceptUniqueId);
		}
		final DtsConcept concept = dos.findConceptByUniqueId(conceptUniqueId,
				DTS_OPTIONS_ALL_ATTRIBUTES_THROWS_EXCEPTION);
		return concept;
	}
}
