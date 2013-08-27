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
package edu.utah.further.core.api.ws;

import edu.utah.further.core.api.context.Named;

/**
 * Useful HTTP headers. Using a type-safe enum instead of string constants of header
 * names.
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
public enum HttpHeader implements Named
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Content type (MIME type).
	 */
	CONTENT_TYPE
	{
		/**
		 * Return this header's name.
		 *
		 * @return header name string
		 * @see edu.utah.further.core.api.ws.HttpHeader#getName()
		 */
		@Override
		public String getName()
		{
			return "Content-Type";
		}
	},

	/**
	 * Response content length.
	 */
	CONTENT_LENGTH
	{
		/**
		 * Return this header's name.
		 *
		 * @return header name string
		 * @see edu.utah.further.core.api.ws.HttpHeader#getName()
		 */
		@Override
		public String getName()
		{
			return "Content-Length";
		}
	},

	/**
	 * Resource storage code, if this response streams the contents of a server resource.
	 */
	RESOURCE_STORAGE_CODE
	{
		/**
		 * Return this header's name.
		 *
		 * @return header name string
		 * @see edu.utah.further.core.api.ws.HttpHeader#getName()
		 */
		@Override
		public String getName()
		{
			return "Resource-Storage-Code";
		}
	};

	// ========================= CONSTANTS =================================

	// ========================= METHODS ======================================

	/**
	 * Return this HTTP header's name.
	 *
	 * @return header name string
	 * @see edu.utah.further.core.api.ws.HttpHeader#getName()
	 */
	@Override
	public abstract String getName();
}
