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
package edu.utah.further.security.impl.ldap;

import org.springframework.stereotype.Service;

import edu.utah.further.security.api.UserDetailsBuilder;
import edu.utah.further.security.api.UserDetailsFactory;

/**
 * A {@link UserDetailsFactory} factory implementation for the University of Utah's LDAP
 * provider.
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
 * @version May 25, 2010
 */
// TODO: consider adding @Service annotation here in the future for simple spring context
// deployment via annotation scanning
@Service("userDetailsFactoryUuLdap")
public class UserDetailsFactoryUuLdapImpl implements UserDetailsFactory
{
	// ========================= IMPL: UserDetailsFactory ==================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.security.impl.UserDetailsFactory#newBuilder()
	 */
	@Override
	public UserDetailsBuilder newBuilder()
	{
		return new UuLdapUserDetailsImpl.Builder();
	}
}
