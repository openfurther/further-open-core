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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Parses a file line into a list of tokens.
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
 * @version Sep 2, 2010
 */
public final class CsvLineScanner extends AbstractLineScanner<List<String>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CsvLineScanner.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * By default, uses a comma delimiter.
	 */
	public CsvLineScanner()
	{
		setDelimiter(",");
	}

	// ========================= IMPL: LineScanner =======================

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.util.text.LineScanner#parse(java.lang.String)
	 */
	@Override
	public List<String> parse(final String line)
	{
		final List<String> tokens = CollectionUtil.newList();
		if (StringUtils.isNotBlank(line))
		{
			try(final Scanner scanner = new Scanner(line))
			{
				scanner.useDelimiter(getDelimiter());
				while (scanner.hasNext())
				{
					tokens.add(scanner.next());
				}
			}

		}
		return tokens;
	}

}
