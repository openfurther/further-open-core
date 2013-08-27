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

import static edu.utah.further.core.api.text.StringUtil.quote;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A utility to process a text file into a list of line strings.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see http://www.javapractices.com/topic/TopicAction.do?Id=42
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 24, 2008
 */
public class LineListFileScanner<T> extends AbstractFileScanner<List<T>, T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(LineListFileScanner.class);

	// ========================= FIELDS ====================================

	/**
	 * A flag to ignore blank lines.
	 */
	private boolean ignoreBlankLines = true;

	/**
	 * A flag to trim lines before passing them on to the line parser.
	 */
	private boolean trimLines = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(getLineScanner(), "A line scanner must be set");
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the ignoreBlankLines property.
	 *
	 * @param ignoreBlankLines
	 *            the ignoreBlankLines to set
	 */
	public void setIgnoreBlankLines(final boolean ignoreBlankLines)
	{
		this.ignoreBlankLines = ignoreBlankLines;
	}

	/**
	 * Set a new value for the trimLines property.
	 *
	 * @param trimLines
	 *            the trimLines to set
	 */
	public void setTrimLines(final boolean trimLines)
	{
		this.trimLines = trimLines;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Template method that parses an entire input from a scanner.
	 *
	 * @param scanner
	 *            provides input
	 * @param lineScanner
	 *            scans each line into an entity
	 * @return list of entities, one per line
	 */
	@Override
	protected List<T> parseInput(final Scanner scanner)
	{
		final List<T> entities = CollectionUtil.newList();
		int lineNumber = 0;
		String line = null;
		try
		{
			while (scanner.hasNextLine())
			{
				lineNumber++;
				line = scanner.nextLine();
				if (isIgnoreBlankLines() && StringUtils.isBlank(line))
				{
					if (log.isDebugEnabled())
					{
						log.debug("Ignoring blank line " + lineNumber);
					}
				}
				else
				{
					if (log.isDebugEnabled())
					{
						log.debug("Parsing line " + lineNumber + ": " + quote(line));
					}
					final String filteredLine = filterLine(line);
					entities.add(getLineScanner().parse(filteredLine));
				}
			}
		}
		catch (final Exception e)
		{
			throw new ApplicationException("Failed to parse file at line " + lineNumber
					+ ": " + quote(line), e);
		}
		finally
		{
			// Ensure the underlying stream is always closed
			scanner.close();
		}
		return entities;
	}

	/**
	 * @param line
	 * @return
	 */
	protected String filterLine(final String line)
	{
		return isTrimLines() ? line.trim() : line;
	}

	/**
	 * Return the ignoreBlankLines property.
	 *
	 * @return the ignoreBlankLines
	 */
	protected boolean isIgnoreBlankLines()
	{
		return ignoreBlankLines;
	}

	/**
	 * Return the trimLines property.
	 *
	 * @return the trimLines
	 */
	protected boolean isTrimLines()
	{
		return trimLines;
	}
}
