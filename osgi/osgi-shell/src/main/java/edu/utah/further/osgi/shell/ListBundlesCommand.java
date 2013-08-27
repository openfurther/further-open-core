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
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.slf4j.Logger;

/**
 * Lists the FURTHeR bundles. Can be customized to list any bundle subset whose element
 * symbolic names start with a common prefix.
 * <p>
 * Will catch Spring events only if refreshed before FURTHeR bundles are. Otherwise the
 * spring column will be empty as in https://issues.apache.org/jira/browse/FELIX-1995
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see http://felix.apache.org/site/61-extending-the-console.html
 * @see https://wiki.chpc.utah.edu/display/further/Custom+Fuse+4.2+Shell+Commands
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 8, 2010
 */
@Command(scope = "f", name = "fls", description = "Lists all FURTHeR bundles")
public final class ListBundlesCommand extends OsgiCommandSupport
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log1 = getLogger(ListBundlesCommand.class);

	/**
	 * Controls bundle name display.
	 */
	public static enum BundleView
	{
		NAME, SYMBOLIC_NAME, LOCATION, UPDATE_LOCATION;
	}

	/**
	 * Useful for option parsing.
	 */
	private static enum BundleViewOption
	{
		n(BundleView.NAME), s(BundleView.SYMBOLIC_NAME), l(BundleView.LOCATION), u(
				BundleView.UPDATE_LOCATION);

		private final BundleView bundleView;

		/**
		 * @param bundleView
		 */
		private BundleViewOption(final BundleView bundleView)
		{
			this.bundleView = bundleView;
		}

		/**
		 * Return the bundleView property.
		 *
		 * @return the bundleView
		 */
		public BundleView getBundleView()
		{
			return bundleView;
		}

	}

	// ========================= COMMAND OPTIONS ===========================

	@Argument(index = 0, name = "view", description = "Bundle view type. n: bundle names; s = bundle locations; l = locations; u = update locations", required = false, multiValued = false)
	BundleViewOption view = BundleViewOption.n;

	@Option(name = "-s", description = "Display a single-column view. If not specified, a double-column view is displayed.", required = false, multiValued = false)
	boolean showSingle = false;

	@Option(name = "-v", description = "Show bundle versions.", required = false, multiValued = false)
	boolean showVersion = false;

	@Option(name = "-a", description = "Show bundle fragments and hosts.", required = false, multiValued = false)
	boolean showAttachments = true;

	@Option(name = "-f", description = "Show the full list of included bundles -- ignore bundle name exclusion patterns.", required = false, multiValued = false)
	boolean showFullList = false;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Bundle listeners. Each one adds a column with information on a particular aspect of
	 * the bundle state.
	 */
	private final List<BundleStateListener> bundleStateListeners = new ArrayList<>();

	/**
	 * List of inclusion regular expressions of bundle symbolic names to use in filtering
	 * bundles.
	 */
	private final List<Pattern> bundleSymbolicNameIncludePatterns = new ArrayList<>();

	/**
	 * List of exclusion regular expressions of bundle symbolic names to use in filtering
	 * bundles.
	 */
	private final List<Pattern> bundleSymbolicNameExcludePatterns = new ArrayList<>();

	/**
	 * Each key in this map is a pattern that is replaced-all in output bundle names by
	 * its corresponding map value. Patterns are replaced by their order of appearance in
	 * the map, so always use an ordered map implementation for deterministic results. One
	 * can use an unordered map if the patterns are disjoint.
	 * <p>
	 * Useful for hiding common bundle name prefixes, for example in the bundle list.
	 */
	private final Map<Pattern, String> bundleNameReplacementPatterns = new LinkedHashMap<>();

	// ========================= IMPL: OsgiCommandSupport ==================

	/**
	 * @return
	 * @throws Exception
	 * @see org.apache.felix.karaf.shell.console.OsgiCommandSupport#doExecute()
	 */
	@Override
	protected Object doExecute() throws Exception
	{
		try
		{
			// Validate arguments
			// Add validation rules here ...

			// Acquire service references that contain the information we need
			final ServiceReference serviceRef = getBundleContext().getServiceReference(
					StartLevel.class.getName());
			final StartLevel startLevel = getStartLevel(serviceRef);

			final ServiceReference packageRef = getBundleContext().getServiceReference(
					PackageAdmin.class.getName());
			final PackageAdmin admin = getPackageAdmin(packageRef);

			// Display active start level
			if (startLevel != null)
			{
				println("START LEVEL " + startLevel.getStartLevel());
			}

			final List<Bundle> bundles = getBundles();
			final BundleRowBuilder titleBuilder = createTitleBuilder();
			final ListBundlesBuilder listBuilder = new ListBundlesBuilder()
					.setShowSingle(showSingle)
					.setTitleBuilder(titleBuilder);

			for (int i = 0; i < bundles.size(); i++)
			{
				final BundleInfo bundleInfo = new BundleInfoBundleImpl(bundles.get(i));
				if (isBundleMatches(bundleInfo))
				{
					final BundleRowBuilder rowBuilder = createRowBuilder(admin,
							bundleInfo);
					listBuilder.addBundleRowBuilder(rowBuilder);
				}
			}
			System.out.print(listBuilder.build());

			// Release service references
			getBundleContext().ungetService(serviceRef);
			getBundleContext().ungetService(packageRef);
		}
		catch (final Throwable e)
		{
			println("List bundles command failed: " + e);
			e.printStackTrace();
			if (log1.isErrorEnabled())
			{
				log1.error("List bundles command failed", e);
			}
		}
		return null;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @param bundleStateListenerFactories
	 */
	public void setBundleStateListenerFactories(
			final List<BundleStateListener.Factory> bundleStateListenerFactories)
	{
		// It seems that we can set listener references here and reuse them for all
		// subsequent doExecute() calls.
		this.bundleStateListeners.clear();
		for (final BundleStateListener.Factory factory : bundleStateListenerFactories)
		{
			final BundleStateListener listener = factory.getListener();
			if (listener != null)
			{
				if (log1.isDebugEnabled())
				{
					log1.debug("Registering listener " + listener);
				}

				this.bundleStateListeners.add(listener);
			}
		}
	}

	/**
	 * Set a new value for the bundleSymbolicNameIncludePatterns property.
	 *
	 * @param bundleSymbolicNameIncludePatterns
	 *            the bundleSymbolicNameIncludePatterns to set
	 */
	public void setBundleSymbolicNameIncludePatterns(
			final List<Pattern> bundleSymbolicNameIncludePatterns)
	{
		this.bundleSymbolicNameIncludePatterns.clear();
		this.bundleSymbolicNameIncludePatterns.addAll(bundleSymbolicNameIncludePatterns);
	}

	/**
	 * Set a new value for the bundleSymbolicNameExcludePatterns property.
	 *
	 * @param bundleSymbolicNameExcludePatterns
	 *            the bundleSymbolicNameExcludePatterns to set
	 */
	public void setBundleSymbolicNameExcludePatterns(
			final List<Pattern> bundleSymbolicNameExcludePatterns)
	{
		this.bundleSymbolicNameExcludePatterns.clear();
		this.bundleSymbolicNameExcludePatterns.addAll(bundleSymbolicNameExcludePatterns);
	}

	/**
	 * Set a new value for the bundleNameReplacementPatterns property.
	 *
	 * @param bundleNameReplacementPatterns
	 *            the bundleNameReplacementPatterns to set
	 */
	public void setBundleNameReplacementPatterns(
			final Map<Pattern, String> bundleNameReplacementPatterns)
	{
		this.bundleNameReplacementPatterns.clear();
		this.bundleNameReplacementPatterns.putAll(bundleNameReplacementPatterns);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param ref
	 * @return
	 */
	private StartLevel getStartLevel(final ServiceReference ref)
	{
		final StartLevel startLevel = (ref == null) ? null
				: (StartLevel) getBundleContext().getService(ref);
		if (startLevel == null)
		{
			println("StartLevel service is unavailable.");
		}
		return startLevel;
	}

	/**
	 * @param pkgref
	 * @return
	 */
	private PackageAdmin getPackageAdmin(final ServiceReference pkgref)
	{
		final PackageAdmin admin = (pkgref == null) ? null
				: (PackageAdmin) getBundleContext().getService(pkgref);
		if (admin == null)
		{
			println("PackageAdmin service is unavailable.");
		}
		return admin;
	}

	/**
	 * @return
	 */
	private List<Bundle> getBundles()
	{
		final Bundle[] bundles = getBundleContext().getBundles();
		return (bundles == null) ? Collections.EMPTY_LIST : Arrays.asList(bundles);
	}

	/**
	 * @param bundleInfo
	 * @return
	 */
	private boolean isBundleMatches(final BundleInfo bundleInfo)
	{
		final String symbolicName = bundleInfo.getSymbolicName();
		return (symbolicName != null)
				&& matchesOneOf(symbolicName, bundleSymbolicNameIncludePatterns)
				&& (showFullList || !matchesOneOf(symbolicName,
						bundleSymbolicNameExcludePatterns));
	}
	
	private  boolean matchesOneOf(final String s,
			final Collection<? extends Pattern> patterns)
	{
		if (s == null)
		{
			return false;
		}
		if (patterns == null)
		{
			return true;
		}
		for (final Pattern pattern : patterns)
		{
			if (pattern.matcher(s).matches())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	private BundleRowBuilder createTitleBuilder()
	{
		return new BundleRowBuilder(new BundleInfoTitleImpl(), bundleStateListeners)
				.setOpeningBracket(" ")
				.setClosingBracket(" ");
	}

	/**
	 * @param admin
	 * @param bundleInfo
	 * @return
	 */
	private BundleRowBuilder createRowBuilder(final PackageAdmin admin,
			final BundleInfo bundleInfo)
	{
		final BundleRowBuilder rowBuilder = new BundleRowBuilder(bundleInfo,
				bundleStateListeners)
				.setView(view.getBundleView())
				.setShowVersion(showVersion)
				.setShowAttachments(showAttachments)
				.setBundleNameReplacementPatterns(bundleNameReplacementPatterns);
		if (admin != null)
		{
			rowBuilder.setFragments(bundleInfo.getFragments(admin)).setHosts(
					bundleInfo.getHosts(admin));
		}
		return rowBuilder;
	}
}
