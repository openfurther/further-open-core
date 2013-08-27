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
package edu.utah.further.core.util.jaxb;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Utilities of converting JAXB API types to other types.
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
 * @version Feb 3, 2011
 */
@Utility
@Api
public final class JaxbConversionUtil
{
	// ========================= CONSTANTS =================================

	public static final DatatypeFactory DATATYPE_FACTORY;

	static
	{
		try
		{
			DATATYPE_FACTORY = DatatypeFactory.newInstance();
		}
		catch (final DatatypeConfigurationException e)
		{
			throw new ApplicationException(
					"Could not instantiate JAXB data type factory", e);
		}
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private JaxbConversionUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Convert a JAXB calendar object to a {@link Date}.
	 *
	 * @param {@link XMLGregorianCalendar} date object
	 * @return corresponding {@link Date object
	 */
	public static Date toDate(final XMLGregorianCalendar calendar)
	{
		return (calendar == null) ? null : calendar.toGregorianCalendar().getTime();
	}

	/**
	 * Parse a string into a date.
	 *
	 * @param s
	 *            date string. If blank, this method returns <code>null</code>
	 * @param dateFormat
	 *            date format
	 * @return parsed date
	 */
	public static Date parseDateFormat(final String s, final SimpleDateFormat dateFormat)
	{
		if (StringUtils.isBlank(s))
		{
			return null;
		}
		try
		{
			return dateFormat.parse(s);
		}
		catch (final ParseException e)
		{
			throw new ApplicationException("Bad date field " + s, e);
		}
	}

	/**
	 * @param s
	 * @param dateFormat
	 * @return
	 */
	public static XMLGregorianCalendar parseDateGregorian(final String s,
			final SimpleDateFormat dateFormat)
	{
		try
		{
			final Date date = dateFormat.parse(s);
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return DATATYPE_FACTORY.newXMLGregorianCalendar(calendar);
		}
		catch (final ParseException e)
		{
			throw new ApplicationException("Bad date field " + s, e);
		}
	}
}
