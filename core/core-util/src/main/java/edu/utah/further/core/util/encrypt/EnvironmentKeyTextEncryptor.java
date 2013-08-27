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
package edu.utah.further.core.util.encrypt;

import static org.slf4j.LoggerFactory.getLogger;

import org.jasypt.util.text.TextEncryptor;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;

import edu.utah.further.core.api.text.StringUtil;

/**
 * A text encryption service whose key is taken from an environment variable. Delegates to
 * a Jaspyt service.
 * <p>
 * Note: encrypting the same text with the same key twice will NOT produce the same
 * digest.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 */
// @Service("environmentKeyTextEncryptor")
public class EnvironmentKeyTextEncryptor implements TextEncryptor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(EnvironmentKeyTextEncryptor.class);

	// ========================= FIELDS =====================================

	/**
	 * Underlying Jasypt service to delegate to.
	 */
	private final TextEncryptor textEncryptor;

	// ========================= DEPENDENCIES ===============================

	// /**
	// * Type of underlying Jasypt encryption to use.
	// */
	// private final EncryptionType encryptionType;
	//
	// /**
	// * Name of environment variable to read key from and set on {@link #textEncryptor}.
	// */
	// private final String keyEnvironmentVariable;

	// ========================= CONSTRCUTORS ===============================

	/**
	 * Create an environment-variable-key-based text encryption service.
	 * 
	 * @param encryptionType
	 *            Type of underlying Jasypt encryption to use.
	 * @param keyEnvironmentVariable
	 *            Name of environment variable to read key from and set on
	 *            {@link #textEncryptor}
	 */
	public EnvironmentKeyTextEncryptor(final EncryptionType encryptionType,
			final String keyEnvironmentVariable)
	{
		super();
		// this.encryptionType = encryptionType;
		// this.keyEnvironmentVariable = keyEnvironmentVariable;
		final String password = System.getenv(keyEnvironmentVariable);
		if (password == null)
		{
			throw new BeanInitializationException("Encryption key environment "
					+ StringUtil.quote(keyEnvironmentVariable) + " must be set");
		}
		if (log.isDebugEnabled())
		{
			log.debug("Using master password " + password);
		}
		this.textEncryptor = encryptionType.newTextEncryptor(password);
	}

	// ========================= IMPLEMENTATION: TextEncryptor ==============

	/**
	 * @param encryptedMessage
	 * @return
	 * @see org.jasypt.util.text.TextEncryptor#decrypt(java.lang.String)
	 */
	@Override
	public String decrypt(final String encryptedMessage)
	{
		return textEncryptor.decrypt(encryptedMessage);
	}

	/**
	 * @param message
	 * @return
	 * @see org.jasypt.util.text.TextEncryptor#encrypt(java.lang.String)
	 */
	@Override
	public String encrypt(final String message)
	{
		return textEncryptor.encrypt(message);
	}
}
