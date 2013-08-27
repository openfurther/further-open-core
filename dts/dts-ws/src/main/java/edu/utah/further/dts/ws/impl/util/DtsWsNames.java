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
package edu.utah.further.dts.ws.impl.util;

import static edu.utah.further.core.api.constant.Strings.PROPERTY_SCOPE_CHAR;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;
import edu.utah.further.core.api.context.Singleton;

/**
 * Centralizes resource error codes and XML tag names specific to the MDR web service
 * resources.
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
 * @version Nov 11, 2008
 */
@Api
@Singleton
@Constants
public abstract class DtsWsNames
{
	// ========================= CONSTANTS: SOAP SERVICE ENDPOINTS =========

	/**
	 * Service endpoint interface name prefix.
	 */
	public static final String SOAP_SERVICE_PACKAGE = "edu.utah.further.dts.ws.api.service.soap";

	/**
	 * Spring bean name of SOAP search/query service.
	 */
	public static final String SOAP_SEARCH_SERVICE_SPRING_NAME = "dtsSearchService";

	/**
	 * Spring bean name of SOAP translation service.
	 */
	public static final String SOAP_TRANSLATION_SERVICE_SPRING_NAME = "dtsTranslateService";

	/**
	 * Fully-qualified class name of SOAP search/query service interface.
	 */
	public static final String SOAP_SEARCH_SERVICE_INTERFACE = SOAP_SERVICE_PACKAGE
			+ PROPERTY_SCOPE_CHAR + "SearchServiceSoap";

	/**
	 * Fully-qualified class name of SOAP translation service interface.
	 */
	public static final String SOAP_TRANSLATION_SERVICE_INTERFACE = SOAP_SERVICE_PACKAGE
			+ PROPERTY_SCOPE_CHAR + "TranslateServiceSoap";

	// ========================= CONSTANTS: ERROR MESSAGES =================

	/**
	 * Source namespace's name.
	 */
	public static final String SOURCE_NAMESPACE = "Source namespace";

	/**
	 * Source concept ID.
	 */
	public static final String SOURCE_CONCEPT_ID = "Source concept ID";

	/**
	 * Source property name.
	 */
	public static final String SOURCE_PROPERTY_NAME = "Source property name";

	/**
	 * Source property value.
	 */
	public static final String SOURCE_PROPERTY_VALUE = "Source property value";

	/**
	 * Target property name.
	 */
	public static final String TARGET_PROPERTY_NAME = "Target property name";
}
