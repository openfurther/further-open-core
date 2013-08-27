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
package edu.utah.further.dts.ws.fixture;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;
import edu.utah.further.dts.api.to.DtsDataTo;
import edu.utah.further.dts.api.to.DtsDataToFactory;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.dts.ws.api.domain.DtsConceptToWsImpl;

/**
 * A factory of DTS entity transfer objects.
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
 * @version Dec 8, 2008
 */
// @Service
public class DtsDataToFactoryWsImpl implements DtsDataToFactory
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Create a new transfer object corresponding to the argument's DTS entity type, and
	 * copy the original fields into it.
	 *
	 * @param original
	 *            DTS entity
	 * @return corresponding DTS transfer object copy of <code>original</code>
	 * @see edu.utah.further.dts.api.to.DtsDataToFactory#newInstance(edu.utah.further.dts.api.domain.namespace.DtsData)
	 */
	@Override
	public DtsDataTo newInstance(final DtsData original)
	{
		final DtsDataType type = original.getType();
		switch (type)
		{
			case NAMESPACE:
			{
				return new DtsNamespaceToImpl().copyFrom(original);
			}

			case CONCEPT:
				// case CONCEPT_CHILD:
			{
				return new DtsConceptToWsImpl().copyFrom(original);
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(type));
			}
		}
	}
}
