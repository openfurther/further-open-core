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

import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.PublicMapEntry;
import edu.utah.further.core.api.collections.PublicMapEntryImpl;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;

/**
 * An example utility to read a properties file.
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
public final class PropertiesFileScanner extends
		AbstractFileScanner<Properties, PublicMapEntry<String, String>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PropertiesFileScanner.class);

	// ========================= FIELDS ====================================

	// ========================= IMPL: AbstractFileScanner =================

	// ========================= PRIVATE METHODS ===========================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.util.text.AbstractFileScanner#parseInput(java.util.Scanner,
	 * edu.utah.further.core.util.text.LineScanner)
	 */
	@Override
	protected Properties parseInput(final Scanner scanner)
	{
		final Properties properties = new Properties();
		int lineNumber = 0;
		try
		{
			while (scanner.hasNextLine())
			{
				lineNumber++;
				final String line = scanner.nextLine();
				if (log.isDebugEnabled())
				{
					log.debug("Parsing line " + StringUtil.quote(line));
				}
				final PublicMapEntry<String, String> entry = LINE_SCANNER.parse(line);
				properties.put(entry.getKey(), entry.getValue());
			}
		}
		catch (final Exception e)
		{
			throw new ApplicationException("Failed to parse line " + lineNumber, e);
		}
		finally
		{
			// Ensure the underlying stream is always closed
			scanner.close();
		}
		return properties;
	}

	/**
	 * Parses a single line of a properties file.
	 * <P>
	 * Expects simple name-value pairs, separated by an '=' sign. Examples of valid input
	 * : <tt>height = 167cm</tt> <tt>mass =  65kg</tt> <tt>disposition =  "grumpy"</tt>
	 * <tt>this is the name = this is the value</tt>
	 *
	 * @param line
	 *            text line
	 */
	private static final LineScanner<PublicMapEntry<String, String>> LINE_SCANNER = new LineScanner<PublicMapEntry<String, String>>()
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see edu.utah.further.core.util.text.LineScanner#parse(java.lang.String)
		 */
		@Override
		public PublicMapEntry<String, String> parse(final String line)
		{
			try (final Scanner scanner = new Scanner(line)) 
			{
				scanner.useDelimiter("=");
				String name = null;
				String value = null;
				if (scanner.hasNext())
				{
					name = scanner.next().trim();
					value = scanner.next().trim();
					log.debug("Name " + quote(name) + " value " + quote(value));
				}
				else
				{
					log.debug("Empty or invalid line. Unable to process.");
				}
	
				// No need for finally here because we read from String source
				scanner.close();
	
				return new PublicMapEntryImpl<>(name, value);
			}
		}
	};

}
