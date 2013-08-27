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

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQSequence;

/**
 * A call-back interface that processes an XQuery result set into a custom type while the
 * XQuery connection is open.
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
 * @version May 4, 2010
 */
public interface XQueryResultProcessor<T>
{
	// ========================= METHODS ===================================

	/**
	 * Process an XQuery result set. Assumes that the XQuery connection is open.
	 * 
	 * @param results
	 *            XQuery result set
	 * @return an optional return type, e.g. a converted/formatted result set.
	 *         Implementations may return <code>null</code> if no return value is required
	 * @throws XQException
	 *             upon XQJ API call failure
	 */
	T process(XQSequence results) throws XQException;
}
