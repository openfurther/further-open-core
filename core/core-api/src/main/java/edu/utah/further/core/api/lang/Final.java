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
package edu.utah.further.core.api.lang;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.utah.further.core.api.context.Api;

/**
 * A class, member or method annotated with this annotation emulates a <i>final</i>
 * qualifier, i.e. are supposed to be set exactly once. This is useful for Hibernate
 * proxing: a proxy can still be created because in the actual code, that
 * class/member/methods might not be final, however compilation checks on security access
 * to this field should still be performed. This annotation is to be used by a compiler
 * plug-in or "simulated compilation" phase. where the final keyword is inserted or
 * emulated to make sure the code can still be compiled; however at runtime, this
 * annotation is ignored so proxies can still wrap these Java elements.
 * <p>
 * This also means that if the annotated element is a method, sub-classes are strongly
 * discouraged from overriding the method.
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
{ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
@Documented
@Api
public @interface Final
{

}
