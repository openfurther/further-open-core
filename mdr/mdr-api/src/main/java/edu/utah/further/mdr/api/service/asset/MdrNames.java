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
package edu.utah.further.mdr.api.service.asset;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * Centralizes MDR constants assumed by MDR services.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 26, 2010
 */
@Api
@Constants
public abstract class MdrNames
{
	// ========================= CONSTANTS: Asset Types ====================

	/**
	 * An example asset type label. Useful for testing. Resources of this type usually
	 * contain small XML fields.
	 */
	public static final String RESOURCE_TYPE_MODEL_METADATA = "Structural Metadata (XMI)";

	/**
	 * Basic asset type description (or part of its name) of XMI resources. Useful for
	 * testing. Resources of this type usually contain large XML fields.
	 */
	public static final String XMI = "XMI";

	/**
	 * A binary asset type description. Useful for BLOB testing.
	 */
	public static final String JPEG = "JPEG";

	/**
	 * XQuery asset type description (or part of its name). Includes both main XQuery
	 * programs and library modules.
	 */
	public static final String XQUERY = "XQuery";

	/**
	 * Basic asset type description (or part of its name) of Schematron XML validation
	 * resources. Useful for testing. Resources of this type usually contain small XML
	 * fields.
	 */
	public static final String SCHEMATRON = "Schematron";

	/**
	 * FURTHeR asset description (or part of its name). Useful for testing.
	 */
	public static final String FURTHER = "FURTHeR";

	// ========================= CONSTANTS: Resource paths =================

	// These are useful for tests. Pick resources whose paths are not likely to change,
	// e.g. core libraries.

	/**
	 * The relative path of the MDR resource that centralizes FQE XQuery program
	 * constants.
	 */
	public static final String PATH_FQE_XQUERY_CONSTANTS = "fqe/further/xq/constants.xq";

	/**
	 * The relative path of the MDR resource that redirects to another resource.
	 * constants.
	 */
	public static final String PATH_RESOURCE_REDIRECT = "ds/i2b2/data-sources.xml";

	/**
	 * @Deprecated As of version 1.1, will be removed in 1.2
	 * The relative path of some SVN-type resource.
	 */
	public static final String PATH_SCHEMATRON_RESOURCE = "edu/utah/further/ds/impl/validation/search-query.sch";

	/**
	 * The relative path of an MDR resource that references
	 * {@link #PATH_FQE_XQUERY_CONSTANTS}.
	 */
	public static final String PATH_FQE_XQUERY_FURTHER = "fqe/further/xq/further.xq";

	// ========================= CONSTANTS: Place holders (mostly for tests)

	/**
	 * A server URL place holder known to appear in the test resource of
	 * {@link #getActiveResourceByPath()}.
	 */
	public static final String PLACE_HOLDER_DTS_WS_SERVER_URL = "${server.dts.ws}";

	/**
	 * A "normal" place holder known to appear in the test resource of
	 * {@link #getActiveResourceByPath()}.
	 */
	public static final String PLACE_HOLDER_DTS_WS_TRANSLATION_SERVICE = "${path.dts.ws.translate}";
}
