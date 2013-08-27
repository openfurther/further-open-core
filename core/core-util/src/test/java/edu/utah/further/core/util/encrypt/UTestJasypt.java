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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;

import org.jasypt.util.password.PasswordEncryptor;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.util.fixture.CoreUtilFixture;

/**
 * Test the Java Simplified Encryption (Jasypt) library.
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
 * @version Sep 11, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestJasypt extends CoreUtilFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestJasypt.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Encryption service to test.
	 */
	@Autowired
	@Qualifier("environmentKeyTextEncryptor")
	private EnvironmentKeyTextEncryptor encryptor;

	/**
	 * Encryption service to test.
	 */
	private final PasswordEncryptor passwordEncryptor = EncryptionType.STRONG
			.newPasswordEncryptor();

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test that the encryption service encrypts and decrypts properly. The master
	 * password environment variable should be set, but not necessarily to the correct
	 * FURTHeR master password.
	 */
	@Test
	public void textEncryption() throws Throwable
	{
		try
		{
			final String input = randomUtilService.randomWord(10, 10);
			final String encrypted = encryptor.encrypt(input);
			final String recovered = encryptor.decrypt(encrypted);
			if (log.isDebugEnabled())
			{
				log.debug("Original : " + input);
				log.debug("Encrypted: " + encrypted);
				log.debug("Recovered: " + recovered);
			}
			assertEquals("Failed to encrypt and recover string", input, recovered);
		}
		catch (final Throwable t)
		{
			fail("Text encryption failed. Make sure that you have the "
					+ "unlimited Java Cryptography Extension installed.");
			handleAssertionError(t);
		}
	}

	/**
	 * Test password encryption.
	 */
	@Test
	public void passwordEncryption() throws Throwable
	{
		try
		{
			final String password = "password";
			final String encryptedPassword = passwordEncryptor.encryptPassword(password);
			assertTrue("Correct password is not verified to be correct",
					passwordEncryptor.checkPassword(password, encryptedPassword));
			assertFalse("Wrong password is verified to be correct",
					passwordEncryptor.checkPassword("wrong", encryptedPassword));
		}
		catch (final Throwable t)
		{
			fail("Password encryption failed. Make sure that you have the "
					+ "unlimited Java Cryptography Extension installed.");
			handleAssertionError(t);

		}
	}

	/**
	 * Check that the value of your key environment is the correct FURTHeR master
	 * password. Testing using a pre-cooked digest.
	 */
	// Use PbeStringEncryptor instead of deterministic results
	@Ignore
	// @Test
	public void checkYourMasterPassword()
	{
		try
		{
			final String input = "plichntdnv";
			final String encrypted = encryptor.encrypt(input);
			final String expectedEncrypted = "/3KHQDcDypjy6VYSFRz+vkHQMg0DAooh";
			if (log.isDebugEnabled())
			{
				log.debug("Original : " + input);
				log.debug("Encrypted: " + encrypted);
				log.debug("Expected : " + expectedEncrypted);
			}
			assertEquals("Wrong master password", expectedEncrypted, encrypted);
		}
		catch (final Throwable t)
		{
			fail("Encryption with master password failed. Make sure that you have the "
					+ "unlimited Java Cryptography Extension installed and you have set "
					+ "the FURTHER_PASSWORD environment variable.");
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Handles AssertionErrors so they are propagated if the test fails for that reason
	 * 
	 * @param t
	 * @throws Throwable
	 */
	private void handleAssertionError(final Throwable t) throws Throwable
	{
		if (ReflectionUtil.instanceOf(t, AssertionError.class))
		{
			throw t;
		}
	}
}