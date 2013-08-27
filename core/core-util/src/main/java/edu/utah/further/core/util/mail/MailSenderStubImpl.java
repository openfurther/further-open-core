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

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import edu.utah.further.core.api.context.Stub;

/**
 * A stub implementation of an e-mail sending service.
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
@Qualifier("mailSender")
@Stub
public class MailSenderStubImpl implements MailSender
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MailServiceImpl.class);

	// ========================= IMPLEMENTATION: MailService ===============

	/**
	 * @param simpleMessage
	 * @throws MailException
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.MailMessage)
	 */
	@Override
	public void send(final SimpleMailMessage simpleMessage) throws MailException
	{
		log.debug("send(" + simpleMessage + ")");
	}

	/**
	 * @param simpleMessages
	 * @throws MailException
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.MailMessage[])
	 */
	@Override
	public void send(final SimpleMailMessage[] simpleMessages) throws MailException
	{
		log.debug("send(" + simpleMessages + ")");
	}

}
