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
package edu.utah.further.osgi.shell;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 9, 2010
 */
interface BundleInfo
{
	// ========================= METHODS ===================================

	/**
	 * @return
	 */
	String getName();

	/**
	 * @return
	 */
	String getVersion();

	/**
	 * @return
	 * @see org.osgi.framework.Bundle#getBundleId()
	 */
	String getBundleId();

	/**
	 * @return
	 * @see org.osgi.framework.Bundle#getLocation()
	 */
	String getLocation();

	/**
	 * @return
	 */
	String getUpdateLocation();

	/**
	 * @return
	 * @see org.osgi.framework.Bundle#getState()
	 */
	String getState();

	/**
	 * @return
	 * @see org.osgi.framework.Bundle#getState()
	 */
	String getState(BundleStateListener listener);

	/**
	 * @param admin
	 * @return
	 */
	List<Bundle> getFragments(PackageAdmin admin);

	/**
	 * @param admin
	 * @return
	 */
	List<Bundle> getHosts(PackageAdmin admin);

	/**
	 * @return
	 * @see org.osgi.framework.Bundle#getSymbolicName()
	 */
	String getSymbolicName();

	/**
	 * @return
	 */
	String getStartLevel(StartLevel startLevel);
}