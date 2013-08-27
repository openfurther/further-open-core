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
package edu.utah.further.dts.impl.util;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * Centralizes useful constants used by DTS tests.
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
 * @version Sep 2, 2010
 */
@Api
@Constants
public abstract class DtsTestingNames
{
	// ========================= CONSTANTS =================================

	/**
	 * UUEDW identifier property name.
	 */
	public static final String LOCAL_CODE = "Local Code";

	/**
	 * Chorioamnionitis affecting fetus or newborn [762.7].
	 */
	public static final String CHORIOAMIONITIS_CODE_IN_SOURCE = "762.7";

	/**
	 * An example concept name.
	 */
	public static final String ANALGESIC_PRODUCT = "Analgesic (product)";

	/**
	 * Sample DWID from the UUEDW namespace.
	 */
	public static final String MARRIED_UUEDW_DWID = "300195";

	/**
	 * Sample DWID from the UUEDW namespace.
	 */
	public static final String LEGALLY_SEP_UUEDW_DWID = "300201";
	
	/**
	 * Sample UPDB_ID from the UPDB nsmespace 
	 */
	public static final String ALABAMA = "ALABAMA";
}
