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
package edu.utah.further.core.xml.xquery;

import javax.xml.xquery.XQDataSource;

/**
 * A convenient wrapper around {@link XQDataSource} that adds flags to account for slight
 * differences among XQJ implementations.
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
 * @version Dec 17, 2008
 */
public abstract class AbstractXQueryDataSource implements XQueryDataSource
{
	// ========================= FIELDS ====================================

	/**
	 * If <code>true</code>, this XQJ implementation supports inline declarations of
	 * variables at the top of an XQuery (prolog section).
	 */
	private boolean supportsInlineVariables;

	// ========================= GETTERS & SETTERS =========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.xquery.XQueryDataSource#isSupportsInlineVariables()
	 */
	@Override
	public boolean isSupportsInlineVariables()
	{
		return supportsInlineVariables;
	}

	/**
	 * Set a new value for the supportsInlineVariables property.
	 * 
	 * @param supportsInlineVariables
	 *            the supportsInlineVariables to set
	 */
	public void setSupportsInlineVariables(final boolean supportsInlineVariables)
	{
		this.supportsInlineVariables = supportsInlineVariables;
	}
}
