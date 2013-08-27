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
package edu.utah.further.core.api.lang;

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static java.lang.Math.max;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Properties;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.MavenPhase;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.io.SystemProperty;

/**
 * Core utilities that are likely required by the entire code and do not depend on much
 * other code.
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
 * @version Oct 13, 2008
 */
@Utility
@Api
public final class CoreUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Caches common exception object.
	 */
	private static final UnsupportedOperationException NOT_IMPLEMENTED_YET = new UnsupportedOperationException(
			"Not implemented yet");

	/**
	 * The singleton instance of this class.
	 */
	private static final CoreUtil INSTANCE = new CoreUtil();

	/**
	 * Used in reading input streams.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in a singleton class.
	 * </p>
	 */
	private CoreUtil()
	{
		super();
	}

	// ========================= METHODS ===================================

	/**
	 * Make sure this method is final. It is called by utility class constructors to
	 * prevent reflection-based attacks to instantiate such classes.
	 */
	public static final void preventUtilityConstruction()
	{
		throw new IllegalAccessError("A utility class cannot be instantiated");
	}

	/**
	 * A useful method to use in a code that is still in development.
	 *
	 * @return an {@link UnsupportedOperationException} to be thrown
	 */
	public static final UnsupportedOperationException newUnsupportedOperationExceptionNotImplementedYet()
	{
		return NOT_IMPLEMENTED_YET;
	}

	/**
	 * Locate a resource on the classpath of this class' {@link ClassLoader}
	 *
	 * @param name
	 *            resource name on the classpath
	 * @return resource as stream
	 */
	public static InputStream getResourceAsStream(final String name)
	{
		final ClassLoader cl = INSTANCE.getClass().getClassLoader();
		return (cl == null) ?
		// A system class
		ClassLoader.getSystemResourceAsStream(name)
				: cl.getResourceAsStream(name);
	}

	/**
	 * @param resourceName
	 * @return
	 */
	public static InputStream getResourceAsStreamValidate(final String resourceName)
	{
		final InputStream inputStream = CoreUtil.getResourceAsStream(resourceName);
		if (inputStream == null)
		{
			throw new IllegalArgumentException("Classpath resource "
					+ quote(resourceName) + " not found");
		}
		return inputStream;
	}

	/**
	 * Return the maven phase given by the {@link SystemProperty#TEST_PHASE} system
	 * property.
	 *
	 * @return the maven phase, or <code>null</code> if system property not found or can't
	 *         be parsed
	 */
	public static MavenPhase getMavenPhaseSystemProperty()
	{
		return MavenPhase.getValue(SystemProperty.TEST_PHASE.getValue());
	}

	/**
	 * A copy constructor of a properties object.
	 *
	 * @param original
	 *            object to copy
	 * @return copy of <code>original</code>
	 */
	public static Properties copyProperties(final Properties original)
	{
		final Properties copy = new Properties();
		for (final String key : original.stringPropertyNames())
		{
			copy.setProperty(key, original.getProperty(key));
		}
		return copy;
	}

	/**
	 * Get all values of a key set.
	 *
	 * @param properties
	 *            properties bject
	 * @param keys
	 *            keys
	 * @return corresponding property values
	 */
	public static List<String> getPropertyValues(final Properties properties,
			final String... keys)
	{
		final List<String> values = CollectionUtil.newList();
		for (final String key : keys)
		{
			values.add(properties.getProperty(key));
		}
		return values;
	}

	/**
	 * Read a system property, overriding security manager restrictions if exist.
	 *
	 * @param name
	 *            system property name
	 * @return corresponding system property value
	 */
	public static String getSystemProperty(final String name)
	{
		if (System.getSecurityManager() != null)
		{
			// Security manager turned on, override access controls
			return (String) AccessController
					.<Object> doPrivileged(new PrivilegedAction<Object>()
					{
						@Override
						public Object run()
						{
							return System.getProperty(name);
						}
					});
		}
		// Security manager turned off
		return System.getProperty(name);
	}

	/**
	 * Make a copy of a {@link Properties} object.
	 *
	 * @param properties
	 *            original object
	 * @return a copy of properties
	 */
	public static Properties newPropertiesCopy(final Properties properties)
	{
		final Properties copy = new Properties();
		copy.putAll(properties);
		return copy;
	}

	/**
	 * Calculates the exclusive OR gate of <code>n</code> of arguments.
	 *
	 * @param input
	 *            argument list
	 * @return <code>true</code> if and only if exactly one of the arguments is
	 *         <code>true</code>
	 */
	public static boolean oneTrue(final boolean... input)

	{
		return numOccurrencesEquals(true, 1, input);
	}

	/**
	 * Calculates the adjoint of an exclusive OR gate of <code>n</code> of arguments.
	 *
	 * @param input
	 *            argument list
	 * @return <code>true</code> if and only if exactly one of the arguments is
	 *         <code>false</code>
	 */
	public static boolean oneFalse(final boolean... input)

	{
		return numOccurrencesEquals(false, 1, input);
	}

	/**
	 * Calculates the gate (<code>#(argument = value) = k</code>) for <code>n</code> of
	 * arguments.
	 *
	 * @param value
	 *            value to look for
	 * @param occurrences
	 *            number of occurrences (k)
	 * @param input
	 *            argument list
	 * @return <code>true</code> if and only if exactly <code>k</code> of the arguments
	 *         equal <code>value</code>
	 */
	public static boolean numOccurrencesEquals(final boolean value,
			final int occurrences, final boolean... input)
	{
		int found = 0;
		for (final boolean argument : input)
		{
			if (argument == value)
			{
				found++;
			}
			if (found > occurrences)
			{
				return false;
			}
		}
		return (found == occurrences);
	}

	/**
	 * Read bytes from an input stream. Based on CXF's <code>IOUtils</code> class. Only
	 * use with <code>IoUtil</code> is not available.
	 *
	 * @param in
	 *            input stream
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] readBytesFromStream(final InputStream in) throws IOException
	{
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(max(in.available(),
				DEFAULT_BUFFER_SIZE));
		copy(in, bos);
		in.close();
		return bos.toByteArray();
	}

	/**
	 * @param <T>
	 * @return
	 */
	public static <T extends Cloneable> T clone(final T original)
	{
		// Security manager turned on, override access controls
		return AccessController.<T> doPrivileged(new PrivilegedAction<T>()
		{
			@Override
			public T run()
			{
				try
				{
					final Method m = Object.class.getDeclaredMethod("clone");
					final boolean originalAccessiblity = m.isAccessible();
					m.setAccessible(true);
					final T copy = (T) m.invoke(original);
					m.setAccessible(originalAccessiblity);
					return copy;
				}
				catch (final Throwable e)
				{
					throw new ApplicationException("Error invoking clone() on "
							+ original, e);
				}
			}
		});

	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Copy the contents of an input stream into an output stream. Taken from CXF's
	 * <code>IOUtils</code> class.
	 *
	 * @param input
	 *            source stream
	 * @param output
	 *            target stream
	 * @return total number of bytes read
	 * @throws IOException
	 *             if copying fails
	 */
	private static int copy(final InputStream input, final OutputStream output)
			throws IOException
	{
		return copy(input, output, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Copy the contents of an input stream into an output stream. Taken from CXF's
	 * <code>IOUtils</code> class.
	 *
	 * @param input
	 *            source stream
	 * @param output
	 *            target stream
	 * @param bufferSize
	 *            buffer size
	 * @return total number of bytes read
	 * @throws IOException
	 *             if copying fails
	 */
	private static int copy(final InputStream input, final OutputStream output,
			final int bufSize) throws IOException
	{
		int avail = input.available();
		if (avail > 262144)
		{
			avail = 262144;
		}
		int bufferSize = bufSize;
		if (avail > bufferSize)
		{
			bufferSize = avail;
		}
		final byte[] buffer = new byte[bufferSize];
		int n = 0;
		n = input.read(buffer);
		int total = 0;
		while (-1 != n)
		{
			output.write(buffer, 0, n);
			total += n;
			n = input.read(buffer);
		}
		return total;
	}

	/**
	 * Convert a {@link Integer} to an int.
	 *
	 * @param value
	 *            boxed integer
	 * @return integer value. If the argument is <code>null</code>, returns
	 *         <code>INVALID_VALUE_INTEGER</code>
	 */
	public static int getAsInt(final Integer value)
	{
		return value == null ? INVALID_VALUE_INTEGER : value.intValue();
	}
}
