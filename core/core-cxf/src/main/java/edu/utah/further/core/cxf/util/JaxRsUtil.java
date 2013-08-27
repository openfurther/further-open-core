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
package edu.utah.further.core.cxf.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Utility methods for the JAX-RS related classes.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Feb 4, 2011
 */
public final class JaxRsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(JaxRsUtil.class);

	/**
	 * This class should not be instantiated.
	 */
	private JaxRsUtil()
	{
		preventUtilityConstruction();
	}

	/**
	 * Returns a map with the first value of the multivalued map with the same key.
	 * 
	 * @param multivaluedMap
	 * @return
	 */
	public static <K, V> Map<K, V> toSingleValuedMap(
			final MultivaluedMap<K, V> multivaluedMap)
	{
		final Map<K, V> singleValuedMap = CollectionUtil.newMap();
		for (final K key : multivaluedMap.keySet())
		{
			singleValuedMap.put(key, multivaluedMap.getFirst(key));
		}

		return singleValuedMap;
	}

}
