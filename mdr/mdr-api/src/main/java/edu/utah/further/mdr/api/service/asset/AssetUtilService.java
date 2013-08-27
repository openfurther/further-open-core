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
package edu.utah.further.mdr.api.service.asset;

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;

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
 * @see MdrNames
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Api
public interface AssetUtilService
{
	// ========================= METHODS ===================================

	/**
	 * Return an asset list by label. Validates that the match list is non-empty.
	 * 
	 * @param label
	 *            asset label
	 * @return list of matching assets
	 */
	List<Asset> findAssetsByLikeLabel(String label);

	/**
	 * Return a list of resources that match a type label.
	 * 
	 * @param typeLabel
	 *            resource type label to match
	 * @return the first encountered resource that matches the type label
	 */
	List<Resource> findResourcesByLikeTypeLabel(String typeLabel);

	/**
	 * Return the first encountered resource with a certain type label.
	 * 
	 * @param typeLabel
	 *            resource type label to match
	 * @return the first encountered resource that matches the type label
	 */
	Resource findResourceByLikeTypeLabel(String typeLabel);

	/**
	 * Return the resource types that match a label. Validates that the match list is
	 * non-empty.
	 * 
	 * @param label
	 *            resource type label
	 * @return first resource type on the list of matches
	 */
	ResourceType findResourceTypesByLikeTypeLabel(String label);
}
