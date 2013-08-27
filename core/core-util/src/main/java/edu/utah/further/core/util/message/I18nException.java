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
package edu.utah.further.core.util.message;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A base class for exceptions with internationalizable messages.
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
 * @version Oct 13, 2008
 */
@Api
public abstract class I18nException extends ApplicationException
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = -1964511891508466498L;

	// ========================= DEPENDENCIES ==============================

	/**
	 * i18n error message.
	 */
	private final MessageSourceResolvable i18nMessage;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an exception with the specified detail message and nested exception.
	 * 
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the nested exception
	 */
	public I18nException(final MessageSourceResolvable message)
	{
		super(message.getCodes()[0]);
		this.i18nMessage = message;
	}

	/**
	 * Construct an exception with the specified detail message and nested exception.
	 * 
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the nested exception
	 */
	public I18nException(final Enum<?> code, final Object... args)
	{
		super(code.name());
		this.i18nMessage = new DefaultMessageSourceResolvable(
				new String[] { code.name() }, args);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the i18nMessage property.
	 * 
	 * @return the i18nMessage
	 */
	public MessageSourceResolvable getI18nMessage()
	{
		return i18nMessage;
	}

	// ---------- may later need to implement some Spring resolvable interface
	// to auto-i18n the toString() method of this exception ----
	//
	// // ========================= DEPENDENCY INJECTION ======================
	//
	// /**
	// * Set a new value for the messageSource property.
	// *
	// * @param messageSource
	// * the messageSource to set
	// */
	// public void setMessageSource(MessageSource messageSource)
	// {
	// this.messageSource = messageSource;
	// }

}
