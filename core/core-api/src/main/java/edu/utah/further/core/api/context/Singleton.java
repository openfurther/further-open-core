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
package edu.utah.further.core.api.context;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A class annotated with this annotation is assumed to be a singleton at run-time. This
 * does not necessarily mean that there <i>can be</i> exactly one instance of the class.
 * For instance, the Spring container might manage a unique bean instance in a production
 * code. However, the class allows instantiation so that test doubles can be constructed.
 * <p>
 * Note: do not annotate Spring-<code>Configurable</code> domain entities with this
 * annotation, because a double-weaving will likely occur that will result in an error
 * message:
 * <p>
 * [AppClassLoader@130c19b] error at \...\SomeConfigurableEntity.java::0 class
 * '\...\SomeConfigurableEntity' is already woven and has not been built in reweavable
 * mode
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
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Api
public @interface Singleton
{

}
