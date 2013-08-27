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

import static edu.utah.further.osgi.shell.ShellUtil.println;

import java.io.InputStream;
import java.net.URL;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dustin noted that sometimes updating a bundle does not refresh it. This method combines
 * the two operations.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 9, 2010
 */
@Command(scope = "f", name = "fls", description = "Lists all FURTHeR bundles")
public final class UpdateRefreshCommand extends BundleCommand
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log1 = LoggerFactory.getLogger(UpdateRefreshCommand.class);

	// ========================= COMMAND OPTIONS ===========================

	@Argument(index = 1, name = "location", description = "The bundle's update location", required = false, multiValued = false)
	String location;

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPL: OsgiCommandSupport ==================

	/**
	 * @return
	 * @throws Exception
	 * @see org.apache.felix.karaf.shell.console.OsgiCommandSupport#doExecute()
	 */
	@Override
	protected Object doExecute() throws Exception
	{
		println("Updating " + id);
		doExecute(new UpdateExecutor());
		println("Refreshing " + id);
		return doRefresh();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Update the bundle.
	 */
	private class UpdateExecutor implements BundleExecutor
	{
		/**
		 * @param bundle
		 * @throws Exception
		 * @see edu.utah.further.osgi.shell.BundleExecutor#doExecute(org.osgi.framework.Bundle)
		 */
		@Override
		public void doExecute(final Bundle bundle) throws Exception
		{
			if (location != null)
			{
				if (log1.isDebugEnabled())
				{
					log1
							.debug("Updating " + bundle + " using update location "
									+ location);
				}
				@SuppressWarnings("resource")
				// Resource is closed by update(is) method
				final InputStream is = new URL(location).openStream();
				bundle.update(is);
			}
			else
			{
				if (log1.isDebugEnabled())
				{
					log1.debug("Updating " + bundle);
				}
				bundle.update();
			}
		}
	}

	/**
	 * Refresh the bundle.
	 * 
	 * @return
	 * @throws Exception
	 * @see edu.utah.further.osgi.shell.BundleCommand#doExecute()
	 */
	private Object doRefresh() throws Exception
	{
		// Get package admin service.
		final ServiceReference ref = getBundleContext().getServiceReference(
				PackageAdmin.class.getName());
		if (ref == null)
		{
			println("PackageAdmin service is unavailable.");
			return null;
		}
		try
		{
			final PackageAdmin pa = (PackageAdmin) getBundleContext().getService(ref);
			if (pa == null)
			{
				println("PackageAdmin service is unavailable.");
				return null;
			}
			final Bundle bundle = getBundleContext().getBundle(id);
			if (bundle == null)
			{
				println("Bundle " + id + " not found");
				return null;
			}
			if (log1.isDebugEnabled())
			{
				log1.debug("Refreshing bundle " + bundle);
			}
			pa.refreshPackages(new Bundle[]
			{ bundle });
		}
		finally
		{
			getBundleContext().ungetService(ref);
		}
		return null;
	}
}
