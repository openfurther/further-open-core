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

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.core.api.scope.Namespaces;
import edu.utah.further.security.api.EnhancedUserDetails;
import edu.utah.further.security.api.authentication.FederatedAuthenticationToken;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserProperty;
import edu.utah.further.security.api.domain.UserRole;
import edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService;
import edu.utah.further.security.impl.domain.DefaultUserDetailsImpl;

/**
 * An implementation of a {@link ContextualAuthenticationUserDetailsService} which
 * utilizes a data source id as its means for determining the context in which the
 * username should be loaded. The default data source id is "FURTHeR"
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
 * @version Apr 30, 2012
 */
@Service("userDetailsService")
public class DatasourceAuthenticatedUserDetailsService implements
		ContextualAuthenticationUserDetailsService<Integer>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DatasourceAuthenticatedUserDetailsService.class);

	// ========================= FIELDS =================================

	/**
	 * The data source identifier to load the username by
	 */
	private int dataSourceId;

	/**
	 * Hibernate {@link SessionFactory} to query
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
	 * The role prefix to attach to role names.
	 */
	private final String rolePrefix = "";

	@PostConstruct
	public void afterPropertiesSet()
	{
		// Set the default data source ID to FURTHeR
		this.dataSourceId = namespaceService.getNamespaceId(Namespaces.FURTHER);
	}

	// =================== Impl: UserDetailsService ==============

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.AuthenticationUserDetailsService#
	 * loadUserDetails(org.springframework.security.core.Authentication)
	 */
	@Override
	@Transactional
	public EnhancedUserDetails loadUserDetails(final Authentication token)
			throws UsernameNotFoundException
	{
		Validate.isTrue(
				FederatedAuthenticationToken.class.isAssignableFrom(token.getClass()),
				"Only FederatedAuthenticationTokens are supported");

		Validate.notNull(getContext(), "A context is required to load the user details");

		// Load the user details based on the datasource at hand
		Long federatedUsername = null;
		try
		{
			federatedUsername = Long.valueOf(token.getName());
		}
		catch (final Exception e)
		{
			throw new ApplicationException("Expected a federated username but received "
					+ token.getName());
		}

		final Query query = sessionFactory.getCurrentSession().createQuery(
				"from UserEntity where id = :federatedUsername");
		query.setParameter("federatedUsername", federatedUsername);
		final User user = (User) query.uniqueResult();

		if (user == null)
		{
			throw new UsernameNotFoundException("User " + federatedUsername
					+ " does not exist.");
		}

		final DefaultUserDetailsImpl userDetails = new DefaultUserDetailsImpl();

		// Load the roles
		final Collection<UserRole> roles = user.getRoles();
		final Collection<GrantedAuthority> authorities = CollectionUtil.newList();
		if (roles != null && roles.size() > 0)
		{
			for (final UserRole role : roles)
			{
				authorities.add(new GrantedAuthorityImpl(rolePrefix
						+ role.getRole().getName()));
			}

			userDetails.setAuthorities(authorities);
		}

		// Load the properties
		final Collection<UserProperty> properties = user.getProperties();
		final Map<String, String> userProperties = newMap();
		if (properties != null && properties.size() > 0)
		{
			for (final UserProperty property : properties)
			{
				// Only populate the user details with properties from this namespace
				if (property.getProperty().getNamespace().intValue() == getContext()
						.intValue())
				{
					userProperties.put(property.getProperty().getName(),
							property.getPropertyValue());
				}
			}

			userDetails.setProperties(userProperties);
		}

		userDetails.setUsername(String.valueOf(federatedUsername));
		userDetails.setEnabled(true);
		userDetails.setCredentialsNonExpired(true);
		userDetails.setAccountNonExpired(true);
		userDetails.setAccountNonLocked(true);

		return userDetails;
	}

	// =================== Impl: ContextualUserDetailsService ==============

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService
	 * #setContext(java.lang.Object)
	 */
	@Override
	public void setContext(final Integer context)
	{
		this.dataSourceId = context.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.ContextualAuthenticationUserDetailsService
	 * #getContext()
	 */
	@Override
	public Integer getContext()
	{
		return new Integer(dataSourceId);
	}

	// =================== GET/SET ==========================================

	/**
	 * Return the sessionFactory property.
	 * 
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
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
