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

import edu.utah.further.core.api.lang.Builder;

/**
 * A utility class that builds a time duration in milliseconds from its parts (days,
 * hours, etc.).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 8, 2010
 */
public final class DurationBuilder implements Builder<Long>
{
	// ========================= FIELDS ====================================

	/**
	 * Holds the number of days in the time duration.
	 */
	private long days = 0l;

	/**
	 * Holds the hours part of the time duration.
	 */
	private int hours = 0;

	/**
	 * Holds the minutes part of the time duration.
	 */
	private int minutes = 0;

	/**
	 * Holds the seconds part of the time duration.
	 */
	private int seconds = 0;

	/**
	 * Holds the milliseconds part of the time duration.
	 */
	private int milliseconds = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Convert time duration, expressed in individual date fields, to milliseconds.
	 *
	 * @param days
	 *            the days part of the time duration
	 * @param hours
	 *            the hours part of the time duration
	 * @param minutes
	 *            the minutes part of the time duration
	 * @param seconds
	 *            the seconds part of the time duration
	 * @param milliseconds
	 *            the milliseconds part of the time duration
	 * @return time duration in milliseconds
	 */
	public static long toMillis(final long days, final int hours, final int minutes, final int seconds,
			final int milliseconds)
	{
		return new DurationBuilder()
				.setDays(days)
				.setHours(hours)
				.setMinutes(minutes)
				.setSeconds(seconds)
				.setMilliseconds(milliseconds)
				.build()
				.longValue();
	}

	// ========================= METHODS ===================================

	/**
	 * Builds the list command output and returns it.
	 *
	 * @return the list command output, based on all information gathered by this builder
	 *         instance
	 */
	@Override
	public Long build()
	{
		return new Long(days * TimeUtil.ONE_DAY + hours * TimeUtil.ONE_HOUR + minutes
				* TimeUtil.ONE_MINUTE + seconds * TimeUtil.ONE_SECOND + milliseconds
				* TimeUtil.ONE_MILLISECOND);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the days property.
	 *
	 * @return the days
	 */
	public long getDays()
	{
		return days;
	}

	/**
	 * Set a new value for the days property.
	 *
	 * @param days
	 *            the days to set
	 * @return this, for method chaining
	 */
	public DurationBuilder setDays(final long days)
	{
		this.days = days;
		return this;
	}

	/**
	 * Return the hours property.
	 *
	 * @return the hours
	 */
	public int getHours()
	{
		return hours;
	}

	/**
	 * Set a new value for the hours property.
	 *
	 * @param hours
	 *            the hours to set
	 * @return this, for method chaining
	 */
	public DurationBuilder setHours(final int hours)
	{
		this.hours = hours;
		return this;
	}

	/**
	 * Return the minutes property.
	 *
	 * @return the minutes
	 */
	public int getMinutes()
	{
		return minutes;
	}

	/**
	 * Set a new value for the minutes property.
	 *
	 * @param minutes
	 *            the minutes to set
	 * @return this, for method chaining
	 */
	public DurationBuilder setMinutes(final int minutes)
	{
		this.minutes = minutes;
		return this;
	}

	/**
	 * Return the seconds property.
	 *
	 * @return the seconds
	 */
	public int getSeconds()
	{
		return seconds;
	}

	/**
	 * Set a new value for the seconds property.
	 *
	 * @param seconds
	 *            the seconds to set
	 * @return this, for method chaining
	 */
	public DurationBuilder setSeconds(final int seconds)
	{
		this.seconds = seconds;
		return this;
	}

	/**
	 * Return the milliseconds property.
	 *
	 * @return the milliseconds
	 */
	public int getMilliseconds()
	{
		return milliseconds;
	}

	/**
	 * Set a new value for the milliseconds property.
	 *
	 * @param milliseconds
	 *            the milliseconds to set
	 * @return this, for method chaining
	 */
	public DurationBuilder setMilliseconds(final int milliseconds)
	{
		this.milliseconds = milliseconds;
		return this;
	}

}
