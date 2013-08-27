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
package edu.utah.further.i2b2.query.model;

import edu.utah.further.core.api.context.Constants;

/**
 * XML element constants of the i2b2 request XML.
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
 * @version Apr 13, 2010
 */
@Constants
abstract class I2b2QueryModelConstants
{
	// ========================= CONSTANTS =================================

	/**
	 * Must match i2b2_ws_further_hook's <code>RequestElementNames#REQUEST_XML_NAMESPACE</code>.
	 */
	public static final String NAMESPACE = "http://www.i2b2.org/xsd/cell/crc/psm/1.1/";

	public static final String QUERY_DEFINITION = "query_definition";

	public static final String USER = "user";

	public static final String PANEL = "panel";

	public static final String INVERT = "invert";

	public static final String TOTAL_ITEM_OCCURRENCES = "total_item_occurrences";

	public static final String ITEM = "item";

	public static final String ITEM_NAME = "item_name";

	public static final String ITEM_KEY = "item_key";

	public static final String ITEM_ICON = "item_icon";

	public static final String ITEM_IS_SYNONYM = "item_is_synonym";

	public static final String HLEVEL = "hlevel";

	public static final String TOOLTIP = "tooltip";

	public static final String CLASS = "class";

	public static final String DATE_FROM = "date_from";

	public static final String DATE_TO = "date_to";

	public static final String CONSTRAIN_BY_DATE = "constrain_by_date";

	public static final String CONSTRAIN_BY_VALUE = "constrain_by_value";

	public static final String VALUE_TYPE = "value_type";

	public static final String VALUE_UNIT_OF_MEASURE = "value_unit_of_measure";

	public static final String VALUE_OPERATOR = "value_operator";

	public static final String VALUE_CONSTRAINT = "value_constraint";
	
	public static final String RESULT_OUTPUT_LIST = "result_output_list";
}
