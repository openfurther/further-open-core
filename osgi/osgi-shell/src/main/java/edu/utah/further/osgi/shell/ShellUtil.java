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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.felix.service.command.CommandSession;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.startlevel.StartLevel;

/**
 * Centralizes useful utilities, e.g. collection factory methods.
 * <p>
 * Repeats some code from <code>core-api</code>, but we would like to keep this module
 * decoupled from the FURTHeR code for broader reusability.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 8, 2010
 */
final class ShellUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ShellUtil()
	{
		throw new IllegalAccessError("A utility class cannot be instantiated");
	}

	// ========================= METHODS ===================================

	/**
	 * Return a textual representation of a {@link Bundle} state.
	 *
	 * @param state
	 *            bundle state
	 * @return textual representation of state
	 */
	public static String getBundleStateAsString(final int state)
	{
		switch (state)
		{
			case Bundle.ACTIVE:
			{
				return "Active";
			}

			case Bundle.INSTALLED:
			{
				return "Installed";
			}

			case Bundle.RESOLVED:
			{
				return "Resolved";
			}

			case Bundle.STARTING:
			{
				return "Starting";
			}

			case Bundle.STOPPING:
			{
				return "Stopping";
			}

			default:
			{
				return "Unknown";
			}
		}
	}

	/**
	 * @param bundles
	 * @return
	 */
	public static List<Long> getBundleIds(final Collection<? extends Bundle> bundles)
	{
		final List<Long> output = new ArrayList<>();
		for (final Bundle bundle : bundles)
		{
			output.add(new Long(bundle.getBundleId()));
		}
		return output;
	}

	/**
	 * Check if a bundle is a system bundle (start level < 50)
	 *
	 * @param bundleContext
	 * @param bundle
	 * @return true if the bundle has start level minor than 50
	 */
	public static boolean isASystemBundle(final BundleContext bundleContext,
			final Bundle bundle)
	{
		final ServiceReference ref = bundleContext.getServiceReference(StartLevel.class
				.getName());
		if (ref != null)
		{
			final StartLevel sl = (StartLevel) bundleContext.getService(ref);
			if (sl != null)
			{
				final int level = sl.getBundleStartLevel(bundle);
				int sbsl = 49;
				final String sbslProp = bundleContext
						.getProperty("karaf.systemBundlesStartLevel");
				if (sbslProp != null)
				{
					try
					{
						sbsl = Integer.valueOf(sbslProp).intValue();
					}
					catch (final Exception ignore)
					{
						// ignore
					}
				}
				return level <= sbsl;
			}
		}
		return false;
	}

	/**
	 * Ask the user to confirm the access to a system bundle
	 *
	 * @param bundleId
	 * @param session
	 * @return true if the user confirm
	 * @throws IOException
	 */
	public static boolean accessToSystemBundleIsAllowed(final long bundleId,
			final CommandSession session) throws IOException
	{
		for (;;)
		{
			final StringBuffer sb = new StringBuffer();
			System.err.print("You are about to access system bundle " + bundleId
					+ ".  Do you want to continue (yes/no): ");
			System.err.flush();
			for (;;)
			{
				final int c = session.getKeyboard().read();
				if (c < 0)
				{
					return false;
				}
				System.err.print((char) c);
				if (c == '\r' || c == '\n')
				{
					break;
				}
				sb.append((char) c);
			}
			final String str = sb.toString();
			if ("yes".equals(str))
			{
				return true;
			}
			if ("no".equals(str))
			{
				return false;
			}
		}
	}

	/**
	 * Print a string to standard output.
	 *
	 * @param s
	 *            string to print
	 */
	public static void println(final String s)
	{
		System.out.println(s);
	}
}
