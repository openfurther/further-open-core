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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.service.startlevel.StartLevel;
import org.slf4j.Logger;

import edu.utah.further.osgi.shell.ListBundlesCommand.BundleView;

/**
 * A class that builds the part of {@link ListBundlesCommand}'s output pertaining to a
 * single bundle.
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
 * @version Mar 8, 2010
 */
final class BundleRowBuilder
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(BundleRowBuilder.class);

	// ========================= FIELDS ====================================

	/**
	 * Bundle fragments output line.
	 */
	private StringBuilder fragmentsLine = new StringBuilder();

	/**
	 * Bundle hosts output line.
	 */
	private StringBuilder hostsLine = new StringBuilder();

	/**
	 * Holds the bundle name display part.
	 */
	private String namePart;

	/**
	 * Holds the bundle version display part.
	 */
	private String versionPart;

	/**
	 * Holds the level version display part.
	 */
	private String levelPart;

	/**
	 * Open bracket character for most fields.
	 */
	private String openingBracket = "[";

	/**
	 * Closing bracket character for most fields.
	 */
	private String closingBracket = "]";

	/**
	 * Each key in this map is a pattern that is replaced-all in output bundle names by
	 * its corresponding map value. Patterns are replaced by their order of appearance in
	 * the map, so use an ordered map
	 */
	private final Map<Pattern, String> bundleNameReplacementPatterns = new LinkedHashMap<>();

	/**
	 * Keeps track of the set bundle view.
	 */
	private BundleView view;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Bundle information to print.
	 */
	private final BundleInfo bundleInfo;

	/**
	 * Bundle listeners. Each one adds a column with information on a particular aspect of
	 * the bundle state.
	 */
	private final List<BundleStateListener> bundleStateListeners;

	/**
	 * Controls whether bundle attachment lines are shown (fragments and hosts
	 * information).
	 */
	private boolean showAttachments;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param bundleInfo
	 * @param bundleStateListeners
	 */
	public BundleRowBuilder(final BundleInfo bundleInfo,
			final List<BundleStateListener> bundleStateListeners)
	{
		super();
		this.bundleInfo = bundleInfo;
		this.bundleStateListeners = bundleStateListeners;

		// Default parameter setting
		setView(BundleView.NAME);
		setShowVersion(false);
		setShowStartLevel(false, null);
	}

	// ========================= METHODS ===================================

	/**
	 * Builds the list command output and returns it.
	 * 
	 * @return the list command output, based on all information gathered by this builder
	 *         instance
	 */
	public List<StringBuilder> build()
	{
		final List<StringBuilder> lines = new ArrayList<>();

		// Construct main row
		lines.add(createMainLine());

		// Append optional attachment lines
		if (showAttachments)
		{
			if (fragmentsLine.length() > 0)
			{
				lines.add(fragmentsLine);
			}
			if (hostsLine.length() > 0)
			{
				lines.add(hostsLine);
			}
		}

		return lines;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the view property.
	 * 
	 * @param view
	 *            the view to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setView(final BundleView view)
	{
		this.view = view;
		switch (view)
		{
			case NAME:
			{
				setViewToName();
				break;
			}

			case LOCATION:
			{
				setViewToLocation();
				break;
			}

			case SYMBOLIC_NAME:
			{
				setViewToSymbolicName();
				break;
			}

			case UPDATE_LOCATION:
			{
				setViewToUpdateLocation();
				break;
			}

			default:
			{
				break;
			}
		}

		return this;
	}

	/**
	 * Set a new value for the showVersion property.
	 * 
	 * @param showVersion
	 *            the showVersion to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setShowVersion(final boolean showVersion)
	{
		if (showVersion)
		{
			final String version = bundleInfo.getVersion();
			versionPart = (version == null) ? "" : "(" + version + ")";
		}
		else
		{
			versionPart = "";
		}
		return this;
	}

	/**
	 * Set a new value for the showStartLevel property.
	 * 
	 * @param showStartLevel
	 *            the showStartLevel to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setShowStartLevel(final boolean showStartLevel,
			final StartLevel startLevel)
	{
		if (showStartLevel)
		{
			final String level = bundleInfo.getStartLevel(startLevel);
			levelPart = openingBracket + level + closingBracket;
		}
		else
		{
			levelPart = "";
		}
		return this;
	}

	/**
	 * Set a new value for the showAttachments property.
	 * 
	 * @param showAttachments
	 *            the showAttachments to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setShowAttachments(final boolean showAttachments)
	{
		this.showAttachments = showAttachments;
		return this;
	}

	/**
	 * @param fragments
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setFragments(final List<Bundle> fragments)
	{
		if (!fragments.isEmpty())
		{
			fragmentsLine = buildAttachmentLine(fragments, "Fragments");
		}
		return this;
	}

	/**
	 * @param hosts
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setHosts(final List<Bundle> hosts)
	{
		if (!hosts.isEmpty())
		{
			hostsLine = buildAttachmentLine(hosts, "Hosts");
		}
		return this;
	}

	/**
	 * Set a new value for the openingBracket property.
	 * 
	 * @param openingBracket
	 *            the openingBracket to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setOpeningBracket(final String openingBracket)
	{
		this.openingBracket = openingBracket;
		return this;
	}

	/**
	 * Set a new value for the closingBracket property.
	 * 
	 * @param closingBracket
	 *            the closingBracket to set
	 * @return this object, for method chaining
	 */
	public BundleRowBuilder setClosingBracket(final String closingBracket)
	{
		this.closingBracket = closingBracket;
		return this;
	}

	/**
	 * Set a new value for the bundleNameReplacementPatterns property.
	 * 
	 * @param bundleNameReplacementPatterns
	 *            the bundleNameReplacementPatterns to set
	 */
	public BundleRowBuilder setBundleNameReplacementPatterns(
			final Map<Pattern, String> bundleNameReplacementPatterns)
	{
		this.bundleNameReplacementPatterns.clear();
		this.bundleNameReplacementPatterns.putAll(bundleNameReplacementPatterns);

		// May need to update name field, so set the view to the current view
		setView(view);

		return this;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param s
	 * @return
	 */
	private static String getSeparator(final String s)
	{
		return ((s == null) || s.isEmpty()) ? "" : " ";
	}

	/**
	 * @param attachments
	 * @param title
	 * @return
	 */
	private StringBuilder buildAttachmentLine(final List<Bundle> attachments,
			final String title)
	{
		return (attachments == null) ? null : new StringBuilder().append(
				String.format("%40s%s:", "", title)).append(
				chain(ShellUtil.getBundleIds(attachments), ","));
	}

	/**
	 *
	 */
	private void setViewToName()
	{
		// Get the bundle name or location.
		namePart = getBundleName();

		// If there is no name, fall back to symbolic name.
		namePart = (namePart == null) ? bundleInfo.getSymbolicName() : namePart;

		// If there is no symbolic name, fall back to location.
		namePart = (namePart == null) ? bundleInfo.getLocation() : namePart;
	}

	/**
	 * @return
	 */
	private String getBundleName()
	{
		String name = bundleInfo.getName();
		if (name == null)
		{
			return null;
		}
		// Replace patterns - useful for hiding common bundle name prefixes, for example.
		// Note that the map is iterated over in its natural order, depending on its
		// implementation.
		for (final Map.Entry<Pattern, String> replace : bundleNameReplacementPatterns
				.entrySet())
		{
			final Pattern key = replace.getKey();
			final String value = replace.getValue();
			if (log.isDebugEnabled())
			{
				log.debug("Bundle name " + name + ", replacing regexp " + key + " by "
						+ value);
			}
			name = name.replaceAll(key.pattern(), value);
		}
		return name;
	}

	/**
	 *
	 */
	private void setViewToLocation()
	{
		namePart = bundleInfo.getLocation();
		setShowVersion(false);
	}

	/**
	 *
	 */
	private void setViewToSymbolicName()
	{
		namePart = bundleInfo.getSymbolicName();
		namePart = (namePart == null) ? "<no symbolic name>" : namePart;
	}

	/**
	 *
	 */
	private void setViewToUpdateLocation()
	{
		namePart = bundleInfo.getUpdateLocation();
		namePart = (namePart == null) ? bundleInfo.getLocation() : namePart;
		setShowVersion(false);
	}

	/**
	 * @param format
	 * @param args
	 * @return
	 */
	private String formatWithBrackets(final String format, final Object... args)
	{
		return openingBracket + String.format(format, args) + closingBracket;
	}

	/**
	 * @return
	 */
	private StringBuilder createMainLine()
	{
		// Mandatory left-most columns
		final StringBuilder line = new StringBuilder()
				.append(formatWithBrackets("%-4s", bundleInfo.getBundleId()))
				.append(" ")
				.append(formatWithBrackets("%-11s", bundleInfo.getState()));

		// Optional columns built from listeners
		for (final BundleStateListener listener : bundleStateListeners)
		{
			final String state = bundleInfo.getState(listener);
			line.append(" ").append(
					formatWithBrackets("%-" + listener.getName().length() + "s",
							(state == null) ? "" : state));
		}

		// Right-most columns (some optional, some not)
		line.append(getSeparator(levelPart))
				.append(levelPart)
				.append(getSeparator(namePart))
				.append(namePart)
				.append(getSeparator(versionPart))
				.append(versionPart);
		return line;
	}

	/**
	 * Chain all the elements of a collection into a single {@link String} with a
	 * delimiter according to the collection's iteration order.
	 * <p>
	 * If the given array empty an empty string will be returned. Null elements of the
	 * array are allowed and will be treated like empty Strings.
	 * 
	 * @param collection
	 *            collection to be joined into a string.
	 * @param delimiter
	 *            string to place between list elements
	 * @return concatenation of all the elements of the given list with the the delimiter
	 *         in between
	 */
	private String chain(final Collection<?> collection, final String delimiter)
	{
		// Nothing in the array return empty string
		// has the side effect of throwing a NullPointerException if
		// the array is null.
		if ((collection == null) || collection.isEmpty())
		{
			return "";
		}

		// Iterate over collection and concatenate elements into a string builder
		final Iterator<?> iterator = collection.iterator();
		final StringBuilder result = new StringBuilder();
		while (iterator.hasNext())
		{
			final Object element = iterator.next();
			if (element != null)
			{
				result.append(element);
			}
			if (iterator.hasNext())
			{
				result.append(delimiter);
			}
		}

		return result.toString();
	}
}
