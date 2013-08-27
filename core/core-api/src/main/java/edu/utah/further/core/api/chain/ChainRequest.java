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
package edu.utah.further.core.api.chain;

import edu.utah.further.core.api.message.SeverityMessageContainer;

/**
 * A request object that's passed around a chain of handles in a chain-of-responsibility
 * pattern.
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
 * @version Sep 28, 2009
 */
public interface ChainRequest extends AttributeContainer, SeverityMessageContainer
{
	// ========================= CONSTANTS =================================

	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	// Request attribute name conventions
	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	/**
	 * Last exception associated with this request.
	 */
	String EXCEPTION = "exception";

	// ========================= METHODS ===================================

	/**
	 * Return the latest exception associated with this request.
	 * 
	 * @return the latest exception instance
	 */
	Throwable getException();

	/**
	 * Set the latest exception associated with this request.
	 * 
	 * @param throwable
	 *            exception class
	 */
	void setException(Throwable throwable);

	/**
	 * Return <code>true</code> if and only if there is an exception stored in this
	 * request.
	 * 
	 * @return <code>true</code> if and only if {@link #getException()} is non-
	 *         <code>null</code>
	 */
	boolean hasException();

	/**
	 * Cancel this request. If this cancel requested is issued by another thread (typical)
	 * then ensure that access to shared data is synchronized.
	 */
	void cancel();

	/**
	 * Whether or not this request is canceled. If this cancel requested is issued by
	 * another thread (typical) then ensure that access to shared data is synchronized.
	 * 
	 * @return true if this requested is canceled, false otherwise
	 */
	boolean isCanceled();
}
