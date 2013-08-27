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
package edu.utah.further.security.impl.domain;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.security.api.domain.Property;
import edu.utah.further.security.api.domain.Role;
import edu.utah.further.security.api.domain.User;
import edu.utah.further.security.api.domain.UserProperty;
import edu.utah.further.security.api.domain.UserRole;

/**
 * Test persisting the security entites
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
 * @version May 4, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/security-impl-test-context-annotation.xml",
		"/security-impl-test-context-datasource.xml" })
public class UTestPersistSecurityModel
{
	/**
	 * Data access object for saving entities
	 */
	@Autowired
	private Dao dao;

	/**
	 * Test saving a user to an in memory database
	 */
	@Test
	public void save()
	{
		final User user = new UserEntity();
		user.setFirstname("Dustin");
		user.setLastname("Schultz");
		user.setCreatedBy(user);
		user.setCreatedDate(new Date());
		user.setEmail("awesome@awesome.com");
		user.setExpireDate(new Date());

		final Property property = new PropertyEntity();
		property.setName("unid");
		property.setDescription("University of Utah Network Id");

		final UserProperty userProperty = new UserPropertyEntity();
		userProperty.setProperty(property);
		userProperty.setPropertyValue("u0405293");
		userProperty.setUser(user);

		final Collection<UserProperty> properties = CollectionUtil.newList();
		properties.add(userProperty);
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

		dao.save(user);
	}
}
