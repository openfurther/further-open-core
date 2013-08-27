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
package edu.utah.further.ds.api.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a method as a executing a Hibernate command. This is a FUR-946 workaround until
 * we figure out the correct idle connection JPA configuration in the context file. We
 * intend to use AOP for now to programmatically rerun all methods annotated with this
 * type in case of a <code>JDBCConnnectionException</code>, indicating a stale connection.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 24, 2010
 */
@Target(
{ METHOD })
@Retention(RUNTIME)
@Inherited
@Documented
public @interface HibernateExecutor
{
	// ========================= CONSTANTS ===============================

	/**
	 * Annotation's fully qualified name. Must be a constant expression for use in AspectJ
	 * pointcuts.
	 */
	static final String CLASS_NAME = "edu.utah.further.ds.api.annotation.HibernateExecutor";
}
