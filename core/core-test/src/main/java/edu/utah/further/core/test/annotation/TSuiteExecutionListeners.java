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
package edu.utah.further.core.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListeners;

import edu.utah.further.core.test.spring.TSuiteExecutionListener;

/**
 * Similar to Spring's {@link TestExecutionListeners}. SuiteExecutionListeners defines
 * class-level metadata for configuring which {@link SuiteExecutionListener
 * SuiteExecutionListeners} should be registered with a {@link TestContextManager}.
 * Typically, {@link TSuiteExecutionListeners @SuiteExecutionListeners} will be used in
 * conjunction with {@link ContextConfiguration @ContextConfiguration}.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * Copyright 2002-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * -----------------------------------------------------------------------------------
 * 
 * @author Sam Brannen (original version)
 * @since 2.5
 * @see SuiteExecutionListener
 * @see TestContextManager
 * @see ContextConfiguration
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jan 23, 2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface TSuiteExecutionListeners
{
	// ========================= METHODS ===================================

	/**
	 * <p>
	 * The {@link SuiteExecutionListener SuiteExecutionListeners} to register with a
	 * {@link TestContextManager}.
	 * </p>
	 * 
	 * @see org.springframework.test.context.support.DependencyInjectionSuiteExecutionListener
	 * @see org.springframework.test.context.support.DirtiesContextSuiteExecutionListener
	 * @see org.springframework.test.context.transaction.TransactionalSuiteExecutionListener
	 */
	Class<? extends TSuiteExecutionListener>[] value();

	/**
	 * <p>
	 * Whether or not {@link #value() SuiteExecutionListeners} from superclasses should be
	 * <em>inherited</em>.
	 * </p>
	 * <p>
	 * The default value is <code>true</code>, which means that an annotated class will
	 * <em>inherit</em> the listeners defined by an annotated superclass. Specifically,
	 * the listeners for an annotated class will be appended to the list of listeners
	 * defined by an annotated superclass. Thus, subclasses have the option of
	 * <em>extending</em> the list of listeners. In the following example,
	 * <code>AbstractBaseTest</code> will be configured with
	 * <code>DependencyInjectionSuiteExecutionListener</code> and
	 * <code>DirtiesContextSuiteExecutionListener</code>; whereas,
	 * <code>TransactionalTest</code> will be configured with
	 * <code>DependencyInjectionSuiteExecutionListener</code>,
	 * <code>DirtiesContextSuiteExecutionListener</code>, <strong>and</strong>
	 * <code>TransactionalSuiteExecutionListener</code>, in that order.
	 * </p>
	 * 
	 * <pre class="code">
	 * {@link TSuiteExecutionListeners @SuiteExecutionListeners}({ DependencyInjectionSuiteExecutionListener.class,
	 *     DirtiesContextSuiteExecutionListener.class })
	 * public abstract class AbstractBaseTest {
	 *     // ...
	 * }
	 * 
	 * {@link TSuiteExecutionListeners @SuiteExecutionListeners}({ TransactionalSuiteExecutionListener.class })
	 * public class TransactionalTest extends BaseTest {
	 *     // ...
	 * }
	 * </pre>
	 * 
	 * <p>
	 * If <code>inheritListeners</code> is set to <code>false</code>, the listeners for
	 * the annotated class will <em>shadow</em> and effectively replace any listeners
	 * defined by a superclass.
	 * </p>
	 */
	boolean inheritListeners() default true;
}
