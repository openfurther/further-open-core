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
package edu.utah.further.mdr.common.asset;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.api.service.asset.ResourceContentService;

/**
 * A base class of asset service implementations. See FUR-756.
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
 * @version Apr 26, 2010
 */
@Implementation
public abstract class AbstractAssetService implements AssetService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractAssetService.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles resource content I/O.
	 */
	@Autowired
	private ResourceContentService resourceContentService;

	/**
	 * Resolves property place-holders in resource storage.
	 */
	@Autowired
	private PlaceHolderResolver resolver;

	// ========================= FIELDS ====================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the resolver property.
	 *
	 * @param resolver
	 *            the resolver to set
	 */
	public void setResolver(final PlaceHolderResolver resolver)
	{
		this.resolver = resolver;
	}

	/**
	 * Set a new value for the resourceContentService property.
	 *
	 * @param resourceContentService
	 *            the resourceContentService to set
	 */
	public void setResourceContentService(
			final ResourceContentService resourceContentService)
	{
		this.resourceContentService = resourceContentService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getActiveResourceInputstreamByPath
	 * (java.lang.String)
	 */
	@Override
	public InputStream getActiveResourceInputstreamByPath(final String path)
	{
		return new ByteArrayInputStream(getActiveResourceContentByPath(path).getBytes());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getActiveResourceContentByPath
	 * (java.lang.String)
	 */
	@Override
	public String getActiveResourceContentByPath(final String path)
	{
		// final String fullPath = toStringList(path);
		final String fullPath = path;

		if (log.isDebugEnabled())
		{
			log.debug("Getting resource filtered XML content by path " + fullPath);
		}
		final Resource resource = getActiveResourceByPath(fullPath);
		return getResourceContentService().getResourceContentAsString(resource);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the resourceContentService property.
	 *
	 * @return the resourceContentService
	 */
	protected final ResourceContentService getResourceContentService()
	{
		return resourceContentService;
	}

	/**
	 * Return the resolver property.
	 *
	 * @return the resolver
	 */
	protected PlaceHolderResolver getResolver()
	{
		return resolver;
	}

	/**
	 * A reference to ourselves so that we can self-invoke and have Spring call aspects on
	 * the self-invocation call.
	 *
	 * @return self (will be a proxy of this object if using AOP)
	 */
	protected final AssetService getSelf()
	{
		return MdrCommonResourceLocator.getInstance().getAssetService();
	}
}
