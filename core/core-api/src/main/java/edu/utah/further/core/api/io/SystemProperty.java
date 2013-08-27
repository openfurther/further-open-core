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

import edu.utah.further.core.api.collections.Parameter;
import edu.utah.further.core.api.constant.MavenPhase;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * Centralizes useful system properties as an enumeration (FUR-751). The enum's name is
 * the system property key, and its value is its environment-specific value. Note that the
 * value will be <code>null</code> if the system property is not set.
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
 * @version Apr 28, 2010
 * @see
 */
public enum SystemProperty implements Parameter
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Path used to find directories and JAR archives containing class files. Elements of
	 * the class path are separated by a platform-specific character specified in the
	 * {@link #PATH_SEPARATOR} property.
	 */
	JAVA_CLASS_PATH("java.class.path"),

	/**
	 * Java class path component separator.
	 */
	JAVA_PATH_SEPARATOR("path.separator"),

	/**
	 * JVM's library path.
	 * 
	 * @see http://wrapper.tanukisoftware.org/doc/english/prop-java-library
	 *      -path-append-system-path.html
	 */
	JAVA_LIBRARY_PATH("java.library.path"),

	/**
	 * JVM Extension directories.
	 */
	JAVA_EXTENSION_DIRECTORIES("java.ext.dirs"),

	/**
	 * Character that separates components of a file path. This is "/" on UNIX and "\" on
	 * Windows.
	 */
	FILE_SEPARATOR("file.separator"),

	/**
	 * New line character/string.
	 */
	LINE_SEPARATOR("line.separator"),

	/**
	 * Used to set up a {@link MavenPhase} in portable tests.
	 */
	TEST_PHASE("testPhase"),

	/**
	 * Name of Java system property representing a temporary directory name.
	 */
	TEMP_DIRECTORY("java.io.tmpdir");

	// ========================= FIELDS ====================================

	/**
	 * The system property's name.
	 */
	private final String name;

	/**
	 * The system property's value. Cached upon creating this enumerated type insta.ce
	 */
	private final String value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 */
	private SystemProperty(final String name)
	{
		this.name = name;
		this.value = CoreUtil.getSystemProperty(name);
	}

	// ========================= IMPL: Object ===============================

	/**
	 * Return the value of this system property. Will print <code>"null"</code> for
	 * <code>null</code> properties.
	 * 
	 * @return
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return value;
	}

	// ========================= IMPL: Parameter ============================

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.Parameter#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.Parameter#getValue()
	 */
	@Override
	public String getValue()
	{
		return value;
	}

}
