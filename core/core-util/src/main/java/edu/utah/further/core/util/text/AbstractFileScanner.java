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
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

/**
 * A base class of {@link FileScanner}s. Uses a template pattern to implement the public
 * methods.
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
public abstract class AbstractFileScanner<T, L> implements FileScanner<T>
{
	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Parses individual lines.
	 */
	private LineScanner<L> lineScanner;

	/**
	 * Loads classpath resources.
	 */
	@Autowired
	private ResourceLoader resourceLoader;

	// ========================= IMPL: FileScanner =========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.util.text.FileScanner#parse(java.lang.String,
	 * edu.utah.further.core.util.text.LineScanner)
	 */
	@Override
	public T parse(final String string)
	{
		try (Scanner scanner = new Scanner(string)) {
			return parseInput(scanner);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.util.text.FileScanner#parse(java.io.File,
	 * edu.utah.further.core.util.text.LineScanner)
	 */
	@Override
	@SuppressWarnings("resource")
	// Caller closes file
	public T parse(final File file) throws FileNotFoundException
	{
		return parseInput(new Scanner(file));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.util.text.FileScanner#parse(java.io.InputStream,
	 * edu.utah.further.core.util.text.LineScanner)
	 */
	@Override
	@SuppressWarnings("resource")
	// Caller closes inputstream
	public T parse(final InputStream is)
	{
		return parseInput(new Scanner(is));
	}

	/**
	 * @param location
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @see edu.utah.hci.filegen.util.scanner.FileScanner#parseResource(java.lang.String)
	 */
	@Override
	public T parseResource(final String location) throws FileNotFoundException,
			IOException
	{
		return parse(getResourceAsFile(location));
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the lineScanner property.
	 * 
	 * @param lineScanner
	 *            the lineScanner to set
	 */
	public void setLineScanner(final LineScanner<L> lineScanner)
	{
		this.lineScanner = lineScanner;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parses an entire input from a scanner.
	 * 
	 * @param scanner
	 *            provides input
	 * @param aLineScanner
	 *            scans each line into an entity
	 * @return list of entities, one per line
	 */
	abstract protected T parseInput(Scanner scanner);

	/**
	 * Return the lineScanner property.
	 * 
	 * @return the lineScanner
	 */
	protected LineScanner<L> getLineScanner()
	{
		return lineScanner;
	}

	/**
	 * @param location
	 * @return
	 * @throws IOException
	 * @see org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
	 */
	private File getResourceAsFile(final String location) throws IOException
	{
		return resourceLoader.getResource(location).getFile();
	}
}