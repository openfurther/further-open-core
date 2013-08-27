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
package edu.utah.further.dts.ws.api.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.dts.ws.api.domain.DtsConceptList;
import edu.utah.further.dts.ws.api.domain.DtsConceptToWsImpl;
import edu.utah.further.dts.ws.api.domain.DtsNamespaceList;

/**
 * REST search/query web services.
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
 * @version Feb 18, 2009
 */
@Path(SearchServiceRest.PATH)
@Produces("application/xml")
@Documentation(name = "DTS search/query WS-REST", description = "Restful "
		+ "search/query web service")
public interface SearchServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * Web service base path of this class.
	 */
	static final String PATH = "/search";

	// ========================= WEB METHODS ===============================

	/**
	 * Return the list of namespaces. If no namespaces are found, returns an empty list.
	 *
	 * @return the list of namespaces
	 */
	@GET
	@Path("/ns")
	@ExamplePath("ns")
	@Documentation(name = "Namespace list", description = "Return the list of all known namespaces.")
	DtsNamespaceList getNamespaceList() throws WsException;

	/**
	 * Return the a namespace by ID.
	 *
	 * @param namespaceId
	 *            DTS namespace ID
	 * @return a single namespace with the specified ID
	 */
	@GET
	@Path("/ns/{namespaceId}")
	@ExamplePath("ns/30")
	@Documentation(name = "Namespace by ID", description = "Return namespace by ID")
	DtsNamespaceToImpl getNamespace(
			@PathParam("namespaceId") @Documentation(description = "namespace ID") int namespaceId)
			throws WsException;

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
	@GET
	@Path("/concept/{namespace}/{propertyName}/{propertyValue}")
	@ExamplePath("concept/UUEDW/DWID/300195")
	@Documentation(name = "Find concept", description = "Return the unique DTS concept that matches input criteria.")
	DtsConceptToWsImpl findConcept(
			@PathParam("namespace") @Documentation(description = "name of the namespace of "
					+ "the source concept") String namespace,
			@PathParam("propertyName") @Documentation(description = "source concept property "
					+ "name") String propertyName,
			@PathParam("propertyValue") @Documentation(description = "source concept property "
					+ "value") String propertyValue) throws WsException;

	/**
	 * Return a fully loaded DTS concept by unique concept ID.
	 *
	 * @param namespaceId
	 *            ID of concept's namespace
	 * @param conceptId
	 *            ID of concept
	 * @return corresponding DTS concept
	 */
	@GET
	@Path("/concept/{namespaceId}/{conceptId}")
	@ExamplePath("concept/30/38033")
	@Documentation(name = "Find concept by unique ID", description = "Return a fully loaded DTS concept by unique concept ID.")
	DtsConceptToWsImpl findConceptByUniqueId(
			@PathParam("namespaceId") @Documentation(description = "namespace ID") int namespaceId,
			@PathParam("conceptId") @Documentation(description = "concept ID within the namespace") int conceptId)
			throws WsException;

	/**
	 * Return the list of children of a DTS concept by unique concept ID.
	 *
	 * @param namespaceId
	 *            ID of concept's namespace
	 * @param conceptId
	 *            ID of concept
	 * @return corresponding DTS concept
	 */
	@GET
	@Path("/concept/{namespaceId}/{conceptId}/children")
	@ExamplePath("concept/32803/2/children")
	@Documentation(name = "Find concept children list by unique ID", description = "Return a fully loaded DTS concept by unique concept ID.")
	DtsConceptList findChildren(
			@PathParam("namespaceId") @Documentation(description = "namespace ID") int namespaceId,
			@PathParam("conceptId") @Documentation(description = "concept ID within the namespace") int conceptId)
			throws WsException;

	/**
	 * Return the list concepts that match a name.
	 *
	 * @param namespaceId
	 *            ID of namespace to search in
	 * @param conceptName
	 *            partial concept name string to match
	 * @return corresponding DTS concept
	 */
	@GET
	@Path("/concept/{namespaceId}/match/{conceptName}")
	@ExamplePath("concept/30/match/colonosc")
	@Documentation(name = "Match concepts by name", description = "Return all concepts whose name starts with a specified string.")
	DtsConceptList findConceptsByName(
			@PathParam("namespaceId") @Documentation(description = "namespace ID to search in") final int namespaceId,
			@PathParam("conceptName") @Documentation(description = "partial concept name string to match") final String conceptName)
			throws WsException;

	// ========================= METHODS ===================================

	/**
	 * Return the URI info context of this service.
	 *
	 * @return the uriInfo context object
	 */
	UriInfo getUriInfo();

	/**
	 * set the URI info context of this service.
	 *
	 */
	void setUriInfo(@Context UriInfo uriInfo);
}
