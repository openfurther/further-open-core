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
package edu.utah.further.dts.impl.aspect;

import edu.utah.further.core.api.context.Constants;
import edu.utah.further.dts.api.annotation.DtsTransactional;

/**
 * Defines shared point cut definitions throughout the entire application.
 * <p>
 * From {@link http://static.springframework.org/spring/docs/2.5.x/reference/aop.html}:
 * "When working with enterprise applications, you often want to refer to modules of the
 * application and particular sets of operations from within several aspects. We recommend
 * defining a "SystemArchitecture" aspect that captures common pointcut expressions for
 * this purpose."
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
 * @version Nov 6, 2008
 */
@Constants
abstract class DtsPointcuts
{
	// ========================= CONSTANTS =================================

	/**
	 * {@link DtsTransactional} class fully qualified name.
	 */
	public static final String ANNOTATION_DTS_TRANSACTIONAL_CLASS = "edu.utah.further.dts.api.annotation.DtsTransactional";

	// /**
	// * Signifies that a method that is annotated with the {@link DtsTransactional}
	// * annotation.
	// */
	// public static final String ANNOTATED_METHOD = "@annotation("
	// + ANNOTATION_DTS_TRANSACTIONAL_CLASS + ")";

	// ========================= CONSTANTS =================================

	// /**
	// * A method that is annotated with the {@link DtsTransactional} annotation.
	// */
	// @Pointcut("@annotation(" + ANNOTATION_DTS_TRANSACTIONAL_CLASS + ")")
	// public void annotatedMethod()
	// {
	//
	// }
}
