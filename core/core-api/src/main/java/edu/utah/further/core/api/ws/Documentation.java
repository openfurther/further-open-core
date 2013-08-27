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
package edu.utah.further.core.api.ws;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.utah.further.core.api.context.Api;

/**
 * A documentation annotation on a class, method or method parameter element. Useful for
 * auto-generation of pretty documentation, e.g. for web services.
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
@Target(
{ TYPE, METHOD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Api
@Documentation(name = "Java element documentation annotation", description = "A "
		+ "documentation annotation on a class, method or method parameter element. "
		+ "Useful for auto-generation of pretty documentation, e.g. for web services.")
public @interface Documentation
{
	/**
	 * Documentation summary.
	 */
	@Documentation(name = "Name", description = "Documentation summary of the Java element")
	String name() default "";

	/**
	 * Long description of the element.
	 */
	@Documentation(name = "Description", description = "Long description of the Java element")
	String description() default "";
}
