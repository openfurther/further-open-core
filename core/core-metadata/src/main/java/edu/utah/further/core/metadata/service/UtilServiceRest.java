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
package edu.utah.further.core.metadata.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.core.metadata.to.MetaData;

/**
 * REST web service utilities.
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
@Path(UtilServiceRest.PATH)
@Produces("application/xml")
@Documentation(name = "DTS utility WS-REST", description = "Restful web service utilities.")
public interface UtilServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * Web service base path of this class.
	 */
	static final String PATH = "/util";

	// ========================= METHODS ===================================

	/**
	 * Return the list of all known RESTful services' meta data objects.
	 *
	 * @return a composite MD object containing a list of service meta data objects
	 */
	@GET
	@Path("/soap")
	@ExamplePath("soap")
	@Documentation(name = "SOAP Service list", description = "Return the list of all known"
			+ " services' meta data objects.")
	MetaData getAllSoapServiceMd() throws WsException;

	/**
	 * Return the list of all known RESTful services' meta data objects.
	 *
	 * @return a composite MD object containing a list of service meta data objects
	 */
	@GET
	@Path("/rest")
	@ExamplePath("rest")
	@Documentation(name = "REST Service list", description = "Return the list of all known"
			+ " services' meta data objects.")
	MetaData getAllRestServiceMd() throws WsException;

	// TODO: add methods to get a specific REST/SOAP service method's MD

	/**
	 * Return the release version number of web services.
	 *
	 * @return version number
	 */
	@GET
	@Produces("text/html")
	@Path("/version")
	@ExamplePath("version")
	@Documentation(name = "Release version", description = "Return the release version number"
			+ " of this web service suite.")
	String getVersion() throws WsException;
}
