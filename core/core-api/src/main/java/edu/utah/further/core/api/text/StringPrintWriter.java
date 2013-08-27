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
package edu.utah.further.core.api.text;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * A PrintWriter that maintains a String as its backing store.
 * </p>
 * Based on <code>commons-lang</code> so that we don't need the full dependency in
 * <code>core-api</code>.
 * <p>
 * Usage:
 * 
 * <pre>
 * StringPrintWriter out = new StringPrintWriter();
 * printTo(out);
 * println(out.getString());
 * </pre>
 * 
 * </p>
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Alex Chaffee
 * @author Scott Stanchfield
 * @author Gary D. Gregory
 * @version Jun 14, 2010
 */
final class StringPrintWriter extends PrintWriter
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a new instance.
	 */
	public StringPrintWriter()
	{
		super(new StringWriter());
	}

	/**
	 * Constructs a new instance using the specified initial string-buffer size.
	 * 
	 * @param initialSize
	 *            initial buffer size
	 */
	public StringPrintWriter(int initialSize)
	{
		super(new StringWriter(initialSize));
	}

	// ========================= METHODS ===================================

	/**
	 * <p>
	 * Since toString() returns information *about* this object, we want a separate method
	 * to extract just the contents of the internal buffer as a String.
	 * </p>
	 * 
	 * @return the contents of the internal string buffer
	 */
	public String getString()
	{
		flush();
		return ((StringWriter) this.out).toString();
	}

}
