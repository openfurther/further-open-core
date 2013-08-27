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

import static edu.utah.further.core.api.constant.Strings.NULL_TO_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static java.lang.String.format;

import java.io.Serializable;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Commonly used debugging messages, usually regarding exceptions and assertions.
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
@Api
@Utility
public final class Messages
{
	// ========================= PUBLIC CONSTANTS ==========================

	/**
	 * A standard name of a persistent entity's identifier object.
	 */
	public static final String ID_NAME = "ID";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private Messages()
	{
		preventUtilityConstruction();
	}

	// ========================= PRIVATE CONSTANTS =========================

	private static final String NOT_NULL_MESSAGE = "%s cannot be null";

	private static final String NOT_EMPTY_MESSAGE = "%s cannot be empty";

	private static final String NOT_FOUND_MESSAGE = "%s not found";

	private static final String NOT_WIRED_MESSAGE = "%s: dependency not properly wired";

	private static final String FAILED_MESSAGE = "%s failed: %s";

	private static final String ILLEGAL_VALUE_MESSAGE1 = "Illegal %s";

	private static final String ILLEGAL_VALUE_MESSAGE2 = "Illegal %s: %s";

	private static final String ILLEGAL_ARGUMENT_MESSAGE = "%s: argument \"%s\" has an illegal value: %s";

	private static final String NOT_IN_RANGE_INT = "Invalid value %d for %s: must be in range [%d,%d]";

	private static final String NOT_IN_RANGE_DOUBLE = "Invalid value %f for %s: must be in range [%f,%f]";

	private static final String UNSUPPORTED_MESSAGE = "Unsupported %s: %s";

	private static final String UNSUPPORTED_OPERATION_MESSAGE = "Unsupported operation: %s";

	private static final String UNSUPPORTED_MUTABLE_OPERATION_MESSAGE = "Unsupported mutable operation on an immutable type: %s";

	private static final String ALREADY_A_MEMBER_OF_MESSAGE = "Cannot add %s to %s: %s is already a member of %s";

	private static final String CANNOT_ADD_MESSAGE = "Cannot add %s to %s: %s";

	private static final String CANNOT_CLONE_MESSAGE = "Cannot clone object of type %s: %s";

	private static final String CANNOT_GENERATE_MESSAGE = "Cannot generate %s: %s";

	private static final String CANNOT_CONVERT_MESSAGE = "Cannot convert %s to %s";

	private static final String CANNOT_COMPARE_MESSAGE = "Cannot compare %s with %s";

	private static final String CREATE_MESSAGE = "Creating %s: email=\"%s\"";

	private static final String CREATE_UNDER_MESSAGE = "Created under \"%s\"";

	private static final String CREATE_DEMO_MESSAGE = "Creating demo %s: name=\"%s\"";

	private static final String READ_MESSAGE = "Reading %s: id=%d, name=\"%s\"";

	private static final String READ_INFO_MESSAGE = "Reading into %s: id=%d, name=\"%s\"";

	private static final String UPDATE_MESSAGE = "Updating %s: email=\"%s\"";

	private static final String MERGE_MESSAGE = "Merging %s: \"%s\"";

	private static final String COPIED_MESSAGE = "Copied from \"%s\"";

	private static final String MOVED_MESSAGE = "Moved from \"%s\" to \"%s\"";

	private static final String MOVED_CHILD_MESSAGE = "Moved child #%d to %d";

	private static final String CANNOT_UPDATE_MESSAGE = "Cannot update %s \"%s\": %s";

	private static final String DELETE_MESSAGE = "Deleting %s \"%s\"";

	private static final String FIND_ALL_MESSAGE = "Find all %ss";

	private static final String FIND_BY_ID_MESSAGE = "Find %s by id: %d";

	private static final String SET_UP_MESSAGE = "Setting up";

	private static final String TEAR_DOWN_MESSAGE = "Tearing down";

	private static final String INPUT_OUTPUT_ERROR_MESSAGE = "I/O error %s: %s";

	// ========================= MESSAGE METHODS ===========================

	/**
	 * @param objectName
	 * @return constructed message
	 */
	public static String notNullMessage(final String objectName)
	{
		return format(NOT_NULL_MESSAGE, objectName);
	}

	/**
	 * @param collectionName
	 * @return constructed message
	 */
	public static String notEmptyMessage(final String collectionName)
	{
		return format(NOT_EMPTY_MESSAGE, collectionName);
	}

	/**
	 * @param objectName
	 * @return constructed message
	 */
	public static String notFoundMessage(final String objectName)
	{
		return format(NOT_FOUND_MESSAGE, objectName);
	}

	/**
	 * @param objectName
	 * @return constructed message
	 */
	public static String notWiredMessage(final String objectName)
	{
		return format(NOT_WIRED_MESSAGE, objectName);
	}

	/**
	 * @param valueName
	 * @param reason
	 * @return constructed message
	 */
	public static String failedMessage(final String valueName, final String reason)
	{
		return format(FAILED_MESSAGE, valueName, reason);
	}

	/**
	 * @param valueName
	 * @return constructed message
	 */
	public static String illegalValueMessage(final String valueName)
	{
		return format(ILLEGAL_VALUE_MESSAGE1, valueName);
	}

	/**
	 * @param property
	 * @return constructed message
	 */
	public static String illegalValueMessage(final String property, final Object value)
	{
		return format(ILLEGAL_VALUE_MESSAGE2, property, value);
	}

	/**
	 * @param methodName
	 * @param argumentName
	 * @return constructed message
	 */
	public static String illegalArgumentMessage(final String methodName,
			final String argumentName, final Object argumentValue)
	{
		return format(ILLEGAL_ARGUMENT_MESSAGE, methodName, argumentName, argumentValue
				.toString());
	}

	/**
	 * @param name
	 * @param value
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static String notInRange(final Object name, final int value,
			final int minValue, final int maxValue)
	{
		return format(NOT_IN_RANGE_INT, new Integer(value), name, new Integer(minValue),
				new Integer(maxValue));
	}

	/**
	 * @param name
	 * @param value
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static String notInRange(final Object name, final double value,
			final double minValue, final double maxValue)
	{
		return format(NOT_IN_RANGE_DOUBLE, new Double(value), name, new Double(minValue),
				new Double(maxValue));
	}

	/**
	 * @param object
	 * @return constructed message
	 */
	public static String unsupportedMessage(final Object object)
	{
		return format(UNSUPPORTED_MESSAGE, getClassNameNullSafe(object), object
				.toString());
	}

	/**
	 * @param operationName
	 * @return constructed message
	 */
	public static String unsupportedOperationMessage(final String operationName)
	{
		return format(UNSUPPORTED_OPERATION_MESSAGE, operationName);
	}

	/**
	 * @param operationName
	 * @return constructed message
	 */
	public static String unsupportedMutableOperationMessage(final String operationName)
	{
		return format(UNSUPPORTED_MUTABLE_OPERATION_MESSAGE, operationName);
	}

	/**
	 * @param memberEntityName
	 * @param collectionEntityName
	 * @param memberName
	 * @param collectionName
	 * @return constructed message
	 */
	public static String alreadyAMemberOfMessage(final String memberEntityName,
			final Object memberName, final String collectionEntityName,
			final Object collectionName)
	{
		return format(ALREADY_A_MEMBER_OF_MESSAGE, memberEntityName.toString(),
				collectionEntityName.toString(), memberName.toString(), collectionName
						.toString());
	}

	/**
	 * @param object
	 * @param collection
	 * @param reason
	 * @return constructed message
	 */
	public static String cannotAddMessage(final Object child, final Object collection,
			final String reason)
	{
		return format(CANNOT_ADD_MESSAGE, child, collection, reason);
	}

	/**
	 * @param objectName
	 * @param reason
	 * @return constructed message
	 */
	public static String cannotGenerateMessage(final String objectName,
			final String reason)
	{
		return format(CANNOT_GENERATE_MESSAGE, objectName, reason);
	}

	/**
	 * @param object
	 * @param reason
	 * @return constructed message
	 */
	public static String cannotCloneMessage(final Object object, final String reason)
	{
		return format(CANNOT_CLONE_MESSAGE, object.getClass().getCanonicalName(), reason);
	}

	/**
	 * @param source
	 * @param destination
	 * @return constructed message
	 */
	public static String cannotConvertMessage(final String source,
			final String destination)
	{
		return format(CANNOT_CONVERT_MESSAGE, source, destination);
	}

	/**
	 * @param object1Name
	 * @param object2Name
	 * @return constructed message
	 */
	public static String cannotCompareMessage(final Object object1Name,
			final Object object2Name)
	{
		return format(CANNOT_COMPARE_MESSAGE, object1Name.toString(), object2Name
				.toString());
	}

	/**
	 * @param object
	 * @return constructed message
	 */
	public static String createMessage(final Object object)
	{
		return format(CREATE_MESSAGE, getClassNameNullSafe(object),
				getNameNullSafe(object));
	}

	/**
	 * @param entityClass
	 * @param objectName
	 * @return constructed message
	 */
	public static String createUnderMessage(final Object object)
	{
		return format(CREATE_UNDER_MESSAGE, getNameNullSafe(object));
	}

	/**
	 * @param object
	 * @return constructed message
	 */
	public static String createDemoMessage(final Named object)
	{
		return format(CREATE_DEMO_MESSAGE, getClassNameNullSafe(object), object.getName());
	}

	/**
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T extends PersistentEntity<?>> String readMessage(final T object)
	{
		return format(READ_MESSAGE, getClassNameNullSafe(object), object.getId(),
				getNameNullSafe(object));
	}

	/**
	 * @param <T>
	 * @param id
	 * @param object
	 * @return
	 */
	public static <T extends PersistentEntity<?>> String readIntoMessage(Object id,
			final T object)
	{
		return format(READ_INFO_MESSAGE, getClassNameNullSafe(object), id,
				getNameNullSafe(object));
	}

	/**
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T extends PersistentEntity<?>> String updateMessage(final T object)
	{
		return format(UPDATE_MESSAGE, getClassNameNullSafe(object),
				getNameNullSafe(object));
	}

	/**
	 * @param entityClass
	 * @param objectName
	 * @return constructed message
	 */
	public static String mergeMessage(final Class<?> entityClass, final String objectName)
	{
		return format(MERGE_MESSAGE, entityClass.getSimpleName(), objectName);
	}

	/**
	 * @param from
	 * @param to
	 * @return constructed message
	 */
	public static String copiedMessage(final String from, final String to)
	{
		return format(COPIED_MESSAGE, from, to);
	}

	/**
	 * @param from
	 * @param to
	 * @return constructed message
	 */
	public static String movedMessage(final String from, final String to)
	{
		return format(MOVED_MESSAGE, from, to);
	}

	/**
	 * @param from
	 * @param to
	 * @return constructed message
	 */
	public static String movedChildMessage(final int from, final int to)
	{
		return format(MOVED_CHILD_MESSAGE, new Integer(from), new Integer(to));
	}

	/**
	 * @param object
	 * @param reason
	 * @return constructed message
	 */
	public static String cannotUpdateMessage(final Class<?> entityClass,
			final String objectName, final String reason)
	{
		return format(CANNOT_UPDATE_MESSAGE, entityClass.getSimpleName(), objectName,
				reason);
	}

	/**
	 * @param object
	 * @return constructed message
	 */
	public static String deleteMessage(final Object object)
	{
		return format(DELETE_MESSAGE, getClassNameNullSafe(object),
				getNameNullSafe(object));
	}

	/**
	 * @param entityClass
	 * @return constructed message
	 */
	public static String findAllMessage(final Class<?> entityClass)
	{
		return format(FIND_ALL_MESSAGE, entityClass.getSimpleName());
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return constructed message
	 */
	public static <T extends PersistentEntity<ID>, ID extends Comparable<ID> & Serializable> String findByIdMessage(
			final Class<T> entityClass, final ID id)
	{
		return format(FIND_BY_ID_MESSAGE, entityClass.getSimpleName(), id);
	}

	/**
	 * @return constructed message
	 */
	public static String setUpMessage()
	{
		return format(SET_UP_MESSAGE);
	}

	/**
	 * @return constructed message
	 */
	public static String tearDownMessage()
	{
		return format(TEAR_DOWN_MESSAGE);
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return constructed message
	 */
	public static String inputOutputErrorMessage(final String duringOperation,
			final String reason)
	{
		return format(INPUT_OUTPUT_ERROR_MESSAGE, duringOperation, reason);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return an item's name, or <code>NULL_TO_STRING</code>, if the item is
	 * <code>null</code>.
	 * 
	 * @param item
	 *            a named item
	 * @return the item's name, or <code>NULL_TO_STRING</code>, if the item is
	 *         <code>null</code>
	 */
	private static String getClassNameNullSafe(final Object item)
	{
		return (item == null) ? NULL_TO_STRING : item.getClass().getSimpleName();
	}

	/**
	 * Return an item's name, if it implements {@link Named}; or its string
	 * representation, if not; or <code>NULL_TO_STRING</code>, if the item is
	 * <code>null</code>.
	 * 
	 * @param item
	 *            a named item
	 * @return the item's name, or <code>NULL_TO_STRING</code>, if the item is
	 *         <code>null</code>
	 */
	private static String getNameNullSafe(final Object item)
	{
		if (item == null)
		{
			return NULL_TO_STRING;
		}
		return ReflectionUtil.instanceOf(item, Named.class) ? ((Named) item).getName()
				: item.toString();
	}
}
