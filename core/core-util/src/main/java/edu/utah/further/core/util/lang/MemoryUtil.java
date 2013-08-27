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
package edu.utah.further.core.util.lang;

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.text.StringUtil;

/**
 * U JVM memory management utilities.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 26, 2010
 */
@Utility
@Api
public final class MemoryUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MemoryUtil.class);

	/**
	 * Byte unit conversion factor.
	 */
	private static final long KILO_BYTE = 1024L;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private MemoryUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Print memory statistics using {@link Runtime}.
	 *
	 * @return string of basic memory statistics
	 */
	public static String printBasicMemoryStats()
	{
		final Runtime runtime = Runtime.getRuntime();
		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();

		return StringUtil
				.newStringBuilder()
				.append(NEW_LINE_STRING)
				.append(StringUtil.repeat("=", 70))
				.append(NEW_LINE_STRING)
				.append(printMemoryLine("Free memory", freeMemory))
				.append(printMemoryLine("Allocated memory", allocatedMemory))
				.append(printMemoryLine("Max memory", maxMemory))
				.append(printMemoryLine("Total free memory", freeMemory
						+ (maxMemory - allocatedMemory)))
				.append(StringUtil.repeat("=", 70))
				.append(NEW_LINE_STRING)
				.toString();
	}

	/**
	 * Print elaborate memory statistics. J2SE 5+ required.
	 *
	 * @return string of elaborate memory statistics
	 */
	public static String printMemoryManagementStats()
	{
		final StringBuilder s = StringUtil
				.newStringBuilder()
				.append(NEW_LINE_STRING)
				.append(StringUtil.repeat("=", 70))
				.append(NEW_LINE_STRING);

		s.append("DUMPING MEMORY INFO").append(NEW_LINE_STRING);
		// Read MemoryMXBean
		final MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
		s.append("Heap Memory Usage: " + memorymbean.getHeapMemoryUsage()).append(
				NEW_LINE_STRING);
		s.append("Non-Heap Memory Usage: " + memorymbean.getNonHeapMemoryUsage()).append(
				NEW_LINE_STRING);

		// Read Garbage Collection information
		final List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
				.getGarbageCollectorMXBeans();
		for (final GarbageCollectorMXBean gcmbean : gcmbeans)
		{
			s.append(NEW_LINE_STRING)
					.append("Name: " + gcmbean.getName())
					.append(NEW_LINE_STRING);
			s.append("Collection count: " + gcmbean.getCollectionCount()).append(
					NEW_LINE_STRING);
			s.append("Collection time: " + gcmbean.getCollectionTime()).append(
					NEW_LINE_STRING);
			s.append("Memory Pools: ").append(NEW_LINE_STRING);
			final String[] memoryPoolNames = gcmbean.getMemoryPoolNames();
			for (int i = 0; i < memoryPoolNames.length; i++)
			{
				s.append("\t" + memoryPoolNames[i]).append(NEW_LINE_STRING);
			}
		}

		// Read Memory Pool Information
		s.append("Memory Pools Info");
		final List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
				.getMemoryPoolMXBeans();
		for (final MemoryPoolMXBean mempoolmbean : mempoolsmbeans)
		{
			s.append(NEW_LINE_STRING)
					.append("Name: " + mempoolmbean.getName())
					.append(NEW_LINE_STRING);
			s.append("Usage: " + mempoolmbean.getUsage()).append(NEW_LINE_STRING);
			s.append("Collection Usage: " + mempoolmbean.getCollectionUsage()).append(
					NEW_LINE_STRING);
			s.append("Peak Usage: " + mempoolmbean.getPeakUsage())
					.append(NEW_LINE_STRING);
			s.append("Type: " + mempoolmbean.getType()).append(NEW_LINE_STRING);
			s.append("Memory Manager Names: ").append(NEW_LINE_STRING);
			final String[] memManagerNames = mempoolmbean.getMemoryManagerNames();
			for (int i = 0; i < memManagerNames.length; i++)
			{
				s.append(Strings.TAB_STRING + memManagerNames[i]).append(NEW_LINE_STRING);
			}
			s.append("\n");
		}

		return s.toString();
	}

	/**
	 * Convert bytes to kilobytes.
	 *
	 * @param bytes
	 *            number of bytes
	 * @return corresponding number of kilobytes
	 */
	public static long toKiloBytes(final long bytes)
	{
		return bytes / KILO_BYTE;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param title
	 * @param freeMemory
	 * @return
	 */
	private static String printMemoryLine(final String title, final long freeMemory)
	{
		return String.format("%-20s: %dK", title, new Long(toKiloBytes(freeMemory)))
				+ NEW_LINE_STRING;
	}
}
