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

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * Core utilities related to times, dates and timestamps.
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
 * @version Oct 13, 2008
 */
@Utility
@Api
public final class TimeUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Number of milliseconds in one milliseconds. If this is not one, I don't know what
	 * one is.
	 */
	public static final long ONE_MILLISECOND = TimeUnit.MILLISECONDS.toMillis(1l);

	/**
	 * Number of milliseconds in one second.
	 */
	public static final long ONE_SECOND = TimeUnit.SECONDS.toMillis(1l);

	/**
	 * Number of milliseconds in one minute.
	 */
	public static final long ONE_MINUTE = TimeUnit.MINUTES.toMillis(1l);

	/**
	 * Number of milliseconds in one hour.
	 */
	public static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1l);

	/**
	 * Number of milliseconds in one day.
	 */
	public static final long ONE_DAY = TimeUnit.DAYS.toMillis(1l);

	/**
	 * Maximum number of seconds.
	 */
	public static final long SECONDS = TimeUnit.MINUTES.toSeconds(1l);

	/**
	 * Maximum number of minutes.
	 */
	public static final long MINUTES = TimeUnit.HOURS.toMinutes(1l);

	/**
	 * Maximum number of hours.
	 */
	public static final long HOURS = TimeUnit.DAYS.toHours(1l);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in a singleton class.
	 * </p>
	 */
	private TimeUtil()
	{
		CoreUtil.preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param time
	 * @return
	 */
	public static Timestamp getTimeAsTimestampNullSafe(final Long time)
	{
		return (time == null) ? null : new Timestamp(time.longValue());
	}

	/**
	 * @param ts
	 * @return
	 */
	public static Timestamp getTimestampNullSafe(final Date ts)
	{
		return (ts == null) ? null : new Timestamp(ts.getTime());
	}

	/**
	 * @param ts
	 * @return
	 */
	public static Long getDateAsTime(final Date ts)
	{
		return (ts == null) ? null : new Long(ts.getTime());
	}

	/**
	 * Return the duration of a time interval (end-start) in milliseconds. If start or end
	 * are <code>null</code>, returns <code>null</code>.
	 * 
	 * @param start
	 *            interval start date
	 * @param end
	 *            interval end date
	 * @return interval duration in milliseconds
	 */
	public static Long getDuration(final Timestamp start, final Date end)
	{
		return ((start == null) || (end == null)) ? null : new Long(start.getTime()
				- end.getTime());
	}

	// ========================= PRIVATE METHODS ===========================
}
