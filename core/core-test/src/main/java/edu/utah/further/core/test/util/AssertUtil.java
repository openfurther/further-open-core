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
package edu.utah.further.core.test.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.util.MathUtils;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.ThrowingRunnable;
import edu.utah.further.core.api.math.TolerantlyEquals;

/**
 * Test assertion utilities.
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
 * @version Mar 23, 2009
 */
@Utility
public final class AssertUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssertUtil.class);

	// ========================= NESTED TYPES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private AssertUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Assert that two values are equal up to a tolerance.
	 * 
	 * @param expected
	 *            expected result
	 * @param actual
	 *            actual result
	 * @param maxUlps
	 *            tolerance in ULPs
	 */
	public static <T extends TolerantlyEquals<T>> void assertTolerantlyEquals(
			final T expected, final T actual, final int maxUlps)
	{
		assertTrue("Expected " + expected + ", actual " + actual,
				expected.tolerantlyEquals(actual, maxUlps));
	}

	/**
	 * Assert that two double-precision values are equal up to a tolerance.
	 * 
	 * @param expected
	 *            expected result
	 * @param actual
	 *            actual result
	 * @param maxUlps
	 *            tolerance in ULPs
	 */
	public static void assertTolerantlyEquals(final double expected, final double actual,
			final int maxUlps)
	{
		assertTrue("Expected " + expected + ", actual " + actual,
				MathUtils.equals(expected, actual, maxUlps));
	}

	/**
	 * Assert that two collections contain the same elements up to ordering.
	 * 
	 * @param <T>
	 *            element type
	 * @param expected
	 *            expected collection
	 * @param actual
	 *            actual collection
	 */
	public static <T> void assertCollectionsEqualInAnyOrder(
			final Collection<? extends T> expected, final Collection<? extends T> actual)
	{
		assertEquals(
				"Expected size " + expected.size() + ", actual size " + actual.size(),
				expected.size(), actual.size());
		assertTrue("Expected collection " + expected + ", actual collcetion " + actual,
				expected.containsAll(actual));
	}

	/**
	 * Check results; save any exception thrown so that we can print bodies for debugging
	 * purposes (they seem to be printed correctly only after asserts are executed), then
	 * throw the exception.
	 * 
	 * @param mainCode
	 *            main code
	 * @param postProcessingCode
	 *            post-processing code. Does not throw checked exceptions
	 * @throws Throwable
	 */
	public static void failAfterPostProcessing(final ThrowingRunnable mainCode,
			final Runnable postProcessingCode) throws Throwable
	{
		// Run main code; save any exception thrown
		Throwable ex = null;
		try
		{
			mainCode.run();
		}
		catch (final Throwable e)
		{
			ex = e;
		}

		// Usually some useful debugging printouts / cleanups
		postProcessingCode.run();

		// Now throw exception
		if (ex != null)
		{
			throw ex;
		}
	}

	/**
	 * Assert that a collection size equals an expected size.
	 * 
	 * @param collection
	 *            collection
	 * @param expectedSize
	 *            expected
	 */
	@SuppressWarnings("boxing")
	public static void assertSizeEquals(final Collection<?> collection,
			final int expectedSize)
	{
		assertThat(collection.size(), is(expectedSize));
	}

	/**
	 * Assert that a collection is empty.
	 * 
	 * @param collection
	 *            collection
	 */
	public static void assertEmpty(final Collection<?> collection)
	{
		assertTrue(collection.isEmpty());
	}

	/**
	 * Assert that a collection is not empty.
	 * 
	 * @param collection
	 *            collection
	 */
	public static void assertNotEmpty(final Collection<?> collection)
	{
		assertFalse(collection.isEmpty());
	}

	/**
	 * Takes an initializer an attribute name and asserts that it receives the expected
	 * value from the initializer.
	 * 
	 * @param initializer
	 * @param key
	 * @param expectedValue
	 */
	public static void assertAttributeEquals(final AttributeContainer initializer,
			final Labeled key, final Object expectedValue)
	{
		final Object attribute = initializer.getAttribute(key);
		final Object cast = expectedValue.getClass().cast(attribute);
		assertThat(cast, is(expectedValue));
	}

	/**
	 * Assert that Hibernate's DISTINCT_ROOT_ENTITY collocated root entities.
	 * 
	 * @param entities
	 *            persistent entity list
	 */
	public static void assertDistinctEntities(
			final List<? extends PersistentEntity<?>> entities)
	{
		assertTrue("DISTINCT_ROOT_ENTITY didn't collocate person entities correctly",
				isAllEntitiesDistinct(entities));
	}

	/**
	 * @param entities
	 * @return
	 */
	public static boolean isAllEntitiesDistinct(
			final List<? extends PersistentEntity<?>> entities)
	{
		final int numPersonEntities = entities.size();
		if (log.isDebugEnabled())
		{
			for (int i = 0; i < numPersonEntities; i++)
			{
				log.debug("Entity #" + i + " " + entities.get(i).getId());
			}
		}

		final Set<? extends PersistentEntity<?>> distinctEntities = CollectionUtil
				.newSet(entities);
		final boolean allEntitiesAreDistinct = (numPersonEntities == distinctEntities
				.size());
		return allEntitiesAreDistinct;
	}

	// ========================= PRIVATE METHODS ===================================
}
