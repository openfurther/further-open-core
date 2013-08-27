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

/**
 * Parses input files into an object.
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
 * @version Sep 7, 2010
 */
public interface FileScanner<T>
{
	// ========================= METHODS ===================================

	/**
	 * @param string
	 *            full name of an existing, readable file.
	 * @param lineScanner
	 *            scans each line into an entity
	 */
	T parse(String string);

	/**
	 * Parse 
	 * 
	 * Caller is responsible for closing file.
	 * 
	 * @param aFileName
	 *            full name of an existing, readable file.
	 * @param lineScanner
	 *            scans each line into an entity
	 * @throws FileNotFoundException
	 */
	T parse(File file) throws FileNotFoundException;

	/**
	 * Parse 
	 * 
	 * Caller is responsible for closing file.
	 * 
	 * @param is
	 *            input stream
	 * @param lineScanner
	 *            scans each line into an entity
	 */
	T parse(InputStream is);

	/**
	 * Parse a spring classpath resource.
	 * 
	 * @param location
	 *            resource classpath URL
	 */
	T parseResource(String location) throws FileNotFoundException, IOException;
}