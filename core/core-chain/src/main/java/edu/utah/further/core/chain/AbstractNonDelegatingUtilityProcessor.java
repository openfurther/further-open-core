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
package edu.utah.further.core.chain;

import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A base class utility processors that do not have a delegate.
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
 * @version Oct 1, 2010
 */
public abstract class AbstractNonDelegatingUtilityProcessor extends
		AbstractUtilityProcessor<Object>
{
	// ========================= CONSTANTS =================================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the processsing delegate property.
	 *
	 * @return the delegate
	 */
	@Override
	protected final Object getDelegate()
	{
		throw new ApplicationException(
				unsupportedOperationMessage("This is a non-delegating processor"));
	}
}
