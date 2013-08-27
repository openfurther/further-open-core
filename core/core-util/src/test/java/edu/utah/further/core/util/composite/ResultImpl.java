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
package edu.utah.further.core.util.composite;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An observer pattern message that signals a change in a data source's state.
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
 * @version May 29, 2009
 */
final class ResultImpl implements Result
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Type of state for which we report a change in this message.
	 */
	private final List<String> data;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a data message in reply to another message.
	 *
	 * @param type
	 * @param originalMessage
	 * @param node
	 * @param data
	 * @param observer
	 */
	public ResultImpl(final List<String> data)
	{
		this.data = data;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Print debugging information on the message.
	 *
	 * @return debugging information on the message
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("data", data);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: TaskStateChangeMessage ====

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.SimpleDataMessage#getData()
	 */
	@Override
	public List<String> getData()
	{
		return data;
	}
}
