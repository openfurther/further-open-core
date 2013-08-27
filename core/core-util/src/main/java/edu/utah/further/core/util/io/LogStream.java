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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ConnectException;

/**
 * A logging stream that decorates some {@link PrintStream} and also sends the output to a
 * secondary stream. Useful to suppress system output and error messages, say upon
 * {@link ConnectException}s.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Oct 18, 2009
 */
public final class LogStream extends PrintStream
{
	// ========================= FIELDS ====================================

	/**
	 * Secondary stream to send output to in addition to the original stream we wrap.
	 */
	private final OutputStream secondaryOutputStream;

	/**
	 * This flag indicates whether the original stream <code>out1</code> outputs output
	 * from this object. If <code>false</code> (the default value), only <code>out2</code>
	 * will output.
	 */
	private boolean keepOriginalStream = false;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A factory method: wrap a stream and send output to a secondary stream.
	 * 
	 * @param originalOutputStream
	 *            original output stream
	 * @param secondaryOutputStream
	 *            secondary output stream
	 * @return a logging stream instance
	 */
	public static LogStream newRedirectedStream(final OutputStream originalOutputStream,
			final OutputStream secondaryOutputStream)
	{
		return new LogStream(originalOutputStream, secondaryOutputStream);
	}

	/**
	 * A factory method: wrap a stream and send output to a {@link FileOutputStream}.
	 * 
	 * @param out
	 *            original output stream
	 * @param fileName
	 *            name of file to send messages to
	 * @return a logging stream instance
	 */
	public static LogStream newFileLogStream(final OutputStream out, final String fileName)
	{
		return new LogStream(out, fileName);
	}

	/**
	 * A factory method: suppress output from a stream. Equivalent to
	 * {@link #newRedirectedStream(OutputStream, OutputStream)} with a <code>null</code>
	 * second argument.
	 * 
	 * @param out
	 *            original output stream to suppress
	 * @return a logging stream instance
	 */
	public static LogStream newSuppressedStream(final OutputStream out)
	{
		return new LogStream(out, (OutputStream) null);
	}

	/**
	 * Wrap a stream and send output to a secondary stream.
	 * 
	 * @param out1
	 *            original output stream
	 * @param secondaryOutputStream
	 *            secondary output stream. If <code>null</code>, output from the original
	 *            stream is totally suppressed
	 */
	private LogStream(final OutputStream out1, final OutputStream secondaryOutputStream)
	{
		super(out1);
		this.secondaryOutputStream = secondaryOutputStream;
	}

	/**
	 * Wrap a stream and send output to a {@link FileOutputStream}.
	 * 
	 * @param out1
	 *            original output stream
	 * @param fileName
	 *            name of file to send messages to
	 */
	private LogStream(final OutputStream out1, final String fileName)
	{
		super(out1);
		if (fileName == null)
		{
			this.secondaryOutputStream = null;
		}
		else
		{
			try
			{
				this.secondaryOutputStream = new PrintStream(new FileOutputStream(
						fileName));
			}
			catch (final FileNotFoundException e)
			{
				throw new IllegalStateException("Could not open file output stream "
						+ fileName, e);
			}
		}
	}

	// ========================= IMPLEMENTATION: PrintStream ===============

	/**
	 * @param buf
	 * @param off
	 * @param len
	 * @see java.io.PrintStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte buf[], final int off, final int len)
	{
		try
		{
			if (keepOriginalStream)
			{
				super.write(buf, off, len);
			}
			if (secondaryOutputStream != null)
			{
				secondaryOutputStream.write(buf, off, len);
			}
		}
		catch (final Exception e)
		{
		}
	}

	/**
	 * 
	 * @see java.io.PrintStream#flush()
	 */
	@Override
	public void flush()
	{
		if (keepOriginalStream)
		{
			super.flush();
		}
		if (secondaryOutputStream != null)
		{
			try
			{
				secondaryOutputStream.flush();
			}
			catch (final IOException e)
			{
				throw new IllegalStateException(
						"Could not flush secondary output stream", e);
			}
		}
	}

	// ========================= FIELDS ====================================

	/**
	 * Return the keepOriginalStream property. This indicates whether the original stream
	 * <code>out1</code> outputs output from this object. If <code>false</code> (the
	 * default value), only <code>out2</code> will output.
	 * 
	 * @return the keepOriginalStream
	 */
	public boolean isKeepOriginalStream()
	{
		return keepOriginalStream;
	}

	/**
	 * Set a new value for the keepOriginalStream property. This flag indicates whether
	 * the original stream <code>out1</code> outputs output from this object. If
	 * <code>false</code>, only <code>out2</code> will output.
	 * 
	 * @param keepOriginalStream
	 *            the keepOriginalStream to set
	 */
	public void setKeepOriginalStream(final boolean keepOriginalStream)
	{
		this.keepOriginalStream = keepOriginalStream;
	}
}