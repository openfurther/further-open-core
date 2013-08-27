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
package edu.utah.further.core.util.schema;

import java.text.SimpleDateFormat;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;
import edu.utah.further.core.util.context.PFixtureManager;

/**
 * This class centralizes constants related to the "further" custom spring schema.
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
 * @version Oct 13, 2008
 */
@Api
@Constants
abstract class CoreSchemaConstants
{
	// ========================= CONSTANTS =================================

	// -----------------------------------------------------------------------
	// Element names
	// -----------------------------------------------------------------------

	/**
	 * {@link Component} bean element.
	 */
	public static final String ELEMENT_COMPONENT = "component";

	/**
	 * {@link SimpleDateFormat} bean element.
	 */
	public static final String ELEMENT_DATE_FORMAT = "dateformat";

	/**
	 * {@link PFixtureManager} bean element.
	 */
	public static final String ELEMENT_PORTABLE_FIXTURE = "pfixture";

	/**
	 * Location component - reusable argument nested in other bean tags.
	 */
	public static final String ELEMENT_LOCATION = "location";

	/**
	 * Auto-proxying creator bean element.
	 */
	public static final String ELEMENT_ASPECTJ_AUTOPROXY = "aspectj-autoproxy";

	/**
	 * Aspect inclusion regular expression.
	 */
	public static final String ELEMENT_INCLUDE = "include";

	/**
	 * Proxied bean inclusion regular expression.
	 */
	public static final String ELEMENT_INCLUDE_BEAN = "include-bean";

	// -----------------------------------------------------------------------
	// Common attribute names
	// -----------------------------------------------------------------------

	/**
	 * Name attributes for all value tags.
	 */
	public static final String ATTRIBUTE_NAME = "name";

	/**
	 * Value attributes for all value tags.
	 */
	public static final String ATTRIBUTE_VALUE = "value";
}
