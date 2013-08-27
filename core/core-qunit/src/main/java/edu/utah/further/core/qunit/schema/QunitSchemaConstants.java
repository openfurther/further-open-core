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
package edu.utah.further.core.qunit.schema;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestSuite;

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
abstract class QunitSchemaConstants
{
	// ========================= CONSTANTS =================================

	// -----------------------------------------------------------------------
	// Element names
	// -----------------------------------------------------------------------

	/**
	 * {@link QTestSuite} bean element.
	 */
	public static final String ELEMENT_TEST_SUITE = "testSuite";

	/**
	 * {@link QTestData} bean element.
	 */
	public static final String ELEMENT_TEST = "test";

	/**
	 * {@link QTestData} XML transformation field element.
	 */
	public static final String ELEMENT_TRANSFORMER = "transformer";

	/**
	 * {@link QTestData} input field element.
	 */
	public static final String ELEMENT_IN = "in";

	/**
	 * {@link QTestData} output field element.
	 */
	public static final String ELEMENT_OUT = "out";

	/**
	 * {@link QTestData} always-fail field element.
	 */
	public static final String ELEMENT_SPECIAL_ACTION = "specialAction";

	/**
	 * {@link QTestData} externally-binded parameter element.
	 */
	public static final String ELEMENT_PARAM = "param";

	/**
	 * {@link QTestData} ignored element field member.
	 */
	public static final String ELEMENT_IGNORED_ELEMENT = "ignoredElement";

	/**
	 * {@link QTestContext} namespace ID element field member.
	 */
	public static final String ELEMENT_TARGET_NAMESPACE_ID = "targetNamespaceId";

	/**
	 * {@link QTestContext} service user ID element field member.
	 */
	public static final String ELEMENT_SERVICE_USER_ID = "serviceUserId";

	/**
	 * {@link QTestContext} master input class-path element field member.
	 */
	public static final String ELEMENT_INPUT_CLASS_PATH = "inputClassPath";

	/**
	 * {@link QTestContext} master expected class-path element field member.
	 */
	public static final String ELEMENT_EXPECTED_CLASS_PATH = "expectedClassPath";

	// -----------------------------------------------------------------------
	// Common attribute names
	// -----------------------------------------------------------------------

	/**
	 * Name attribute (e.g. map entry key).
	 */
	public static final String ATTRIBUTE_NAME = "name";

	/**
	 * Value attribute (e.g. map entry value).
	 */
	public static final String ATTRIBUTE_VALUE = "value";
}
