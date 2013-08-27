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
package edu.utah.further.mdr.api.domain.uml;

import static edu.utah.further.core.api.message.Severity.ERROR;
import static edu.utah.further.core.api.message.Severity.INFO;
import static edu.utah.further.core.api.message.Severity.NONE;
import static edu.utah.further.core.api.message.Severity.WARNING;
import edu.utah.further.core.api.message.Severity;

/**
 * A UML element state that allows fine-grained control over its loading.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
public enum ElementStatus
{
	// ========================= CONSTANTS =================================

	/**
	 * The element was not loaded at all due to a XMI validation error.
	 */
	NOT_LOADED
	{
		/**
		 * Return the severity level of this element status.
		 *
		 * @return the severity level of this element status.
		 * @see edu.utah.further.mdr.api.domain.uml.ElementStatus#getSeverity()
		 */
		@Override
		public Severity getSeverity()
		{
			return ERROR;
		}
	},

	/**
	 * The element was loaded from the XMI file, but did not pass validation during UML
	 * model integration (usually due to an invalid XMI tag value).
	 */
	INVALID
	{
		/**
		 * Return the severity level of this element status.
		 *
		 * @return the severity level of this element status.
		 * @see edu.utah.further.mdr.api.domain.uml.ElementStatus#getSeverity()
		 */
		@Override
		public Severity getSeverity()
		{
			return ERROR;
		}
	},

	/**
	 * The element was loaded from the XMI file and passed validation, but tagging is not
	 * yet finished, hence it is not yet a well-formed, valid element.
	 */
	IN_PROGRESS
	{
		/**
		 * Return the severity level of this element status.
		 *
		 * @return the severity level of this element status.
		 * @see edu.utah.further.mdr.api.domain.uml.ElementStatus#getSeverity()
		 */
		@Override
		public Severity getSeverity()
		{
			return WARNING;
		}
	},

	/**
	 * A well-formed, valid element, which admits some minor corrections (e.g. bad naming
	 * conventions).
	 */
	ACTIVE_WITH_INFO
	{
		/**
		 * Return the severity level of this element status.
		 *
		 * @return the severity level of this element status.
		 * @see edu.utah.further.mdr.api.domain.uml.ElementStatus#getSeverity()
		 */
		@Override
		public Severity getSeverity()
		{
			return INFO;
		}
	},

	/**
	 * A well-formed, valid element.
	 */
	ACTIVE
	{
		/**
		 * Return the severity level of this element status.
		 *
		 * @return the severity level of this element status.
		 * @see edu.utah.further.mdr.api.domain.uml.ElementStatus#getSeverity()
		 */
		@Override
		public Severity getSeverity()
		{
			return NONE;
		}
	};

	// ========================= METHODS ======================================

	/**
	 * Return the severity level of this element status.
	 *
	 * @return the severity level of this element status.
	 */
	public abstract Severity getSeverity();
}
