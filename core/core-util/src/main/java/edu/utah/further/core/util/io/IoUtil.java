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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStream;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.message.Messages.inputOutputErrorMessage;
import static edu.utah.further.core.api.text.StringUtil.newStringBuilder;
import static edu.utah.further.core.api.text.StringUtil.stripNewLinesAndTabs;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.io.SystemProperty;
import edu.utah.further.core.api.text.StringUtil;

/**
 * I/O and classpath-related utilities.
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
 * @version May 29, 2009
 * @see http://java.sun.com/docs/books/tutorial/essential/environment/sysprop.html
 */
@Utility
public final class IoUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(IoUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private IoUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the list of files in a directory.
	 * 
	 * @param directoryName
	 *            directory name relative to the path where the JVM was started
	 * @return list of children files of the directory
	 */
	public static String[] directoryListing(final String directoryName)
	{
		final File dir = new File(directoryName);
		final FilenameFilter filter = new FilenameFilter()
		{
			@Override
			public boolean accept(final File dir1, final String name)
			{
				return !name.startsWith(".");
			}
		};
		return dir.list(filter);
	}

	/**
	 * Deletes all files and sub-directories under dir. Returns true if all deletions were
	 * successful. If a deletion fails, the method stops attempting to delete and returns
	 * false.
	 */
	public static boolean deleteDir(final File dir)
	{
		if (dir.isDirectory())
		{
			final String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				final boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Delete local folder if exists, and create a new one.
	 * 
	 * @param localFolderPath
	 *            local folder path
	 */
	public static void createLocalFolder(final String localFolderPath)
	{
		// Delete local directory if exists
		final File localFolder = new File(localFolderPath);
		if (localFolder.exists())
		{
			deleteDir(localFolder);
		}

		// Create local directory to save files in
		final boolean success = localFolder.mkdir();
		if (success)
		{
			log.info("Local Folder " + localFolder + " created");
		}
	}

	/**
	 * Return a list of jar files in the classpath of this JVM, by their order of
	 * appearance in the classpath.
	 * 
	 * @return list of jar files in the classpath of this JVM, by their order of
	 *         appearance in the classpath
	 */
	public static List<String> getTokenizedClassPath()
	{
		StringTokenizer tokenizer = new StringTokenizer(
				SystemProperty.JAVA_CLASS_PATH.getValue(), Strings.NEW_LINE_STRING);
		final List<String> jars = newList();
		while (tokenizer.hasMoreTokens())
		{
			jars.add(tokenizer.nextToken());
		}

		tokenizer = new StringTokenizer(SystemProperty.JAVA_LIBRARY_PATH.getValue(),
				SystemProperty.JAVA_PATH_SEPARATOR.getValue());
		while (tokenizer.hasMoreTokens())
		{
			jars.add(tokenizer.nextToken());
		}

		tokenizer = new StringTokenizer(
				SystemProperty.JAVA_EXTENSION_DIRECTORIES.getValue(),
				SystemProperty.JAVA_PATH_SEPARATOR.getValue());
		while (tokenizer.hasMoreTokens())
		{
			jars.add(tokenizer.nextToken());
		}

		return jars;
	}

	/**
	 * Print a list of jar files in the classpath of this JVM, by their order of
	 * appearance in the classpath.
	 */
	public static void printTokenizedClassPath()
	{
		final List<String> jars = IoUtil.getTokenizedClassPath();
		log.debug("####################### CLASSPATH jars START #######################");
		for (final String jar : jars)
		{
			log.debug(jar);
		}
		log.debug("####################### CLASSPATH jars END   #######################");
	}

	/**
	 * @param classLoader
	 */
	public static void printClassPathUrls(final ClassLoader classLoader)
	{
		// Get the URLs
		final URL[] urls = ((URLClassLoader) classLoader).getURLs();
		log.debug("####################### CLASSPATH jars START #######################");
		for (int i = 0; i < urls.length; i++)
		{
			log.debug(urls[i].getFile());
		}
		log.debug("####################### CLASSPATH jars START #######################");
	}

	/**
	 * Return a string with the list of jar files in the classpath of this JVM, by their
	 * order of appearance in the classpath.
	 * 
	 * @return a string with the list of jar files in the classpath of this JVM, by their
	 *         order of appearance in the classpath.
	 */
	public static String toStringTokenizedClassPath()
	{
		final List<String> jars = IoUtil.getTokenizedClassPath();
		final StringBuilder s = newStringBuilder();
		s.append("####################### CLASSPATH jars START #######################")
				.append(NEW_LINE_STRING);
		for (final String jar : jars)
		{
			s.append(jar).append(NEW_LINE_STRING);
		}
		s.append("####################### CLASSPATH jars END   #######################")
				.append(NEW_LINE_STRING);
		return s.toString();
	}

	/**
	 * Print a class path within a spring context.
	 * 
	 * @param resolver
	 *            spring resource resolver
	 */
	public static void printClassPathWithinSpring(final ResourcePatternResolver resolver)
	{
		try
		{
			final Resource[] resources = resolver.getResources("classpath*:**/*");
			if (resources != null)
			{
				// Parse every resource.
				for (final Resource res : resources)
				{
					final String path = res.getURL().getPath();
					// // Path name string should not be empty.
					// if (!path.equals(""))
					// {
					// if (path.endsWith(".class"))
					// {
					// log.debug("Class: " + path);
					// }
					// else if (path.endsWith(".jar"))
					// {
					// log.debug("JAR  : " + path);
					// }
					// }
					log.error(path);
				}
			}
		}
		catch (final Exception ignore)
		{
			ignore.printStackTrace();
		}
	}

	/**
	 * Returns a string that represents the contents of a file.
	 * 
	 * @param file
	 *            the file to read
	 * @return string the contents of a file as a String
	 * @exception IOException
	 *                if the file is not found, or if there is any problem reading the
	 *                file
	 */
	public static String getFileAsString(final File file)
	{
		try
		{
			return FileCopyUtils.copyToString(new FileReader(file));
		}
		catch (final FileNotFoundException e)
		{
			throw new ApplicationException(inputOutputErrorMessage("File not found: ",
					Strings.EMPTY_STRING + file));
		}
		catch (final IOException e)
		{
			throw new ApplicationException(inputOutputErrorMessage(Strings.EMPTY_STRING,
					e.getMessage()));
		}
	}

	/**
	 * Find the full file name (path) of a classpath resource name.
	 * 
	 * @param resourceName
	 *            a classpath resource name
	 * @return the corresponding absolute file name
	 * @throws ApplicationException
	 *             on I/O error
	 */
	public static String getAbsoluteFileName(final String resourceName)
	{
		final ClassPathResource resource = new ClassPathResource(resourceName);
		try
		{
			return resource.getFile().getAbsolutePath();
		}
		catch (final IOException e)
		{
			throw new ApplicationException(inputOutputErrorMessage(
					"loading resource file" + resource, e.toString()));
		}
	}

	/**
	 * Compute the message digest hash of a file stream.
	 * 
	 * @param inputStream
	 *            input stream
	 * @param messageDigest
	 *            digest algorithm name
	 * @return MD hash
	 * @throws IOException
	 *             if input stream cannot be properly read
	 * @throws NoSuchAlgorithmException
	 *             if MD algorithm with the name <code>messageDigest</code> is not found
	 */
	public static String computeMessageDigestHash(final InputStream inputStream,
			final String messageDigest) throws IOException, NoSuchAlgorithmException
	{
		final MessageDigest md = MessageDigest.getInstance(messageDigest);
		try (final InputStream in = new BufferedInputStream(inputStream))
		{
			final byte[] buffer = new byte[64 * 1024];
			int count;
			while ((count = in.read(buffer)) > 0)
				md.update(buffer, 0, count);
		}
		final byte hash[] = md.digest();
		final StringBuilder buf = StringUtil.newStringBuilder();
		for (int i = 0; i < hash.length; i++)
		{
			final int b = hash[i] & 0xFF;
			int c = (b >> 4) & 0xF;
			c = c < 10 ? '0' + c : 'A' + c - 10;
			buf.append((char) c);
			c = b & 0xF;
			c = c < 10 ? '0' + c : 'A' + c - 10;
			buf.append((char) c);
		}
		return buf.toString();
	}

	/**
	 * Read bytes from an classpath resource input stream into a string and strip new
	 * lines and tabs from the string. Useful for XML message/web service testing.
	 * 
	 * @param resourceName
	 *            classpath input stream resource name
	 * @return stripped string
	 */
	@SuppressWarnings("resource")
	// Resource is closed in finally block in calling function
	public static String copyToStringAndStripNewLinesAndTabs(final String resourceName)
	{
		final InputStream in = getResourceAsStream(resourceName);
		if (in == null)
		{
			throw new ApplicationException("Classpath resource "
					+ StringUtil.quote(resourceName) + " not found");
		}
		final String string = copyToStringAndStripNewLinesAndTabs(in);
		return string;
	}

	/**
	 * Read bytes from an input stream into a string and strip new lines and tabs from the
	 * string. Useful for XML message/web service testing.
	 * 
	 * @param in
	 *            input stream
	 * @return stripped string
	 */
	public static String copyToStringAndStripNewLinesAndTabs(final InputStream in)
	{
		return stripNewLinesAndTabs(getInputStreamAsString(in));
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getInputStreamAsString(final InputStream in)
	{
		try
		{
			if (in == null)
			{
				throw new ApplicationException("Could not open input stream");
			}
			final String string = new String(FileCopyUtils.copyToByteArray(in));
			return string;
		}
		catch (final IOException e)
		{
			throw new ApplicationException("I/O error copying resource to string", e);
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (final IOException e)
			{
				// Ignore
			}
		}
	}

	/**
	 * @param resourceName
	 * @return
	 */
	public static String getResourceAsString(final String resourceName)
	{
		return getInputStreamAsString(getResourceAsStream(resourceName));
	}

	/**
	 * @param resource
	 * @return
	 */
	public static URL getResourceAsUrl(final Resource resource)
	{
		try
		{
			return resource.getURL();
		}
		catch (final IOException e)
		{
			throw new ApplicationException("Failed to load resource URL "
					+ StringUtil.quote(resource.toString()), e);
		}
	}

	/**
	 * Copy an input stream into a byte array output stream. The input stream is reset to
	 * its original position after copying.
	 * 
	 * @param input
	 *            input stream
	 * @return a byte array output stream copy of <code>input</code>
	 */
	public static ByteArrayOutputStream copyInputStream(final InputStream input)
	{
		try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
		{
			input.mark(Integer.MAX_VALUE);
			// int byteCount = 0;
			final byte[] buffer = new byte[FileCopyUtils.BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = input.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
				// byteCount += bytesRead;
			}
			out.flush();
			input.reset();
			return out;
		}
		catch (final IOException e)
		{
			throw new ApplicationException("Failed to copy input stream", e);
		}
	}

	/**
	 * @param inputStream
	 */
	public static void closeSilently(final InputStream inputStream)
	{
		try
		{
			inputStream.close();
		}
		catch (final IOException ignored)
		{
			// ignore
		}
	}

	/**
	 * Causes the currently executing thread to sleep (temporarily cease execution) for
	 * the specified number of milliseconds, subject to the precision and accuracy of
	 * system timers and schedulers. The thread does not lose ownership of any monitors.
	 * Catches {@link InterruptedException}s if they occur to simplify try-catch blocks.
	 * 
	 * @param millis
	 *            the length of time to sleep in milliseconds
	 * @see Thread#sleep(long)
	 */
	public static void sleep(final long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * A factory method which creates an {@link IOException} from the given exception and
	 * message
	 * 
	 * @param cause
	 *            cause of exception
	 * @return {@link IOException} instance
	 */
	public static IOException newIoException(final Throwable cause)
	{
		return newIoException(cause.getMessage(), cause);
	}

	/**
	 * A factory method which creates an {@link IOException} from the given exception and
	 * message.
	 * 
	 * @param message
	 *            message
	 * @param cause
	 *            cause of exception
	 * @return {@link IOException} instance
	 */
	public static IOException newIoException(final String message, final Throwable cause)
	{
		final IOException answer = new IOException(message);
		answer.initCause(cause);
		return answer;
	}

	/**
	 * Return the fully qualified name of a file under the Java temporary directory.
	 * 
	 * @param fileName
	 *            unqualified file name
	 * @return fully qualified name of the corresponding file under the Java temporary
	 *         directory
	 */
	public static String getTempFileNameWithName(final String fileName)
	{
		return SystemProperty.TEMP_DIRECTORY + VIRTUAL_DIRECTORY + fileName;
	}

	/**
	 * Check to make sure that the stream has not been closed
	 * 
	 * @param out
	 *            output stream
	 * @throws IOException
	 */
	public static void ensureOpen(final OutputStream out) throws IOException
	{
		if (out == null)
		{
			throw new IOException("Stream closed");
		}
	}

	/**
	 * Load a properties file from the classpath.
	 * 
	 * @param propsName
	 * @return Properties
	 */
	public static Properties loadProperties(final String propsName)
	{
		try
		{
			final Properties props = new Properties();
			final URL url = ClassLoader.getSystemResource(propsName);
			props.load(url.openStream());
			return props;
		}
		catch (final Exception e)
		{
			throw new ApplicationException("Failed to load properties from resource "
					+ propsName, e);
		}
	}

	/**
	 * Load a Properties file.
	 * 
	 * @param propsFile
	 * @return Properties
	 */
	public static Properties loadProperties(final File propsFile)
	{
		try (final FileInputStream fis = new FileInputStream(propsFile))
		{
			final Properties props = new Properties();
			props.load(fis);
			return props;
		}
		catch (final IOException e)
		{
			throw new ApplicationException("Failed to load properties from file "
					+ propsFile, e);
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
