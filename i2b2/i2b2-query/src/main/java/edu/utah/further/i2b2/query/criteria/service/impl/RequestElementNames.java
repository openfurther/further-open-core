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
package edu.utah.further.i2b2.query.criteria.service.impl;

import static edu.utah.further.core.api.xml.XmlUtil.fullTag;

/**
 * Centralizes XML element names added to the i2b2 query that are used to configure the
 * FURTHeR processing flow fork.
 * <p>
 * Note: adding these extra elements to the i2b2 request does not seem to interfere with
 * any subsequent i2b2 processing, so they are left there. In case there's a problem.
 * simply remove that part of the request string before forwarding it to the i2b2 filter
 * chain.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version May 21, 2010
 */
abstract class RequestElementNames
{
	// ========================= CONSTANTS =================================

	/**
	 * Main namespace of i2b2 messages.
	 */
	public static final String I2b2_HIVE_NAMESPACE = "http://www.i2b2.org/xsd/hive/msg/1.1/";

	/**
	 * A root node namespace for the output FURTHeR query XML document. Note that it
	 * corresponds to the namespace of the request tag in the original i2b2 request, even
	 * though it is attached to the query_definition root tag in the output FURTHeR
	 * request XML.
	 */
	public static final String REQUEST_XML_NAMESPACE = "http://www.i2b2.org/xsd/cell/crc/psm/1.1/";

	/**
	 * Root element of query element of the i2b2 message.
	 */
	public static final String QUERY_DEFINITION = "query_definition";

	/**
	 * Root element of user ID element of the i2b2 message.
	 */
	public static final String USER = "user";

	/**
	 * Namespace alias for query definition namespace in generated FURTHeR XML.
	 */
	public static final String NS1 = "ns1";

	/**
	 * A boolean flag that controls whether to send the query to the FURTHeR FQE for
	 * processing.
	 */
	public static final String FQE_PROCESS = "fqe-process";

	/**
	 * Matches FQE processing flag in an XML string.
	 */
	public static final String FQE_PROCESSING_FLAG_STRING = fullTag(FQE_PROCESS, "true")
			.toString();
}
