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
package edu.utah.further.core.util.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

import org.springframework.core.io.Resource;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Counts the number of lines in an ASCII input stream. Note: if the input is an
 * {@link InputStream} or {@link Reader}, it will be closed or at the end-of-line after
 * using this class.
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
 * @version Jan 31, 2011
 */
public final class LineCounter
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Internal stream line scanner.
	 */
	private final Scanner scanner;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a new line counter from a ready <code>Scanner</code>.
	 * 
	 * @param scanner
	 *            A character source scanner
	 */
	public LineCounter(final Scanner scanner)
	{
		this.scanner = scanner;
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified source.
	 * 
	 * @param source
	 *            A character source implementing the {@link Readable} interface
	 */
	@SuppressWarnings("resource")
	public LineCounter(final Readable source)
	{
		this(new Scanner(source));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified input stream. Bytes from the stream are converted into characters using
	 * the underlying platform's {@linkplain java.nio.charset.Charset#defaultCharset()
	 * default charset}.
	 * 
	 * @param source
	 *            An input stream to be scanned
	 */
	@SuppressWarnings("resource")
	public LineCounter(final InputStream source)
	{
		this(new Scanner(source));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified input stream. Bytes from the stream are converted into characters using
	 * the specified charset.
	 * 
	 * @param source
	 *            An input stream to be scanned
	 * @param charsetName
	 *            The encoding type used to convert bytes from the stream into characters
	 *            to be scanned
	 * @throws IllegalArgumentException
	 *             if the specified character set does not exist
	 */
	@SuppressWarnings("resource")
	public LineCounter(final InputStream source, final String charsetName)
	{
		this(new Scanner(source, charsetName));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified file. Bytes from the file are converted into characters using the
	 * underlying platform's {@linkplain java.nio.charset.Charset#defaultCharset() default
	 * charset}.
	 * 
	 * @param source
	 *            A file to be scanned
	 * @throws FileNotFoundException
	 *             if source is not found
	 */
	@SuppressWarnings("resource")
	public LineCounter(final File source) throws FileNotFoundException
	{
		this(new Scanner(source));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified file. Bytes from the file are converted into characters using the
	 * specified charset.
	 * 
	 * @param source
	 *            A file to be scanned
	 * @param charsetName
	 *            The encoding type used to convert bytes from the file into characters to
	 *            be scanned
	 * @throws FileNotFoundException
	 *             if source is not found
	 * @throws IllegalArgumentException
	 *             if the specified encoding is not found
	 */
	@SuppressWarnings("resource")
	public LineCounter(final File source, final String charsetName)
			throws FileNotFoundException
	{
		this(new Scanner(source, charsetName));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified string.
	 * 
	 * @param source
	 *            A string to scan
	 */
	@SuppressWarnings("resource")
	public LineCounter(final String source)
	{
		this(new Scanner(source));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified channel. Bytes from the source are converted into characters using the
	 * underlying platform's {@linkplain java.nio.charset.Charset#defaultCharset() default
	 * charset}.
	 * 
	 * @param source
	 *            A channel to scan
	 */
	@SuppressWarnings("resource")
	public LineCounter(final ReadableByteChannel source)
	{
		this(new Scanner(source));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified channel. Bytes from the source are converted into characters using the
	 * specified charset.
	 * 
	 * @param source
	 *            A channel to scan
	 * @param charsetName
	 *            The encoding type used to convert bytes from the channel into characters
	 *            to be scanned
	 * @throws IllegalArgumentException
	 *             if the specified character set does not exist
	 */
	@SuppressWarnings("resource")
	public LineCounter(final ReadableByteChannel source, final String charsetName)
	{
		this(new Scanner(source, charsetName));
	}

	/**
	 * Constructs a new <code>Scanner</code> that produces values scanned from the
	 * specified channel. Bytes from the source are converted into characters using the
	 * specified charset.
	 * 
	 * @param resource
	 *            Spring resource
	 * @throws IllegalArgumentException
	 *             if the specified character set does not exist
	 */
	public LineCounter(final Resource resource)
	{
		try
		{
			this.scanner = new Scanner(resource.getInputStream());
		}
		catch (final IOException e)
		{
			throw new ApplicationException("Failed to open input stream", e);
		}
	}

	/**
	 * Close any resources uses by this LineCounter
	 */
	public void close()
	{
		if (scanner != null)
		{
			scanner.close();
		}
	}

	// ========================= METHODS ===================================

	/**
	 * Return the line count of the input stream.
	 * 
	 * @return number of lines
	 */
	public long getLineCount()
	{
		long count = 0;
		while (scanner.hasNextLine())
		{
			count++;
			scanner.nextLine();
		}
		return count;
	}
}
