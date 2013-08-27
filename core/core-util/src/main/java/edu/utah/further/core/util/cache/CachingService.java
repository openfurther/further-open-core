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
package edu.utah.further.core.util.cache;

import java.util.List;

/**
 * An object caching service.
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
 * @version Jan 28, 2009
 */
public interface CachingService
{
	// ========================= METHODS ===================================

	/**
	 * Declare a new caching region. If the region is already declared, this method has no
	 * effect.
	 * 
	 * @param regionName
	 *            region's name
	 */
	void addRegion(String regionName);

	/**
	 * Get a cached object from a cache region.
	 * 
	 * @param regionName
	 *            region's name
	 * @param id
	 *            object cache identifier
	 * @param object
	 *            object to save
	 */
	<T> T getObject(String regionName, Object id);

	/**
	 * Get all keys from a cache region.
	 * 
	 * @param regionName
	 * @param groupName
	 *            TODO
	 * @return keys
	 */
	List<Object> getKeys(String regionName);

	/**
	 * Returns the size of a cache region.
	 * 
	 * @param cacheRegion
	 * @return size
	 */
	int getSize(String cacheRegion);

	/**
	 * Save an object in a cache region.
	 * 
	 * @param regionName
	 *            region's name
	 * @param id
	 *            object cache identifier
	 * @param object
	 *            object to save
	 */
	void saveObject(String regionName, Object id, Object object);

	/**
	 * Remove an object in a cache region.
	 * 
	 * @param cacheRegion
	 * @param id
	 */
	void removeObject(String cacheRegion, Object id);

	/**
	 * Set a new value for JCS configuration file name.
	 * 
	 * @param configFileName
	 *            the configFileName to set
	 */
	void setConfigFileName(String configFileName);

	/**
	 * Return the configFileName property.
	 * 
	 * @return the configFileName
	 */
	String getConfigFileName();

}
