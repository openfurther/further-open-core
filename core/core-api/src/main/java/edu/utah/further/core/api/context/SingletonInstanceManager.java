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

/**
 * Keeps track of a singleton class' instantiation state. Useful for Spring-managed
 * singletons that have to have a public constructor.
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
 * @version Sep 28, 2009
 */
public final class SingletonInstanceManager
{
	// ========================= FIELDS ====================================

	/**
	 * Keeps track of instance creation. keeping one copy of this variable for all
	 * threads.
	 */
	private volatile boolean instanceCreated = false;

	// ========================= FIELDS ====================================

	/**
	 * The singleton class whose instantiation state is managed by this class.
	 */
	private final Class<?> singletonClass;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a singleton class state manager.
	 * 
	 * @param singletonClass
	 *            The singleton class whose instantiation state is managed by this class
	 */
	public SingletonInstanceManager(final Class<?> singletonClass)
	{
		super();
		this.singletonClass = singletonClass;
	}

	// ========================= METHODS ===================================

	/**
	 * <p>
	 * Hide constructor in a singleton class.
	 * </p>
	 */
	public void validateInstance()
	{
		synchronized (singletonClass)
		{
			if (!instanceCreated)
			{
				instanceCreated = true;
			}
			else
			{
				throw new IllegalAccessError(
						"Attempted to create a second instance of a singleton class");
			}
		}
	}

}
