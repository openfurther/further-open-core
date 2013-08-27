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
package edu.utah.further.core.util.text;

import java.util.Scanner;

import edu.utah.further.core.api.text.StringParser;

/**
 * Parses a CSV file line into an object.
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
 * @version Jan 27, 2011
 */
public abstract class AbstractStringParser<E> implements StringParser<E>
{
	// ========================= CONSTANTS =================================

	/**
	 * By default, we parse space-delimited strings.
	 */
	private static final String DEFAULT_DELIMITER = " ";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Field delimiter.
	 */
	private String delimiter = DEFAULT_DELIMITER;

	// ========================= IMPL: StringParser ========================

	/**
	 * @param s
	 * @return
	 * @see edu.utah.further.core.api.text.StringParser#fromString(java.lang.String)
	 */
	@Override
	public final E fromString(final String s)
	{
		try (@SuppressWarnings("resource")
		//useDelimiter returns the same scanner but Eclipse thinks it's 2 different scanners
		final Scanner scanner = new Scanner(s).useDelimiter(getDelimiter()))
		{
			final E result = fromScanner(scanner);
			return result;
		}
	}

	/**
	 * Parse a line using a scanner into an object.
	 *
	 * @param scanner
	 *            line scanner
	 * @return target object
	 */
	abstract protected E fromScanner(final Scanner scanner);

	// ========================= GET & SET =================================

	/**
	 * Return the delimiter property.
	 *
	 * @return the delimiter
	 */
	protected final String getDelimiter()
	{
		return delimiter;
	}

	/**
	 * Set a new value for the delimiter property.
	 *
	 * @param delimiter
	 *            the delimiter to set
	 */
	@Override
	public void setDelimiter(final String delimiter)
	{
		this.delimiter = delimiter;
	}
}
