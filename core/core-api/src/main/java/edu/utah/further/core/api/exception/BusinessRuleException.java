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
package edu.utah.further.core.api.exception;

import static edu.utah.further.core.api.message.Severity.ERROR;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.message.Severity;

/**
 * A dedicated exception to be thrown from business services upon illegal arguments,
 * violating business rules, etc.
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
public final class BusinessRuleException extends ApplicationException
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1775051650361375005L;

	// ========================= FIELDS ====================================

	/**
	 * Error severity.
	 */
	private final Severity severity;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an exception with a message and a cause (wrap and re-throw the cause).
	 *
	 * @param message
	 *            message to be displayed
	 * @param cause
	 *            cause of the exception
	 * @param error
	 *            severity
	 */
	public BusinessRuleException(final Severity severity, final String message,
			final Throwable cause)
	{
		super(message, cause);
		this.severity = severity;
	}

	/**
	 * Create an exception with a message.
	 *
	 * @param message
	 *            message to be displayed
	 */
	public BusinessRuleException(final Severity severity, final String message)
	{
		super(message);
		this.severity = severity;
	}

	/**
	 * Create an exception with a message and a cause (wrap and re-throw the cause).
	 *
	 * @param message
	 *            message to be displayed
	 * @param cause
	 *            cause of the exception
	 */
	public BusinessRuleException(final String message, final Throwable cause)
	{
		this(ERROR, message, cause);
	}

	/**
	 * Create an exception with a message.
	 *
	 * @param message
	 *            message to be displayed
	 */
	public BusinessRuleException(final String message)
	{
		this(ERROR, message);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the severity property.
	 *
	 * @return the severity
	 */
	public Severity getSeverity()
	{
		return severity;
	}
}
