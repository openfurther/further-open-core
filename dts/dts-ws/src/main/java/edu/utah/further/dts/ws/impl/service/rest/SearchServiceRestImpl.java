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

import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOAP_SEARCH_SERVICE_SPRING_NAME;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.dts.ws.api.domain.DtsConceptList;
import edu.utah.further.dts.ws.api.domain.DtsConceptToWsImpl;
import edu.utah.further.dts.ws.api.domain.DtsNamespaceList;
import edu.utah.further.dts.ws.api.service.rest.SearchServiceRest;
import edu.utah.further.dts.ws.api.service.soap.SearchServiceSoap;

/**
 * Terminology search/query RESTful web service implementation.
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
@Service("dtsSearchServiceRest")
// Redundant -- @Path already appears in the interface, but required for FUR-1332.
@Path(SearchServiceRest.PATH)
public class SearchServiceRestImpl implements SearchServiceRest
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SearchServiceRestImpl.class);

	/**
	 * Double slash in path strings.
	 */
	@SuppressWarnings("unused")
	private static final String DOUBLE_SLASH = VIRTUAL_DIRECTORY + VIRTUAL_DIRECTORY;

	// ========================= DEPENDENCIES ==============================

	/**
	 * For REST links.
	 */
	private UriInfo uriInfo;

	// ========================= FIELDS ====================================

	/**
	 * SOAP version of this service that allows complex input data types. We delegate most
	 * of the implementation to this object.
	 */
	@Autowired
	@Qualifier(SOAP_SEARCH_SERVICE_SPRING_NAME)
	private SearchServiceSoap searchServiceSoap;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: SearchServiceRest =========

	/**
	 * @return
	 * @see edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#getNamespaceList()
	 */
	@Override
	public DtsNamespaceList getNamespaceList() throws WsException
	{
		return searchServiceSoap.getNamespaceList();
	}

	/**
	 * @see edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#getNamespace(int)
	 * @param namespaceId
	 * @return
	 * @throws WsException
	 */
	@Override
	public DtsNamespaceToImpl getNamespace(final int namespaceId) throws WsException
	{
		return searchServiceSoap.getNamespace(namespaceId);
	}

	/**
	 * Return a fully loaded DTS concept.
	 *
	 * @param namespace
	 *            name of the namespace of the source concept
	 * @param propertyName
	 *            source concept property name
	 * @param propertyValue
	 *            source concept property value
	 * @param targetPropertyName
	 *            target concept's property name whose value is output
	 * @return corresponding DTS concept
	 */
	@Override
	public DtsConceptToWsImpl findConcept(final String namespace,
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
		return searchServiceSoap.findConcept(conceptId);
	}

	/**
	 * Return a fully loaded DTS concept by uniquely-identifying information.
	 *
	 * @param namespaceId
	 *            ID of concept's namespace
	 * @param conceptId
	 *            ID of concept
	 * @return corresponding DTS concept
	 * @see edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#findConceptByUniqueId(int,
	 *      int)
	 */
	@Override
	public DtsConceptToWsImpl findConceptByUniqueId(final int namespaceId,
			final int conceptId) throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Input arguments:");
			log.debug("namespaceId\t= " + namespaceId);
			log.debug("conceptId\t= " + conceptId);
		}

		// Prepare input beans from raw JAX-RS parameters
		final DtsConceptUniqueId conceptUniqueId = new DtsConceptUniqueId(namespaceId,
				conceptId);
		return searchServiceSoap.findConceptByUniqueId(conceptUniqueId);
	}

	/**
	 * @param namespaceId
	 * @param conceptId
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#findChildren(int,
	 *      int)
	 */
	@Override
	public DtsConceptList findChildren(final int namespaceId, final int conceptId)
			throws WsException
	{
		return searchServiceSoap.findChildren(namespaceId, conceptId);
	}

	/**
	 * @param namespaceId
	 * @param conceptName
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#findConceptsByName(int,
	 *      java.lang.String)
	 */
	@Override
	public DtsConceptList findConceptsByName(final int namespaceId, final String conceptName)
			throws WsException
	{
		return searchServiceSoap.findConceptsByName(namespaceId, conceptName);
	}

	/**
	 * Return the uriInfo property.
	 *
	 * @return the uriInfo
	 */
	@Override
	public UriInfo getUriInfo()
	{
		return uriInfo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.dts.ws.api.service.rest.SearchServiceRest#setUriInfo(javax.ws.
	 * rs.core.UriInfo)
	 */
	@Override
	public void setUriInfo(final UriInfo uriInfo)
	{
		this.uriInfo = uriInfo;
	}

	// ========================= PRIVATE METHODS ===========================

}
