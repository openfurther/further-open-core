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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.dts.api.to.DtsConceptsTo;
import edu.utah.further.dts.ws.api.domain.ViewType;

/**
 * Terminology translation REST web services - central class. Each method is a different
 * web service.
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
@Path(TranslateServiceRest.PATH)
@Produces("application/xml")
@Documentation(name = "DTS Translation WS-REST", description = "RESTful Web services "
		+ "that perform translation of DTS concepts.")
public interface TranslateServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * Web service base path of this class.
	 */
	static final String PATH = "/translate";

	// ========================= METHODS ===================================

	/**
	 * Translate a concept in a non-standard namespace to the associated concept in a
	 * standard FURTHeR namespace.
	 *
	 * @param namespace
	 *            name of the namespace of the source concept
	 * @param propertyName
	 *            source concept property name
	 * @param propertyValue
	 *            source concept property value
	 * @param targetNamespace
	 *            target concept's namespace name
	 * @param targetPropertyName
	 *            target concept's property name whose value is output
	 * @param view
	 *            returned view. In a MACHINE view, only the target property value is
	 *            returned. If HUMAN, the target namespace and target property name are
	 *            returned
	 * @return the value of the target concept's <code>targetPropertyName</code> property
	 * @throws WsException
	 *             if translation fails
	 */
	@GET
	@Path("/{namespace}/{propertyName}/{propertyValue}/{targetNamespace}/{targetPropertyName}")
	@ExamplePath("UUEDW/DWID/300195/SNOMED%20CT/Code%20in%20Source?view=HUMAN")
	@Documentation(name = "Translate concept", description = "Translate "
			+ "a in a non-standard namespace to the inversely associated concept in a standard FURTHeR "
			+ "namespace. Returns a custom view (e.g. human-readable).")
	DtsConceptsTo translateConcept(
			@PathParam("namespace") @Documentation(description = "name of the "
					+ "non-standard namespace of " + "the source concept") String namespace,
			@PathParam("propertyName") @Documentation(description = "source concept "
					+ "property " + "name") String propertyName,
			@PathParam("propertyValue") @Documentation(description = "source concept "
					+ "property " + "value") String propertyValue,
			@PathParam("targetNamespace") @Documentation(description = "target concept's "
					+ "namespace name") String targetNamespace,
			@PathParam("targetPropertyName") @Documentation(description = "the value of "
					+ "the target concept's <code>targetPropertyName</code> "
					+ "property in the FURTHeR namespace") String targetPropertyName,
			@QueryParam("view") @DefaultValue("MACHINE") @Documentation(description = "returned view. In a MACHINE view, "
					+ "only the target property value is returned. If HUMAN, the target "
					+ "namespace and target property name are returned") ViewType view)
			throws WsException;

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
	 */
	@GET
	@Produces("text/html")
	@Path("/preferred/{namespace}/{propertyName}/{propertyValue}")
	@ExamplePath("preferred/UUEDW/DWID/102548")
	@Documentation(name = "Concept preferred name", description = "Return the preferred name synonym of a concept.")
	String getConceptPreferredName(
			@PathParam("namespace") @Documentation(description = "name of the namespace of "
					+ "the concept") String namespace,
			@PathParam("propertyName") @Documentation(description = "Concept property "
					+ "name") String propertyName,
			@PathParam("propertyValue") @Documentation(description = "Concept property "
					+ "value") String propertyValue) throws WsException;
}
