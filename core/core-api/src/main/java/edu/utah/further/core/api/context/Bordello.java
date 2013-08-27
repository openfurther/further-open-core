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

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.collections.CollectionUtil.MapType.CONCURRENT_HASH_MAP;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.Map;

/**
 * A provider of services that implements the generic service locator pattern. Service
 * implementations are assumed to have a public no-argument constructor.
 * <p>
 * Personally, I feel this approach is inferior to DI, except perhaps in case of injecting
 * services into domain entities. In that case, using a static {@link Bordello} method in
 * contrast to Spring's <code>Configurable</code> annotation may be simpler, as it does
 * not require any start-up initialization overhead of the Spring framework, and does not
 * require a JVM javaagent that may be needed to be reserved for something else (e.g.
 * performance testing agents). On the other hand, {@link Bordello} has many
 * disadvantages, e.g. the default implementation must be declared in a maven API module
 * rather than hidden from clients in a maven implementation module.
 * <p>
 * In sum, use {@link Bordello} for simple DI cases. It will be faster than LTW. Use LTW
 * with <code>Configurable</code> to minimize dependencies between API and implementation
 * modules, and when performance is not super-critical.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 * @see http://www.artima.com/weblogs/viewpost.jsp?thread=238562
 */
@Utility
@Api
@Deprecated
public final class Bordello
{
	// ========================= CONSTANTS =================================

	/**
	 * Keeps track of the service interface-to-implementation mapping.
	 */
	private static final Map<Class<?>, Object> services = newMap(CONCURRENT_HASH_MAP);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private Bordello()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Acquire an implementation of a service. If one has not already been instantiated,
	 * instantiate the class defined by the Implementor annotation on the interface
	 * 
	 * @param <T>
	 *            service interface type
	 * @param interfaceClass
	 *            service interface class
	 * @return service implementation instance
	 */
	public static <T> T getService(final Class<T> interfaceClass)
	{
		synchronized (interfaceClass)
		{
			Object service = services.get(interfaceClass);
			if (service == null)
			{
				try
				{
					final Class<?> implementingClass = interfaceClass.getAnnotation(
							DefaultImplementation.class).value();
					service = implementingClass.newInstance();
					services.put(interfaceClass, service);
				}
				catch (final Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			return interfaceClass.cast(service);
		}
	}

	/**
	 * Set an alternate service implementation. Typically only called in unit tests.
	 * 
	 * @param <T>
	 *            service interface type
	 * @param interfaceClass
	 *            service interface class
	 * @param provider
	 *            service implementation class to set as the one returned from
	 *            {@link #getService(Class)}.
	 */
	public static <T> void setService(final Class<T> interfaceClass, final T provider)
	{
		synchronized (interfaceClass)
		{
			services.put(interfaceClass, provider);
		}
	}
}