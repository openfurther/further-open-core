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
package edu.utah.further.core.util.context;

import static edu.utah.further.core.api.text.StringUtil.quote;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Set;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Alias;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.util.io.ResourceMatchingFactoryBean;

/**
 * This class builds a map of aliases-to-enumerated-types. Includes only enumerated types
 * annotated by {@link Alias}.
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
// @Service("enumAlias") // no default constructor
public class EnumAliasService extends ResourceMatchingFactoryBean<Class<?>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(EnumAliasService.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	/**
	 * The list of annotated classes. May be set by a constructor or by scanning for
	 * annotations.
	 */
	private volatile BidiMap<String, Class<? extends Enum<?>>> enumAliases;

	/**
	 * A set of enumerated classes that implement the {@link Labeled} interface.
	 */
	private volatile Set<Class<? extends Enum<?>>> labeledClasses;

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Return the enum aliases map.
	 *
	 * @return the enum aliases map
	 */
	public BidiMap<String, Class<? extends Enum<?>>> getEnumAliases()
	{
		// using the double-checked locking with volatile
		// @see http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
		if (enumAliases == null)
		{
			synchronized (this)
			{
				if (enumAliases == null)
				{
					enumAliases = initializeEnumAliases();
				}
			}
		}
		// Defensive copy. TODO: move to CollectionUtil as a generic factory method
		return new DualHashBidiMap<>(enumAliases);
	}

	/**
	 * Return the labeled classes set.
	 *
	 * @return the labeled classes set
	 */
	public Set<Class<? extends Enum<?>>> getLabeledClasses()
	{
		// using the double-checked locking with volatile
		// @see http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
		if (labeledClasses == null)
		{
			synchronized (this)
			{
				if (labeledClasses == null)
				{
					labeledClasses = initializeLabeledClasses();
				}
			}
		}
		return CollectionUtil.newSet(labeledClasses);
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set the static enum alias map field.
	 * <p>
	 * Warning: do not call this method outside this class. It is meant to be called by
	 * Spring when instantiating the singleton instance of this bean.
	 */
	@SuppressWarnings("unchecked")
	private BidiMap<String, Class<? extends Enum<?>>> initializeEnumAliases()
	{
		final Class<Alias> annotationClass = Alias.class;
		// TODO: move to CollectionUtil as a generic factory method
		final BidiMap<String, Class<? extends Enum<?>>> map = new DualHashBidiMap<>();
		for (final Class<?> annotatedClass : this.getObject())
		{
			if (annotatedClass.isEnum())
			{
				final String alias = annotatedClass
						.getAnnotation(annotationClass)
						.value();
				final Class<? extends Enum<?>> otherClass = map.get(alias);
				if (otherClass != null)
				{
					// This is the second time that we encountered the alias
					// key, signal name collision
					final String errorMessage = "Enum alias collision!!! Both "
							+ otherClass + ", " + annotatedClass
							+ " have the same alias: " + quote(alias) + ". Rename the @"
							+ annotationClass.getSimpleName()
							+ " annotation value one of them.";
					// Log message before throwing, otherwise Spring will
					// show an incomprehensible TargetInvocationException
					// without its cause here.
					log.error(errorMessage);
					throw new ApplicationException(errorMessage);
				}
				map.put(alias, (Class<? extends Enum<?>>) annotatedClass);
			}
		}
		return map;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<? extends Enum<?>>> initializeLabeledClasses()
	{
		final Set<Class<? extends Enum<?>>> set = CollectionUtil.newSet();
		for (final Class<?> annotatedClass : this.getObject())
		{
			if (annotatedClass.isEnum()
					&& ReflectionUtil.classIsInstanceOf(annotatedClass, Labeled.class))
			{
				set.add((Class<? extends Enum<?>>) annotatedClass);
			}
		}
		return set;
	}

}
