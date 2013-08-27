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
package edu.utah.further.fqe.ds.api.util;

import edu.utah.further.core.api.context.Named;

/**
 * An enum centralizing all JMS message header naming conventions that the FQE and DS
 * agree upon.
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
 * @version Mar 1, 2010
 */
public enum MessageHeader implements Named
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * A unique identifier of this command. Used to be correlate the command with its
	 * response in synchronous routes.
	 */
	COMMAND_ID("commandId"),

	/**
	 * FQE command type. Dictates the flow triggered within data sources.
	 */
	COMMAND_TYPE(MessageHeader.COMMAND_TYPE_NAME),

	// Embedded in QueryContext
	// /**
	// * Execution identifier within a federated query plan, if this command is part of a
	// * non-broadcast plan.
	// */
	// EXECUTION_ID(FqeNames.EXECUTION_ID),

	/**
	 * Name of the data source identifier, if this command is directed to a specific data
	 * source.
	 */
	DATA_SOURCE_ID(FqeNames.DATA_SOURCE_ID),

	/**
	 * A header for dictating command flow when something has been canceled.
	 */
	CANCELED("canceled");

	// ========================= CONSTANTS =================================

	/**
	 * If a constant is defined as a direct constant member of the enumerated type and
	 * referenced above, the compiler complains that you can't use it before it has been
	 * defined. But you can't add it before enumerated constants either (syntax error).
	 * Declaring it within a nested class seems to work.
	 */
	// Javadoc doesn't like a nested class here, putting constants directly inside the
	// MessageHeader interface
	/**
	 * An unfortunate duplication of {@link #COMMAND_TYPE}'s name, required for the
	 * constancy of camel annotation expressions.
	 */
	public static final String COMMAND_TYPE_NAME = "commandType";

	// ========================= FIELDS =======================================

	/**
	 * Namespace's name.
	 */
	private final String name;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Construct a message header constant.
	 * 
	 * @param name
	 *            header name
	 */
	private MessageHeader(final String name)
	{
		this.name = name;
	}

	// ========================= IMPL: Named ==================================

	/**
	 * Return this header's name.
	 * 
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public final String getName()
	{
		return name;
	}
}
