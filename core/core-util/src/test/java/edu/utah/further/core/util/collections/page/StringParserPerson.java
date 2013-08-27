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
package edu.utah.further.core.util.collections.page;

import java.util.Scanner;

import edu.utah.further.core.util.text.AbstractStringParser;

/**
 * Parses a line to a {@link Person} entity.
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
final class StringParserPerson extends AbstractStringParser<Person>
{
	// ========================= IMPLEMENTATION: AbstractStringParserCsv ===

	/**
	 * @param scanner
	 * @return
	 * @see edu.utah.further.core.util.text.AbstractStringParser#fromScanner(java.util.Scanner)
	 */
	@Override
	protected Person fromScanner(final Scanner scanner)
	{
		final Person person = new Person();
		person.setFirstName(scanner.next().substring(1)); // Remove leading guard char
		final String s = scanner.next();
		person.setLastName(s.substring(0, s.length() - 1)); // remove trailing guard char
		return person;
	}
}
