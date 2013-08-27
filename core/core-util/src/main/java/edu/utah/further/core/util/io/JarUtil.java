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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.slf4j.Logger;

/**
 * JAR file utilities.
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
 * @version Dec 9, 2008
 */
public final class JarUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(JarUtil.class);

	/**
	 * Jar file entry reader buffer size.
	 */
	private static int BUFFER_SIZE = 10240;

	// ========================= METHODS ===================================

	/**
	 * @param archiveFile
	 * @param toBeJared
	 */
	public static void createJarArchive(final File archiveFile, final File[] toBeJared)
	{
		try (final FileOutputStream stream = new FileOutputStream(archiveFile);
			final JarOutputStream out = new JarOutputStream(stream, new Manifest()))
		{
			final byte buffer[] = new byte[BUFFER_SIZE];

			for (int i = 0; i < toBeJared.length; i++)
			{
				if (toBeJared[i] == null || !toBeJared[i].exists()
						|| toBeJared[i].isDirectory())
				{
					continue; // Just in case...
				}
				if (log.isDebugEnabled())
				{
					// ============================================================
					// TODO: add entry name instead of file name here!!!!
					// ============================================================
					if (log.isDebugEnabled())
					{
						log.debug("Adding " + toBeJared[i].getName());
					}
				}

				// Add archive entry

				// ============================================================
				// ============================================================
				// TODO: create a map of prefixes (e.g. edu/)-to-jarred files
				// as the parameter of this method rather than a File[]
				// ============================================================
				// ============================================================

				final JarEntry jarAdd = new JarEntry("edu/" + toBeJared[i].getName());
				jarAdd.setTime(toBeJared[i].lastModified());
				out.putNextEntry(jarAdd);

				// Write file to archive
				try (final FileInputStream in = new FileInputStream(toBeJared[i])) {
					while (true)
					{
						final int nRead = in.read(buffer, 0, buffer.length);
						if (nRead <= 0)
							break;
						out.write(buffer, 0, nRead);
					}
				}
			}

			if (log.isDebugEnabled())
			{
				log.debug("Adding completed OK");
			}

		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
	}
}
