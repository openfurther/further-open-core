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
package edu.utah.further.core.data.service;

import static edu.utah.further.core.api.collections.CollectionUtil.getNullSafeList;
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.data.util.HibernateUtil.getFromClauseWhere;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.data.util.HibernateUtil;
import edu.utah.further.core.query.domain.SearchQuery;

/**
 * A Hibernate generic DAO implementation that uses HQL to eagerly fetch all properties of
 * all returned persistent entities.
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
 * @version Oct 13, 2008
 */
@Implementation
@Repository("daoEager")
public class DaoHibernateEagerImpl extends DaoHibernateImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DaoHibernateEagerImpl.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for a Spring DAO bean.
	 *
	 * @param sessionFactory
	 *            Hibernate session factory
	 */
	@Autowired
	public DaoHibernateEagerImpl(final SessionFactory sessionFactory)
	{
		super(sessionFactory);
	}

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPLEMENTATION: Dao =======================

	/**
	 * @param <T>
	 * @param domainClass
	 * @param start
	 * @param max
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findAll(java.lang.Class, int, int)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findAll(final Class<T> domainClass,
			final int start, final int max)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return getCurrentSession()
				.createQuery(getFromClause(entityClass))
				.setFirstResult(start)
				.setMaxResults(max)
				.setFetchSize(max)
				.list();
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findAll(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends PersistentEntity<?>> List<T> findAll(final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return getNullSafeList(getHibernateTemplate().find(getFromClause(entityClass)));
	}

	/**
	 * @param <T>
	 * @param exampleInstance
	 * @param excludeZeros
	 * @param excludeProperty
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByExample(edu.utah.further.core.api.data.PersistentEntity,
	 *      boolean, java.lang.String[])
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByExample(final T exampleInstance,
			final boolean excludeZeros, final String... excludeProperty)
	{
		warnImplementationNotWrittenYet();
		return super.findByExample(exampleInstance, excludeZeros, excludeProperty);
	}

	/**
	 * @param <T>
	 * @param <ID>
	 * @param domainClass
	 * @param id
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findById(java.lang.Class,
	 *      java.lang.Comparable)
	 */
	@Override
	public <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> T findById(
			final Class<T> domainClass, final ID id)
	{
		return findUniqueResultByCriteriaUsingHql(domainClass, "id = ?", id);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param propertyName
	 * @param value
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByLikeProperty(java.lang.Class,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByLikeProperty(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		return findByCriteriaUsingHql(domainClass, propertyName + " like ?", value);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param propertyName
	 * @param value
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByProperty(java.lang.Class,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByProperty(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		return findByCriteriaUsingHql(domainClass, propertyName + " = ?", value);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param properties
	 * @return
	 * @see edu.utah.further.core.data.service.DaoHibernateImpl#findByProperties(java.lang.Class,
	 *      java.util.Map)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByProperties(
			final Class<T> domainClass, final Map<String, Object> properties)
	{
		warnImplementationNotWrittenYet();
		return super.findByProperties(domainClass, properties);
	}

	/**
	 * @param <T>
	 * @param exampleInstance
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByUniqueExample(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> T findByUniqueExample(final T exampleInstance)
	{
		warnImplementationNotWrittenYet();
		return super.findByUniqueExample(exampleInstance);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param propertyName
	 * @param uniqueValue
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByUniqueProperty(java.lang.Class,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> T findByUniqueProperty(
			final Class<T> domainClass, final String propertyName,
			final Object uniqueValue)
	{
		return findUniqueResultByCriteriaUsingHql(domainClass, propertyName + " = ?",
				uniqueValue);
	}

	// ========================= IMPLEMENTATION: SearchEngine ==============

	/**
	 * @param searchCriteria
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchEngine#search(java.lang.Class,
	 *      edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public <E extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> List<E> search(
			final Class<E> domainClass, final SearchQuery searchCriteria)
	{
		warnImplementationNotWrittenYet();
		return super.search(domainClass, searchCriteria);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Useful to place in unsupported method during code development.
	 */
	private void warnImplementationNotWrittenYet()
	{
		if (log.isWarnEnabled())
		{
			log
					.warn("An Eagerly-fetching implementation has been written yet. Using default implementation");
		}
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param queryCriteria
	 * @param value
	 * @return
	 */
	private <T extends PersistentEntity<?>> T findUniqueResultByCriteriaUsingHql(
			final Class<T> domainClass, final String queryCriteria, final Object value)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return (T) getCurrentSession()
				.createQuery(
						getFromClauseWhere(entityClass, EMPTY_STRING, true)
								+ queryCriteria)
				.setParameter(0, value)
				.setMaxResults(1)
				.uniqueResult();
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @param queryCriteria
	 * @param value
	 * @return
	 */
	private <T extends PersistentEntity<?>> List<T> findByCriteriaUsingHql(
			final Class<T> domainClass, final String queryCriteria, final Object value)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return HibernateUtil.findByCriteriaUsingHql(getHibernateTemplate(), entityClass,
				EMPTY_STRING, true, queryCriteria, value);
	}

	/**
	 * @param <T>
	 * @param domainClass
	 * @return
	 */
	private static <T extends PersistentEntity<?>> String getFromClause(
			final Class<T> domainClass)
	{
		return HibernateUtil.getFromClause(domainClass, EMPTY_STRING, true);
	}
}
