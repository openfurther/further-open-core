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
package edu.utah.further.core.api.collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import edu.utah.further.core.api.discrete.HasIdentifier;

/**
 * A use case of a generic bounded wildcard type with multiple super-interfaces. To use
 * both super-interfaces using the same type parameter in client code, an interface
 * extending both of them must be written.
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
 * @version Oct 2, 2009
 */
public final class UTestMultiExtendsUseCase
{
	// ========================= CONSTANTS =================================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test a client that needs a list whose type extends two interfaces.
	 */
	@Test
	public void getMultiExtendsWildCardType()
	{
		// Doesn't compile: "Syntax error on token "&", , expected
		// List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>> list1 =
		// getSomeList();

		// OK
		final List<? extends EmbeddedId<?, ?>> list2 = getSomeList();
		assertEmpty(list2);
	}

	/**
	 * The same as {@link #getMultiExtendsWildCardType()}, with the addition of casting
	 * the result of the generic method {@link #getSomeResult()} to a list.
	 */
	@Test
	public void getMultiExtendsWildCardTypeWithListCast()
	{
		// Doesn't compile: "Syntax error on token "&", , expected
		// List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>> list1 =
		// getSomeResult();

		// OK
		final List<? extends EmbeddedId<?, ?>> list = getSomeResult();
		assertNull(list);
	}

	/**
	 * Test casting a list element from a generic type that extends two interfaces to one
	 * of the interfaces.
	 */
	@Test
	@SuppressWarnings(
	{ "unchecked" })
	public void cannotCastFromTwoSuperInterfacesToOneOfThem()
	{
		final List<ConcreteEmbeddedId<Long, Long>> list = (List<ConcreteEmbeddedId<Long, Long>>) getListOfElementsExtendingBothInterfaces();

		// Fine as long as we don't access elements of the list
		assertNotEmpty(list);

		// OK because we implement each super interface separately
		final HasIdentifier<Long> element = list.get(0);
		assertNotNull(element);
	}

	/**
	 * Suppose a list element implements two interfaces separately; show that casting it
	 * to an interface that implements the same two interfaces fails, because it is not
	 * directly implemented by the element.
	 */
	@Test(expected = ClassCastException.class)
	@SuppressWarnings(
	{ "unchecked" })
	public void cannotCastFromTwoSuperInterfacesToAnInterfaceThatExtendsBoth()
	{
		final List<ConcreteEmbeddedId<Long, Long>> list = (List<ConcreteEmbeddedId<Long, Long>>) getListOfElementsExtendingBothInterfaces();

		// Fine to access list methods as long as we don't access list elements
		assertNotEmpty(list);

		// Not OK because the element type does not directly implement EmbeddedId, even
		// though it contains all the methods to do so. The following line therefore
		// throws ClassCastException.
		@SuppressWarnings("unused")
		final ConcreteEmbeddedId<Long, Long> element = list.get(0);
	}

	/**
	 * Use our collections decorator API. No unchecked warnings generated here!
	 */
	@Test
	public void newMultiExtendsWildCardTypeUsingListDecorator()
	{
		// Behaves like List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		final ConcreteListDecorator<?> decorator = ConcreteListDecorator
				.newInstance(getSomeList());

		// OK
		assertEmpty(decorator.list);
	}

	/**
	 * Use our collections decorator API. No unchecked warnings generated here!
	 */
	@Test
	@SuppressWarnings(
	{ "boxing" })
	public void useMultiExtendsWildCardTypeUsingListDecorator()
	{
		// Behaves like List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		final ConcreteListDecorator<?> decorator = ConcreteListDecorator
				.newInstance(getListOfElementsExtendingBothInterfaces());

		// Call a list method - OK
		assertNotEmpty(decorator.list);

		// We get a JavaBeans-compliant size method for the same price
		assertThat(decorator.getSize(), is(1));

		// Cast list element to interface #1 - OK
		final HasIdentifier<?> element1 = decorator.list.get(0);
		assertNull(element1.getId());

		// Cast list element to interface #2 - OK
		final SomeQueryIdentifier<?> element2 = decorator.list.get(0);
		assertNull(element2.getQueryId());
	}

	/**
	 * Note that one can pass any <code>List<?></code> to {@link ConcreteListDecorator}.
	 */
	@Test
	public void listDecoratorIsNotCompletelyTypeSafe()
	{
		// Behaves like List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		final ConcreteListDecorator<?> decorator = ConcreteListDecorator
				.newInstance(getSomeList());

		// Call a list method - OK
		assertEmpty(decorator.list);
	}

	/**
	 * Use our collections {@link List2} API.
	 */
	@Test
	public void useMultiExtendsWildCardTypeUsingList2ArgumentOfType1()
	{
		// Behaves like List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		final List2<HasIdentifier<?>, SomeQueryIdentifier<?>> decorator = List2
				.newInstance1(getListOfElementsIfType1());

		useMultiExtendsWildCardTypeUsingList2(decorator);
	}

	/**
	 * Use our collections {@link List2} API.
	 */
	@Test
	public void useMultiExtendsWildCardTypeUsingList2ArgumentOfType2()
	{
		// Behaves like List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		final List2<HasIdentifier<?>, SomeQueryIdentifier<?>> decorator = List2
				.newInstance2(getListOfElementsIfType2());

		useMultiExtendsWildCardTypeUsingList2(decorator);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Assert that a collection size equals an expected size.
	 * 
	 * @param collection
	 *            collection
	 * @param expectedSize
	 *            expected
	 */
	@SuppressWarnings("boxing")
	private static void assertSizeEquals(final Collection<?> collection,
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
	private static void assertEmpty(final Collection<?> collection)
	{
		assertTrue(collection.isEmpty());
	}

	/**
	 * Assert that a collection is not empty.
	 * 
	 * @param collection
	 *            collection
	 */
	private static void assertNotEmpty(final Collection<?> collection)
	{
		assertFalse(collection.isEmpty());
	}

	/**
	 * Test our collections {@link List2} API. No unchecked warnings generated here!
	 * 
	 * @param decorator
	 *            a {@link List2}-type list decorator
	 */
	private void useMultiExtendsWildCardTypeUsingList2(
			final List2<HasIdentifier<?>, SomeQueryIdentifier<?>> decorator)
	{
		// Call a list method - OK
		assertNotEmpty(decorator.list1);
		assertSizeEquals(decorator.list1, 1);

		// Cast list element to interface #1 - OK
		final HasIdentifier<?> element1 = decorator.list1.get(0);
		assertNull(element1.getId());

		// Cast list element to interface #2 - OK
		final SomeQueryIdentifier<?> element2 = decorator.list2.get(0);
		assertNull(element2.getQueryId());
	}

	/**
	 * A wildcard-capturing generic method. Simulates a Hibernate DAO call.
	 * 
	 * @param <T>
	 * @return
	 */
	private <T> T getSomeResult()
	{
		// The only thing that can be cast to any thing
		return null;
	}

	/**
	 * A wildcard-capturing generic method. Simulates a Hibernate DAO call.
	 * 
	 * @param <T>
	 * @return
	 */
	private <T> List<T> getSomeList()
	{
		return CollectionUtil.<T> newList();
	}

	/**
	 * A wildcard-capturing generic method. Simulates a Hibernate DAO call.
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("synthetic-access")
	private List<? extends HasIdentifier<?>> getListOfElementsIfType1()
	{
		return Collections
				.<HasIdentifier<?>> singletonList(new ConcreteExtendedOfBothInterfaces<Long, Long>());
	}

	/**
	 * A wildcard-capturing generic method. Simulates a Hibernate DAO call.
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("synthetic-access")
	private List<? extends SomeQueryIdentifier<?>> getListOfElementsIfType2()
	{
		return Collections
				.<SomeQueryIdentifier<?>> singletonList(new ConcreteExtendedOfBothInterfaces<Long, Long>());
	}

	/**
	 * A wildcard-capturing generic method. Simulates a Hibernate DAO call.
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("synthetic-access")
	private List<? extends ConcreteExtendedOfBothInterfaces<Long, Long>> getListOfElementsExtendingBothInterfaces()
	{
		return Collections
				.<ConcreteExtendedOfBothInterfaces<Long, Long>> singletonList(new ConcreteExtendedOfBothInterfaces<Long, Long>());
	}

	/**
	 * An interface that extends two super-interfaces.
	 */
	private interface EmbeddedId<ID1 extends Comparable<ID1> & Serializable, ID2 extends Comparable<ID2>>
			extends HasIdentifier<ID1>, SomeQueryIdentifier<ID2>
	{

	}

	/**
	 * An implementation of {@link EmbeddedId}. Also implements the two-super interfaces
	 * by indirect inheritance.
	 */
	@SuppressWarnings("synthetic-access")
	private class ConcreteEmbeddedId<ID1 extends Comparable<ID1> & Serializable, ID2 extends Comparable<ID2>>
			extends ConcreteExtendedOfBothInterfaces<ID1, ID2> implements
			EmbeddedId<ID1, ID2>
	{

	}

	/**
	 * Implements each of the super-interface directly, but does not implement
	 * {@link EmbeddedId} even though it contains the implementation to do so.
	 */
	private class ConcreteExtendedOfBothInterfaces<ID1 extends Comparable<ID1> & Serializable, ID2 extends Comparable<ID2>>
			implements HasIdentifier<ID1>, SomeQueryIdentifier<ID2>
	{
		/**
		 * @return
		 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
		 */
		@Override
		public ID1 getId()
		{
			return null;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.collections.SomeQueryIdentifier#getQueryId()
		 */
		@Override
		public ID2 getQueryId()
		{
			return null;
		}

		/**
		 * @param queryId
		 * @see edu.utah.further.core.api.collections.SomeQueryIdentifier#setQueryId(java.lang.Comparable)
		 */
		@Override
		public void setQueryId(final ID2 queryId)
		{
			// Method stub
		}

	}
}
