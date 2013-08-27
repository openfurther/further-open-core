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
package edu.utah.further.core.test.spring;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.test.annotation.TSuiteExecutionListeners;

/**
 * {@link FurtherSpringJUnit4ClassRunner} is a custom extension of
 * {@link SpringJUnit4ClassRunner} which provides functionality of suite-level execution
 * listeners ({@link TSuiteExecutionListener}s).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * Copyright 2002-2008 the original author or authors.
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
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Oct 24, 2009
 */
public final class FurtherSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Holds information on the test suite.
	 */
	private final TSuiteContext suiteContext;

	@Final
	private FurtherTestContextManager furtherTestContextManager;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public FurtherSpringJUnit4ClassRunner(final Class<?> clazz)
			throws InitializationError
	{
		super(clazz);
		this.suiteContext = new TSuiteContext(clazz);
		this.suiteContext.setFurtherTestContextManager(getTestContextManager());

		final List<Class<? extends TSuiteExecutionListener>> listenerClasses = getListenerClasses(clazz);
		registerListenersInContext(listenerClasses);
	}

	// ========================= IMPLEMENTATION: SpringJUnit4ClassRunner ===

	/**
	 * @param notifier
	 * @return
	 * @see org.junit.runners.ParentRunner#classBlock(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	protected Statement classBlock(final RunNotifier notifier)
	{
		runListenerExecutor(notifier, getDescription(), "Before",
				beforeClassListenerExecutor);
		final Statement classBlock = super.classBlock(notifier);
		return classBlock;
	}

	/**
	 * @param notifier
	 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner#run(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	public void run(final RunNotifier notifier)
	{
		final EachTestNotifier testNotifier = new EachTestNotifier(notifier,
				getDescription());
		try
		{
			final Statement statement = classBlock(notifier);
			statement.evaluate();
			runListenerExecutor(notifier, getDescription(), "After",
					afterClassListenerExecutor);
		}
		catch (final AssumptionViolatedException e)
		{
			testNotifier.fireTestIgnored();
		}
		catch (final StoppedByUserException e)
		{
			throw e;
		}
		catch (final Throwable e)
		{
			testNotifier.addFailure(e);
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Creates a new {@link TestContextManager}. Can be overridden by subclasses.
	 *
	 * @param clazz
	 *            the Class object corresponding to the test class to be managed
	 */
	@Override
	protected FurtherTestContextManager createTestContextManager(final Class<?> clazz)
	{
		this.furtherTestContextManager = new FurtherTestContextManager(clazz);
		return furtherTestContextManager;
	}

	private static interface ListenerExecutor
	{
		void run(TSuiteContext context) throws Exception;
	}

	/**
	 * @param notifier
	 * @param description
	 * @param title
	 * @param listenerExecutor
	 */
	private void runListenerExecutor(final RunNotifier notifier,
			final Description description, final String title,
			final ListenerExecutor listenerExecutor)
	{
		try
		{
			listenerExecutor.run(suiteContext);
		}
		catch (final InvocationTargetException ex)
		{
			notifier.fireTestAssumptionFailed(new Failure(description, ex.getCause()));
			return;
		}
		catch (final Exception ex)
		{
			notifier.fireTestAssumptionFailed(new Failure(description, ex));
			return;
		}
	}

	/**
	 * Executor of {@link BeforeClass} methods on all listeners.
	 */
	private final ListenerExecutor beforeClassListenerExecutor = new ListenerExecutor()
	{
		@Override
		public void run(final TSuiteContext context) throws Exception
		{
			for (final TSuiteExecutionListener listener : context.getListeners())
			{
				listener.beforeClass(context);
			}
		}
	};

	/**
	 * Executor of {@link AfterClass} methods on all listeners.
	 */
	private final ListenerExecutor afterClassListenerExecutor = new ListenerExecutor()
	{
		@Override
		public void run(final TSuiteContext context) throws Exception
		{
			for (final TSuiteExecutionListener listener : context.getListeners())
			{
				listener.afterClass(context);
			}
		}
	};

	/**
	 * @param clazz
	 * @return
	 */
	private List<Class<? extends TSuiteExecutionListener>> getListenerClasses(
			final Class<?> clazz)
	{
		final TSuiteExecutionListeners annotation = clazz
				.getAnnotation(TSuiteExecutionListeners.class);
		final List<Class<? extends TSuiteExecutionListener>> listenerClasses = (annotation != null) ? CollectionUtil
				.<Class<? extends TSuiteExecutionListener>> newList(annotation
						.value())
				: CollectionUtil.<Class<? extends TSuiteExecutionListener>> newList();
		return listenerClasses;
	}

	/**
	 * @param listenerClasses
	 * @throws InitializationError
	 */
	private void registerListenersInContext(
			final List<Class<? extends TSuiteExecutionListener>> listenerClasses)
			throws InitializationError
	{
		for (final Class<? extends TSuiteExecutionListener> listenerClass : listenerClasses)
		{
			try
			{
				final TSuiteExecutionListener listenerBean = getFirstBeanOfType(
						getApplicationContext(), listenerClass);
				suiteContext.addListener(listenerBean);
			}
			catch (final Throwable e)
			{
				throw new InitializationError(Collections.singletonList(e));
			}
		}
	}

	/**
	 * @return
	 */
	private ApplicationContext getApplicationContext()
	{
		return furtherTestContextManager.getInternalTestContext().getApplicationContext();
	}

	/**
	 * @param <T>
	 * @param beanType
	 * @return
	 */
	// for Spring 2.5.6.X
	private static <T> T getFirstBeanOfType(final ApplicationContext applicationContext,
			final Class<T> beanType)
	{
		final Map<String, T> beansOfType = applicationContext.getBeansOfType(beanType);
		return beansOfType.isEmpty() ? null : beanType.cast(beansOfType
				.entrySet()
				.iterator()
				.next()
				.getValue());
	}
}
