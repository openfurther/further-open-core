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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.utah.further.core.api.xml.GenericJaxbMapAdapter.JaxbMapEntryList;

/**
 * A generic JAXB map adapter that prints out a map in the following form:
 * 
 * &lt;map&gt;
 *   &lt;entry&gt;
 *     &lt;key xsi:type=&quot;xs:string&quot;&gt;2&lt;/key&gt;
 *     &lt;value xsi:type=&quot;xs:string&quot;&gt;Two&lt;/value&gt;
 *   &lt;/entry&gt;
 *   &lt;entry&gt;
 *     &lt;key xsi:type=&quot;xs:string&quot;&gt;1&lt;/key&gt;
 *     &lt;value xsi:type=&quot;xs:string&quot;&gt;One&lt;/value&gt;
 *   &lt;/entry&gt;
 * &lt;/map&gt;
 * 
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
 * @version May 6, 2013
 */
public class GenericJaxbMapAdapter<K, V> extends XmlAdapter<JaxbMapEntryList<K, V>, Map<K, V>>
{
	
	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Map<K, V> unmarshal(final JaxbMapEntryList<K, V> list) throws Exception
	{
		final Map<K, V> map = new HashMap<>();
		for (final JaxbMapEntry<K, V> entry : list.getEntry())
		{
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public JaxbMapEntryList<K, V> marshal(final Map<K, V> map) throws Exception
	{
		final List<JaxbMapEntry<K, V>> entries = new ArrayList<>();
		for (final Entry<K, V> entry : map.entrySet())
		{
			entries.add(new JaxbMapEntry<>(entry.getKey(), entry.getValue()));
		}
		return new JaxbMapEntryList<>(entries);
	}

	/**
	 * A list of {@link JaxbMapEntry}'s since JAXB can't handle interfaces very well
	 * <p>
	 * -----------------------------------------------------------------------------------<br>
	 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 *
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version May 6, 2013
	 */
	public static final class JaxbMapEntryList<K, V>
	{
		private List<JaxbMapEntry<K, V>> entry;

		public JaxbMapEntryList()
		{
		}

		public JaxbMapEntryList(final List<JaxbMapEntry<K, V>> entry)
		{
			super();
			this.entry = entry;
		}

		public List<JaxbMapEntry<K, V>> getEntry()
		{
			return entry;
		}

		public void setEntry(final List<JaxbMapEntry<K, V>> entry)
		{
			this.entry = entry;
		}

	}
	
	/**
	 * A JAXB map entry with a key and a value
	 * <p>
	 * -----------------------------------------------------------------------------------<br>
	 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 *
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version May 6, 2013
	 */
	public static final class JaxbMapEntry<K, V>
	{
		private K key;

		private V value;

		public JaxbMapEntry()
		{
		}

		public JaxbMapEntry(final K key, final V value)
		{
			super();
			this.key = key;
			this.value = value;
		}

		public K getKey()
		{
			return key;
		}

		public void setKey(final K key)
		{
			this.key = key;
		}

		public V getValue()
		{
			return value;
		}

		public void setValue(final V value)
		{
			this.value = value;
		}

	}

}
