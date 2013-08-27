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
package edu.utah.further.core.util.io;

import static edu.utah.further.core.util.io.ClasspathUtil.loadClassWithDetailedExceptions;

import java.util.Set;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Api;

/**
 * Filters resources to classes that implement one or more interfaces or are a sub-class
 * of a class.
 * <p>
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
@Api
class ImplementingClassResourceMatcher implements ResourceMatcher<Class<?>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A class' resource path ends with this string.
	 */
	private static final String CLASS_SUFFIX = ".class";

	// ========================= FIELDS ====================================

	/**
	 * List of super-classes/interfaces to match.
	 */
	private final Set<Class<?>> superClasses;

	// ========================= CONSTRUCTORS ==============================

	// /**
	// * @param superClasses
	// */
	// public ImplementingClassResourceMatcher(final Set<Class<?>> superClasses)
	// {
	// super();
	// this.superClasses = superClasses;
	// }

	/**
	 * @param superClasses
	 */
	public ImplementingClassResourceMatcher(final Set<String> superClasses)
	{
		super();
		this.superClasses = toClassSet(superClasses);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the textual representation of this matcher.
	 *
	 * @return the textual representation of this matcher object
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return "Super-classes matcher (" + superClasses.size() + ")";
	}

	// ========================= IMPLEMENTATION: ResourceMatcher ===========

	/**
	 * @param path
	 * @return
	 * @see edu.utah.further.core.util.io.ClasspathUtil.ResourceMatcher#match(java.lang.String)
	 */
	@Override
	public Class<?> match(final String path)
	{
		// Convert the resource path to a class name
		final String className = pathToQualifiedClassName(path);
		// if the class implements one of the annotations, add it
		return ClasspathUtil.isClassASubClassOf(className, superClasses);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return a fully qualified class name of a path.
	 *
	 * @param path
	 *            path to format
	 * @return fully qualified class name of a path
	 */
	private static String pathToQualifiedClassName(final String path)
	{
		return path.replaceAll("/", ".").substring(0,
				path.length() - CLASS_SUFFIX.length());
	}

	/**
	 * Injects the set of annotation types.
	 *
	 * @param classNames
	 *            the set of qualified annotation class names to set.
	 * @return
	 */
	private static Set<Class<?>> toClassSet(final Set<String> classNames)
	{
		final Set<Class<?>> classList = CollectionUtil.newSet();
		// Filter tabs and new lines
		for (final String className : classNames)
		{
			classList.add(loadClassWithDetailedExceptions(className));
		}
		return classList;
	}
}
