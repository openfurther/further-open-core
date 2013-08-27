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
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A JAXB adapter of a map of {@link Named} objects. Items are sorted by name in the XML
 * view, even though the underlying data structure is a plain {@link Map}. This adapter is
 * responsible for sorting the map for the view.
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
public abstract class NamedMapAdapter<T extends Named, S> extends
		XmlAdapter<S, Map<String, T>> implements NamedMapAdapterInterface<T, S>
{
	// ========================= CONSTANTS =================================

	/**
	 * Name of list field in the target <code>ValueType</code> of this adapter.
	 */
	private static final String VALUE_TYPE_LIST_FIELD_NAME = "list";

	/**
	 * Index of the type argument "S".
	 */
	private static final int TYPE_ARUMENT_INDEX_S = 1;

	// ========================= FIELDS ====================================

	/**
	 * Generic-type arguments of the specific implementation of this class.
	 */
	private final List<Class<?>> typeArguments = getInterfaceTypeArguments(
			NamedMapAdapter.class, getClass());

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: XmlAdapter ================

	/**
	 * @param valueType
	 * @return
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Map<String, T> unmarshal(final S valueType)
	{
		// Generate a sorted map from incoming list
		final SortedMap<String, T> map = CollectionUtil.newSortedMap();
		if (valueType != null)
		{
			final List<T> list = getValueTypeList(valueType);
			for (final T property : list)
			{
				map.put(property.getName(), property);
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
	public S marshal(final Map<String, T> boundType)
	{
		// Sort incoming map
		final SortedMap<String, T> sortedMap = CollectionUtil.newSortedMap();
		if (boundType != null)
		{
			sortedMap.putAll(boundType);
		}

		// Convert to a list
		final List<T> list = CollectionUtil.newList();
		for (final Map.Entry<String, T> entry : sortedMap.entrySet())
		{
			list.add(entry.getValue());
		}

		final S properties = newValueTypeInstance();
		setValueTypeArray(properties, list);
		return properties;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private S newValueTypeInstance()
	{
		try
		{
			return ((Class<S>) typeArguments.get(TYPE_ARUMENT_INDEX_S)).newInstance();
		}
		catch (final InstantiationException | IllegalAccessException e)
		{
			throw new ApplicationException(ErrorCode.INTERNAL_ERROR,
					"Could not instantiate value type", e);
		}
	}

	/**
	 * @param valueType
	 * @return
	 */
	private List<T> getValueTypeList(final S valueType)
	{
		try
		{
			final Field listField = valueType.getClass().getDeclaredField(
					VALUE_TYPE_LIST_FIELD_NAME);
			return (List<T>) listField.get(valueType);
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
	private void setValueTypeArray(final S valueType, final List<T> list)
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
		catch (final IllegalAccessException | SecurityException e)
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
