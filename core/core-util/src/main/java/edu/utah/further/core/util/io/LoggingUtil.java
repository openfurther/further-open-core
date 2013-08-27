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
package edu.utah.further.core.util.io;

import static edu.utah.further.core.api.text.StringUtil.centerWithSpace;
import static edu.utah.further.core.api.text.StringUtil.repeat;

import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.io.LogLevel;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Utilities related to logging messages.
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
 * @version Jun 29, 2010
 */
@Utility
@Api
public final class LoggingUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Print a logging message the logging level represented by this enumerated type.
	 *
	 * @param log
	 *            logger
	 * @param level
	 *            our logging level
	 * @param s
	 *            string
	 */
	public static void log(final Logger log, final LogLevel level, final String s)
	{
		switch (level)
		{
			case NONE:
			{
				// Suppress all printouts
				break;
			}

			case ERROR:
			{
				if (log.isErrorEnabled())
				{
					log.error(s);
				}
				break;
			}

			case WARN:
			{
				if (log.isWarnEnabled())
				{
					log.warn(s);
				}
				break;
			}

			case INFO:
			{
				if (log.isInfoEnabled())
				{
					log.info(s);
				}
				break;
			}

			case DEBUG:
			{
				if (log.isDebugEnabled())
				{
					log.debug(s);
				}
				break;
			}

			case TRACE:
			{
				if (log.isTraceEnabled())
				{
					log.trace(s);
				}
				break;
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized log level: "
						+ level);
			}
		}
	}

	/**
	 * Print a logging message. Truncate message if it is larger than a specified number
	 * of characters.
	 *
	 * @param logger
	 *            logger
	 * @param level
	 *            logging level
	 * @param s
	 *            string to print
	 * @param numChars
	 *            maximum number of characters to print. If <code>s</code> is longer, it
	 *            is truncated to this size
	 */
	public static void logTruncate(final Logger logger, final LogLevel level,
			final String s, final int numChars)
	{
		log(logger, level, StringUtil.truncate(s, numChars, true));
	}

	/**
	 * Print a small centered title to a logger at DEBUG level.
	 *
	 * @param log
	 *            logger
	 * @param str
	 *            title
	 */
	public static void debugPrintAndCenter(final Logger log, final String str)
	{
		if (log.isDebugEnabled())
		{
			log.debug(centerWithSpace(str, 70, "="));
		}
	}

	/**
	 * Print a small centered title to a logger at TRACE level.
	 *
	 * @param log
	 *            logger
	 * @param str
	 *            title
	 */
	public static void tracePrintAndCenter(final Logger log, final String str)
	{
		if (log.isTraceEnabled())
		{
			log.trace(centerWithSpace(str, 70, "="));
		}
	}

	/**
	 * Print a big title to a logger.
	 *
	 * @param log
	 *            logger
	 * @param string
	 *            title
	 */
	public static void debugPrintBigTitle(final Logger log, final String string)
	{
		if (log.isDebugEnabled())
		{
			log.debug(repeat("=", 70));
			log.debug(string);
			log.debug(repeat("=", 70));
		}
	}

	/**
	 * Print a debugging printout about beginning a new test.
	 * <p>
	 * TODO: replace by Spring AOP that automatically gets the executed method's name in a
	 * before-method.
	 *
	 * @param testName
	 *            test name
	 */
	public static void printStartTest(final Logger log, final String testName)
	{
		if (log.isDebugEnabled())
		{
			log.debug(StringUtil.centerWithSpace(" Start test: " + testName, 50, "="));
		}
	}

	/**
	 * Print the IDs of persistent entities in a collection. Useful for persistence layer
	 * debugging.
	 *
	 * @param log
	 *            logger to print to
	 * @param entities
	 *            entity collection to print
	 */
	public static void printEntityList(final Logger log, final List<?> entities)
	{
		if (log.isTraceEnabled())
		{
			int entityNum = 0;
			for (final Object entity : entities)
			{
				if (ReflectionUtil.instanceOf(entity, PersistentEntity.class))
				{
					log.trace("Entity #" + entityNum + " ID "
							+ ((PersistentEntity<?>) entity).getId() + " " + entity);
				}
				else
				{
					log.trace("Entity #" + entityNum + " " + entity);
				}
				entityNum++;
			}
		}
	}
}
