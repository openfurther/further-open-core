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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;

@Implementation
@Service("cachingService")
public class CachingServiceEHCacheImpl implements CachingService
{
	@Autowired
	private CacheManager cacheManager;

	@Override
	public void addRegion(final String regionName)
	{
		if (!cacheManager.cacheExists(regionName))
		{
			cacheManager.addCache(regionName);
		}
	}

	@Override
	public <T> T getObject(final String regionName, final Object id)
	{
		final Cache cache = getCache(regionName);
		final Element e = cache.get(id);
		if (e == null)
		{
			return null;
		}
		return (T) e.getObjectValue();
	}

	private Cache getCache(final String regionName)
	{
		Cache cache = cacheManager.getCache(regionName);
		if (cache == null)
		{
			cacheManager.addCache(regionName);
			cache = cacheManager.getCache(regionName);
		}
		return cache;
	}

	@Override
	public List<Object> getKeys(final String regionName)
	{
		return getCache(regionName).getKeys();
	}

	@Override
	public int getSize(final String regionName)
	{
		return getCache(regionName).getSize();
	}

	@Override
	public void saveObject(final String regionName, final Object id, final Object object)
	{
		getCache(regionName).put(new Element(id, object));
	}

	@Override
	public void removeObject(final String regionName, final Object id)
	{
		getCache(regionName).remove(id);
	}

	@Override
	public void setConfigFileName(final String configFileName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getConfigFileName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public CacheManager getCacheManager()
	{
		return cacheManager;
	}

	public void setCacheManager(final CacheManager cacheManager)
	{
		this.cacheManager = cacheManager;
	}

}
