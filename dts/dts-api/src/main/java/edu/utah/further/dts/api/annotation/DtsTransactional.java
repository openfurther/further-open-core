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
package edu.utah.further.dts.api.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Describes Apelon DTS transaction attributes on a method or class. See Spring's
 * <code>Transactional</code> annotation.
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
@Target(
{ METHOD, TYPE })
@Retention(RUNTIME)
@Inherited
@Documented
public @interface DtsTransactional
{
	// ========================= CONSTANTS ===============================

	/**
	 * Must be a constant expression to be part of an AspectJ annotation value.
	 */
	static final String CLASS_NAME = "edu.utah.further.dts.api.annotation.DtsTransactional";

	// ========================= FIELDS ==================================

	/**
	 * The transaction propagation type.
	 * <p>
	 * Defaults to {@link DtsPropagation#REQUIRED}.
	 */
	DtsPropagation propagation() default DtsPropagation.REQUIRED;
}
