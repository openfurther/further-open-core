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

import static edu.utah.further.osgi.shell.ShellConstants.COLUMN_DELIMITER;
import static edu.utah.further.osgi.shell.ShellConstants.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that builds the output of the {@link ListBundlesCommand} (follows the Builder
 * Pattern).
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
final class ListBundlesBuilder
{
	// ========================= COSNTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Keeps track of the title line display.
	 */
	private BundleRowBuilder titleBuilder;

	/**
	 * Keeps track of bundle displays.
	 */
	private final List<BundleRowBuilder> bundleRowBuilders = new ArrayList<>();

	// ========================= DEPENDENCIES ==============================

	/**
	 * Display a single-column view. If this option is not specified, a double-column view
	 * is displayed.
	 */
	private boolean showSingle = false;

	// ========================= METHODS ===================================

	/**
	 * Builds the list command output and returns it.
	 * 
	 * @return the list command output, based on all information gathered by this builder
	 *         instance
	 */
	public String build()
	{
		if (bundleRowBuilders.isEmpty())
		{
			return "There are no installed bundles.";
		}
		final StringBuilder line = new StringBuilder();
		if (showSingle)
		{
			// Single-column view. For each bundle, display all generated lines, separated
			// by new lines
			if (titleBuilder != null)
			{
				final StringBuilder titleLine = titleBuilder.build().get(0);
				line.append(titleLine).append(System.getProperty("line.separator"));
			}
			for (final BundleRowBuilder rowBuilder : bundleRowBuilders)
			{
				for (final StringBuilder row : rowBuilder.build())
				{
					line.append(row).append(System.getProperty("line.separator"));
				}
			}
		}
		else
		{
			// Double-column view. Show only the first row per bundle and truncate it as
			// needed.
			final int numColumns = 2;
			final int columnWidth = (SCREEN_WIDTH - (numColumns - 1)
					* COLUMN_DELIMITER.length())
					/ numColumns;

			// Title line, tiled
			if (titleBuilder != null)
			{
				final StringBuilder titleLine = titleBuilder.build().get(0);
				final List<StringBuilder> titleTiled = new ArrayList<>();
				for (int col = 0; col < numColumns; col++)
				{
					titleTiled.add(titleLine);
				}
				appendMultiColumnRow(line, columnWidth, COLUMN_DELIMITER, titleTiled);
				line.append(System.getProperty("line.separator"));
			}

			// Bundle rows
			final int numBundles = bundleRowBuilders.size();
			final int numRows = (int) Math.ceil(((double) numBundles) / numColumns);
			for (int row = 0; row < numRows; row++)
			{
				final List<StringBuilder> relevantBundles = new ArrayList<>();
				for (int col = 0; col < numColumns; col++)
				{
					final int index = numRows * col + row;
					if (index < numBundles)
					{
						relevantBundles.add(bundleRowBuilders.get(index).build().get(0));
					}
				}
				appendMultiColumnRow(line, columnWidth, COLUMN_DELIMITER, relevantBundles);
				line.append(System.getProperty("line.separator"));
			}
		}
		return line.toString();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the showSingle property.
	 * 
	 * @param showSingle
	 *            the showSingle to set
	 * @return this object, for method chaining
	 */
	public ListBundlesBuilder setShowSingle(final boolean showSingle)
	{
		this.showSingle = showSingle;
		return this;
	}

	/**
	 * @param rowBuilder
	 * @return this object, for method chaining
	 * @see java.util.List#add(java.lang.Object)
	 */
	public ListBundlesBuilder addBundleRowBuilder(final BundleRowBuilder rowBuilder)
	{
		bundleRowBuilders.add(rowBuilder);
		return this;
	}

	/**
	 * Set a new value for the titleBuilder property.
	 * 
	 * @param titleBuilder
	 *            the titleBuilder to set
	 * @return this object, for method chaining
	 */
	public ListBundlesBuilder setTitleBuilder(final BundleRowBuilder titleBuilder)
	{
		this.titleBuilder = titleBuilder;
		return this;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param line
	 * @param columnWidth
	 * @param columns
	 */
	private void appendMultiColumnRow(final StringBuilder line, final int columnWidth,
			final String delimiter, final List<StringBuilder> columns)
	{
		final String columnFormat = "%-" + columnWidth + "s";
		final int numColumns = columns.size();
		for (int i = 0; i < numColumns; i++)
		{
			final StringBuilder column = columns.get(i);
			line.append(String.format(columnFormat,
					column.substring(0, Math.min(column.length(), columnWidth))));
			if (i < numColumns - 1)
			{
				line.append(delimiter);
			}
		}
	}
}
