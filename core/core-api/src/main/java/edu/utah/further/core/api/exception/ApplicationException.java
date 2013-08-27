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

import java.util.Collections;
import java.util.List;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * A base class for all exceptions thrown by business classes. Contains an optional error
 * code. Originally sub-classed Spring's <code>NestedRuntimeException</code>, but to
 * minimize dependence on the Spring API we duplicate that code here; since it is short
 * and basic, not much reusability is lost.
 * <p>
 * TODO: replace telescoping c-tors by a builder.
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
public class ApplicationException extends RuntimeException implements HasErrorCode
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Error representation.
	 */
	private final ApplicationError error;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an exception with a message and a cause (wrap and re-throw the cause).
	 * 
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 * @param cause
	 *            cause of the exception
	 */
	public ApplicationException(final ErrorCode code, final String message,
			final Throwable cause)
	{
		super(message, cause);
		this.error = new ApplicationError(code, message);
	}

	/**
	 * Create an exception from an error object.
	 * 
	 * @param error
	 *            error object
	 */
	public ApplicationException(final ApplicationError error)
	{
		this(error.getCode(), error.getMessage(), null);
	}

	/**
	 * Create an exception with a message and a cause (wrap and re-throw the cause).
	 * 
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 * @param cause
	 *            cause of the exception
	 */
	public ApplicationException(final String message, final Throwable cause)
	{
		this(ErrorCode.INTERNAL_ERROR, message, cause);
	}

	/**
	 * Create an exception with a message without a cause.
	 * 
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 */
	public ApplicationException(final ErrorCode code, final String message)
	{
		this(code, message, null);
	}

	/**
	 * Create an exception with a message without a cause.
	 * 
	 * @param message
	 *            error message
	 */
	public ApplicationException(final String message)
	{
		this(message, null);
	}

	/**
	 * Propagate a message and code from an application exception cause.
	 * 
	 * @param cause
	 *            cause of the exception
	 */
	public ApplicationException(final ApplicationException cause)
	{
		this(cause.getCode(), cause.getMessage(), cause);
	}

	// ========================= IMPL: RuntimeException ====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.ws.ApplicationError.ApplicationError#getMessage()
	 */
	@Override
	public String getMessage()
	{
		return error.getMessage();
	}

	// ========================= METHODS ===================================

	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * 
	 * @return the innermost exception, or <code>null</code> if none
	 */
	public Throwable getRootCause()
	{
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause)
		{
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Retrieve the most specific cause of this exception, that is, either the innermost
	 * cause (root cause) or this exception itself.
	 * <p>
	 * Differs from {@link #getRootCause()} in that it falls back to the present exception
	 * if there is no root cause.
	 * 
	 * @return the most specific cause (never <code>null</code>)
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause()
	{
		final Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * Check whether this exception contains an exception of the given type: either it is
	 * of the given class itself or it contains a nested cause of the given type.
	 * 
	 * @param exType
	 *            the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	public boolean contains(final Class<?> exType)
	{
		if (exType == null)
		{
			return false;
		}
		if (exType.isInstance(this))
		{
			return true;
		}
		Throwable cause = getCause();
		if (cause == this)
		{
			return false;
		}
		// In original Spring code, all NestedRuntimeExceptions are covered instead. So
		// we lose a little here.
		if (ReflectionUtil.instanceOf(cause, ApplicationException.class))
		{
			return ((ApplicationException) cause).contains(exType);
		}
		while (cause != null)
		{
			if (exType.isInstance(cause))
			{
				return true;
			}
			if (cause.getCause() == cause)
			{
				break;
			}
			cause = cause.getCause();
		}
		return false;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the error property.
	 * 
	 * @return the error
	 */
	public ApplicationError getError()
	{
		return error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.exception.HasErrorCode#getCode()
	 */
	@Override
	public ErrorCode getCode()
	{
		return error.getCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.exception.ApplicationError#getArguments()
	 */
	public List<String> getArguments()
	{
		return Collections.unmodifiableList(error.getArguments());
	}

}
