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
package edu.utah.further.security.api.exceptions;

/**
 * An authorization exception for when a principal is not authorized
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 7, 2012
 */
public class AuthorizationException extends RuntimeException
{

	/**
	 * Generated Serial UID
	 */
	private static final long serialVersionUID = -6874400543375909055L;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public AuthorizationException(final String arg0, final Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public AuthorizationException(final String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public AuthorizationException(final Throwable arg0)
	{
		super(arg0);
	}

}
