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
package edu.utah.further.core.api.message;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.message.Messages.cannotCompareMessage;
import static edu.utah.further.core.api.message.Messages.notEmptyMessage;
import static edu.utah.further.core.api.message.Messages.notInRange;
import static edu.utah.further.core.api.message.Messages.notNullMessage;
import static edu.utah.further.core.api.message.Messages.notWiredMessage;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import java.util.Collection;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Common validation templates, assertions and exceptions. The templates also convert all
 * exceptions to {@link BusinessRuleException}.
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
public final class ValidationUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ValidationUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param entityName
	 * @param object
	 */
	public static void validateNotNull(final String entityName, final Object object)
	{
		try
		{
			notNull(object, notNullMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param severity
	 * @param entityName
	 * @param object
	 */
	public static void validateNotNull(final Severity severity, final String entityName,
			final Object object)
	{
		try
		{
			notNull(object, notNullMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(severity, e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param string
	 */
	public static void validateNotEmpty(final String entityName, final String string)
	{
		try
		{
			notEmpty(string, notEmptyMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param object
	 */
	public static void validateNotEmpty(final String entityName, final Object[] object)
	{
		try
		{
			notEmpty(object, notEmptyMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param object
	 */
	public static void validateNotEmpty(final String entityName,
			final Collection<?> object)
	{
		try
		{
			notEmpty(object, notEmptyMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * A useful check for {@link Comparable#compareTo(Object)} methods.
	 * 
	 * @param object1
	 * @param object2
	 */
	public static void validateEqualClass(final Object object1, final Object object2)
	{
		try
		{
			if ((object1 != object2) && (object1.getClass() != object2.getClass()))
			{
				throw new ClassCastException(cannotCompareMessage(object1.getClass(),
						object2.getClass()));
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * A useful check for {@link Comparable#compareTo(Object)} methods.
	 * 
	 * @param object
	 * @param type
	 */
	public static void validateInstanceOf(final Object object, final Class<?> type)
	{
		try
		{
			if (!ReflectionUtil.instanceOf(object, type))
			{
				throw new ClassCastException(
						cannotCompareMessage(type, object.getClass()));
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param object
	 */
	public static void validateWired(final String entityName, final Object object)
	{
		try
		{
			notNull(object, notWiredMessage(entityName));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param value
	 * @param minValue
	 * @param maxValue
	 */
	public static void validateInRange(final String entityName, final int value,
			final int minValue, final int maxValue)
	{
		try
		{
			isTrue((value >= minValue) && (value <= maxValue), notInRange(entityName,
					value, minValue, maxValue));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param entityName
	 * @param value
	 * @param minValue
	 * @param maxValue
	 */
	public static void validateInRange(final String entityName, final double value,
			final double minValue, final double maxValue)
	{
		try
		{
			isTrue((value >= minValue) && (value <= maxValue), notInRange(entityName,
					value, minValue, maxValue));
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param condition
	 * @param message
	 */
	public static void validateIsTrue(final boolean condition, final String message)
	{
		try
		{
			isTrue(condition, message);
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

	/**
	 * @param severity
	 * @param condition
	 * @param message
	 */
	public static void validateIsTrue(final Severity severity, final boolean condition,
			final String message)
	{
		try
		{
			isTrue(condition, message);
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(severity, e.getMessage());
		}
	}

	/**
	 * @param condition
	 * @param message
	 */
	public static void validateIsFalse(final boolean condition, final String message)
	{
		try
		{
			isTrue(!condition, message);
		}
		catch (final IllegalArgumentException e)
		{
			throw new BusinessRuleException(e.getMessage());
		}
	}

}
