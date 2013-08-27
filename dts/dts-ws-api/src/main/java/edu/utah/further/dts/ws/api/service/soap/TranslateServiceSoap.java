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

import javax.jws.WebParam;
import javax.jws.WebService;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptsTo;
import edu.utah.further.dts.ws.api.domain.OptionsImpl;

/**
 * Terminology translation SOAP web services - central class. Each method is a different
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
@WebService
@Documentation(name = "DTS concept translation services", description = "DTS concept translation services")
public interface TranslateServiceSoap
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Translate a concept in a non-standard namespace to the inversely associated concept
	 * in a standard FURTHeR namespace.
	 * 
	 * @param source
	 *            source concept information at source namespace: includes namespace,
	 *            property name, property value. The namespace must be a non-standard
	 *            namespace
	 * @param targetNamespace
	 *            name of target concept's namespace
	 * @param targetPropertyName
	 *            name of target concept's property to return
	 * @param options
	 *            options that customize the returned type's view
	 * @return FURTHeR-ExactMatch-associated concept information at the target FURTHeR
	 *         namespace. Assumed to be unique in the the entire DTS. Includes at the most
	 *         namespace, property name, property value for human view, or just the
	 *         property value for minimal machine view
	 */
	@Documentation(name = "Translate concept", description = "Translate a "
			+ "concept in a non-standard namespace to the inversely associated concept in a "
			+ "standard FURTHeR namespace. Returns the machine view by default.")
	DtsConceptsTo translateConcept(@WebParam(name = "source") DtsConceptId source,
			@WebParam(name = "targetNamespace") String targetNamespace,
			@WebParam(name = "targetPropertyName") String targetPropertyName,
			@WebParam(name = "options") OptionsImpl options) throws WsException;

	/**
	 * Return the preferred name synonym of a concept.
	 * 
	 * @param conceptId
	 *            concept information at source namespace: includes namespace, property
	 *            name, property value
	 * @return concept's preferred name synonym
	 */
	@Documentation(name = "Concept preferred name", description = "Return the preferred name synonym of a concept.")
	String getConceptPreferredName(@WebParam(name = "concept") DtsConceptId conceptId)
			throws WsException;
}
