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
package edu.utah.further.core.data.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Utility class for retrieving JPA annotations and JPA annotation properties of JPA
 * entities.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 22, 2009
 */
public final class JpaAnnotationUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Private constructor to prevent construction
	 */
	private JpaAnnotationUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Determines whether a JPA annotated field has a an association annotation.
	 * 
	 * @param field
	 *            the field to check
	 * @return true if it has an association annotation, false if it does not
	 */
	public static boolean hasAssociationAnnotation(final Field field)
	{
		for (final Annotation annotation : field.getAnnotations())
		{
			if (ReflectionUtil.instanceOfOneOf(annotation, OneToOne.class,
					OneToMany.class, ManyToOne.class, ManyToMany.class))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves the targetEntity property of a JPA {@link OneToOne} or {@link OneToMany}
	 * annotation. Unchecked warning are suppressed due to exception check.
	 * 
	 * @param field
	 *            The field which contains the JPA annotation.
	 * @return a class representing the targetEntity
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends PersistentEntity<?>> getTargetEntity(final Field field)
	{
		final Class<?> targetEntityClass = getTargetEntityFromAnnotation(field);
		if (PersistentEntity.class.isAssignableFrom(targetEntityClass))
		{
			return (Class<? extends PersistentEntity<?>>) targetEntityClass;
		}
		throw new IllegalStateException(
				"Found a Hibernate association to a non-persistent entity "
						+ targetEntityClass);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param field
	 * @return
	 */
	private static Class<?> getTargetEntityFromAnnotation(final Field field)
	{
		for (final Annotation annotation : field.getAnnotations())
		{
			if (instanceOf(annotation, OneToMany.class))
			{
				return ((OneToMany) annotation).targetEntity();
			}
			else if (instanceOf(annotation, OneToOne.class))
			{
				return ((OneToOne) annotation).targetEntity();
			}
		}
		return null;
	}

}
