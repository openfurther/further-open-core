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
package edu.utah.further.mdr.api.to.asset;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.MutableActivationInfo;

/**
 * A useful addition of {@link MutableActivationInfo} for transfer objects.
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
 * @version Mar 20, 2009
 */
public interface ImmutableActivationTimeInfo
{
	// ========================= METHODS ===================================

	/**
	 * Return the asset activation time. No setter is provided to maintain a consistent
	 * object state, use {@link Asset#setActivationDate(java.sql.Timestamp)} instead.
	 *
	 * @return asset activation time
	 */
	Long getActivationTime();

	/**
	 * Return the asset de-activation time. No setter is provided to maintain a consistent
	 * object state, use {@link Asset#setDeactivationDate(java.sql.Timestamp)} instead.
	 *
	 * @return asset de-activation time
	 */
	Long getDeactivationTime();
}