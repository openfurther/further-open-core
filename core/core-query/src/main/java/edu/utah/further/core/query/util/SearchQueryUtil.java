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
package edu.utah.further.core.query.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.query.domain.SearchQuery;

/**
 * {@link SearchQuery} utility methods.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 21, 2013
 */
public final class SearchQueryUtil
{

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private SearchQueryUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Loads the root object entity class for a given query. Searches within the given
	 * package for classes with the exact name of the passed rootObject parameter as well
	 * as rootObject parameter + 'Entity'"
	 * 
	 * @param domainClassPackage
	 *            the package to search for the domain class
	 * @param query
	 *            the searchquery object with a rootObject
	 * @return
	 */
	public static Class<? extends PersistentEntity<?>> getDomainClass(
			final String domainClassPackage, final String rootObject)
	{
		String fqcn = domainClassPackage + "." + rootObject;

		Class<?> domainClass = loadClass(fqcn);

		if (domainClass != null)
		{
			return castToPersistentEntity(domainClass);
		}

		// Try loading with classname + 'Entity'
		fqcn = fqcn + "Entity";
		domainClass = loadClass(fqcn);

		if (domainClass != null)
		{
			return castToPersistentEntity(domainClass);
		}

		throw new ApplicationException(
				"Unable to load root object class '"
						+ rootObject
						+ "' under the package '"
						+ domainClassPackage
						+ "'. Ensure the object exists and that the object extends PersistentEntity");
	}

	/**
	 * Return a {@link PersistentEntity} class or throw an exception if the class cannot
	 * be cast to a {@link PersistentEntity}.
	 * 
	 * @param domainClass
	 */
	@SuppressWarnings("unchecked")
	private static Class<? extends PersistentEntity<?>> castToPersistentEntity(
			final Class<?> domainClass)
	{
		if (!PersistentEntity.class.isAssignableFrom(domainClass))
		{
			throw new ApplicationException(
					"Found domain class for SearchQuery but domain class is not assignable from PersistEntity");
		}
		return (Class<? extends PersistentEntity<?>>) domainClass;
	}

	/**
	 * Load a root entity class or return null if it can't be loaded.
	 * 
	 * @param fqcn
	 * @return
	 */
	private static Class<?> loadClass(final String fqcn)
	{
		try
		{
			return Thread.currentThread().getContextClassLoader().loadClass(fqcn);

		}
		catch (final ClassNotFoundException e)
		{
			return null;
		}
	}

}
