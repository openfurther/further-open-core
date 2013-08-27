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
package edu.utah.further.dts.ws.api.service.soap;

import javax.jws.WebService;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.dts.ws.api.domain.DtsConceptList;
import edu.utah.further.dts.ws.api.domain.DtsConceptToWsImpl;
import edu.utah.further.dts.ws.api.domain.DtsNamespaceList;

/**
 * Concept search/query SOAP web services - central class. Each method is a different web
 * service.
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
@WebService
@Documentation(name = "DTS concept search services", description = "DTS concept search services")
public interface SearchServiceSoap
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Return the list of namespaces. If no namespaces are found, returns an empty list.
	 *
	 * @return the list of namespaces
	 */
	@Documentation(name = "Namespace list", description = "Return the list of namespaces")
	DtsNamespaceList getNamespaceList() throws WsException;

	/**
	 * Return the a namespace by ID.
	 *
	 * @param namespaceId
	 *            DTS namespace ID
	 * @return a single namespace with the specified ID
	 */
	@Documentation(name = "Namespace by ID", description = "Return namespace by ID")
	DtsNamespaceToImpl getNamespace(int namespaceId) throws WsException;

	/**
	 * Return a fully loaded DTS concept.
	 *
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @return corresponding DTS concept
	 */
	@Documentation(name = "Find concept by property", description = "Find a unique concept by property")
	DtsConceptToWsImpl findConcept(DtsConceptId conceptId) throws WsException;

	/**
	 * Return a fully loaded DTS concept.
	 *
	 * @param conceptUniqueId
	 *            concept uniquely-identifying information: namespace ID and concept ID
	 * @return corresponding DTS concept
	 */
	@Documentation(name = "Find concept by concept ID", description = "Find concept by unique concept identifier")
	DtsConceptToWsImpl findConceptByUniqueId(DtsConceptUniqueId conceptUniqueId)
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
	@Documentation(name = "Find concept children list by unique ID", description = "Return a fully loaded DTS concept by unique concept ID.")
	DtsConceptList findChildren(int namespaceId, int conceptId) throws WsException;

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
	 */
	@Documentation(name = "Find matching concepts by name", description = "Find matching concepts by name")
	DtsConceptList findConceptsByName(final int namespaceId, String conceptName)
			throws WsException;
}
