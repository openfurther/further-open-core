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

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.replace;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import edu.utah.further.core.api.context.Implementation;

/**
 * Sends a test e-mail message to a user.
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
// @Service("mailService") // Need dependencies in Spring context file before it can
// be auto-detected
@Implementation
public class MailServiceImpl implements MailService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MailServiceImpl.class);

	/**
	 * Default subject for mail message.
	 */
	private static final String DEFAULT_SUBJECT = "Hard-coded subject value";

	/**
	 * Default body text for mail message.
	 */
	private static final String DEFAULT_TEXT = "Hard-coded text value for email %USER%.";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Mail service.
	 */
	@Autowired
	private MailSender mailSender;

	/**
	 * A basic mail message template for testing purposes. If not found, will use
	 * hard-coded values.
	 */
	@Autowired(required = false)
	@Qualifier("helloMailMessage")
	private SimpleMailMessage mailMessage;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: MailService ===============

	/**
	 * Send a test e-mail.
	 *
	 * @param recipient
	 *            recipient's e-mail address
	 */
	@Override
	public void sendTestMessage(final String recipient)
	{
		// Prepare message
		final SimpleMailMessage message = prepareMessage(recipient);

		// Send message using Spring
		mailSender.send(message);
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * Prepare a simple e-mail message to send using {@link MailSender}.
	 *
	 * @param recipient
	 *            recipient's e-mail address
	 * @return a simple e-mail message object
	 */
	private SimpleMailMessage prepareMessage(final String recipient)
	{
		// Create a message
		SimpleMailMessage message;
		if (mailMessage != null)
		{
			message = new SimpleMailMessage(mailMessage);
		}
		else
		{
			message = new SimpleMailMessage();
			message.setSubject(DEFAULT_SUBJECT);
			message.setText(DEFAULT_TEXT);
		}
		message.setTo(recipient);

		// Replace arguments by values
		final String args[] =
		{ "%USER%" };
		final String values[] =
		{ recipient };
		String text = message.getText();
		for (int i = 0; i < args.length; i++)
		{
			text = replace(text, args[i], values[i]);
		}
		message.setText(text);
		return message;
	}
}
