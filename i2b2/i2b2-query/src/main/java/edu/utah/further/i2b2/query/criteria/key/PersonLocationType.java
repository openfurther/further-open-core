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
package edu.utah.further.i2b2.query.criteria.key;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.regex.RegExUtil.capture;

import java.util.Map;

import edu.utah.further.core.api.context.Named;

/**
 * All location codes are a code with a prefix followed by a '|' character and then the
 * code. This Enum represents the possible types of prefixes that represent a location.
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
 * @version Aug 6, 2010
 */
public enum PersonLocationType implements Named
{
	// ========================= CONSTANTS ====================================
	/**
	 * Current Location
	 */
	CURRENT("CR", "CurrentLocation"),

	/**
	 * Death Location
	 */
	DEATH("DTH", "DeathLocation"),
	
	/**
	 * Death Location
	 */
	BIRTH("BTH", "BirthLocation"),

	/**
	 * Unknown location type
	 */
	UNKNOWN("UNKNOWN", "UnknownLocation");

	// ========================= FIELDS ====================================

	/**
	 * The name of the location
	 */
	private final String name;

	/**
	 * The name of the field for this LocationType.
	 */
	private final String fieldName;

	/**
	 * A map match the code/name to the LocationType
	 */
	private static final Map<String, PersonLocationType> nameToEnum = newMap();

	/**
	 * Capture everything before the '|'
	 */
	private static final String locationCapture = "(\\w*)\\|[A-Za-z0-9]";

	// ========================= STATIC INIT ====================================

	static
	{
		for (final PersonLocationType type : values())
		{
			nameToEnum.put(type.getName(), type);
		}
	}

	// ========================= CONSTRUCTORS ====================================

	/**
	 * Constructor
	 *
	 * @param name
	 */
	private PersonLocationType(final String name, final String fieldName)
	{
		this.name = name;
		this.fieldName = fieldName;
	}

	// ========================= Impl: Named =====================================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= GETTERS =========================================

	/**
	 * Return the fieldName property.
	 *
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	// ========================= STATIC METHODS ===================================

	/**
	 * Returns a location type from it's location code.
	 *
	 * @param code
	 * @return
	 */
	public static final PersonLocationType fromCode(final String code)
	{
		final String name = capture(code, locationCapture).get(0);
		return fromName(name);
	}

	/**
	 * Returns a location type from it's name.
	 *
	 * @param name
	 * @return
	 */
	public static final PersonLocationType fromName(final String name)
	{
		return nameToEnum.get(name);
	}
}
