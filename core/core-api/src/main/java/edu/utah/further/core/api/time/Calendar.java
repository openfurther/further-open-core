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

/**
 * Wraps {@link java.util.Calendar} fields to type safe enums.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Mar 25, 2010
 */
public enum Calendar
{
	// ========================= CONSTANTS =================================

	SECOND(java.util.Calendar.SECOND),

	MINUTE(java.util.Calendar.MINUTE),

	HOUR(java.util.Calendar.HOUR),

	DAY(java.util.Calendar.DATE),

	MONTH(java.util.Calendar.MONTH);

	// ========================= FIELDS =================================

	/**
	 * The {@link java.util.Calendar} constant field
	 */
	private int field;

	// ========================= CONSTRUCTORS ===========================

	/**
	 * @param field
	 */
	private Calendar(final int field)
	{
		this.field = field;
	}
	
	// ========================= GET/SET =================================
	
	/**
	 * Return the field property.
	 *
	 * @return the field
	 */
	public int getField()
	{
		return field;
	}


	// ========================= Impl: Object =============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return String.valueOf(field);
	}
}
