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

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;
import org.springframework.osgi.context.event.OsgiBundleContextFailedEvent;
import org.springframework.osgi.context.event.OsgiBundleContextRefreshedEvent;
import org.springframework.osgi.extender.event.BootstrappingDependencyEvent;
import org.springframework.osgi.service.importer.event.OsgiServiceDependencyEvent;
import org.springframework.osgi.service.importer.event.OsgiServiceDependencyWaitStartingEvent;

public final class SpringApplicationListener implements
		OsgiBundleApplicationContextListener, BundleListener, Destroyable,
		BundleStateListener
{
	public static enum SpringState
	{
		Unknown, Waiting, Started, Failed;
	}

	private static final Logger log = LoggerFactory
			.getLogger(SpringApplicationListener.class);

	private final Map<Long, SpringState> states;
	private final BundleContext bundleContext;
	private final ServiceRegistration registration;

	/**
	 * @param bundleContext
	 */
	public SpringApplicationListener(final BundleContext bundleContext)
	{
		this.states = new ConcurrentHashMap<>();
		this.bundleContext = bundleContext;
		this.bundleContext.addBundleListener(this);
		this.registration = this.bundleContext.registerService(
				OsgiBundleApplicationContextListener.class.getName(), this,
				new Hashtable<>());
	}

	/**
	 * @throws Exception
	 * @see edu.utah.further.osgi.shell.BundleListenerFactoryImpl.Destroyable#destroy()
	 */
	@Override
	public void destroy() throws Exception
	{
		bundleContext.removeBundleListener(this);
		registration.unregister();
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleStateListener#getName()
	 */
	@Override
	public String getName()
	{
		return "Spring ";
	}

	/**
	 * @param bundle
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleStateListener#getState(org.osgi.framework.Bundle)
	 */
	@Override
	@SuppressWarnings("boxing")
	public String getState(final Bundle bundle)
	{
		final SpringState state = states.get(bundle.getBundleId());
		if (state == null || bundle.getState() != Bundle.ACTIVE
				|| state == SpringState.Unknown)
		{
			return null;
		}
		return state.toString();
	}

	/**
	 * @param bundle
	 * @return
	 */
	@SuppressWarnings("boxing")
	public SpringState getSpringState(final Bundle bundle)
	{
		SpringState state = states.get(bundle.getBundleId());
		if (state == null || bundle.getState() != Bundle.ACTIVE)
		{
			state = SpringState.Unknown;
		}
		return state;
	}

	/**
	 * @param event
	 * @see org.springframework.osgi.context.event.OsgiBundleApplicationContextListener#onOsgiApplicationEvent(org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void onOsgiApplicationEvent(final OsgiBundleApplicationContextEvent event)
	{
		SpringState state = null;
		if (event instanceof BootstrappingDependencyEvent)
		{
			final OsgiServiceDependencyEvent de = ((BootstrappingDependencyEvent) event)
					.getDependencyEvent();
			if (de instanceof OsgiServiceDependencyWaitStartingEvent)
			{
				state = SpringState.Waiting;
			}
		}
		else if (event instanceof OsgiBundleContextFailedEvent)
		{
			state = SpringState.Failed;
		}
		else if (event instanceof OsgiBundleContextRefreshedEvent)
		{
			state = SpringState.Started;
		}
		if (state != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Spring app state changed to " + state + " for bundle "
						+ event.getBundle().getBundleId());
			}
			states.put(event.getBundle().getBundleId(), state);
		}
	}

	/**
	 * @param event
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void bundleChanged(final BundleEvent event)
	{
		if (event.getType() == BundleEvent.UNINSTALLED)
		{
			states.remove(event.getBundle().getBundleId());
		}
	}

}
