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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.core.style.ToStringCreator;
import org.springframework.test.context.TestContextManager;
import org.springframework.util.Assert;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * {@link TSuiteContext} encapsulates the context of a test suite, agnostic of the actual
 * testing framework in use. It is similar to <code>TestContext</code> at the test suite
 * level.
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
 * @version Oct 21, 2009
 */
public final class TSuiteContext
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(TSuiteContext.class);

	// ========================= FIELDS ====================================

	/**
	 * The {@link Class} object corresponding to the test class for which the test context
	 * should be constructed (must not be <code>null</code>).
	 */
	private final Class<?> testClass;
	/**
	 * A list of listeners (function pointer) that execute at the beginning and end of
	 * each test suite that is configured with this manager.
	 */
	private final List<TSuiteExecutionListener> listeners = CollectionUtil.newList();


	/**
	 * Test context manager.
	 */
	private TestContextManager furtherTestContextManager;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a new test context for the supplied {@link Class test class} and
	 * <code>ContextCache</code> context cache and parses the corresponding
	 * <code>@ContextConfiguration</code> annotation, if present.
	 *
	 * @param testClass
	 *            the {@link Class} object corresponding to the test class for which the
	 *            test context should be constructed (must not be <code>null</code>)
	 */
	TSuiteContext(final Class<?> testClass)
	{
		Assert.notNull(testClass, "Test class must not be null");
		this.testClass = testClass;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Provide a String representation of this test context's state.
	 */
	@Override
	public String toString()
	{
		return new ToStringCreator(this).append("testClass", this.testClass).toString();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Get the {@link Class test class} for this test context.
	 *
	 * @return the test class (never <code>null</code>)
	 */
	public final Class<?> getTestClass()
	{
		return this.testClass;
	}

	/**
	 * Register a listener.
	 *
	 * @param listener
	 *            a listener to add to the listeners list
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void addListener(final TSuiteExecutionListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public void addListeners(final Collection<? extends TSuiteExecutionListener> c)
	{
		listeners.addAll(c);
	}

	/**
	 * Unregister a listener.
	 *
	 * @param listener
	 *            a listener to add to the listeners list
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeListener(final TSuiteExecutionListener listener)
	{
		return listeners.remove(listener);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public void removeListeners(final Collection<?> c)
	{
		listeners.removeAll(c);
	}

	/**
	 *
	 * @see java.util.List#clear()
	 */
	public void clear()
	{
		listeners.clear();
	}

	/**
	 * Return the listeners property.
	 *
	 * @return the listeners
	 */
	public List<TSuiteExecutionListener> getListeners()
	{
		return CollectionUtil.newList(listeners);
	}

	/**
	 * Return the furtherTestContextManager property.
	 *
	 * @return the furtherTestContextManager
	 */
	public TestContextManager getFurtherTestContextManager()
	{
		return furtherTestContextManager;
	}

	/**
	 * Set a new value for the furtherTestContextManager property.
	 *
	 * @param furtherTestContextManager the furtherTestContextManager to set
	 */
	protected void setFurtherTestContextManager(final TestContextManager furtherTestContextManager)
	{
		this.furtherTestContextManager = furtherTestContextManager;
	}

}
