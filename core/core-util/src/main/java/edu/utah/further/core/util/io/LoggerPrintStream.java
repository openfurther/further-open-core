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

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.newUnsupportedOperationExceptionNotImplementedYet;
import static edu.utah.further.core.util.io.LoggingUtil.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.io.LogLevel;

/**
 * An object adapter of an SLF4J{@link Logger} to a {@link PrintStream}. The latter does
 * not have an interface, so for now we extend it. One could extract an interface from
 * {@link PrintStream} in the future if necessary.
 * <p>
 * Only the <code>print()</code> methods are currently overridden and supported. The rest
 * thrown an {@link UnsupportedOperationException}.
 * <p>
 * Not an efficient implementation: uses "EMPTY_STRING + x" to print a non-string object
 * x. -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 7, 2010
 */
public final class LoggerPrintStream extends PrintStream
{
	// ========================= CONSTANTS =================================

	/**
	 * Logger to adapt.
	 */
	private final Logger logger;

	/**
	 * Printouts' logging level.
	 */
	private final LogLevel level;

	// ========================= CONSTANTS =================================

	/**
	 * @param out
	 * @param logger
	 * @param level
	 */
	public LoggerPrintStream(final OutputStream out, final Logger logger,
			final LogLevel level)
	{
		super(out);
		this.logger = logger;
		this.level = level;
	}

	// ========================= IMPL: PrintStream =========================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(boolean)
	 */
	@Override
	public void print(final boolean b)
	{
		log(logger, level, EMPTY_STRING + b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(char)
	 */
	@Override
	public void print(final char c)
	{
		log(logger, level, EMPTY_STRING + c);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(int)
	 */
	@Override
	public void print(final int i)
	{
		log(logger, level, EMPTY_STRING + i);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(long)
	 */
	@Override
	public void print(final long l)
	{
		log(logger, level, EMPTY_STRING + l);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(float)
	 */
	@Override
	public void print(final float f)
	{
		log(logger, level, EMPTY_STRING + f);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(double)
	 */
	@Override
	public void print(final double d)
	{
		log(logger, level, EMPTY_STRING + d);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(char[])
	 */
	@Override
	public void print(final char[] s)
	{
		log(logger, level, new String(s));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(java.lang.String)
	 */
	@Override
	public void print(final String s)
	{
		log(logger, level, s);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(java.lang.Object)
	 */
	@Override
	public void print(final Object obj)
	{
		log(logger, level, (obj == null) ? Strings.NULL_TO_STRING : obj.toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println()
	 */
	@Override
	public void println()
	{
		log(logger, level, EMPTY_STRING);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(boolean)
	 */
	@Override
	public void println(final boolean x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(char)
	 */
	@Override
	public void println(final char x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(int)
	 */
	@Override
	public void println(final int x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(long)
	 */
	@Override
	public void println(final long x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(float)
	 */
	@Override
	public void println(final float x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(double)
	 */
	@Override
	public void println(final double x)
	{
		log(logger, level, EMPTY_STRING + x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(char[])
	 */
	@Override
	public void println(final char[] x)
	{
		log(logger, level, new String(x));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(java.lang.String)
	 */
	@Override
	public void println(final String x)
	{
		log(logger, level, x);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(java.lang.Object)
	 */
	@Override
	public void println(final Object x)
	{
		log(logger, level, (x == null) ? Strings.NULL_TO_STRING : x.toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#printf(java.lang.String, java.lang.Object[])
	 */
	@Override
	public PrintStream printf(final String format, final Object... args)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#printf(java.util.Locale, java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public PrintStream printf(final Locale l, final String format, final Object... args)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#flush()
	 */
	@Override
	public void flush()
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#close()
	 */
	@Override
	public void close()
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#checkError()
	 */
	@Override
	public boolean checkError()
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#setError()
	 */
	@Override
	protected void setError()
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#clearError()
	 */
	@Override
	protected void clearError()
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#write(int)
	 */
	@Override
	public void write(final int b)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte[] buf, final int off, final int len)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#format(java.lang.String, java.lang.Object[])
	 */
	@Override
	public PrintStream format(final String format, final Object... args)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#format(java.util.Locale, java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public PrintStream format(final Locale l, final String format, final Object... args)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#append(java.lang.CharSequence)
	 */
	@Override
	public PrintStream append(final CharSequence csq)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public PrintStream append(final CharSequence csq, final int start, final int end)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#append(char)
	 */
	@Override
	public PrintStream append(final char c)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(final byte[] b) throws IOException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

}
