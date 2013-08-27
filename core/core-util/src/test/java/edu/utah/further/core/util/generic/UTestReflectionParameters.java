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
package edu.utah.further.core.util.generic;

import static edu.utah.further.core.api.lang.ReflectionUtil.getInterfaceTypeArguments;
import static edu.utah.further.core.api.lang.ReflectionUtil.getSuperclassTypeArguments;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;

import com.google.common.collect.ForwardingMap;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Testing reflection parameter retrieval.
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
 * @version Nov 6, 2008
 */
public final class UTestReflectionParameters
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestReflectionParameters.class);

	// ========================= TYPES =====================================

	private static interface InterfaceUserType<T>
	{
	}

	private static abstract class AbstractUserType<T> implements InterfaceUserType<T>
	{
		AbstractUserType()
		{
		}

		public Class<?> returnedClass()
		{
			return getSuperclassTypeArguments(AbstractUserType.class, getClass()).get(0);
		}
	}

	private static class ConcreteExtendingUserType extends AbstractUserType<String>
	{
		ConcreteExtendingUserType()
		{
			super();
		}
	}

	private static class ConcreteImplementingUserType implements
			InterfaceUserType<String>
	{
	}

	// ========================= METHODS ===================================

	/**
	 * Test super-class type argument determination.
	 */
	@Test
	public void superClassTypeArguments()
	{
		final Class<MapLongString> childClass = MapLongString.class;
		final List<Class<?>> typeArguments = getSuperclassTypeArguments(ForwardingMap.class,
				childClass);
		log.debug("Class " + childClass + " typeArguments = " + typeArguments);
		assertEquals(asList(Long.class, String.class), typeArguments);
	}

	/**
	 * Test super-class type argument determination for an invalid super-class.
	 */
	@Test
	public void superClassTypeArgumentsReturnedByMethod()
	{
		final Class<?> returnedClass = new ConcreteExtendingUserType().returnedClass();
		log.debug("returnedClass = " + returnedClass);
		assertEquals(String.class, returnedClass);
	}

	/**
	 * Test super-class type argument determination in a method return type.
	 */
	@Test(expected = ApplicationException.class)
	public void invalidSuperClassTypeArguments()
	{
		final Class<ConcreteExtendingUserType> childClass = ConcreteExtendingUserType.class;
		// InterfaceUserType is not a super-class, but a super-interface, of
		// ConcreteUserType, so the problem is not caught at compile-time in the
		// restrictions on getSuperclassTypeArguments()'s argument types
		getSuperclassTypeArguments(InterfaceUserType.class, childClass);
	}

	/**
	 * Test super-interface type argument determination.
	 */
	@Test
	public void interfaceTypeArgumentsOfExtendingClass()
	{
		final Class<ConcreteExtendingUserType> childClass = ConcreteExtendingUserType.class;
		final List<Class<?>> typeArguments = getInterfaceTypeArguments(
				InterfaceUserType.class, childClass);
		log.debug("Class " + childClass + " typeArguments = " + typeArguments);
		assertEquals(asList(String.class), typeArguments);
	}

	/**
	 * Test super-interface type argument determination.
	 */
	@Test
	public void interfaceTypeArgumentsOfImplementingClass()
	{
		final Class<ConcreteImplementingUserType> childClass = ConcreteImplementingUserType.class;
		final List<Class<?>> typeArguments = getInterfaceTypeArguments(
				InterfaceUserType.class, childClass);
		log.debug("Class " + childClass + " typeArguments = " + typeArguments);
		assertEquals(asList(String.class), typeArguments);
	}
}
