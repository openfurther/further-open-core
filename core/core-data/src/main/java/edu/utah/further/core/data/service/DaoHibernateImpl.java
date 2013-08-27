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
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_BOXED_LONG;
import static edu.utah.further.core.api.message.Messages.createMessage;
import static edu.utah.further.core.api.message.Messages.updateMessage;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.data.hibernate.adapter.CriteriaType.CRITERIA;
import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.ilike;
import static org.hibernate.criterion.Restrictions.lt;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteriaFactory;
import edu.utah.further.core.data.hibernate.query.QueryBuilderHibernateImpl;
import edu.utah.further.core.data.util.HibernateUtil;
import edu.utah.further.core.query.domain.SearchEngine;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryBuilderImpl;

/**
 * Implementation of the generic DAO interface for the Hibernate persistent layer.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz
 *         {@code <dustin.schultz@utah.edu>}
 * @version Nov 20, 2009
 */
@Implementation
@Repository("dao")
public class DaoHibernateImpl extends HibernateDaoSupport implements Dao, SearchEngine
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DaoHibernateImpl.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Cached list of entity classes registered with the session factory.
	 */
	@Final
	private Set<Class<? extends PersistentEntity<?>>> entityClasses;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for a Spring DAO bean.
	 * 
	 * @param sessionFactory
	 *            Hibernate session factory
	 */
	@Autowired
	public DaoHibernateImpl(final SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * Initialize the persistent layer service.
	 */
	@PostConstruct
	private void setupEntityClasses()
	{
		this.entityClasses = HibernateUtil.getEntityClasses(getSessionFactory());
		if (log.isDebugEnabled())
		{
			log.debug("Entity classes: " + getEntityClasses());
		}
	}

	// ========================= IMPLEMENTATION: Dao =======================

	/**
	 * @param domainClass
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#count()
	 */
	@Override
	public <T extends PersistentEntity<?>> Long count(final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		// "domain" is used as a dummy alias for the count entity
		return (Long) getHibernateTemplate().find(
				"select count(*) from " + entityClass.getName() + " domain").get(0);
	}

	/**
	 * @param domain
	 * @see edu.utah.further.core.api.data.Dao#delete(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void delete(final T domain)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Deleting " + domain + " class " + domain.getClass());
		}
		getHibernateTemplate().delete(domain);
	}

	/**
	 * Delete all instances of the domain entity from database using a query. Does not
	 * seem to respect cascading operations, but could be faster than
	 * {@link #deleteAll(Class)}.
	 * 
	 * @param domain
	 *            domain entity class
	 * @see edu.utah.further.core.api.data.Dao#deleteAll(java.lang.Class)
	 */
	@Override
	public <T extends PersistentEntity<?>> void deleteAllUsingQuery(
			final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		if (log.isDebugEnabled())
		{
			log.debug("Deleting all " + quote(entityClass.toString()) + " entities");
		}
		getCurrentSession().createQuery(
				"delete from " + entityClass.getName() + " domain").executeUpdate();
	}

	/**
	 * Delete all instances of the domain entity from database.
	 * 
	 * @param domain
	 *            domain entity class
	 * @see edu.utah.further.core.api.data.Dao#deleteAll(java.lang.Class)
	 */
	@Override
	public <T extends PersistentEntity<?>> void deleteAll(final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		if (log.isDebugEnabled())
		{
			log.debug("Deleting all " + quote(entityClass.toString()) + " entities");
		}
		final List<? extends T> allEntities = findAll(entityClass);
		getHibernateTemplate().deleteAll(allEntities);
	}

	/**
	 * Remove a domain entity from the current session.
	 * 
	 * @param domain
	 *            domain entity to be removed from the current session
	 * @see edu.utah.further.core.api.data.Dao#evict(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void evict(final T domain)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Evicting " + domain + " class " + domain.getClass());
		}
		getHibernateTemplate().evict(domain);
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
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		try
		{
			final T domain = getHibernateTemplate().get(entityClass, id);
			if ((domain != null) && (domain.getId() != null))
			{
				getHibernateTemplate().initialize(domain);
			}
			return domain;
		}
		catch (final ObjectNotFoundException e)
		{
			return null;
		}
		catch (final EntityNotFoundException e)
		{
			return null;
		}
	}

	/**
	 * Read an entity from the database. Pre-populated fields are overridden if they are
	 * persistent, or unaffected if they are transient. <code>entity</code> must of course
	 * be non-<code>null</code> because Java passes the entity reference by value, so it
	 * cannot be changed -- only the object it points to can be. Assumes that a persistent
	 * entity with this <code>id</code> exists in the database.
	 * 
	 * @param <T>
	 *            domain entity type
	 * @param <ID>
	 *            identifier type
	 * @param id
	 *            unique identifier to search for and load by
	 * @param entity
	 *            entity to be read
	 * @return loaded persistent entity
	 * @see edu.utah.further.core.api.data.Dao#read(java.io.Serializable,
	 *      edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> void read(
			final T entity, final ID id)
	{
		getHibernateTemplate().load(entity, id);
	}

	/**
	 * @param domainClass
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findAll()
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findAll(final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return getHibernateTemplate().find("from " + entityClass.getName());
	}

	/**
	 * Find all entity of this type within a given range
	 * 
	 * @param the
	 *            starting record
	 * @param the
	 *            max records to retrieve
	 * @return a list of all entity of this type
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findAll(final Class<T> domainClass,
			final int start, final int max)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return getCurrentSession()
				.createQuery("from " + entityClass.getName())
				.setFirstResult(start)
				.setMaxResults(max)
				.setFetchSize(max)
				.list();
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
		final GenericCriteria crit = createCriteria(exampleInstance.getClass());
		final Example example = Example.create(exampleInstance);
		if (excludeZeros)
		{
			example.excludeZeroes();
		}
		for (final String exclude : excludeProperty)
		{
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return getNullSafeList(crit.<T> list());
	}

	/**
	 * @param propertyName
	 * @param value
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByLikeProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByLikeProperty(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final GenericCriteria crit = createCriteria(entityClass);
		crit.add(ilike(propertyName, "%" + value + "%"));
		// crit.addOrder(Order.asc(propertyName));
		return getNullSafeList(crit.<T> list());
	}

	/**
	 * @param propertyName
	 * @param value
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByProperty(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		final Criterion c = eq(propertyName, value);
		return findByCriteria(domainClass, c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.data.Dao#findByPropertyGt(java.lang.Class,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByPropertyGt(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		final Criterion c = gt(propertyName, value);
		return findByCriteria(domainClass, c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.data.Dao#findByPropertyLt(java.lang.Class,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByPropertyLt(
			final Class<T> domainClass, final String propertyName, final Object value)
	{
		final Criterion c = lt(propertyName, value);
		return findByCriteria(domainClass, c);
	}

	/**
	 * @param exampleInstance
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByUniqueExample(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> T findByUniqueExample(final T exampleInstance)
	{
		final GenericCriteria crit = createCriteria(exampleInstance.getClass());
		crit.add(Example.create(exampleInstance));
		return this.<T> findUniqueResult(crit);
	}

	/**
	 * We know that if we use this method, the results should contain zero or one records,
	 * but set the max # of records to 1 just in case. Then uniqueResult() will not throw
	 * a {@link NonUniqueResultException}.
	 * 
	 * @param propertyName
	 * @param uniqueValue
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByUniqueProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> T findByUniqueProperty(
			final Class<T> domainClass, final String propertyName,
			final Object uniqueValue)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final GenericCriteria crit = createCriteria(entityClass);
		crit.add(eq(propertyName, uniqueValue));
		return this.<T> findUniqueResult(crit);
	}

	/**
	 * @param propertyName
	 * @param value
	 * @return
	 * @see edu.utah.further.core.api.data.Dao#findByProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> findByProperties(
			final Class<T> domainClass, final Map<String, Object> properties)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final GenericCriteria criteria = createCriteria(entityClass);
		for (final Map.Entry<String, Object> property : properties.entrySet())
		{
			criteria.add(eq(property.getKey(), property.getValue()));
		}
		return findByCriteria(criteria);
	}

	/**
	 * @see edu.utah.further.core.api.data.Dao#getById(java.lang.Class,
	 *      java.io.Serializable)
	 */
	@Override
	public <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> T getById(
			final Class<T> domainClass, final ID id)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final T domain = getHibernateTemplate().get(entityClass, id);
		getHibernateTemplate().initialize(domain);
		return domain;
	}

	/**
	 * Persist an object to the database. This is useful for transient entities with
	 * embedded IDs that can be non-<code>null</code>. *
	 * 
	 * @param domain
	 *            a transient object
	 * @see edu.utah.further.core.api.data.Dao#create(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void create(final T domain)
	{
		if (log.isDebugEnabled())
		{
			createMessage(domain);
		}
		getHibernateTemplate().persist(domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.data.Dao#save(edu.utah.further.core.api.data.PersistentEntity
	 * )
	 */
	@Override
	public <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> ID save(
			final T domain)
	{
		return (ID) getHibernateTemplate().save(domain);
	}

	/**
	 * Performs save-or-update operation. Will never fail (even on a transient object).
	 * 
	 * @param arg0
	 *            object to save-or-update
	 * @see edu.utah.further.core.api.data.Dao#update(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void update(final T domain)
	{
		if (log.isDebugEnabled())
		{
			updateMessage(domain);
		}
		if (domain.getId() == null)
		{
			// Transient entity
			getHibernateTemplate().persist(domain);
		}
		else
		{
			// Detached entity
			getHibernateTemplate().saveOrUpdate(domain);
			// getHibernateTemplate().merge(domain);
		}
	}

	/**
	 * Performs a merge operation.
	 * 
	 * @param arg0
	 *            object to merge
	 * @see http ://www.hibernate.org/hib_docs/v3/reference/en/html_single/#objectstate
	 *      -saveorupdate
	 * @see edu.utah.further.core.api.data.Dao#merge(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void merge(final T domain)
	{
		getHibernateTemplate().merge(domain);
	}

	/**
	 * Re-read the state of the given persistent instance.
	 * 
	 * @param domain
	 *            the persistent instance to re-read
	 * @see edu.utah.further.core.api.data.Dao#refresh(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public <T extends PersistentEntity<?>> void refresh(final T domain)
	{
		getHibernateTemplate().refresh(domain, LockMode.PESSIMISTIC_WRITE);
	}

	// /**
	// * Close current persistence session. Deprecated, use {@link DataService}
	// *
	// * @see edu.utah.further.core.api.data.DataService#closeSession()
	// */
	// @Deprecated
	// public void closeSession()
	// {
	// getHibernateTemplate().getSessionFactory().getCurrentSession().close();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.data.Dao#executeNamedQuery(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public <T extends PersistentEntity<?>> List<T> executeNamedQuery(
			final String namedQuery, final Object... parameters)
	{
		return executeNamedQuery(namedQuery, DEFAULT_NAMED_PARAM_LIST, parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.data.Dao#executeNamedQuery(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends PersistentEntity<?>> List<T> executeNamedQuery(
			final String namedQuery, final String paramBindName,
			final Object... parameters)
	{
		final Query query = getHibernateTemplate()
				.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(namedQuery)
				.setParameterList(paramBindName, parameters);
		return getNullSafeList(query.list());
	}

	/**
	 * Flush the current persistent session.
	 * 
	 * @see edu.utah.further.core.api.data.DataService#flush()
	 */
	@Override
	public void flush()
	{
		getCurrentSession().flush();
	}

	/**
	 * Close the current persistent session.
	 * 
	 * @see edu.utah.further.core.api.data.DataService#close()
	 */
	@Override
	public void close()
	{
		getCurrentSession().close();
	}

	/**
	 * Return the identifier value of the given entity as associated with this session. An
	 * exception is thrown if the given entity instance is transient or detached in
	 * relation to this session.
	 * 
	 * @param domainObject
	 *            a persistent instance
	 * @return the identifier; if the instance is transient or associated with a different
	 *         session <code>HibernateException</code>, returns
	 *         <code>INVALID_VALUE_LONG_BOXED</code>
	 * @see edu.utah.further.core.api.data.DataService#getIdentifier(edu.utah.further.core.api.data.PersistentEntity,
	 *      java.io.Serializable)
	 */
	@Override
	public Serializable getIdentifier(final PersistentEntity<?> domainObject)
	{
		try
		{
			return getCurrentSession().getIdentifier(domainObject);
		}
		catch (final TransientObjectException e)
		{
			return INVALID_VALUE_BOXED_LONG;
		}
	}

	/**
	 * Return the entity name for a persistent entity. If the entity is <code>null</code>,
	 * returns <code>null</code>. If the entity is transient, returns the class name of
	 * the entity as a POJO.
	 * 
	 * @param domainObject
	 *            a persistent entity
	 * @return the entity name
	 * @see edu.utah.further.core.api.data.DataService#getEntityName(edu.utah.further.core.api.data.PersistentEntity)
	 */
	@Override
	public String getEntityName(final PersistentEntity<?> domainObject)
	{
		if (domainObject == null)
		{
			return null;
		}

		try
		{
			return getCurrentSession().getEntityName(domainObject);
		}
		catch (final HibernateException e)
		{
			return domainObject.getClass().getName();
		}
	}

	/**
	 * Return the list of persistent classes registered with the session factory.
	 * 
	 * @return the list of persistent classes
	 * @see edu.utah.further.core.api.data.DataService#getEntityClasses()
	 */
	@Override
	public Set<Class<? extends PersistentEntity<?>>> getEntityClasses()
	{
		return entityClasses;
	}

	/**
	 * Return the entity implementing or extending a base class.
	 * 
	 * @param <T>
	 *            base class/interface type
	 * @param baseClass
	 *            base class/interface
	 * @return the unique entity class implementing or extending a base class in the
	 *         session factory
	 * @throws ApplicationException
	 *             if no sub-class or more than one sub-class if found in the session
	 *             factory
	 * @see edu.utah.further.core.api.data.DataService#getEntityClass(java.lang.Class)
	 */
	@Override
	public <T extends PersistentEntity<?>> Class<? extends T> getEntityClass(
			final Class<T> baseClass)
	{
		return ReflectionUtil.<T, PersistentEntity<?>> getUniqueSubclassInSet(
				entityClasses, baseClass);
	}

	// ========================= IMPLEMENTATION: SearchEngine ==============

	/**
	 * @param searchCriteria
	 * @return
	 * @see edu.utah.further.core.query.domain.SearchEngine#search(java.lang.Class,
	 *      edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> List<T> search(
			final Class<T> domainClass, final SearchQuery searchCriteria)
	{
		// The entity class passed in may be an interface, get it's implementation
		final Class<? extends T> entityClass = getEntityClass(domainClass);

		// Ensure that the rootObject is the right object, (e.g. not the interface) by
		// copying and resetting it.
		final SearchQuery query = new SearchQueryBuilderImpl(searchCriteria)
				.setRootObject(entityClass.getSimpleName())
				.build();
		
		final GenericCriteria criteria = QueryBuilderHibernateImpl.convert(
				CriteriaType.CRITERIA, entityClass.getPackage().getName(),
				getSessionFactory(), query);
		
		if (log.isTraceEnabled())
		{
			log.trace("Search Criteria: " + query);
			log.trace("Hibernate Search Criteria: " + criteria);
		}
		
		return findByCriteria(criteria);
	}

	// ========================= GETTERS & SETTERS =========================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return search criteria.
	 * 
	 * @return Return search criteria
	 */
	protected final <T extends PersistentEntity<?>> GenericCriteria createCriteria(
			final Class<T> domainClass)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		return GenericCriteriaFactory
				.criteria(CRITERIA, entityClass, getCurrentSession());
	}

	/**
	 * Use this inside subclasses as a convenience method to search for items by criteria.
	 * 
	 * @param criterion
	 *            a variable list of criteria
	 * @return list of items that match the criteria
	 */
	protected final <T extends PersistentEntity<?>> List<T> findByCriteria(
			final Class<T> domainClass, final Criterion... criterion)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final GenericCriteria crit = createCriteria(entityClass);
		for (final Criterion c : criterion)
		{
			crit.add(c);
		}
		return findByCriteria(crit);
	}

	/**
	 * Find by criteria object.
	 * 
	 * @param criteria
	 *            Hibernate criteria object
	 * @return list of items that match the criteria
	 */
	protected final <T extends PersistentEntity<?>> List<T> findByCriteria(
			final GenericCriteria criteria)
	{
		final List<T> list = criteria.setResultTransformer(DISTINCT_ROOT_ENTITY).list();
		return getNullSafeList(list);
	}

	/**
	 * Plug-in point to allow DAOs the ability to manage how unique results are handled.
	 * 
	 * @param crit
	 *            the search criteria to perform against the persistent class
	 * @return unique item matching the specified criteria
	 */
	protected final <T extends PersistentEntity<?>> T findUniqueResult(
			final GenericCriteria crit)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Finding unique result by criteria: " + crit);
		}
		crit.setMaxResults(1);
		return crit.<T> uniqueResult();
	}

	/**
	 * Use this inside subclasses as a convenience method to search for items by criteria.
	 * 
	 * @param criterion
	 *            a variable list of criteria
	 * @return list of items that match the criteria
	 */
	protected final <T extends PersistentEntity<?>> T findUniqueResult(
			final Class<T> domainClass, final Criterion... criterion)
	{
		final Class<? extends T> entityClass = getEntityClass(domainClass);
		final GenericCriteria crit = createCriteria(entityClass);
		for (final Criterion c : criterion)
		{
			crit.add(c);
		}
		return crit.<T> uniqueResult();
	}

	// /**
	// * Prevent calling {@link #getSession()} to avoid running out of connections. Likely
	// * caused FUR-1190. Can't override because the super-class method is final, but
	// * we could replace inheritance by delegation in the future to achieve that.
	// *
	// * @see http://forum.springsource.org/archive/index.php/t-48676.html
	// * @see https://jira.chpc.utah.edu/browse/FUR-1190
	// */
	// protected final Session getSession()
	// {
	// throw new UnsupportedOperationException("Use getCurrentSession() instead");
	// }

	/**
	 * Get the <i>current</i> session rather than use {@link #getSession()} to avoid
	 * running out of connections. Likely caused FUR-1190.
	 * 
	 * @return Hibernate current session
	 * @see http://forum.springsource.org/archive/index.php/t-48676.html
	 * @see https://jira.chpc.utah.edu/browse/FUR-1190
	 */
	protected final Session getCurrentSession()
	{
		return getHibernateTemplate().getSessionFactory().getCurrentSession();
	}
}
