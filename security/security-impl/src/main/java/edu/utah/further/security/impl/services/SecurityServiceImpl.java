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
package edu.utah.further.security.impl.services;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.core.api.scope.Namespaces;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.services.SecurityService;
import edu.utah.further.security.impl.domain.UserEntity;

/**
 * An implementation of general security service
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
 * @version May 8, 2012
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService
{

	/**
	 * SessionFactory for executing a query
	 */
	@Autowired
	@Qualifier("userSessionFactory")
	private SessionFactory sessionFactory;

	/**
	 * For resolving namespace identifiers
	 */
	@Autowired
	private NamespaceService namespaceService;

	/**
	 * The default namespace. Defaults to FURTHeR namespace identifier.
	 */
	private int defaultNamespace;

	@PostConstruct
	public void afterPropertiesSet()
	{
		// Defaults to FURTHeR namespace identifier.
		defaultNamespace = namespaceService.getNamespaceId(Namespaces.FURTHER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.SecurityService#findUserByFederatedUsername
	 * (java.lang.Long)
	 */
	@Override
	@Transactional
	public User findUserByFederatedUsername(final Long federatedUsername)
	{
		return (User) sessionFactory.getCurrentSession().get(UserEntity.class,
				federatedUsername);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.SecurityService#getFederatedUsernameBy(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public Long getFederatedUsernameByAlias(final String aliasName,
			final String aliasValue)
	{
		final Query query = sessionFactory.getCurrentSession().createQuery(
				"select u.id from UserEntity u join u.properties as props "
						+ "where props.property.name = :propertyName and "
						+ "props.property.namespace = :propertyNamespace and "
						+ "props.propertyValue = :propertyValue");
		query.setParameter("propertyNamespace", Long.valueOf(defaultNamespace));
		query.setParameter("propertyName", aliasName);
		query.setParameter("propertyValue", aliasValue);
		final Long federatedUsername = (Long) query.uniqueResult();
		return federatedUsername;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.SecurityService#getUsernameAlias(java.lang
	 * .Long, int)
	 */
	@Override
	@Transactional
	public String getUsernameAlias(final Long federatedUsername, final int namespaceId)
	{
		final Query query = sessionFactory.getCurrentSession().createQuery(
				"select props.propertyValue from UserEntity u join u.properties as props "
						+ "where props.property.name = :propertyName and "
						+ "props.property.namespace = :propertyNamespace and "
						+ "u.id = :federatedUsername");
		query.setParameter("propertyNamespace", Long.valueOf(namespaceId));
		query.setParameter("propertyName", "USERNAME");
		query.setParameter("federatedUsername", federatedUsername);
		final String usernameAlias = (String) query.uniqueResult();
		return usernameAlias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.SecurityService#userExists(java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean userExists(final Long federatedUsername)
	{
		final Query query = sessionFactory.getCurrentSession().createQuery(
				"select u.id from UserEntity u where u.id = :federatedUsername");
		query.setParameter("federatedUsername", federatedUsername);
		if (query.uniqueResult() != null)
		{
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.SecurityService#getUsernameAlias(java.lang
	 * .Long)
	 */
	@Override
	@Transactional
	public String getUsernameAlias(final Long federatedUsername)
	{
		return getUsernameAlias(federatedUsername, defaultNamespace);
	}

	/**
	 * Return the defaultNamespace property.
	 * 
	 * @return the defaultNamespace
	 */
	public int getDefaultNamespace()
	{
		return defaultNamespace;
	}

	/**
	 * Set a new value for the defaultNamespace property.
	 * 
	 * @param defaultNamespace
	 *            the defaultNamespace to set
	 */
	public void setDefaultNamespace(final int defaultNamespace)
	{
		this.defaultNamespace = defaultNamespace;
	}

	/**
	 * Set a new value for the sessionFactory property.
	 * 
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Return the namespaceService property.
	 * 
	 * @return the namespaceService
	 */
	public NamespaceService getNamespaceService()
	{
		return namespaceService;
	}

	/**
	 * Set a new value for the namespaceService property.
	 * 
	 * @param namespaceService
	 *            the namespaceService to set
	 */
	public void setNamespaceService(final NamespaceService namespaceService)
	{
		this.namespaceService = namespaceService;
	}

}
