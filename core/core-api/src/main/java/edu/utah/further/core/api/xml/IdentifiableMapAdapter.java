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
package edu.utah.further.core.api.xml;

import static edu.utah.further.core.api.lang.ReflectionUtil.getInterfaceTypeArguments;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.MutablePublicMapEntry;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A JAXB adapter of a map of {@link Comparable} objects. Items are sorted by natural key
 * order in the XML view unless a comparator is provided, even though the underlying data
 * structure is a plain {@link Map} . This adapter is responsible for sorting the map for
 * the view.
 * <p>
 * Implementations of this class must define an inner static member class that has a field
 * named {@link #VALUE_TYPE_LIST_FIELD_NAME} of type <code>List<T></code>. For example,
 * suppose <code>NamedImpl</code> implements {@link Named} and we would like to bind a
 * <code>NamedImpl</code> map in some JAXB entity into an XML element containing a list of
 * XML sub-elements, each named <code>property</code> and corresponding to one map
 * element. The usage is then
 * 
 * <pre>
 * private static class PropertyMapAdapter extends
 * 		NamedMapAdapter&lt;NamedImpl, PropertyMapAdapter.DtsProperties&gt;
 * {
 * 	&#064;XmlAccessorType(XmlAccessType.FIELD)
 * 	public static class DtsProperties
 * 	{
 * 		&#064;SuppressWarnings(&quot;unused&quot;)
 * 		&#064;XmlElement(name = &quot;property&quot;)
 * 		public List&lt;NamedImpl&gt; list;
 * 	}
 * }
 * 
 * &#064;XmlElement(name = &quot;properties&quot;)
 * &#064;XmlJavaTypeAdapter(PropertyMapAdapter.class)
 * private final Map&lt;String, NamedImpl&gt; properties = newMap();
 * </pre>
 * 
 * The marshalled XML might look like
 * 
 * <pre>
 * &lt;properties&gt;
 * 		&lt;property&gt;
 * 			&lt;name&gt;Code in Source&lt;/name&gt;
 * 			&lt;value&gt;38033009&lt;/value&gt;
 * 		&lt;/property&gt;
 * 		&lt;property&gt;
 * 			&lt;name&gt;Concept Status&lt;/name&gt;
 * 			&lt;value&gt;Current&lt;/value&gt;
 * 		&lt;/property&gt;
 *      ...
 * &lt;/properties&gt;
 * </pre>
 * 
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 8, 2009
 */
public abstract class IdentifiableMapAdapter<K extends Comparable<? super K>, V, E extends MutablePublicMapEntry<K, V>, S>
		extends XmlAdapter<S, Map<K, V>> implements
		IdentifiableMapAdapterInterface<K, V, E, S>
{
	// ========================= CONSTANTS =================================

	/**
	 * Name of list field in the target <code>ValueType</code> of this adapter.
	 */
	private static final String VALUE_TYPE_LIST_FIELD_NAME = "list";

	/**
	 * Index of the type argument "E".
	 */
	private static final int TYPE_ARUMENT_INDEX_E = 2;

	/**
	 * Index of the type argument "S".
	 */
	private static final int TYPE_ARUMENT_INDEX_S = 3;

	/**
	 * A Comparator for configurable sort order. If the comparator is null, sort ordering
	 * is TreeMap Natural Ordering.
	 */
	private Comparator<? super K> comparator;

	// ========================= FIELDS ====================================

	/**
	 * Generic-type arguments of the specific implementation of this class.
	 */
	private final List<Class<?>> typeArguments = getInterfaceTypeArguments(
			IdentifiableMapAdapter.class, getClass());

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: XmlAdapter ================

	/**
	 * @param valueType
	 * @return
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Map<K, V> unmarshal(final S valueType)
	{
		// Generate a sorted map from incoming list
		SortedMap<K, V> map = CollectionUtil.newSortedMap();
		
		// Specified sort
		if (comparator != null)
		{
			map = CollectionUtil.newSortedMap(comparator);
		}
		
		if (valueType != null)
		{
			final List<E> list = getValueTypeList(valueType);
			for (final E element : list)
			{
				map.put(element.getKey(), element.getValue());
			}
		}
		return map;
	}

	/**
	 * @param boundType
	 * @return
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public S marshal(final Map<K, V> boundType)
	{
		// Do not generate an empty tag for an empty map
		if ((boundType == null) || boundType.isEmpty())
		{
			return null;
		}
		// Sort incoming map
		// Default sort
		SortedMap<K, V> sortedMap = CollectionUtil.newSortedMap();

		// Specified sort
		if (comparator != null)
		{
			sortedMap = CollectionUtil.newSortedMap(comparator);
		}

		sortedMap.putAll(boundType);

		// Convert to a list
		final List<E> list = CollectionUtil.newList();
		for (final Map.Entry<K, V> entry : sortedMap.entrySet())
		{
			final E entryEntity = newEntryTypeInstance();
			entryEntity.setKey(entry.getKey());
			entryEntity.setValue(entry.getValue());
			list.add(entryEntity);
		}

		final S map = newValueTypeInstance();
		setValueTypeArray(map, list);
		return map;
	}

	/**
	 * Set a new value for the comparator property.
	 * 
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(final Comparator<? super K> comparator)
	{
		this.comparator = comparator;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private E newEntryTypeInstance()
	{
		return this.<E> newTypeArgumentInstance(TYPE_ARUMENT_INDEX_E);
	}

	/**
	 * @return
	 */
	private S newValueTypeInstance()
	{
		return this.<S> newTypeArgumentInstance(TYPE_ARUMENT_INDEX_S);
	}

	/**
	 * @return
	 */
	private <T> T newTypeArgumentInstance(final int index)
	{
		try
		{
			return ((Class<T>) typeArguments.get(index)).newInstance();
		}
		catch (final InstantiationException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not instantiate type argument #" + index, e);
		}
		catch (final IllegalAccessException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not instantiate type argument #" + index, e);
		}
	}

	/**
	 * @param valueType
	 * @return
	 */
	private List<E> getValueTypeList(final S valueType)
	{
		try
		{
			final Field listField = valueType.getClass().getDeclaredField(
					VALUE_TYPE_LIST_FIELD_NAME);
			return (List<E>) listField.get(valueType);
		}
		catch (final IllegalArgumentException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not get value type's list field in the value type instance "
							+ valueType, e);
		}
		catch (final IllegalAccessException | SecurityException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not get value type's list field", e);
		}
		catch (final NoSuchFieldException e)
		{
			throw new ApplicationException(
					ErrorCode.INTERNAL_ERROR,
					"Missing value type field called '"
							+ VALUE_TYPE_LIST_FIELD_NAME
							+ "'. Must be declared with this name and annotated with JAXB @XmlElement!",
					e);
		}
	}

	/**
	 * @param valueType
	 * @param list
	 * @return
	 */
	private void setValueTypeArray(final S valueType, final List<E> list)
	{
		try
		{
			final Field listField = valueType.getClass().getDeclaredField(
					VALUE_TYPE_LIST_FIELD_NAME);
			listField.set(valueType, list);
		}
		catch (final IllegalArgumentException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not set value type's list field in the value type instance "
							+ valueType, e);
		}
		catch (final IllegalAccessException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not set value type's list field", e);
		}
		catch (final SecurityException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not set value type's list field", e);
		}
		catch (final NoSuchFieldException e)
		{
			throw new ApplicationException(
					ErrorCode.INTERNAL_ERROR,
					"Missing value type field called '"
							+ VALUE_TYPE_LIST_FIELD_NAME
							+ "'. Must be declared with this name and annotated with JAXB @XmlElement!",
					e);
		}
	}
}
