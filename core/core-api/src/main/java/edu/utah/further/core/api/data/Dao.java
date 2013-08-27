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
package edu.utah.further.core.api.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * An abstract generic DAO pattern. Should be extended by concrete DAOs for domain model
 * classes. Depends on both the domain entity type (T) and its identifier type (ID).
 * <p>
 * To this end, I don't know how to wire generic beans in Spring. Hence, the interface has
 * been made non-generic, and all methods are generic instead.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz {@code
 *         <dustin.schultz@utah.edu>}
 * 
 * @version Nov 20, 2009
 */
@Api
public interface Dao
{
	// ========================= CONSTANTS ==============================

	/**
	 * The default named parameters list to use when calling named queries.
	 */
	static final String DEFAULT_NAMED_PARAM_LIST = "parameters";

	// ========================= CRUD METHODS ==============================

	/**
	 * Persist an object to the database. This is useful for transient entities with
	 * embedded IDs that can be non-<code>null</code>.
	 * 
	 * @param domain
	 *            a transient object
	 */
	<T extends PersistentEntity<?>> void create(T domain);

	/**
	 * Persists an object to the database and returns the identifier.
	 * 
	 * @param domain
	 *            a transient object
	 * @return the generated identifier
	 */
	<T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> ID save(
			T domain);

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
	 * @param entity
	 *            entity to be read
	 * @param id
	 *            unique identifier to search for and load by
	 * @return loaded persistent entity
	 */
	<T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> void read(
			T entity, ID id);

	/**
	 * Performs a persistent (if this is a transient object) or merge (if this is a
	 * persistent object) operation.
	 * 
	 * @param domain
	 *            a detached instance with state to be persisted/updated in the database
	 */
	<T extends PersistentEntity<?>> void update(T domain);

	/**
	 * Performs a merge operation.
	 * 
	 * @param arg0
	 *            object to merge
	 * @see http://www.hibernate.org/hib_docs/v3/reference/en/html_single/#objectstate-
	 *      saveorupdate
	 */
	<T extends PersistentEntity<?>> void merge(T domain);

	/**
	 * Re-read the state of the given persistent instance.
	 * 
	 * @param domain
	 *            the persistent instance to re-read
	 */
	<T extends PersistentEntity<?>> void refresh(T domain);

	/**
	 * Delete domain entity from database.
	 * 
	 * @param domain
	 *            domain entity to be deleted
	 */
	<T extends PersistentEntity<?>> void delete(T domain);

	/**
	 * Delete all instances of the domain entity from database using a query. Does not
	 * seem to respect cascading operations, but could be faster than
	 * {@link #deleteAll(Class)}.
	 * 
	 * @param domain
	 *            domain entity class
	 * @see edu.utah.further.core.api.data.Dao#deleteAll(java.lang.Class)
	 */
	<T extends PersistentEntity<?>> void deleteAllUsingQuery(Class<T> domainClass);

	/**
	 * Delete all instances of the domain entity from database.
	 * 
	 * @param domainClass
	 *            domain entity class
	 */
	<T extends PersistentEntity<?>> void deleteAll(Class<T> domainClass);

	/**
	 * Remove a domain entity from the current session.
	 * 
	 * @param domain
	 *            domain entity to be removed from the current session
	 */
	<T extends PersistentEntity<?>> void evict(T domain);

	// ========================= FINDER METHODS ============================

	/**
	 * Find an domain entity by a unique identifier.
	 * 
	 * @param id
	 *            a unique identifier to look for
	 * @return persistent domain entity, if found. If not found, returns <code>null</code>
	 */
	<T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> T findById(
			Class<T> domainClass, ID id);

	/**
	 * Find all entity of this type.
	 * 
	 * @return a list of all entity of this type
	 */
	<T extends PersistentEntity<?>> List<T> findAll(Class<T> domainClass);

	/**
	 * Find all entity of this type within a given range
	 * 
	 * @param the
	 *            starting record
	 * @param the
	 *            max records to retrieve
	 * @return a list of all entity of this type
	 */
	<T extends PersistentEntity<?>> List<T> findAll(Class<T> domainClass, int start,
			int max);

	/**
	 * Find by an example entity. Some properties are excluded if the appropriate flags
	 * and arguments are specified.
	 * 
	 * @param exampleInstance
	 *            an example entity to search for
	 * @param excludeZeros
	 *            if <code>true</code>, zero-valued properties will be excluded
	 * @param excludeProperty
	 *            property names to exclude
	 * @return list of items identical to exampleInstance
	 */
	<T extends PersistentEntity<?>> List<T> findByExample(T exampleInstance,
			boolean excludeZeros, String... excludeProperty);

	/**
	 * Find an item by property.
	 * 
	 * @param propertyName
	 *            property name
	 * @param value
	 *            property value
	 * @return list of items that have this property value
	 */
	<T extends PersistentEntity<?>> List<T> findByProperty(Class<T> domainClass,
			String propertyName, Object value);

	/**
	 * Find an item by property for which the returned entities have a property which is
	 * greater than the specified value.
	 * 
	 * @param propertyName
	 *            property name
	 * @param value
	 *            property value
	 * @return list of items that have this property value
	 */
	<T extends PersistentEntity<?>> List<T> findByPropertyGt(Class<T> domainClass,
			String propertyName, Object value);

	/**
	 * Find an item by property for which the returned entities have a property which is
	 * greater than the specified value.
	 * 
	 * @param propertyName
	 *            property name
	 * @param value
	 *            property value
	 * @return list of items that have this property value
	 */
	<T extends PersistentEntity<?>> List<T> findByPropertyLt(Class<T> domainClass,
			String propertyName, Object value);

	/**
	 * Find an item by property (case-insensitive value).
	 * 
	 * @param propertyName
	 *            property name
	 * @param value
	 *            case-insensitive property value
	 * @return list of items that have this property value
	 */
	<T extends PersistentEntity<?>> List<T> findByLikeProperty(Class<T> domainClass,
			String propertyName, Object value);

	/**
	 * Find an item by unique property.
	 * 
	 * @param propertyName
	 *            property name
	 * @param uniqueValue
	 *            property value; must be unique per entity
	 * @return unique item that has this property value
	 */
	<T extends PersistentEntity<?>> T findByUniqueProperty(Class<T> domainClass,
			String propertyName, Object uniqueValue);

	/**
	 * Find by multiple properties. Returns the list of entities that match ALL of the
	 * property name-value pairs.
	 * 
	 * @param <T>
	 * @param domainClass
	 *            the persistent domain class or interface
	 * @param properties
	 *            properties to match (in each entity, the value of the property named
	 *            according to the map entry key must match the entry's value)
	 * @return list of matches
	 */
	<T extends PersistentEntity<?>> List<T> findByProperties(Class<T> domainClass,
			Map<String, Object> properties);

	/**
	 * Find by a unique example entity. Zeroed properties are excluded.
	 * 
	 * @param exampleInstance
	 *            an example entity to search for
	 * @return unique item that has this property value
	 */
	<T extends PersistentEntity<?>> T findByUniqueExample(T exampleInstance);

	/**
	 * Get a <code>PersistentEntity</code> by its ID.
	 * 
	 * @param domainClass
	 *            the persistent domain class or interface
	 * @param id
	 *            The id of the domain object to load
	 * 
	 * @return The domain object that corresponds to the provided id. A null object will
	 *         be returned if the PersistentEntity object of id does not exist.
	 */
	<T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> T getById(
			Class<T> domainClass, ID id);

	// ========================= UTILITIES =================================

	/**
	 * Return the number of persistent objects of this type.
	 * 
	 * @param domainClass
	 *            persistent object type
	 * @return the number of persistent objects of this type
	 */
	<T extends PersistentEntity<?>> Long count(Class<T> domainClass);

	/**
	 * Execute a named query binding any parameters to parameter list 'parameters' (i.e.
	 * call foo(:parameters)). This is a convenience method supporting 'convention over
	 * configuration'
	 * 
	 * @param namedQuery
	 *            the name of the Named Query to execute
	 * @param parameters
	 *            parameters to pass to the named query in order of parameters expected by
	 *            procedure or function
	 * @return the results of the named query
	 */
	<T extends PersistentEntity<?>> List<T> executeNamedQuery(String namedQuery,
			Object... parameters);

	/**
	 * Execute a named query binding any parameters
	 * 
	 * @param namedQuery
	 *            the name of the Named Query to execute
	 * @param paramBindName
	 *            the argument name to bind the parameters too (i.e. call foo(:parameters)
	 *            -- parameters is paramBindName).
	 * @param parameters
	 *            parameters to pass to the named query in order of parameters expected by
	 *            procedure or function
	 * @return the results of the named query
	 */
	<T extends PersistentEntity<?>> List<T> executeNamedQuery(String namedQuery,
			String paramBindName, Object... parameters);

	// ========================= METHODS ===================================

	/**
	 * Flush the current persistent session. Useful for testing.
	 */
	void flush();

	/**
	 * Close the current session.
	 */
	void close();

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
	Serializable getIdentifier(PersistentEntity<?> domainObject);

	/**
	 * Return the entity name for a persistent entity. If the entity is <code>null</code>,
	 * returns <code>null</code>. If the entity is transient, returns the class name of
	 * the entity as a POJO.
	 *
	 * @param domainObject
	 *            a persistent entity
	 * @return the entity name
	 */
	String getEntityName(PersistentEntity<?> domainObject);

	/**
	 * Return the list of persistent classes registered with the session factory.
	 * 
	 * @return the list of persistent classes
	 */
	Set<Class<? extends PersistentEntity<?>>> getEntityClasses();

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
	 */
	<T extends PersistentEntity<?>> Class<? extends T> getEntityClass(Class<T> baseClass);
}
