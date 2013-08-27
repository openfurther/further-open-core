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
package edu.utah.further.fqe.api.validation;

import edu.utah.further.core.query.domain.SearchQuery;

/**
 * Abstract class for executing a list of execution rules against a search query using
 * xpath
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Feb 1, 2012
 */
public interface RuleExecutor<O>
{

	// ========================= CONSTANTS ===============================

	/**
	 * The xpath path to the root criterion
	 */
	public static final String PATH_TO_ROOT_CRITERIA = "//query:rootCriterion";

	/**
	 * The xpath path to the root search type which we expect to always be the same
	 */
	public static final String PATH_TO_ROOT_SEARCHTYPE = "//query:rootCriterion/query:searchType";

	/**
	 * The path to all SIMPLE search types
	 */
	public static final String PATH_TO_SIMPLE_SEARCHTYPE = "//query:criteria[query:searchType='SIMPLE']";

	/**
	 * The path to all IN search types
	 */
	public static final String PATH_TO_IN_SEARCHTYPE = "//query:criteria[query:searchType='IN']";

	/**
	 * The path to all DISJUNCTION search types
	 */
	public static final String PATH_TO_DISJUNCTION_SEARCHTYPE = "//query:criteria[query:searchType='DISJUNCTION']";

	/**
	 * The path to all CONJUNCTION search types
	 */
	public static final String PATH_TO_CONJUNCTION_SEARCHTYPE = "//query:criteria[query:searchType='CONJUNCTION']";

	/**
	 * The path to a parameter value. Concatenate with value to test.
	 */
	public static final String PATH_TO_PARAMETER_VAL = "//query:parameters/query:parameter[text()=%s]";

	/**
	 * The path to all parameters in a search type.
	 */
	public static final String PATH_TO_PARAMETERS = "//query:parameters/query:parameter/text()";

	/**
	 * The path to all BETWEEN search types
	 */
	public static final String PATH_TO_BETWEEN_SEARCHTYPE = "//query:criteria[query:searchType='BETWEEN']";

	/**
	 * The path to all BETWEEN search types
	 */
	public static final String PATH_TO_NOT_IN = "//query:criteria[query:searchType='NOT']/query:criteria[query:searchType='IN']";

	/**
	 * Main method which parses SearchQuery against a validation rule
	 * 
	 * @param query
	 * @param rule
	 * @return pass/fail
	 */
	boolean executeRule(SearchQuery query, O rule);
}
