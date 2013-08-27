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
package edu.utah.further.core.api.constant;

import edu.utah.further.core.api.context.Api;

/**
 * Centralizes error/exception codes throughout the entire FURTHeR code.
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
 * @version Nov 11, 2008
 */
@Api
public enum ErrorCode
{
	// ========================= 1XX CODES: EXECUTION ERRORS ===============

	/**
	 * The service encountered an internal error that is not classified under other
	 * service-specific codes.
	 */
	INTERNAL_ERROR(100),

	/**
	 * The server (e.g. Apelon DTS server) reported an execution error.
	 */
	SERVER_ERROR(101),

	/**
	 * The URL was not found on the server.
	 */
	URL_NOT_FOUND(102),

	/**
	 * HTTP transport error.
	 */
	TRANSPORT_ERROR(103),

	/**
	 * HTTP protocol violation.
	 */
	PROTOCOL_VIOLATION(104),

	/**
	 * Error related to mathematical computations.
	 */
	MATH_ERROR(105),

	/**
	 * Some input/output error.
	 */
	IO_ERROR(106),

	/**
	 * Some input/output error.
	 */
	RECOVERABLE_IO_ERROR(107, true),

	// ========================= 2XX CODES: PROGRAMMING ERRORS =============

	/**
	 * There was a missing input argument in the request. The specific argument name is
	 * often reported in the error message arguments.
	 */
	MISSING_INPUT_ARGUMENT(200),

	/**
	 * An input argument of an invalid type was passed in.
	 */
	INVALID_INPUT_ARGUMENT_TYPE(201),

	/**
	 * An input argument has an invalid value.
	 */
	INVALID_INPUT_ARGUMENT_VALUE(202),

	/**
	 * The request operation is not supported by the current code.
	 */
	UNSUPPORTED_OPERATION(203),

	// ========================= 3XX CODES: TERMINOLOGY SERVICE ERRORS =====

	/**
	 * Generic DTS call failure.
	 */
	DTS_ERROR(300),

	/**
	 * The requested concept was not found in DTS.
	 */
	RESOURCE_NOT_FOUND(301),

	/**
	 * Multiple concepts were found in DTS when a unique concept was expected.
	 */
	MULTIPLE_CONCEPTS_FOUND(302),

	/**
	 * The requested DTS object property was not found.
	 */
	PROPERTY_NOT_FOUND(303),

	/**
	 * An ontylog concept was found where a non-ontylog concept was expected, or vice
	 * versa. Can happen for instance when a DOS is invoked on a transfer object that does
	 * not support a business operation on that object.
	 */
	BAD_CONCEPT_TYPE(304),

	// ========================= 4XX CODES: METADATA REPOSITORY SERVICE ERRORS

	/**
	 * Generic DTS call failure.
	 */
	MDR_ERROR(401),
	
	// ========================= 5XX CODES: FQE SERVICE ERRORS =====
	
	/**
	 * The ExportContext is not valid
	 */
	INVALID_EXPORT_CONTEXT(500),
	
	/**
	 * The query doesn't exist 
	 */
	QUERY_NOT_EXIST(501),
	
	/**
	 * The results of the query are not valid
	 */
	INVALID_RESULTS(502);


	// ========================= FIELDS =======================================

	/**
	 * A unique identifying integer of the error code.
	 */
	private final int serialNumber;

	/**
	 * Is this is a recoverable, benign error or not.
	 */
	private final boolean recoverable;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Create an enumerated error code type.
	 *
	 * @param serialNumber
	 *            error code's unique identifier
	 */
	private ErrorCode(final int serialNumber)
	{
		this(serialNumber, false);
	}

	/**
	 * Create an enumerated e rror code type.
	 * @param serialNumber  error code's unique identifier
	 * @param recoverable is error recoverable
	 */
	private ErrorCode(final int serialNumber, final boolean recoverable)
	{
		this.serialNumber = serialNumber;
		this.recoverable = recoverable;
	}

	// ========================= METHODS ======================================

	// ========================= METHODS ======================================

	/**
	 * Return the serialNumber property.
	 *
	 * @return the serialNumber
	 */
	public int getSerialNumber()
	{
		return serialNumber;
	}

	/**
	 * Return the recoverable property.
	 *
	 * @return the recoverable
	 */
	public boolean isRecoverable()
	{
		return recoverable;
	}

}
