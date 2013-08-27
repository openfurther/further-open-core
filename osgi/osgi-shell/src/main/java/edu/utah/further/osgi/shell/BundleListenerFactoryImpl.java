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
package edu.utah.further.osgi.shell;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A general listener factory implementation that instantiates a listener on this bundle's
 * classpath using the bundle context {@link ClassLoader}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 9, 2010
 */
public final class BundleListenerFactoryImpl implements BundleStateListener.Factory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(BundleListener.class);

	// ========================= FIELDS ====================================

	/**
	 * The produced listener. Once created, it is cached.
	 */
	private BundleStateListener listener;

	// ========================= DEPENDENCIES ==============================

	/**
	 * This bundle's context.
	 */
	private BundleContext bundleContext;

	/**
	 * Listener class to instantiate. Must be on this bundle's classpath.
	 */
	private String listenerClass;

	// ========================= IMPL: Factory =============================

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleStateListener.Factory#getListener()
	 */
	@Override
	public synchronized BundleStateListener getListener()
	{
		if (listener == null)
		{
			listener = createListener();
		}
		return listener;
	}

	// ========================= METHODS ===================================

	/**
	 * Initialize the listener instance and cache it.
	 */
	public void init()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing");
		}
		getListener();
	}

	/**
	 * Destroy the listener if it implements {@link Destroyable}.
	 *
	 * @throws Exception
	 */
	public void destroy() throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Destroying");
		}
		if (listener instanceof Destroyable)
		{
			((Destroyable) listener).destroy();
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the listenerClass property.
	 *
	 * @param listenerClass
	 *            the listenerClass to set
	 */
	public void setListenerClass(final String listenerClass)
	{
		this.listenerClass = listenerClass;
	}

	/**
	 * Set a new value for the bundleContext property.
	 *
	 * @param bundleContext
	 *            the bundleContext to set
	 */
	public void setBundleContext(final BundleContext bundleContext)
	{
		this.bundleContext = bundleContext;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Create the listener.
	 *
	 * @return listener instance
	 */
	private BundleStateListener createListener()
	{
		try
		{
			// Use dynamic class loading to make sure we actually try to reload the class
			// for dynamic imports to kick in if possible
			final Class<?> cl = getClass().getClassLoader().loadClass(listenerClass);
			return (BundleStateListener) cl
					.getConstructor(BundleContext.class)
					.newInstance(bundleContext);
			// return new SpringApplicationListener(bundleContext);
		}
		catch (final Throwable t)
		{
			return null;
		}
	}

}
