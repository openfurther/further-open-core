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
package edu.utah.further.core.util.options;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * This class holds the information for a <i>set</i> of options. A set can hold any number
 * of <code>OptionData</code> instances which are checked together to determine success or
 * failure.
 * <p>
 * The approach to use this class looks like this:
 * <p>
 * <ol>
 * <li>The user uses any of the <code>Options.addSet()</code> (e. g.
 * {@link Options#addSet(String)}) to create any number of sets required (or just relies
 * on the default set, if only one set is required)
 * <li>The user adds all required option definitions to each set
 * <li>Using any of the <code>Options.check()</code> methods, each set can be checked
 * whether the options that were specified on the command line satisfy its requirements
 * <li>If the check was successful for a given set, several data items are available from
 * this class:
 * <ul>
 * <li>All options defined for the set (through with e. g. values, details, and
 * multiplicity are available)
 * <li>All data items found (these are the items on the command line which do not start
 * with the prefix, i. e. non-option arguments)
 * <li>All unmatched arguments on the command line (these are the items on the command
 * line which start with the prefix, but do not match to one of the options). Programs can
 * elect to ignore these, or react with an error
 * </ul>
 * </ol>
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
 * @version Nov 19, 2008
 */
public class OptionSet
{

	private static final String CLASS = "OptionSet";

	private final List<OptionData> options = CollectionUtil.newList();
	private final Map<String, OptionData> keys = CollectionUtil.newMap();
	private final List<String> unmatched = CollectionUtil.newList();
	private final List<String> data = CollectionUtil.newList();
	private String setName = null;
	private int minData = 0;
	private int maxData = 0;
	private Options.Prefix prefix = null;
	private Options.Multiplicity defaultMultiplicity = null;

	/**
	 * Constructor.
	 */
	OptionSet(final Options.Prefix prefix,
			final Options.Multiplicity defaultMultiplicity, final String setName,
			final int minData, final int maxData)
	{
		if (setName == null)
			throw new IllegalArgumentException(CLASS + ": setName may not be null");
		if (minData < 0)
			throw new IllegalArgumentException(CLASS + ": minData must be >= 0");
		if (maxData < minData)
			throw new IllegalArgumentException(CLASS + ": maxData must be >= minData");
		this.prefix = prefix;
		this.defaultMultiplicity = defaultMultiplicity;
		this.setName = setName;
		this.minData = minData;
		this.maxData = maxData;
	}

	/**
	 * Get a list of all the options defined for this set
	 * <p>
	 *
	 * @return A list of {@link OptionData} instances defined for this set
	 */

	public List<OptionData> getOptionData()
	{
		return options;
	}

	/**
	 * Get the data for a specific option, identified by its key name (which is unique)
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 *            <p>
	 * @return The {@link OptionData} instance
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or unknown in this set
	 */
	public OptionData getOption(final String key)
	{
		if (key == null)
			throw new IllegalArgumentException(CLASS + ": key may not be null");
		if (!keys.containsKey(key))
			throw new IllegalArgumentException(CLASS + ": unknown key: " + key);
		return keys.get(key);
	}

	/**
	 * Check whether a specific option is set, i. e. whether it was specified at least
	 * once on the command line.
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 *            <p>
	 * @return <code>true</code> or <code>false</code>, depending on the outcome of the
	 *         check
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or unknown in this set
	 */

	public boolean isSet(final String key)
	{
		if (key == null)
			throw new IllegalArgumentException(CLASS + ": key may not be null");
		if (!keys.containsKey(key))
			throw new IllegalArgumentException(CLASS + ": unknown key: " + key);
		return keys.get(key).getResultCount() > 0 ? true : false;
	}

	/**
	 * Getter method for <code>setName</code> property
	 * <p>
	 *
	 * @return The value for the <code>setName</code> property
	 */

	public String getSetName()
	{
		return setName;
	}

	/**
	 * Getter method for <code>minData</code> property
	 * <p>
	 *
	 * @return The value for the <code>minData</code> property
	 */

	public int getMinData()
	{
		return minData;
	}

	/**
	 * Getter method for <code>maxData</code> property
	 * <p>
	 *
	 * @return The value for the <code>maxData</code> property
	 */

	public int getMaxData()
	{
		return maxData;
	}

	/**
	 * Return the data items found (these are the items on the command line which do not
	 * start with the prefix, i. e. non-option arguments)
	 * <p>
	 *
	 * @return A list of strings with all data items found
	 */

	public List<String> getData()
	{
		return data;
	}

	/**
	 * Return all unmatched items found (these are the items on the command line which
	 * start with the prefix, but do not match to one of the options)
	 * <p>
	 *
	 * @return A list of strings with all unmatched items found
	 */
	public List<String> getUnmatched()
	{
		return unmatched;
	}

	/**
	 * Add a non-value option with the given key, and the default prefix and multiplicity
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined
	 */
	public OptionSet addOption(final String key)
	{
		return addOption(key, defaultMultiplicity);
	}

	/**
	 * Add a non-value option with the given key and multiplicity, and the default prefix
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 * @param multiplicity
	 *            The multiplicity for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined or if <code>multiplicity</code> is
	 *             <code>null</code>
	 */
	public OptionSet addOption(final String key, final Options.Multiplicity multiplicity)
	{
		return addOption(key, false, Options.Separator.NONE, false, multiplicity);
	}

	/**
	 * Add a value option with the given key and separator, no details, and the default
	 * prefix and multiplicity
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 * @param separator
	 *            The separator for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined or if <code>separator</code> is
	 *             <code>null</code>
	 */
	public OptionSet addOption(final String key, final Options.Separator separator)
	{
		return addOption(key, false, separator, true, defaultMultiplicity);
	}

	/**
	 * Add a value option with the given key, separator, and multiplicity, no details, and
	 * the default prefix
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 * @param separator
	 *            The separator for the option
	 * @param multiplicity
	 *            The multiplicity for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined or if <code>separator</code> or
	 *             <code>multiplicity</code> are <code>null</code>
	 */
	public OptionSet addOption(final String key, final Options.Separator separator,
			final Options.Multiplicity multiplicity)
	{
		return addOption(key, false, separator, true, multiplicity);
	}

	/**
	 *
	 * Add a value option with the given key and separator, possibly details, and the
	 * default prefix and multiplicity
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 * @param details
	 *            A boolean indicating whether details are expected for the option
	 * @param separator
	 *            The separator for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined or if <code>separator</code> is
	 *             <code>null</code>
	 */

	public OptionSet addOption(final String key, final boolean details,
			final Options.Separator separator)
	{
		return addOption(key, details, separator, true, defaultMultiplicity);
	}

	/**
	 * Add a value option with the given key, separator, and multiplicity, possibly
	 * details, and the default prefix
	 * <p>
	 *
	 * @param key
	 *            The key for the option
	 * @param details
	 *            A boolean indicating whether details are expected for the option
	 * @param separator
	 *            The separator for the option
	 * @param multiplicity
	 *            The multiplicity for the option
	 *            <p>
	 * @return The set instance itself (to support invocation chaining for
	 *         <code>addOption()</code> methods)
	 *         <p>
	 * @throws IllegalArgumentException
	 *             If the <code>key</code> is <code>null</code> or a key with this name
	 *             has already been defined or if <code>separator</code> or
	 *             <code>multiplicity</code> are <code>null</code>
	 */
	public OptionSet addOption(final String key, final boolean details,
			final Options.Separator separator, final Options.Multiplicity multiplicity)
	{
		return addOption(key, details, separator, true, multiplicity);
	}

	/**
	 * The master method to add an option. Since there are combinations which are not
	 * acceptable (like a NONE separator and a true value), this method is not public.
	 * Internally, we only supply acceptable combinations.
	 */
	OptionSet addOption(final String key, final boolean details,
			final Options.Separator separator, final boolean value,
			final Options.Multiplicity multiplicity)
	{

		if (key == null)
			throw new IllegalArgumentException(CLASS + ": key may not be null");
		if (multiplicity == null)
			throw new IllegalArgumentException(CLASS + ": multiplicity may not be null");
		if (separator == null)
			throw new IllegalArgumentException(CLASS + ": separator may not be null");
		if (keys.containsKey(key))
			throw new IllegalArgumentException(CLASS + ": the key " + key
					+ " has already been defined for this OptionSet");

		final OptionData od = new OptionData(prefix, key, details, separator, value,
				multiplicity);
		options.add(od);
		keys.put(key, od);

		return this;

	}

}
