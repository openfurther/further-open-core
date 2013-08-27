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

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import edu.utah.further.core.api.context.Api;

/**
 * Supported Jasypt encryption types.
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
 * @version Nov 10, 2009
 */
@Api
public enum EncryptionType
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Fast yet basic encryption.
	 */
	BASIC
	{
		/**
		 * A factory method of text encryptors for this encryption type.
		 * 
		 * @param password
		 *            password to set for the encryptor
		 * @return text encryptor instance
		 * @see edu.utah.further.core.util.encrypt.EncryptionType#newTextEncryptor(java.lang.String)
		 */
		@Override
		public BasicTextEncryptor newTextEncryptor(final String password)
		{
			final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(password);
			return textEncryptor;
		}

		/**
		 * A factory method of password encryptors for this encryption type.
		 * 
		 * @return password encryptor instance
		 * @see edu.utah.further.core.util.encrypt.EncryptionType#newTextEncryptor(java.lang.String)
		 */
		@Override
		public BasicPasswordEncryptor newPasswordEncryptor()
		{
			return new BasicPasswordEncryptor();
		}
	},

	/**
	 * Strong encryption; more computationally expensive than {@link #BASIC}.
	 */
	STRONG
	{
		/**
		 * A factory method of text encryptors for this encryption type.
		 * 
		 * @param password
		 *            password to set for the encryptor
		 * @return text encryptor instance
		 * @see edu.utah.further.core.util.encrypt.EncryptionType#newTextEncryptor(java.lang.String)
		 */
		@Override
		public StrongTextEncryptor newTextEncryptor(final String password)
		{
			final StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(password);
			return textEncryptor;
		}

		/**
		 * A factory method of password encryptors for this encryption type.
		 * 
		 * @return password encryptor instance
		 * @see edu.utah.further.core.util.encrypt.EncryptionType#newTextEncryptor(java.lang.String)
		 */
		@Override
		public StrongPasswordEncryptor newPasswordEncryptor()
		{
			return new StrongPasswordEncryptor();
		}
	};

	// ========================= FACTORY METHODS ===========================

	/**
	 * A factory method of text encryptors.
	 * 
	 * @param password
	 *            password to set for the encryptor
	 * @return text encryptor instance
	 */
	public abstract TextEncryptor newTextEncryptor(String password);

	/**
	 * A factory method of password encryptors.
	 * 
	 * @return password encryptor instance
	 */
	public abstract PasswordEncryptor newPasswordEncryptor();
}
