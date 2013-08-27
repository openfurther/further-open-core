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
package edu.utah.further.core.api.io;

import java.io.PrintStream;

import edu.utah.further.core.api.context.Api;

/**
 * A useful facade enumeration of {@link System} output streams. Used for example by
 * {@link SuppressSystemStream}.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Api
public enum SystemOutputStream
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Standard output.
	 */
	OUT(LogLevel.INFO)
	{
		/**
		 * Return the corresponding {@link System} output stream.
		 *
		 * @return the corresponding {@link System} output stream
		 * @see edu.utah.further.core.api.io.SystemOutputStream#getStream()
		 */
		@Override
		public PrintStream getStream()
		{
			return System.out;
		}

		/**
		 * Reassigns the corresponding {@link System} standard output stream.
		 *
		 * <p>
		 * First, if there is a security manager, its <code>checkPermission</code> method
		 * is called with a <code>RuntimePermission("setIO")</code> permission to see if
		 * it's ok to reassign the "standard" output stream.
		 *
		 * @param out
		 *            the new standard output stream
		 *
		 * @throws SecurityException
		 *             if a security manager exists and its <code>checkPermission</code>
		 *             method doesn't allow reassigning of the standard output stream.
		 *
		 * @see SecurityManager#checkPermission
		 * @see java.lang.RuntimePermission
		 * @see System#setOut(PrintStream)
		 */
		@Override
		public void setStream(final PrintStream out)
		{
			System.setOut(out);
		}
	},

	/**
	 * Standard error.
	 */
	ERR(LogLevel.ERROR)
	{
		/**
		 * Return the corresponding {@link System} output stream.
		 *
		 * @return the corresponding {@link System} output stream
		 * @see edu.utah.further.core.api.io.SystemOutputStream#getStream()
		 */
		@Override
		public PrintStream getStream()
		{
			return System.err;
		}

		/**
		 * Reassigns the corresponding {@link System} standard error stream.
		 *
		 * <p>
		 * First, if there is a security manager, its <code>checkPermission</code> method
		 * is called with a <code>RuntimePermission("setIO")</code> permission to see if
		 * it's ok to reassign the "standard" output stream.
		 *
		 * @param out
		 *            the new standard error stream
		 *
		 * @throws SecurityException
		 *             if a security manager exists and its <code>checkPermission</code>
		 *             method doesn't allow reassigning of the standard output stream.
		 *
		 * @see SecurityManager#checkPermission
		 * @see java.lang.RuntimePermission
		 * @see System#setOut(PrintStream)
		 */
		@Override
		public void setStream(final PrintStream out)
		{
			System.setErr(out);
		}
	};

	// ========================= FIELDS ====================================

	/**
	 * Default log level to redirect this output stream to.
	 */
	private final LogLevel defaultLogLevel;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param defaultLogLevel
	 */
	private SystemOutputStream(final LogLevel defaultLogLevel)
	{
		this.defaultLogLevel = defaultLogLevel;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the corresponding {@link System} output stream.
	 *
	 * @return the corresponding {@link System} output stream
	 */
	public abstract PrintStream getStream();

	/**
	 * Reassigns the corresponding {@link System} stream.
	 *
	 * <p>
	 * First, if there is a security manager, its <code>checkPermission</code> method is
	 * called with a <code>RuntimePermission("setIO")</code> permission to see if it's ok
	 * to reassign the "standard" output stream.
	 *
	 * @param out
	 *            the new system stream
	 *
	 * @throws SecurityException
	 *             if a security manager exists and its <code>checkPermission</code>
	 *             method doesn't allow reassigning of the standard output stream.
	 *
	 * @see SecurityManager#checkPermission
	 * @see java.lang.RuntimePermission
	 */
	public abstract void setStream(PrintStream out);

	/**
	 * Return the defaultLogLevel property.
	 *
	 * @return the defaultLogLevel
	 */
	public LogLevel getDefaultLogLevel()
	{
		return defaultLogLevel;
	}
}
