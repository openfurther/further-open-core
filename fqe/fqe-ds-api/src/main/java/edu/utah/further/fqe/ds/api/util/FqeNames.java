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
 * This class centralizes general-purpose FQE constants.
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
 * @version Jun 2, 2010
 */
@Api
@Constants
public abstract class FqeNames
{
	// ========================= CONSTANTS =================================

	/**
	 * Query ID object property, column name, etc.
	 */
	public static final String QUERY_ID = "queryId";

	/**
	 * Result set translation - local DTS namespace ID (e.g. XQuery parameter name).
	 */
	public static final String LOCAL_NAMESPACE_ID = "localNamespaceId";

	/**
	 * Camel header name: data source identifier column name.
	 */
	public static final String DATA_SOURCE_ID = "dataSourceId";

	/**
	 * Camel header name: execution identifier within a federated query plan, if this
	 * command is part of a non-broadcast plan.
	 */
	public static final String EXECUTION_ID = "executionId";

	/**
	 * The mask string to display when results are less than the mask value but greater
	 * than zero.
	 */
	public static final String MASK_STRING = "*";
}
