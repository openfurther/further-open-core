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

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.context.Named;

/**
 * FQE command types. Although it is {@link Named}, we can afford to use the
 * {@link #name()} values as {@link #getName()}, because these constants are to be used as
 * the values of a single custom FQE camel header whose name is supposed to be guaranteed
 * to have no name collisions with our camel headers, as its corresponding type (
 * {@link MessageHeader}) also implements {@link Named}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 27, 2009
 */
public enum CommandType implements Named, Labeled
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * A data query.
	 */
	DATA_QUERY,
	
	/**
	 * A count query.
	 */
	COUNT_QUERY,

	/**
	 * A Master Patient Index (MPI) query.
	 */
	MPI_QUERY,

	/**
	 * Data source meta data retrieval request.
	 */
	META_DATA,

	/**
	 * Data source remote controlling from the FQE. Mostly for testing purposes.
	 */
	REMOTE_CONTROL;

	// ========================= CONSTANTS =================================

	/**
	 * A useful cached part of {@link #getLabel()}'s computation.
	 */
	private static final String LABEL_PREFIX = CommandType.class
			.getCanonicalName()
			.toLowerCase()
			+ Strings.PROPERTY_SCOPE_CHAR;

	// ========================= IMPL: Named ===============================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name();
	}

	// ========================= IMPL: Labeled ==============================

	/**
	 * Returns a label to be used as a chain request header value relating to this command
	 * type. Its computation is aimed at minimizing the chance of a naming conflicts with
	 * other headers.
	 * <p>
	 * Can be viewed as a string-valued hash code of the enumerated constant.
	 * 
	 * @return the command type's corresponding label
	 */
	@Override
	public String getLabel()
	{
		return LABEL_PREFIX + name();
	}

}
