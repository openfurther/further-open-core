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
import static edu.utah.further.core.data.util.JpaAnnotationUtil.hasAssociationAnnotation;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.PropertyMapping;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.ReflectionUtils;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.adapter.ScrollMode;

/**
 * Hibernate persistent layer utilities. In particular, these utilities centralize
 * generics warning suppression due to non-generic Hibernate API.
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
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jan 28, 2009
 */
@Utility
@Api
public final class HibernateUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * The maximum number of elements in an IN list.
	 */
	public static final int MAX_IN = 1000;

	/**
	 * Scope of this object in HQL.
	 */
	public static final String THIS = "this.";

	/**
	 * HQL clause that fetches all properties of the returned entity/entities.
	 */
	public static final String FETCH_ALL_PROPERTIES = "fetch all properties ";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private HibernateUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param <T>
	 * @param query
	 * @return
	 */
	public static <T> List<T> list(final Query query)
	{
		return query.list();
	}

	/**
	 * @deprecated This method does not work well due to Hibernate bug HHH-817, nor does
	 *             AliasToBeanResultTransformer handle multi-level property values.
	 *             Therefore it should not be used.
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-817
	 */
	@Deprecated
	public static Projection createProjectionList(final String identifierProperty,
			final Type identifierType, final String[] propertyNames,
			final Class<?> domainClass)
	{
		final ProjectionList projectionList = Projections.projectionList();
		if (identifierType.isComponentType())
		{
			final String[] idProperties = ((ComponentType) (identifierType))
					.getPropertyNames();
			for (final String idProperty : idProperties)
			{
				final String idPath = identifierProperty + "." + idProperty;
				projectionList.add(Projections.property(idPath));
			}
		}
		else
		{
			projectionList.add(Projections.id());
		}
		for (final String propertyName : propertyNames)
		{
			final Field field = ReflectionUtils.findField(domainClass, propertyName);
			if (!hasAssociationAnnotation(field))
			{
				projectionList.add(Projections.property(propertyName), propertyName);
			}

		}
		return projectionList;
	}

	/**
	 * @deprecated This method does not work well due to Hibernate bug HHH-817, nor does
	 *             AliasToBeanResultTransformer handle multi-level property values.
	 *             Therefore it should not be used.
	 *
	 * @param propertyNames
	 * @param alias
	 * @param aliasPath
	 * @param domainClass
	 * @return
	 *
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-817
	 */
	@Deprecated
	public static Projection createAliasedProjectionList(final String[] propertyNames,
			final String alias, final String aliasPath, final Class<?> domainClass)
	{
		final ProjectionList projectionList = Projections.projectionList();
		for (final String propertyName : propertyNames)
		{
			final Field field = ReflectionUtils.findField(domainClass, propertyName);
			if (!hasAssociationAnnotation(field))
			{
				final String aliasedProperty = alias + "." + propertyName;
				projectionList
						.add(Projections.property(aliasedProperty), aliasedProperty);
			}
		}
		return projectionList;
	}

	/**
	 * Returns the {@link PropertyMapping} object used by Hibernate to map object
	 * properties to database fields. This object is very useful when writing
	 * {@link Restrictions#sqlRestriction(String)}'s as you can look up column names of an
	 * entity instead of hard coding them. However, NOTE, that this should NOT be used for
	 * normal development and should only be used in cases where a Hibernate bug exists
	 * and you need the column names to develop a work around. Let Hibernate do what
	 * Hibernate does unless it does it wrong :)
	 *
	 * @param entityClass
	 * @param sessionFactory
	 * @return
	 * @throws ClassCastException
	 *             if {@link SessionFactory} can't be cast to
	 *             {@link SessionFactoryImplementor} or {@link EntityPersister} can't be
	 *             cast to {@link PropertyMapping}
	 */
	public static PropertyMapping getPropertyMapping(
			final Class<? extends PersistentEntity<?>> entityClass,
			final SessionFactory sessionFactory)
	{
		final SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
		return (PropertyMapping) sessionFactoryImplementor.getEntityPersister(entityClass
				.getName());
	}

	/**
	 * Generates an SQL in (...) statement to be used with in statements involving
	 * composite keys.
	 *
	 * This method is strictly a workaround utility method for an issue with using
	 * {@link Restrictions#in(String, java.util.Collection)} with a composite key. It
	 * generates an sql statement correctly but binds the parameters wrong. This issue has
	 * been reported SEVERAL times, as early as 05 and it still hasn't been fixed (it's
	 * 09). There is a 2 second correction that needs to be added to the code to fix the
	 * issue but for some reason Hibernate developers refuse to do it. In light of not
	 * knowing what else it might break in custom compiling Hibernate with the fix it was
	 * preferred to do a work around. It is to be used directly with
	 * {@link Restrictions#sqlRestriction(String)}.
	 *
	 * @param entityClass
	 *            the entity which contains the composite key for which an SQL in
	 *            statement needs to be generated
	 * @param sessionFactory
	 *            the {@link SessionFactory} assosciated with the entities
	 * @param elementsSize
	 *            the size of the elements that will be in the in clause
	 * @return
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-708
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-1575
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-1743
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-1832
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-1972
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-3164
	 */
	public static String sqlRestrictionCompositeIn(
			final Class<? extends PersistentEntity<?>> entityClass,
			final SessionFactory sessionFactory, final int elementsSize)
	{
		final ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
		final String identifierName = classMetadata.getIdentifierPropertyName();
		final Type identifierType = classMetadata.getIdentifierType();

		if (!(identifierType.isComponentType()))
		{
			return Strings.EMPTY_STRING;
		}

		final ComponentType componentType = (ComponentType) identifierType;
		final String[] idPropertyNames = componentType.getPropertyNames();

		final PropertyMapping propertyMapping = getPropertyMapping(entityClass,
				sessionFactory);

		final StringBuilder inString = StringUtil.newStringBuilder();

		// field names -- ({alias}.field1, {alias}.field2)
		inString.append('(');
		for (int i = 0; i < idPropertyNames.length; i++)
		{
			inString.append("{alias}").append('.')
			// Look up the database field from the property path
					.append(propertyMapping.toColumns(identifierName + "."
							+ idPropertyNames[i])[0]);
			if (i + 1 < idPropertyNames.length)
			{
				inString.append(", ");
			}
			else
			{
				inString.append(')');
			}
		}

		// values -- in ( (?, ?), (?, ?) )
		inString.append(" in ").append('(');
		for (int i = 0; i < elementsSize; i++)
		{
			if (elementsSize % MAX_IN == 0)
			{
				inString.append(')');
				inString.append(Strings.EMPTY_STRING);
				inString.append("or");
				inString.append(Strings.EMPTY_STRING);
				inString.append('(');
			}

			inString.append('(');
			for (int j = 0; j < idPropertyNames.length; j++)
			{
				inString.append('?');
				if (j + 1 < idPropertyNames.length)
				{
					inString.append(",");
				}
			}
			inString.append(')');
			if ((i + 1 < elementsSize) && (i + 1 % MAX_IN != 0))
			{
				inString.append(", ");
			}
		}
		inString.append(')');

		return inString.toString();
	}

	/**
	 * @param sessionFactory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<Class<? extends PersistentEntity<?>>> getEntityClasses(
			final SessionFactory sessionFactory)
	{
		final Set<Class<? extends PersistentEntity<?>>> entityClasses = CollectionUtil
				.newSet();
		for (final Object persister : sessionFactory.getAllClassMetadata().values())
		{
			final Class<?> entityClass = ((EntityPersister) persister)
					.getClassMetadata()
					.getMappedClass(EntityMode.POJO);
			// Must perform an unchecked cast because a) Hibernate's API uses raw types
			// and b) Hibernate is not aware of our PersistentEntity<?> at compile-time,
			// although effectively enforces it on all mapped classes at runtime.
			entityClasses.add((Class<? extends PersistentEntity<?>>) entityClass);
		}
		return entityClasses;
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param alias
	 * @param fetchAllProperties
	 * @return
	 */
	public static <T extends PersistentEntity<?>> String getFromClauseWhere(
			final Class<T> domainClass, final String alias,
			final boolean fetchAllProperties)
	{
		return getFromClause(domainClass, alias, fetchAllProperties) + "where ";
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param alias
	 * @param fetchAllProperties
	 * @return
	 *
	 */
	public static <T extends PersistentEntity<?>> String deleteFromClauseWhere(
			final Class<T> domainClass, final String alias)
	{
		return "delete from " + domainClass.getSimpleName() + " " + alias + " " + "where ";
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param alias
	 * @return
	 */
	public static <T extends PersistentEntity<?>> String getFromClause(
			final Class<T> domainClass, final String alias,
			final boolean fetchAllProperties)
	{
		return "from " + domainClass.getSimpleName() + " " + alias + " "
				+ (fetchAllProperties ? FETCH_ALL_PROPERTIES : Strings.EMPTY_STRING);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param value
	 * @param queryCriteria
	 * @return
	 */
	public static <T extends PersistentEntity<?>> List<T> findByCriteriaUsingHql(
			final HibernateTemplate hibernateTemplate,
			final Class<? extends T> entityClass, final String alias,
			final boolean fetchAllProperties, final String queryCriteria,
			final Object value)
	{
		return hibernateTemplate.find(
				getFromClauseWhere(entityClass, alias, fetchAllProperties)
						+ queryCriteria, value);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param value
	 * @param queryCriteria
	 * @return
	 */
	public static <T extends PersistentEntity<?>> List<T> findByCriteriaUsingHql(
			final HibernateTemplate hibernateTemplate,
			final Class<? extends T> entityClass, final String alias,
			final boolean fetchAllProperties, final String queryCriteria,
			final Object... values)
	{
		return hibernateTemplate.find(
				getFromClauseWhere(entityClass, alias, fetchAllProperties)
						+ queryCriteria, values);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param value
	 * @param queryCriteria
	 * @return
	 *
	 */
	public static <T extends PersistentEntity<?>> int deleteByCriteriaUsingHql(
			final HibernateTemplate hibernateTemplate,
			final Class<T> entityClass, final String alias, final String queryCriteria,
			final Object... values)
	{
		return hibernateTemplate.bulkUpdate(
				deleteFromClauseWhere(entityClass, alias)
						+ queryCriteria, values);
	}

	/**
	 * @param <T>
	 * @param results
	 * @return
	 */
	public static <T> List<T> asList(final ScrollableResults results)
	{
		final List<T> resultList = CollectionUtil.newList();
		while (results.next())
		{
			resultList.add((T) results.get(0));
		}
		return resultList;
	}

	/**
	 * @param <T>
	 * @param results
	 * @return
	 */
	public static <T> List<T> scrollAsList(final GenericCriteria criteria,
			final ScrollMode scrollMode)
	{
		return asList(criteria.scroll(scrollMode));
	}
}
