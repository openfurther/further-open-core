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
package edu.utah.further.i2b2.query.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * I2b2 dates include the time zone ("Z") when they are marshalled. This adapter removes
 * the time zone before parsing it into a date. This alleviates problems where time zones
 * affect years on boundaries like 1/1/2006 potentially being 12/31/2005 in another
 * time zone.
 * 
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
 * @version Nov 16, 2011
 */
public class DateNoTimeZoneAdapter extends XmlAdapter<String, Date>
{
	/**
	 * A date format with the time zone - default for what i2b2 uses
	 */
	private static final String datePatternWithTimezone = "yyyy-MM-ddZ";

	/**
	 * A date format without the time zone since we only care about the date
	 */
	private static final String datePatternWithoutTimezone = "yyyy-MM-dd";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final Date date) throws Exception
	{
		//Return to normal i2b2 format when marshalled
		final SimpleDateFormat dateFormat = new SimpleDateFormat(datePatternWithTimezone);
		return dateFormat.format(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Date unmarshal(final String date) throws Exception
	{
		//Remove the Z for time zone when 
		final String withoutTimezone = date.replace('Z', ' ').trim();
		return new SimpleDateFormat(datePatternWithoutTimezone).parse(withoutTimezone);
	}

}
