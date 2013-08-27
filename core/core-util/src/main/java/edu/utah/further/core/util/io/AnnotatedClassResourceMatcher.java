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

import static edu.utah.further.core.util.io.ClasspathUtil.isAnnotatedWith;
import static edu.utah.further.core.util.io.ClasspathUtil.loadClassWithDetailedExceptions;

import java.lang.annotation.Annotation;
import java.util.Set;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Api;

/**
 * A filter of resources.
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
public class AnnotatedClassResourceMatcher implements ResourceMatcher<Class<?>>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * List of annotation classes; a matching class should implement at least one of them.
	 */
	private final Set<Class<? extends Annotation>> annotationClasses;

	// ========================= CONSTRUCTORS ==============================

	// /**
	// * @param annotationClasses
	// */
	// public AnnotatedClassResourceMatcher(
	// final Set<Class<? extends Annotation>> annotationClasses)
	// {
	// super();
	// this.annotationClasses = annotationClasses;
	// }

	/**
	 * @param annotationClasses
	 */
	public AnnotatedClassResourceMatcher(final Set<String> annotationClassNames)
	{
		super();
		this.annotationClasses = toAnnotationClassSet(annotationClassNames);
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
		return "Annotation classes matcher (" + annotationClasses + ")";
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
		final String className = ClasspathUtil.pathToQualifiedClassName(path);
		// if the class implements one of the annotations, add it
		final Class<?> clazz = ClasspathUtil.loadClass(className);
		return (clazz != null) && isAnnotatedWith(clazz, annotationClasses) ? clazz : null;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Injects the set of annotation types.
	 *
	 * @param classNames
	 *            the set of qualified annotation class names to set.
	 * @return
	 */
	private static Set<Class<? extends Annotation>> toAnnotationClassSet(
			final Set<String> classNames)
	{
		final Set<Class<? extends Annotation>> classList = CollectionUtil.newSet();
		// Filter tabs and new lines
		for (final String annotationClass : classNames)
		{
			classList
					.add((Class<? extends Annotation>) loadClassWithDetailedExceptions(annotationClass));
		}
		return classList;
	}
}
