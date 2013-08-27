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

import java.util.Properties;

import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.StorageCode;

/**
 * Gets and sets a resource content on a {@link Resource} class based on the storage type.
 * Also responsible for applying rules to filter resource contents (e.g. resolving place
 * holders in XQuery resources).
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
 * @version Apr 9, 2009
 */
public interface ResourceContentService
{
	// ========================= METHODS ======================================

	/**
	 * Return the resource's content as a string.
	 *
	 * @param resource
	 *            resource
	 * @return content string
	 */
	String getResourceContentAsString(Resource resource);

	/**
	 * Return the content field of a resource corresponding to this storage code.
	 *
	 * @param resource
	 *            MDR resource
	 * @return resource content
	 */
	byte[] getResourceContent(Resource resource);

	/**
	 * Convert a resource raw byte content into the corresponding resource type.
	 *
	 * @param storageCode
	 *            resource type storage code
	 * @param content
	 *            raw content
	 * @return resource as a class
	 */
	Object toStorageType(StorageCode storageCode, byte[] content);

	/**
	 * Set the content field of a resource corresponding to this storage code.
	 *
	 * @param resource
	 *            MDR resource
	 * @param content
	 *            new content entity to set in the resource
	 */
	void setResourceContent(Resource resource, byte[] content);

	/**
	 * Filter a resource's meta data fields (e.g. URL) using the property place holder
	 * resolver.
	 *
	 * @param resource
	 *            resource to filter
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 */
	void filterResourceMetadata(Resource resource, PlaceHolderResolver resolver);

	/**
	 * Filter a resource's content using the property place holder resolver. Right now
	 * only XQuery files are filtered; more complex rules can be incorporated in the
	 * future (and perhaps stored in the MDR).
	 *
	 * @param resource
	 *            resource to filter
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 */
	void filterResourceContent(Resource resource, PlaceHolderResolver resolver);

	/**
	 * Filter a resource's content using the property place holder resolver. Right now
	 * only XQuery files are filtered; more complex rules can be incorporated in the
	 * future (and perhaps stored in the MDR).
	 *
	 * @param resource
	 *            resource to filter
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 * @param overriddenPlaceholders
	 *            map of key-value place holders to use. Is overlaid over the resolver's
	 *            default place holder map, potentially overriding the resolver's default
	 *            values with identical keys in both maps
	 */
	void filterResourceContent(Resource resource, PlaceHolderResolver resolver,
			Properties overridenPlaceholders);
}