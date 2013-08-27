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

import static edu.utah.further.core.util.collections.page.PagerUtil.CSV_GUARD_CHARACTER;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.api.text.StringParser;

/**
 * A pager of a CSV file input stream. Translates each row into an object using a
 * {@link StringParser}.
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
final class CsvStreamPager<E> extends AbstractListPager<E>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(CsvStreamPager.class);

	// ========================= FIELDS ====================================

	/**
	 * parses each CSV file line into an object of type <code>E</code>.
	 */
	private final StringParser<E> stringParser;

	/**
	 * Internal stream line scanner.
	 */
	private final Scanner scanner;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param inputStream
	 * @param pagingStrategy
	 * @param stringParser
	 */
	@SuppressWarnings("resource")
	// Caller closes scanner or passed inputstreams
	CsvStreamPager(final InputStream inputStream, final PagingStrategy pagingStrategy,
			final StringParser<E> stringParser)
	{
		super(pagingStrategy);
		ValidationUtil.validateNotNull("inputStream", inputStream);
		this.stringParser = stringParser;
		this.scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)));
	}

	// ========================= IMPL: Iterator<List<E>> ===================

	/**
	 * @return
	 * @see edu.utah.further.core.util.collections.page.AbstractPager#hasNextInternal()
	 */
	@Override
	protected boolean hasNextInternal()
	{
		return scanner.hasNextLine();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.collections.page.Pager#close()
	 */
	@Override
	public void close()
	{
		if (scanner != null)
		{
			scanner.close();
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a view of the page of the iterable object starting at the current iterant
	 * returned from the object's iterator and of size <code>currentPageSize</code> (or
	 * smaller, if the object iteration ends before the page size).
	 * 
	 * @param currentPageSize
	 *            maximum page size
	 */
	@Override
	protected List<E> getPage(final int currentPageSize)
	{
		final List<E> page = CollectionUtil.newList();
		// Allow sub-classes to override -- don't use our private field here
		for (int i = 0; !isEndOfIteration() && (i < currentPageSize)
				&& scanner.hasNextLine(); i++)
		{
			final String line = scanner.nextLine();
			incrementTotalIterantCounter();
			if (getTotalIterantCounter() > getNumHeaderRows())
			{
				if (log.isDebugEnabled())
				{
					log.debug("Read line " + getTotalIterantCounter() + " " + line + " ");
				}
				try
				{
					// Guard both the beginning and end of the string
					final E entity = stringParser.fromString(CSV_GUARD_CHARACTER + line
							+ CSV_GUARD_CHARACTER);
					if (log.isDebugEnabled())
					{
						log.debug("Converted line to " + entity);
					}
					page.add(entity);
					addSingleIterant();
				}
				catch (final Throwable e)
				{
					if (log.isErrorEnabled())
					{
						log.error("Bad line " + getTotalIterantCounter() + ", skipping: "
								+ line + " error " + e);
					}
				}
			}
		}
		return page;
	}
}
