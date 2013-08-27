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

import static edu.utah.further.osgi.shell.ShellUtil.getBundleStateAsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

/**
 * A convenient JavaBean that acts as a bridge between the OSGi {@link Bundle} API and our
 * list command builders (the Adapter Pattern).
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
final class BundleInfoBundleImpl implements BundleInfo
{
	// ========================= FIELDS ====================================

	/**
	 * The OSGi bundle to wrap / adapt.
	 */
	private final Bundle bundle;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param bundle
	 */
	public BundleInfoBundleImpl(final Bundle bundle)
	{
		super();
		this.bundle = bundle;
	}

	// ========================= IMPL: BundleInfo ==========================

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getName()
	 */
	@Override
	public String getName()
	{
		return (String) bundle.getHeaders().get(Constants.BUNDLE_NAME);
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getVersion()
	 */
	@Override
	public String getVersion()
	{
		return (String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getBundleId()
	 */
	@Override
	public String getBundleId()
	{
		return "" + bundle.getBundleId();
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getLocation()
	 */
	@Override
	public String getLocation()
	{
		return bundle.getLocation();
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getUpdateLocation()
	 */
	@Override
	public String getUpdateLocation()
	{
		return (String) bundle.getHeaders().get(Constants.BUNDLE_UPDATELOCATION);
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getState()
	 */
	@Override
	public String getState()
	{
		return getBundleStateAsString(bundle.getState());
	}

	/**
	 * @param listener
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getState(edu.utah.further.osgi.shell.BundleStateListener)
	 */
	@Override
	public String getState(final BundleStateListener listener)
	{
		return listener.getState(bundle);
	}

	/**
	 * @param admin
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getFragments(org.osgi.service.packageadmin.PackageAdmin)
	 */
	@Override
	public List<Bundle> getFragments(final PackageAdmin admin)
	{
		final Bundle[] fragments = admin.getFragments(bundle);
		if (fragments != null) {
			return Arrays.asList(fragments);
		}
		return new ArrayList<>();
	}

	/**
	 * @param admin
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getHosts(org.osgi.service.packageadmin.PackageAdmin)
	 */
	@Override
	public List<Bundle> getHosts(final PackageAdmin admin)
	{
		final Bundle[] hosts = admin.getHosts(bundle);
		if (hosts != null) {
			return Arrays.asList(hosts);
		}
		return new ArrayList<>();
	}

	/**
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getSymbolicName()
	 */
	@Override
	public String getSymbolicName()
	{
		return bundle.getSymbolicName();
	}

	/**
	 * @param startLevel
	 * @return
	 * @see edu.utah.further.osgi.shell.BundleInfo#getStartLevel(org.osgi.service.startlevel.StartLevel)
	 */
	@Override
	public String getStartLevel(final StartLevel startLevel)
	{
		return "" + startLevel.getBundleStartLevel(bundle);
	}
}
