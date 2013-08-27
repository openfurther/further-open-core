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
package edu.utah.further.mdr.api.dao;

import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.MdrNames;

/**
 * An abstraction of {@link Resource} data access objects.
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
public interface ResourceDao
{
	// ========================= METHODS ===================================

	/**
	 * Return the currently active resource with a specified MDR path. Implementations
	 * must always eagerly fetch the resource's storage fields here.
	 * <p>
	 * The returned resource's storage field will be filtered according to the resource's
	 * type. Currently supported rules:
	 * <ul>
	 * <li>If resource type = {@link MdrNames#XQUERY}, filter place-holders
	 *
	 * @param path
	 *            MDR path
	 * @return active resource, if found. If not found, returns <code>null</code>
	 */
	Resource getActiveResourceByPath(String path);
}