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
package edu.utah.further.core.query.domain;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * This class centralizes search query language constants.
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
 * @version Dec 22, 2010
 */
@Api
@Constants
public abstract class SearchNames
{
	// ========================= CONSTANTS =================================

	/**
	 * A keyword that is interpreted as the result of a query. Accompanied by a subscript,
	 * e.g. "QUERY[1].id" accesses the person ID field of the search query with qid 1.
	 * Useful in phased federated queries where a search query may depend on the result of
	 * another
	 */
	public static final String QUERY = "QUERY";

	/**
	 * Query result keyword: person ID field.
	 */
	public static final String ID = "id";
}
