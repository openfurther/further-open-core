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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Signals that an I/O, transport or business exception of some sort has occurred in a web
 * service. Must be a checked exception to work with CXF exception mappers.
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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.CORE_WS, name = WsException.ENTITY_NAME, propOrder =
{ "error" })
@XmlRootElement(namespace = XmlNamespace.CORE_WS, name = WsException.ENTITY_NAME)
public final class WsException extends Exception implements HasErrorCode
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "wsException";

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default error to use if a <code>null</code> {@link Error} is fed into a
	 * {@link WsException} constructor.
	 */
	private static final String UNKNOWN_ERROR = "Unknown error";

	// ========================= FIELDS ====================================

	/**
	 * Error representation.
	 */
	@Final
	@XmlElement(name = "error", required = true)
	private ApplicationError error;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Used for XML marshallers only. DO NOT CALL IN NORMAL CLIENT CODE!
	 */
	public WsException()
	{
		this(null, null, null);
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
	public WsException(final ErrorCode code, final String message, final Throwable cause)
	{
		super(message, cause);
		this.error = new ApplicationError(code, getMessagePrefix(code) + message);
	}

	/**
	 * Create an exception from an error object.
	 *
	 * @param error
	 *            error object
	 */
	public WsException(final ApplicationError error)
	{
		this(getCodeNullSafe(error),
				(error == null) ? UNKNOWN_ERROR : error.getMessage(), null);
	}

	/**
	 * Create an exception with a message without a cause.
	 *
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 */
	public WsException(final ErrorCode code, final String message)
	{
		this(code, message, null);
	}

	/**
	 * Create an exception with a message without a cause.
	 *
	 * @param message
	 *            error message
	 */
	public WsException(final String message)
	{
		this(ErrorCode.INTERNAL_ERROR, message, null);
	}

	/**
	 * Propagate a message and code from an application exception cause.
	 *
	 * @param cause
	 *            cause of the exception
	 */
	public WsException(final ApplicationException cause)
	{
		this(getCodeNullSafe(cause), cause.getMessage(), cause);
	}

	/**
	 * Propagate a message and code from a web service exception cause.
	 *
	 * @param cause
	 *            cause of the exception
	 */
	public WsException(final WsException cause)
	{
		this(getCodeNullSafe(cause), cause.getMessage(), cause);
	}

	/**
	 * Return the substring that a {@link WsException}'s message string starts with.
	 * Useful for test assertions.
	 *
	 * @param code
	 *            exception's error code
	 * @return corresponding exception's message prefix
	 */
	public static String getMessagePrefix(final ErrorCode code)
	{
		return (code == null) ? Strings.EMPTY_STRING : "Error " + code.getSerialNumber()
				+ ": ";
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

	/**
	 * DO NOT CALL - FOR XML MARSHALLERS ONLY.
	 * <p>
	 * Set a new value for the wsError property.
	 *
	 * @param error
	 *            the error to set
	 */
	public void setError(final ApplicationError error)
	{
		this.error = error;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.ws.ApplicationError.ApplicationError#getMessage()
	 */
	@Override
	public String getMessage()
	{
		return error.getMessage();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.exception.ApplicationException#getCode()
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

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param cause
	 * @return
	 */
	private static ErrorCode getCodeNullSafe(final HasErrorCode cause)
	{
		return ((cause == null) || (cause.getCode() == null)) ? ErrorCode.INTERNAL_ERROR
				: cause.getCode();
	}
}
