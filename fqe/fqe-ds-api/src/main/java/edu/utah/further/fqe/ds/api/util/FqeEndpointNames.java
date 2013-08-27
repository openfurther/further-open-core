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
package edu.utah.further.fqe.ds.api.util;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * This class centralizes FQE camel endpoint naming conventions that both the FQE and data
 * sources agree on and are aware of in their routes.
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
 * @version Mar 3, 2010
 */
@Api
@Constants
public abstract class FqeEndpointNames
{
	// ========================= CONSTANTS =================================

	/**
	 * The topic to which FQE posts federated requests.
	 */
	public static final String REQUEST = "request";

	/**
	 * The topic to which FQE posts federated requests.
	 */
	public static final String MARSHAL_REQUEST = "marshalRequest";

	/**
	 * The result topic to which raw (non-aggregated) data source responses are returned.
	 * Raw responses are aggregated back into {@link #REQUEST}.
	 */
	public static final String RESULT = "result";

	/**
	 * The result topic to which federated query contexts are submitted.
	 */
	public static final String FEDERATED_RESULT = "federatedResult";
}
