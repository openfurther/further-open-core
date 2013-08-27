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

import static edu.utah.further.core.api.collections.CollectionUtil.ListType.ARRAY_LIST;
import static edu.utah.further.core.api.collections.CollectionUtil.MapType.HASH_MAP;
import static edu.utah.further.core.api.collections.CollectionUtil.MapType.TREE_MAP;
import static edu.utah.further.core.api.collections.CollectionUtil.SetType.HASH_SET;
import static edu.utah.further.core.api.collections.CollectionUtil.SetType.TREE_SET;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.discrete.HasIdentifier;

/**
 * Utilities related to collections and arrays.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Utility
@Api
public final class CollectionUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Make sure that we don't create a lot of useless objects when we call
	 * <code>toArray()</code> methods.
	 */
	public static final Integer[] EMPTY_INTEGER_ARRAY = new Integer[0];

	/**
	 * Make sure that we don't create a lot of useless objects when we call
	 * <code>toArray()</code> methods.
	 */
	public static final Long[] EMPTY_LONG_ARRAY = new Long[0];

	/**
	 * Make sure that we don't create a lot of useless objects when we call
	 * <code>toArray()</code> methods.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * The empty set (immutable). This set is serializable.
	 *
	 * @see #emptySet()
	 */
	@SuppressWarnings("rawtypes")
	public static final SortedSet EMPTY_SORTED_SET = CollectionUtil.newSortedSet();

	// ========================= NESTED TYPES ==============================

	/**
	 * Centralizes map implementation types.
	 */
	public enum MapType
	{
		/**
		 * The default implementation.
		 */
		HASH_MAP,

		LINKED_HASH_MAP,

		CONCURRENT_HASH_MAP,

		/**
		 * The default sorted set implementation.
		 */
		TREE_MAP;
	}

	/**
	 * Centralizes set implementation types.
	 */
	public enum SetType
	{
		/**
		 * The default implementation.
		 */
		HASH_SET,

		LINKED_HASH_SET,

		/**
		 * A concurrent hash set. Backed by an underlying concurrent hash map.
		 */
		CONCURRENT_HASH_SET,

		/**
		 * The default sorted set implementation.
		 */
		TREE_SET;
	}

	public enum ListType
	{
		/**
		 * The default implementation.
		 */
		ARRAY_LIST,

		LINKED_LIST;
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private CollectionUtil()
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
	public static <E extends Enum<E>> E valueOf(final Class<E> enumType, final String name)
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
	 * Converts a variable argument list to a set.
	 *
	 * @param <E>
	 * @param element
	 *            variable-length argument array of elements
	 * @return hash set with the elements
	 */
	@SafeVarargs
	// Safe because asList is safe
	public static <E> Set<E> toSet(final E... element)
	{
		return new HashSet<>(asList(element));
	}

	/**
	 * Converts a variable argument list to a sorted set.
	 *
	 * @param <E>
	 * @param element
	 *            variable-length argument array of elements
	 * @return hash set with the elements
	 */
	@SafeVarargs
	// Safe because asList is safe
	public static <E> SortedSet<E> toSortedSet(final E... element)
	{
		return new TreeSet<>(asList(element));
	}

	/**
	 * Convert an integer array to a list.
	 *
	 * @param array
	 *            integer array
	 * @return hash set with the elements
	 */
	public static List<Integer> toList(final int[] array)
	{
		final List<Integer> list = newList();
		for (final int element : array)
		{
			list.add(new Integer(element));
		}
		return list;
	}

	/**
	 * Convert an array to a list.
	 *
	 * @param array
	 *            integer array
	 * @return hash set with the elements
	 */
	public static <E> List<E> toList(final E[] array)
	{
		final List<E> list = newList();
		for (final E element : array)
		{
			list.add(element);
		}
		return list;
	}

	/**
	 * Truncate a list to a size by removing elements at its end, if necessary.
	 *
	 * @param list
	 *            a list
	 * @param newSize
	 *            desired size
	 */
	public static void truncate(final List<?> list, final int newSize)
	{
		final int currentSize = list.size();
		for (int i = currentSize - 1; i >= newSize; i--)
		{
			list.remove(i);
		}
	}

	/**
	 * Convert a list to a delimited string.
	 *
	 * @param list
	 *            a list
	 * @param delimiter
	 *            desired delimited between elements
	 * @return delimited list string
	 */
	public static String printDelimited(final List<?> list, final String delimiter)
	{
		final StringBuilder s = new StringBuilder("");
		final int size = list.size();
		for (int i = 0; i < size; i++)
		{
			s.append(list.get(i));
			if (i < size - 1)
			{
				s.append(delimiter);
			}
		}
		return s.toString();
	}

	/**
	 * Convert a persistent entity list to an ID list.
	 *
	 * @param list
	 *            a list of persistent entities
	 * @return entity ID list
	 */
	public static <ID extends Comparable<ID> & Serializable> List<ID> toIdList(
			final List<? extends HasIdentifier<ID>> list)
	{
		final List<ID> ids = newList();
		for (final HasIdentifier<ID> entity : list)
		{
			ids.add(entity.getId());
		}
		return ids;
	}

	/**
	 * Check if a collection is empty or <code>null</code>.
	 *
	 * @param <T>
	 *            collection element type
	 * @param collection
	 *            a collection
	 * @return <code>true</code> if and only if the collection is <code>null</code> or
	 *         empty
	 */
	public static <T> boolean isEmpty(final Collection<T> collection)
	{
		return ((collection == null) || collection.isEmpty());
	}

	/**
	 * Return a list, or an empty list if the list is null.
	 *
	 * @param <T>
	 * @param list
	 *            list to warrant
	 * @return list, or an empty list if the list is null
	 */
	public static <T> List<T> getNullSafeList(final List<T> list)
	{
		return (list == null) ? CollectionUtil.<T> newList() : list;
	}

	/**
	 * Return a copy of a list, or an empty list if the list is empty null.
	 *
	 * @param <T>
	 * @param list
	 *            list to warrant
	 * @return list copy, or a <code>null</code> list if list is empty
	 */
	public static <T> List<T> copyListOrNull(final List<T> list)
	{
		return isEmpty(list) ? null : CollectionUtil.<T> newList(list);
	}

	/**
	 * Does a list start with a sub-list. If either list is <code>null</code>, returns
	 * <code>false</code>.
	 *
	 * @param <T>
	 * @param list
	 *            a list
	 * @param subList
	 *            sub-list
	 * @return <code>true</code> if and only if list starts with subList
	 */
	public static <T> boolean startsWith(final List<? extends T> list,
			final List<? extends T> subList)
	{
		if ((list == null) || (subList == null) || (list.size() < subList.size()))
		{
			return false;
		}
		return list.subList(0, subList.size()).equals(subList);
	}

	/**
	 * A generic factory method of hash sets.
	 *
	 * @param <E>
	 *            set element type
	 * @return an empty hash set
	 */
	public static <E> Set<E> newSet()
	{
		return newSet(HASH_SET);
	}

	/**
	 * A factory method of sets.
	 *
	 * @param <E>
	 *            set element type
	 * @param setType
	 *            set implementation identifier
	 * @return an empty set
	 */
	public static <E> Set<E> newSet(final SetType setType)
	{
		switch (setType)
		{
			case HASH_SET:
			{
				return new HashSet<>();
			}

			case LINKED_HASH_SET:
			{
				return new LinkedHashSet<>();
			}

			case CONCURRENT_HASH_SET:
			{
				return Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>());
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized set implementation: " + setType);
			}
		}
	}

	/**
	 * A generic factory method of hash sets. Constructs a new set containing the elements
	 * in the specified collection. The <tt>HashMap</tt> is created with default load
	 * factor (0.75) and an initial capacity sufficient to contain the elements in the
	 * specified collection.
	 *
	 * @param c
	 *            the collection whose elements are to be placed into this set
	 * @throws NullPointerException
	 *             if the specified collection is null
	 * @param <E>
	 *            set element type
	 * @return a hash set
	 */
	public static <E> Set<E> newSet(final Collection<? extends E> c)
	{
		return newSet(HASH_SET, c);
	}

	/**
	 * A generic factory method of sets. Constructs a new set containing the elements in
	 * the specified collection. The <tt>HashMap</tt> is created with default load factor
	 * (0.75) and an initial capacity sufficient to contain the elements in the specified
	 * collection.
	 *
	 * @param setType
	 *            set implementation identifier
	 * @param c
	 *            the collection whose elements are to be placed into this set
	 * @throws NullPointerException
	 *             if the specified collection is null
	 * @param <E>
	 *            set element type
	 * @return a set
	 */
	public static <E> Set<E> newSet(final SetType setType, final Collection<? extends E> c)
	{
		switch (setType)
		{
			case HASH_SET:
			{
				return new HashSet<>(c);
			}

			case LINKED_HASH_SET:
			{
				return new LinkedHashSet<>(c);
			}

			case CONCURRENT_HASH_SET:
			{
				final Set<E> set = CollectionUtil.newSet(setType);
				set.addAll(c);
				return set;
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized set implementation: " + setType);
			}
		}
	}

	/**
	 * Returns the empty sorted set (immutable). Unlike the like-named field, this method
	 * is parameterized.
	 *
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty set:
	 *
	 * <pre>
	 * Set&lt;String&gt; s = Collections.emptySortedSet();
	 * </pre>
	 *
	 * Implementation note: Implementations of this method need not create a separate
	 * <tt>SortedSet</tt> object for each call. Using this method is likely to have
	 * comparable cost to using the like-named field. (Unlike this method, the field does
	 * not provide type safety.)
	 *
	 * @see Collections#EMPTY_SET
	 */
	@SuppressWarnings("unchecked")
	public static final <T> SortedSet<T> emptySortedSet()
	{
		return EMPTY_SORTED_SET;
	}

	/**
	 * A generic factory method of tree sets.
	 *
	 * @param <E>
	 *            set element type
	 * @return an empty tree set
	 */
	public static <E> SortedSet<E> newSortedSet()
	{
		return newSortedSet(TREE_SET);
	}

	/**
	 * Constructs a new, empty sorted set, sorted according to the natural ordering of its
	 * elements. All elements inserted into the set must implement the {@link Comparable}
	 * interface. Furthermore, all such elements must be <i>mutually comparable</i>:
	 * {@code e1.compareTo(e2)} must not throw a {@code ClassCastException} for any
	 * elements {@code e1} and {@code e2} in the set. If the user attempts to add an
	 * element to the set that violates this constraint (for example, the user attempts to
	 * add a string element to a set whose elements are integers), the {@code add} call
	 * will throw a {@code ClassCastException}.
	 *
	 * @param setType
	 *            set implementation identifier
	 * @return empty sorted set
	 */
	public static <E> SortedSet<E> newSortedSet(final SetType setType)
	{
		switch (setType)
		{
			case TREE_SET:
			{
				return new TreeSet<>();
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted set implementation: " + setType);
			}
		}
	}

	/**
	 * A generic factory method of hash sets. Constructs a new set containing the elements
	 * in the specified collection. The <tt>HashMap</tt> is created with default load
	 * factor (0.75) and an initial capacity sufficient to contain the elements in the
	 * specified collection.
	 *
	 * @param c
	 *            the collection whose elements are to be placed into this set
	 * @throws NullPointerException
	 *             if the specified collection is null
	 * @param <E>
	 *            set element type
	 * @return a hash set
	 */
	public static <E> SortedSet<E> newSortedSet(final SetType setType,
			final Collection<? extends E> c)
	{
		switch (setType)
		{
			case TREE_SET:
			{
				return new TreeSet<>(c);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted set implementation: " + setType);
			}
		}
	}

	/**
	 * A generic factory method of sorted sets. Constructs a new set containing the
	 * elements in the specified collection. The <tt>HashMap</tt> is created with default
	 * load factor (0.75) and an initial capacity sufficient to contain the elements in
	 * the specified collection.
	 *
	 * @param c
	 *            the collection whose elements are to be placed into this set
	 * @throws NullPointerException
	 *             if the specified collection is null
	 * @param <E>
	 *            set element type
	 * @return a hash set
	 */
	public static <E> SortedSet<E> newSortedSet(final Collection<? extends E> c)
	{
		return newSortedSet(TREE_SET, c);
	}

	/**
	 * Constructs a new, empty tree set, sorted according to the specified comparator. All
	 * elements inserted into the set must be <i>mutually comparable</i> by the specified
	 * comparator: {@code comparator.compare(e1, e2)} must not throw a
	 * {@code ClassCastException} for any elements {@code e1} and {@code e2} in the set.
	 * If the user attempts to add an element to the set that violates this constraint,
	 * the {@code add} call will throw a {@code ClassCastException}.
	 *
	 * @param comparator
	 *            the comparator that will be used to order this set. If {@code null}, the
	 *            {@linkplain Comparable natural ordering} of the elements will be used.
	 * @return empty tree set
	 */
	public static <E> SortedSet<E> newSortedSet(final Comparator<? super E> comparator)
	{
		return newSortedSet(TREE_SET, comparator);
	}

	/**
	 * Constructs a new, empty sorted set, sorted according to the specified comparator.
	 * All elements inserted into the set must be <i>mutually comparable</i> by the
	 * specified comparator: {@code comparator.compare(e1, e2)} must not throw a
	 * {@code ClassCastException} for any elements {@code e1} and {@code e2} in the set.
	 * If the user attempts to add an element to the set that violates this constraint,
	 * the {@code add} call will throw a {@code ClassCastException}.
	 *
	 * @param setType
	 *            set implementation identifier
	 * @param comparator
	 *            the comparator that will be used to order this set. If {@code null}, the
	 *            {@linkplain Comparable natural ordering} of the elements will be used.
	 * @return empty sorted set
	 */
	public static <E> SortedSet<E> newSortedSet(final SetType setType,
			final Comparator<? super E> comparator)
	{
		switch (setType)
		{
			case TREE_SET:
			{
				return new TreeSet<>(comparator);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted set implementation: " + setType);
			}
		}
	}

	/**
	 * A factory method of the default map implementation (hash map).
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @return an empty hash map
	 */
	public static <K, V> Map<K, V> newMap()
	{
		return newMap(HASH_MAP);
	}

	/**
	 * A factory method of maps.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            map implementation identifier
	 * @return an empty map
	 */
	public static <K, V> Map<K, V> newMap(final MapType mapType)
	{
		switch (mapType)
		{
			case HASH_MAP:
			{
				return new HashMap<>();
			}

			case LINKED_HASH_MAP:
			{
				return new LinkedHashMap<>();
			}

			case CONCURRENT_HASH_MAP:
			{
				return new ConcurrentHashMap<>();
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized map implementation: " + mapType);
			}
		}
	}

	/**
	 * A factory method of maps.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            map implementation identifier
	 * @return an empty map
	 */
	public static <K, V> ConcurrentMap<K, V> newConcurrentMap()
	{
		return new ConcurrentHashMap<>();
	}

	/**
	 * A factory method of the default map implementation (hash map). Constructs a new
	 * <tt>HashMap</tt> with the same mappings as the specified <tt>Map</tt>. The
	 * <tt>HashMap</tt> is created with default load factor (0.75) and an initial capacity
	 * sufficient to hold the mappings in the specified <tt>Map</tt>.
	 *
	 * @param m
	 *            the map whose mappings are to be placed in this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @return a hash map
	 */
	public static <K, V> Map<K, V> newMap(final Map<? extends K, ? extends V> m)
	{
		return newMap(HASH_MAP, m);
	}

	/**
	 * A factory method of maps. Constructs a new <tt>Map</tt> with the same mappings as
	 * the specified <tt>Map</tt>. The <tt>Map</tt> is created with default load factor
	 * (0.75) and an initial capacity sufficient to hold the mappings in the specified
	 * <tt>Map</tt>.
	 *
	 * @param m
	 *            the map whose mappings are to be placed in this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            sorted map implementation identifier
	 * @return an empty map
	 */
	public static <K, V> Map<K, V> newMap(final MapType mapType,
			final Map<? extends K, ? extends V> m)
	{
		switch (mapType)
		{
			case HASH_MAP:
			{
				return new HashMap<>(m);
			}

			case LINKED_HASH_MAP:
			{
				return new LinkedHashMap<>(m);
			}

			case CONCURRENT_HASH_MAP:
			{
				return new ConcurrentHashMap<>(m);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized map implementation: " + mapType);
			}
		}
	}

	/**
	 * A factory method of the sorted map default implementation (tree map).
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @return an empty tree map
	 */
	public static <K, V> SortedMap<K, V> newSortedMap()
	{
		return newSortedMap(TREE_MAP);
	}

	/**
	 * A factory method of sorted maps. The returned map is <i>not guranteed to be
	 * synchronized</i>.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            sorted map implementation identifier
	 * @return an empty sorted map
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final MapType mapType)
	{
		switch (mapType)
		{
			case TREE_MAP:
			{
				return new TreeMap<>();
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted map implementation: " + mapType);
			}
		}
	}

	/**
	 * Constructs a new tree map containing the same mappings and using the same ordering
	 * as the specified sorted map. This method runs in linear time.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param m
	 *            the tree map whose mappings are to be placed in this map, and whose
	 *            comparator is to be used to sort this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final SortedMap<K, ? extends V> m)
	{
		return newSortedMap(TREE_MAP, m);
	}

	/**
	 * Constructs a new sorted map containing the same mappings and using the same
	 * ordering as the specified sorted map. This method runs in linear time.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            sorted map implementation identifier
	 * @param m
	 *            the sorted map whose mappings are to be placed in this map, and whose
	 *            comparator is to be used to sort this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final MapType mapType,
			final SortedMap<K, ? extends V> m)
	{
		switch (mapType)
		{
			case TREE_MAP:
			{
				return new TreeMap<>(m);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted map implementation: " + mapType);
			}
		}
	}

	/**
	 * Constructs a new tree map with a custom comparator.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param comparator
	 *            key comparator
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(
			final Comparator<? super K> comparator)
	{
		return newSortedMap(TREE_MAP, comparator);
	}

	/**
	 * /** Constructs a new tree map with a custom comparator.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            sorted map implementation identifier
	 * @param comparator
	 *            key comparator
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final MapType mapType,
			final Comparator<? super K> comparator)
	{
		switch (mapType)
		{
			case TREE_MAP:
			{
				return new TreeMap<>(comparator);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized sorted map implementation: " + mapType);
			}
		}
	}

	/**
	 * Constructs a new tree map containing the same mappings and using the same ordering
	 * as the specified sorted map. This method runs in linear time.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param m
	 *            the tree map whose mappings are to be placed in this map, and whose
	 *            comparator is to be used to sort this map
	 * @param comparator
	 *            key comparator
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final Map<K, ? extends V> m,
			final Comparator<? super K> comparator)
	{
		return newSortedMap(TREE_MAP, m, comparator);
	}

	/**
	 * Constructs a new sorted map containing the same mappings and using the same
	 * ordering as the specified sorted map. This method runs in linear time.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param mapType
	 *            sorted map implementation identifier
	 * @param comparator
	 *            key comparator
	 * @param m
	 *            the sorted map whose mappings are to be placed in this map, and whose
	 *            comparator is to be used to sort this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public static <K, V> SortedMap<K, V> newSortedMap(final MapType mapType,
			final Map<K, ? extends V> m, final Comparator<? super K> comparator)
	{
		final SortedMap<K, V> map = newSortedMap(mapType, comparator);
		map.putAll(m);
		return map;
	}

	/**
	 * A generic factory method of array lists.
	 *
	 * @param <E>
	 *            element type
	 * @return an array list
	 */
	public static <E> List<E> newList()
	{
		return newList(ARRAY_LIST);
	}

	/**
	 * A generic factory method of lists.
	 *
	 * @param <E>
	 *            list element type
	 * @param listType
	 *            list implementation identifier
	 * @return an empty list
	 */
	public static <E> List<E> newList(final ListType listType)
	{
		switch (listType)
		{
			case ARRAY_LIST:
			{
				return new ArrayList<>();
			}

			case LINKED_LIST:
			{
				return new LinkedList<>();
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized list implementation: " + listType);
			}
		}
	}

	/**
	 * A generic factory method of array lists. Constructs a list containing the elements
	 * of the specified collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param c
	 *            the collection whose elements are to be placed into this list
	 *
	 * @param <E>
	 *            element type
	 * @return an array list
	 */
	public static <E> List<E> newList(final Collection<? extends E> c)
	{
		return newList(ARRAY_LIST, c);
	}

	/**
	 * A generic factory method of array lists. Constructs a list containing the elements
	 * of the specified collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param listType
	 *            list implementation identifier
	 * @param c
	 *            the collection whose elements are to be placed into this list
	 *
	 * @param <E>
	 *            element type
	 * @return an array list
	 */
	public static <E> List<E> newList(final ListType listType,
			final Collection<? extends E> c)
	{
		switch (listType)
		{
			case ARRAY_LIST:
			{
				return new ArrayList<>(c);
			}

			case LINKED_LIST:
			{
				return new LinkedList<>(c);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized list implementation: " + listType);
			}
		}
	}

	/**
	 * A factory method of array lists. Converts an array to a list.
	 *
	 * @param <E>
	 *            element type
	 * @param array
	 *            array; if <code>null</code>, it is ignored
	 * @return a corresponding list; if <code>array</code> is <code>null</code>, returns
	 *         an empty list
	 */
	public static <E> List<E> newList(final E[] array)
	{
		return CollectionUtil.<E> newList(ARRAY_LIST, array);
	}

	/**
	 * A factory method of array lists. Converts an array to a list.
	 *
	 * @param <E>
	 *            element type
	 * @param listType
	 *            list implementation identifier
	 * @param array
	 *            array; if <code>null</code>, it is ignored
	 * @return a corresponding list; if <code>array</code> is <code>null</code>, returns
	 *         an empty list
	 */
	public static <E> List<E> newList(final ListType listType,
			final E[] array)
	{
		final List<E> list = newList(listType);
		if (array != null)
		{
			for (final E ns : array)
			{
				list.add(ns);
			}
		}
		return list;
	}

	/**
	 * A generic factory method of array lists. Constructs an empty list with the
	 * specified initial capacity.
	 *
	 * @param <E>
	 *            element type
	 * @param initialCapacity
	 *            the initial capacity of the list
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 * @return an array list
	 */
	public static <E> List<E> newList(final int size)
	{
		return newList(ARRAY_LIST, size);
	}

	/**
	 * A generic factory method of array lists. Constructs an empty list with the
	 * specified initial capacity.
	 *
	 * @param <E>
	 *            element type
	 * @param initialCapacity
	 *            the initial capacity of the list
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 * @return an array list
	 */
	public static <E> List<E> newList(final int size, final E fillValue)
	{
		final List<E> list = newList(size);
		for (int i = 0; i < size; i++)
		{
			list.add(fillValue);
		}
		return list;
	}

	/**
	 * A generic factory method of array lists. Constructs an empty list with the
	 * specified initial capacity.
	 *
	 * @param <E>
	 *            element type
	 * @param listType
	 *            list implementation identifier
	 * @param initialCapacity
	 *            the initial capacity of the list
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 * @return an array list A generic factory method of array lists. Constructs a list
	 *         containing the elements of the specified collection, in the order they are
	 *         returned by the collection's iterator. list implementation identifier
	 * @return an empty list
	 */
	public static <E> List<E> newList(final ListType listType, final int size)
	{
		switch (listType)
		{
			case ARRAY_LIST:
			{
				return new ArrayList<>(size);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unsupported list implementation: " + listType);
			}
		}
	}

	/**
	 * Clear a collection and set its elements to a new list. Works even if the list
	 * reference is final.
	 *
	 * @param <E>
	 *            element type
	 * @param collection
	 *            collection to update
	 * @param elements
	 *            new element list
	 * @return the updated list
	 */
	public static <E> Collection<E> setCollectionElements(final Collection<E> collection,
			final Collection<? extends E> elements)
	{
		collection.clear();
		collection.addAll(elements);
		return collection;
	}

	/**
	 * Clear a list and set its elements to a new list. Works even if the list reference
	 * is final.
	 *
	 * @param <E>
	 *            element type
	 * @param list
	 *            list to update
	 * @param elements
	 *            new element list
	 * @return the updated list
	 */
	public static <E> List<E> setListElements(final List<E> list,
			final List<? extends E> elements)
	{
		list.clear();
		list.addAll(elements);
		return list;
	}

	/**
	 * Clear a map and set its elements to a new map. Works even if the map reference is
	 * final.
	 *
	 * @param <K>
	 *            key type
	 * @param <V>
	 *            value type
	 * @param map
	 *            list to update
	 * @param entries
	 *            contains the new entries to set
	 * @return the updated map
	 */
	public static <K, V> Map<K, V> setMapElements(final Map<K, V> map,
			final Map<? extends K, ? extends V> entries)
	{
		map.clear();
		map.putAll(entries);
		return map;
	}

	/**
	 * Return the collection's size, or 0 if it is <code>null</code>
	 *
	 * @param collection
	 *            collection of elements
	 * @return collection's size, or 0 if it is <code>null</code>
	 */
	public static int getSizeNullSafe(final Collection<?> collection)
	{
		return (collection == null) ? 0 : collection.size();
	}

	/**
	 * Convert a list to an array. If the list is null, returns an empty array.
	 *
	 * @param list
	 *            a list of objects
	 * @return list, as array
	 */
	public static Object[] toArrayNullSafe(final List<?> list)
	{
		return getNullSafeList(list).toArray();
	}

	/**
	 * Add all elements in an array to a list
	 *
	 * @param list
	 *            list
	 * @param values
	 *            values to append at the end of the list
	 */
	@SafeVarargs
	// Safe because varargs Object[] can only be objects of type E
	public static <E> void addAll(final List<E> list, final E... values)
	{
		for (final E value : values)
		{
			list.add(value);
		}
	}

	/**
	 * Convert {@link Properties} to a map.
	 *
	 * @param properties
	 *            properties object
	 * @return map view
	 */
	public static Map<String, String> asMap(final Properties properties)
	{
		final Map<String, String> map = newMap();
		for (final Map.Entry<Object, Object> entry : properties.entrySet())
		{
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}

	/**
	 * Convert a map to a {@link Properties} object.
	 *
	 * @param map
	 *            map object
	 * @return properties view of the map
	 */
	public static Properties asProperties(final Map<String, String> map)
	{
		final Properties properties = new Properties();
		for (final Map.Entry<String, String> entry : map.entrySet())
		{
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		return properties;

	}

	/**
	 * Convert an iterator to an iterable object.
	 *
	 * @param <E>
	 *            iterant type
	 * @param iterator
	 *            iterator
	 * @return an iterable objects whose iterator is <code>iterator</code>
	 */
	public static <E> Iterable<E> iterable(final Iterator<E> iterator)
	{
		return new Iterable<E>()
		{
			@Override
			public Iterator<E> iterator()
			{
				return iterator;
			}
		};
	}

	/**
	 * Extract unique elements from a list in their order of appearance.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * <code>toUniqueList([1 1 2 1 3 3 2]) = {1,2,3}.</code>
	 * </pre>
	 *
	 * @param <E>
	 *            element type
	 * @param list
	 *            list; may have duplicate elements
	 * @return a set containing the unique elements in <code>list</code>. The set's
	 *         iteration order is the order of first appearance of elements in the
	 *         original list.
	 */
	public static <E> Set<E> getUniqueElements(final List<E> list)
	{
		final Set<E> set = CollectionUtil.newSet(SetType.LINKED_HASH_SET);
		set.addAll(list);
		return set;
	}
}
