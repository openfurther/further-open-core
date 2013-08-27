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

import static edu.utah.further.core.api.message.ValidationUtil.validateNotEmpty;
import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.api.service.asset.AssetUtilService;

/**
 * MDR asset utilities that are useful for testing in various modules, hence are in the
 * MDR API module.
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
 * @version Oct 13, 2008
 */
@Service("assetUtilService")
public final class AssetUtilServiceImpl implements AssetUtilService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(AssetUtilServiceImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes asset service.
	 */
	@Autowired
	@Qualifier("assetService")
	private AssetService assetService;

	// ========================= IMPLEMENTATION: AssetUtilService ==========

	/**
	 * Return an asset list by label. Validates that the match list is non-empty.
	 * 
	 * @param label
	 *            asset label
	 * @return list of matching assets
	 * @see edu.utah.further.mdr.api.service.asset.AssetUtilService#findAssetsByLikeLabel(java.lang.String)
	 */
	@Override
	public List<Asset> findAssetsByLikeLabel(final String label)
	{
		final List<Asset> assets = assetService.findAssetsByLikeLabel(label);
		// if (log.isDebugEnabled())
		// {
		// log.debug("assets = " + assets);
		// }
		final String entityName = "Asset result set for type label " + label;
		validateNotNull(entityName, assets);
		validateNotEmpty(entityName, assets);
		return assets;
	}

	/**
	 * Return a list of resources that match a type label.
	 * 
	 * @param typeLabel
	 *            resource type label to match
	 * @return the first encountered resource that matches the type label
	 * @see edu.utah.further.mdr.api.service.asset.AssetUtilService#findResourcesByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public List<Resource> findResourcesByLikeTypeLabel(final String typeLabel)
	{
		final List<Resource> resources = assetService
				.findResourcesByLikeTypeLabel(typeLabel);
		// if (log.isDebugEnabled())
		// {
		// log.debug("resources = " + resources);
		// }
		final String entityName = "Resource result set for type label " + typeLabel;
		validateNotNull(entityName, resources);
		validateNotEmpty(entityName, resources);
		return resources;
	}

	/**
	 * Return the first encountered resource with a certain type label.
	 * 
	 * @param typeLabel
	 *            resource type label to match
	 * @return the first encountered resource that matches the type label
	 * @see edu.utah.further.mdr.api.service.asset.AssetUtilService#findResourceByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public Resource findResourceByLikeTypeLabel(final String typeLabel)
	{
		final List<Resource> resources = assetService
				.findResourcesByLikeTypeLabel(typeLabel);
		// if (log.isDebugEnabled())
		// {
		// log.debug("resources = " + resources);
		// }
		final String entityName = "Resource result set for type label " + typeLabel;
		validateNotNull(entityName, resources);
		validateNotEmpty(entityName, resources);
		return resources.get(0);
	}

	/**
	 * Return the resource types that match a label. Validates that the match list is
	 * non-empty.
	 * 
	 * @param label
	 *            resource type label
	 * @return first resource type on the list of matches
	 * @see edu.utah.further.mdr.api.service.asset.AssetUtilService#findResourceTypesByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public ResourceType findResourceTypesByLikeTypeLabel(final String label)
	{
		final List<ResourceType> resourceTypes = assetService
				.findResourceTypesByLikeTypeLabel(label);
		validateNotEmpty("Resource type list with label " + quote(label), resourceTypes);
		// if (log.isDebugEnabled())
		// {
		// log.debug("resourcesTypes = " + resourceTypes);
		// }
		return resourceTypes.get(0);
	}

}
