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
package edu.utah.further.core.api.time;

import java.util.Date;

/**
 * An abstraction of system time routines. Allows customizing the time source for temporal
 * testing. Also centralizes random number generation, as its seed often depends on system
 * time.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 5, 2008
 */
public final class TimeService
{
	// ========================= CONSTANTS =================================

	/**
	 * The default time source used by this service. Returns the JDK time.
	 */
	private static final TimeSource DEFAULT_TIME_SOURCE = new TimeSource()
	{
		/**
		 * @return
		 * @see edu.utah.further.core.api.time.TimeSource#getMillis()
		 */
		@Override
		public long getMillis()
		{
			return System.currentTimeMillis();
		}
	};

	/**
	 * A custom time source. If <code>null</code>, {@link #DEFAULT_TIME_SOURCE} is used.
	 * <i>Note: setting this variable should be synchronized</i>.
	 */
	private static TimeSource source = null;

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

	/**
	 * Reset the time source.
	 */
	public static void reset()
	{
		setTimeSource(null);
	}

	/**
	 * Set a custom time source.
	 * 
	 * @param source
	 *            time source to set
	 */
	public static void setTimeSource(final TimeSource source)
	{
		synchronized (TimeService.class)
		{
			TimeService.source = source;
		}
	}

	/**
	 * Set system time to a fixed time. Useful in testing.
	 * 
	 * @param fakeTime
	 *            fixed system time [milliseconds]
	 */
	public static void fixSystemTime(final long fakeTime)
	{
		setTimeSource(new TimeSource()
		{
			@Override
			public long getMillis()
			{
				return fakeTime;
			}
		});
	}

	/**
	 * Returns the current time in milliseconds.
	 * 
	 * @return the current time in milliseconds
	 */
	public static long getMillis()
	{
		return getTimeSource().getMillis();
	}

	/**
	 * Returns the current date.
	 * 
	 * @return the current date
	 */
	public static Date getDate()
	{
		return new Date(getMillis());
	}

	/**
	 * Allocates a {@link Date} object and initializes it to represent the specified
	 * number of milliseconds since the standard base time known as "the epoch", namely
	 * January 1, 1970, 00:00:00 GMT.
	 * 
	 * @param millis
	 *            the milliseconds since January 1, 1970, 00:00:00 GMT
	 * @see Date#Date(long)
	 * @see java.lang.System#currentTimeMillis()
	 */
	public static Date getDate(final long millis)
	{
		return new Date(millis);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the current time source.
	 * 
	 * @return the current time source
	 */
	private static TimeSource getTimeSource()
	{
		return source != null ? source : DEFAULT_TIME_SOURCE;
	}
}
