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
import static edu.utah.further.core.api.text.StringUtil.cleanContextFileString;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternResolver;

import edu.utah.further.core.api.context.Implementation;

/**
 * This class automatically finds sub-classes of a list of super-types.
 * <code>Entity</code> annotation).
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
 * @version added supported for other Hibernate annotations, Jan 29, 2008
 */
@Implementation
public class ResourceMatchingFactoryBean<T> implements FactoryBean<Set<T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResourceMatchingFactoryBean.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Resource resolver.
	 */
	@Autowired
	private ResourcePatternResolver resolver;

	/**
	 * A collection of resource search patterns.
	 */
	private final Set<String> searchPatterns = newSet();

	/**
	 * A collection of qualified class name patterns to find in the selected resources.
	 */
	private final Set<String> regexPatterns = newSet();

	// ========================= FIELDS ====================================

	/**
	 * The output set of annotated classes.
	 */
	private final Set<T> matchingResults = newSet();

	/**
	 * Resource matcher corresponding to {@link #superClasses}.
	 */
	private ResourceMatcher<T> resourceMatcher;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: FactoryBean ===============

	/**
	 * Return an instance (possibly shared or independent) of the object managed by this
	 * factory.
	 * <p>
	 * As with a BeanFactory, this allows support for both the Singleton and Prototype
	 * design pattern.
	 * 
	 * @return instance of the object managed by this factory
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Set<T> getObject()
	{
		if (matchingResults.isEmpty())
		{
			if (log.isInfoEnabled())
			{
				log.info("Scanning and matching " + resourceMatcher);
				log.info("Search patterns: " + searchPatterns);
				log.info("Regular expression patterns: " + regexPatterns);
			}
			matchingResults.addAll(ClasspathUtil.getMatchingResources(resolver,
					searchPatterns, regexPatterns, resourceMatcher));
		}

		return matchingResults;
	}

	/**
	 * Return the type of product made by this factory. In this cases, a set.
	 * 
	 * @return the type of object that this FactoryBean creates
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<Set<T>> getObjectType()
	{
		return (Class<Set<T>>) matchingResults.getClass();
	}

	/**
	 * Indicates that this bean is a singleton.
	 * 
	 * @return true
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton()
	{
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Returns the set of resource search patterns.
	 * 
	 * @return the set of resource search patterns
	 */
	public Set<String> getSearchPatterns()
	{
		return searchPatterns;
	}

	/**
	 * Returns the the set of qualified class name patterns.
	 * 
	 * @return the the set of qualified class name patterns
	 */
	public Set<String> getRegexPatterns()
	{
		return regexPatterns;
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Inject the set of resource search patterns.
	 * 
	 * @param searchPatterns
	 *            the set of resource search patterns to set
	 */
	public void setSearchPatterns(final Set<String> searchPatterns)
	{
		// Regular expression are sensitive with special characters.
		for (final String pattern : searchPatterns)
		{
			this.searchPatterns.add(cleanContextFileString(pattern));
		}
	}

	/**
	 * Inject the set of qualified class name patterns.
	 * 
	 * @param regexPatterns
	 *            the set of qualified class name patterns to set
	 */
	public void setRegexPatterns(final Set<String> regexPatterns)
	{
		// Regular expression are sensitive with special characters.
		for (final String pattern : regexPatterns)
		{
			this.regexPatterns.add(cleanContextFileString(pattern));
		}
	}

	/**
	 * Set a new value for the resourceMatcher property.
	 * 
	 * @param resourceMatcher
	 *            the resourceMatcher to set
	 */
	public void setResourceMatcher(final ResourceMatcher<T> resourceMatcher)
	{
		this.resourceMatcher = resourceMatcher;
	}

	// ========================= PRIVATE METHODS ===========================
}
