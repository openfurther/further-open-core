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
package edu.utah.further.core.api.discrete;

import static edu.utah.further.core.api.constant.Strings.ENUM_NAME_SEPARATOR;
import static edu.utah.further.core.api.constant.Strings.SPACE_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.lowerCase;
import static org.apache.commons.lang.StringUtils.replaceChars;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Utilities related to enumerated types and in particular to type conversions.
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
public final class EnumUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private EnumUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the enum constant of the specified enum type with the specified name. The
	 * name must match exactly an identifier used to declare an enum constant in this
	 * type. If it does not, this method returns <code>null</code>.
	 *
	 * @param enumType
	 *            the Class object of the enum type from which to return a constant
	 * @param name
	 *            the name of the constant to return
	 * @return the enum constant of the specified enum type with the specified name or
	 *         <code>null</code> if not found
	 * @see {link Enum.valueOf}
	 */
	public static <E extends Enum<E>> E valueOfNullSafe(final Class<E> enumType, final String name)
	{
		try
		{
			return Enum.valueOf(enumType, name);
		}
		catch (final IllegalArgumentException e)
		{
			return null;
		}
		catch (final NullPointerException e)
		{
			return null;
		}
	}

	/**
	 * Returns the enum constant of the specified enum type with the specified name. The
	 * name must match exactly an identifier used to declare an enum constant in this
	 * type. If it does not, this method returns <code>null</code>.
	 *
	 * @param enumType
	 *            the Class object of the enum type from which to return a constant
	 * @param name
	 *            the name of the constant to return
	 * @return the enum constant of the specified enum type with the specified name or
	 *         <code>null</code> if not found
	 * @see {link Enum.valueOf}
	 */
	public static <E extends Enum<E>> E valueOfNullSafe(final Class<E> enumType, final Object object)
	{
		return (object == null) ? null : valueOfNullSafe(enumType, object.toString());
	}

	/**
	 * Create an enumerated type from string representation (factory method).
	 *
	 * @param enumType
	 *            the Class object of the enum type from which to return a constant
	 * @param s
	 *            string to match
	 * @return enum constant whose <code>toString()</code> matches s
	 * @see {link Enum.valueOf}
	 */
	public static <E extends Enum<E>> E createFromString(final Class<E> enumType, final String s)
	{
		if (s == null)
		{
			return null;
		}
		for (final E t : enumType.getEnumConstants())
		{
			if (s.equals(t.toString()))
			{
				return t;
			}
		}
		return null;
	}

	/**
	 * Convert a string to an enum. Encapsulates unchecked warning suppression.
	 *
	 * @param enumType
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Enum<?> valueOf(final Class<?> enumType, final String value)
	{
		return Enum.valueOf((Class<Enum>) enumType, value);
	}

	/**
	 * Return a default title case string of an enumerated type. The constant's name is
	 * converted to lower case, except the first character, which is capitalized.
	 * Underscore symbols are replaced by white spaces, for improved readability.
	 *
	 * @param enumVal
	 *            The enumerated value to translate
	 *
	 * @return a default title case string
	 */
	public static String getDisplayString(final Enum<?> enumVal)
	{
		return capitalize(replaceChars(lowerCase(enumVal.name()), ENUM_NAME_SEPARATOR,
				SPACE_STRING));
	}

	/**
	 * Indicates whether an object is an enum type or not. Uses
	 * {@link Enum#isAssignableFrom}, which seems to be safer than {@link Object#isEnum}.
	 *
	 * @param object
	 *            An object
	 *
	 * @return is the object of enum type
	 */
	public static boolean isEnum(final Object object)
	{
		return ReflectionUtil.instanceOf(object, Enum.class);
	}

	// ========================= PRIVATE METHODS ===========================
}
