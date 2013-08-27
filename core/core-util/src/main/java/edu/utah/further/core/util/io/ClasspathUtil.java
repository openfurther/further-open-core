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

import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.text.StringUtil.cleanContextFileString;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StopWatch;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Utilities related to Java 5+ annotations.
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
@Utility
@Api
public final class ClasspathUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ClasspathUtil.class);

	/**
	 * A class' resource path ends with this string.
	 */
	private static final String CLASS_SUFFIX = ".class";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ClasspathUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the {@link ClassLoader} bound to the current thread. Useful in an OSGi
	 * context.
	 *
	 * @return the {@link ClassLoader} bound to the current thread
	 */
	public static ClassLoader getThreadClassLoader()
	{
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Check whether the class is annotated with an annotation type.
	 *
	 * @param clazz
	 *            class to check
	 * @param annotationClass
	 *            annotation types to match in <code>clazz</code>
	 * @return <code>true</code> if and only if at least the class has this annotation
	 */
	public static boolean isAnnotatedWith(final Class<?> clazz,
			final Class<? extends Annotation> annotationClass)
	{
		return clazz.getAnnotation(annotationClass) != null;
	}

	/**
	 * Check whether the class implements at least one a list of specified annotation
	 * types.
	 *
	 * @param clazz
	 *            class to check
	 * @param annotationClasses
	 *            list of annotation types to match in <code>clazz</code>
	 * @return <code>true</code> if and only if at least one of the list of annotation
	 *         types is matched by the class
	 */
	public static boolean isAnnotatedWith(final Class<?> clazz,
			final Set<Class<? extends Annotation>> annotationClasses)
	{
		for (final Class<? extends Annotation> annotationClass : annotationClasses)
		{
			if (isAnnotatedWith(clazz, annotationClass))
			{
				if (log.isDebugEnabled())
				{
					log.debug("Found class " + clazz.getSimpleName() + " annotation @"
							+ annotationClass.getSimpleName());
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Load class, if found on the classpath.
	 *
	 * @param className
	 *            class name to load and check
	 * @return the corresponding class object to <code>className</code> if found on the
	 *         classpath of the current class loader; otherwise, returns <code>null</code>
	 */
	public static Class<?> loadClass(final String className)
	{
		try
		{
			// return Class.forName(className);
			return getThreadClassLoader().loadClass(className);
		}
		catch (final Exception ignored)
		{
		}
		catch (final NoClassDefFoundError ignored)
		{
		}
		return null;
	}

	/**
	 * Load an annotation class by name.
	 *
	 * @param <T>
	 *            class type
	 * @param className
	 *            class' fully qualified name
	 * @return class class, if found
	 * @throws FatalBeanException
	 *             if class is not found
	 */
	public static <T> Class<? extends T> loadClassWithDetailedExceptions(
			final String className)
	{
		try
		{
			// return (Class<? extends T>)
			// Class.forName(cleanContextFileString(className));
			return (Class<? extends T>) Thread
					.currentThread()
					.getContextClassLoader()
					.loadClass(cleanContextFileString(className));
		}
		catch (final NoClassDefFoundError ignore)
		{
			throw new FatalBeanException(
					"The class "
							+ className
							+ " in the annotatedClasses property of the sessionFactory declaration is not an annotation type.");
		}
		catch (final ClassCastException e)
		{
			throw new FatalBeanException(
					"Could not find annotation class "
							+ className
							+ " in the annotatedClasses property of the sessionFactory declaration.");
		}
		catch (final Throwable throwable)
		{
			throw new FatalBeanException(
					"Could not add annotation class "
							+ className
							+ " to the list of annotations in the annotatedClasses property of the sessionFactory declaration: "
							+ throwable);
		}
	}

	/**
	 * Check whether the class implements at least one a list of specified annotation
	 * types.
	 *
	 * @param clazz
	 *            class to check
	 * @param superClasses
	 *            list of annotation types to match in <code>clazz</code>
	 * @return <code>true</code> if and only if at least one of the list of annotation
	 *         types is matched by the class
	 */
	public static boolean isClassASubClassOf(final Class<?> clazz,
			final Set<Class<?>> superClasses)
	{
		for (final Class<?> superClass : superClasses)
		{
			if (superClass.isAssignableFrom(clazz))
			{
				if (log.isDebugEnabled())
				{
					log.debug("Found class " + clazz.getSimpleName() + " super-class: "
							+ superClass.getSimpleName());
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the class implements at least one a list of specified annotation
	 * types.
	 *
	 * @param className
	 *            class name to load and check
	 * @return the corresponding class object to <code>className</code> if and only if at
	 *         least one of the list of annotation types is matched by the loaded class;
	 *         otherwise, returns <code>null</code>
	 */
	public static Class<?> isClassASubClassOf(final String className,
			final Set<Class<?>> superClasses)
	{

		try
		{
			// clazz = Class.forName(className);
			final Class<?> clazz = getThreadClassLoader().loadClass(className);

			// Add the class to the annotatedEntityClasses property.
			if (isClassASubClassOf(clazz, superClasses))
			{
				return clazz;
			}
		}
		catch (final Exception ignored)
		{
		}
		catch (final NoClassDefFoundError ignored)
		{
		}
		return null;
	}

	/**
	 * Return a list of all classes on the class path file that implement or extend at
	 * least one a list of types, and whose names match at least one of class name
	 * patterns.
	 *
	 * @param searchPatterns
	 *            A collection of resource search patterns.
	 * @param regexPatterns
	 *            list of class name patterns to match against
	 * @param resourceMatcher
	 *            contains the business logic to decide whether a resource matches the
	 *            criteria of this service
	 * @return list of classes that match at least one pattern and at least one annotation
	 * @throws IOEException
	 *             if jar resource is not found
	 */
	public static <T> Set<T> getMatchingResources(final ResourcePatternResolver resolver,
			final Set<String> searchPatterns, final Set<String> regexPatterns,
			final ResourceMatcher<T> resourceMatcher)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Scanning for resources");
			log.debug("Search patterns: " + searchPatterns);
			log.debug("Regular expression patterns: " + regexPatterns);
			log.debug("Matcher: " + resourceMatcher);
		}
		final StopWatch watch = new StopWatch();
		watch.start();
		final Set<T> matchingResults = newSet();
		// Search resources by every search pattern
		for (final String searchPattern : searchPatterns)
		{
			matchingResults.addAll(getMatchingResources(resolver, regexPatterns,
					resourceMatcher, searchPattern));
		}
		watch.stop();
		if (log.isInfoEnabled())
		{
			log.info("Scanning completed in " + watch.getTotalTimeMillis() + " ms");
			log.info(matchingResults.size() + " matching results found: "
					+ matchingResults);
		}
		return matchingResults;
	}

	/**
	 * Return a fully qualified class name of a path.
	 *
	 * @param path
	 *            path to class
	 * @return fully qualified class name of a path
	 */
	public static String pathToQualifiedClassName(final String path)
	{
		return path.replaceAll("/", ".").substring(0,
				path.length() - CLASS_SUFFIX.length());
	}

	/**
	 * Set the current thread's classloader to a class' class loader.
	 * <p>
	 * This gets around a bug that is similar to the following FUSE JIRA issues:
	 * http://fusesource.com/issues/browse/MR-249
	 * http://fusesource.com/issues/browse/ESB-801 Set the thread context classloader to
	 * be application jar classloader so that jaxb.index is visible to the JAXB context.
	 * Otherwise, the marshal() call will throw an exception.
	 *
	 * @param object
	 *            an object whose class' class loader is set on the current thread
	 */
	public static void setCurrentClassLoader(final Object object)
	{
		final ClassLoader classLoader = object.getClass().getClassLoader();
		if (log.isDebugEnabled())
		{
			log.debug("classLoader " + classLoader);
		}
		Thread.currentThread().setContextClassLoader(classLoader);
	}

	/**
	 * Return the parent directory of a file resource.
	 *
	 * @param fileResource
	 *            file resource
	 * @return parent directory path
	 */
	public static String getParentDirectoryOfFileResource(
			final org.springframework.core.io.Resource fileResource)
	{
		try
		{
			final String absolutePath = fileResource.getURL().toString();
			final int directoryEndIndex = absolutePath.length()
					- fileResource.getFilename().length();
			final String directory = absolutePath.substring(0, directoryEndIndex);
			// Remove trailing slash
			final String slash = Strings.VIRTUAL_DIRECTORY;
			final String trimmedDirectory = directory.endsWith(slash) ? directory
					.substring(0, directory.length() - slash.length()) : directory;
			return trimmedDirectory;
		}
		catch (final IOException e)
		{
			throw new BeanInitializationException("Could not locate directory: ", e);
		}
	}

	/**
	 * Return the package names of a class set.
	 *
	 * @param classes
	 *            class set
	 * @return set of class package names
	 */
	public static Set<String> getPackageNames(final Set<Class<?>> classes)
	{
		final Set<String> packageNames = CollectionUtil.newSet();
		for (final Class<?> clazz : classes)
		{
			packageNames.add(clazz.getPackage().getName());
		}
		return packageNames;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parse every resource in a search pattern and add to matching results.
	 *
	 * @param <T>
	 * @param resolver
	 * @param regexPatterns
	 * @param resourceMatcher
	 * @param searchPattern
	 * @return
	 * @throws IOException
	 */
	private static <T> Set<T> getMatchingResources(
			final ResourcePatternResolver resolver, final Set<String> regexPatterns,
			final ResourceMatcher<T> resourceMatcher, final String searchPattern)
	{
		final Set<T> matchingResults = newSet();
		try
		{
			final Resource[] resources = resolver.getResources(searchPattern);
			for (final Resource res : resources)
			{
				final String path = res.getURL().getPath();
				matchingResults.addAll(path.endsWith(".jar") ? getMatchingResourcesInJar(
						res, regexPatterns, resourceMatcher)
						: getMatchingResourcesInClassPath(path, regexPatterns,
								resourceMatcher));
			}
		}
		catch (final Exception ignored)
		{
			// NPE from a null resources array, IOExceptions getting resources, etc.
		}
		return matchingResults;
	}

	/**
	 * Return a list of all qualified resource names on a class path.
	 *
	 * @param path
	 *            resource path
	 * @return list of all qualified class names
	 */
	private static Set<String> listAllQualifiedResourceNames(final String path)
	{
		final Set<String> qualifiedNames = newSet();

		// Split the QName by the dot (i.e. '.') character
		final String[] pathParts = path.split(VIRTUAL_DIRECTORY);

		// Add the path parts one by one from the end of the array to the beginning
		// and save the intermediate results in the set
		final StringBuilder qName = StringUtil.newStringBuilder();
		for (int i = pathParts.length - 1; i >= 0; i--)
		{
			qName.insert(0, pathParts[i]);
			qualifiedNames.add(qName.toString());
			qName.insert(0, VIRTUAL_DIRECTORY);
		}

		return qualifiedNames;
	}

	/**
	 * Match a path against a set of regular patterns.
	 *
	 * @param path
	 *            path to match
	 * @param regexPatterns
	 *            A collection of regular expression patterns to find in the selected
	 *            resources
	 * @return result of matching
	 */
	private static boolean matchRegexPatterns(final String path,
			final Set<String> regexPatterns)
	{
		for (final String pattern : regexPatterns)
		{
			if (path.matches(pattern))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Return a list of all resources in a jar file that implement at least one a list of
	 * specified annotation types, and whose names match at least one of class name
	 * patterns.
	 *
	 * @param res
	 *            jar file resource name
	 * @param regexPatterns
	 *            list of patterns to match path against
	 * @param resourceMatcher
	 *            contains the business logic to decide whether a resource matches the
	 *            criteria of this service
	 * @return list of matching resources, transformed by the matcher to the result type
	 * @throws IOEException
	 *             if jar resource is not found
	 */
	private static <T> Set<T> getMatchingResourcesInJar(final Resource res,
			final Set<String> regexPatterns, final ResourceMatcher<T> resourceMatcher)
			throws IOException
	{
		final Set<T> matchResults = newSet();
		// Enumerate all entries in this JAR file.
		try (final JarFile jar = new JarFile(res.getFile())) {
			final Enumeration<JarEntry> jarEntries = jar.entries();
			while (jarEntries.hasMoreElements())
			{
				final String path = jarEntries.nextElement().getName();
				// If the entry matches patterns, deal with it. Also improves search
				// performance too by narrowing down the resource list.
				if (matchRegexPatterns(path, regexPatterns))
				{
					final T result = resourceMatcher.match(path);
					if (result != null)
					{
						matchResults.add(result);
					}
				}
			}
			return matchResults;
		}
	}

	/**
	 * Return a list of all classes on a classpath that implement or extend at least one
	 * of a list of specified annotation types, and whose names match at least one of
	 * class name patterns.
	 *
	 * @param path
	 *            class path
	 * @param regexPatterns
	 *            list of class name patterns to match against
	 * @param resourceMatcher
	 *            contains the business logic to decide whether a resource matches the
	 *            criteria of this service
	 * @return list of classes that match at least one pattern and at least one annotation
	 */
	private static <T> Set<T> getMatchingResourcesInClassPath(final String path,
			final Set<String> regexPatterns, final ResourceMatcher<T> resourceMatcher)
	{
		final Set<String> qualifiedNames = listAllQualifiedResourceNames(path);
		if (log.isDebugEnabled())
		{
			log.debug("All resources under path" + path + ":");
			log.debug(qualifiedNames.toString());
		}
		final Set<T> matchResults = newSet();
		for (final String qName : qualifiedNames)
		{
			// Apply the qualified class name pattern to improve the searching
			// performance.
			if (matchRegexPatterns(qName, regexPatterns))
			{
				if (log.isDebugEnabled())
				{
					log.debug("Matches regular expression: " + qName);
				}
				final T result = resourceMatcher.match(qName);
				if (result != null)
				{
					if (log.isDebugEnabled())
					{
						log.debug("Matches resource matcher criteria: " + result);
					}
					matchResults.add(result);
				}
			}
		}
		return matchResults;
	}
}
