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
package edu.utah.further.core.qunit.fixture;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * Centralizes constants used by core-xml tests.
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
 * @version May 7, 2010
 */
@Api
@Constants
public abstract class CoreXmlNames
{
	// ========================= CONSTANTS =================================

	/**
	 * XQuery test file #1.
	 */
	public static final String XQUERY_FILE_UNQUALIFIED = "extract_cds.xq";

	/**
	 * Input XML test file #1.
	 */
	public static final String INPUT_FILE_UNQUALIFIED = "cd_catalog.xml";

	/**
	 * XQuery Output XML test file #1.
	 */
	public static final String OUTPUT_FILE_UNQUALIFIED = "extract_cds_result.xml";

	/**
	 * XML test file #2.
	 */
	public static final String XQUERY_FILE_NAMESPACE_AWARE = "extract_cds_ns_aware.xq";

	/**
	 * Input XML test file #2.
	 */
	public static final String INPUT_FILE_NAMESPACE_AWARE = "cd_catalog_ns_aware.xml";

	/**
	 * Query Output XML test file #2.
	 */
	public static final String OUTPUT_FILE_NAMESPACE_AWARE = "extract_cds_ns_aware_result.xml";

	/**
	 * XML test file #3 (unqualified).
	 */
	public static final String INPUT_FILE_BOOKS_UNQUALIFIED = "books.xml";

	/**
	 * XML test file #4 (namespace-aware).
	 */
	public static final String INPUT_FILE_BOOKS_NAMESPACE_AWARE = "books_ns.xml";

	/**
	 * Input XML test file for date comparison cases. Has empty date elements.
	 */
	public static final String INPUT_FILE_NO_DATE = "result_no_date.xml";

	/**
	 * Input XML test file for date comparison cases.
	 */
	public static final String INPUT_FILE_DATE1 = "result_date1.xml";

	/**
	 * Input XML test file for date comparison cases.
	 */
	public static final String INPUT_FILE_DATE2 = "result_date2.xml";

	/**
	 * Input XML patient set test file for ignored element comparison cases. Its ignored
	 * element has an empty body.
	 *
	 * @see FUR-1086
	 */
	public static final String PATIENT_NO_VALUE = "patient_no_value.xml";

	/**
	 * Input XML patient set test file for ignored element comparison cases. Its ignored
	 * element tag is empty (doesn't even have an empty body).
	 *
	 * @see FUR-1086
	 */
	public static final String PATIENT_NO_VALUE_EMPTY_ELEMENT = "patient_no_value_empty_element.xml";

	/**
	 * Input XML patient set test file for ignored element comparison cases.
	 *
	 * @see FUR-1086
	 */
	public static final String PATIENT_WITH_VALUE = "patient_with_value.xml";
}
