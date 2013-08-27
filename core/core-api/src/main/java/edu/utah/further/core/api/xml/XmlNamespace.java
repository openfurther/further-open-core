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
package edu.utah.further.core.api.xml;

import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * This interface centralizes JAXB / XML namespace constants. Useful for Spring injection
 * via <code>util:constant</code> tags.
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
public abstract class XmlNamespace
{
	// ========================= Commonly-used namespaces ==================

	/**
	 * Separator of namespace alias and namespace element name in qualified element names.
	 */
	public static final String XMLNS_SCOPE = ":";

	/**
	 * XML namespace attribute name.
	 */
	public static final String XMLNS_ATTRIBUTE = "xmlns";

	/**
	 * XML XSI namespace.
	 */
	public static final String XML_SCHEMA = "xs";

	/**
	 * XML XSI namespace.
	 */
	public static final String XML_SCHEMA_INSTANCE = "xsi";

	/**
	 * XML XSI namespace schema location.
	 */
	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

	/**
	 * XML XSI namespace schema location.
	 */
	public static final String XML_SCHEMA_INSTANCE_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

	/**
	 * Some namespace to use in the transformed document.
	 */
	public static final String XML_NS1 = "ns1";

	/**
	 * xsi:type element name.
	 */
	public static final String XSI_TYPE = "type";

	// ========================= FURTHeR Namespaces ========================

	/**
	 * Base namespace of all xml schemas, tag libraries, etc.
	 */
	public static final String BASE = "http://further.utah.edu";

	/**
	 * Mock namespace for test scopes.
	 */
	public static final String MOCK = BASE + VIRTUAL_DIRECTORY + "mock";

	/**
	 * Core modules - common prefix.
	 */
	private static final String CORE_PREFIX = BASE + VIRTUAL_DIRECTORY + "core"
			+ VIRTUAL_DIRECTORY;

	/**
	 * Core API module namespace.
	 */
	public static final String CORE_API = CORE_PREFIX + "api";

	/**
	 * Core Query namespace prefix
	 */
	public static final String CORE_QUERY_PREFIX = "query";

	/**
	 * Core Query module namespace.
	 */
	public static final String CORE_QUERY = CORE_PREFIX + CORE_QUERY_PREFIX;

	/**
	 * Core DTS module namespace.
	 */
	public static final String CORE_WS = CORE_PREFIX + "ws";

	/**
	 * Cohort selection module.
	 */
	public static final String DS = BASE + VIRTUAL_DIRECTORY + "ds";

	/**
	 * Portal module namespace.
	 */
	public static final String PORTAL = BASE + VIRTUAL_DIRECTORY + "portal";

	/**
	 * MDR web service modules.
	 */
	public static final String DTS = BASE + VIRTUAL_DIRECTORY + "dts";

	/**
	 * MDR web service modules.
	 */
	public static final String MDR = BASE + VIRTUAL_DIRECTORY + "mdr";

	/**
	 * Federated query engine modules.
	 */
	public static final String FQE = BASE + VIRTUAL_DIRECTORY + "fqe";

	/**
	 * Federated query engine modules. Used to qualify only the root element.
	 */
	public static final String FQE_DUMMY = BASE + VIRTUAL_DIRECTORY + "fqe-dummy";

	/**
	 * Tools (dummy) namespaces.
	 */
	public static final String ETC = BASE + VIRTUAL_DIRECTORY + "etc";

	/**
	 * i2b2 integration modules.
	 */
	public static final String I2B2 = BASE + VIRTUAL_DIRECTORY + "i2b2";
}
