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
package edu.utah.further.core.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import edu.utah.further.core.api.context.Implementation;

/**
 * A simple SMTP authenticator for e-mail configurations that require authentication.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jan 7, 2009
 */
// @Service("smtpAuthenticationService")// Need dependencies in Spring context file before
// it can be auto-detected
@Implementation
public class SmtpAuthenticationServiceImpl extends Authenticator implements
		MailAuthenticator
{
	// ========================= FIELDS ====================================

	/**
	 * User-name to log into the SMTP server.
	 */
	private final String username;

	/**
	 * Password to log into the SMTP server.
	 */
	private final String password;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param username
	 * @param password
	 */
	public SmtpAuthenticationServiceImpl(final String username, final String password)
	{
		super();
		this.username = username;
		this.password = password;
	}

	// ========================= IMPLEMENTATION: Authenticator =============

	/**
	 * @return
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	@Override
	public PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(username, password);
	}
}
