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
package edu.utah.further.security.impl.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.core.api.scope.Namespaces;
import edu.utah.further.security.api.domain.Property;
import edu.utah.further.security.api.domain.Role;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserProperty;
import edu.utah.further.security.api.domain.UserRole;
import edu.utah.further.security.api.services.SecurityService;
import edu.utah.further.security.impl.domain.PropertyEntity;
import edu.utah.further.security.impl.domain.RoleEntity;
import edu.utah.further.security.impl.domain.UserEntity;
import edu.utah.further.security.impl.domain.UserPropertyEntity;
import edu.utah.further.security.impl.domain.UserRoleEntity;
import edu.utah.further.security.impl.services.SecurityServiceImpl;

/**
 * Test for the general implementation of the {@link SecurityServiceImpl}
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/security-impl-test-context-annotation.xml",
		"/security-impl-test-context-datasource.xml" })
public class UTestSecurityServiceImpl
{
	/**
	 * Dao for saving a new user for testing
	 */
	@Autowired
	private Dao dao;

	/**
	 * Security Service to test
	 */
	@Autowired
	private SecurityService securityService;

	@Autowired
	private NamespaceService namespaceService;

	/**
	 * A reference to the saved federated username after setup has run.
	 */
	private Long savedFederatedUsername;

	/**
	 * The username alias to test with
	 */
	private final String usernameAlias = "u0405293";

	/**
	 * Setup a user in the DB
	 */
	@Before
	public void setup()
	{
		final User user = new UserEntity();
		user.setFirstname("Dustin");
		user.setLastname("Schultz");
		user.setCreatedBy(user);
		user.setCreatedDate(new Date());
		user.setEmail("awesome@awesome.com");
		user.setExpireDate(new Date());

		final Property furtherProp = new PropertyEntity();
		furtherProp.setName("USERNAME");
		furtherProp.setNamespace(new Long(namespaceService
				.getNamespaceId(Namespaces.FURTHER)));
		furtherProp.setDescription("University of Utah Network Id");

		final UserProperty furtherUserProperty = new UserPropertyEntity();
		furtherUserProperty.setProperty(furtherProp);
		furtherUserProperty.setPropertyValue(usernameAlias);
		furtherUserProperty.setUser(user);

		final Collection<UserProperty> properties = CollectionUtil.newList();
		properties.add(furtherUserProperty);
		user.setProperties(properties);

		final Role role = new RoleEntity();
		role.setName("further");
		role.setDescription("FURTHeR users");

		final UserRole userRole = new UserRoleEntity();
		userRole.setCreatedDate(new Date());
		userRole.setRole(role);
		userRole.setUser(user);

		final Collection<UserRole> roles = CollectionUtil.newList();
		roles.add(userRole);
		user.setRoles(roles);

		savedFederatedUsername = dao.save(user);
	}

	@After
	public void after()
	{
		dao.deleteAll(UserEntity.class);
	}

	@Test
	@Rollback
	public void findUserByFederatedUsername()
	{
		final User user = securityService
				.findUserByFederatedUsername(savedFederatedUsername);
		assertNotNull(user);
		assertThat(user.getFirstname(), is("Dustin"));
	}

	/**
	 * Returns the federated username by an alias
	 */
	@Test
	@Rollback
	public void getFederatedUsernameByAlias()
	{
		final Long federatedUsername = securityService.getFederatedUsernameByAlias(
				"USERNAME", "u0405293");
		assertThat(federatedUsername, is(savedFederatedUsername));
	}

	/**
	 * Returns an alias for the federated username
	 */
	@Test
	public void getAliasByFederatedUsername()
	{
		final String username = securityService.getUsernameAlias(savedFederatedUsername);
		assertThat(username, is(usernameAlias));
	}

	/**
	 * Test the a user exists
	 */
	@Test
	public void userExists()
	{
		assertTrue(securityService.userExists(savedFederatedUsername));
	}

	/**
	 * Test that a user doesn't exist
	 */
	@Test
	public void userDoesNotExist()
	{
		assertFalse(securityService.userExists(new Long(-1L)));
	}
}
