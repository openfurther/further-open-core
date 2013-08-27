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
package edu.utah.further.core.util.registry;

import java.util.List;
import java.util.UUID;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.api.observer.Message;
import edu.utah.further.core.api.observer.Observer;

/**
 * An observer pattern message that signals a change in a task's state.
 *S <p>
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
@Api
public interface SimpleDataMessage extends Message, HasIdentifier<UUID>,
		Comparable<SimpleDataMessage>
{
	// ========================= CONSTANTS =================================

	enum Type
	{
		DATA_REQUEST, RESPONSE
	}

	// ========================= METHODS ===================================

	/**
	 * Return the type of this message.
	 *
	 * @return the type of this message
	 */
	Type getType();

	/**
	 * Return the subject that sent this message.
	 *
	 * @return the subject that sent this message
	 */
	@Override
	Node getSubject();

	/**
	 * Return the data of this message.
	 *
	 * @return message data
	 */
	List<String> getData();

	/**
	 * Returns the original message, if this message is of type {@link Type#RESPONSE}.
	 * Otherwise, returns <code>null</code>.
	 *
	 * @return the original message
	 */
	SimpleDataMessage getOriginalMessage();

	/**
	 * Return the observer property.
	 *
	 * @return the observer
	 */
	Observer getObserver();
}