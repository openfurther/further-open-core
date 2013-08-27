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
package edu.utah.further.core.api.constant;

import edu.utah.further.core.api.context.Api;


/**
 * This class centralizes miscellaneous constants.
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
@edu.utah.further.core.api.context.Constants
public abstract class Constants
{
	// ========================= CONSTANTS =================================

	// -----------------------------------------------------------------------
	// Miscellaneous constants
	// -----------------------------------------------------------------------

	/**
	 * Invalid value of an integer field. May serve as a useful default value or when an
	 * index is not found in an array.
	 */
	public static final int INVALID_VALUE_INTEGER = -1;

	/**
	 * Invalid value of a long field. May serve as a useful default value or when an index
	 * is not found in an array.
	 */
	public static final long INVALID_VALUE_LONG = -1l;

	/**
	 * Invalid value of an integer field. May serve as a useful default value or when an
	 * index is not found in an array.
	 */
	public static final Integer INVALID_VALUE_BOXED_INTEGER = new Integer(-1);

	/**
	 * Invalid value of a long field. May serve as a useful default value or when an index
	 * is not found in an array.
	 */
	public static final Long INVALID_VALUE_BOXED_LONG = new Long(-1l);

	/**
	 * Seed for version numbers of versioned entities.
	 */
	public static final int VERSION_SEED = -1;

	/**
	 * Method calling recursion limit. Usually limited by JVM capabilities.
	 */
	public static final int MAX_RECURSION = 256;

	// Use TimeUnit for all other conversion constants
	/**
	 * Convert milliseconds to years by multiplying by this constant.
	 */
	public static final double MILLISECONDS_TO_YEARS = 3.16887385068114e-11;

	/**
	 * A small positive number.
	 */
	public static final double SMALL_NUMBER = 1e-15d;
	
	/**
	 * The maximum number of elements in an IN list.
	 */
	public static final int MAX_IN = 1000;

	// ========================= CONSTANTS: Spring Beans ===================

	public abstract class Scope
	{
		/**
		 * Indicates a singleton bean in Spring annotations.
		 */
		public static final String SINGLETON = "singleton";

		/**
		 * Indicates a prototype bean in Spring annotations.
		 */
		public static final String PROTOTYPE = "prototype";

		/**
		 * Indicates a request-scope backing bean in Spring annotations.
		 */
		public static final String REQUEST = "request";
	}

	// -----------------------------------------------------------------------
	// Arrays and lists
	// -----------------------------------------------------------------------
}
