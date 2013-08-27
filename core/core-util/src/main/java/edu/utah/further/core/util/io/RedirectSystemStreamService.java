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
package edu.utah.further.core.util.io;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.io.LogLevel;
import edu.utah.further.core.api.io.SystemOutputStream;

/**
 * Similar to {@link SuppressSystemStreamAspect}, but redirects all relevant system
 * streams to log stream when created, for the duration of the aspect's life, instead of
 * during annotation method execution only.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 11, 2010
 */
public class RedirectSystemStreamService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RedirectSystemStreamService.class);

	// ========================= FIELDS ====================================

	/**
	 * Keeps track of redirected streams.
	 */
	private volatile List<SystemOutputStream> redirectedStreams = CollectionUtil
			.newList();

	/**
	 * Backs up the original system output streams.
	 */
	private volatile Map<SystemOutputStream, PrintStream> originalStreams = CollectionUtil
			.newMap();

	// ========================= CONSTRUCTORS ==============================
	/**
	 * Redirect all system output streams to log streams.
	 */
	@SuppressWarnings("resource")
	@PostConstruct
	private void redirectStreams()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Redirecting streams");
		}

		// Create map of system-stream-enum-to-original-streams. Use default log levels.
		for (final SystemOutputStream stream : SystemOutputStream.values())
		{
			final LogLevel level = stream.getDefaultLogLevel();

			// Save original stream
			redirectedStreams.add(stream);
			final PrintStream out = stream.getStream();
			originalStreams.put(stream, out);

			// Redirect output to suppressed LogStream
			stream.setStream(LogStream.newRedirectedStream(out, new LoggingOutputStream(
					log, level)));
		}
	}

	/**
	 * Restore all system streams.
	 */
	@PreDestroy
	private void restoreStreams()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Restoring streams");
		}

		// Restore original streams
		for (final SystemOutputStream stream : redirectedStreams)
		{
			stream.setStream(originalStreams.get(stream));
		}
	}

	// ========================= POINTCUTS =================================

	// ========================= METHODS ===================================

	// ========================= PRIVATE METHODS ===========================

}
